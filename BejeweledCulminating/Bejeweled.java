/**
 * Programmer: Chris Li / Ms. Lam / Mr. Skuja
 * Date Last Modified: 1/22/2024
 * Bejeweled.java (Skeleton)
 * <p>
 * This class represents a Bejeweled (TM)
 * game, which allows player to make moves
 * by swapping two pieces. Chains formed after
 * valid moves disappears and the pieces on top
 * fall to fill in the gap, and new random pieces
 * fill in the empty slots.  Game ends after a
 * certain number of moves or player chooses to
 * end the game.
 */


import javax.swing.*;
import java.awt.Color;
import java.io.*;
import java.util.*;


public class Bejeweled {


    BejeweledGUI gui;    // the object referring to the GUI


    // CONSTANTS
    final Color COLOUR_DELETE = Color.RED; // Color to highlight pieces that are about to be deleted
    final Color COLOUR_SELECT = Color.YELLOW; // color to highlight the first piece that the user selects
    final int CHAIN_REQ = 3; // minimum size required to form a chain
    final int NUMMOVE = 10; // number of moves to be played in one game
    final int NUMPIECESTYLE = 7; // number of different piece styles
    final int EMPTY = -1; // represents a slot on the game board where the piece has disappeared
    final int NUMROW = 8; // number of rows in the game board
    final int NUMCOL = 8; // number of columns in the game board
    String GAMEFILEFOLDER = "gamefiles";


    public int[][] board = new int[NUMROW][NUMCOL]; // global variable for the game board
    public int score = 0; // global variable for the score
    public int numMoveLeft = NUMMOVE; // global variable for the number of moves left
    public boolean firstSelection; // global variable to check if the piece the user clicks is the first selection or not
    public int slot1Row; // global variable for the first row selection
    public int slot1Col; // global variable for the first column selection
    public int slot2Row; // global variable for the second row selection
    public int slot2Col; // global variable for the second column selection


    /*==============================================================================
    |  public void initBoard()                                                     |
    |------------------------------------------------------------------------------|
    |  Initializes the game board with random values.                              |
    |                                                                              |
    |  Parameters:                                                                 |
    |    - None                                                                    |
    |                                                                              |
    |  Return Type:                                                                |
    |    - void                                                                    |
    |                                                                              |
    |  Description:                                                                |
    |    This method populates the game board with random integers representing    |
    |    different elements or states.                                             |
    ==============================================================================*/
    public void initBoard() {
        /* This for loop goes through each of the rows and columns of the
         *  board array, and initializes each of them to be a random
         *  number from 0 to 6 inclusively
         */
        for (int i = 0; i < NUMROW; i++) {
            for (int j = 0; j < NUMCOL; j++) {
                board[i][j] = (int) (Math.random() * NUMPIECESTYLE);
            }
        }
    }


    /*==============================================================================
    |  public boolean adjacentSlots(int r1, int col1, int r2, int col2)            |
    |------------------------------------------------------------------------------|
    |  Determines if two slots on the game board are adjacent.                     |
    |                                                                              |
    |  Parameters:                                                                 |
    |    - int r1: Row index of the first slot                                     |
    |    - int col1: Column index of the first slot                                |
    |    - int r2: Row index of the second slot                                    |
    |    - int col2: Column index of the second slot                               |
    |                                                                              |
    |  Return Type:                                                                |
    |    - boolean: true if the slots are adjacent, false otherwise                |
    |                                                                              |
    |  Description:                                                                |
    |    This method checks whether two slots on the game board are adjacent by    |
    |    comparing their row and column indices. Returns true if they are adjacent,|
    |    and false otherwise.                                                      |
    ==============================================================================*/
    public boolean adjacentSlots(int r1, int col1, int r2, int col2) {
        return ((((r1 + 1 == r2) || (r1 - 1 == r2)) && col1 == col2) || (((col1 + 1 == col2) || (col1 - 1 == col2)) && r1 == r2)); // checks if the slots are adjacent by comparing their row and column indexes
    }


