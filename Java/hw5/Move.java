/**
 *  a move represents a row and col location on the board
 */
public class Move {
   private int row, col;

   public Move(){ // noarg constructor, does nothing
   }

   public Move(int r, int c){ // other constructor with args
      row = r;                // when all info known
      col = c;
   }

   public int getRow(){
       return row;
   }

   public int getCol(){
       return col;
   }

   public void setRow(int r){
       this.row = r;
   }

   public void setCol(int c){
       this.col = c;
   }

   public void setRowCol(int r, int c){
       this.row = r;
       this.col = c;
   }

   public String toString(){
      return " row " + row + " col " + col;
   }
        
}
