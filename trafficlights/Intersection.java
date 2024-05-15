package unit11.trafficlights;

class Intersection {
    private final TrafficLight northSouthLight;
    private final TrafficLight eastWestLight;

    public Intersection() {
        northSouthLight = new TrafficLight("North/South");
        eastWestLight = new TrafficLight("East/West");
        new Thread(northSouthLight).start();
        new Thread(eastWestLight).start();
    }
    //method that check from what direction vehicle should go
    public void checkAndPass(Vehicle vehicle) throws InterruptedException {
        if (vehicle.getDirection().equals("North") || vehicle.getDirection().equals("South")) {
            synchronized (northSouthLight) {
                northSouthLight.waitForGreenLight();
            }
        } else {
            synchronized (eastWestLight) {
                eastWestLight.waitForGreenLight();
            }
        }
        System.out.println("  Vehicle (" + vehicle.getId() + ") headed " + vehicle.getDirection() + " drives through the intersection.");
    }
}