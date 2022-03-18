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

        public static void setPneumatics(){
            if(Robot.pneumaticsTimer.get() > 0.5){
                Components.intakePneumatic.set(Value.kOff);
                // Robot.AutoStep++;
            }
        }

        static double power = 0.2; 
        public static void drive(double dist){
            //power = Components.pid.calculate(Components.BL.getPosition(), dist);
            if(dist<0){ //backwards
                // power = -Components.LimelightPID.calculate(Components.BL.getPosition(), dist);
                // if(power<-0.5)
                // {
                //     power = -0.5;
                // }
                    Components.CANBackLeft.set(-power);
                    Components.CANBackRight.set(-power);
                    Components.CANFrontLeft.set(-power);
                    Components.CANFrontRight.set(-power);
                    System.out.println("Going backwards"+Components.BL.getPosition());
                if(Components.BL.getPosition() <= dist){
                    Components.CANBackLeft.set(0);
                    Components.CANBackRight.set(0);
                    Components.CANFrontLeft.set(0);
                    Components.CANFrontRight.set(0);
                    System.out.println("Done going backwards");
                    Components.BL.setPosition(0);
                    Robot.AutoStep++;
                }

            }
            else if(dist>0) //fowards 
            {
                // power = Components.LimelightPID.calculate(Components.BL.getPosition(), dist);
                // if(power>0.5)
                // {
                //     power = 0.5;
                // }
                Components.CANBackLeft.set(power);
                Components.CANBackRight.set(power);
                Components.CANFrontLeft.set(power);
                Components.CANFrontRight.set(power);
                System.out.println("Going fowards"+Components.BL.getPosition());
                if(Components.BL.getPosition() >= dist){
                    Components.CANBackLeft.set(0);
                    Components.CANBackRight.set(0);
                    Components.CANFrontLeft.set(0);
                    Components.CANFrontRight.set(0);
                    System.out.println("Done going fowards");
                    Components.BL.setPosition(0);
                    Robot.AutoStep++;
                }
            }
        }

        public static Timer uptakeTimer = new Timer();
        public static void uptake(double ballNumber){
            //Possibly recalc shooting speed from distance
            Components.Uptake.set(Robot.uptakeSpeed);
            Components.Indexer1.set(Robot.indexerPower);
            Components.Indexer2.set(Robot.indexerPower);
            if(uptakeTimer.get() > 1*ballNumber){
                //stops Intake and Indexer
                Components.Uptake.set(0);
                Components.Indexer1.set(0);
                Components.Indexer2.set(0);
                //stops flywheel
                Components.CANShooter1.set(0);
                Components.CANShooter2.set(0);
                Robot.AutoStep++;
            }
        }

        static double turnPower = 0.2;
        public static void turn(double degrees, Boolean direction){
            if(direction){ //clockwise
                // turnPower = Components.LimelightPID.calculate(Components.gyro.getAngle(), degrees);
                Components.CANBackLeft.set(turnPower);
                Components.CANBackRight.set(-turnPower);
                Components.CANFrontLeft.set(turnPower);
                Components.CANFrontRight.set(-turnPower);
                if (degrees <= Components.gyro.getAngle()){
                    Components.CANBackLeft.set(0);
                    Components.CANBackRight.set(0);
                    Components.CANFrontLeft.set(0);
                    Components.CANFrontRight.set(0);
                    Components.BL.setPosition(0);
                    Robot.AutoStep++;
                }
            } else if(!direction){ //counterclockwise
                // turnPower = -Components.LimelightPID.calculate(Components.gyro.getAngle(), degrees);
                Components.CANBackLeft.set(-turnPower);
                Components.CANBackRight.set(turnPower);
                Components.CANFrontLeft.set(-turnPower);
                Components.CANFrontRight.set(turnPower);
                if (degrees >= Components.gyro.getAngle()){
                    Components.CANBackLeft.set(0);
                    Components.CANBackRight.set(0);
                    Components.CANFrontLeft.set(0);
                    Components.CANFrontRight.set(0);
                    Components.BL.setPosition(0);
                    Robot.AutoStep++;
                }
            }
        }
        // static double turnPower = 0.2;
        public static void LimelightTurnToAligned(){
            if (Robot.validTarget() && Components.XBController.getAButton()){
                // move the robot
                
                // turns the robot until the angle is 0
                if(Robot.tx.getDouble(0.0) > 2 ||Robot.tx.getDouble(0.0) < -2) {
                  if(Robot.tx.getDouble(0.0) < -2 ){
                    // move robot counterclockwise
                    // speed = -0.2;
                    System.out.println(Robot.tx.getDouble(0.0));
                    turnPower = -Components.LimelightPID.calculate(Robot.tx.getDouble(0.0), 0);
                    Components.CANFrontLeft.set(-turnPower);
                    Components.CANFrontRight.set(turnPower);
                    Components.CANBackLeft.set(-turnPower);
                    Components.CANBackRight.set(turnPower);
                    System.out.println("turning, Angle ="+Robot.tx.getDouble(0.0)+"speed"+turnPower);
                }
                else if(Robot.tx.getDouble(0.0) > 2){
                // speed = -0.2;
                System.out.println(Robot.tx.getDouble(0.0));
                turnPower = Components.LimelightPID.calculate(Robot.tx.getDouble(0.0), 0);
                Components.CANFrontLeft.set(turnPower);
                Components.CANFrontRight.set(-turnPower);
                Components.CANBackLeft.set(turnPower);
                Components.CANBackRight.set(-turnPower);
                System.out.println("turning, Angle ="+Robot.tx.getDouble(0.0)+"speed"+turnPower);
        
                // SmartDashboard.putData(Se);
                }
                }
                if(!(Robot.tx.getDouble(0.0)>2 ||Robot.tx.getDouble(0.0)<-2))
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
        // public static void forward(double go){
        //     double power = Components.pid.calculate(Components.BL.getPosition(), go);
        //     Components.CANBackLeft.set(power);
        //     Components.CANBackRight.set(power);
        //     Components.CANFrontLeft.set(power);
        //     Components.CANFrontRight.set(power);
        // }
        // public void turning(double go){
        //     double power = Components.pid.calculate(Components.BL.getPosition(), go);
        //     Components.CANBackLeft.set(-power);
        //     Components.CANBackRight.set(power);
        //     Components.CANFrontLeft.set(-power);
        //     Components.CANFrontRight.set(power);
        // }
        // public void strafing(double go){
        //     double power = Components.pid.calculate(Components.BL.getPosition(), go);
        //     Components.CANBackLeft.set(power);
        //     Components.CANBackRight.set(-power);
        //     Components.CANFrontLeft.set(power);
        //     Components.CANFrontRight.set(-power);
        // }
        // public static void move(double forward, double turning, double strafing){
        //     double bl = forward - turning + strafing;
        //     double br = forward + turning - strafing;
        //     double fl = forward - turning + strafing;
        //     double fr = forward + turning - strafing;

        //     double blspeed = Components.pid.calculate(Components.BL.getPosition(), bl);
        //     double brspeed = Components.pid.calculate(Components.BR.getPosition(), br);
        //     double flspeed = Components.pid.calculate(Components.FL.getPosition(), fl);
        //     double frspeed = Components.pid.calculate(Components.FR.getPosition(), fr);


        //     Components.CANBackLeft.set(blspeed);
        //     Components.CANBackRight.set(brspeed);
        //     Components.CANFrontLeft.set(flspeed);
        //     Components.CANFrontRight.set(frspeed);
        // }
        //         public static void TurnAngle(double deg, double speed, boolean direction)//Yes means turn right, no means turn left
        //     {
        //         Components.gyro.reset();;
        //         if(!direction)
        //         {
        //             speed=-speed;
        //         }

        //             /**
        //              * Clamps the motor powers while maintaining power ratios.
        //              * @param powers The motor powers to clamp.
        //              */
        //         while(Components.gyro.getAngle() < Math.toRadians(deg)) //I'm not sure if we are using a gyro or imu, but I think this will work to call it.
        //         //Otherwsie we can figure out degrees/count and use encoders.
        //         {
        //             //driving
        //             Components.CANFrontLeft.set(speed);
        //             Components.CANFrontRight.set(-speed);
        //             Components.CANBackLeft.set(speed);
        //             Components.CANBackRight.set(-speed);
        //         }
        //             //stopped
        //             Components.CANFrontLeft.set(0);
        //             Components.CANFrontRight.set(0);
        //             Components.CANBackLeft.set(0);
        //             Components.CANBackRight.set(0);
            
        //     }
        //         private static void clampPowers(List<Double> motors) {
        //         }

    }