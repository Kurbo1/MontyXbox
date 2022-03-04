package frc.robot.Components;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;

public class Intake {
    Solenoid intake;
    TalonSRX srxMyAss;

    boolean position;

    public Intake() {
        intake = new Solenoid(PneumaticsModuleType.CTREPCM, 6);
        srxMyAss = new TalonSRX(13);
        position = intake.get();
    }

    public void toggle() {
        position = !position;
        intake.set(position);
    }

    public void set(double sped) {
        srxMyAss.set(ControlMode.PercentOutput, sped);
    }

    public boolean get() {
        return intake.get();
    }
    
}