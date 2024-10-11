package assignment;

import java.util.ArrayList;

public class myBrain implements Brain{

    private ArrayList<Board> options;
    private ArrayList<Board.Action> firstMoves;

    private double[] parameters;

    public myBrain(double[] parameters){
        this.parameters = parameters;
    }

    /**
     * Decide what the next move should be based on the state of the board.
     */
    public Board.Action nextMove(Board currentBoard) {

        // Fill the our options array with versions of the new Board
        options = new ArrayList<>();
        firstMoves = new ArrayList<>();
        enumerateOptions(currentBoard);

        double best = 0;
        int bestIndex = 0;

        // Check all of the options and get the one with the highest score
        for (int i = 0; i < options.size(); i++) {
            double score = scoreBoard(options.get(i));
            if (score > best) {
                best = score;
                bestIndex = i;
            }
        }

        // We want to return the first move on the way to the best Board
        return firstMoves.get(bestIndex);




    }

    /**
     * Test all of the places we can put the current Piece.
     * Since this is just a Lame Brain, we aren't going to do smart
     * things like rotating pieces.
     */
    private void enumerateOptions(Board currentBoard) {





        // All possible landing spots if you don't rotate the piece.


        options.add(currentBoard.testMove(Board.Action.DROP));
        firstMoves.add(Board.Action.DROP);

        // Now we'll add all the places to the left we can DROP
        Board left = currentBoard.testMove(Board.Action.LEFT);
        while (left.getLastResult() == Board.Result.SUCCESS) {
            options.add(left.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.LEFT);
            left.move(Board.Action.LEFT);
        }
        // And then the same thing to the right
        Board right = currentBoard.testMove(Board.Action.RIGHT);
        while (right.getLastResult() == Board.Result.SUCCESS) {
            options.add(right.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.RIGHT);
            right.move(Board.Action.RIGHT);
        }

        // All possible landing spots if you rotate the piece clockwise.


        Board clockwise = currentBoard.testMove(Board.Action.CLOCKWISE);

        options.add(clockwise.testMove(Board.Action.DROP));
        firstMoves.add(Board.Action.CLOCKWISE);


        // Now we'll add all the places to the left we can DROP
        Board clockwise_left = clockwise.testMove(Board.Action.LEFT);
        while (clockwise_left.getLastResult() == Board.Result.SUCCESS) {
            options.add(clockwise_left.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.CLOCKWISE);
            clockwise_left.move(Board.Action.LEFT);
        }
        // And then the same thing to the right
        Board clockwise_right = currentBoard.testMove(Board.Action.RIGHT);
        while (clockwise_right.getLastResult() == Board.Result.SUCCESS) {
            options.add(clockwise_right.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.CLOCKWISE);
            clockwise_right.move(Board.Action.RIGHT);
        }


        // All possible landing spots if you rotate the piece counterclockwise.


        Board counterclockwise = currentBoard.testMove(Board.Action.COUNTERCLOCKWISE);

        options.add(counterclockwise.testMove(Board.Action.DROP));
        firstMoves.add(Board.Action.COUNTERCLOCKWISE);


        // Now we'll add all the places to the left we can DROP
        Board counterclockwise_left = counterclockwise.testMove(Board.Action.LEFT);
        while (counterclockwise_left.getLastResult() == Board.Result.SUCCESS) {
            options.add(counterclockwise_left.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.COUNTERCLOCKWISE);
            counterclockwise_left.move(Board.Action.LEFT);
        }
        // And then the same thing to the right
        Board counterclockwise_right = currentBoard.testMove(Board.Action.RIGHT);
        while (counterclockwise_right.getLastResult() == Board.Result.SUCCESS) {
            options.add(counterclockwise_right.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.COUNTERCLOCKWISE);
            counterclockwise_right.move(Board.Action.RIGHT);
        }





    }

    /**
     * Since we're trying to avoid building too high,
     * we're going to give higher scores to Boards with
     * MaxHeights close to 0.
     */
    private double scoreBoard(Board newBoard) {

        // computing feature 1: aggregate height

        int aggregateHeight = 0;

        for (int x = 0; x < newBoard.getWidth(); x++){
            aggregateHeight += newBoard.getColumnHeight(x);
        }


        // computing feature 2: number of completed lines.

        int numCompletedLines = newBoard.getRowsCleared();


        // computing feature 3: bumpiness

        int bumpiness = 0;

        for (int x = 0; x < newBoard.getWidth() - 1; x++){
            bumpiness += Math.abs(newBoard.getColumnHeight(x) - newBoard.getColumnHeight(x + 1));
        }


        double score = parameters[0] * aggregateHeight + parameters[1] * numCompletedLines + parameters[2] * bumpiness;

        return score;
    }
}
