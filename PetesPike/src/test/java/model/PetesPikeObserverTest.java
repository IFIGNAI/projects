package model;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import petespike.model.Direction;
import petespike.model.Move;
import petespike.model.PetesPike;
import petespike.model.PetesPikeException;
import petespike.model.PetesPikeObserver;
import petespike.model.Position;

public class PetesPikeObserverTest{

    @Test
    public void notifyTest() throws IOException , PetesPikeException{
        //setup
        PetesPike petesPike = new PetesPike("data/petes_pike_4_8_5_no_solution.txt");
        Position pos = new Position(2, 3);
        Move move = new Move(pos, Direction.RIGHT);
        PetesPikeObserver observer = new PetesPikeObserver() {
            public boolean check(){
                return check;
            }
            boolean check = false;
            public void PieceMoved(Position from, Position to){
                check = true;
            }
        };
        //invoke
        petesPike.RegisterObserver(observer);
        petesPike.makeMove(move);
        //analyze
        assertEquals(observer.check(), true);

    }
}
