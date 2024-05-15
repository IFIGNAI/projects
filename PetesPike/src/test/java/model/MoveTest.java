package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import petespike.model.Direction;
import petespike.model.Move;
import petespike.model.Position;

public class MoveTest {
    
    @Test
    public void testMove() {
        // setup
        Position pos = new Position(0, 0);
        Direction dir = Direction.DOWN;
        Move move = new Move(pos, dir);

        String expectedPos = "(0,0)";
        String expectedDir = "DOWN";

        // invoke
        String actualPos = move.getPosition().toString();
        String actualDir = move.getDirection().toString();

        // analyze

        assertEquals(expectedPos, actualPos);
        assertEquals(expectedDir, actualDir);
    }
}
