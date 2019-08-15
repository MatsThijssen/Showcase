

public class TreadMill extends GymMachine {
    protected int angle;
    protected double mph;
    
    public TreadMill(){
        this(1,1.0);
    }
    
    public TreadMill(int angle, double mph){
        super();
        this.angle = angle;
        this.mph = mph;
    }
    
    public double getCalories(){
        String tempTime = this.getWorkoutTime();
        double calTime = ((double) usedTime)/3600000;
        return (20.0*mph + 15.0*angle) * calTime;
    }
    
    
    public int getAngle(){
        return angle;
    }
    
    public double getMph(){
        return mph;
    }
    
    public void setAngle(int angle){
        this.angle = angle;
    }
    
    public void setMph(double mph){
        this.mph = mph;
    }
    public void incAng(){
        if (angle<15)
            this.angle++;
    }
    public void incMph(){
        if (mph<15.0)
            this.mph++;
    }
    public void decAng(){
        if (angle>0)
            this.angle--;
    }
    public void decMph(){
        if (mph>0.0)
            this.mph--;
    }
    
    public String toString(){
        return "Angle of Inclination: " + angle + ", mph: " + mph;
    }
    public String show(){
        return "Incline and mph: " + angle +", " + mph + "\nWorkout Time: " + this.getWorkoutTime() + "\nCalories burned: " + String.format("%.4f",this.getCalories());
    }
    
}