import java.util.ArrayList;
import java.util.List;

class CarObservation {
    String plateNumber;
    String carType; 
    int speed;
    boolean seatbeltFastened;

    CarObservation(String plateNumber, String carType, int speed, boolean seatbeltFastened) {
        this.plateNumber = plateNumber;
        this.carType = carType;
        this.speed = speed;
        this.seatbeltFastened = seatbeltFastened;
    }
}

interface Rule {
    Violation check(CarObservation obs);
}

class Violation {
    String description;
    int fee;

    Violation(String description, int fee) {
        this.description = description;
        this.fee = fee;
    }
}

class SpeedRule implements Rule {
    int maxSpeed;
    String carType;

    SpeedRule(String carType, int maxSpeed) {
        this.carType = carType;
        this.maxSpeed = maxSpeed;
    }

    public Violation check(CarObservation obs) {
        if (obs.carType.equals(carType) && obs.speed > maxSpeed) {
            return new Violation("speed of " + obs.speed + " exceeded max allowed " + maxSpeed, 300);
        }
        return null;
    }
}

class SeatbeltRule implements Rule {
    public Violation check(CarObservation obs) {
        if (!obs.seatbeltFastened) {
            return new Violation("Seatbelt not fastened", 100);
        }
        return null;
    }
}

class Fine {
    CarObservation obs;
    List<Violation> violations = new ArrayList<>();

    Fine(CarObservation obs) {
        this.obs = obs;
    }

    void addViolation(Violation v) {
        violations.add(v);
    }

    void printFine() {
        if (violations.isEmpty()) return;
        int total = 0;
        System.out.println("Traffic fine for car " + obs.plateNumber);
        for (Violation v : violations) {
            total += v.fee;
        }
        System.out.println("Total amount: " + total + " EGP");
        System.out.println("Violations:");
        for (Violation v : violations) {
            System.out.println("- " + v.description + " : " + v.fee + " EGP");
        }
    }
}

class Radar {
    List<Rule> rules = new ArrayList<>();
    List<Fine> fines = new ArrayList<>();

    void addRule(Rule rule) {
        rules.add(rule);
    }

    void observe(CarObservation obs) {
        Fine fine = new Fine(obs);
        for (Rule r : rules) {
            Violation v = r.check(obs);
            if (v != null) fine.addViolation(v);
        }
        if (!fine.violations.isEmpty()) {
            fines.add(fine);
            fine.printFine();
        }
    }

    void printAllFines() {
        for (Fine f : fines) {
            int total = 0;
            for (Violation v : f.violations) total += v.fee;
            System.out.println(f.obs.plateNumber + " -> " + total + " EGP");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Radar radar = new Radar();
        radar.addRule(new SpeedRule("Truck", 60));
        radar.addRule(new SpeedRule("Private", 80));
        radar.addRule(new SeatbeltRule());

        CarObservation obs1 = new CarObservation("ABC1234", "Private", 94, false);
        CarObservation obs2 = new CarObservation("XYZ5678", "Truck", 55, true);

        radar.observe(obs1);
        radar.observe(obs2);

        System.out.println("\nAll fines summary:");
        radar.printAllFines();
    }
}
