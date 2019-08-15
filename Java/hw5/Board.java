import processing.core.PApplet;

/**
 * a tictactoe board is 3 columns and 3 rows, represented
 * by a 2D array of characters
 *  an open spot on the board will be represented by a BLANK
 *   the winner will be represented by the type of marker - 'X' or 'O' 
 *  if no winner yet then winner = BLANK
 */

public class Board {

    // keep track of the size of the canvas
    private int desiredWidth;
        
    // keep a reference to the PApplet object - this will let us
    // draw directly to the canvas at will
    PApplet canvas;
    int colsize, rowsize;  // size of box, depends on canvas size, all boxes same size
        
    // keep track of plays on the board using a 2D array
    char[][] grid;  // first index: row (y), second index: col (x)
        
    // this is our special character that denotes an open space
    public static final char BLANK = ' ';
    public static final char OPEN = BLANK; 
        
    // this is the current winner, described by its token type
    //  default to BLANK when not yet a winner
    private char winner = BLANK;
        
 // --------------------------------------------------------------

    // constructor
    public Board(int size, PApplet canvas) { // store incoming values
        this.desiredWidth = size;
        this.canvas = canvas;
                
        // create the grid
        this.grid = new char[3][3];
                
        // initialize the grid by setting all entries to OPEN
        clearGrid();
    }
        
 // --------------------------------------------------------------

    /**
     * check if there is a winner.  
     * @return  token symbol of winner ('X' or 'O') if there is one, else BLANK char.
     */ 
    public char checkWinner() {
        if ((this.grid[0][0]==this.grid[1][1]) && (this.grid[2][2]==this.grid[0][0])) return this.grid[0][0];
        if ((this.grid[0][2]==this.grid[1][1]) && (this.grid[2][0]==this.grid[0][2])) return this.grid[0][2];
        for (int i=0;i<3;i++){
            if ((this.grid[i][0]==this.grid[i][1]) && (this.grid[i][0]==this.grid[i][2])) return this.grid[i][0];
            if ((this.grid[0][i]==this.grid[1][i]) && (this.grid[0][i]==this.grid[2][i])) return this.grid[0][i];
            
        }
        return BLANK;
    }
        
        
 // --------------------------------------------------------------
        
    // set a token onto the board
    public void setToken(char token, int row, int col) throws InvalidMoveException {
        if (this.grid[row][col] != OPEN) {
            throw new InvalidMoveException();
        }
                
        this.grid[row][col] = token;
    }
        
 // --------------------------------------------------------------

    public char[][] getGrid(){
        return this.grid;
    }
 
    // inspect a location on the board
    public char getStatus(int row, int col) {

        return this.grid[row][col];
    }
        
 // --------------------------------------------------------------

    // returns true if board is completely empty, else false
    public boolean isEmpty() {

        for (int row = 0; row < this.grid.length; row++) {
        for (int col = 0; col < this.grid[row].length; col++) {
            if (this.grid[row][col] != OPEN) {
                return false;
            }
        }
        }
                
        return true;
    }
        
 // --------------------------------------------------------------

    // returns true if board is completely full, else false
    public boolean isFull() {

        for (int row = 0; row < this.grid.length; row++) {
        for (int col = 0; col < this.grid[row].length; col++) {
            if (this.grid[row][col] == OPEN) {
                return false;
            }
        }
        }
                
        return true;
    }

        
        
 // --------------------------------------------------------------
        
    // compute which box (i.e. row and col)  was selected 
    // based on the mouse's position 
    // This will return null if the mouse is not currently pressed
    public Move getBoxLocation() {
        // array that we will send back to the caller
        Move returnValue = null;
                
        // only compute row/col if the mouse is pressed
        if (this.canvas.mousePressed) {
             // set up our return value
             returnValue = new Move();
                        
             // compute which row was clicked
             if (this.canvas.mouseX < this.canvas.width/3) {
                 returnValue.setCol(0);
             }
             else if (this.canvas.mouseX < this.canvas.width/3*2) {
                 returnValue.setCol(1);
             }
             else {
                 returnValue.setCol(2);
             }
                        
             // compute which col was clicked
             if (this.canvas.mouseY < this.canvas.height/3) {
                  returnValue.setRow(0);
             }
             else if (this.canvas.mouseY < this.canvas.height/3*2) {
                  returnValue.setRow(1);
             }
             else {
                  returnValue.setRow(2);
             }
        }// end if mousePressed
                
        return returnValue;
    }

        
 // --------------------------------------------------------------
        
    // display the current grid to the screen
    public void display() {
        // erase the background
        canvas.background(255);
            
        // draw the grid lines
        canvas.strokeWeight(5);
        canvas.stroke(0);
        canvas.line(0, canvas.height/3, canvas.width, canvas.height/3);
        canvas.line(0, canvas.height/3 * 2, canvas.width, canvas.height/3 * 2);
        canvas.line(canvas.width/3, 0, canvas.width/3, canvas.height);
        canvas.line(canvas.width/3 * 2, 0, canvas.width/3 * 2, canvas.height);
                
        rowsize = canvas.height/3;
        colsize = canvas.width/3;
            
        // set text preferences (big font, centered)
        canvas.fill(0);
        canvas.textSize(50);
        canvas.textAlign(canvas.CENTER);

        // draw in the markers
        for (int row = 0; row < this.grid.length; row++) {
        for (int col = 0; col < this.grid[row].length; col++) {
             // if this isn't a space we should draw this character onto the board
             if (this.grid[row][col] != OPEN) {
                  // compute location
                  int xPos = canvas.width/6 + (canvas.width/3 * col);
                  int yPos = canvas.height/6 + (canvas.height/3 * row) + (int)canvas.textAscent()/2;
                                        
                  // draw the marker onto the board
                  canvas.text(this.grid[row][col], xPos, yPos);
             }
        }
        }
    }
        
 // --------------------------------------------------------------
        
    // clear the grid of all markers, start fresh
    public void clearGrid() {
       // reset winner
       winner = OPEN;
                
       for (int row = 0; row < this.grid.length; row++) {
       for (int col = 0; col < this.grid[row].length; col++) {
           this.grid[row][col] = OPEN; //blank space denotes no marker at this spot
       }
       }
    }
}// end class