    /*==============================================================================
    |  public int countLeft(int row, int col)                                      |
    |------------------------------------------------------------------------------|
    |  Counts the length of a chain of identical pieces to the left of a position. |
    |                                                                              |
    |  Parameters:                                                                 |
    |    - int row: Row index of the position                                      |
    |    - int col: Column index of the position                                   |
    |                                                                              |
    |  Return Type:                                                                |
    |    - int: The length of the chain to the left of the position                |
    |                                                                              |
    |  Description:                                                                |
    |    This method counts the number of identical pieces to the left of the      |
    |    specified position on the game board, starting from the given row and     |
    |    column indices. Returns the length of the chain.                          |
    ==============================================================================*/
    public int countLeft(int row, int col) {
        int chainLength = -1; // sets the integer chain length to -1
        int currentPiece = board[row][col]; // gets the value of the board piece at the given parameter

        /* This while loop checks to see if the left-adjacent piece is the same as the first
         * and adds one to the chain length if it is. It stops when the piece is not the same
         */
        while (col > -1 && board[row][col] == currentPiece && board[row][col] != -1) {
            chainLength++;
            col--;
        }

        return chainLength; // returns the integer chain length
    }


    /*==============================================================================
    |  public int countRight(int row, int col)                                     |
    |------------------------------------------------------------------------------|
    |  Counts the length of a chain of identical pieces to the right of a position.|
    |                                                                              |
    |  Parameters:                                                                 |
    |    - int row: Row index of the position                                      |
    |    - int col: Column index of the position                                   |
    |                                                                              |
    |  Return Type:                                                                |
    |    - int: The length of the chain to the right of the position               |
    |                                                                              |
    |  Description:                                                                |
    |    This method counts the number of identical pieces to the right of the     |
    |    specified position on the game board, starting from the given row and     |
    |    column indices. Returns the length of the chain.                          |
    ==============================================================================*/
    public int countRight(int row, int col) {
        int chainLength = -1; // sets the integer chain length to -1
        int currentPiece = board[row][col]; // gets the value of the board piece at the given parameter


        /* This while loop checks to see if the right-adjacent piece is the same as the first
         * and adds one to the chain length if it is. It stops when the piece is not the same
         */
        while (col < NUMCOL && board[row][col] == currentPiece) {
            chainLength++;
            col++;
        }

        return chainLength; // returns the integer chain length
    }


    /*==============================================================================
    |  public int countUp(int row, int col)                                        |
    |------------------------------------------------------------------------------|
    |  Counts the length of a chain of identical pieces upwards from a position.   |
    |                                                                              |
    |  Parameters:                                                                 |
    |    - int row: Row index of the position                                      |
    |    - int col: Column index of the position                                   |
    |                                                                              |
    |  Return Type:                                                                |
    |    - int: The length of the chain upwards from the position                  |
    |                                                                              |
    |  Description:                                                                |
    |    This method counts the number of identical pieces upwards from the        |
    |    specified position on the game board, starting from the given row and     |
    |    column indices. Returns the length of the chain.                          |
    ==============================================================================*/
    public int countUp(int row, int col) {
        int chainLength = -1; // sets the integer chain length to -1
        int currentPiece = board[row][col]; // gets the value of the board piece at the given parameter


        /* This while loop checks to see if the upper-adjacent piece is the same as the first
         * and adds one to the chain length if it is. It stops when the piece is not the same
         */
        while (row > -1 && board[row][col] == currentPiece) {
            chainLength++;
            row--;
        }

        return chainLength; // returns the integer chain length
    }


