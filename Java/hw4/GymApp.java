/**
 * 
 * @author:      Mats Thijssen
 * @since:       Apr.22 2018
 * 
 * Gym App letting user control start/stop and intensity of each apparatus. Controls are printed on screen. Note that the calculated calories is wrong if adjusting 
 * intensity after starting workout as it assumes same intensity throughout workout. This can be fixed, but I left it like this for now.
 * 
 */


import processing.core.PApplet;
import java.util.ArrayList;
public class GymApp extends PApplet {	
    //dimensions of the canvas
    int xMax = 1500;
    int yMax = 400;
    ArrayList<GymMachine> machines; //List to hold machines


    public void setup() {
        size(xMax,yMax);
        machines = new ArrayList<GymMachine>(3); //Initialize to 3 
        //Add all three
        TreadMill TM = new TreadMill();
        Stepper ST = new Stepper();
        Bicycle BC = new Bicycle();
        
        machines.add(TM);
        machines.add(ST);
        machines.add(BC);

    }

    public void draw() {	
        //redraw the background in each iteration of the draw method
        background(64,64,64);
        stroke(255,255,255);
        //Lines to seperate each thing.
        line(xMax/3,0,xMax/3,yMax);
        line(2*xMax/3,0,2*xMax/3,yMax);
        
        
        //Titles
        textSize(24);
        textAlign(CENTER);
        fill(255,0,0);
        text("Treadmill",xMax/6,50);
        text("Stepper",xMax/2,50);
        text("Bicycle",5*xMax/6,50);
        //Info on how to change intensity
        textSize(16);
        text("Q/A to inc/dec incline, W/S to inc/dec mph", xMax/6, 200);
        text("T/G to inc/dec level", xMax/2, 200);
        text("I/K to inc/dec rpm", 5*xMax/6, 200);
        
        //Draw machines, gets their info from show(). Also add "buttons": start/stop and restart.
        for (int i=0;i<machines.size();i++){
            fill(255,0,0);
            textSize(24);
            text(machines.get(i).show(),xMax/6 + i* xMax/3, 300);
            fill(0,204,0);
            rect(i*xMax/3 + 10, 120,230,50);
            fill(0,0,0);
            textSize(18);
            text("Start/Stop", i*xMax/3 + 130,145);
            fill(255,128,0);
            rect(i*xMax/3 + 260, 120,230,50);
            fill(0,0,0);
            text("Restart",i*xMax/3 + 380,145);
        }
        
        //Check for mouse press inside button coordinates. If so, do appropriate action.
        if (mousePressed){
            for (int i=0; i<machines.size();i++){
                if (mouseX<(i*xMax/3 + 240) && mouseX>(i*xMax/3 +10) && mouseY<170 && mouseY>120){
                    if (!machines.get(i).isWorking()){
                        machines.get(i).startWorkout();
                        delay(200);
                    }
                    else{
                        machines.get(i).endWorkout();
                        delay(200);
                    }
                }
                if (mouseX<(i*xMax/3 + 490) && mouseX>(i*xMax/3 +260) && mouseY<170 && mouseY>120){
                    machines.get(i).endWorkout();
                    machines.get(i).reStartWorkout();
                    delay(200);
                
                }
            }
        }//end if
        //Check for key press, adjust levels accordingly.
        if (keyPressed){
            if (key == 'Q' || key == 'q'){
                machines.get(0).incAng();
                delay(100);
            }
            else if (key == 'A' || key == 'a'){
                machines.get(0).decAng();
                delay(100);
            }
            else if (key == 'W' || key == 'w'){
                machines.get(0).incMph();
                delay(100);
            }
            else if (key == 'S' || key == 's'){
                machines.get(0).decMph();
                delay(100);
            }
            else if (key == 'T' || key == 't'){
                machines.get(1).incLevel();
                delay(100);
            }
            else if (key == 'G' || key == 'g'){
                machines.get(1).decLevel();
                delay(100);
            }
            else if (key == 'I' || key == 'i'){
                machines.get(2).incRpm();
                delay(100);
            }
            else if (key == 'K' || key == 'k'){
                machines.get(2).decRpm();
                delay(100);
            }
        }//end if
        
   


    }//end draw()

}//End class

