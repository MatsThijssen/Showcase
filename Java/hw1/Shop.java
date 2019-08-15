import java.util.Scanner;

public class Shop { 
    public static void main(String[] args) { 
        
        
        int[][] sales = new int[5][9]; //Initialize array to store sales. Rows are nationalitites, columns are items
        
        inputData(sales);
        printSales(sales);
        
        System.out.println(); //For formatting
        
        int totPurchases = 0;
        int maxPerNation; //Holds most pop item pr nation
        int maxIndexNation;
        int nrItems = 0;
        int[] itemCount = new int[9]; //Array to count how much of each item was sold
        
        
        //Loop through data getting info
        for (int i=0;i<sales.length;i++){
            maxPerNation=0; //After loop through nation, set nation variables to 0
            maxIndexNation=0;
            totPurchases = 0;
            //Loop through every nation
            for (int j=0;j<sales[i].length;j++){ 
                totPurchases += sales[i][j]; //Add up total purchases (per nation)
                
                if (sales[i][j]>maxPerNation){ //Find most popular item per nation
                    maxPerNation=sales[i][j];
                    maxIndexNation=j;
                }
                itemCount[j] += sales[i][j]; //Increment count for item
                nrItems += sales[i][j]; //Keep track of total nr of items for later
            }
            
        System.out.print("Nationality " + i + " bought " + totPurchases + " items.");  
        System.out.println(" Their most popular items was item " + (maxIndexNation+1));
        
        }
        
        System.out.println(); //For formatting
        
        //Initialize variables to hold least and most popular
        int maxTot = 0;
        int minTot = nrItems; //For safety, initialize min to above max possibe purchases (here we need tot number)
        int maxIndex = 0;
        int minIndex = 0;
        //Loop through item count to present and find least and most popular.
        for (int i=0;i<itemCount.length;i++){
            System.out.println("Item " + (i+1) + " was purchased " + itemCount[i] + " times.");
            //Find max and min with if statements (most and least popular)
            if (itemCount[i]>maxTot){ 
                    maxTot = itemCount[i];
                    maxIndex = i;
                }
            if (itemCount[i]<minTot){
                    minTot = itemCount[i];
                    minIndex = i;
                }
        }
        
        System.out.println("The most popular item overall was: " + (maxIndex+1));
        System.out.println("The least popular item overall was: " + (minIndex+1));
        
        
        
        
    }    
    //Method to input data 
    public static void inputData(int[][] a){
        Scanner input = new Scanner(System.in);
        int nat; //stores nationality
        int item; //stores item
        
        int nrItems = input.nextInt(); //Get number of items, first integer in the list
        for (int i=0;i<nrItems;i++){ //Loop through all the items, placing them where they belong
            nat = input.nextInt(); //Get nationality and item for this line
            item = input.nextInt();
            a[nat][item]+=1; //Increment the sale of this item by this nationaility by one.
        }
    
    }
    public static void printSales(int[][] a){ //Method to print array
        System.out.println("Nationality\\item\t 1\t2\t3\t4\t5\t6\t7\t8\t9");
        
        for (int i=0;i<a.length;i++){
            System.out.print(i + "\t\t   \t"); //Weird, but for formatting
            
            for (int j=0;j<a[i].length;j++){
                System.out.print(a[i][j] +"\t");
            }
            
            System.out.println();
        }
    }
}