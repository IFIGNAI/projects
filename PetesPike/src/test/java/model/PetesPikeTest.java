package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import petespike.model.*;

public class PetesPikeTest {
    public static final String filename = "data/petes_pike_5_7_4_0.txt";
    /*
    5 7 <- Row, Column
    -----2-
    -0-----
    -----P-
    --T----
    -3--1--
    */

    @Test
    public void testGetMountainTop() throws PetesPikeException, IOException {
        // setup
        PetesPike petesPike = new PetesPike(filename);
        Position expected = new Position(3, 2);

        // invoke
        Position actual = petesPike.getMountainTop();

        // analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSymbol() throws PetesPikeException, IOException {
        // setup
        PetesPike petesPike = new PetesPike(filename);
        Position pos = new Position(4, 1);
        char expected = '3';
 
        // invoke
        char actual = petesPike.getSymbolAt(pos);
 
        // analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSymbolsFail() throws PetesPikeException, IOException {
        // setup
        PetesPike petesPike = new PetesPike(filename);
        Position pos = new Position(5, 7);
        PetesPikeException expected = new PetesPikeException("Invalid location.");
 
        // invoke & analyze
        try {
            char actual = petesPike.getSymbolAt(pos);
        } catch (PetesPikeException e) {
            assertEquals(expected.toString(), e.toString());
        }
    }

    @Test
    public void testGetRow() throws PetesPikeException, IOException {
        // setup
        PetesPike petesPike = new PetesPike(filename);
        int expected = 4;
 
        // invoke
        int actual = petesPike.getRows();
 
        // analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCol() throws PetesPikeException, IOException {
        // setup
        PetesPike petesPike = new PetesPike(filename);
        int expected = 6;
 
        // invoke
        int actual = petesPike.getCols();
 
        // analyze
        assertEquals(expected, actual);
    }
}
