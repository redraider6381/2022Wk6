// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import javax.lang.model.util.ElementScanner6;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.MjpegServer;
import edu.wpi.first.cscore.VideoMode.PixelFormat;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */

public class Robot extends TimedRobot {
  private NetworkTable limeTable = NetworkTableInstance.getDefault().getTable("limelight");
  // int limeStateNum = limeTable.getEntry("ledMode").getNumber(0).intValue();

  public void updateLimelightDashboard() {
    String limelightState;

    int limeStateNum = limeTable.getEntry("ledMode").getNumber(0).intValue();

    if (limeStateNum == 1)
      limelightState = "OFF";
    else if (limeStateNum == 3)
      limelightState = "ON";
    else
      limelightState = "UNKNOWN";

    SmartDashboard.putString("LimelightState", limelightState);
  }

  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  // Teleop Variables
  public static double drivePower = 0.25;
  public static double indexerPower = 0.33;
  public static double ShootingPower = 0.34;
  public static double uptakeSpeed = -0.5;

  NetworkTableEntry tx;
  NetworkTableEntry ty;
  NetworkTableEntry tv;
  NetworkTable table;

  double leftYAxis;
  double rightYAxis;
  double leftXAxis;
  double rightXAxis;
  int caseNumber = 1;

  Timer timer = new Timer();

  int AutoStep = 0;

  // Auto Variables
  public double driveSpeed = 0.25;
  public double turnSpeed = 0.25;
  public double FlywheelSpeed = 0.75;
  public double IntakeSpeed = 0.75;
  public double distance = 40;
  // turns to the right
  public double turnamount = 0;
  public double intaketime = 5;
  public double flwheeltime = 4;

  // Auto 2 Variables
  public double driveSpeed2 = 0.125;
  public double turnSpeed2 = 0.125;
  public double FlywheelSpeed2 = 0.375;
  public double IntakeSpeed2 = 0.375;
  public double distance2 = 40;
  // turns to the right
  public double turnamount2 = 0;
  public double intaketime2 = 5;
  public double flwheeltime2 = 3;

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    Components.CANBackLeft.setIdleMode(CANSparkMax.IdleMode.kBrake);
    Components.CANFrontLeft.setIdleMode(CANSparkMax.IdleMode.kBrake);
    Components.CANBackRight.setIdleMode(CANSparkMax.IdleMode.kBrake);
    Components.CANFrontRight.setIdleMode(CANSparkMax.IdleMode.kBrake);

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    // limitSwitch = new DigitalInput(1);
    Components.CANFrontLeft.setInverted(true);
    Components.CANBackLeft.setInverted(true);
    Components.CANShooter2.setInverted(true);
    Components.Indexer2.setInverted(true);
    Components.CANBackLeft.set(0);
    Components.CANBackRight.set(0);
    Components.CANFrontLeft.set(0);
    Components.CANFrontRight.set(0);
    Components.BL.setPosition(0);
    Components.BL.setPositionConversionFactor(0.1);

    // Components.intakePneumatic.set(Value.kReverse);
    // Components.intakePneumatic.set(Value.kForward); //Out

    // Initial Pos:
    // Components.HoodServo.setPosition(0.5);
    // Components.HoodServo2.setPosition(0.5);

