package assignment;

import java.awt.*;

/**
 * An immutable representation of a tetris piece in a particular rotation.
 * 
 * All operations on a TetrisPiece should be constant time, except for its
 * initial construction. This means that rotations should also be fast - calling
 * clockwisePiece() and counterclockwisePiece() should be constant time! You may
 * need to do pre-computation in the constructor to make this possible.
 */
public final class TetrisPiece implements Piece {

    //Instance variables with all necessary information stored, allowing quick returns
    private PieceType self;
    private int rotationIndex;
    private int width;
    private int height;
    private int[] skirt;
    private Point[] body;
    private TetrisPiece counterClockwise;
    private TetrisPiece clockwise;

    /**
     * Construct a tetris piece of the given type. The piece should be in its spawn orientation,
     * i.e., a rotation index of 0.
     * 
     * You may freely add additional constructors, but please leave this one - it is used both in
     * the runner code and testing code.
     */
    public TetrisPiece(PieceType type) {
        self = type;
        rotationIndex = 0;
        //Use the bounding box to get all the information about a piece
        Dimension d = type.getBoundingBox();
        width = (int)d.getWidth();
        height = (int)d.getHeight();
        body = type.getSpawnBody();

        skirt = Helper.createSkirt(body, width);

        //Generate body for clockwise rotation, pass along other parameters
        Point[] newBody = Helper.rotateClockwise(body, width, height);
        counterClockwise = null;
        //Use secondary constructor to initialize clockwise rotation piece
        clockwise = new TetrisPiece(type, 1, height, width, newBody, this, this);
        // TODO: Implement me.
    }

    public TetrisPiece(PieceType type, int rIndex, int w, int h,
                       Point[] b, TetrisPiece previous, TetrisPiece original){
        self = type;
        rotationIndex = rIndex;
        width = w;
        height = h;
        body = b;
        counterClockwise = previous;

        skirt = Helper.createSkirt(body, width);

        counterClockwise = previous;
        if (rotationIndex == 3){
            //3rd clockwise rotation is the counterclockwise rotation of the original
            clockwise = original;
            original.setCounterClockwise(this);
        }
        else{
            //Continue generating clockwise rotations until 3rd generation
            Point[] newBody = Helper.rotateClockwise(b, w, h);
            clockwise = new TetrisPiece(type, rIndex + 1, h, w, newBody, this, original);
        }
    }

    //Setter method that can be accessed by other pieces
    public void setCounterClockwise(TetrisPiece c) {
        counterClockwise = c;
    }

    @Override
    public PieceType getType() {
        return self;
    }

    @Override
    public int getRotationIndex() {
        // TODO: Implement me.
        return rotationIndex;
    }

    @Override
    public Piece clockwisePiece() {
        // TODO: Implement me.
        return clockwise;
    }

    @Override
    public Piece counterclockwisePiece() {
        // TODO: Implement me.
        return counterClockwise;
    }

    @Override
    public int getWidth() {
        // TODO: Implement me.
        return width;
    }

    @Override
    public int getHeight() {
        // TODO: Implement me.
        return height;
    }

    @Override
    public Point[] getBody() {
        // TODO: Implement me.
        return body;
    }

    @Override
    public int[] getSkirt() {
        // TODO: Implement me.
        return skirt;
    }

    @Override
    public boolean equals(Object other) {
        // Ignore objects which aren't also tetris pieces.
        if(!(other instanceof TetrisPiece)) return false;
        TetrisPiece otherPiece = (TetrisPiece) other;

        //Checking that piece is correct type and orientation
        if (self.equals(((TetrisPiece) other).getType())){
            if (rotationIndex == ((TetrisPiece) other).getRotationIndex()){
                return true;
            }
        }
        // TODO: Implement me.
        return false;
    }
}