    /*==============================================================================
    |  public int countDown(int row, int col)                                      |
    |------------------------------------------------------------------------------|
    |  Counts the length of a chain of identical pieces downwards from a position.  |
    |                                                                              |
    |  Parameters:                                                                 |
    |    - int row: Row index of the position                                       |
    |    - int col: Column index of the position                                    |
    |                                                                              |
    |  Return Type:                                                                |
    |    - int: The length of the chain downwards from the position                |
    |                                                                              |
    |  Description:                                                                |
    |    This method counts the number of identical pieces downwards from the      |
    |    specified position on the game board, starting from the given row and     |
    |    column indices. Returns the length of the chain.                           |
    ==============================================================================*/
    public int countDown(int row, int col) {
        int chainLength = -1; // sets the integer chain length to -1
        int currentPiece = board[row][col]; // gets the value of the board piece at the given parameter


        /* This while loop checks to see if the lower-adjacent piece is the same as the first
         * and adds one to the chain length if it is. It stops when the piece is not the same
         */
        while (row < NUMROW && board[row][col] == currentPiece) {
            chainLength++;
            row++;
        }

        return chainLength; // returns the integer chain length
    }


    /*==============================================================================
    |  public void swapPieces(int r1, int col1, int r2, int col2)                  |
    |------------------------------------------------------------------------------|
    |  Swaps the positions of two pieces on the game board.                        |
    |                                                                              |
    |  Parameters:                                                                 |
    |    - int r1: Row index of the first piece                                    |
    |    - int col1: Column index of the first piece                               |
    |    - int r2: Row index of the second piece                                   |
    |    - int col2: Column index of the second piece                              |
    |                                                                              |
    |  Return Type:                                                                |
    |    - void                                                                    |
    |                                                                              |
    |  Description:                                                                |
    |    This method swaps the positions of two pieces on the game board given     |
    |    their respective row and column indices. After the swap, the game board is|
    |    updated accordingly.                                                      |
    ==============================================================================*/
    public void swapPieces(int r1, int col1, int r2, int col2) {
        int temp = board[r1][col1]; // initializes the variable temp, so that the pieces can be swapped


        board[r1][col1] = board[r2][col2]; //sets the first piece to the second piece


        board[r2][col2] = temp; //sets the second piece equal to the original first piece


        updateGameBoard(); // updates the game board
    }


    /*==============================================================================
    |  public void markDeletePiece(int row, int col)                               |
    |------------------------------------------------------------------------------|
    |  Marks a game board piece for deletion at the specified position.            |
    |                                                                              |
    |  Parameters:                                                                 |
    |    - int row: Row index of the piece to be marked for deletion               |
    |    - int col: Column index of the piece to be marked for deletion            |
    |                                                                              |
    |  Return Type:                                                                |
    |    - void                                                                    |
    |                                                                              |
    |  Description:                                                                |
    |    This method marks a game board piece at the specified position for        |
    |    deletion by updating its value to represent an empty slot.                |
    ==============================================================================*/
    public void markDeletePiece(int row, int col) {
        gui.highlightSlot(row, col, COLOUR_DELETE); // highlights the slot
        board[row][col] = EMPTY; // sets the given slot to empty (-1)
    }


    /*==============================================================================
    |  public void markDeletePieceRight(int row, int col, int pieces)              |
    |------------------------------------------------------------------------------|
    |  Marks a series of game board pieces to the right for deletion.              |
    |                                                                              |
    |  Parameters:                                                                 |
    |    - int row: Row index of the starting piece                                |
    |    - int col: Column index of the starting piece                             |
    |    - int pieces: Number of pieces to mark for deletion to the right          |
    |                                                                              |
    |  Return Type:                                                                |
    |    - void                                                                    |
    |                                                                              |
    |  Description:                                                                |
    |    This method marks a series of game board pieces to the right of the       |
    |    starting position for deletion by updating their values to represent      |
    |    empty slots.                                                              |
    ==============================================================================*/
    public void markDeletePieceRight(int row, int col, int pieces) {
        /* This for loop sets "pieces" number of pieces to the right of
         *  the given slot to be empty, after highlighting them so that
         *  the user can see what chain was formed
         */
        for (int i = col; i < col + pieces; i++) {
            gui.highlightSlot(row, i + 1, COLOUR_DELETE);
            board[row][i + 1] = EMPTY;
        }
    }


