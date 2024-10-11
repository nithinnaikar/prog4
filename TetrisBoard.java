package assignment;

import java.awt.*;

/**
 * Represents a Tetris board -- essentially a 2D grid of piece types (or nulls). Supports
 * tetris pieces and row clearing.  Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2D board.
 */
public final class TetrisBoard implements Board {
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
            lastResult = Result.NO_PIECE;
            rowsCleared = 0;
            return Result.NO_PIECE;
        }

        if (act == Action.DROP){
            int x = (int)piecePosition.getX();
            int y = dropHeight(currentPiece, x);
            Point newPoint = new Point(x,y);
            if (!Helper.inBounds(grid, currentPiece, newPoint)){
                lastResult = Result.OUT_BOUNDS;
                rowsCleared = 0;
                return Result.OUT_BOUNDS;
            }
            else{
                Helper.place(grid, currentPiece, newPoint);
                lastResult = Result.PLACE;
            }
        }

        if (act == Action.DOWN){
            int x = (int)piecePosition.getX();
            int y = (int)piecePosition.getY() - 1;
            Point newPoint = new Point(x,y);
            if (!Helper.inBounds(grid, currentPiece, newPoint)){
                Helper.place(grid, currentPiece, piecePosition);
                lastResult = Result.PLACE;
            }
            else{
                piecePosition = newPoint;
                lastResult = Result.SUCCESS;
                rowsCleared = 0;
                return Result.SUCCESS;
            }
        }

        if (act == Action.NOTHING){
            lastResult = Result.SUCCESS;
            rowsCleared = 0;
            return Result.SUCCESS;
        }

        if (act == Action.CLOCKWISE){
            if (currentPiece.getType() == Piece.PieceType.STICK){
                for (Point p : Piece.I_CLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()]){
                    Piece newPiece = currentPiece.clockwisePiece();
                    int x = (int) piecePosition.getX() + (int)p.getX();
                    int y = (int) piecePosition.getY() + (int)p.getY();
                    Point newPoint = new Point(x,y);
                    if (Helper.inBounds(grid, newPiece, newPoint)){
                        currentPiece = newPiece;
                        piecePosition = newPoint;
                        lastResult = Result.SUCCESS;
                        rowsCleared = 0;
                        return Result.SUCCESS;
                    }
                }
                lastResult = Result.OUT_BOUNDS;
                rowsCleared = 0;
                return Result.OUT_BOUNDS;
            }
            for (Point p : Piece.NORMAL_CLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()]){
                Piece newPiece = currentPiece.clockwisePiece();
                int x = (int) piecePosition.getX() + (int)p.getX();
                int y = (int) piecePosition.getY() + (int)p.getY();
                Point newPoint = new Point(x,y);
                if (Helper.inBounds(grid, newPiece, newPoint)){
                    currentPiece = newPiece;
                    piecePosition = newPoint;
                    lastResult = Result.SUCCESS;
                    rowsCleared = 0;
                    return Result.SUCCESS;
                }
            }
            lastResult = Result.OUT_BOUNDS;
            rowsCleared = 0;
            return Result.OUT_BOUNDS;
        }

        if (act == Action.COUNTERCLOCKWISE){
            if (currentPiece.getType() == Piece.PieceType.STICK){
                for (Point p : Piece.I_COUNTERCLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()]){
                    Piece newPiece = currentPiece.counterclockwisePiece();
                    int x = (int) piecePosition.getX() + (int)p.getX();
                    int y = (int) piecePosition.getY() + (int)p.getY();
                    Point newPoint = new Point(x,y);
                    if (Helper.inBounds(grid, newPiece, newPoint)){
                        currentPiece = newPiece;
                        piecePosition = newPoint;
                        lastResult = Result.SUCCESS;
                        rowsCleared = 0;
                        return Result.SUCCESS;
                    }
                }
                lastResult = Result.OUT_BOUNDS;
                rowsCleared = 0;
                return Result.OUT_BOUNDS;
            }
            for (Point p : Piece.NORMAL_COUNTERCLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()]){
                Piece newPiece = currentPiece.counterclockwisePiece();
                int x = (int) piecePosition.getX() + (int)p.getX();
                int y = (int) piecePosition.getY() + (int)p.getY();
                Point newPoint = new Point(x,y);
                if (Helper.inBounds(grid, newPiece, newPoint)){
                    currentPiece = newPiece;
                    piecePosition = newPoint;
                    lastResult = Result.SUCCESS;
                    rowsCleared = 0;
                    return Result.SUCCESS;
                }
            }
            lastResult = Result.OUT_BOUNDS;
            rowsCleared = 0;
            return Result.OUT_BOUNDS;
        }

        if (act == Action.LEFT){
            int x = (int) piecePosition.getX() - 1;
            int y = (int) piecePosition.getY();
            Point newPoint = new Point(x,y);
            if (Helper.inBounds(grid, currentPiece,newPoint)){
                piecePosition = newPoint;
                lastResult = Result.SUCCESS;
                rowsCleared = 0;
                return Result.SUCCESS;
            }
            lastResult = Result.OUT_BOUNDS;
            rowsCleared = 0;
            return Result.OUT_BOUNDS;
        }

        if (act == Action.RIGHT){
            int x = (int) piecePosition.getX() + 1;
            int y = (int) piecePosition.getY();
            Point newPoint = new Point(x,y);
            if (Helper.inBounds(grid, currentPiece,newPoint)){
                piecePosition = newPoint;
                lastResult = Result.SUCCESS;
                rowsCleared = 0;
                return Result.SUCCESS;
            }
            lastResult = Result.OUT_BOUNDS;
            rowsCleared = 0;
            return Result.OUT_BOUNDS;
        }

        rowsCleared = 0;
        for (int y = boardHeight - 1; y >= 0; y--){
            boolean full = true;
            for (int x = 0; x < boardWidth; x++){
                if (grid[x][y] == null){
                    full = false;
                    break;
                }
            }
            if (full){
                rowsCleared++;
                for (int i = y; i < boardHeight - 1; i++){
                    for (int x = 0; x < boardWidth; x++){
                        grid[x][i] = grid[x][i+1];
                    }
                }
            }
        }


        for (int y = 0 ; y < boardHeight; y++){
            int count = 0;
            for (int x = 0; x < boardWidth; x++){
                if (grid[x][y] != null) {
                    count++;
                }
            }
            rowWidths[y] = count;
        }

        maxHeight = 0;
        for (int x = 0 ; x < boardWidth; x++){
            int y = boardHeight - 1;
            while (y >= 0){
                if (grid[x][y] != null){
                    break;
                }
                y--;
            }
            columnHeights[x] = y + 1;
            maxHeight = Math.max(maxHeight, y + 1);
        }

        return Result.PLACE;
    }

    @Override
    public Board testMove(Action act) {
        Board b = new TetrisBoard(boardWidth, boardHeight, grid, rowWidths, columnHeights,
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
            if (b.getCurrentPiece() != currentPiece){
                return false;
            }
            if (b.getCurrentPiecePosition() != piecePosition){
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
        int y = boardHeight - piece.getHeight();
        while (true){
            Point newPoint = new Point(x, y);
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
