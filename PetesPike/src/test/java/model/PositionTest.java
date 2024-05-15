package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import petespike.model.Position;

public class PositionTest {

    // POSITION NEEDS A EQUALS FUNCTION

    @Test
    public void testGetRow() {
        // setup
        Position position = new Position(1, 2);
        int expected = 1;

        // invoke
        int actual = position.getRow();

        //analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCol() {
        // setup
        Position position = new Position(1, 2);
        int expected = 2;

        // invoke
        int actual = position.getCol();

        //analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        // setup
        Position position = new Position(8, 8);
        String expected = "(8,8)";

        // invoke
        String actual = position.toString();

        //analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        // setup
        Position position1 = new Position(8, 8);
        Position position2 = new Position(8, 8);
        boolean expected = true;

        // invoke
        boolean actual = position1.equals(position2);

        //analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testNotEqual() {
        // setup
        Position position1 = new Position(1, 2);
        Position position2 = new Position(2, 1);
        boolean expected = false;

        // invoke
        boolean actual = position1.equals(position2);

        //analyze
        assertEquals(expected, actual);
    }
}
