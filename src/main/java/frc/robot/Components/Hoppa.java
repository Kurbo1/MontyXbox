package frc.robot.Components;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Hoppa {
    TalonSRX hopper1;
    TalonSRX hopper2;
    TalonSRX indexer;

    public Hoppa() {
        hopper1 = new TalonSRX(7);
        hopper2 = new TalonSRX(8);
        indexer = new TalonSRX(9);
    }

    public void run() {
        hopper1.set(ControlMode.PercentOutput, -.5);
        hopper2.set(ControlMode.PercentOutput, .5);
        indexer.set(ControlMode.PercentOutput, .5);
    }

    public void stop() {
        hopper1.set(ControlMode.PercentOutput, 0);
        hopper2.set(ControlMode.PercentOutput, 0);
        indexer.set(ControlMode.PercentOutput, 0);
    }

    public void unjam() {
        hopper1.set(ControlMode.PercentOutput, .5);
        hopper2.set(ControlMode.PercentOutput, -.5);
        indexer.set(ControlMode.PercentOutput, -.5);
    }
}