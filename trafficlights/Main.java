package unit11.trafficlights;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {
        Intersection intersection = new Intersection();

        for (int i = 0; i < 100; i++) {
            String[] directions = {"North", "South", "East", "West"};
            String randomDirection = directions[ThreadLocalRandom.current().nextInt(0, directions.length)];//creating vehicles from random positions
            Vehicle vehicle = new Vehicle(randomDirection, intersection);
            new Thread(vehicle).start();//run vehicle

            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(0, 2000));//random sleep time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
