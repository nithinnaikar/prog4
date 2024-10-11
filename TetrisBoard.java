package assignment;

import java.awt.*;

/**
 * Represents a Tetris board -- essentially a 2D grid of piece types (or nulls). Supports
 * tetris pieces and row clearing.  Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2D board.
 */
public final class TetrisBoard implements Board {

    //Instance variables precalculated for fast returns
    private int boardWidth;
    private int boardHeight;
    private Piece.PieceType[][] grid;
    private int[] rowWidths;
    private int[] columnHeights;
    private int maxHeight;
    private Piece currentPiece;
    private int rowsCleared;
    private Action lastAction;
    private Result lastResult;
    private Point piecePosition;

    // JTetris will use this constructor
    public TetrisBoard(int width, int height) {
        boardWidth = width;
        boardHeight = height;
        //Set instance variables to defaults
        grid = new Piece.PieceType[width][height];
        rowWidths = new int[height];
        columnHeights = new int[width];
        maxHeight = 0;
        currentPiece = null;
        rowsCleared = 0;
        lastAction = null;
        lastResult = null;
        piecePosition = null;
    }

    //Second constructor to clone board
    public TetrisBoard(int width, int height, Piece.PieceType[][] input, int[] rWidths, int[] cHeights,
                       int max, Piece currPiece, int clears, Action lastA, Result lastR, Point spot) {
        boardWidth = width;
        boardHeight = height;
        grid = input;
        rowWidths = rWidths;
        columnHeights = cHeights;
        maxHeight = max;
        currentPiece = currPiece;
        rowsCleared = clears;
        lastAction = lastA;
        lastResult = lastR;
        piecePosition = spot;
    }