    /*==============================================================================
    |  public void markDeletePieceLeft(int row, int col, int pieces)               |
    |------------------------------------------------------------------------------|
    |  Marks a series of game board pieces to the left for deletion.               |
    |                                                                              |
    |  Parameters:                                                                 |
    |    - int row: Row index of the starting piece                                |
    |    - int col: Column index of the starting piece                             |
    |    - int pieces: Number of pieces to mark for deletion to the left           |
    |                                                                              |
    |  Return Type:                                                                |
    |    - void                                                                    |
    |                                                                              |
    |  Description:                                                                |
    |    This method marks a series of game board pieces to the left of the        |
    |    starting position for deletion by updating their values to represent      |
    |    empty slots.                                                              |
    ==============================================================================*/
    public void markDeletePieceLeft(int row, int col, int pieces) {
        /* This for loop sets "pieces" number of pieces to the left of
         *  the given slot to be empty, after highlighting them so that
         *  the user can see what chain was formed
         */
        for (int i = col; i > col - pieces; i--) {
            gui.highlightSlot(row, i - 1, COLOUR_DELETE);
            board[row][i - 1] = EMPTY;
        }
    }


    /*==============================================================================
    |  public void markDeletePieceUp(int row, int col, int pieces)                 |
    |------------------------------------------------------------------------------|
    |  Marks a series of game board pieces upwards for deletion.                   |
    |                                                                              |
    |  Parameters:                                                                 |
    |    - int row: Row index of the starting piece                                |
    |    - int col: Column index of the starting piece                             |
    |    - int pieces: Number of pieces to mark for deletion upwards               |
    |                                                                              |
    |  Return Type:                                                                |
    |    - void                                                                    |
    |                                                                              |
    |  Description:                                                                |
    |    This method marks a series of game board pieces upwards from the starting |
    |    position for deletion by updating their values to represent empty slots.  |
    ==============================================================================*/
    public void markDeletePieceUp(int row, int col, int pieces) {
        /* This for loop sets "pieces" number of pieces to the top of
         *  the given slot to be empty, after highlighting them so that
         *  the user can see what chain was formed
         */
        for (int i = row; i > row - pieces; i--) {
            gui.highlightSlot(i - 1, col, COLOUR_DELETE);
            board[i - 1][col] = EMPTY;
        }
    }


    /*==============================================================================
    |  public void markDeletePieceDown(int row, int col, int pieces)               |
    |------------------------------------------------------------------------------|
    |  Marks a series of game board pieces downwards for deletion.                 |
    |                                                                              |
    |  Parameters:                                                                 |
    |    - int row: Row index of the starting piece                                |
    |    - int col: Column index of the starting piece                             |
    |    - int pieces: Number of pieces to mark for deletion downwards             |
    |                                                                              |
    |  Return Type:                                                                |
    |    - void                                                                    |
    |                                                                              |
    |  Description:                                                                |
    |    This method marks a series of game board pieces downwards from the        |
    |    starting position for deletion by updating their values to represent      |
    |    empty slots.                                                              |
    ==============================================================================*/
    public void markDeletePieceDown(int row, int col, int pieces) {
        /* This for loop sets "pieces" number of pieces to the bottom of
         *  the given slot to be empty, after highlighting them so that
         *  the user can see what chain was formed
         */
        for (int i = row; i < row + pieces; i++) {
            gui.highlightSlot(i + 1, col, COLOUR_DELETE);
            board[i + 1][col] = EMPTY;
        }
    }


    /*==============================================================================
    |  public Bejeweled(BejeweledGUI gui)                                          |
    |------------------------------------------------------------------------------|
    |  Constructor for the Bejeweled game. Initializes the game with the specified |
    |  graphical user interface.                                                   |
    |                                                                              |
    |  Parameters:                                                                 |
    |    - BejeweledGUI gui: The graphical user interface for the Bejeweled game   |
    |                                                                              |
    |  Return Type:                                                                |
    |    - None (Constructor)                                                      |
    |                                                                              |
    |  Description:                                                                |
    |    Initializes a new instance of the Bejeweled game with the provided        |
    |    graphical user interface. The game is started immediately after           |
    |    initialization.                                                           |
    ==============================================================================*/
    public Bejeweled(BejeweledGUI gui) {
        this.gui = gui;
        start(); // starts game
    }


