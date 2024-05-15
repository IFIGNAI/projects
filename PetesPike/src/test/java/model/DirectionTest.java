package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import petespike.model.Direction;

public class DirectionTest {
    
    @Test
    public void printUp() {
        // setup
        Direction up = Direction.UP;
        String expected = "UP";

        // invoke
        String actual = up.toString();
        
        // analyze
        assertEquals(expected, actual);
    }

    @Test
    public void printDown() {
        // setup
        Direction down = Direction.DOWN;
        String expected = "DOWN";

        // invoke
        String actual = down.toString();
        
        // analyze
        assertEquals(expected, actual);
    }

    @Test
    public void printLeft() {
        // setup
        Direction left = Direction.LEFT;
        String expected = "LEFT";

        // invoke
        String actual = left.toString();
        
        // analyze
        assertEquals(expected, actual);
    }

    @Test
    public void printRight() {
        // setup
        Direction right = Direction.RIGHT;
        String expected = "RIGHT";

        // invoke
        String actual = right.toString();
        
        // analyze
        assertEquals(expected, actual);
    }
}
