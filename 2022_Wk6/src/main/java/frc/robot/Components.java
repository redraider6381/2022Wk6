package frc.robot;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.ADIS16448_IMU;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.SerialPort.StopBits;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;
import edu.wpi.first.wpilibj.Servo;

import edu.wpi.first.wpilibj.Ultrasonic;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoMode.PixelFormat;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.MjpegServer;






public class Components {
    public static CANSparkMax CANBackLeft     = new CANSparkMax(1, MotorType.kBrushless);
    public static CANSparkMax CANBackRight    = new CANSparkMax(12, MotorType.kBrushless);
    public static CANSparkMax CANFrontLeft    = new CANSparkMax(6, MotorType.kBrushless);
    public static CANSparkMax CANFrontRight   = new CANSparkMax(14, MotorType.kBrushless);
    public static XboxController XBController = new XboxController(0);
    public static DoubleSolenoid intakePneumatic = new DoubleSolenoid(PneumaticsModuleType.REVPH, 0, 1);
    // public static Compressor compressor = new Compressor(0,PneumaticsModuleType.REVPH);
    public static CANSparkMax Indexer1    = new CANSparkMax(5, MotorType.kBrushless);
    public static CANSparkMax Indexer2   = new CANSparkMax(13, MotorType.kBrushless);
    public static XboxController XBController2 = new XboxController(1);
    public static CANSparkMax intakeMotor = new CANSparkMax(7, MotorType.kBrushless);

    //Indexers:
    // public static Servo HoodServo  = new Servo(0);
    // public static Servo HoodServo2  = new Servo(1);
    public static CANSparkMax Uptake  = new CANSparkMax(8, MotorType.kBrushless);
    // public static CANSparkMax RightUptake  = new CANSparkMax(9, MotorType.kBrushless);
    
    //Flywheels:
    public static CANSparkMax CANShooter1 = new CANSparkMax(11, MotorType.kBrushless);
    public static CANSparkMax CANShooter2 = new CANSparkMax(2, MotorType.kBrushless);


 //   // Creates UsbCamera and MjpegServer [1] and connects them
//     static UsbCamera usbCamera = new UsbCamera("USB Camera 0", 0);
//     static MjpegServer mjpegServer1 = new MjpegServer("serve_USB Camera 0", 1181);

// // Creates the CvSink and connects it to the UsbCamera
// static CvSink cvSink = new CvSink("opencv_USB Camera 0");

// // Creates the CvSource and MjpegServer [2] and connects them
// static CvSource outputStream = new CvSource("Blur", PixelFormat.kMJPEG, 640, 480, 30);
// static MjpegServer mjpegServer2 = new MjpegServer("serve_Blur", 1182);


// public static AnalogPotentiometer ultrasonic = new AnalogPotentiometer(0, 195, 0);
// public static AnalogPotentiometer lidar = new AnalogPotentiometer(0, 1, 0);

    // limelight
    // public static NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

    //Uptake:
    public static CANSparkMax RightUptake = new CANSparkMax(9, MotorType.kBrushless);


    // encoders
    // public static RelativeEncoder BL = CANBackLeft.getEncoder();
    public static RelativeEncoder BL = CANBackLeft.getEncoder();
    public static RelativeEncoder BR = CANBackRight.getEncoder(); //Mwahaha!!!!!
    // public static RelativeEncoder BR = CANBackRight.getEncoder();
    // public static RelativeEncoder FL = CANFrontLeft.getEncoder();
    // public static RelativeEncoder FR = CANFrontRight.getEncoder();

    // public static Ultrasonic ultrasonic = new Ultrasonic(1,2);

    //gyros
    // private SPI.Port m_spi_port = new SPI.Port();
    public static final ADIS16448_IMU gyro = new ADIS16448_IMU(ADIS16448_IMU.IMUAxis.kX,SPI.Port.kMXP, ADIS16448_IMU.CalibrationTime._2s);
    // public static AnalogGyro gyro = new AnalogGyro(0); // angle increases going clockwise
    // public static AnalogGyro driveGyro = new AnalogGyro(0);

    // pids
    public static PIDController LimelightPID = new PIDController(0.15, 0, 0);
    // public static PIDController TurnPID = new PIDController(0.01, 0, 0);
    public static PIDController TranslationalPID = new PIDController(0.0085, 0, 0);
    // public static ProfiledPIDController TranslationalPID = new ProfiledPIDController();
    public static PIDController BLStrafingPID = new PIDController(0.2, 0, 0);
    public static PIDController BRStrafingPID = new PIDController(0.2, 0, 0);
    public static PIDController FLStrafingPID = new PIDController(0.2, 0, 0);
    public static PIDController FRStrafingPID = new PIDController(0.2, 0, 0);

}
