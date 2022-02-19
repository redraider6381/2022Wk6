package frc.robot;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.XboxController;

public class Components {
    public static CANSparkMax CANBackLeft     = new CANSparkMax(1, MotorType.kBrushless);
    public static CANSparkMax CANBackRight    = new CANSparkMax(12, MotorType.kBrushless);
    public static CANSparkMax CANFrontLeft    = new CANSparkMax(6, MotorType.kBrushless);
    public static CANSparkMax CANFrontRight   = new CANSparkMax(14, MotorType.kBrushless);
    public static XboxController XBController= new XboxController(0);
    public static DoubleSolenoid intakePneumatic = new DoubleSolenoid(PneumaticsModuleType.REVPH, 0, 1);
    // public static Compressor compressor = new Compressor(0, PneumaticsModuleType.REVPH);
    public static CANSparkMax Indexer1    = new CANSparkMax(5, MotorType.kBrushless);
    public static CANSparkMax Indexer2   = new CANSparkMax(13, MotorType.kBrushless);
    public static Joystick happyStick = new Joystick(1);
    public static CANSparkMax intakeMotor = new CANSparkMax(7, MotorType.kBrushless);
}
