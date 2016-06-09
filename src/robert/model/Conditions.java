package robert.model;

import robert.gui.Board;

import java.awt.*;
import java.util.Random;

/**
 * Created by robert on 03.06.16.
 */
public class Conditions {
    private static Conditions self = null;
    private static final int RANGE = Board.getMatrixSize(); // type of colors and IDs
    private final Random r = new Random();
    private final Color[] colors;

    private Conditions() {
        colors = new Color[RANGE];
        initNewColors();
    }

    public void initNewColors() {
        for (int i = 0; i < RANGE; i++) {
            colors[i] = new Color(r.nextFloat(), r.nextFloat(), r.nextFloat());
        }
    }

    public Color getColorById(int id) {
        return colors[id];
    }

    public static Conditions getConditions() {
        if (self == null) {
            self = new Conditions();
            System.out.println("Singleton init");
        }
        return self;
    }

    public int getRandomId() {
        return r.nextInt(RANGE);
    }
}