    /*==============================================================================
    |  public void start()                                                         |
    |------------------------------------------------------------------------------|
    |  Initiates the starting actions for the Bejeweled game.                      |
    |                                                                              |
    |  Parameters:                                                                 |
    |    - None                                                                    |
    |                                                                              |
    |  Return Type:                                                                |
    |    - void                                                                    |
    |                                                                              |
    |  Description:                                                                |
    |    Initiates the starting actions for the Bejeweled game, such as updating   |
    |    the game board.                                                           |
    ==============================================================================*/
    public void start() {
        initBoard();

        firstSelection = false; // sets first selection to false
        score = 0; // sets score to 0
        numMoveLeft = NUMMOVE; // sets the number of moves left back to 10

        gui.setScore(score); // display the score
        gui.setMoveLeft(numMoveLeft); // display the number of moves left

        updateGameBoard(); // update the game board
    }


    /*==============================================================================
    |  public void play(int row, int column)                                       |
    |------------------------------------------------------------------------------|
    |  Executes a move in the Bejeweled game at the specified position.            |
    |                                                                              |
    |  Parameters:                                                                 |
    |    - int row: Row index of the selected position                             |
    |    - int column: Column index of the selected position                       |
    |                                                                              |
    |  Return Type:                                                                |
    |    - void                                                                    |
    |                                                                              |
    |  Description:                                                                |
    |    Executes a move in the Bejeweled game at the specified position, handling |
    |    piece swapping, chain counting, deletion marking, scoring, and updating   |
    |    the game board and graphical user interface accordingly.                  |
    ==============================================================================*/
    public void play(int row, int column) {

        // creates variables to store the amount of pieces around the first piece
        int up;
        int down;
        int left;
        int right;

        // creates variables to store the amount of pieces around the second piece
        int up2;
        int down2;
        int left2;
        int right2;

        // creates variables for the chain lengths of the first piece
        int verticalChainLength1;
        int horizontalChainLength1;

        // creates variables for the chain lengths of the second piece
        int verticalChainLength2;
        int horizontalChainLength2;


        int totalChainLength = 0; // sets the chain length variable equal to 0


        // checks to see if the first selection has been made or not
        if (!firstSelection) {
            gui.highlightSlot(row, column, COLOUR_SELECT); // highlights the first selected slot
            slot1Row = row; // sets it to be the first row
            slot1Col = column; // sets it to be the first column
            firstSelection = true; // sets the first selection to true, since it has been made
        }
        //first selection has been made
        else {


            unhighlightAll(); // unhighlights everything


            slot2Row = row; // sets the second selection row to be slot2Row
            slot2Col = column; //sets the second selection column to be slot2Col


            //checks to see if the pieces are adjacent
            if (adjacentSlots(slot1Row, slot1Col, slot2Row, slot2Col)) {


                swapPieces(slot1Row, slot1Col, slot2Row, slot2Col); // if they are adjacent, perform the swap


                up = countUp(slot1Row, slot1Col); // counts how many same pieces are above the first selection
                down = countDown(slot1Row, slot1Col); // counts how many same pieces are below the first selection
                left = countLeft(slot1Row, slot1Col); // counts how many same pieces are left to the first selection
                right = countRight(slot1Row, slot1Col); // counts how many same pieces are right to the first selection


                up2 = countUp(slot2Row, slot2Col); // counts how many same pieces are above the second selection
                down2 = countDown(slot2Row, slot2Col); // counts how many same pieces are below the second selection
                left2 = countLeft(slot2Row, slot2Col); // counts how many same pieces are left to the second selection
                right2 = countRight(slot2Row, slot2Col); // counts how many same pieces are right to the second selection


                verticalChainLength1 = up + down + 1; // counts the first vertical chain length by adding the pieces above and below and 1 for the piece itself
                horizontalChainLength1 = left + right + 1; // counts the first horizontal chain length by adding the pieces left and right and 1 for the piece itself


                verticalChainLength2 = up2 + down2 + 1; // counts the second vertical chain length by adding the pieces above and below and 1 for the piece itself
                horizontalChainLength2 = left2 + right2 + 1; // counts the second horizontal chain length by adding the pieces left and right and 1 for the piece itself




               /* Two if blocks are used here, but it could also just be one, but that would
                  also mean calling the markDeletePiece method 4 times, once each individual
                  nested if statement, so they would not interfere
                */


                // checks to see if any chain(s) is made
                if (verticalChainLength1 >= CHAIN_REQ || horizontalChainLength1 >= CHAIN_REQ) {


                    // checks the vertical chain
                    if (verticalChainLength1 >= CHAIN_REQ) {
                        markDeletePieceUp(slot1Row, slot1Col, up); // highlights, then marks all the pieces above as empty
                        markDeletePieceDown(slot1Row, slot1Col, down); // highlights, then marks all the pieces below as empty
                    }


                    // checks the horizontal chain
                    if (horizontalChainLength1 >= CHAIN_REQ) {
                        markDeletePieceLeft(slot1Row, slot1Col, left); // highlights, then marks all the pieces to the left as empty
                        markDeletePieceRight(slot1Row, slot1Col, right); // highlights, then marks all the pieces to the right as empty
                    }


                    markDeletePiece(slot1Row, slot1Col); //marks the piece itself as empty, since it is also involved in the match
                }


                // checks to see if the second piece created any valid chains
                if (verticalChainLength2 >= CHAIN_REQ || horizontalChainLength2 >= CHAIN_REQ) {


                    // checks the vertical chain
                    if (verticalChainLength2 >= CHAIN_REQ) {
                        markDeletePieceUp(slot2Row, slot2Col, up2); // highlights, then marks all the pieces above as empty
                        markDeletePieceDown(slot2Row, slot2Col, down2); // highlights, then marks all the pieces below as empty
                    }
                    // checks the horizontal chain
                    if (horizontalChainLength2 >= CHAIN_REQ) {
                        markDeletePieceLeft(slot2Row, slot2Col, left2); // highlights, then marks all the pieces to the left as empty
                        markDeletePieceRight(slot2Row, slot2Col, right2); // highlights, then marks all the pieces to the right as empty
                    }


                    markDeletePiece(slot2Row, slot2Col); // marks the piece itself as empty, since it was involved in the match
                }


                // goes through each row and column, to see how many pieces were deleted. There is no need to check if valid chains were made, because we only deleted pieces if that was true
                for (int i = 0; i < NUMROW; i++) {
                    for (int j = 0; j < NUMCOL; j++) {
                        if (board[i][j] == EMPTY) {
                            totalChainLength++; //adds to the total chain length if the current index is empty


                        }
                    }
                }


                // checks to see if no chains are formed
                if (totalChainLength == 0) {
                    gui.highlightSlot(slot1Row, slot1Col, COLOUR_SELECT);
                    gui.highlightSlot(slot2Row, slot2Col, COLOUR_SELECT);
                    gui.showInvalidMoveMessage(); // shows an invalid move message
                    swapPieces(slot1Row, slot1Col, row, column); // swaps the pieces back, since no chain formed
                }
                // we know that if the total length is not 0, a chain must have been formed
                else {
                    gui.showChainSizeMessage(totalChainLength);


                    score += totalChainLength; // since a chain is formed, we can safely add it to the total score


                    numMoveLeft--; // we know that a chain was formed, so we can take away a move
                }


            }
            // the pieces are not adjacent
            else {
                gui.highlightSlot(slot1Row, slot1Col, COLOUR_SELECT);
                gui.highlightSlot(slot2Row, slot2Col, COLOUR_SELECT);
                gui.showInvalidMoveMessage(); // shows the invalid move message
            }

            firstSelection = false; // sets first selection back to false, since we might be starting a new move

            unhighlightAll(); // unhighlights everything
        }

        updateGameBoard(); // updates the board before the possibility the game ends

        gui.setScore(score); // updates the score

        gui.setMoveLeft(numMoveLeft); // shows the number of moves left


        // checks if the user has no moves left
        if (numMoveLeft == 0) {
            endGame(); // ends the game
        }

    }