    @Override
    public Result move(Action act) {
        lastAction = act;
        if (currentPiece == null){
            //Reset variables between actions
            lastResult = Result.NO_PIECE;
            rowsCleared = 0;
            return Result.NO_PIECE;
        }

        if (act == Action.DROP){
            int x = (int)piecePosition.getX();
            int y = dropHeight(currentPiece, x);
            Point newPoint = new Point(x,y);
            if (!Helper.inBounds(grid, currentPiece, newPoint)){
                //Reset variables between actions
                lastResult = Result.OUT_BOUNDS;
                rowsCleared = 0;
                return Result.OUT_BOUNDS;
            }
            else{
                //Place the piece and resolve ramifications later
                Helper.place(grid, currentPiece, newPoint);
                lastResult = Result.PLACE;
            }
        }

        if (act == Action.DOWN){
            int x = (int)piecePosition.getX();
            int y = (int)piecePosition.getY() - 1;
            Point newPoint = new Point(x,y);
            if (!Helper.inBounds(grid, currentPiece, newPoint)){
                //If going down is not valid, it should be place where it is
                Helper.place(grid, currentPiece, piecePosition);
                lastResult = Result.PLACE;
            }
            else{
                //Update piece location and other variables
                piecePosition = newPoint;
                lastResult = Result.SUCCESS;
                rowsCleared = 0;
                return Result.SUCCESS;
            }
        }

        if (act == Action.NOTHING){
            //Update variables
            lastResult = Result.SUCCESS;
            rowsCleared = 0;
            return Result.SUCCESS;
        }

        if (act == Action.CLOCKWISE){
            //Different wall kick checks for stick than the others
            if (currentPiece.getType() == Piece.PieceType.STICK){
                //Use pre-made list of wall kick spots to check
                for (Point p : Piece.I_CLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()]){
                    Piece newPiece = currentPiece.clockwisePiece();
                    //Combine relative coordinates to get absolute coordinates
                    int x = (int) piecePosition.getX() + (int)p.getX();
                    int y = (int) piecePosition.getY() + (int)p.getY();
                    Point newPoint = new Point(x,y);
                    if (Helper.inBounds(grid, newPiece, newPoint)){
                        //Update piece orientation and position
                        currentPiece = newPiece;
                        piecePosition = newPoint;
                        lastResult = Result.SUCCESS;
                        rowsCleared = 0;
                        return Result.SUCCESS;
                    }
                }
                //Update variables
                lastResult = Result.OUT_BOUNDS;
                rowsCleared = 0;
                return Result.OUT_BOUNDS;
            }
            for (Point p : Piece.NORMAL_CLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()]){
                Piece newPiece = currentPiece.clockwisePiece();
                //Combine relative coordinates to get absolute coordinates
                int x = (int) piecePosition.getX() + (int)p.getX();
                int y = (int) piecePosition.getY() + (int)p.getY();
                Point newPoint = new Point(x,y);
                if (Helper.inBounds(grid, newPiece, newPoint)){
                    //Update piece location and orientation
                    currentPiece = newPiece;
                    piecePosition = newPoint;
                    lastResult = Result.SUCCESS;
                    rowsCleared = 0;
                    return Result.SUCCESS;
                }
            }
            //Update variables
            lastResult = Result.OUT_BOUNDS;
            rowsCleared = 0;
            return Result.OUT_BOUNDS;
        }

        if (act == Action.COUNTERCLOCKWISE){
            //Different wall kick checks for stick than the others
            if (currentPiece.getType() == Piece.PieceType.STICK){
                //Use pre-made list of wall kick spots to check
                for (Point p : Piece.I_COUNTERCLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()]){
                    Piece newPiece = currentPiece.counterclockwisePiece();
                    //Combine relative coordinates to create absolute coordinates
                    int x = (int) piecePosition.getX() + (int)p.getX();
                    int y = (int) piecePosition.getY() + (int)p.getY();
                    Point newPoint = new Point(x,y);
                    if (Helper.inBounds(grid, newPiece, newPoint)){
                        //Update piece location and orientation
                        currentPiece = newPiece;
                        piecePosition = newPoint;
                        lastResult = Result.SUCCESS;
                        rowsCleared = 0;
                        return Result.SUCCESS;
                    }
                }
                //Update variables
                lastResult = Result.OUT_BOUNDS;
                rowsCleared = 0;
                return Result.OUT_BOUNDS;
            }
            for (Point p : Piece.NORMAL_COUNTERCLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()]){
                Piece newPiece = currentPiece.counterclockwisePiece();
                //Combine relative coordinates for absolute coordinates
                int x = (int) piecePosition.getX() + (int)p.getX();
                int y = (int) piecePosition.getY() + (int)p.getY();
                Point newPoint = new Point(x,y);
                if (Helper.inBounds(grid, newPiece, newPoint)){
                    //Update piece location and orientation
                    currentPiece = newPiece;
                    piecePosition = newPoint;
                    lastResult = Result.SUCCESS;
                    rowsCleared = 0;
                    return Result.SUCCESS;
                }
            }
            //Update variables
            lastResult = Result.OUT_BOUNDS;
            rowsCleared = 0;
            return Result.OUT_BOUNDS;
        }

        if (act == Action.LEFT){
            int x = (int) piecePosition.getX() - 1;
            int y = (int) piecePosition.getY();
            Point newPoint = new Point(x,y);
            if (Helper.inBounds(grid, currentPiece,newPoint)){
                //If successful, update position
                piecePosition = newPoint;
                lastResult = Result.SUCCESS;
                rowsCleared = 0;
                return Result.SUCCESS;
            }
            //Update variables
            lastResult = Result.OUT_BOUNDS;
            rowsCleared = 0;
            return Result.OUT_BOUNDS;
        }

        if (act == Action.RIGHT){
            int x = (int) piecePosition.getX() + 1;
            int y = (int) piecePosition.getY();
            Point newPoint = new Point(x,y);
            if (Helper.inBounds(grid, currentPiece,newPoint)){
                //If successful, update position
                piecePosition = newPoint;
                lastResult = Result.SUCCESS;
                rowsCleared = 0;
                return Result.SUCCESS;
            }
            //Update variables
            lastResult = Result.OUT_BOUNDS;
            rowsCleared = 0;
            return Result.OUT_BOUNDS;
        }

        rowsCleared = 0;
        for (int y = boardHeight - 1; y >= 0; y--){
            //Check each row top to bottom to see if it is cleared
            boolean full = true;
            for (int x = 0; x < boardWidth; x++){
                //If any cells are empty, the row is not cleared
                if (grid[x][y] == null){
                    full = false;
                    break;
                }
            }
            if (full){
                rowsCleared++;
                //Move everything above the cleared row down one
                for (int i = y; i < boardHeight - 1; i++){
                    for (int x = 0; x < boardWidth; x++){
                        grid[x][i] = grid[x][i+1];
                    }
                }
            }
        }

        //Update rowWidths after piece placed
        for (int y = 0 ; y < boardHeight; y++){
            int count = 0;
            for (int x = 0; x < boardWidth; x++){
                if (grid[x][y] != null) {
                    count++;
                }
            }
            rowWidths[y] = count;
        }

        //Update columnHeights and maxHeight after piece placed
        maxHeight = 0;
        for (int x = 0 ; x < boardWidth; x++){
            int y = boardHeight - 1;
            while (y >= 0){
                if (grid[x][y] != null){
                    break;
                }
                y--;
            }
            //One above the first out of bounds is the highest point
            columnHeights[x] = y + 1;
            maxHeight = Math.max(maxHeight, y + 1);
        }

        currentPiece = null;
        return Result.PLACE;
    }

    @Override
    public Board testMove(Action act) {
        //Duplicate the fields that are modified so that the original board is not disturbed
        Piece.PieceType[][] newGrid = new Piece.PieceType[boardWidth][boardHeight];
        int[] newColumnHeights = new int[boardWidth];
        int[] newRowWidths = new int[boardHeight];

        for (int i = 0; i < boardWidth; i++){
            for (int j = 0; j < boardHeight; j++){
                newGrid[i][j] = grid[i][j];
            }
            newColumnHeights[i] = columnHeights[i];
        }

        for (int i = 0; i < boardHeight; i++){
            newRowWidths[i] = rowWidths[i];
        }

        //Create a new board and run the action on it
        Board b = new TetrisBoard(boardWidth, boardHeight, newGrid, newRowWidths, newColumnHeights,
                maxHeight, currentPiece, rowsCleared, lastAction, lastResult, piecePosition);
        b.move(act);
        return b;
    }

    @Override
    public Piece getCurrentPiece() { return currentPiece; }

    @Override
    public Point getCurrentPiecePosition() { return piecePosition;}

    @Override
    public void nextPiece(Piece p, Point spawnPosition) {
        //If valid spawn, update piece and location
        if (!Helper.inBounds(grid, p, spawnPosition)){
            throw new IllegalArgumentException();
        } else{
            currentPiece = p;
            piecePosition = spawnPosition;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Board){
            Board b = (Board) other;
            //Other board must have same current piece with same position and identical grid
            if (!(b.getCurrentPiece().equals(currentPiece))){
                return false;
            }
            if (!(b.getCurrentPiecePosition().equals(piecePosition))){
                return false;
            }
            for (int x = 0; x < boardWidth; x++){
                for (int y = 0; y < boardHeight; y++){
                    if (b.getGrid(x,y) != grid[x][y]){
                        return false;
                    }
                }
            }
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public Result getLastResult() { return lastResult;}

    @Override
    public Action getLastAction() { return lastAction;}

    @Override
    public int getRowsCleared() { return rowsCleared;}

    @Override
    public int getWidth() { return boardWidth; }

    @Override
    public int getHeight() { return boardHeight; }

    @Override
    public int getMaxHeight() { return maxHeight;}

    @Override
    public int dropHeight(Piece piece, int x) {
        //Start at the highest valid spot
        int y = boardHeight - piece.getHeight();
        while (true){
            Point newPoint = new Point(x, y);
            //The highest valid spot is one above the first invalid spot
            if (!Helper.inBounds(grid, piece, newPoint)){
                return y + 1;
            }
            y--;
        }
    }

    @Override
    public int getColumnHeight(int x) { return columnHeights[x];}

    @Override
    public int getRowWidth(int y) { return rowWidths[y];}

    @Override
    public Piece.PieceType getGrid(int x, int y) { return grid[x][y];}
}
