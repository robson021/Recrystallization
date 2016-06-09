package robert.gui;

import robert.model.Conditions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by robert on 03.06.16.
 */
public class MainFrame extends JFrame {
    private static MainFrame self = null;

    private final Board board;
    private final JButton startButton, stopButton, recButton;
    private Thread boardThread = null;
    private final JLabel infoLabel;

    private MainFrame() {
        super("Monte Carlo Method + recrystallization");
        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new FlowLayout());
        board = new Board();

        infoLabel = new JLabel("Info");

        startButton = new JButton("Start");
        startButton.addActionListener(new StartButtonAction());
        stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> {
            board.setRec(false);
            board.stopThread();
            try {
                boardThread.join();
                System.out.println("Thread joined");
            } catch (Exception e1) {
            } finally {
                boardThread = null;
                startButton.setEnabled(true);
                infoLabel.setText("Finished");
            }
        });

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            if (board.isRunning()) {
                infoLabel.setText("Can not clean while board is running. Stop it first.");
                return;
            }
            Conditions.getConditions().initNewColors();
            board.clearBoard();
            infoLabel.setText("Cleared");
        });

        northPanel.add(infoLabel);
        JPanel southPanel = new JPanel(new FlowLayout());
        southPanel.add(startButton);
        southPanel.add(stopButton);
        southPanel.add(clearButton);
        recButton = new JButton("Recrystallize");
        recButton.addActionListener(new RecrystallizationAction());
        southPanel.add(recButton);

        // adding to the frame
        this.add(northPanel, BorderLayout.NORTH);
        this.add(board, BorderLayout.CENTER);
        this.add(southPanel, BorderLayout.SOUTH);

        //noinspection MagicConstant
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setResizable(false);
        Dimension size = board.getSize();
        size.setSize(size.getWidth(), size.getHeight() + 75);
        setSize(size);
        System.out.println("Main frame ready");
    }

    public static MainFrame getFrame() {
        if (self == null) {
            self = new MainFrame();
        }
        return self;
    }

    private class StartButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!board.isRunning() && boardThread == null) {
                startButton.setEnabled(false);
                infoLabel.setText("Started");
                boardThread = new Thread(board);
                boardThread.start();
            }
        }
    }

    private class RecrystallizationAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (board.isRunning() || board.isRec()) return;
            new Thread(() -> {
                board.recrystallize();
            }).start();
            System.out.println("Recrystallization started.");
        }
    }

    public JButton getStartButton() {
        return startButton;
    }
}