    // Components.cvSink.setSource(Components.usbCamera);
    // Components.mjpegServer2.setSource(Components.outputStream);
    // Components.mjpegServer1.setSource(Components.usbCamera);

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and
   * test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {

    // Creates the CvSink and connects it to the UsbCamera
    // CvSink cvSink = CameraServer.getVideo();

    // Creates the CvSource and MjpegServer [2] and connects them
    // CvSource outputStream = CameraServer.putVideo("Blur", 640, 480);
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different
   * autonomous modes using the dashboard. The sendable chooser code works with
   * the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the
   * chooser code and
   * uncomment the getString line to get the auto name from the text box below the
   * Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure
   * below with additional strings. If using the SendableChooser make sure to add
   * them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    AutoStep = 0;
    m_autoSelected = m_chooser.getSelected();
    m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    Components.BL.setPosition(0);
    timer.reset();
    timer.start();

  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto: // Start of Auto 1:
        // Put custom auto code here

        // No turning Code:
        switch (AutoStep) {
          case 0:
            timer.reset();
            timer.start();
            AutoStep++;
            break;
          case 1:
            Components.CANBackLeft.set(0);
            Components.CANBackRight.set(0);
            Components.CANFrontLeft.set(0);
            Components.CANFrontRight.set(0);
            System.out.println("Flywheel running for this many seconds: " + timer.get());
            Components.CANShooter1.set(FlywheelSpeed);
            Components.CANShooter2.set(FlywheelSpeed);
            if (timer.get() > flwheeltime) {
              Components.Uptake.set(uptakeSpeed);
              Components.Uptake.set(uptakeSpeed);
              AutoStep++;

            }
            break;
          case 2:
            // Run Intake too
            Components.CANShooter1.set(0);
            Components.CANShooter2.set(0);
            Components.Uptake.set(0);
            Components.Uptake.set(0);
            Components.intakeMotor.set(1);
            Components.intakeMotor.set(1);
            Components.Indexer1.set(1);
            Components.Indexer2.set(1);

            System.out.println("Driving Forward, step 1, Postion: " + Components.BL.getPosition());
            Components.CANBackLeft.set(driveSpeed);
            Components.CANBackRight.set(driveSpeed);
            Components.CANFrontLeft.set(driveSpeed);
            Components.CANFrontRight.set(driveSpeed);
            // }
            if (Components.BL.getPosition() > distance) {
              Components.CANBackLeft.set(0);
              Components.CANBackRight.set(0);
              Components.CANFrontLeft.set(0);
              Components.CANFrontRight.set(0);
              Components.BL.setPosition(0);
              System.out.println("Done with moving");
              AutoStep++;
              timer.reset();
              timer.start();
            }
            break;
          case 3:
            Components.CANBackLeft.set(0);
            Components.CANBackRight.set(0);
            Components.CANFrontLeft.set(0);
            Components.CANFrontRight.set(0);
            System.out.println("Indexer and Intake!" + timer.get());
            // run intake and indexer here
            Components.Indexer1.set(IntakeSpeed);
            Components.Indexer2.set(IntakeSpeed);
            Components.intakeMotor.set(1);
            Components.intakeMotor.set(1);
            if (timer.get() > intaketime) {
              System.out.println("Done with Intaking!");
              Components.BL.setPosition(0);
              timer.reset();
              timer.start();
              AutoStep = 2;
            }
            break;
          case 4:
            // turns to the right
            Components.CANBackLeft.set(turnSpeed);
            Components.CANBackRight.set(-turnSpeed);
            Components.CANFrontLeft.set(turnSpeed);
            Components.CANFrontRight.set(-turnSpeed);
            System.out.println("Turning a little: " + Components.BL.getPosition());

            if (Components.BL.getPosition() >= turnamount) {
              Components.CANBackLeft.set(0);
              Components.CANBackRight.set(0);
              Components.CANFrontLeft.set(0);
              Components.CANFrontRight.set(0);
              timer.reset();
              timer.start();
              AutoStep++;
            }
            break;

          case 5:
            Components.CANBackLeft.set(0);
            Components.CANBackRight.set(0);
            Components.CANFrontLeft.set(0);
            Components.CANFrontRight.set(0);
            System.out.println("keep it all running until the end " + timer.get());

            break;
        }
        break;

      // This would be the start of a second Auto Option:
      case kDefaultAuto:

        // No turning Code:
        switch (AutoStep) {
          case 0:
            timer.reset();
            timer.start();
            AutoStep++;
          case 1:
            // Run Intake too
            // Components.intakeMotor.set(1);
            // Components.intakeMotor.set(1);
            // Components.indexer1.set(1);
            // Components.indexer2.set(1);

            System.out.println("Driving Forward, step 1, Postion: " + timer.get());
            Components.CANBackLeft.set(driveSpeed2);
            Components.CANBackRight.set(driveSpeed2);
            Components.CANFrontLeft.set(driveSpeed2);
            Components.CANFrontRight.set(driveSpeed2);
            // }
            if (timer.get() > 2) {
              Components.CANBackLeft.set(0);
              Components.CANBackRight.set(0);
              Components.CANFrontLeft.set(0);
              Components.CANFrontRight.set(0);
              Components.BL.setPosition(0);
              System.out.println("Done with moving");
              timer.reset();
              timer.start();
              AutoStep++;
            }
            break;
          case 2:
            Components.CANBackLeft.set(0);
            Components.CANBackRight.set(0);
            Components.CANFrontLeft.set(0);
            Components.CANFrontRight.set(0);
            break;
          // System.out.println("Indexer and Intake!"+timer.get());
          // //run intake and indexer here
          // // Components.Indexer1.set(IntakeSpeed);
          // // Components.Indexer2.set(IntakeSpeed);
          // // Components.intakeMotor.set(1);
          // // Components.intakeMotor.set(1);
          // if(timer.get() > intaketime2)
          // {
          // System.out.println("Done with Intaking!");
          // // Components.CANShooter1.set(FlywheelSpeed);
          // // Components.CANShooter2.set(FlywheelSpeed);
          // Components.BL.setPosition(0);
          // timer.reset();
          // timer.start();
          // AutoStep =2;
          // }
          // break;
          // case 2:
          // //turns to the right
          // Components.CANBackLeft.set(turnSpeed2);
          // Components.CANBackRight.set(-turnSpeed2);
          // Components.CANFrontLeft.set(turnSpeed2);
          // Components.CANFrontRight.set(-turnSpeed2);
          // System.out.println("Turning a little: " + Components.BL.getPosition());
          // // Components.CANShooter1.set(FlywheelSpeed);
          // // Components.CANShooter2.set(FlywheelSpeed);
          // if(Components.BL.getPosition() >= turnamount2){
          // Components.CANBackLeft.set(0);
          // Components.CANBackRight.set(0);
          // Components.CANFrontLeft.set(0);
          // Components.CANFrontRight.set(0);
          // timer.reset();
          // timer.start();
          // AutoStep++;
          // }
          // break;
          // case 3:
          // Components.CANBackLeft.set(0);
          // Components.CANBackRight.set(0);
          // Components.CANFrontLeft.set(0);
          // Components.CANFrontRight.set(0);
          // System.out.println("Flywheel running for this many seconds: " + timer.get());
          // // Components.CANShooter1.set(FlywheelSpeed);
          // // Components.CANShooter2.set(FlywheelSpeed);
          // if(timer.get() > FlywheelSpeed2){
          // // Components.LeftUptake.set(uptakeSpeed);
          // // Components.RightUptake.set(uptakeSpeed);
          // AutoStep++;

          // }
          // break;
          // case 4:
          // Components.CANBackLeft.set(0);
          // Components.CANBackRight.set(0);
          // Components.CANFrontLeft.set(0);
          // Components.CANFrontRight.set(0);
          // System.out.println("keep it all running until the end " + timer.get());

          // break;
        }
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
  }

