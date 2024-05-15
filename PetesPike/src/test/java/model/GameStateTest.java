package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import petespike.model.GameState;

public class GameStateTest {
    
    @Test
    public void printNew() {
          // setup
        GameState state = GameState.NEW;
        String expected = "NEW";

        // invoke
        String actual = state.toString();
        
        // analyze
        assertEquals(expected, actual);
    }

    @Test
    public void printInProgress() {
          // setup
        GameState state = GameState.IN_PROGRESS;
        String expected = "IN_PROGRESS";

        // invoke
        String actual = state.toString();
        
        // analyze
        assertEquals(expected, actual);
    }

    @Test
    public void printNoMoves() {
          // setup
        GameState state = GameState.NO_MOVES;
        String expected = "NO_MOVES";

        // invoke
        String actual = state.toString();
        
        // analyze
        assertEquals(expected, actual);
    }

    @Test
    public void printWon() {
          // setup
        GameState state = GameState.WON;
        String expected = "WON";

        // invoke
        String actual = state.toString();
        
        // analyze
        assertEquals(expected, actual);
    }
}
