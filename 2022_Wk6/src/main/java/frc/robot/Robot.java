// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import javax.lang.model.util.ElementScanner6;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalSource;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button. Button;


import com.revrobotics.CANSparkMax;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.MjpegServer;
import edu.wpi.first.cscore.VideoMode.PixelFormat;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;

import edu.wpi.first.cscore.UsbCamera;

// import com.analog.adis16448.frc.ADIS16448_IMU;

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
  
  //Webcam:
  UsbCamera camera1;

  //Important
  public static double ShootingPower = 0.355;

  //Limelight
  // private NetworkTable limeTable = NetworkTableInstance.getDefault().getTable("limelight");
  // int limeStateNum = limeTable.getEntry("ledMode").getNumber(0).intValue();

  // public void updateLimelightDashboard() {
  //   String limelightState;

  //   int limeStateNum = limeTable.getEntry("ledMode").getNumber(0).intValue();

  //   if (limeStateNum == 1)
  //     limelightState = "OFF";
  //   else if (limeStateNum == 3)
  //     limelightState = "ON";
  //   else
  //     limelightState = "UNKNOWN";

  //   SmartDashboard.putString("LimelightState", limelightState);
  // }
  private static final String k5BallAuto = "5 ball Auto ";
  private static final String k1BallAuto = "1 ball Auto ";
  private static final String k48InchesAuto = "48 Inches Auto";
  private static final String k48InchesAutoBackwards = "-48 Inches Auto";
  private static final String kTurn90Auto = "90 degrees Auto";

  
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  //pneumatic things
  public static Timer pneumaticsTimer = new Timer();
  public static Timer waitTimer = new Timer();

  // Teleop Variables
  public static double drivePower = 0.25;
  public static double indexerPower = 0.33;
  public static double uptakeSpeed = -0.5;

  static NetworkTableEntry tx;
  NetworkTableEntry ty;
  static NetworkTableEntry tv;
  NetworkTable table;

  double leftYAxis;
  double rightYAxis;
  double leftXAxis;
  double rightXAxis;
  double limeLightTurnSpeed;
  int caseNumber = 1;

  Timer timer = new Timer();
  Timer shootingTimer = new Timer();

  static int AutoStep = 0;

  // Auto Variables
  public double driveSpeed = 0.25;
  public double turnSpeed = 0.25;
  public double FlywheelSpeed = 0.75;
  // public double IntakeSpeed = 0.75;
  public double distance = 40;
  // turns to the right
  public double turnamount = 0;
  public double intaketime = 5;
  public double flwheeltime = 4;

  // Auto 2 Variables
  public double driveSpeed2 = 0.125;
  public double turnSpeed2 = 0.125;
  public double FlywheelSpeed2 = 0.375;
  // public double IntakeSpeed2 = 0.375;
  public double distance2 = 40;
  // turns to the right
  public double turnamount2 = 0;
  public double intaketime2 = 5;
  public double flwheeltime2 = 3;
  static double Conversion = 23/68; //((30/31)*Math.PI);

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  // SmartDashboard.putNumber("RPM1: ", 0);
  //   SmartDashboard.putNumber("RPM2: ", 0);
  @Override
  public void robotInit() {
    SmartDashboard.putNumber("RPM1: ", 0);
    SmartDashboard.putNumber("RPM2: ", 0);
    // Components.compressor.enableDigital();
    //Webcam:
    camera1 = CameraServer.startAutomaticCapture(0);
    camera1.setResolution(160, 120);

    Components.gyro.reset();
    Components.CANBackLeft.setIdleMode(CANSparkMax.IdleMode.kBrake);
    Components.CANFrontLeft.setIdleMode(CANSparkMax.IdleMode.kBrake);
    Components.CANBackRight.setIdleMode(CANSparkMax.IdleMode.kBrake);
    Components.CANFrontRight.setIdleMode(CANSparkMax.IdleMode.kBrake);


    // m_chooser.addOption("5BallAuto", k5BallAuto);
    m_chooser.addOption("1BallAuto", k1BallAuto);
    m_chooser.addOption("48InchesAuto", k48InchesAuto);
    // m_chooser.addOption("-48InchesAuto", k48InchesAutoBackwards);
    // m_chooser.addOption("Turn90Auto", kTurn90Auto);
    SmartDashboard.putData("Auto choices", m_chooser);
    
    // limitSwitch = new DigitalInput(1);
    Components.CANFrontLeft.setInverted(true);
    Components.CANBackLeft.setInverted(true);

    Components.CANShooter2.setInverted(true);
    Components.IndexerLeft.setInverted(true);
    Components.CANBackLeft.set(0);
    Components.CANBackRight.set(0);
    Components.CANFrontLeft.set(0);
    Components.CANFrontRight.set(0);
    Components.BL.setPosition(0);
    Components.BR.setPosition(0);
    Components.CANShooter1.set(0);
    Components.CANShooter2.set(0);
    Components.Uptake.set(0);
    Autonomous.Ramptimer.reset();
    Autonomous.Ramptimer.start();
    Autonomous.waittimer.reset();
    Autonomous.waittimer.start();

    //Pneumatic Things
    
    pneumaticsTimer.reset();

    // Components.BL.setPositionConversionFactor((30/31)*Math.PI);//Circumfrance is 6 Pi, Gear ratio is:10/62 //Important (maybe should be just pi)
    // Components.BR.setPositionConversionFactor((30/31)*Math.PI);//Circumfrance is 6 Pi, Gear ratio is:10/62 //Important (maybe should be just pi)
    // Components.BL.setPositionConversionFactor(Math.PI); //Important (maybe should be just pi)

    //might be important for gyro
    Components.gyro.calibrate();

    // Components.intakePneumatic.set(Value.kReverse);
    // Components.intakePneumatic.set(Value.kForward); //Out

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
    Components.CANShooter1.set(0);
    Components.CANShooter2.set(0);
    Components.Uptake.set(0);
    AutoStep = 0;
    m_autoSelected = SmartDashboard.getString("Auto Selector", k5BallAuto);
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);
    Components.BL.setPosition(0);
    Components.BR.setPosition(0);
    // Autonomous.Ramptimer.reset();
    // Autonomous.Ramptimer.start();
    timer.reset();
    timer.start();
    Components.intakePneumatic.set(Value.kReverse);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
    
        case k48InchesAuto:

        // Go forward 48 inches:
        switch (AutoStep) {
          case 0:
          System.out.println("case 0: Starting Auto");
          Components.BL.setPosition(0);
          Components.BR.setPosition(0);
          Autonomous.Ramptimer.reset();
          Autonomous.Ramptimer.start();
        
          AutoStep++;
          break;
          case 1:
          System.out.println("case 1: Moving 48 in forward");
            Autonomous.drive(16.2353,true);
            break;
          case 2:
          // System.out.println("case 2: Stopping");
          Components.CANBackLeft.set(0);
          Components.CANBackRight.set(0);
          Components.CANFrontLeft.set(0);
          Components.CANFrontRight.set(0);
            break;
        }
        break;
        case k48InchesAutoBackwards:

        // Go backwards 48 inches:
        switch (AutoStep) {
          case 0:
          System.out.println("case 0: Starting Auto");
          Components.BL.setPosition(0);
          Components.BR.setPosition(0);
          Autonomous.Ramptimer.reset();
          Autonomous.Ramptimer.start();
          AutoStep++;
          break;
          case 1:
          System.out.println("case 1: Moving 48 in backwards");
            Autonomous.drive(48*Conversion,false);
            break;
          case 2:
          // System.out.println("case 2: Stopping");
          Components.CANBackLeft.set(0);
          Components.CANBackRight.set(0);
          Components.CANFrontLeft.set(0);
          Components.CANFrontRight.set(0);
            break;
        }
        break;


        case kTurn90Auto:

        switch (AutoStep) {
          case 0:
          System.out.println("case 0: Starting Auto");
          Components.BL.setPosition(0);
          Components.BR.setPosition(0);
          Components.gyro.reset();
          AutoStep++;
          break;
          case 1:
          System.out.println("case 1: turning 90 degrees clockwise");
            Autonomous.turn(-90,true);
            break;
          case 2:
          // System.out.println("case 2: Stopping");
          Components.CANBackLeft.set(0);
          Components.CANBackRight.set(0);
          Components.CANFrontLeft.set(0);
          Components.CANFrontRight.set(0);
            break;
        }
        break;



        case k5BallAuto: 

        switch (AutoStep) {
          case 0:
            System.out.println("Starting 5 ball Auto");
            System.out.println("case 0: Picking up Ball 1");
            //run intake and indexer
            Components.IndexerRight.set(indexerPower);
            Components.IndexerLeft.set(indexerPower);
            Components.intakeMotor.set(1);
            Components.intakePneumatic.set(Value.kOff); //use a timer to wait 0.5 seconds before doing this
            // Autonomous.setPneumatics();

            //go forward to ball 1 and adds to autosteps
            // Autonomous.drive(47);
            break;
          case 1:
            Components.intakePneumatic.set(Value.kOff);
            System.out.println("case 1: Returning to Tarmac and Starting Flywheel");
            //stop intake and indexer
            // Components.Indexer1.set(0);
            // Components.Indexer2.set(0);
            // Components.intakeMotor.set(0);
            //go backwards to tarmac
            // Components.BL.setPosition(0);
            // Components.BR.setPosition(0);
            // Autonomous.drive(-87);
            //start flywheel
            Components.CANShooter1.set(ShootingPower);
            Components.CANShooter2.set(ShootingPower);
            break;
          case 2:
            System.out.println("case 2: Turning to Hub");
            //turn to angle with limelight
            Autonomous.LimelightTurnToAligned();
            break;
          case 3:
            System.out.println("case 3: Shooting 2 balls");
            //Run uptake, wait, stop uptake
            Autonomous.uptakeTimer.reset();
            Autonomous.uptake(2);
            break;
          case 4:
            System.out.println("case 4: Turning to ball 2");
            //turn to ball 2 
            Autonomous.turn(70,true);
            break;
          case 5:
            System.out.println("case 5: Moving to ball 2");
            //run intake and indexer and go forward to ball 2
            Components.IndexerRight.set(indexerPower);
            Components.IndexerLeft.set(indexerPower);
            Components.intakeMotor.set(1);
            Components.BL.setPosition(0);
            Components.BR.setPosition(0);
            // Autonomous.drive(108);
            break;
          case 6:
            System.out.println("case 6: Starting flywheel and returning to tarmac");
            // //stop intake and indexer and run flywheel
            // Components.Indexer1.set(0);
            // Components.Indexer2.set(0);
            // Components.intakeMotor.set(0);
            Components.CANShooter1.set(ShootingPower);
            Components.CANShooter2.set(ShootingPower);
            //go backwards to tarmac
            // Components.BL.setPosition(0);
            // Components.BR.setPosition(0);
            // Autonomous.drive(-108);
            break;
          case 7:
            System.out.println("case 7: Turning towards hub");
            //turn to angle
            Autonomous.LimelightTurnToAligned();
            break;
          case 8:
            System.out.println("case 8: Shooting 1 ball");
            //run uptake, wait, stop uptake and flywheel
            Autonomous.uptakeTimer.reset();
            Autonomous.uptake(1);
            Components.CANShooter1.set(0);
            Components.CANShooter2.set(0);
            break;
          case 9:
            System.out.println("case 9: Turning towards ball 3");
            //turn toward ball 3
            Autonomous.turn(82,true);
            break;
          case 10:
            System.out.println("case 10: Going to pick up balls 3 and 4");
            //run intake and indexer
            Components.IndexerRight.set(indexerPower);
            Components.IndexerLeft.set(indexerPower);
            Components.intakeMotor.set(1);
            //turn to ball 3 
            //drive to ball 3 - MAYBE CHANGE TO SPLINE IN FUTURE, OR ADD TURN
            // Components.BL.setPosition(0);
            // Components.BR.setPosition(0);
            // Autonomous.drive(256);
            break;
          case 11:
            //pause to pick up ball 4
            System.out.println("case 11: Starting to Wait for 4th ball");
            Timer.delay(3);
            System.out.println("Done Waiting for 4th ball");
            AutoStep++;
            break;
          case 12:
            System.out.println("case 12: Starting flywheel and returning to tarmac");
            //stop intake and indexer
            Components.IndexerRight.set(0);
            Components.IndexerLeft.set(0);
            Components.intakeMotor.set(0);
            //start flywheel
            Components.CANShooter1.set(ShootingPower);
            Components.CANShooter2.set(ShootingPower);
            //turn and drive to tarmac - MAYBE CHANGE TO SPLINE IN FUTURE, OR ADD TURN
            // Components.BL.setPosition(0);
            // Components.BR.setPosition(0);
            // Autonomous.drive(-256);
            break;
          case 13:      
            System.out.println("case 13: Turning to Hub");
            Autonomous.LimelightTurnToAligned();
            break;
          case 14:       
            System.out.println("case 14: Shooting 2 balls");
            //turn to angle
            //Run uptake, wait, stop uptake
            Autonomous.uptakeTimer.reset();
            Autonomous.uptake(2);
            //stop flywheel
            Components.CANShooter1.set(0);
            Components.CANShooter2.set(0);
            break;
          case 15:
            System.out.println("case 15: Moving off tarmac");
            //go forward til off tarmac
            // Autonomous.drive(80);
            break;
        }
        break;




        case k1BallAuto:
          switch (AutoStep) {
            case 0:
              System.out.println("Starting 1 ball Auto");
              System.out.println("case 0: Picking up Ball 1");
              // Autonomous.setPneumatics();
              //run intake and indexer
              Components.IndexerLeft.set(-indexerPower);
              Components.IndexerRight.set(-indexerPower);
              Components.intakeMotor.set(-1);
              //go forward to ball 1 and adds to autosteps
              // Components.BL.setPosition(0);
              // Components.BR.setPosition(0);
              waitTimer.reset();
              waitTimer.start();
              Autonomous.drive(16.2353,true);
              break;
              case 1:
              // AutoStep++;
              Autonomous.Ramptimer.reset();
              Autonomous.Ramptimer.start();
              Autonomous.puase(1.5);
              break;
            case 2:
              System.out.println("case 1: Returning to Tarmac and Starting Flywheel");
              //stop intake and indexer
              // Components.Indexer1.set(0);
              // Components.Indexer2.set(0);
              // Components.intakeMotor.set(0);
              //go backwards to tarmac
              // Components.BL.setPosition(0);
              // Components.BR.setPosition(0);
              Components.CANShooter1.set(ShootingPower);
              Components.CANShooter2.set(ShootingPower);Components.CANShooter1.set(ShootingPower);
              Components.CANShooter2.set(ShootingPower);
              Autonomous.drive(23,false);
              Components.intakePneumatic.set(Value.kForward);
              Components.intakeMotor.set(0);
              //start flywheel
              break;
            case 3:
              System.out.println("case 2: Turning to Hub");
              //turn to angle with limelight
              // Autonomous.LimelightTurnToAligned();
              Autonomous.uptakeTimer.reset();
              Autonomous.uptakeTimer.start();
              AutoStep++;
              break;
            case 4:
              System.out.println("case 3: Shooting 2 balls");
              //Run uptake, wait, stop uptake
              Autonomous.uptake(2);
              break;
              case 5:
              System.out.println("case 5: Driving off Tarmac");
              Autonomous.drive(25.706,true);
              //Run uptake, wait, stop uptake
              // Autonomous.uptake(2);
              break;
          }
            break;
      }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    shootingTimer.reset();

  }

  /**
   * This function is called periodically during operator control.
   * 
   * @return
   */
  @Override
  public void teleopPeriodic() {

    SmartDashboard.putNumber("RPM1: ", Components.flywheelEncoder1.getVelocity());
    SmartDashboard.putNumber("RPM2: ", Components.flywheelEncoder2.getVelocity());

    // System.out.println("gyro Angle: "+ Components.gyro.getAngle());

    table = NetworkTableInstance.getDefault().getTable("limelight");
    double targetHeight = 2.6416; // meter, 104 inches
    tx = table.getEntry("tx");
    ty = table.getEntry("ty");
    tv = table.getEntry("tv");



    // System.out.println("ultrasonic says : "+Components.ultrasonic.get());
    // System.out.println(Components.ultrasonic.get());
    // System.out.println("lidar says : " + Components.lidar.get());
    // System.out.println(Components.lidar.get());

    // if (Math.abs(Components.XBController.getRawAxis(2)) > 0.1) {
    //   Components.Indexer2.set(Components.XBController.getRawAxis(2) * indexerPower);
    //   Components.Indexer1.set(Components.XBController.getRawAxis(2) * indexerPower);
    // } else if (Math.abs(Components.XBController.getRawAxis(3)) > 0.1) {
    //   Components.Indexer2.set(-Components.XBController.getRawAxis(3) * indexerPower);
    //   Components.Indexer1.set(-Components.XBController.getRawAxis(3) * indexerPower);
    // } else {
    //   Components.Indexer2.set(0);
    //   Components.Indexer1.set(0);
    // }

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
    // double SpeedToClimb = 0.25*0.7;
    double SpeedToClimb = -0.7;
    double TurnSensitivity = 0.15; //based on the distance really
    if(Components.XBController.getXButton())
    {
      // Components.CANFrontLeft.set(SpeedToClimb);
      // Components.CANFrontRight.set(SpeedToClimb);
      // Components.CANBackLeft.set(SpeedToClimb);
      // Components.CANBackRight.set(SpeedToClimb);
      setDriveForMecanum(Mecanum.joystickToMotion(leftXAxis, -SpeedToClimb, rightXAxis, rightYAxis));
    }
      // else if(Components.XBController.getYButton()) //As it straffes to the right, turns clockwise aka turns to thr right, as it strafes left turns left
      // {
      //   Components.CANFrontLeft.set(drivePower*(leftXAxis + leftXAxis*TurnSensitivity));
      //   Components.CANFrontRight.set(drivePower*(-leftXAxis - leftXAxis*TurnSensitivity));
      //   Components.CANBackLeft.set(drivePower*(-leftXAxis   + leftXAxis*TurnSensitivity));
      //   Components.CANBackRight.set(drivePower*(leftXAxis - leftXAxis*TurnSensitivity));
      // }
    else{
      setDriveForMecanum(Mecanum.joystickToMotion(leftXAxis, leftYAxis, rightXAxis, rightYAxis));
    }

    boolean L = Components.XBController.getLeftBumper();
    boolean R = Components.XBController.getRightBumper();

    // Intake Code:
    if (Components.XBController2.getLeftY() < -0.05) {
      // fowards intake and indexer
      Components.intakePneumatic.set(Value.kOff);
      Components.intakeMotor.set(1);
      Components.IndexerLeft.set(indexerPower);
      Components.IndexerRight.set(indexerPower);

    } 
  
    else if (Components.XBController2.getLeftY() > 0.05) {
      // Backwards intake
      Components.intakeMotor.set(-1);
        // Backwards indexer
        Components.intakePneumatic.set(Value.kOff);
        Components.IndexerLeft.set(-indexerPower);
        Components.IndexerRight.set(-indexerPower);
      } 
      else {
        Components.intakeMotor.set(0);
        // otherwise indexer stays how it is
        Components.IndexerLeft.set(0);
        Components.IndexerRight.set(0);
      }





      if (L && R && !(Components.XBController2.getRightTriggerAxis()>0.05)) {
        drivePower = 0.65;
      } else if (L || R) {
        drivePower = 0.5;
      } else {
        drivePower = 0.25;
      }

    // Shooter Code:
    if (Components.XBController2.getRightTriggerAxis()>0.05)
    {
      // ShootingPower = 0.45-(ty.getDouble(0.0)/100); //Some sort of function for this angle.
      System.out.println("Shooting!!!");
      if (drivePower>0.5)
      {
        drivePower = 0.5;
      }
      Components.CANShooter1.set(ShootingPower);
      Components.CANShooter2.set(ShootingPower);
    } else {
      Components.CANShooter1.set(0);
      Components.CANShooter2.set(0);
      // Components.Uptake.set(0);
    }






    

    

    if (Components.XBController2.getLeftTriggerAxis()>0.05) 
    {
      // Components.intakeMotor.set(-1);
      Components.Uptake.set(uptakeSpeed);
      Components.IndexerLeft.set(-indexerPower);
      Components.IndexerRight.set(-indexerPower);
    } else {
      Components.Uptake.set(0);
    }
    //  // else
    // {
    // Component.intakeMotor.set(0);
    // }
    // double speed = -0.2;

    if (Components.XBController2.getPOV() ==90 ||Components.XBController2.getPOV() ==270){
      System.out.println("Should be off");
      Components.intakePneumatic.set(Value.kOff); //off
    }

    if (Components.XBController2.getPOV() ==180){
      System.out.println("Should be in");
      Components.intakePneumatic.set(Value.kForward); //in
    }
    if (Components.XBController2.getPOV() ==0){
      System.out.println("Should be out");
      Components.intakePneumatic.set(Value.kReverse); //out
    }
    table = NetworkTableInstance.getDefault().getTable("limelight");
    distance = 2.6416; // meter
    tx = table.getEntry("tx");
    ty = table.getEntry("ty");
    tv = table.getEntry("tv");

    if(validTarget()&& !Components.XBController.getAButton())
    {
        // System.out.println("Side Angle:"+tx.getDouble(0.0));
        System.out.println("Distance:"+(74/Math.tan(Math.toRadians(ty.getDouble(0.0)+60.25))-6.125)+"Up Angle:"+ty.getDouble(0.0)+"Side Angle:"+tx.getDouble(0.0));
    }
      else if (validTarget() && Components.XBController.getAButton())
      {
        // Autonomous.LimelightTurnToAligned();
        // if (Robot.validTarget() && Components.XBController.getAButton()){
          // move the robot
          
          double MarginOfError = 1;
          double MaxVel = 0.6;
          // turns the robot until the angle is 0
          if(Robot.tx.getDouble(0.0) > MarginOfError ||Robot.tx.getDouble(0.0) < -MarginOfError) {
            if(Robot.tx.getDouble(0.0) < -MarginOfError ){
              // move robot counterclockwise
              // speed = -0.2;
              System.out.println(Robot.tx.getDouble(0.0));
              limeLightTurnSpeed = -Components.LimelightPID.calculate(Robot.tx.getDouble(0.0), 0);
              if(limeLightTurnSpeed>MaxVel)
                {
                  limeLightTurnSpeed = MaxVel;
                }
              else if(limeLightTurnSpeed<-MaxVel)
                {
                  limeLightTurnSpeed = -MaxVel;
                }
              // Components.CANFrontLeft.set(-limeLightTurnSpeed);
              // Components.CANFrontRight.set(limeLightTurnSpeed);
              // Components.CANBackLeft.set(-limeLightTurnSpeed);
              // Components.CANBackRight.set(limeLightTurnSpeed);
              setDriveForMecanum(Mecanum.joystickToMotion(leftXAxis, leftYAxis, -limeLightTurnSpeed, rightYAxis));
              System.out.println("turning, Angle ="+Robot.tx.getDouble(0.0)+"speed"+limeLightTurnSpeed);
          }
          else if(Robot.tx.getDouble(0.0) > MarginOfError){
          // speed = -0.2;
          System.out.println(Robot.tx.getDouble(0.0));
          limeLightTurnSpeed = -Components.LimelightPID.calculate(Robot.tx.getDouble(0.0), 0);
          if(limeLightTurnSpeed>MaxVel)
            {
              limeLightTurnSpeed = MaxVel;
            }
          else if(limeLightTurnSpeed<-MaxVel)
            {
              limeLightTurnSpeed = -MaxVel;
            }
          // Components.CANFrontLeft.set(limeLightTurnSpeed);
          // Components.CANFrontRight.set(-limeLightTurnSpeed);
          // Components.CANBackLeft.set(limeLightTurnSpeed);
          // Components.CANBackRight.set(-limeLightTurnSpeed);
          setDriveForMecanum(Mecanum.joystickToMotion(leftXAxis, leftYAxis, -limeLightTurnSpeed, rightYAxis));
          System.out.println("turning, Angle ="+Robot.tx.getDouble(0.0)+"speed"+limeLightTurnSpeed);
  
          // SmartDashboard.putData(Se);
          // }
          }
          if(!(Robot.tx.getDouble(0.0)>MarginOfError ||Robot.tx.getDouble(0.0)<-MarginOfError))
          {
          System.out.println("At tape");
          Robot.AutoStep++;
          // Robot.SmartDashboard.getEntry("Ready to Shoot!");
          }
      
      }
      else{
      System.out.println("Don't see the tape");
      }
    }
    else if(Components.XBController.getAButton()){
    System.out.println("Don't see the tape");
    }
    // turns the robot until the limelight has a target
    
  
    

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

  public static boolean validTarget() {
    if (tv.getDouble(0.0) == 0) {
    return false;
    } else {
    return true;
    }
    }
    public double UpAndDown_AngleFromTarget(){
      return ty.getDouble(0.0);
    }
  public double SideToSide_AngleFromTarget(){
    return tx.getDouble(0.0);
  }


  public double distanceFromTarget(){
    return (74/Math.tan(Math.toRadians(ty.getDouble(0.0)+60.25))-6.125);
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