  /**
   * This function is called periodically during operator control.
   * 
   * @return
   */
  @Override
  public void teleopPeriodic() {

    // System.out.println("ultrasonic says : "+Components.ultrasonic.get());
    // System.out.println(Components.ultrasonic.get());
    System.out.println("lidar says : " + Components.lidar.get());
    // System.out.println(Components.lidar.get());

    if (Math.abs(Components.XBController.getRawAxis(2)) > 0.1) {
      Components.Indexer2.set(Components.XBController.getRawAxis(2) * indexerPower);
      Components.Indexer1.set(Components.XBController.getRawAxis(2) * indexerPower);
    } else if (Math.abs(Components.XBController.getRawAxis(3)) > 0.1) {
      Components.Indexer2.set(-Components.XBController.getRawAxis(3) * indexerPower);
      Components.Indexer1.set(-Components.XBController.getRawAxis(3) * indexerPower);
    } else {
      Components.Indexer2.set(0);
      Components.Indexer1.set(0);
    }

    // if(Math.abs(Components.XBController.getRawAxis(1))>0.075)
    // {
    // leftYAxis = -Components.XBController.getRawAxis(1);
    // }
    // else
    // {
    // leftYAxis = 0;
    // }

    // if(Math.abs(Components.XBController.getRawAxis(0))>0.075)
    // {
    // leftXAxis = Components.XBController.getRawAxis(0);
    // }
    // else
    // {
    // leftYAxis = 0;
    // }
    // if(Math.abs(Components.XBController.getRawAxis(4))>0.075)
    // {
    // rightXAxis = Components.XBController.getRawAxis(4);
    // }
    // else
    // {
    // rightXAxis = 0;
    // }

    leftYAxis = -Components.XBController.getRawAxis(1);
    rightYAxis = -Components.XBController.getRawAxis(5);
    leftXAxis = Components.XBController.getRawAxis(0);
    rightXAxis = -Components.XBController.getRawAxis(4);
    // Hood Controls
    // Components.HoodServo.setPosition(Components.happyStick.getRawAxis(3));
    // Components.HoodServo2.setPosition(-Components.happyStick.getRawAxis(3));
    setDriveForMecanum(Mecanum.joystickToMotion(leftXAxis, leftYAxis, rightXAxis, rightYAxis));

    boolean L = Components.XBController.getLeftBumper();
    boolean R = Components.XBController.getRightBumper();

    // Shooter Code:
    if (Components.happyStick.getRawAxis(1) > 0.1) {
      // fowards intake and indexer
      Components.intakeMotor.set(-1);
      Components.Indexer2.set(-indexerPower);
      Components.Indexer1.set(-indexerPower);

    } else if (Components.happyStick.getRawAxis(1) < -0.1) {
      // Backwards intake
      // Components.CANShooter1.set(-Components.happyStick.getRawAxis(1)*ShootingPower);
      // Components.CANShooter2.set(-Components.happyStick.getRawAxis(1)*ShootingPower);
      Components.intakeMotor.set(1);
      // Components.Indexer2.set(-indexerPower);
      // Components.Indexer1.set(-indexerPower);
      if (Components.happyStick.getRawButton(3)) {
        // Backwards indexer
        Components.Indexer2.set(indexerPower);
        Components.Indexer1.set(indexerPower);
      } else {
        // otherwise indexer stays how it is
        Components.Indexer2.set(0);
        Components.Indexer1.set(0);
      }

    }

    else {
      // Components.CANShooter1.set(0);
      // Components.CANShooter2.set(0);
      Components.intakeMotor.set(0);
    }

    // Uptake Code:
    if (Components.happyStick.getRawButton(1)) {
      System.out.println("Shooting!!!");
      Components.CANShooter1.set(ShootingPower);
      Components.CANShooter2.set(ShootingPower);
      // Components.Uptake.set(uptakeSpeed);
    } else {
      Components.CANShooter1.set(0);
      Components.CANShooter2.set(0);
      // Components.Uptake.set(0);
    }

    if (L && R) {
      drivePower = 1;
    } else if (L || R) {
      drivePower = 0.5;
    } else {
      drivePower = 0.25;
    }

    if ((Components.happyStick.getRawButton(2))) {

      // Components.intakeMotor.set(-1);
      Components.Uptake.set(uptakeSpeed);
    } else {
      Components.Uptake.set(0);
    }
    // else
    // {
    // Component.intakeMotor.set(0);
    // }
    if (Components.happyStick.getRawButton(6)) {
      System.out.println("Should be off");
      // Components.intakePneumatic.set(Value.kOff);
    }

    if (Components.happyStick.getRawButton(7)) {
      System.out.println("Should be in");
      // Components.intakePneumatic.set(Value.kReverse);
    }
    if (Components.happyStick.getRawButton(8)) {
      System.out.println("Should be out");
      // Components.intakePneumatic.set(Value.kForward);
    }
    // table = NetworkTableInstance.getDefault().getTable("limelight");
    // distance = 2.6416; // meter
    // NetworkTableEntry tx = table.getEntry("tx");
    // NetworkTableEntry ty = table.getEntry("ty");

    // NetworkTableEntry tv = table.getEntry("tv");

    // if (validTarget() == true) {
    //   // System.out.println(tx + "" + ty);
    //   System.out.println("Distance:" + distanceFromTarget());
    //   System.out.println("angle from target:" + angleFromTarget());
    //   System.out.println("side to side angle from target:" + tx.getDouble(0.0));
    // } else {
    //   System.out.println("No tape");
    // }
    // limeTable.getEntry("ledMode").setNumber(1);
    // updateLimelightDashboard();

  }

