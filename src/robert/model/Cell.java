package robert.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by robert on 03.06.16.
 */
public class Cell {
    public static final int SIZE = 3; // default width & height
    private static Cell[][] cells;
    private static final Random random = new Random();
    private static final Conditions conditions = Conditions.getConditions();

    private static final double CRITICAL_RO = 46842668.248;

    private boolean recrystallized = false;
    private boolean onEdge;
    private double ro;

    private int id;
    private final int x, y, cordX, cordY;
    private Color color;
    private boolean modified = false;

    public Cell(int i, int j) {
        x = i;
        y = j;
        cordX = i * SIZE;
        cordY = j * SIZE;
        this.id = conditions.getRandomId();
    }


    /*public void reset() {
        color = Color.WHITE;
    }*/

    public void reset() {
        modified = false;
        recrystallized = false;
        onEdge = false;
        ro = .0;
    }
    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    private void setColor(Color color) {
        this.color = color;
    }

    public void serColorById(int id) {
        // new RGB color
        color = conditions.getColorById(id);
    }

    public static void setCells(Cell[][] cells) {
        Cell.cells = cells;
    }


    public int getCordX() {
        return cordX;
    }

    public int getCordY() {
        return cordY;
    }

    public void check() {
        Cell otherCell;
        int myEnergy = this.calculateEnergy();

        // pick random neighbour
        for (int i = 0; i < 35; i++) { // finite amount of attempts, otherwise program will freeze
            int _x = random.nextInt(3) + x - 1;
            int _y = random.nextInt(3) + y - 1;
            try {
                otherCell = cells[_x][_y];
                int otherEnergy = otherCell.calculateEnergy();
                if (otherCell != this && !otherCell.modified && myEnergy <= otherEnergy) {
                    //System.out.println("my: " +myEnergy +"; other: " + otherEnergy);
                    otherCell.setId(this.id);
                    otherCell.setColor(this.color);
                    otherCell.modified = true;
                    break;
                }
            } catch (Exception e) {
            }
        }
    }

    private int calculateEnergy() {
        Cell otherCell;
        int myEnergy = 0;
        for (int j, i = x - 1; i < x + 2; i++) {
            for (j = y - 1; j < y + 2; j++) {
                if (i == x && j == y) continue;
                try {
                    otherCell = cells[i][j];
                    if (otherCell.id != this.id) {
                        ++myEnergy;
                    }
                } catch (Exception e) {
                }
            }
        }
        return myEnergy;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x=" + x +
                ", y=" + y +
                ", color=" + color +
                ", id=" + id +
                '}';
    }

    public void setModified() {
        this.modified = false;
    }

    public void setRandomId() {
        this.id = conditions.getRandomId();
    }


    public void checkIfOnEdge() {
        this.modified = false;
        Cell otherCell;
        for (int j, i = x - 1; i < x + 2; i++) {
            for (j = y - 1; j < y + 2; j++) {
                if (i == x && j == y) continue;
                try {
                    otherCell = cells[i][j];
                    if (otherCell.id != this.id) {
                        onEdge = true;
                        return;
                    }
                } catch (Exception e) {
                }
            }
        }
        onEdge = false;
    }

    public void addRo(double avgRo) {
        if (onEdge) {
            int r = random.nextInt(61);
            this.ro += (120.0 + r) * avgRo / 100;
        } else {
            double r = random.nextInt(31);
            this.ro += r * avgRo / 100;
        }
    }

    public void tryRec() {
        if (this.recrystallized || !this.onEdge) return;

        Cell otherCell;
        java.util.List<Cell> cellList = new ArrayList<>();
        for (int j, i = x - 1; i < x + 2; i++) {
            for (j = y - 1; j < y + 2; j++) {
                if (i == x && j == y) continue;
                try {
                    otherCell = cells[i][j];
                    if (otherCell.recrystallized && !otherCell.modified) {
                        cellList.add(otherCell);
                    }
                } catch (Exception e) {
                }
            }
        }

        if (!cellList.isEmpty()) {
            otherCell = cellList.get(random.nextInt(cellList.size()));
            this.setId(otherCell.getId());
            this.setColor(otherCell.getColor());
            this.recrystallized = true;
            this.modified = true;
            this.ro = .0;
        } else if (this.ro > CRITICAL_RO) {
            this.recrystallized = true;
            this.ro = .0;
            this.modified = true;
            this.setId(conditions.getRandomId());
            setColor(conditions.getColorById(this.id));
        }

    }
}
