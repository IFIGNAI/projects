package petespike.view;

import java.io.IOException;
import java.util.Set;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import petespike.model.Direction;
import petespike.model.Move;
import petespike.model.PetesPike;
import petespike.model.PetesPikeException;
import petespike.model.PetesPikeObserver;
import petespike.model.Position;


/*
    JavaFX Tips:
    You can set the background color of the GridPane itself, resulting in all column & row spaces having the given background.
    You can remove a control from a GridPane by calling
    gridPane.getChildren().remove(<control object>)
    You can enable or disable a button by calling
    button.setDisable(<true|false>)

*/
public class PetesPikeGUI extends Application {
    private static final int INSET_SIZE = 30;
    private static final String TITLE = "Pete's Pike";
    private static final String INVALID_FILE = "INVALID FILE NAME!";
    private PetesPike game;
    private static GridPane Arrows = new GridPane();
    private static Button Hint;
    private static GridPane pane = new GridPane();
    private String filename;
    private int CountMoves;
    public static boolean Arrow_enable = false;
    private static Button selectedPiece;
    public static Position move = new Position(-1, -1);
    /**
    * Helper method for creating buttons.
    * @return Button
    */
    private static Button makeButton() {
        Button button = new Button();
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return button;
    }

    /**
    * Helper method for creating labels.
    * @return Label
    */
    private static Label makeLabel() {
        Label label = new Label();
        return label;
    }

    public static void hasWon(PetesPike game){
        if (game.getPete().equals(game.mountainTop)){
            pane.setDisable(true);
            Arrows.setDisable(true);
            Hint.setDisable(true);
        }
    }

