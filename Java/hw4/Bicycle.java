public class Bicycle extends GymMachine {
    protected double rpm;
    
    public Bicycle(){
        this(1.0);
    }
    
    public Bicycle(double rpm){
        super();
        this.rpm = rpm;
    }
    
    public double getCalories(){
        String tempTime = this.getWorkoutTime();
        double calTime = ((double) usedTime)/3600000;
        return 10.0*rpm * calTime;
    }
    
    public double getRpm(){
        return rpm;
    }
    
    public void setRpm(double rpm){
        this.rpm = rpm;
    }
    public void incRpm(){
        if (rpm<150)
            this.rpm++;
    }
    public void decRpm(){
        if (rpm>0)
            this.rpm--;
    }
    
    public String toString(){
        return "Rounds per minute: " + rpm;
    }
    
    public String show(){
        return "RPM: " + rpm + "\nWorkout Time: " + this.getWorkoutTime() + "\nCalories burned: " + String.format("%.4f",this.getCalories());
    }
}
   