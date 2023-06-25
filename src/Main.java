package src;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            WindowUI ui = new WindowUI();
            ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ui.setSize(600, 600);
            Game game = new Game(ui);
            game.Start();

            ui.setVisible(true);
        } catch (Exception exc) {
            System.err.println(exc.getLocalizedMessage());
        }
    }
}
