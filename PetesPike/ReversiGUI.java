package unit04.reversi.view;

import unit04.reversi.model.Reversi;
import unit04.reversi.model.ReversiException;
import unit04.reversi.model.Square;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public class ReversiGUI extends Application {
    public static final Image SQUARE_IMAGE = 
        new Image("file:media/images/reversi/square.png"); // test
    public static final Image BLACK_IMAGE = 
        new Image("file:media/images/reversi/blackpiece.png");
        public static final Image WHITE_IMAGE = 
        new Image("file:media/images/reversi/whitepiece.png");
        public static final Image BLANK_IMAGE = 
        new Image("file:media/images/reversi/blank.png");

    private static final Font HELVETICA_18 = new Font("Helvetica", 18);

    private Reversi board;
    private Label status;
    private Label player1score;
    private Label player2score;

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane main = new BorderPane();


        status = makeLabel("Let's play Reversi!");
        status.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(status, Priority.ALWAYS);

        HBox bottom = new HBox();
        bottom.setPadding(new Insets(5));
        bottom.getChildren().addAll(
            status,
            new Button("Start Over")
        );
        main.setBottom(bottom);

        VBox players = new VBox();
        players.setBackground(new Background(
            new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, 
            Insets.EMPTY)));

        player1score = makeLabel("Score: 5");
        VBox player1 = makePlayerBox("Player 1", BLACK_IMAGE, player1score);
        player2score = makeLabel("Score: 2");
        VBox player2 = makePlayerBox("Player 2", WHITE_IMAGE, player2score);
        players.getChildren().addAll(
            makeFillerLabel(),
            player1,
            player2,
            makeFillerLabel()
        );
        player2.setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THICK)));
        main.setLeft(players);

        GridPane boardPane = new GridPane();
        main.setCenter(boardPane);

        board = new Reversi();

        for(int col=0; col<8; col++) {
            for(int row=0; row<8; row++) {
                Button button = makeButton(col, row);
                boardPane.add(button, col, row);
            }
        }

        board.start();

        stage.setTitle("Reversi!");
        stage.setScene(new Scene(main));
        stage.show();

        // makeMove(5, 3);
    }

    void makeMove(int row, int col) {
        try {
            board.move(row, col);
            status.setText("Good move!");
        } catch (ReversiException re) {
            status.setText(re.getMessage());
        }
    }

    private VBox makePlayerBox(String name, Image piece, Label score) {
        VBox player = new VBox();
        player.setPadding(new Insets(20));
        // VBox.setVgrow(player, Priority.SOMETIMES);

        Label nameLabel = makeLabel(name);
        VBox.setVgrow(nameLabel, Priority.NEVER);

        ImageView pieceView = new ImageView(piece);
        VBox.setVgrow(pieceView, Priority.NEVER);

        player.getChildren().addAll(
            nameLabel,
            pieceView,
            score
        );

        return player;
    }

    private static Label makeLabel(String text) {
        Label label = new Label(text);
        label.setFont(HELVETICA_18);
        label.setAlignment(Pos.CENTER);
        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return label;
    }

    private static Label makeFillerLabel() {
        Label filler = new Label(" ");
        VBox.setVgrow(filler, Priority.ALWAYS);
        HBox.setHgrow(filler, Priority.ALWAYS);
        filler.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return filler;
    }

    private Button makeButton(int col, int row) {
        ImageView squareView = new ImageView(BLANK_IMAGE);
        Button button = new Button("", squareView);
        button.setBackground(new Background(new BackgroundImage(SQUARE_IMAGE, 
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
            BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        button.setPadding(new Insets(0));

        button.setOnAction(new MoveMaker(row, col, this));

        Square square = board.getSquare(row, col);
        square.setOnChange(new SquareChanger(squareView));

        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}