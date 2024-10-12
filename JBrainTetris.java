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

        double[] parameters = new double[5];

        // Example settings: tune around with these parameters, you could use a genetic algorithm to do this.
        // These are some pretty good settings for the parameters. The intuition behind this is in the paper.
        parameters[0] = -5.0;
        parameters[1] = 5.0;
        parameters[2] = -5.0;
        parameters[3] = -5.0;
        parameters[4] = 1000.0;


        myBrain mybrain = new myBrain(parameters);

        // Redefining the timer so that every tick it automates an action using the brain.
        timer = new javax.swing.Timer(DELAY, new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                Board.Action nextMove = mybrain.nextMove(board);
                tick(nextMove);

                tick(Board.Action.DOWN);


            }
        });


    }



}
