public class FlexStringArray{
	private int length;
	private int numUsed;
	private String[] storage; 
	private String[] newStorage;

	
	
	FlexStringArray(){
                this.length = 1;
		storage = new String[1];
		numUsed = 0;
	}
	
	FlexStringArray(int length){
                this.length = length;
		storage = new String[length];
		numUsed = 0;
	}

	public void insert(String elem){
		if (numUsed<length){
			storage[numUsed] = elem;
			numUsed++;
		}
		else{
			length*=2;
			newStorage = new String[length];
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
	
	public String getItem(int i){
		return storage[i];
	}
	
	public void delete(int i){
            storage[i] = storage[numUsed-1];
            numUsed--;
	}

}