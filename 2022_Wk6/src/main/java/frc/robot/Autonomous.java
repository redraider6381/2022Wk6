    package frc.robot;

    import java.util.Arrays;
    import java.util.Collections;
    import java.util.List;

    import edu.wpi.first.wpilibj.AnalogGyro;
    import edu.wpi.first.wpilibj.Timer;
    import frc.robot.Mecanum;

    public class Autonomous {

            

        static double power = 0.2; 
        public static void drive(double dist){
            //power = Components.pid.calculate(Components.BL.getPosition(), dist);
            if(dist<Components.BL.getPosition()){ //backwards
                    Components.CANBackLeft.set(-power);
                    Components.CANBackRight.set(-power);
                    Components.CANFrontLeft.set(-power);
                    Components.CANFrontRight.set(-power);
                if(Components.BL.getPosition() < dist){
                    Components.CANBackLeft.set(0);
                    Components.CANBackRight.set(0);
                    Components.CANFrontLeft.set(0);
                    Components.CANFrontRight.set(0);
                    Robot.AutoStep++;
                }

            }
            else if(dist>Components.BL.getPosition()) //fowards 
            {
                Components.CANBackLeft.set(power);
                Components.CANBackRight.set(power);
                Components.CANFrontLeft.set(power);
                Components.CANFrontRight.set(power);
                if(Components.BL.getPosition() > dist){
                    Components.CANBackLeft.set(0);
                    Components.CANBackRight.set(0);
                    Components.CANFrontLeft.set(0);
                    Components.CANFrontRight.set(0);
                    Robot.AutoStep++;
                }
            }
        }

        public static Timer uptakeTimer = new Timer();
        public static void uptake(double ballNumber){
            Components.Uptake.set(Robot.uptakeSpeed);
            Components.Indexer1.set(Robot.indexerPower);
            Components.Indexer2.set(Robot.indexerPower);
            if(uptakeTimer.get() > 1.5*ballNumber){
                Components.Uptake.set(0);
                Components.Indexer1.set(0);
                Components.Indexer2.set(0);    
            }
        }

        static double turnPower = 0.2;
        public static void turn(double degrees){
            if(degrees > Components.gyro.getAngle()){
                Components.CANBackLeft.set(turnPower);
                Components.CANBackRight.set(-turnPower);
                Components.CANFrontLeft.set(turnPower);
                Components.CANFrontRight.set(-turnPower);
                if (degrees < Components.gyro.getAngle()){
                    Components.CANBackLeft.set(0);
                    Components.CANBackRight.set(0);
                    Components.CANFrontLeft.set(0);
                    Components.CANFrontRight.set(0);
                }
            } else if(degrees < Components.gyro.getAngle()){
                Components.CANBackLeft.set(-turnPower);
                Components.CANBackRight.set(turnPower);
                Components.CANFrontLeft.set(-turnPower);
                Components.CANFrontRight.set(turnPower);
                if (degrees > Components.gyro.getAngle()){
                    Components.CANBackLeft.set(0);
                    Components.CANBackRight.set(0);
                    Components.CANFrontLeft.set(0);
                    Components.CANFrontRight.set(0);
                }
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