package petespike.model;

public interface PetesPikeObserver {
    public boolean check();
    public void PieceMoved(Position from, Position to);
}