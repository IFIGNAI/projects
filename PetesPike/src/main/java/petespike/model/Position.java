package petespike.model;

public class Position implements Comparable<Position> {
    private int row;
    private int col;
    
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position other = (Position)obj;
            return this.row == other.row && this.col == other.col && this.hashCode() == other.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.row * this.col;
    }

    // Will you need to determine if two different Position objects represent the same position on the board?
    // Will your Position class need to work with hashing data structures like HashSet or HashMap?
    // Will you want to print a nicely formatted version of the class?

    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }

    public static void main(String[] args) {
        Position testPos = new Position(1, 1);
        System.out.println(testPos);
        System.out.println(testPos.getCol() + " " + testPos.getRow());
    }

    @Override
    public int compareTo(Position o) {
        if (o.row != this.row) {
            return Integer.compare(this.row, o.row);
            // return this.row - o.row;
        } else {
            return Integer.compare(this.col, o.col);
        }
    }
}
