package unit11.trafficlights;

public class Vehicle implements Runnable {
    private static int idCounter = 1;

    private final int id;
    private final String direction;
    private final Intersection intersection;

    public Vehicle(String direction, Intersection intersection) {
        this.id = idCounter++;
        this.direction = direction;
        this.intersection = intersection;
    }

    @Override
    public void run() {
        try {
            System.out.println("Vehicle (" + id + ") headed " + direction + " arrives at the intersection.");
            intersection.checkAndPass(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getDirection(){
        return direction;
    }

    public int getId(){
        return id;
    }
}