    /*==============================================================================
    |  public void updateGameBoard()                                               |
    |------------------------------------------------------------------------------|
    |  Updates the graphical representation of the game board on the GUI.          |
    |                                                                              |
    |  Parameters:                                                                 |
    |    - None                                                                    |
    |                                                                              |
    |  Return Type:                                                                |
    |    - void                                                                    |
    |                                                                              |
    |  Description:                                                                |
    |    Updates the graphical representation of the game board on the graphical   |
    |    user interface (GUI) by setting the pieces at each position on the board. |
    ==============================================================================*/
    public void updateGameBoard() {
        // goes through every column
        for (int c = 0; c < NUMCOL; c++) {
            int spaces = 0; // sets the total number of spaces to 0
            for (int r = NUMROW - 1; r > -1; r--) {


                // adds to the number of spaces if the current slot is empty
                if (board[r][c] == EMPTY) {
                    spaces++;
                }
                // checks if there are spaces
                else if (spaces > 0) {


                    /* Since it is impossible to have a piece, then some spaces, then another piece
                     *  due to the nature of the game, we can just move all the pieces with index
                     *  [row + spaces][column] down to [row][column]. note: adding to the rows
                     *  goes down, not up, so index [row][column] is actually above [row + spaces][column]
                     * */


                    board[r + spaces][c] = board[r][c]; // moves the piece down
                    board[r][c] = EMPTY; // sets the upper piece to empty
                }
            }
        }


        // goes through each slot of the board
        for (int i = 0; i < NUMROW; i++) {
            for (int j = 0; j < NUMCOL; j++) {


                // if the slot is empty, replace it with a random piece
                if (board[i][j] == EMPTY) {
                    board[i][j] = (int) (Math.random() * NUMPIECESTYLE);
                }
                gui.setPiece(i, j, board[i][j]); // sets the piece on the gui
            }
        }
    }