  private static void setDriveForMecanum(Mecanum.Motion motion) {
    Mecanum.Wheels wheels = Mecanum.motionToWheels(motion);
    /*
     * runtime.reset();
     * robot.FL.setPower(Range.clip(Math.abs(wheels.frontLeft*DrivePower*(runtime.
     * seconds()/0.75)), -1.0, 1.0));
     * robot.FR.setPower(Range.clip(Math.abs(wheels.frontRight*DrivePower*(runtime.
     * seconds()/0.75)), -1.0, 1.0));
     * robot.BL.setPower(Range.clip(Math.abs(wheels.backLeft*DrivePower*(runtime.
     * seconds()/0.75)), -1.0, 1.0));
     * robot.BR.setPower(Range.clip(Math.abs(wheels.backRight*DrivePower*(runtime.
     * seconds()/0.75)), -1.0, 1.0));
     */
    Components.CANFrontLeft.set(wheels.frontLeft * drivePower);
    Components.CANFrontRight.set(wheels.frontRight * drivePower);
    Components.CANBackLeft.set(wheels.backLeft * drivePower);
    Components.CANBackRight.set(wheels.backRight * drivePower);

  }

  public boolean validTarget() {
    if (tv.getDouble(0.0) == 0) {
      return false;
    } else {
      return true;
    }
  }

  public double angleFromTarget() {
    return ty.getDouble(0.0);
  }

  public double sideangleFromTarget() {
    return tx.getDouble(0.0);
  }

  public double distanceFromTarget() {
    return (104 * Math.sin(ty.getDouble(0.0))) / (Math.sin(90 -
        ty.getDouble(0.0)));
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
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
