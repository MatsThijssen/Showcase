import java.util.Date;

public class Review{
    //Instance Variables
    private String reviewerName;
    private String reviewedItem;
    private String type;
    private double stars;
    private String dateString;
    private long exactTime;
    
    //empty constructor
    Review(){
        reviewerName = "NoName";
        reviewedItem = "None";
        type = "None";
        stars = 0;
        Date date = new Date();
        dateString = date.toString();
        exactTime = date.getTime();
    }
    //Constructor
    Review(String name, String item, String type, double stars){
        reviewerName = name;
        reviewedItem = item;
        this.type = type;
        this.stars = stars;
        Date date = new Date();
        dateString = date.toString();
        exactTime = date.getTime();
    }
    
    //Getters
    
    public String getReviewer(){
        return reviewerName;
    }
    
    public String getReviewed(){
        return reviewedItem;
    }
    
    public String getType(){
        return type;
    }
    
    public String getDate(){
        return dateString;
    }
    
    public double getStars(){
        return stars;
    }
    
    public long getExactTime(){
        return exactTime;
    }
    
    //Setters
    
    public void setReviewer(String name){
        reviewerName = name;
    }
    
    public void setReviewed(String item){
        reviewedItem = item;
    }
    
    public void setType(String type){
        this.type = type;
    }
    
    public void setStars(double stars){
        this.stars = stars;
    }
    
    //toString method, returns everything stored.
    public String toString(){
        //return reviewerName + " gave " + reviewedItem + ", a " + type + ", a rating of " + stars + " stars on " + dateString;
        return reviewerName + "\n" + reviewedItem + " (" + type + ")\nStars: " + stars + "\nDate: " + dateString; 
    }
    
    
    
    
    
    
    

}