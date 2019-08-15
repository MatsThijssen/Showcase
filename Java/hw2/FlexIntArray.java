public class FlexIntArray{
	private int length;
	private int numUsed;
	private int[] storage; 
	private int[] newStorage;

	
	
	FlexIntArray(){
                this.length = 1;
		storage = new int[1];
		numUsed = 0;
	}
	
	FlexIntArray(int length){
                this.length = length;
		storage = new int[length];
		numUsed = 0;
	}

	public void insert(int elem){
		if (numUsed<length){
			storage[numUsed] = elem;
			numUsed++;
		}
		else{
			length*=2;
			newStorage = new int[length];
			for (int i=0;i<length/2;i++){
				newStorage[i]=storage[i];
			}
			storage=newStorage;
			storage[numUsed]=elem;
			numUsed++;
		}
		
	}

	public int getLength(){
		return length;
	}	
	
	public int getUsed(){
		return numUsed;
	}
	
	public int getItem(int i){
		return storage[i];
	}
	
	public void delete(int i){
            storage[i] = storage[numUsed-1];
            numUsed--;
	}
	public void increment(int i){
            storage[i]+=1;
	}

}