

public class GymMachine {
    protected long startTime;
    protected long endTime;
    protected long totTime;
    protected boolean isWorking = false;
    protected long usedTime = 0;
    protected long breakTime = 0;
    protected boolean hasWorked = false;
    
    public GymMachine(){
        
    }
    //This starts workout, both initially and after stop
    public void startWorkout(){
        if (hasWorked){
            breakTime += System.currentTimeMillis() - endTime;
        }
        else
            startTime = System.currentTimeMillis();
        isWorking = true;
    }
    //restarts workout from 0 seconds. Can be used initially or whenever.
    public void reStartWorkout(){
        startTime = System.currentTimeMillis();
        isWorking = true;
    }
    //stops (pauses) workout.
    public void endWorkout(){
        endTime = System.currentTimeMillis();
        if (hasWorked){
            totTime = endTime - startTime - breakTime;
        }
        else
            totTime = endTime - startTime;
        isWorking = false;
        hasWorked = true;
    }
    
    public String getWorkoutTime(){
        if (isWorking){
            usedTime = System.currentTimeMillis() - this.startTime - breakTime;
        } 
        else {
            usedTime = totTime;
        }
        long hours = usedTime/3600000;
        long minutes = (usedTime - 3600000*hours)/60000;
        long seconds = (usedTime - 3600000*hours -  60000 * minutes)/1000;
        return hours + ":" + minutes + ":" + seconds;
    }
    public String getCurrentTime(){
        long timeNow = System.currentTimeMillis();
        long hours = timeNow/3600000;
        long minutes = (timeNow - 3600000*hours)/60000;
        long seconds = (timeNow - 3600000*hours -  60000 * minutes)/1000;
        return "Current Time: " + hours + ":" + minutes + ":" + seconds;
    }
    public String show(){
        return "Workout Time: " + getWorkoutTime();
    }
    //Bunch of functions that must be here to work in app itself, are all overriden, but array is declared with elements of GymMachine type.
    public void incAng(){}
    public void decAng(){}
    public void incMph(){}
    public void decMph(){}
    public void incLevel(){}
    public void decLevel(){}
    public void incRpm(){}
    public void decRpm(){}
    
    
    public boolean isWorking(){
        return isWorking;
    }
}