package petespike.model;

public class Move {
    private Position position;
    private Direction direction;

    public Move(Position position, Direction direction){
        this.position = position;
        this.direction = direction;
    }

    public Position getPosition(){
        return position;
    }

    public Direction getDirection(){
        return direction;
    }
}
