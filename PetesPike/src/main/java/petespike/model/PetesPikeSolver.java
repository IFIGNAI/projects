package petespike.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import backtracker.Backtracker;
import backtracker.Configuration;

public class PetesPikeSolver implements Configuration<PetesPikeSolver> {
    private PetesPike petesPike;
    private List<Move> solutionMoves;

    public PetesPikeSolver(PetesPike petesPike) {
        this.petesPike = petesPike;
        this.solutionMoves = new ArrayList<>();
    }

    public List<Move> getSolutionMoves() {
        return solutionMoves;
    }

    @Override
    public Collection<PetesPikeSolver> getSuccessors() {
        List<PetesPikeSolver> successors = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            for (Position position : petesPike.getBoard()) {
                Move move = new Move(position, direction);
                try {
                    PetesPike newGame = new PetesPike(petesPike);
                    newGame.makeMove(move);
                    PetesPikeSolver successor = new PetesPikeSolver(newGame);
                    successors.add(successor);
                } catch (PetesPikeException e) {
                    e.printStackTrace();
                }
            }
        }
        return successors;
    }

    @Override
    public boolean isValid() {
        try{
            Position petePos = petesPike.getPete();
            Direction[] DirArray = Direction.values();
            for(Direction direction: DirArray){
                Move tempMove = new Move(petePos, direction);
                if(petesPike.checkMove(tempMove) == true){
                    return true;
                }
            }
    
            for(int i = 0; i < petesPike.goatSize(); i++){
                char tempgoat = (char)( i + '0');
                for(Direction direction: DirArray){
                    Move tempMove = new Move(petesPike.getgoats(tempgoat), direction);
                    if(petesPike.checkMove(tempMove) == true){
                        return true;
                    }
                }
            }
    
            }catch(Exception ie){}
            return false;
    }

    @Override
    public boolean isGoal() {
        return petesPike.getGameState() == GameState.WON;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Moves right now:\n");
        for (Move move : solutionMoves) {
            sb.append(move).append("\n");
        }
        sb.append("Current configuration:\n");
        return sb.toString();
    }

    public static PetesPikeSolver solve(PetesPike petesPike) {
        PetesPikeSolver initialConfig = new PetesPikeSolver(petesPike);
        Backtracker<PetesPikeSolver> backtracker = new Backtracker<>(false);
        return backtracker.solve(initialConfig);
    }
}