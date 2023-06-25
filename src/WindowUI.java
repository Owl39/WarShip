package src;

import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class WindowUI extends JFrame implements UI, MouseListener {
    private static final int CELL_SIZE = 30;
    private static final int MATRIX_SIZE = 10;
    private int middleX;
    private int middleY;
    private CallbackListener _listener = null;
    CellState[][] userFieldCells = null;
    CellState[][] botFieldCells = null;
    private static final int HORIZONTAL_MARGIN = 30;
    private static final int CELLS_COUNT = 5;

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX() - middleX;
        int y = e.getY() - middleY;
        Coord coord = new Coord(y / CELL_SIZE, x / CELL_SIZE);
        if (y < 0 || x < 0 || (coord.Row < 0) || (coord.Row > 9) ||
                (coord.Column < 0) || (coord.Column > 9))
            return;
        if (_listener != null)
            _listener.onStep(coord);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void DrawGame() {

        addMouseListener(this);
        setTitle("WarShip");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setBackground(Color.WHITE);

        setSize((MATRIX_SIZE * CELL_SIZE) * 2 + HORIZONTAL_MARGIN * 3, 800);

        setLocationRelativeTo(null);
        setLayout(null);
        setVisible(true);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(_bufferedImage, 0, 0, null);
    }

    private void DrawScene(Graphics g) {
        middleX = (getWidth() + HORIZONTAL_MARGIN) / 2;
        middleY = (getHeight() / 2 - (MATRIX_SIZE * CELL_SIZE) / 2);
        String message = null;

        for (int i = 0; i < CELLS_COUNT; i++) {
            switch (i) {
                case 0 -> {
                    g.setColor(Color.WHITE);
                    message = "Water";
                }
                case 1 -> {
                    g.setColor(Color.red);
                    message = "Ship";
                }

                case 2 -> {
                    g.setColor(Color.BLUE);
                    message = "Bomb";
                }
                case 3 -> {
                    g.setColor(Color.BLACK);
                    message = "Hitted Ship";
                }
                case 4 -> {
                    g.setColor(Color.CYAN);
                    message = "Hitted Bomb";
                }
            }
            g.fillRect((getWidth() - ((CELL_SIZE + HORIZONTAL_MARGIN) * CELLS_COUNT) + (CELL_SIZE * CELLS_COUNT * i)) / 2, 100 - CELL_SIZE / 2, CELL_SIZE, CELL_SIZE);
            g.setColor(Color.GREEN);
            g.drawRect((getWidth() - ((CELL_SIZE + HORIZONTAL_MARGIN) * CELLS_COUNT) + (CELL_SIZE * CELLS_COUNT * i)) / 2, 100 - CELL_SIZE / 2, CELL_SIZE, CELL_SIZE);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            int textMargin = ((CELL_SIZE + HORIZONTAL_MARGIN) * CELLS_COUNT);
            if (i == 3 || i == 4)
                textMargin = ((CELL_SIZE + HORIZONTAL_MARGIN + getFont().getSize() / 2) * CELLS_COUNT);
            g.drawString(message, (getWidth() - textMargin + (CELL_SIZE * CELLS_COUNT * i)) / 2, 100 + CELL_SIZE);
        }

        DrawPanel(g, HORIZONTAL_MARGIN, middleY);
        DrawPanel(g, middleX, middleY);
        FillingField(g, userFieldCells, HORIZONTAL_MARGIN, middleY);
        FillingField(g, botFieldCells, middleX, middleY);
    }

    private BufferedImage _bufferedImage;

    public void DrawField(CellState[][] cells, boolean userField) {
        if (userField)
            userFieldCells = cells;
        else
            botFieldCells = cells;

        if (_bufferedImage == null)
            _bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = _bufferedImage.createGraphics();
        DrawScene(g2);
        g2.dispose();
    }

    @Override
    public void Repaint() {
        this.repaint();
    }

    private void FillingField(Graphics g, CellState[][] cells, int xOffset, int yOffset) {
        if (cells == null)
            return;

        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                switch (cells[i][j]) {
                    case Ship -> {
                        g.setColor(Color.red);
                        g.fillRect(xOffset + (j * CELL_SIZE), yOffset + i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    }
                    case HittedShip -> {
                        g.setColor(Color.BLACK);
                        g.fillRect(xOffset + (j * CELL_SIZE), yOffset + i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    }
                    case Bomb -> {
                        g.setColor(Color.BLUE);
                        g.fillRect(xOffset + (j * CELL_SIZE), yOffset + i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    }
                    case KilledBomb -> {
                        g.setColor(Color.CYAN);
                        g.fillRect(xOffset + (j * CELL_SIZE), yOffset + i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    }
                    case Water -> {
                        g.setColor(Color.WHITE);
                        g.fillRect(xOffset + (j * CELL_SIZE), yOffset + (i * CELL_SIZE), CELL_SIZE, CELL_SIZE);
                    }
                }
            }
        }
    }

    private void DrawPanel(Graphics g, int xOffset, int yOffset) {
        g.setColor(Color.lightGray);
        g.fillRect(xOffset, yOffset, CELL_SIZE * MATRIX_SIZE, CELL_SIZE * MATRIX_SIZE);

        for (int i = 0; i < MATRIX_SIZE; i++) {
            g.setColor(Color.gray);
            g.drawLine(xOffset, i * CELL_SIZE + yOffset, xOffset + CELL_SIZE * MATRIX_SIZE, i * CELL_SIZE + yOffset);
            g.drawLine(i * CELL_SIZE + xOffset, yOffset, i * CELL_SIZE + xOffset, yOffset + CELL_SIZE * MATRIX_SIZE);
            g.drawRect(xOffset, yOffset, CELL_SIZE * MATRIX_SIZE, CELL_SIZE * MATRIX_SIZE);
        }
    }

    public void ShowMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Info message", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void SetCallbackListener(CallbackListener listener) {
        _listener = listener;
    }
}
