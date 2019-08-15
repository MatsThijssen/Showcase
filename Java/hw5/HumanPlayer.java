public class HumanPlayer extends Player {       
        
        // all human players need to know that they are ready to play
        // this is told to us by the main program and is used to prevent "click spamming"
        // this is static because all human players use the same mouse - therefore
        // they need to make sure that the mouse has been fully released before
        // they can place a token
        static boolean readyToPlay = true;
        
 // --------------------------------------------------------------
        // constructor
        public HumanPlayer(char token) {
            super(token);
        }

 // --------------------------------------------------------------

        // generateMove - used to pick a spot on the board 
        @Override
        public Move generateMove(Board theBoard) {
            // human players can only generate moves if the mouse has been pressed and then released
            if (this.readyToPlay) {                       
                // figure out what spot the user is clicking on
                // the board can do this because it knows about the graphical canvas
                // it also knows if the mouse is up or down
                Move temp = theBoard.getBoxLocation();
                
                // if this is a valid move then we need to set our internal "ready to play" flag to false
                if (temp != null) {
                    // make a note that we have played this turn - this boolean will flip
                    // back to true when the mouse is released
                    readyToPlay = false;
                } 
                        
                        
                // the move could be a valud move or 'null' - either way return it to
                // the caller and make them deal with it :)
                return temp;
            }
                
            // otherwise just return null
            return null;
        }

 // --------------------------------------------------------------

        // all human players have the ability to be told that it is their turn
        public static void setReadyToPlay() {
            readyToPlay = true;
        }

 // --------------------------------------------------------------

        public String toString(){
          return "is a human player";
        }
}