    /*==============================================================================
    |  public void endGame()                                                       |
    |------------------------------------------------------------------------------|
    |  Ends the Bejeweled game and displays the game over message.                 |
    |                                                                              |
    |  Parameters:                                                                 |
    |    - None                                                                    |
    |                                                                              |
    |  Return Type:                                                                |
    |    - void                                                                    |
    |                                                                              |
    |  Description:                                                                |
    |    Ends the Bejeweled game and displays the game over message on the         |
    |    graphical user interface (GUI) along with the final score and remaining   |
    |    moves.                                                                    |
    ==============================================================================*/
    public void endGame() {
        gui.showGameOverMessage(score, 10 - numMoveLeft); // shows the game over message, along with the score and the number of moves used

        numMoveLeft = NUMMOVE; // resets number of moves left

        score = 0; // resets score

        initBoard(); // resets the board in case the user wants to play again

        updateGameBoard(); // updates the board so the user can see the board

        gui.setScore(score); // updates the score

        gui.setMoveLeft(numMoveLeft); // shows the number of moves left
    }


    /*==============================================================================
    |  public boolean saveToFile(String fileName)                                  |
    |------------------------------------------------------------------------------|
    |  Saves the current state of the Bejeweled game to a file in the              |
    |  game files folder.                                                          |
    |                                                                              |
    |  Parameters:                                                                 |
    |    - String fileName: The name of the file to save the game state to         |
    |                                                                              |
    |  Return Type:                                                                |
    |    - boolean: true if the game state is successfully saved, false otherwise  |
    |                                                                              |
    |  Description:                                                                |
    |    Saves the current state of the Bejeweled game, including the score,       |
    |    remaining moves, and the arrangement of pieces on the game board, to a    |
    |    specified file. Returns true if the save operation is successful,         |
    |    otherwise returns false.                                                  |
    ==============================================================================*/
    public boolean saveToFile(String fileName) {


        boolean gameSaved = false; // sets the gameSaved variable to false, in case there is an error


        // since we are doing file IO, we need a try catch
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(GAMEFILEFOLDER + "/" + fileName, false)); // creates the buffered writer


