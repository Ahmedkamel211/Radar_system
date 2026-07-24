# Traffic Radar Fine System

A Java console application that simulates a traffic radar system. It observes cars passing through a checkpoint, evaluates them against a configurable set of rules (speed limits per car type, seatbelt usage), and generates itemized fines for any violations detected.

## Overview

The system is built around a simple, extensible rule-based architecture:

- Each car observation is checked against a list of `Rule` implementations.
- Any rule that detects a violation produces a `Violation` with a description and fee.
- All violations for a car are collected into a `Fine`, which is printed and stored for later summary reporting.

This design makes it straightforward to add new rule types without modifying existing logic.

## Project Structure

| Class | Responsibility |
|---|---|
| `CarObservation` | Represents a single observed car: plate number, car type, speed, and seatbelt status. |
| `Rule` (interface) | Defines the contract for a violation check: `Violation check(CarObservation obs)`. |
| `Violation` | Represents a single rule violation with a description and fee (in EGP). |
| `SpeedRule` | Checks whether a car of a given type exceeds its maximum allowed speed. |
| `SeatbeltRule` | Checks whether the driver's seatbelt is fastened. |
| `Fine` | Aggregates all violations for a car and prints a formatted fine report. |
| `Radar` | Holds the active rules, observes cars, generates fines, and prints a summary of all fines issued. |
| `Main` | Entry point: configures rules, feeds sample observations, and runs the simulation. |

## How It Works

1. **Configure rules** — `Radar` is set up with one or more `Rule` implementations (e.g., speed limits for "Truck" and "Private" car types, plus a universal seatbelt check).
2. **Observe a car** — `radar.observe(obs)` runs the `CarObservation` through every registered rule.
3. **Collect violations** — Any rule returning a non-null `Violation` is added to that car's `Fine`.
4. **Report** — If a car has at least one violation, its fine is printed immediately and stored.
5. **Summarize** — After all observations, `radar.printAllFines()` prints a summary of total fees per car.

## Requirements

- Java Development Kit (JDK) 8 or later

## Usage

Compile and run from the project directory:

```bash
javac Main.java
java Main
```

### Sample Output

```
Traffic fine for car ABC1234
Total amount: 400 EGP
Violations:
- speed of 94 exceeded max allowed 80 : 300 EGP
- Seatbelt not fastened : 100 EGP

All fines summary:
ABC1234 -> 400 EGP
```

*(Only cars with at least one violation appear in the output. In the sample data, the truck `XYZ5678` complies with all rules and is not fined.)*

## Extending the System

To add a new type of violation:

1. Create a class that implements `Rule`.
2. Implement `check(CarObservation obs)` to return a `Violation` (with a description and fee) when the condition is met, or `null` otherwise.
3. Register it with `radar.addRule(new YourNewRule(...))`.

No changes are required to `Radar`, `Fine`, or `Main` beyond registering the new rule.

## Currency

All fees are denominated in Egyptian Pounds (EGP).

## License

This project is provided as-is for educational purposes.
