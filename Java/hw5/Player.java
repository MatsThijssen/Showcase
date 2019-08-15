/** this is an abstract class which means that we can't instantiate it
 * it contains methods that are just "stubs" - we are relying on the subclasses
 * to fill in the details and fully implement them for us
 * the generateMove() method is abstract, A computerPlayer or Human Player
 * will have to implement it, in their own ways
 */
public abstract class Player {

        private char myToken; // all players need to know what their token is
                
 // ---------------------------------------------

        // constructor
        public Player(char token) {
           myToken = token; // store our token
        }

 // ---------------------------------------------

        // all players should be able to report their token
        public char getToken() {
           return myToken;
        }
                
 // ---------------------------------------------

        // all players should be able place their token onto the board
        // note that the board could throw an exception here - we will allow it 
        // to propogate up to our caller method
        public void setToken(Board ticTacToeBoard, Move m) throws InvalidMoveException {
           // place our token into the appropriate slot on the board
           ticTacToeBoard.setToken(getToken(), m.getRow(), m.getCol());
        }

 // ---------------------------------------------

        // all players should be able to generate a move
        // however, computers and humans will do this differently
        // therefore we mark this abstract to force
        // our subclasses to implement this method themselves
        public abstract Move generateMove(Board theBoard);
}
