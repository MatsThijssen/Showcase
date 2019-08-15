import java.util.Random;

public class ComputerPlayer extends Player {
    // create a random number generator
    // this is needed because the computer's first move is random
    Random rand = new Random();
 // --------------------------------------------

    // constructor
    public ComputerPlayer(char token) {
        super(token);
    }

 // --------------------------------------------
    // generate a Move
    /** Determines AI strategy. Will never lose. Draws if human plays perfect as well. Play edge as second player to lose.   */
    @Override
    public Move generateMove(Board ticTacToeBoard) {
        // create a Move object
        Move m = new Move();

        // is the board empty? For perfect player, corner or center is equivalent. Will go with center.
        if (ticTacToeBoard.isEmpty()) {
            
            m.setRow(1);
            m.setCol(1);

            return m;
        }
        // otherwise we have to pick a spot (row and col)  for our next move
        else {
            
            // define our token
            char myToken = getToken();
            char otherToken;
            //for generality
            if (myToken == 'X') otherToken='O';
            else otherToken = 'X';
            
            char[][] grid = ticTacToeBoard.getGrid();
            char[][] tranGrid = new char[3][3]; //transpose of grid matrix
            for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                tranGrid[i][j]=grid[j][i];
            }
            }
            //Check winning pos
            //define diagonals
            char[] diag = {grid[0][0],grid[1][1],grid[2][2]};
            char[] diag2 = {grid[2][0],grid[1][1],grid[0][2]};
            //check diagonols first
            if (WhereToPlace(diag, myToken)<3){
                m.setRowCol(WhereToPlace(diag, myToken),WhereToPlace(diag, myToken)); return m;
            }
            if (WhereToPlace(diag2, myToken)<3){
                m.setRowCol(2-WhereToPlace(diag2, myToken),WhereToPlace(diag2, myToken)); return m;
            }
            for (int i=0;i<3;i++){
                if (WhereToPlace(grid[i], myToken) < 3){
                    m.setRowCol(i, WhereToPlace(grid[i], myToken)); return m;
                }
                if (WhereToPlace(tranGrid[i], myToken) < 3){
                    m.setRowCol( WhereToPlace(tranGrid[i], myToken),i); return m;
                }
            }
            //If can't win, see if should block, again check diagonals first
            if (WhereToPlace(diag, otherToken)<3){
                m.setRowCol(WhereToPlace(diag, otherToken),WhereToPlace(diag, otherToken)); return m;
            }
            if (WhereToPlace(diag2, otherToken)<3){
                m.setRowCol(2-WhereToPlace(diag2, otherToken),WhereToPlace(diag2, otherToken)); return m;
            }
            for (int i=0;i<3;i++){
                if (WhereToPlace(grid[i], otherToken) < 3){
                    m.setRowCol(i, WhereToPlace(grid[i], otherToken)); return m;
                }
                if (WhereToPlace(tranGrid[i], otherToken) <3 ){
                    m.setRowCol( WhereToPlace(tranGrid[i], otherToken),i); return m;
                }
            }
            //if no win or block, try center
            if (ticTacToeBoard.getStatus(1,1)==ticTacToeBoard.OPEN){
                m.setRowCol(1,1); return m;
            }
            //Check for fork
            if (grid[0][0]==otherToken && grid[2][2] == otherToken){
                if (grid[0][1]==' ' && grid[0][2]==' ' && grid[1][2]==' '){
                    m.setRowCol(1,2); return m;
                }
                if (grid[1][0]==' ' && grid[2][0]==' ' && grid[2][1]==' '){
                    m.setRowCol(2,1); return m;
                }
            }
            if (grid[0][2]==otherToken && grid[2][0] == otherToken){
                if (grid[0][1]==' ' && grid[0][0]==' ' && grid[1][0]==' '){
                    m.setRowCol(1,2); return m;
                }
                if (grid[1][2]==' ' && grid[0][2]==' ' && grid[0][1]==' '){
                    m.setRowCol(2,1); return m;
                }
            }
            //if no win, block, or center, try opposite corner
            for (int i=0; i<3;i+=2){
                for (int j=0; j<3;j+=2){
                    if (grid[i][j]==' ' && grid[2-i][2-j]==otherToken){
                        m.setRowCol(i,j); return m;
                    }
                }
            }
            //Finally, do any free corner
            for (int i=0; i<3;i+=2){
                for (int j=0; j<3;j+=2){
                    if (grid[i][j]==' '){
                        m.setRowCol(i,j); return m;
                    }
                }
            }
            //if not, first available edge
            for (int i=0;i<3;i++){
                for (int j=0;j<3;j++){
                    char s = ticTacToeBoard.getStatus(i,j);
                    if (s == ticTacToeBoard.OPEN){
                        m.setRowCol(i,j); return m;
                    }
                } // end for j
            }            // end for i
        }// end else
       return null;
    }
    //Gives where to place, either for win or defense, depending on token specified. In one row/column.
    public int WhereToPlace(char[] a, char token){
        if (a[0]==token){
            if (a[0]==a[1] && a[2]==' ') return 2;
            if (a[0]==a[2] && a[1]==' ') return 1;
        }
        if ((a[1]==token) && (a[1]==a[2]) && a[0]==' ') return 0;
        return 5;
    }

 // --------------------------------------------

    public String toString(){
      return "is a computer";
    }

}
