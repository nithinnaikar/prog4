package assignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class JBrainTetris extends JTetris {

    public static void main(String[] args) {
        createGUI(new JBrainTetris());
    }

    JBrainTetris() {
        super();

        double[] parameters = new double[3];

        // Example settings: tune around with these weights, you could use a genetic algorithm to do this but we ran out of time.
        parameters[0] = 100.0;
        parameters[1] = -100.0;
        parameters[2] = 100.0;


        myBrain mybrain = new myBrain(parameters);

        // Redefining the timer.
        timer = new javax.swing.Timer(DELAY, new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                Board.Action nextMove = mybrain.nextMove(board);
                tick(nextMove);

                tick(Board.Action.DOWN);


            }
        });
    }




}
















