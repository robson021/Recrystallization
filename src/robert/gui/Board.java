package robert.gui;

import robert.model.Cell;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by robert on 03.06.16.
 */
public class Board extends JPanel implements Runnable {
    private static final int SIZE_Y = 220;
    private static final int SIZE_X = SIZE_Y * 2;
    private final Cell[][] cells = new Cell[SIZE_X][SIZE_Y];

    private final java.util.List<Cell> cellList = new ArrayList<>();

    private boolean running = false;

    public Board() {
        setBackground(Color.WHITE);

        int xy = SIZE_Y * Cell.SIZE;
        setSize(new Dimension(xy * 2, xy + 20));

        Cell.setCells(cells);

        for (int j, i = 0; i < SIZE_X; i++) {
            for (j = 0; j < SIZE_Y; j++) {
                Cell cell = new Cell(i, j);
                cells[i][j] = cell;
                cellList.add(cell);
            }
        }
        clearBoard();
        System.out.println("Board ready");
    }


    public void clearBoard() {
        Cell cell;
        for (int j, i = 0; i < SIZE_X; i++) {
            for (j = 0; j < SIZE_Y; j++) {
                cell = cells[i][j];
                cell.setRandomId();
                cell.serColorById(cell.getId());
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g.create();
        Rectangle rect;
        for (int x, y, j, i = 0; i < SIZE_X; i++) {
            for (j = 0; j < SIZE_Y; j++) {
                graphics2D.setColor(cells[i][j].getColor());
                x = cells[i][j].getCordX();
                y = cells[i][j].getCordY();
                rect = new Rectangle(x, y, Cell.SIZE, Cell.SIZE);
                graphics2D.fill(rect);
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void stopThread() {
        this.running = false;
    }

    @Override
    public void run() {
        running = true;
        System.out.println("Board thread started");
        int i = 0;
        while (running) {
            resetModifyFlag();
            Collections.shuffle(this.cellList, new Random(System.nanoTime())); // random access order
            cellList.forEach(Cell::check);
            repaint();
            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
            }
            System.out.println("Cycle " + (++i) + " done");
        }
        System.out.println("Board thread finished");
    }

    private void resetModifyFlag() {
        cellList.forEach(Cell::setModified);
    }


    public static int getMatrixSize() {
        return SIZE_Y * SIZE_X;
    }
}
