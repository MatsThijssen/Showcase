public class Stepper extends GymMachine {
    protected int level;
    
    public Stepper(){
        this(1);
    }
    
    public Stepper(int level){
        super();
        this.level = level;
    }
    
    public double getCalories(){
        String tempTime = this.getWorkoutTime();
        double calTime = ((double) usedTime)/3600000;
        return 100.0*level * calTime;
    }
    
    
    public int getLevel(){
        return level;
    }
    
    public void setLevel(int level){
        this.level = level;
    }
    public void incLevel(){
        if (level<10)
            this.level++;
    }
    public void decLevel(){
        if (level>0)
            this.level--;
    }
    
    public String toString(){
        return "Level: " + level;
    }
    
    public String show(){
        return "Level: " + level + "\nWorkout Time: " + this.getWorkoutTime() + "\nCalories burned: " + String.format("%.4f",this.getCalories());
    }
    
}