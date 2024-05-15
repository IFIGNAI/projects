package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import javafx.geometry.Pos;
import petespike.model.Direction;
import petespike.model.Move;
import petespike.model.PetesPike;
import petespike.model.PetesPikeException;
import petespike.model.Position;

public class DeepCopyTest {
    public static final String filename = "data/petes_pike_5_7_4_0.txt";
    @Test
    public void DeepCopyStartTest() throws PetesPikeException, IOException {
        // setup
        PetesPike game = new PetesPike(filename);
        PetesPike deepCopy = new PetesPike(game);

        // invoke
        game.makeMove(new Move(new Position(2, 5), Direction.UP));

        Position pete = game.getPete();
        Position otherPete = deepCopy.getPete();

        boolean expected = false;
        boolean actual = pete.equals(otherPete);

        // analyze
        assertEquals(expected, actual);
    }

    @Test
    public void DeepCopyBeforeCompleteTest() throws PetesPikeException, IOException {
        // setup
        PetesPike game = new PetesPike(filename);

        // invoke
        game.makeMove(new Move(new Position(2, 5), Direction.UP));
        PetesPike deepCopy = new PetesPike(game);
        game.makeMove(new Move(new Position(1, 5), Direction.LEFT));
    
        Position pete = game.getPete();
        Position otherPete = deepCopy.getPete();

        boolean expected = false;
        boolean actual = pete.equals(otherPete);

        // analyze
        assertEquals(expected, actual);
    }

    @Test
    public void DeepCopy2Moves() throws PetesPikeException, IOException {
        // setup
        PetesPike game = new PetesPike(filename);

        // invoke
        game.makeMove(new Move(new Position(2, 5), Direction.UP));
        PetesPike deepCopy = new PetesPike(game);
        game.makeMove(new Move(new Position(1, 5), Direction.LEFT));
    
        int moveCount = game.getMoveCount();
        int moveCountOther = deepCopy.getMoveCount();

        boolean expected = false;
        boolean actual = moveCount == moveCountOther;

        // analyze
        assertEquals(expected, actual);
    }
}
