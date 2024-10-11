package assignment;

import java.awt.*;

public class Helper {
    //Generate body of clockwise rotation
    public static Point[] rotateClockwise(Point[] points, int width, int height){
        Point[] result = new Point[points.length];
        for (int i = 0; i < points.length; i++){
            //Clockwise rotation of (x, y) is (-y, x)
            int x = (int) points[i].getX();
            int y = (int) points[i].getY();
            //Coordinates are adjusted using half of width and height to center the origin
            int newX = y - height / 2 + width / 2;
            int newY = -x + width / 2 + height / 2 - 1 + width % 2;
            // - 1 + width % 2 fixes integer division issues with negative x
            Point newPoint = new Point(newX, newY);
            result[i] = newPoint;
        }
        return result;
    }

    public static int[] createSkirt(Point[] body, int width){
        int[] result = new int[width];
        for (int i = 0; i < width; i++){
            int min = Integer.MAX_VALUE;
            for (Point p : body){
                if (p.getX() == i){
                    //Set minimum if a new minimum in the column is found
                    min = (int) Math.min(min, p.getY());
                }
            }
            result[i] = min;
        }
        return result;
    }

    public static boolean inBounds(Piece.PieceType[][] grid, Piece piece, Point point){
        Point[] body = piece.getBody();
        for (Point p: body){
            //Add relative coordinates together to get absolute coordinates
            int x = (int)p.getX() + (int)point.getX();
            int y = (int)p.getY() + (int)point.getY();
            //If empty or off the board, return false
            if (x < 0 || x >= grid.length){
                return false;
            }
            if (y < 0 || y >= grid[0].length){
                return false;
            }
            if (grid[x][y] != null){
                return false;
            }
        }
        return true;
    }

    //Modifies the grid to cement current piece onto board
    public static void place(Piece.PieceType[][] grid, Piece piece, Point point){
        Point[] body = piece.getBody();
        for (Point p : body){
            //Add relative coordinates together to get absolute coordinates
            int x = (int)p.getX() + (int)point.getX();
            int y = (int)p.getY() + (int)point.getY();
            grid[x][y] = piece.getType();
        }
    }
}
