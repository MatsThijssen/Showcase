import java.util.Scanner;

public class MergeArrays { 
    public static void main(String[] args) { 
        
        int[] array1 = inputArray();
        int[] array2 = inputArray();
        printArray(array1,5);
        printArray(array2,5);
        int[] mergedArray = mergeArrays(array1,array2);
        printArray(mergedArray,5);
        
            
        
    } 
    //Get input to make array. First length, then elements.
    public static int[] inputArray(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the length of the array (integer): ");
        int arrayLength = input.nextInt();
        int[] array = new int[arrayLength];
        
        System.out.println("Enter array elements one by one, in sorted order (integer): ");
        for (int i=0;i<array.length;i++)
            array[i]=input.nextInt();
        return array;
    }
    
    public static int[] mergeArrays(int[] a1, int[] a2){
    
        int[] mergedArray = new int[(a1.length + a2.length)];
        int i = 0, j = 0, k=0;
        //Loop through arrays until all elements from one has been added.
        while (i<a1.length && j<a2.length){
            //If current element in a2 is smallest, add to merged array and increment j and k
            if (a1[i]>a2[j])
                mergedArray[k++] = a2[j++];
            //If a1 has smallest element, add that instead. This also handles the case of equal elements.
            else
                mergedArray[k++] = a1[i++];
        }
        //If initial arrays of different size, need to finish one of them off. That's done here:
        System.out.println(i +""+  j);
        while (i<a1.length)
            mergedArray[k++] = a1[i++];
        while (j<a2.length)
            mergedArray[k++] = a2[j++];
        return mergedArray;
    }
    
    
    //PrintArray method from class.
    public static void printArray(int[] a, int n){
        int count = 0; 

        System.out.println("The array is:");

        for (int i=0; i<a.length; i++){
         System.out.print(a[i] + "  " );
         count++;
         if (count >= n) { // need <CR> and reset counter
            System.out.println(); 
            count = 0;
         }
        }
      
        System.out.println();
   }

}