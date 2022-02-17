// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import javax.lang.model.util.ElementScanner6;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();


  public static double drivePower = 0.125;
  public static double Power = 0.125;

  double leftYAxis;
  double rightYAxis;
  double leftXAxis;
  double rightXAxis;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    // SuperSecretFunction();
    Components.Indexer2.setInverted(true);
    // Components.CANBackLeft.setInverted(true);
    // Components.CANFrontLeft.setInverted(true);
    Components.compressor.enableDigital();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    

  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

    if(Math.abs(Components.XBController.getRawAxis(2))>0.1)
    {
    Components.Indexer2.set(Components.XBController.getRawAxis(2)*Power);
    Components.Indexer1.set(Components.XBController.getRawAxis(2)*Power);
    }
    else if(Math.abs(Components.XBController.getRawAxis(3))>0.1)
    {
    Components.Indexer2.set(-Components.XBController.getRawAxis(3)*Power);
    Components.Indexer1.set(-Components.XBController.getRawAxis(3)*Power);
    }
    else
    {
    Components.Indexer2.set(0);
    Components.Indexer1.set(0);
    }

    // if(Math.abs(Components.XBController.getRawAxis(1))>0.075)
    // {
    // leftYAxis = -Components.XBController.getRawAxis(1);
    // }
    // else
    // {
    //   leftYAxis = 0;
    // }

    // if(Math.abs(Components.XBController.getRawAxis(0))>0.075)
    // {
    //   leftXAxis = Components.XBController.getRawAxis(0);
    // }
    // else
    // {
    //   leftYAxis = 0;
    // }
    // if(Math.abs(Components.XBController.getRawAxis(4))>0.075)
    // {
    //   rightXAxis = Components.XBController.getRawAxis(4);
    // } 
    // else
    // {
    //   rightXAxis = 0;
    // }
    
    leftYAxis = -Components.XBController.getRawAxis(1);
    rightYAxis = -Components.XBController.getRawAxis(5);
    leftXAxis = Components.XBController.getRawAxis(0);
    rightXAxis = Components.XBController.getRawAxis(4);
    setDriveForMecanum(Mecanum.joystickToMotion(leftXAxis,leftYAxis,rightXAxis,rightYAxis));

    boolean L = Components.XBController.getLeftBumper();
    boolean R = Components.XBController.getRightBumper();
    
    
    if(L&&R)
    {
        drivePower = 1;
    }
    else if(L||R)
    {
        drivePower = 0.5;
    }
    else
    {
      drivePower = 0.125;
    }

    if((Components.happyStick.getRawButton(2)))//When it is pulled back, the intake starts
    {
        Components.intakeMotor.set(-1);
    }
    else  
    {
        Components.intakeMotor.set(0);
    } 
    

  if(Components.happyStick.getRawButton(7)){
    Components.intakePneumatic.set(Value.kReverse);
    System.out.println("Reverse. Beep. Beep. Beep.");
  }
  if(Components.happyStick.getRawButton(8)){
    Components.intakePneumatic.set(Value.kForward);
    System.out.println("Here I go! Forward.");
  }
  }
  private static void setDriveForMecanum(Mecanum.Motion motion) {
    Mecanum.Wheels wheels = Mecanum.motionToWheels(motion);
    /*runtime.reset();
    robot.FL.setPower(Range.clip(Math.abs(wheels.frontLeft*DrivePower*(runtime.seconds()/0.75)), -1.0, 1.0));
    robot.FR.setPower(Range.clip(Math.abs(wheels.frontRight*DrivePower*(runtime.seconds()/0.75)), -1.0, 1.0));
    robot.BL.setPower(Range.clip(Math.abs(wheels.backLeft*DrivePower*(runtime.seconds()/0.75)), -1.0, 1.0));
    robot.BR.setPower(Range.clip(Math.abs(wheels.backRight*DrivePower*(runtime.seconds()/0.75)), -1.0, 1.0));*/
    Components.CANFrontLeft.set(wheels.frontLeft*drivePower);
    Components.CANFrontRight.set(-wheels.frontRight*drivePower);
    Components.CANBackLeft.set(wheels.backLeft*drivePower);
    Components.CANBackRight.set(-wheels.backRight*drivePower);
     
   
}

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

}
