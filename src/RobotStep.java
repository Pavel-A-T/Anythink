import java.util.ArrayList;

public class RobotStep {
    private volatile static int id = 0;
    ArrayList<Leg> legs = new ArrayList<>(); //Количество ног

    public void walk() throws InterruptedException {

        synchronized (legs.get(0)) {

            legs.get(0).notify();

        }
    }

    public class Leg extends Thread {
        private int id;

        public Leg(int id) throws InterruptedException {
            this.id = id;
        }


        public void run() {
            try {
                while (true) {
                    synchronized (this) {
                        this.wait();
                    }

                    System.out.println(id + "\t\t" + Thread.currentThread().getName());
                    Thread.sleep(300);

                    if (id < legs.size() - 1) {

                        Leg nextLeg = legs.get(id + 1);

                        synchronized (nextLeg) {
                            nextLeg.notify();
                        }
                    } else {
                        Leg nextLeg = legs.get(0);
                        synchronized (nextLeg) {
                            nextLeg.notify();
                        }
                    }
                }
            } catch (InterruptedException ignored) {
            }
        }

        @Override
        public String toString() {
            return "This is thread " + id;
        }

    }


    public static void main(String[] args) throws InterruptedException {

        RobotStep robot = new RobotStep();


        for (int i = 0; i < 51; i++) {

            Leg leg = robot.new Leg(i);
            robot.legs.add(leg);
            leg.start();

        }

        robot.walk();

    }
}
