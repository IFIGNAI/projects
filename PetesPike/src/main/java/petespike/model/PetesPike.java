package petespike.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class PetesPike {
    private static final int ROW_INDEX = 0;
    private static final int COL_INDEX = 1;

    private static final char MOUNTAINTOP_SYMBOL = 'T';
    public static final char EMPTY_SYMBOL = '-';
    public static final char PETE_SYMBOL = 'P';
    public static String info;
    public static final Set<Character> GOAT_SYMBOLS = new HashSet<Character>() {{
        add('0');
        add('1');
        add('2');
        add('3');
        add('4');
        add('5');
        add('6');
        add('7');
        add('8');
    }};

    private int moveCount;
    private List<PetesPikeObserver> petesPikeObservers = new ArrayList<>();
    private GameState gameState;
    private Set<Position> board;
    private int row;
    private int col;
    public Position mountainTop;

    // extra variables not stated in UML diagram
    private Map<Position, Character> goatPositions;
    /*used in hint function to get goat positions */
    private Map<Character, Position> goatHintMap;
    private Position pete;

    /*Obsevers*/
    public void RegisterObserver(PetesPikeObserver observer){
        petesPikeObservers.add(observer);
    }

    public void removeObserver(PetesPikeObserver observer){
        petesPikeObservers.remove(observer);
    }

    private void notifyObserver(Position from, Position to){
        for (PetesPikeObserver observer : petesPikeObservers) {
            observer.PieceMoved(from, to);
        }
    }
    
    /*returns pete position */
    public Position getPete(){
        if (pete == null){
            gameState = GameState.WON;
            Position pos = new Position(0, 0);
            return pos;

        }
        return pete;
    }

    /*returns goats position */
    public Position getgoats(Character goat){
        return goatHintMap.get(goat);
    }

    /*returns size of Goat Array for Hint Function */
    public int goatSize(){
        return goatHintMap.size();
    }
    
    /*initializes variable and reads file to create the board */
    public PetesPike(String filename) throws PetesPikeException, IOException {
        this.gameState = GameState.NEW;
        this.moveCount = 0;
        // best constructor lol]]
        
        File file = new File(filename);
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);
        /* work of roosevelt labranche
        Here are some things to note given this file;
        
        the first line is are the rows and the columns of the board

        4 8 <- Row, Column
        T----41-
        --0-----
        ---3-P--
        --2-----
        
        */
        String line = reader.readLine(); // rows && columns

        // set up rows & columns
        String[] firstLine = line.split(" ");
        this.row = Integer.parseInt(firstLine[ROW_INDEX]);
        this.col = Integer.parseInt(firstLine[COL_INDEX]);

        // create the board && goat positions 
        this.board = new HashSet<Position>();
        this.goatPositions = new HashMap<Position, Character>();
        this.goatHintMap = new HashMap<Character, Position>();

        // extremely botched setup :)
        int currentRow = 0;
        while (true) {
            line = reader.readLine();
            if (line == null) {
                break;
            }

            // now we parse everything now that we have the rows and columns;
            String[] splitLine = line.split("");
            for (int i = 0; i < this.col; i++) {
                // will finish adding extras later.
                char character = line.charAt(i);

                Position position = new Position(currentRow, i);
                board.add(position);

                if (character == MOUNTAINTOP_SYMBOL) {
                    this.mountainTop = position;
                } else if (character == PETE_SYMBOL) {
                    this.pete = position;
                } else if (GOAT_SYMBOLS.contains(character)) {
                    this.goatPositions.put(position, character);
                    this.goatHintMap.put(character, position);
                }
            }
            currentRow++;
        }

        // TESTING PURPOSES
        // for (Position pos : board) {
        //     System.out.println(pos);
        // }
    }

    public PetesPike(PetesPike other) {
        this.gameState = other.gameState;
        this.board = new HashSet<>(other.board);
        this.goatPositions = new HashMap<>(other.goatPositions);
        this.goatHintMap = new HashMap<>(other.goatHintMap);
        this.moveCount = other.moveCount;
        this.row = other.row;
        this.col = other.col;
        this.mountainTop = other.mountainTop;
        this.pete = other.pete;
    }

    public int getMoveCount(){
        return moveCount;
    }

    public int getRows(){
        return this.row;
    }

    public int getCols(){
        return this.col;
    }

    public GameState getGameState(){
        return gameState;
    }

    /*
    Takes in a move and checks if there is a Symbol at the positin. If yes checks to make sure move is valid.
    Updated to return if a move is invalid if it does not update the position.
    */
    public void makeMove(Move move) throws PetesPikeException {
    gameState = GameState.IN_PROGRESS;
    Position oldPos = move.getPosition();
    if (getSymbolAt(move.getPosition()) != EMPTY_SYMBOL && getSymbolAt(move.getPosition()) != MOUNTAINTOP_SYMBOL) {
        Position from = new Position(move.getPosition().getRow(), move.getPosition().getCol());
        if (move.getDirection() == Direction.UP) {
            int check = moveCount;
            for (int i = move.getPosition().getRow(); i > 0; i--) {
                Position pos_cur = new Position(i, move.getPosition().getCol());
                Position pos_next = new Position(i - 1, move.getPosition().getCol());
                if (getSymbolAt(pos_next) != EMPTY_SYMBOL && getSymbolAt(pos_next) != MOUNTAINTOP_SYMBOL) {
                    if (getSymbolAt(move.getPosition()) == PETE_SYMBOL) {
                        if (!oldPos.equals(pos_cur)) {
                            pete = pos_cur;
                            moveCount++;
                            hasWon();
                            info = "Great Move!";
                            notifyObserver(from, pos_cur);
                        }
                    } else if (goatPositions.containsKey(move.getPosition())) {
                        if (!oldPos.equals(pos_cur)) {
                            char goat = goatPositions.get(move.getPosition()).charValue();
                            goatPositions.remove(move.getPosition());
                            goatPositions.put(pos_cur, goat);
                            goatHintMap.put(goat, pos_cur);
                            moveCount++;
                            hasWon();
                            info = "Great Move!";
                            notifyObserver(from, pos_cur);
                        }
                    }
                }
            }
            if (check == moveCount) {
                info = "There is no piece in that direction to stop you";
                if (oldPos.equals(move.getPosition())) {
                    info = "Invalid move, your position stays the same.";
                    System.out.println("Making this move doesnt change your position. ");
                } else {
                    System.out.println("There is no piece in that direction to stop you");
                }
            }
        } else if (move.getDirection() == Direction.DOWN) {
            int check = moveCount;
            for (int i = move.getPosition().getRow(); i < row; i++) {
                Position pos_cur = new Position(i, move.getPosition().getCol());
                Position pos_next = new Position(i + 1, move.getPosition().getCol());
                if (getSymbolAt(pos_next) != EMPTY_SYMBOL && getSymbolAt(pos_next) != MOUNTAINTOP_SYMBOL) {
                    if (getSymbolAt(move.getPosition()) == PETE_SYMBOL) {
                        if (!oldPos.equals(pos_cur)) {
                            pete = pos_cur;
                            moveCount++;
                            hasWon();
                            info = "Great Move!";
                            notifyObserver(from, pos_cur);
                        }
                    } else if (goatPositions.containsKey(move.getPosition())) {
                        if (!oldPos.equals(pos_cur)) {
                            char goat = goatPositions.get(move.getPosition()).charValue();
                            goatPositions.remove(move.getPosition());
                            goatPositions.put(pos_cur, goat);
                            goatHintMap.put(goat, pos_cur);
                            moveCount++;
                            hasWon();
                            info = "Great Move!";
                            notifyObserver(from, pos_cur);
                        }
                    }
                }
            }
            if (check == moveCount) {
                info = "There is no piece in that direction to stop you";
                if (oldPos.equals(move.getPosition())) {
                    info = "Invalid move, your position stays the same.";
                    System.out.println("Making this move doesnt change your position. ");
                } else {
                    System.out.println("There is no piece in that direction to stop you");
                }
            }
        } else if (move.getDirection() == Direction.LEFT) {
            int check = moveCount;
            for (int i = move.getPosition().getCol(); i > 0; i--) {
                Position pos_cur = new Position(move.getPosition().getRow(), i);
                Position pos_next = new Position(move.getPosition().getRow(), i - 1);
                if (getSymbolAt(pos_next) != EMPTY_SYMBOL && getSymbolAt(pos_next) != MOUNTAINTOP_SYMBOL) {
                    if (getSymbolAt(move.getPosition()) == PETE_SYMBOL) {
                        if (!oldPos.equals(pos_cur)) {
                            pete = pos_cur;
                            moveCount++;
                            hasWon();
                            info = "Great Move!";
                            notifyObserver(from, pos_cur);
                        }
                    } else if (goatPositions.containsKey(move.getPosition())) {
                        if (!oldPos.equals(pos_cur)) {
                            char goat = goatPositions.get(move.getPosition()).charValue();
                            goatPositions.remove(move.getPosition());
                            goatPositions.put(pos_cur, goat);
                            goatHintMap.put(goat, pos_cur);
                            moveCount++;
                            hasWon();
                            info = "Great Move!";
                            notifyObserver(from, pos_cur);
                        }
                    }
                }
            }
            if (check == moveCount) {
                info = "There is no piece in that direction to stop you";
                if (oldPos.equals(move.getPosition())) {
                    info = "Invalid move, your position stays the same.";
                    System.out.println("Making this move doesnt change your position. ");
                } else {
                    System.out.println("There is no piece in that direction to stop you");
                }
            }
        } else {
            int check = moveCount;
            for (int i = move.getPosition().getCol(); i < col; i++) {
                Position pos_cur = new Position(move.getPosition().getRow(), i);
                Position pos_next = new Position(move.getPosition().getRow(), i + 1);
                if (getSymbolAt(pos_next) != EMPTY_SYMBOL && getSymbolAt(pos_next) != MOUNTAINTOP_SYMBOL) {
                    if (getSymbolAt(move.getPosition()) == PETE_SYMBOL) {
                        if (!oldPos.equals(pos_cur)) {
                            pete = pos_cur;
                            moveCount++;
                            hasWon();
                            info = "Great Move!";
                            notifyObserver(from, pos_cur);
                        }
                    } else if (goatPositions.containsKey(move.getPosition())) {
                        if (!oldPos.equals(pos_cur)) {
                            char goat = goatPositions.get(move.getPosition()).charValue();
                            goatPositions.remove(move.getPosition());
                            goatPositions.put(pos_cur, goat);
                            goatHintMap.put(goat, pos_cur);
                            moveCount++;
                            hasWon();
                            info = "Great Move!";
                            notifyObserver(from, pos_cur);
                        }
                    }
                }
            }
            if (check == moveCount) {
                info = "There is no piece in that direction to stop you";
                if (oldPos.equals(move.getPosition())) {
                    info = "Invalid move, your position stays the same.";
                    System.out.println("Making this move doesnt change your position. ");
                } else {
                    System.out.println("There is no piece in that direction to stop you");
                }
            }
        }
    } else {
        info = "There is no piece in that direction to stop you";
        //System.out.println("There is no piece in that position");
    }
    //System.out.println("Test");
    hasWon();
}


    public void hasWon() throws PetesPikeException{
        if(getPete().equals(this.mountainTop)){
            gameState = GameState.WON;
            info = "Pete Has Won";
            System.out.println("Pete Has Won");
        }
        //System.out.println(mountainTop);
        //System.out.println(getPete());
    }

    // public void makeMove(Move move) throws PetesPikeException{
    //     Direction[] DirArray = Direction.values();
    //     gameState = GameState.IN_PROGRESS;
    //     if (getSymbolAt(move.getPosition()) != EMPTY_SYMBOL & getSymbolAt(move.getPosition()) != MOUNTAINTOP_SYMBOL){
    //         Position from = new Position(move.getPosition().getRow(), move.getPosition().getCol());
    //     for(Direction direction : DirArray){
    //         if(move.getDirection() == direction){
    //             int check = moveCount;
    //             for (int i = move.getPosition().getRow(); i > 0; i--){
    //                 Position pos_cur = new Position(i, move.getPosition().getCol());
    //                 Position pos_next = new Position(i - 1, move.getPosition().getCol());
    //                 if (getSymbolAt(pos_next) != EMPTY_SYMBOL & getSymbolAt(move.getPosition()) != MOUNTAINTOP_SYMBOL){
    //                     if (getSymbolAt(move.getPosition()) == PETE_SYMBOL){
    //                         pete = pos_cur;
    //                         moveCount++;
    //                         notifyObserver(from, pos_cur);
    //                         break;
    //                     }
    //                     else if (getSymbolAt(move.getPosition()) == goatPositions.get(move.getPosition()).charValue()){
    //                         char goat =  goatPositions.get(move.getPosition()).charValue();
    //                         goatPositions.remove(move.getPosition());
    //                         goatPositions.put(pos_cur, goat);
    //                         goatHintMap.put(goat, pos_cur);
    //                         moveCount++;
    //                         notifyObserver(from, pos_cur);
    //                         break;
    //                     }
    //                 }
    //             }
    //             if (check == moveCount){
    //                 System.out.println("There is no piece in that direction to stop you");
    //             }
    //             }
    //         }
    //     }
    //     System.out.println("There is no piece in that position");
    // }

    private boolean doReturn(Position pos_next, Move move) throws PetesPikeException { // dont repeat yourself lol
        if (getSymbolAt(pos_next) != EMPTY_SYMBOL & getSymbolAt(move.getPosition()) != MOUNTAINTOP_SYMBOL){
            if (getSymbolAt(move.getPosition()) == PETE_SYMBOL){
                return true;
            }
            else if (getSymbolAt(move.getPosition()) == goatPositions.get(move.getPosition()).charValue()){
                return true;
            }
        }
        return false;
    }

    public boolean checkMove(Move move) throws PetesPikeException {
        Direction[] DirArray = Direction.values();
        gameState = GameState.IN_PROGRESS;
        
        if (getSymbolAt(move.getPosition()) != EMPTY_SYMBOL && getSymbolAt(move.getPosition()) != MOUNTAINTOP_SYMBOL) {
            for (Direction direction : DirArray) {
                if (move.getDirection() == direction) {
                    int check = moveCount;
                    int rowDelta = 0;
                    int colDelta = 0;
                    switch(direction) {
                        case UP:
                            rowDelta = -1;
                            break;
                        case DOWN:
                            rowDelta = 1;
                            break;
                        case LEFT:
                            colDelta = -1;
                            break;
                        case RIGHT:
                            colDelta = 1;
                            break;
                    }
                    
                    for (int i = 0; i < row; i++) {
                        int newRow = move.getPosition().getRow() + rowDelta * (i + 1);
                        int newCol = move.getPosition().getCol() + colDelta * (i + 1);
                        Position pos_next = new Position(newRow, newCol);
                        hasWon();
                        if (newRow >= 0 && newRow < row && newCol >= 0 && newCol < col) {
                            if (getSymbolAt(pos_next) != EMPTY_SYMBOL && getSymbolAt(pos_next) != MOUNTAINTOP_SYMBOL) {
                                if (getSymbolAt(pos_next) == PETE_SYMBOL) {
                                    return true;
                                } else if (getSymbolAt(pos_next) == goatPositions.get(pos_next).charValue()) {
                                    return true;
                                }
                                //System.out.println(pos_next);
                                // && !pos_next.equals(move.getPosition()
                            }
                        } else {
                            break;
                        }
                    }
                    
                    if (check == moveCount) {
                        return false;
                    }
                }
            }
        }
        return false;
    }
    
    

    // /*Checks to Make sure move is valid */
    // public boolean checkMove(Move move) throws PetesPikeException{
    //     gameState = GameState.IN_PROGRESS;
    //     if (getSymbolAt(move.getPosition()) != EMPTY_SYMBOL & getSymbolAt(move.getPosition()) != MOUNTAINTOP_SYMBOL){
    //         if(move.getDirection() == Direction.UP){
    //             int check = moveCount; // so aparently to check if a move can be done, move count is appended.
    //             for (int i = move.getPosition().getRow(); i > 0; i--){
    //                 Position pos_cur = new Position(i, move.getPosition().getCol());
    //                 Position pos_next = new Position(i - 1, move.getPosition().getCol());
                    
    //                 if (doReturn(pos_next, move)) {return true;}
    //             }
    //             if (check == moveCount){
    //                 return false;
    //             }
    //         }
    //         else if(move.getDirection() == Direction.DOWN){
    //             int check = moveCount;
    //             for (int i = move.getPosition().getRow(); i < row; i++){
    //                 Position pos_cur = new Position(i, move.getPosition().getCol());
    //                 Position pos_next = new Position(i + 1, move.getPosition().getCol());
    //                 if (doReturn(pos_next, move)) {return true;}
    //             }
    //             if (check != moveCount){
    //                 return false;
    //             }
    //         }
    //         else if(move.getDirection() == Direction.LEFT){
    //             int check = moveCount;
    //             for (int i = move.getPosition().getCol(); i > 0; i--){
    //                 Position pos_cur = new Position(move.getPosition().getRow(), i);
    //                 Position pos_next = new Position(move.getPosition().getRow(), i - 1);
    //                 if (doReturn(pos_next, move)) {return true;}
    //             }
    //             if (check == moveCount){
    //                 return false;
    //             }
    //         }
    //         else {
    //             int check = moveCount;
    //             for (int i = move.getPosition().getCol(); i < col; i++){
    //                 Position pos_cur = new Position(move.getPosition().getRow(), i);
    //                 Position pos_next = new Position(move.getPosition().getRow(), i + 1);
    //                 if (doReturn(pos_next, move)) {return true;}
    //             }
    //             if (check == moveCount){
    //                 return false;
    //             }
    //         }
    //     }
    //     else {
    //         return false;
    //     }
    //     return false;
    // }
        
    public Map<Position, Character> getGoatPositions() {
        return goatPositions;
    }

    public Position getMountainTop() {
        return this.mountainTop;
    }

    public TreeSet<Position> getBoard() {
        TreeSet<Position> set = new TreeSet<>();
        set.addAll(board);

        return set;
    }

    /*gets symbol at a position if not there throw an error */
    public char getSymbolAt(Position position) throws PetesPikeException {
        int tRow = position.getRow();
        int tCol = position.getCol();

        if ((tRow < 0 || tRow > this.row) || (tCol < 0 || tCol > this.col) ) {
            throw new PetesPikeException("Invalid location.");
        }

        if (pete.equals(position)) {
            return PETE_SYMBOL;
        } else if (mountainTop.equals(position)) {
            return MOUNTAINTOP_SYMBOL;
        } else if (goatPositions.get(position) != null) {
            return goatPositions.get(position).charValue();
        }
        return EMPTY_SYMBOL;
    }

    public static void main(String[] args) throws PetesPikeException, IOException {
        // TESTING PURPOSES
        PetesPike petesPike = new PetesPike("data/petes_pike_4_8_5_no_solution.txt");
        
        //System.out.println("MOUNTAIN TOP: " + petesPike.getMountainTop());
        //petesPike.getBoardString();
        // petesPike.getSymbolAt(new Position(-2, -2));
    }

    public void PieceMoved(Position from, Position to){
        System.out.println("Piece from position " + from + " has successfully moved to " + to);
    }
   
}