package unit11.trafficlights;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class TrafficLight implements Runnable {
    private enum LightColor {RED, GREEN, YELLOW}

    private final String direction;
    private LightColor lightColor;
    private final Lock lock = new ReentrantLock();
    private final Condition greenCondition = lock.newCondition();
    private boolean readyToSwitch = false;

    public TrafficLight(String direction) {
        this.direction = direction;
        this.lightColor = LightColor.RED;
    }

    @Override
    public void run() {
        try {
            while (true) {
                lock.lock();
                try {
                    while (!readyToSwitch) {
                        greenCondition.await(); // wait until ready to switch
                    }

                    switch (lightColor) {
                        case RED:
                            System.out.println("The " + direction + " light is RED");
                            Thread.sleep(1000); // spend at least 1 second red
                            changeLightColor(LightColor.GREEN);
                            break;
                        case GREEN:
                            System.out.println("The " + direction + " light changes from RED to GREEN");
                            Thread.sleep(5000); // spend 5 seconds green
                            changeLightColor(LightColor.YELLOW);
                            break;
                        case YELLOW:
                            System.out.println("The " + direction + " light changes from GREEN to YELLOW");
                            Thread.sleep(2000); // spend 2 seconds yellow
                            changeLightColor(LightColor.RED);
                            break;
                    }

                    readyToSwitch = false; // reset ready to switch after completing one cycle
                } finally {
                    lock.unlock();
                }

                // let other thread (the other light) react to the light change
                Thread.sleep(10000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void changeLightColor(LightColor newColor) {
        lightColor = newColor;
    }

    public void waitForGreenLight() throws InterruptedException {
        lock.lock();
        try {
            readyToSwitch = true;
            greenCondition.signal(); // signal that this light is ready to switch
        } finally {
            lock.unlock();
        }
    }
}
