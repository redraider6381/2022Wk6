package frc.robot;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import frc.robot.Mecanum;

public class Autonomous {


    static double MarginOfError = 2.75; 

    static double Leftpower = 0.2;
<<<<<<< Updated upstream
    static double Rightpower = 0.2;
=======
    static double RIghtpower = 0.2;
    static double BackLeftpower = 0.2;
    static double BackRightpower = 0.2;
    static double FrontLeftpower = 0.2;
    static double FrontRightpower = 0.2;
    static boolean strafing = false;
>>>>>>> Stashed changes
    static double Divider = 1;
    static double MaxVelocity = 0.2;
    static double AxcelleratingVelocity = 0.2;
    static double AccelorationTime = 1;
    public static Timer Ramptimer = new Timer();
    public static Timer waittimer = new Timer();
    static double direction = 1;
    static double Conversion = 1;//(30/31)*Math.PI;

<<<<<<< Updated upstream
    public static void drive(double dist,Boolean Direction) {
        if(!Direction)
        {
            direction = -1;
        }
        else
        {
            direction = 1;   
        }
        
=======
    public static void drive(double dist, double xDist) {
        //Strafing
        if(strafing = true){
            BackLeftpower = -Components.BLStrafingPID.calculate(Components.BL.getPosition(), dist);
            BackRightpower = Components.BRStrafingPID.calculate(Components.BR.getPosition(), dist);
            FrontLeftpower = Components.FLStrafingPID.calculate(Components.BR.getPosition(), dist);
            FrontRightpower = -Components.FRStrafingPID.calculate(Components.BR.getPosition(), dist);

            Components.CANBackLeft.set(BackLeftpower);
            Components.CANBackRight.set(BackRightpower);
            Components.CANFrontLeft.set(FrontLeftpower);
            Components.CANFrontRight.set(FrontRightpower);
        }
>>>>>>> Stashed changes
        // power = Components.pid.calculate(Components.BL.getPosition(), dist);
        // if (dist < 0) { // backwards
        //     direction = -1;
        //     // Components.CANFrontLeft.setInverted(false);
        //     // Components.CANBackLeft.setInverted(false);
        //     // Components.CANFrontRight.setInverted(true);
        //     // Components.CANBackRight.setInverted(true);

        // }
        // else
        // {
        //     // Components.CANFrontLeft.setInverted(true);
        //     // Components.CANBackLeft.setInverted(true);
        //     // Components.CANFrontRight.setInverted(false);
        //     // Components.CANBackRight.setInverted(false);
        //     direction = 1;
        // }

            //PID calculates the value 
            Leftpower = Components.TranslationalPID.calculate(direction*Components.BL.getPosition()*Conversion, dist);
            Rightpower = Components.TranslationalPID.calculate(direction*Components.BR.getPosition()*Conversion, dist);

            //Keeping the ratio from the PID it ramps up:
            if(Ramptimer.get()<AccelorationTime)
            {
                System.out.println("Time Accelerating: "+ Ramptimer.get());
                AxcelleratingVelocity = MaxVelocity*Ramptimer.get()/AccelorationTime;
                if (Leftpower > AxcelleratingVelocity) {
                    Divider = Leftpower / AxcelleratingVelocity;
                    // Leftpower = -0.4;
                    Leftpower = Leftpower / Divider;
                    Rightpower = Rightpower / Divider;
                }
                if (Rightpower > AxcelleratingVelocity) {
                    // Rightpower = -0.4;
                    Divider = Rightpower / AxcelleratingVelocity;
                    // Leftpower = -0.4;
                    Leftpower = Leftpower / Divider;
                    Rightpower = Rightpower / Divider;
                }

                //Powers:
                Components.CANBackLeft.set(Leftpower*direction);
                Components.CANBackRight.set(Rightpower*direction);
                Components.CANFrontLeft.set(Leftpower*direction);
                Components.CANFrontRight.set(Rightpower*direction);
                System.out.println("Going fowards: Left Pos:" + Components.BL.getPosition()*direction + "Left Speed: " + Leftpower*direction
                                        + "RightPos: " + Components.BR.getPosition()*direction + "RightSpeed " + Rightpower*direction);
                
                //Checks if done:
                if (Components.BL.getPosition()*direction*Conversion >= dist-MarginOfError && (Components.BR.getPosition()*direction*Conversion >= dist-MarginOfError)) {
                    Components.CANBackLeft.set(0);
                    Components.CANBackRight.set(0);
                    Components.CANFrontLeft.set(0);
                    Components.CANFrontRight.set(0);
                    System.out.println("Done going fowards");
                    Components.BL.setPosition(0);
                    Components.BR.setPosition(0);
                    Autonomous.Ramptimer.reset();
                    Autonomous.Ramptimer.start();
                    Autonomous.waittimer.reset();
                    Autonomous.waittimer.start();
                    Robot.AutoStep++;
                }
            }

            //Done Ramping up:
            else{
           
            //Keeping the ratio from the PID it stays below a max velocity:
            if (Leftpower > MaxVelocity) {
                Divider = Leftpower / MaxVelocity;
                // Leftpower = -0.4;
                Leftpower = Leftpower / Divider;
                Rightpower = Rightpower / Divider;
            }
            if (Rightpower > MaxVelocity) {
                // RIghtpower = -0.4;
                Divider = Rightpower / MaxVelocity;
                // Leftpower = -0.4;
                Leftpower = Leftpower / Divider;
                Rightpower = Rightpower / Divider;
            }

            //Powers:
            Components.CANBackLeft.set(Leftpower*direction);
            Components.CANBackRight.set(Rightpower*direction);
            Components.CANFrontLeft.set(Leftpower*direction);
            Components.CANFrontRight.set(Rightpower*direction);
            System.out.println("Going fowards: Left Pos:" + Components.BL.getPosition()*Conversion + "Left Speed: " + Leftpower*direction
                    + "RightPos: " + Components.BR.getPosition()*Conversion + "RightSpeed " + Rightpower*direction);


            //Checks if Done:
            if (Components.BL.getPosition()*direction*Conversion >= dist-MarginOfError && (Components.BR.getPosition()*direction*Conversion >= dist-MarginOfError)) {
                Components.CANBackLeft.set(0);
                Components.CANBackRight.set(0);
                Components.CANFrontLeft.set(0);
                Components.CANFrontRight.set(0);
                System.out.println("Done going fowards");
                Components.BL.setPosition(0);
                Components.BR.setPosition(0);
                Autonomous.Ramptimer.reset();
                Autonomous.Ramptimer.start();
                Autonomous.waittimer.reset();
                Autonomous.waittimer.start();
                Robot.AutoStep++;
            }
        }


        // if (dist < 0) { // backwards
        //     Leftpower = -Components.TranslationalPID.calculate(Components.BL.getPosition(), dist);
        //     RIghtpower = -Components.TranslationalPID.calculate(Components.BR.getPosition(), dist);

        //     if(Ramptimer.get()<AccelorationTime)
        //     {
        //         System.out.println("Time Accelerating: "+ Ramptimer.get());
        //         AxcelleratingVelocity = MaxVelocity*Ramptimer.get()/AccelorationTime;
        //         if (Leftpower < -AxcelleratingVelocity) {
        //             Divider = Leftpower /-AxcelleratingVelocity;
        //             // Leftpower = -0.4;
        //             Leftpower = Leftpower / Divider;
        //             RIghtpower = RIghtpower / Divider;
        //         }
        //         if (RIghtpower < -AxcelleratingVelocity) {
        //             // RIghtpower = -0.4;
        //             Divider = RIghtpower / -AxcelleratingVelocity;
        //             // Leftpower = -0.4;
        //             Leftpower = Leftpower / Divider;
        //             RIghtpower = RIghtpower / Divider;
        //         }
        //     // }
        //         Components.CANBackLeft.set(-Leftpower);
        //         Components.CANBackRight.set(-RIghtpower);
        //         Components.CANFrontLeft.set(-Leftpower);
        //         Components.CANFrontRight.set(-RIghtpower);
        //         System.out.println("Going fowards: Left Pos:" + Components.BL.getPosition() + "Left Speed: " + Leftpower
        //                 + "RightPos: " + Components.BR.getPosition() + "RightSpeed " + RIghtpower);
        //         if (Components.BL.getPosition() <= dist && (Components.BR.getPosition() <= dist)) {
        //             Components.CANBackLeft.set(0);
        //             Components.CANBackRight.set(0);
        //             Components.CANFrontLeft.set(0);
        //             Components.CANFrontRight.set(0);
        //             System.out.println("Done going fowards");
        //             Components.BL.setPosition(0);
        //             Components.BR.setPosition(0);
        //             Autonomous.Ramptimer.reset();
        //             Autonomous.Ramptimer.start();
        //             Autonomous.waittimer.reset();
        //             Autonomous.waittimer.start();
        //             Robot.AutoStep++;
        //         }
        //     }
        //     else{
        //     // RIghtpower = Leftpower = 0.25;
        //     if (Leftpower < -MaxVelocity) {
        //         Divider = Leftpower / -MaxVelocity;
        //         // Leftpower = -0.4;
        //         Leftpower = Leftpower / Divider;
        //         RIghtpower = RIghtpower / Divider;
        //     }
        //     if (RIghtpower < -MaxVelocity) {
        //         // RIghtpower = -0.4;
        //         Divider = RIghtpower / -MaxVelocity;
        //         // Leftpower = -0.4;
        //         Leftpower = Leftpower / Divider;
        //         RIghtpower = RIghtpower / Divider;
        //     }
        //     Components.CANBackLeft.set(Leftpower);
        //     Components.CANBackRight.set(RIghtpower);
        //     Components.CANFrontLeft.set(Leftpower);
        //     Components.CANFrontRight.set(RIghtpower);
        //     System.out.println("Going backwards: Left Pos:" + Components.BL.getPosition() + "Left Speed: " + Leftpower
        //             + "RightPos: " + Components.BR.getPosition() + "RightSpeed " + RIghtpower);
        //     if ((Components.BL.getPosition() <= dist) && (Components.BR.getPosition() <= dist)) {
        //         Components.CANBackLeft.set(0);
        //         Components.CANBackRight.set(0);
        //         Components.CANFrontLeft.set(0);
        //         Components.CANFrontRight.set(0);
        //         System.out.println("Done going backwards");
        //         Components.BL.setPosition(0);
        //         Components.BR.setPosition(0);
        //         Autonomous.Ramptimer.reset();
        //         Autonomous.Ramptimer.start();
        //         Autonomous.waittimer.reset();
        //         Autonomous.waittimer.start();
        //         Robot.AutoStep++;
        //     }
        // }
        // } 
        // else if (dist > 0) //fowards
        // {
        //     Leftpower = Components.TranslationalPID.calculate(Components.BL.getPosition(), dist);
        //     RIghtpower = Components.TranslationalPID.calculate(Components.BR.getPosition(), dist);
        //     if(Ramptimer.get()<AccelorationTime)
        //     {
        //         System.out.println("Time Accelerating: "+ Ramptimer.get());
        //         AxcelleratingVelocity = MaxVelocity*Ramptimer.get()/AccelorationTime;
        //         if (Leftpower > AxcelleratingVelocity) {
        //             Divider = Leftpower / AxcelleratingVelocity;
        //             // Leftpower = -0.4;
        //             Leftpower = Leftpower / Divider;
        //             RIghtpower = RIghtpower / Divider;
        //         }
        //         if (RIghtpower > AxcelleratingVelocity) {
        //             // RIghtpower = -0.4;
        //             Divider = RIghtpower / AxcelleratingVelocity;
        //             // Leftpower = -0.4;
        //             Leftpower = Leftpower / Divider;
        //             RIghtpower = RIghtpower / Divider;
        //         }
        //     // }
        //         Components.CANBackLeft.set(Leftpower);
        //         Components.CANBackRight.set(RIghtpower);
        //         Components.CANFrontLeft.set(Leftpower);
        //         Components.CANFrontRight.set(RIghtpower);
        //         System.out.println("Going fowards: Left Pos:" + Components.BL.getPosition() + "Left Speed: " + Leftpower
        //                 + "RightPos: " + Components.BR.getPosition() + "RightSpeed " + RIghtpower);
        //         if (Components.BL.getPosition() >= dist && (Components.BR.getPosition() >= dist)) {
        //             Components.CANBackLeft.set(0);
        //             Components.CANBackRight.set(0);
        //             Components.CANFrontLeft.set(0);
        //             Components.CANFrontRight.set(0);
        //             System.out.println("Done going fowards");
        //             Components.BL.setPosition(0);
        //             Components.BR.setPosition(0);
        //             Autonomous.Ramptimer.reset();
        //             Autonomous.Ramptimer.start();
        //             Autonomous.waittimer.reset();
        //             Autonomous.waittimer.start();
        //             Robot.AutoStep++;
        //         }
        //     }
        //     else{
           
        //     if (Leftpower > MaxVelocity) {
        //         Divider = Leftpower / MaxVelocity;
        //         // Leftpower = -0.4;
        //         Leftpower = Leftpower / Divider;
        //         RIghtpower = RIghtpower / Divider;
        //     }
        //     if (RIghtpower > MaxVelocity) {
        //         // RIghtpower = -0.4;
        //         Divider = RIghtpower / MaxVelocity;
        //         // Leftpower = -0.4;
        //         Leftpower = Leftpower / Divider;
        //         RIghtpower = RIghtpower / Divider;
        //     }
        // // }
        //     Components.CANBackLeft.set(Leftpower);
        //     Components.CANBackRight.set(RIghtpower);
        //     Components.CANFrontLeft.set(Leftpower);
        //     Components.CANFrontRight.set(RIghtpower);
        //     System.out.println("Going fowards: Left Pos:" + Components.BL.getPosition() + "Left Speed: " + Leftpower
        //             + "RightPos: " + Components.BR.getPosition() + "RightSpeed " + RIghtpower);
        //     if (Components.BL.getPosition() >= dist && (Components.BR.getPosition() >= dist)) {
        //         Components.CANBackLeft.set(0);
        //         Components.CANBackRight.set(0);
        //         Components.CANFrontLeft.set(0);
        //         Components.CANFrontRight.set(0);
        //         System.out.println("Done going fowards");
        //         Components.BL.setPosition(0);
        //         Components.BR.setPosition(0);
        //         Autonomous.Ramptimer.reset();
        //         Autonomous.Ramptimer.start();
        //         Autonomous.waittimer.reset();
        //         Autonomous.waittimer.start();
        //         Robot.AutoStep++;
        //     }
        // }
        // }
    }

    static double turnPower = 0.2;
    static double GyroFactor = 20000;

    // static double GyroFactor = 1;
    public static void turn(double degrees, Boolean direction) {
        if (direction) { // clockwise
            System.out.println("Turning clockwise Degrees =" + Components.gyro.getAngle() * GyroFactor);
            // turnPower = Components.LimelightPID.calculate(Components.gyro.getAngle(),
            // degrees);
            Components.CANBackLeft.set(turnPower);
            Components.CANBackRight.set(-turnPower);
            Components.CANFrontLeft.set(turnPower);
            Components.CANFrontRight.set(-turnPower);
            if (Components.gyro.getAngle() * GyroFactor <= degrees) {
                Components.CANBackLeft.set(0);
                Components.CANBackRight.set(0);
                Components.CANFrontLeft.set(0);
                Components.CANFrontRight.set(0);
                Components.BL.setPosition(0);
                Autonomous.Ramptimer.reset();
                Autonomous.Ramptimer.start();
                Autonomous.waittimer.reset();
                Autonomous.waittimer.start();
                Robot.AutoStep++;
            }
        } else if (!direction) { // counterclockwise
            System.out.println("Turning counterclockwise Degrees =" + Components.gyro.getAngle() * GyroFactor);
            // turnPower = -Components.LimelightPID.calculate(Components.gyro.getAngle(),
            // degrees);
            Components.CANBackLeft.set(-turnPower);
            Components.CANBackRight.set(turnPower);
            Components.CANFrontLeft.set(-turnPower);
            Components.CANFrontRight.set(turnPower);
            if (Components.gyro.getAngle() * GyroFactor >= degrees) {
                System.out.println("Done turning");
                Components.CANBackLeft.set(0);
                Components.CANBackRight.set(0);
                Components.CANFrontLeft.set(0);
                Components.CANFrontRight.set(0);
                Components.BL.setPosition(0);
                Autonomous.Ramptimer.reset();
                Autonomous.Ramptimer.start();
                Autonomous.waittimer.reset();
                Autonomous.waittimer.start();
                Robot.AutoStep++;
            }
        }
    }

    public static Timer uptakeTimer = new Timer();

    public static void uptake(double ballNumber) {
        // Possibly recalc shooting speed from distance
        Components.Uptake.set(-0.15);
        Components.IndexerRight.set(-Robot.indexerPower);
        Components.IndexerLeft.set(-Robot.indexerPower);
        if (uptakeTimer.get() > 2 * ballNumber) {
            // stops Intake and Indexer
            Components.Uptake.set(0);
            Components.IndexerRight.set(0);
            Components.IndexerLeft.set(0);
            // stops flywheel
            Components.CANShooter1.set(0);
            Components.CANShooter2.set(0);
            Autonomous.Ramptimer.reset();
            Autonomous.Ramptimer.start();
            Autonomous.waittimer.reset();
            Autonomous.waittimer.start();
            Robot.AutoStep++;
        }
    }

    // static double turnPower = 0.2;

    // public static void turn(double degrees, Boolean direction) {
    // if (direction) { // clockwise
    // // turnPower = Components.LimelightPID.calculate(Components.gyro.getAngle(),
    // // degrees);
    // Components.CANBackLeft.set(turnPower);
    // Components.CANBackRight.set(-turnPower);
    // Components.CANFrontLeft.set(turnPower);
    // Components.CANFrontRight.set(-turnPower);
    // if (degrees <= Components.gyro.getAngle()) {
    // Components.CANBackLeft.set(0);
    // Components.CANBackRight.set(0);
    // Components.CANFrontLeft.set(0);
    // Components.CANFrontRight.set(0);
    // Components.BL.setPosition(0);
    // Robot.AutoStep++;
    // }
    // } else if (!direction) { // counterclockwise
    // // turnPower = -Components.LimelightPID.calculate(Components.gyro.getAngle(),
    // // degrees);
    // Components.CANBackLeft.set(-turnPower);
    // Components.CANBackRight.set(turnPower);
    // Components.CANFrontLeft.set(-turnPower);
    // Components.CANFrontRight.set(turnPower);
    // if (degrees >= Components.gyro.getAngle()) {
    // Components.CANBackLeft.set(0);
    // Components.CANBackRight.set(0);
    // Components.CANFrontLeft.set(0);
    // Components.CANFrontRight.set(0);
    // Components.BL.setPosition(0);
    // Robot.AutoStep++;
    // }
    // }
    // }

    // static double turnPower = 0.2;
    public static void LimelightTurnToAligned() {
        if (Robot.validTarget() && Components.XBController.getAButton()) {
            // move the robot

            // turns the robot until the angle is 0
            if (Robot.tx.getDouble(0.0) > 2 || Robot.tx.getDouble(0.0) < -2) {
                if (Robot.tx.getDouble(0.0) < -2) {
                    // move robot counterclockwise
                    // speed = -0.2;
                    System.out.println(Robot.tx.getDouble(0.0));
                    turnPower = Components.LimelightPID.calculate(Robot.tx.getDouble(0.0), 0);
                    Components.CANFrontLeft.set(-turnPower);
                    Components.CANFrontRight.set(turnPower);
                    Components.CANBackLeft.set(-turnPower);
                    Components.CANBackRight.set(turnPower);
                    System.out.println("turning, Angle =" + Robot.tx.getDouble(0.0) + "speed" + turnPower);
                } else if (Robot.tx.getDouble(0.0) > 2) {
                    // speed = -0.2;
                    System.out.println(Robot.tx.getDouble(0.0));
                    turnPower = Components.LimelightPID.calculate(Robot.tx.getDouble(0.0), 0);
                    Components.CANFrontLeft.set(turnPower);
                    Components.CANFrontRight.set(-turnPower);
                    Components.CANBackLeft.set(turnPower);
                    Components.CANBackRight.set(-turnPower);
                    System.out.println("turning, Angle =" + Robot.tx.getDouble(0.0) + "speed" + turnPower);

                    // SmartDashboard.putData(Se);
                }
            }
            if (!(Robot.tx.getDouble(0.0) > 2 || Robot.tx.getDouble(0.0) < -2)) {
                System.out.println("At tape");
                Autonomous.Ramptimer.reset();
                Autonomous.Ramptimer.start();
                Autonomous.waittimer.reset();
                Autonomous.waittimer.start();
                Robot.AutoStep++;
                // Robot.SmartDashboard.getEntry("Ready to Shoot!");
            }

        } else {
            System.out.println("Don't see the tape");
        }
    }
    // public static void forward(double go){
    // double power = Components.pid.calculate(Components.BL.getPosition(), go);
    // Components.CANBackLeft.set(power);
    // Components.CANBackRight.set(power);
    // Components.CANFrontLeft.set(power);
    // Components.CANFrontRight.set(power);
    // }
    // public void turning(double go){
    // double power = Components.pid.calculate(Components.BL.getPosition(), go);
    // Components.CANBackLeft.set(-power);
    // Components.CANBackRight.set(power);
    // Components.CANFrontLeft.set(-power);
    // Components.CANFrontRight.set(power);
    // }
    // public void strafing(double go){
    // double power = Components.pid.calculate(Components.BL.getPosition(), go);
    // Components.CANBackLeft.set(power);
    // Components.CANBackRight.set(-power);
    // Components.CANFrontLeft.set(power);
    // Components.CANFrontRight.set(-power);
    // }
    // public static void move(double forward, double turning, double strafing){
    // double bl = forward - turning + strafing;
    // double br = forward + turning - strafing;
    // double fl = forward - turning + strafing;
    // double fr = forward + turning - strafing;

    public static void setPneumatics() {
        System.out.println("Pnuematics waiting for: "+Robot.pneumaticsTimer.get());
        if(Robot.pneumaticsTimer.get()>0.5)
        {
            Components.intakePneumatic.set(Value.kOff);
        }
    }

    public static void puase(double time) {
        System.out.println("Waiting for: "+Robot.waitTimer.get());
        if(Robot.waitTimer.get()>time)
        {
            Autonomous.Ramptimer.reset();
            Autonomous.Ramptimer.start();
            Autonomous.waittimer.reset();
            Autonomous.waittimer.start();
            Robot.AutoStep++;
        }
    }

    // double blspeed = Components.pid.calculate(Components.BL.getPosition(), bl);
    // double brspeed = Components.pid.calculate(Components.BR.getPosition(), br);
    // double flspeed = Components.pid.calculate(Components.FL.getPosition(), fl);
    // double frspeed = Components.pid.calculate(Components.FR.getPosition(), fr);

    // Components.CANBackLeft.set(blspeed);
    // Components.CANBackRight.set(brspeed);
    // Components.CANFrontLeft.set(flspeed);
    // Components.CANFrontRight.set(frspeed);
    // }
    // public static void TurnAngle(double deg, double speed, boolean
    // direction)//Yes means turn right, no means turn left
    // {
    // Components.gyro.reset();;
    // if(!direction)
    // {
    // speed=-speed;
    // }

    // /**
    // * Clamps the motor powers while maintaining power ratios.
    // * @param powers The motor powers to clamp.
    // */
    // while(Components.gyro.getAngle() < Math.toRadians(deg)) //I'm not sure if we
    // are using a gyro or imu, but I think this will work to call it.
    // //Otherwsie we can figure out degrees/count and use encoders.
    // {
    // //driving
    // Components.CANFrontLeft.set(speed);
    // Components.CANFrontRight.set(-speed);
    // Components.CANBackLeft.set(speed);
    // Components.CANBackRight.set(-speed);
    // }
    // //stopped
    // Components.CANFrontLeft.set(0);
    // Components.CANFrontRight.set(0);
    // Components.CANBackLeft.set(0);
    // Components.CANBackRight.set(0);

    // }
    // private static void clampPowers(List<Double> motors) {
    // }

}