    public static GridPane Hint(PetesPike game) throws PetesPikeException {
        GridPane Hint_pane = new GridPane();
        int rows_hint = 0;

        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getCols(); col++) {
                Position currentPos = new Position(row, col);
                char symbol = game.getSymbolAt(currentPos);
    
                if (symbol != '-' && symbol != 'T') {
                    for (Direction direction : Direction.values()) {
                        Move tempMove = new Move(currentPos, direction);
                        if (game.checkMove(tempMove)) {
                            String filename = makeFilename(currentPos, game);
                            String Arrowname = ArrowFilename(direction);
                            Image image_obj = new Image(filename);
                            Image image_dir = new Image(Arrowname);
                            ImageView View_obj = new ImageView(image_obj);
                            ImageView View_dir = new ImageView(image_dir);
                            View_obj.setFitWidth(32);
                            View_obj.setFitHeight(32);
                            View_dir.setFitWidth(32);
                            View_dir.setFitHeight(32);
                            Hint_pane.add(View_obj, 0, rows_hint);
                            Hint_pane.add(View_dir, 1, rows_hint);
                            rows_hint++;
                        }
                    }
                }
            }
        }
        return Hint_pane;
    }
    
    

    public static String ArrowFilename(Direction direction){
        if (direction == Direction.UP){
            return "file:images/up.png";
        }
        else if (direction == Direction.DOWN){
            return "file:images/down.png";
        }
        else if (direction == Direction.LEFT){
            return "file:images/left.png";
        }
        else {
            return "file:images/right.png";
        }
    }

    public static String makeFilename(Position pos, PetesPike game) throws PetesPikeException{
        String directory = "file:images/";
            if (game.getSymbolAt(pos) != '-') {
                for (int i = 0; i <=8; i++){
                    if (game.getSymbolAt(pos) == (char) (i + '0')){
                        String file = "goat_" + String.valueOf(i) + ".png";
                        directory += file;
                    }
                }
                if (game.getSymbolAt(pos) == 'P') {
                    directory += "pete.png";
                } else if (game.getSymbolAt(pos) == 'T') {
                    directory += "mountain_top.png";
                }
            } else {
                directory += "blank.png";
            }
        return directory;
    }

    /**
    * Helper method for creating the puzzle
    * @return GridPane
     * @throws PetesPikeException 
    */
    private static GridPane createPuzzle(PetesPike game) throws PetesPikeException {
        GridPane pane = new GridPane();
        pane.setHgap(5);
        pane.setVgap(5);

        // refactored to use the board instead
        Set<Position> board = game.getBoard();
        for (Position pos : board) {
            Button piece = makeButton();
            PetesPikeObserver observer = new PetesPikeObserver() {

                @Override
                public boolean check() {
                    return true;
                }

                @Override
                public void PieceMoved(Position from, Position to) {
                    System.out.println("Move from " + from + " to " + to + " has been completed!");
                }
            };

            game.RegisterObserver(observer);

            piece.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (selectedPiece != null) {
                        selectedPiece.setBackground(new Background(new BackgroundFill(Color.WHITE, null, Insets.EMPTY)));
                    } // if a piece exists, uncolor it
                    move = new Position(pos.getRow(), pos.getCol());
                    Arrows.setDisable(false);
                    piece.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, Insets.EMPTY))); // color the piece to indicate its selected
                    selectedPiece = piece; 
                }
            });

            String directory = "file:images/"; // get the directory of the image based on the symbol
            if (game.getSymbolAt(pos) != '-') {
                for (int i = 0; i <=8; i++){
                    if (game.getSymbolAt(pos) == (char) (i + '0')){
                        String file = "goat_" + String.valueOf(i) + ".png";
                        directory += file;
                    }
                }
                if (game.getSymbolAt(pos) == 'P') {
                    directory += "pete.png";
                } else if (game.getSymbolAt(pos) == 'T') {
                    directory += "mountain_top.png";
                }
            } else {
                directory += "blank.png";
            }

            Image image = new Image(directory);
            ImageView view = new ImageView(image);
            view.setPreserveRatio(true);
            view.setFitHeight(64);
            view.setFitWidth(64); // size of the image is 64x6x

            piece.setPrefSize(INSET_SIZE, INSET_SIZE);
            piece.setGraphic(view); // assign image to button
            
            piece.setBackground(new Background(new BackgroundFill(Color.WHITE, null, Insets.EMPTY)));
            //piece.setPrefSize(INSET_SIZE, INSET_SIZE);
            //piece.setText("(" + pos.getRow() + "," + pos.getCol() + ")" + extra);
            //piece.setTextAlignment(TextAlignment.CENTER);
            //piece.setPadding(new Insets(INSET_SIZE));
            pane.add(piece, pos.getCol(), pos.getRow());
        }

        return pane;
    }

    /**
    * Creates a placeholder puzzle for scaling purposes
    * @return GridPane
    */
    private static Label placeHolder() {
        Label piece = makeLabel();
        piece.setPrefSize(500, 500);
        return piece;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(TITLE);
        // Setup HBoxes, & BorderPanes & placeholder GridPane
        BorderPane bPane = new BorderPane();
        HBox hBox = new HBox(); // create an HBox for the reset button, text input, and new puzzle

        Label gridPlaceHolder = placeHolder();
        bPane.setCenter(gridPlaceHolder);

        // Do logic for the first row of UI elements:
        
        // create the reset button
        Button reset = new Button();
        reset.setText("Reset");
        reset.setPadding(new Insets(5, 10, 5, 10));
        
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (filename != null) {
                    // copy and pasted from create.SetOnAction
                    try {
                        game = new PetesPike(filename);
                        GridPane pane = createPuzzle(game);
                        pane.setMaxHeight(Double.POSITIVE_INFINITY);
                        pane.setMaxWidth(Double.POSITIVE_INFINITY);

                        bPane.setCenter(pane); // Add it to the BorderPane
                    } catch (PetesPikeException e) {
                    } catch (IOException e) {}
                }
            }
        });

        // create the text input
        TextField input = new TextField();
        input.setPadding(new Insets(5, 0, 5, 5));
        input.setPromptText("Enter a filename:");

        // create the "new puzzle" button
        Button create = new Button();
        create.setText("New Puzzle");
        create.setPadding(new Insets(5, 10, 5, 10));

        create.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try { // Filename is valid
                    filename = input.getText();
                    game = new PetesPike(filename);

                    pane = createPuzzle(game);
                    pane.setMaxHeight(Double.POSITIVE_INFINITY);
                    pane.setMaxWidth(Double.POSITIVE_INFINITY);

                    bPane.setCenter(pane); // Add it to the BorderPane
                
                } catch (PetesPikeException e) { // When would this ever fire?
                    input.setText(e.toString());

                } catch (IOException e) { // FileNotFoundException
                    input.setText("");
                    input.setPromptText(INVALID_FILE);
                }   
            }
        });
        CountMoves = 0;
        Text CountMovesText = new Text("Moves: " + Integer.toString(CountMoves));
        //creates arrows hint images and MoveCount
        VBox RightSide = new VBox();
        //i would do normal hint button with advices but its 4am and i need to work on the move and moveCount functions - George Zenov 
        Hint = new Button("Get Hint");
        final GridPane Hint_pane[] = new GridPane[1];
        final boolean[] Hint_check = {false};
        Hint.setOnAction(event -> {
            try{
                RightSide.getChildren().clear();
                if (!Hint_check[0]){
                    Hint_pane[0] = Hint(game);
                    RightSide.getChildren().addAll(Arrows, Hint, Hint_pane[0], CountMovesText);
                    Hint_check[0] = true;
                }
                else {
                    Hint_pane[0].getChildren().clear();
                    RightSide.getChildren().addAll(Arrows, Hint, Hint_pane[0], CountMovesText);
                    Hint_check[0] = false;
                }
            }
            catch(PetesPikeException e){
                e.printStackTrace();
            }
        });
        // adding new button "Solve"
        Button solve = new Button("Solve");
        solve.setOnAction(event ->{
            //some activity
        });
        RightSide.getChildren().addAll(Arrows,solve, Hint, CountMovesText);


        bPane.setRight(RightSide);

        //create arrows (looks horrible but works!)
        Text info = new Text("");
        bPane.setBottom(info);
        Button upButton = new Button();
        upButton.setMinSize(64, 64);
        Image imageU = new Image("file:images/up.png");
        ImageView viewU = new ImageView(imageU);
        viewU.setFitWidth(32);
        viewU.setFitHeight(32);
        upButton.setGraphic(viewU);
        upButton.setOnAction(event -> {
            try {
                if (move.getRow() != -1 && move.getCol() != -1) {
                    Move move_c = new Move(move, Direction.UP);
                    game.makeMove(move_c);
                    pane.getChildren().clear(); 
                    pane = createPuzzle(game);
                    bPane.setCenter(pane);
                    info.setText(game.info);
                    bPane.setBottom(info);
                    CountMoves = game.getMoveCount();
                    CountMovesText.setText("Moves: " + Integer.toString(CountMoves));
                    bPane.setRight(RightSide);
                    hasWon(game);
                    move = new Position(-1, -1);
                    Arrows.setDisable(true);
                }
            } catch (PetesPikeException e) {
                e.printStackTrace();
            }
            
        });
        Button downButton = new Button();
        downButton.setMinSize(64, 64);
        Image imageD = new Image("file:images/down.png");
        ImageView viewD = new ImageView(imageD);
        viewD.setFitWidth(32);
        viewD.setFitHeight(32);
        downButton.setGraphic(viewD);
        downButton.setOnAction(event -> {
            try {
                if (move.getRow() != -1 && move.getCol() != -1) {
                    Move move_c = new Move(move, Direction.DOWN);
                    game.makeMove(move_c);
                    pane.getChildren().clear(); 
                    pane = createPuzzle(game);
                    bPane.setCenter(pane);
                    info.setText(game.info);
                    bPane.setBottom(info);
                    CountMoves = game.getMoveCount();
                    CountMovesText.setText("Moves: " + Integer.toString(CountMoves));
                    bPane.setRight(RightSide);
                    hasWon(game);
                    move = new Position(-1, -1);
                    Arrows.setDisable(true);
                }
            } catch (PetesPikeException e) {
                e.printStackTrace();
            }
        });
        Button leftButton = new Button();
        leftButton.setMinSize(64, 64);
        Image imagel = new Image("file:images/left.png");
        ImageView viewl = new ImageView(imagel);
        viewl.setFitWidth(32);
        viewl.setFitHeight(32);
        leftButton.setGraphic(viewl);
        leftButton.setOnAction(event -> {
            try {
                if (move.getRow() != -1 && move.getCol() != -1) {
                    Move move_c = new Move(move, Direction.LEFT);
                    game.makeMove(move_c);
                    pane.getChildren().clear(); 
                    pane = createPuzzle(game);
                    bPane.setCenter(pane);
                    info.setText(game.info);
                    bPane.setBottom(info);
                    CountMoves = game.getMoveCount();
                    CountMovesText.setText("Moves: " + Integer.toString(CountMoves));
                    bPane.setRight(RightSide);
                    hasWon(game);
                    move = new Position(-1, -1);
                    Arrows.setDisable(true);
                }
            } catch (PetesPikeException e) {
                e.printStackTrace();
            }
        });
        Button rightButton = new Button();
        rightButton.setMinSize(64, 64);
        Image imageR = new Image("file:images/right.png");
        ImageView viewR = new ImageView(imageR);
        viewR.setFitWidth(32);
        viewR.setFitHeight(32);
        rightButton.setGraphic(viewR);
        rightButton.setOnAction(event -> {
            try {
                if (move.getRow() != -1 && move.getCol() != -1) {
                    Move move_c = new Move(move, Direction.RIGHT);
                    game.makeMove(move_c);
                    pane.getChildren().clear(); 
                    pane = createPuzzle(game);
                    bPane.setCenter(pane);
                    info.setText(game.info);
                    bPane.setBottom(info);
                    CountMoves = game.getMoveCount();
                    CountMovesText.setText("Moves: " + Integer.toString(CountMoves));
                    bPane.setRight(RightSide);
                    hasWon(game);
                    move = new Position(-1, -1);
                    Arrows.setDisable(true);
                }
            } catch (PetesPikeException e) {
                e.printStackTrace();
            }
        });

        Arrows.setPrefSize(120, 120);

        Arrows.add(upButton, 1, 0);
        Arrows.add(downButton, 1, 2);
        Arrows.add(leftButton, 0, 1);
        Arrows.add(rightButton, 2, 1);
        Arrows.setDisable(true);


        

        // Finished logic for the first row of UI elements.
        
        // Add children to the HBox
        hBox.getChildren().addAll(reset, input, create);
        HBox.setHgrow(input, Priority.ALWAYS); // Make the textInput scale with the X size of the window;

        // Add children to the BorderPane
        bPane.setTop(hBox);

        // create scene
        Scene scene = new Scene(bPane);
        
        // show it all
        stage.setScene(scene);
        stage.show();
    }
    
    /*
     note to self: ALWAYS DO launch(args)
     */
    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
