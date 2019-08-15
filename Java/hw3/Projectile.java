
import processing.core.PApplet;

public class Projectile {
	protected float x;
	protected float y;
	final float PI = 3.14159f;
	protected float xSpeed;
	protected float ySpeed;
	protected float g = 2.81f;
	PApplet canvas;

	protected int col;
	protected float red=0, green=0, blue=0;
	private boolean isMoving = false;
	private boolean hitTarget = false;
	protected float r = 10.0f;

	
	public Projectile ( PApplet canvas, double angle, int power )
	{
		this.canvas = canvas;
		x = (float) (50 + 100*Math.cos(angle) - 15*Math.sin(angle));
		y = (float) (250 - 100* Math.sin(angle) - 15*Math.cos(angle));
		
		xSpeed = (float) (power * 10 * Math.cos(angle));
		ySpeed = (float) (-power * 10 * Math.sin(angle));

		
		col = canvas.color(255, 51, 255);
		
	}
	
	
	public void move() {
		// update position based on speed, in accordance with simple gravitational physics
		float timeStep = 1/6.0f;
		x += xSpeed * timeStep;
		y += ySpeed * timeStep + 0.5*g*Math.pow(timeStep,2);
		ySpeed+=g*timeStep;

		
		col = canvas.color(255, 51, 255);
		canvas.fill(col);
		//If hit target, then let fall down.
		if (Math.pow(x-450,2)+Math.pow(y-100,2)<200){
                    xSpeed = 0;
                    ySpeed = 3;
                    hitTarget = true;
		}
		
		
		// Only draw if within canvas. Thus letting it "fly off". set moving to true
		if (x<canvas.width && y<canvas.height && x>0 && y>0){
                    this.canvas.ellipse(x, y, 10, 10);
                    isMoving = true;
                }
                //if outside, moving is false.
                else 
                    isMoving = false;
                
	}
	public boolean isMoving(){
            return isMoving; //Let other program know whether projectile is still moving.
	}
	
	
	public boolean hit(){
            return hitTarget;
	}
}
