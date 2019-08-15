public class FlexArray{
	private int length;
	private int numUsed;
	private Review[] storage; 
	private Review[] newStorage;

	
	
	FlexArray(){
                this.length = 1;
		storage = new Review[1];
		numUsed = 0;
	}
	
	FlexArray(int length){
                this.length = length;
		storage = new Review[length];
		numUsed = 0;
	}

	public void insert(Review elem){
		if (numUsed<length){
			storage[numUsed] = elem;
			numUsed++;
		}
		else{
			length*=2;
			newStorage = new Review[length];
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
	
	public Review getItem(int i){
		return storage[i];
	}
	
	public void delete(int i){
            storage[i] = storage[numUsed-1];
            numUsed--;
	}

}
