package frc.robot.Components;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


public class Drivetrain {
    MotorType brushless = MotorType.kBrushless;

    public CANSparkMax rightMain;
    CANSparkMax rightFollow;
    public CANSparkMax leftMain;
    CANSparkMax leftFollow;

    IdleMode mode;
    boolean brake;

    public Drivetrain() {
        mode = IdleMode.kBrake;
        brake = true;

        rightMain = new CANSparkMax(4, MotorType.kBrushless);
        rightFollow = new CANSparkMax(2, MotorType.kBrushless);
        leftMain = new CANSparkMax(1, MotorType.kBrushless);
        leftFollow = new CANSparkMax(3, MotorType.kBrushless);

        rightFollow.follow(rightMain);
        leftFollow.follow(leftMain);

        rightMain.setInverted(false);
        leftMain.setInverted(true);

        rightMain.setIdleMode(mode);
        leftMain.setIdleMode(mode);

        rightMain.getEncoder().setPosition(0);
        leftMain.getEncoder().setPosition(0);
    }

    //public static CANSparkMax a;

    public void driveId(double forward, double turn, int id) {
        CANSparkMax a = new CANSparkMax(id, MotorType.kBrushless);
        a.set(forward);
        a.close();
        a = null;
        System.gc();
    }

    public void drive(double forward, double turn) {
        leftMain.set(forward + turn);
        rightMain.set(forward - turn);
    }

    public double[] getTemps() {
        return new double[]{leftMain.getMotorTemperature(), leftFollow.getMotorTemperature(), rightMain.getMotorTemperature(), rightFollow.getMotorTemperature()};
    }

    public void toggleMode() {
        brake = !brake;
        if (brake) {
            setMode(IdleMode.kBrake);
        } else {
            setMode(IdleMode.kCoast);
        }
    }
    public boolean getBrake() {
        return brake;
    }

    public void setMode(IdleMode i) {
        leftMain.setIdleMode(i);
        rightMain.setIdleMode(i);
    }

}
