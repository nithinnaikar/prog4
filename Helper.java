package assignment;

import java.awt.*;

public class Helper {
    public static Point[] rotateClockwise(Point[] points, int width, int height){
        Point[] result = new Point[points.length];
        for (int i = 0; i < points.length; i++){
            int x = (int) points[i].getX();
            int y = (int) points[i].getY();
            int newX = y - height / 2 + width / 2;
            int newY = -x + width / 2 + height / 2 - 1 + width % 2;
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
            int x = (int)p.getX() + (int)point.getX();
            int y = (int)p.getY() + (int)point.getY();
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

    public static void place(Piece.PieceType[][] grid, Piece piece, Point point){
        Point[] body = piece.getBody();
        for (Point p : body){
            int x = (int)p.getX() + (int)point.getX();
            int y = (int)p.getY() + (int)point.getY();
            grid[x][y] = piece.getType();
        }
    }
}
