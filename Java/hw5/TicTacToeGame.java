import processing.core.PApplet;

/**
 * TicTacToe Game - players are either humans (who click
 *   in open spot, or computer - who must find a move
 *   
 * @author: Marsha Berger with assistance from Craig Kapp
 * @version 1.2
 */

public class TicTacToeGame extends PApplet{
    // how big is our game?
    final static int GAME_SIZE = 800;

    // board to hold the current state of the game
    Board theBoard;

    // create two players
    // note that these are two generic players - we choose the type of player
    // down below when we instantiate these objects (either human or computer)
    Player player1;
    Player player2;

    // keep track of who's turn it is
    // 1 = 1st player
    // 2 = 2nd player
    int turn = 1;

    // ---------------------------------------------------------------

    /**  set up the game */
    public void setup(){
        // request a graphical canvas size based on our desired size
        size(GAME_SIZE, GAME_SIZE);

        // construct our board object, pass the canvas to board can draw itself
        theBoard = new Board(GAME_SIZE, this);

        // construct our two players
        //player1 = new ComputerPlayer('X');
        player1 = new HumanPlayer('X');
        player2 = new ComputerPlayer('O');
        //player2 = new HumanPlayer('O');

        System.out.println("Player 1 is " + player1 + " and goes first to start. Then loser goes first ");
        System.out.println("Player 2 is " + player2);

        frameRate(30); // no need for high speed
    }

    // -----------------------------------------------------------------

    /** draw called to update board and orchestrate the next move */
    public void draw(){

        // start off by displaying the current state of the board
        // (this will also erase the screen and re-draw the board on top of it)
        theBoard.display();

        // get the current winner
        char winner = theBoard.checkWinner();
                
        // only play if the board hasn't been filled and if we dont have a winner
        boolean donePlaying = (theBoard.isFull() ||  winner != theBoard.OPEN);
        if (!donePlaying){
              if (turn == 1) {
                  try{
                      takeTurn(player1); // handle player 1
                  }
                  catch(InvalidMoveException ex){
                      System.out.println(ex.getMessage());
                  }
              }
              else if ( turn==2){ // handle player 2
                  try{
                      takeTurn(player2);
                  }
                  catch(InvalidMoveException ex){
                      System.out.println(ex.getMessage());
                  }
              }
         }// end if not done playing
        else { // otherwise the board is filled - give them the option to restart
            // but first report results 
            textSize(25);
            fill(255);
            stroke(255,0,0);
            rect(0, 0, width, 50);
            fill(0);
                        
            // case 1: it's a draw!  (board full but no winner, signified by OPEN)
            if (winner == theBoard.OPEN){
                text("Draw!  Hit any key to reset.", width/2, 35);
            }
            else{ // winner either 'X' or 'O'
                text(winner + " is the winner!  Hit any key to reset.",width/2, 35);
            }
        } // end game is over. play again?
    }

// --------------------------------------------------------`

    public void takeTurn(Player p) throws InvalidMoveException {
        // generate a move.  either human or computer method called 
        // depending on type of p (example of polymorphism)
        Move m = p.generateMove(theBoard);

        // only set the token if the move generated by this player is not
        // equal to null (this happens if the player is a human and
        // the haven't clicked yet)
        if (m != null){
            // we might have an issue with setting a token at this spot
            // have to try and catch an error to prevent this from happening
                // attempt to set the token (error could be thrown here)
                p.setToken(theBoard, m);

            // only change turn once move done, so do it inside m!= null test
            turn = (turn ==1) ? 2 : 1; 
        }// player played
    }
        
// --------------------------------------------------------`

    /** when a key is pressed reset the board to play again
     */
    public void keyPressed(){
        // get the current winner
        char winner = theBoard.checkWinner();
                
        // only reset if the board is full or if we have a winner
        if (theBoard.isFull() || winner != theBoard.OPEN){
            theBoard.clearGrid();
        }
    }
        
// --------------------------------------------------------`

    /** mouseReleased indicates human player able to play, Otherwise mousePressed
     *  stays on too long and results in "click spamming"
     */
    public void mouseReleased(){
        HumanPlayer.setReadyToPlay();
    }
        
}// end class
