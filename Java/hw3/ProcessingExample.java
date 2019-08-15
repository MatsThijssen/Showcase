/**
 * 
 * @author:      Mats Thijssen
 * @since:       Apr.21 2018
 * 
 * A small game that lets user adjust angle and power of a cannon with arrow keys. When ctrl is hit,
 * projectile fires. Goal is to hit target. Uses some projectile motion physics to model gravity for projectile. (Not in a earth-physics way, but with similar model). Note that projectile dies when at boundary, thus will not come back down if shot up outside of screen.
 * 
 */

import processing.core.PApplet;

public class ProcessingExample extends PApplet {	
	//dimensions of the canvas
	int xMax = 500;
	int yMax = 300;
	
	float angle = 0.0f;
	final float PI = 3.14159f;
	//initial position of the cannon
	float x4 = (float) 50;
	float y4 = (float) 250;
	float x3 = (float) (50 + 100*Math.cos(angle));
	float y3 = (float) (250-100*Math.sin(angle));
	float x2 = (float) (x3 - 30*Math.sin(angle));
	float y2 = (float) (y3-30*Math.cos(angle));
	float x1 = (float) (50-30*Math.sin(angle));
	float y1 = (float) (250-30*Math.cos(angle));
	
        Projectile bullet;
        
	int PMx = 30;
	int PMy = 50;
	
	boolean bulletExists = false;
	int power = 0;
	
	
	public void setup() {
		size(xMax,yMax);	
	}
	
	public void draw() {	
		//redraw the background in each iteration of the draw method
		background(127,127,127);
		textSize(10);
		fill(0,0,100);
		textAlign(CENTER);
		text("Use arrow keys (Left-Right) to adjust canon position.\n ", 250, 10 );
		text("Adjust power with Up-Down keys. Fire with CTRL. Try to hit target.\n",250,20);
		
		//Draw target to hit
		fill(153,0,0);
		rect(450,100,20,20);
		
		//Cannon coordinates
		x4 = (float) 50;
                y4 = (float) 250;
                x3 = (float) (50 + 100*Math.cos(angle));
                y3 = (float) (250-100*Math.sin(angle));
                x2 = (float) (x3 - 30*Math.sin(angle));
                y2 = (float) (y3-30*Math.cos(angle));
                x1 = (float) (50-30*Math.sin(angle));
                y1 = (float) (250-30*Math.cos(angle));
                    
		fill(255,0,0);
		quad(x1, y1, x2, y2, x3, y3, x4, y4); //Drawing of cannon
		
		//Drawing of power-meter
		if (power>0){
                    fill(255,0,0);
                    rect(PMx,PMy,20,20);
                    if (power>1){
                        fill(255,128,0);
                        rect(PMx,PMy+20,20,20);
                        if (power>2){
                            fill(255,255,0);
                            rect(PMx,PMy+40,20,20);
                            if (power>3){
                                fill(128,255,0);
                                rect(PMx,PMy+60,20,20);
                                if (power>4){
                                    fill(0,255,0);
                                    rect(PMx,PMy+80,20,20);
                                
                                }
                            }
                        }
                    }
		}
//		//Adjust angle of cannon and power of fire.

		if ( keyPressed && key == CODED )
		{
			if ( keyCode == LEFT && angle<PI/2 - 0.05f)
				angle+=0.05;
				
			else if (keyCode == RIGHT && angle>0.04)
				angle-=0.05;
			if (keyCode == UP){
				if (power>=0 && power<5)
                                    power++;
                                    delay(100);
                        }
			else if (keyCode == DOWN ){
				if (power>0 && power<=5)
                                    power--;
                                    delay(100);
			}
			if (keyCode == CONTROL && !bulletExists){ //Only allow new projectile when none on screen.
                            bullet =  new Projectile(this,angle, power);
                            
                            bulletExists = true;
                        }
                        
                        
				
		}
		if (bulletExists){
                    bullet.move();
                    //Keep moving until it's off screen. 
                        
                }
		
		
		if (bulletExists && bullet.hit()){
                    textSize(18);
                    fill(0,0,0);
                    textAlign(CENTER);
                    text("You hit the target!\n", 250, 200 );
		}
		if (bulletExists && !bullet.isMoving()){
                    bulletExists = false;
		}
		
		
	}

}