            writer.write(String.format("%d", score)); // writes the score
            writer.newLine(); // new line
            writer.write(String.format("%d", numMoveLeft)); // writes the number of moves left
            writer.newLine(); // new line


            // iterates through each element in the board
            for (int row = 0; row < NUMROW; row++) {
                for (int col = 0; col < NUMCOL; col++) {
                    writer.write(String.format("%-5d", board[row][col])); // writes the number that the board index stores
                }
                writer.newLine(); // new line
            }


            writer.close(); // closes the writer


            gameSaved = true; // since everything is done, the game has been successfully saved
        }
        // catches IO Exception
        catch (IOException e) {
            System.out.println(e);
        }


        return gameSaved; // returns whether the game was saved
    }


    /*==============================================================================
    |  public boolean loadFromFile(String fileName)                                |
    |------------------------------------------------------------------------------|
    |  Loads the Bejeweled game state from a specified file.                       |
    |                                                                              |
    |  Parameters:                                                                 |
    |    - String fileName: The name of the file containing the saved game state   |
    |                                                                              |
    |  Return Type:                                                                |
    |    - boolean: true if the game state is successfully loaded, false otherwise |
    |                                                                              |
    |  Description:                                                                |
    |    Loads the Bejeweled game state from a specified file, including the score,|
    |    remaining moves, and the arrangement of pieces on the game board. Updates |
    |    the game state on the graphical user interface (GUI). Returns true if the |
    |    load operation is successful, otherwise returns false.                    |
    ==============================================================================*/
    public boolean loadFromFile(String fileName) {


        boolean fileLoaded = false; // sets the fileLoaded variable to false


        //since we are doing file IO, we need a try catch
        try {
            Scanner in = new Scanner(new File(GAMEFILEFOLDER + "/" + fileName)); // creates the file scanner
            score = in.nextInt(); // sets the score to the first integer in the file
            numMoveLeft = in.nextInt(); // sets the number of moves left to the second integer in the file


            // goes through each row and column with a nested for loop
            for (int i = 0; i < NUMROW; i++) {
                for (int j = 0; j < NUMCOL; j++) {
                    board[i][j] = in.nextInt(); // sets board[i][j] to the next integer
                }
            }


            gui.setScore(score); // updates the score
            gui.setMoveLeft(numMoveLeft); // updates the number of moves left


            in.close(); // closes the scanner


            fileLoaded = true; // sets fileLoaded to true, since everything is done


        }
        // catches IO Exception
        catch (IOException e) {
            System.out.println(e);
        }


        return fileLoaded; // returns whether the file was loaded
    }


    /*==============================================================================
    |  public void unhighlightAll()                                                |
    |------------------------------------------------------------------------------|
    |  Removes highlighting from all slots on the GUI.                             |
    |                                                                              |
    |  Parameters:                                                                 |
    |    - None                                                                    |
    |                                                                              |
    |  Return Type:                                                                |
    |    - void                                                                    |
    |                                                                              |
    |  Description:                                                                |
    |    Removes highlighting from all slots on the graphical user interface (GUI) |
    |    by calling the unhighlightSlot method for each slot in the game board.    |
    ==============================================================================*/
    public void unhighlightAll() {
        // goes through each board element
        for (int i = 0; i < NUMROW; i++) {
            for (int j = 0; j < NUMCOL; j++) {
                gui.unhighlightSlot(i, j); // unhighlights the current slot
            }
        }
    }
}



