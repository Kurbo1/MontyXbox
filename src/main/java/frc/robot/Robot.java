/*
░░███╗░░░█████╗░░█████╗░░█████╗░  ██████╗░██╗░░░░░███████╗░██╗░░░░░░░██╗  ░█████╗░██╗░░██╗███████╗███████╗
░████║░░██╔══██╗██╔═══╝░██╔══██╗  ██╔══██╗██║░░░░░██╔════╝░██║░░██╗░░██║  ██╔══██╗██║░░██║██╔════╝╚════██║
██╔██║░░██║░░██║██████╗░╚█████╔╝  ██████╦╝██║░░░░░█████╗░░░╚██╗████╗██╔╝  ██║░░╚═╝███████║█████╗░░░░███╔═╝
╚═╝██║░░██║░░██║██╔══██╗██╔══██╗  ██╔══██╗██║░░░░░██╔══╝░░░░████╔═████║░  ██║░░██╗██╔══██║██╔══╝░░██╔══╝░░
███████╗╚█████╔╝╚█████╔╝╚█████╔╝  ██████╦╝███████╗███████╗░░╚██╔╝░╚██╔╝░  ╚█████╔╝██║░░██║███████╗███████╗
╚══════╝░╚════╝░░╚════╝░░╚════╝░  ╚═════╝░╚══════╝╚══════╝░░░╚═╝░░░╚═╝░░  ░╚════╝░╚═╝░░╚═╝╚══════╝╚══════╝

█▄─██─▄█▄─█▀▀▀█─▄█▄─██─▄███▄─▀█▀─▄█▄─█─▄███▄─▀█▄─▄█▄─██─▄█─▄─▄─█░▄▄░▄█
██─██─███─█─█─█─███─██─█████─█▄█─███▄─▄█████─█▄▀─███─██─████─████▀▄█▀█
▀▀▄▄▄▄▀▀▀▄▄▄▀▄▄▄▀▀▀▄▄▄▄▀▀▀▀▄▄▄▀▄▄▄▀▀▄▄▄▀▀▀▀▄▄▄▀▀▄▄▀▀▄▄▄▄▀▀▀▄▄▄▀▀▄▄▄▄▄▀


*/
package frc.robot;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Components.Drivetrain;
import frc.robot.Components.Hoppa;
import frc.robot.Components.Intake;
import frc.robot.Controls.Primary;
import frc.robot.Controls.Secondary;

public class Robot extends TimedRobot {

  public Drivetrain drive;
  public Controls c;
  Compressor comp;
  Solenoid intake;
  SendableChooser<Primary> PrimaryController;
  SendableChooser<Secondary> SecondaryController;
  Limelight l;
  Intake i;
  Hoppa h;
  PIDController pid;

  @Override
  public void robotInit() {
    l = new Limelight();
    h = new Hoppa();
    i = new Intake();
    comp = new Compressor(0, PneumaticsModuleType.CTREPCM);
    c = new Controls();
    drive = new Drivetrain();

    PrimaryController = new SendableChooser<>();
    SecondaryController = new SendableChooser<>();

    for (Primary a : Primary.values()) {
      PrimaryController.addOption("Primary - " + a.name(), a);
    }
    for (Secondary s : Secondary.values()) {
      SecondaryController.addOption("Secondary - " + s.name(), s);
    }

    PrimaryController.setDefaultOption("Primary - " + Primary.values()[0], Primary.values()[0]);
    SecondaryController.setDefaultOption("Secondary - " + Secondary.values()[0], Secondary.values()[0]);


    pid = new PIDController(0, 0, 0);
  }

  @Override
  public void robotPeriodic() {
    // Driver mode stuff
    SmartDashboard.putData("Primary", PrimaryController);
    SmartDashboard.putData("Secondary", SecondaryController);
    c.setPrimary(PrimaryController.getSelected());
    c.setSecondary(SecondaryController.getSelected());
    SmartDashboard.putString("Current Driver: ", PrimaryController.getSelected().name());
    SmartDashboard.putString("Current Secondary: ", SecondaryController.getSelected().name());

    // Brake Mode
    SmartDashboard.putBoolean("Brake Mode: ", drive.getBrake());

    // Motor Temps
    SmartDashboard.putNumber("Left Front", drive.getTemps()[0]);
    SmartDashboard.putNumber("Left Back", drive.getTemps()[1]);
    SmartDashboard.putNumber("Right Front", drive.getTemps()[2]);
    SmartDashboard.putNumber("Right Back", drive.getTemps()[3]);
    SmartDashboard.putNumber("mootar tomp avg",
        (drive.getTemps()[0] + drive.getTemps()[1] + drive.getTemps()[2] + drive.getTemps()[3]) / 4.0);

    // Stop driving wall nutz
    SmartDashboard.putBoolean("STOP DRIVING IF RED YOU WALNUT",
        !((drive.getTemps()[0] + drive.getTemps()[2]) / 2.0 >= 78));

    // Intake
    SmartDashboard.putBoolean("Intake Pos", i.get());

    // Motor Positions
    SmartDashboard.putNumber("Left Encoder", drive.leftMain.getEncoder().getPosition());
    SmartDashboard.putNumber("Right Encoder", drive.rightMain.getEncoder().getPosition());

    // Battery Voltage
    SmartDashboard.putNumber("Battery Voltage", RobotController.getBatteryVoltage());

    // PID
    SmartDashboard.putNumber("PID Output", pid.calculate(drive.leftMain.getEncoder().getPosition(), 1));

    // Update
    SmartDashboard.updateValues();
  }

  @Override
  public void autonomousInit() {
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    /*pid.setP(10);
    pid.setI(0);
    pid.setD(0);*/
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
  }

  /** This function is called periodically during operator control. */

  int currentId = 1;

  @Override
  public void teleopPeriodic() {
    if (c.primary.getAButton()) {
      drive.drive(pid.calculate(drive.leftMain.getEncoder().getPosition(), 1), 0);
    }
    if (c.primary.getBButtonPressed()) {
      drive.leftMain.getEncoder().setPosition(0);
      drive.rightMain.getEncoder().setPosition(0);
    }

    if (c.getBrake()) {
      drive.toggleMode();
    }

    if (c.getIntakeToggle()) {
      i.toggle();
    }

    if (c.getIntakeIn()) {
      i.set(.7);
    } else if (c.getIntakeOut()) {
      i.set(-.7);
    } else {
      i.set(0);
    }

    if (c.getHopper()) {
      h.run();
    } else if (c.getUnJam()) {
      h.unjam();
    } else {
      h.stop();
    }

    drive.drive(c.getDriveForward(), c.getDriveTurn());
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
    drive.setMode(IdleMode.kBrake);
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {
  }

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
  }
}