    package frc.robot;

    import java.util.Arrays;
    import java.util.Collections;
    import java.util.List;

    import edu.wpi.first.wpilibj.AnalogGyro;
    import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import frc.robot.Mecanum;

    public class Autonomous {

            

        static double Leftpower = 0.2; 
        static double RIghtpower = 0.2; 
        public static void drive(double dist){
            //power = Components.pid.calculate(Components.BL.getPosition(), dist);
            if(dist<0){ //backwards
                // Leftpower = -Components.TranslationalPID.calculate(Components.BL.getPosition(), dist);
                // RIghtpower = -Components.TranslationalPID.calculate(Components.BR.getPosition(), dist);
                RIghtpower = Leftpower = 0.25;
                if(Leftpower<-0.5)
                {
                    Leftpower = -0.5;
                }
                if(RIghtpower<-0.5)
                {
                    RIghtpower = -0.5;
                }
                    Components.CANBackLeft.set(-Leftpower);
                    Components.CANBackRight.set(-RIghtpower);
                    Components.CANFrontLeft.set(-Leftpower);
                    Components.CANFrontRight.set(-RIghtpower);
                    System.out.println("Going backwards: Left Pos:"+ Components.BL.getPosition()+"Left Speed: "+ Leftpower+"RightPos: "+Components.BR.getPosition()+"RightSpeed " + RIghtpower);
                    if((Components.BL.getPosition() <= dist)&&(Components.BR.getPosition() <= dist)){
                    Components.CANBackLeft.set(0);
                    Components.CANBackRight.set(0);
                    Components.CANFrontLeft.set(0);
                    Components.CANFrontRight.set(0);
                    System.out.println("Done going backwards");
                    Components.BL.setPosition(0);
                    Components.BR.setPosition(0);
                    Robot.AutoStep++;
                }

            }
            else if(dist>0) //fowards 
            {
                // RIghtpower = Leftpower = 0.25;
                Leftpower = Components.TranslationalPID.calculate(Components.BL.getPosition(), dist);
                RIghtpower = Components.TranslationalPID.calculate(Components.BR.getPosition(), dist);
                if(Leftpower>0.4)
                {
                    Leftpower = 0.4;
                }
                if(RIghtpower>0.4)
                {
                    RIghtpower = 0.4;
                }
                Components.CANBackLeft.set(Leftpower);
                Components.CANBackRight.set(RIghtpower);
                Components.CANFrontLeft.set(Leftpower);
                Components.CANFrontRight.set(RIghtpower);
                System.out.println("Going fowards: Left Pos:"+ Components.BL.getPosition()+"Left Speed: "+ Leftpower+"RightPos: "+Components.BR.getPosition()+"RightSpeed " + RIghtpower);
                if(Components.BL.getPosition() >= dist&&(Components.BR.getPosition() >= dist)){
                    Components.CANBackLeft.set(0);
                    Components.CANBackRight.set(0);
                    Components.CANFrontLeft.set(0);
                    Components.CANFrontRight.set(0);
                    System.out.println("Done going fowards");
                    Components.BL.setPosition(0);
                    Components.BR.setPosition(0);
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
        static double GyroFactor = 20000;
        // static double GyroFactor = 1;
        public static void turn(double degrees, Boolean direction){
            if(direction){ //clockwise
                System.out.println("Turning clockwise Degrees ="+Components.gyro.getAngle()*GyroFactor);
                // turnPower = Components.LimelightPID.calculate(Components.gyro.getAngle(), degrees);
                Components.CANBackLeft.set(turnPower);
                Components.CANBackRight.set(-turnPower);
                Components.CANFrontLeft.set(turnPower);
                Components.CANFrontRight.set(-turnPower);
                if (Components.gyro.getAngle()*GyroFactor <=degrees){
                    Components.CANBackLeft.set(0);
                    Components.CANBackRight.set(0);
                    Components.CANFrontLeft.set(0);
                    Components.CANFrontRight.set(0);
                    Components.BL.setPosition(0);
                    Robot.AutoStep++;
                }
            } else if(!direction){ //counterclockwise
                System.out.println("Turning counterclockwise Degrees ="+Components.gyro.getAngle()*GyroFactor);
                // turnPower = -Components.LimelightPID.calculate(Components.gyro.getAngle(), degrees);
                Components.CANBackLeft.set(-turnPower);
                Components.CANBackRight.set(turnPower);
                Components.CANFrontLeft.set(-turnPower);
                Components.CANFrontRight.set(turnPower);
                if (Components.gyro.getAngle()*GyroFactor >= degrees){
                    System.out.println("Done turning");
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
                    turnPower = Components.LimelightPID.calculate(Robot.tx.getDouble(0.0), 0);
                    Components.CANFrontLeft.set(-turnPower);
                    Components.CANFrontRight.set(turnPower);
                    Components.CANBackLeft.set(-turnPower);
                    Components.CANBackRight.set(turnPower);
                    System.out.println("turning, Angle ="+Robot.tx.getDouble(0.0)+"speed"+turnPower);
                }
                else if(Robot.tx.getDouble(0.0) > 2){
                // speed = -0.2;
                System.out.println(Robot.tx.getDouble(0.0));
                turnPower = -Components.LimelightPID.calculate(Robot.tx.getDouble(0.0), 0);
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