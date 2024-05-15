package petespike.view;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

import petespike.model.Direction;
import petespike.model.GameState;
import petespike.model.Move;
import petespike.model.PetesPike;
import petespike.model.PetesPikeException;
import petespike.model.PetesPikeSolver;
import petespike.model.Position;

public class PetesPikeCLI {
/*
    In part one of this project, you and your team created a command-line interface that can play the PetesPike game. Add or update the following enhancement to your CLI:
    hint - replace the call to getPossibleMoves with a call to the configuration solve method created in the previous activity to get a next move that would lead to winning the game. If there is not a move that will lead to a winning game, display an appropriate message.
    solve - use the configuration solve method to get a list of winning moves, and automatically play the game to the end. Display each move as it is made and the board after the move is made. If no solution is found, display an appropriate message.
    help - update the help message to include the new solve command. 
*/

    /*prints Board Correctly */
    public static void printBoard(TreeSet<Position> board, PetesPike game) throws PetesPikeException{
        int row = game.getRows();
        int col = game.getCols();
        
        //System.out.println(row + " " + cols);

        int currentRow = -1;
        int currentCol = 0;

        System.out.println();
        System.out.print("  ");
        for (int i = 0; i < col; i++) {
            System.out.print(i + " ");
        }
        //System.out.println();
        for (Position pos : board) {
        
            if (currentCol % col == 0) {
                System.out.println("");
                currentRow++;
                System.out.print(currentRow + " ");
            }

            System.out.print(game.getSymbolAt(pos) + " ");
            currentCol += 1;
        }
        System.out.println();

    }

    /*Prints All possible commands */
    public static void getHelp(){
        System.out.println("Commands: \r\n" + // 
                        "help - displays a list of commands.\r\n" + //
                        "board - displays the current board.\r\n" + //
                        "reset - resets the current puzzle to the initial board configuration and move count to 0.\r\n" + //
                        "new <puzzle_filename> - starts a new puzzle.\r\n" + //
                        "move <row> <col> <direction> - move the piece at <row>, <col> in the <direction>. \r\n" + //
                        "hint - displays a valid move given the current board configuration.\r\n" + //
                        "Solve - Completes the game for the user showing each move. \r\n" + //
                        "quit - quits the game.");
    }

    /*Hint Function
     * 
     * Works by Turning Enum DIRECTIONS into an array and then looping through it to check positions
    */
    // public static void oldHint(PetesPike game) throws PetesPikeException{
    //         Position petePos = game.getPete();
    //         Direction[] DirArray = Direction.values();
    //         for(Direction direction: DirArray){
    //             Move tempMove = new Move(petePos, direction);
    //             if(game.checkMove(tempMove) == true){
    //                 System.out.println(tempMove.getPosition() + " " + direction);
    //             }
    //         }
            
    //         for(int i = 0; i < game.goatSize(); i++){
    //             char tempgoat = (char)( i + '0');
    //             for(Direction direction: DirArray){
    //                 Move tempMove = new Move(game.getgoats(tempgoat), direction);
    //                 if(game.checkMove(tempMove) == true){
    //                     System.out.println(tempMove.getPosition() + " " + direction);
    //                 }
    //             }
    //         }
    // }

    public static void Hint(PetesPike game) throws PetesPikeException{
        // call the solving algorith to make a list of winning move
        // print first move in list
        // if list is empty print proper message


        PetesPikeSolver solver = new PetesPikeSolver(game);
        // solver.solve(game);
        // PetesPikeSolver.solve(game);
        List<Move> moves = solver.getSolutionMoves();
        if(!moves.isEmpty()){
            System.out.println(moves.getFirst());
        }
        System.out.println("There are no valid moves.");
    }

    public static void Solve(PetesPike game) throws PetesPikeException{
        // call to solve game and set to a list variable
        // get next move off list and increment 
        // call make move
        // print board
        // print move
        // if list is empty or isvalid equals false print appriopriate message

        
        PetesPikeSolver solver = new PetesPikeSolver(game);
        solver.solve(game);
        List<Move> moves = solver.getSolutionMoves();
        if(moves.isEmpty()){
            System.out.println("There are no valid moves. Game invalid.");
        }
        while(!moves.isEmpty()){     
            Move nextMove = moves.getFirst();
            moves.remove(0);
            game.makeMove(nextMove);
            System.out.println(game.getBoard());
        }
    }

    /*Takes in a position and a directions and calls PetePike to make a move then updates the board if move is valid */
    public static void makeMove(int row, int col, char direct, PetesPike game){
        System.out.println(direct);
        Direction direction;
        if(direct == 'u'){
            direction = Direction.UP;
        }
        else if (direct == 'd') {
            direction = Direction.DOWN;
        }
        else if(direct == 'l'){
            direction = Direction.LEFT;
        }
        else if(direct == 'r'){
            direction = Direction.RIGHT;
        }
        else{
            System.err.println("Not a valid direction");
            direction = null;
        }
        try {
            game.makeMove(new Move(new Position(row, col), direction));
        } catch (PetesPikeException e) {
            System.out.println("Move Invalid");
        }
    }

    /*If you need a comment for this leave coding */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter FileName: ");
        String file = scan.nextLine();
        PetesPike game;
        // add a recall to make them try again
        try {
            game = new PetesPike(file);
        getHelp();
        while (true) {
            System.out.println("HALLO");
            printBoard(game.getBoard(), game);
            System.out.println("Moves Count:" + game.getMoveCount());
            System.out.println("Enter a command:");
            String temp = scan.nextLine();

            if(temp.equals("help")){
                getHelp();
            }

            else if(temp.equals("board")){
                printBoard(game.getBoard(),game);
            }

            else if(temp.equals("reset")){
                game = new PetesPike(file);
                System.out.println("Board has been reset");
            }

            else if(temp.equals("hint")){
                Hint(game);
            }

            else if(temp.substring(0,4).equals("move")){
                makeMove(Integer.parseInt(temp.substring(5,6)),Integer.parseInt(temp.substring(7,8)) , temp.charAt(9), game);
            }

            else if(temp.substring(0,3).equals("new")){
                file = temp.substring(4);
                game = new PetesPike(file);
                System.out.println("New game created");
            }

            else if(temp.equals("quit")){
                System.out.println("Quitting, Thanks for playing!");
                break;
            }
            else if(temp.equals("solve")){
                Solve(game);
            }

            if (game.getGameState().equals(GameState.WON)) {
                System.out.println("Congrats You have Won!");
                break;
            }

            if(game.getGameState().equals(GameState.NO_MOVES)){
                System.out.println("There Are no more moves.");
                break;
            }
        }
        }
        catch (PetesPikeException e) {
            System.out.println("PetesPikeException");
        } catch (IOException e) {
            System.out.println("File Not Found Enter a different File:");
        }
        scan.close();
    }
}