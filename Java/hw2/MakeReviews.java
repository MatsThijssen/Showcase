import java.util.Scanner;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class MakeReviews{
    public static void main(String[] args) throws IOException{
        //Initialize some variables
        FlexArray reviewArray = new FlexArray();
        String reviewer;
        String reviewed;
        String type;
        double stars;
  
        //Get input from text file, always in order String, String, String, double. Insert input into Review object
        // in array.
        Scanner sc = new Scanner(new File("reviews.txt"));
        while (sc.hasNext()){
            reviewer = sc.nextLine();

            reviewed = sc.nextLine();

            type = sc.nextLine();

            stars = sc.nextDouble();

            sc.nextLine(); //Need to go to the next line, so this just skips a blank.
            
            reviewArray.insert(new Review(reviewer,reviewed,type,stars));
            //Uncomment this to make mostRecent work as expected.
            /* 
            try{
            TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e){}
            */
        }// end while

        sc.close();
        
        //Print first and last Review in array.
        System.out.println("First review:");
        System.out.println(reviewArray.getItem(0));
        System.out.println("\nLast review:");
        System.out.println(reviewArray.getItem(reviewArray.getUsed()-1));
        
        System.out.println("\nThe most popular kind of review is: " + mostPopular(reviewArray));
        System.out.printf("The average number of stars for all hotels is: %.2f \n", avgStars(reviewArray, "hotel"));
        System.out.println("\nThe most recent hotel review is:\n" + mostRecent(reviewArray,"hotel"));
        System.out.println("\nThe most recent restaurant review is:\n" + mostRecent(reviewArray,"restaurant"));
        System.out.println("\nThe most recent movie review is:\n" + mostRecent(reviewArray,"movie"));
        
        
        deleteName(reviewArray,"Michael");
        System.out.printf("\nThe average number of stars for all hotels, without Michael, is: %.2f \n", avgStars(reviewArray, "hotel"));
        
        System.out.println("\nThe reviewer who has reviewed the most is: " + mostReviewer(reviewArray));
    } //end main 

    //method to get most popular item. Assumes only three items, no deviation.
    public static String mostPopular(FlexArray arr){
    
        int hotel = 0;
        int restaurant = 0;
        int movie = 0;
        
        for (int i=0;i<arr.getUsed(); i++){
        
            Review review = arr.getItem(i);
            String type = review.getType();
            
            if (type.equals("hotel"))
                hotel++;
            else if (type.equals("restaurant"))
                restaurant++;
            else
                movie++;
            
        }
        //Have to check all cases, also take into account we could have equal amount of two or all three.
        if (hotel>restaurant){
            if (hotel>movie) return "hotel";
            else if (hotel==movie) return "movie and hotel";
            else return "movie";
        }
        else if (hotel==restaurant){
            if (hotel>movie) return "hotel and restaurant";
            else if (hotel==movie) return "restaurant, movie, and hotel";
            else return "movie";
        }
        else{
            if (restaurant>movie) return "restaurant";
            else if (restaurant==movie) return "restaurant and movie";
            else return "movie";
        }
    } //end mostPopular 
    
    //Method to get avg number of stars for given type
    public static double avgStars(FlexArray arr, String typeWanted){
    
        double starSum=0;
        int totHotels=0;
        
        for (int i=0; i<arr.getUsed();i++){
        
            Review review = arr.getItem(i);
            String type = review.getType();
            
            if (type.equals(typeWanted)){
                totHotels++;
                starSum += review.getStars();
            }
        }
        return starSum/totHotels;
    
    }//end avgStars
    
    //Method to get most recent review for specific type
    //If fast computer, this will give different results every time as multiple reviews are created within 
    // machine precision of the same time.
    //reviews have stored in them a long of ms since January 1, 1970, 00:00:00 GMT. This is "ExactTime".
    public static Review mostRecent(FlexArray arr, String typeWanted){
    
        long recent = 0; 
        int mostRecentReview = 0;
        
        for (int i=0; i<arr.getUsed();i++){
        
            Review review = arr.getItem(i);
            String type = review.getType();
            
            if (type.equals(typeWanted)){
            
                if (review.getExactTime()>recent){
                    recent = review.getExactTime();
                    mostRecentReview = i;
                }
            }
        }
        
        return arr.getItem(mostRecentReview);
    }//end mostRecent 
    
    //Method to delete reviews by given name
    public static void deleteName(FlexArray arr, String name){
    
        int i = 0;
        
        while (i<arr.getUsed()){ //Use .getUsed() since number of used items is not constant.
        
            Review review = arr.getItem(i);
            String reviewer = review.getReviewer();
            
            if (reviewer.equals(name)) 
                arr.delete(i);
            else //Only increment if not found because of the copy from last to "hole"
                i++; 
            
        }//end while
    
    }//end deleteName 

    //Method to find who did most reviews
    /*
    To do this, I create two extra classes: FlexArrays for Strings and Ints. The string arrays hold the reviername,
    and the int arrays hold the corresponding nr of reviews. We loop through the array of reviews, and check each name.
    If the name is already stored in the string array, increment its corresponding counter by one. If not, 
    insert the name in the array and insert a "1" into its counter. After looping through array of reviews and getting
    all the data, loop through counter to find the max.
    */
    public static String mostReviewer(FlexArray arr){
    
        FlexStringArray reviewers = new FlexStringArray();
        FlexIntArray reviewCount = new FlexIntArray();
        boolean found;
        int max=0;
        String mostReviewerName = "None";
        
        for (int i=0; i<arr.getUsed();i++){
        
            Review review = arr.getItem(i);
            String reviewer = review.getReviewer();
            found = false;
            
            for (int j=0;j<reviewers.getUsed();j++){
            
                if (reviewers.getItem(j).equals(reviewer)){
                    reviewCount.increment(j);
                    found = true;
                }//end if
            }//end j-for
            
            if (!found){
                reviewers.insert(reviewer);
                reviewCount.insert(1);
            }//end if
            
        }//end i-for
        
        for (int k=0;k<reviewCount.getUsed();k++){
        
            if (reviewCount.getItem(k)>max){
                max = reviewCount.getItem(k);
                mostReviewerName = reviewers.getItem(k);
            }
        }
        
        return mostReviewerName;
    }// end mostReviewer
    
    
    
    
    
    
    
    
    
    



}