package scx.GreedySnake;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * View interface.
 * 
 * @author Chenxi Song
 */
public class SnakeView implements Observer {
    SnakeController control = null;
    SnakeModel model = null;

    JFrame mainFrame;
    Canvas paintCanvas;
    JLabel labelScore;

    public static final int canvasWidth = 200;
    public static final int canvasHeight = 300;

    public static final int nodeWidth = 10;
    public static final int nodeHeight = 10;

    public SnakeView(SnakeModel model, SnakeController control) {
        this.model = model;
        this.control = control;

        this.mainFrame = new JFrame("SnakeGame");

        Container cp = this.mainFrame.getContentPane();

        // top: show the score 
        this.labelScore = new JLabel("Score:");
        cp.add(this.labelScore, BorderLayout.NORTH);

        // middle: map of the game
        this.paintCanvas = new Canvas();
        this.paintCanvas.setSize(canvasWidth + 1, canvasHeight + 1);
        this.paintCanvas.addKeyListener(control);
        cp.add(this.paintCanvas, BorderLayout.CENTER);

        // bottom: help/user instruction
        JPanel panelButtom = new JPanel();
        panelButtom.setLayout(new BorderLayout());
        JLabel labelHelp;
        labelHelp = new JLabel("PageUp, PageDown for speed;", JLabel.CENTER);
        panelButtom.add(labelHelp, BorderLayout.NORTH);
        labelHelp = new JLabel("ENTER or R or S for start;", JLabel.CENTER);
        panelButtom.add(labelHelp, BorderLayout.CENTER);
        labelHelp = new JLabel("SPACE or P for pause", JLabel.CENTER);
        panelButtom.add(labelHelp, BorderLayout.SOUTH);
        cp.add(panelButtom, BorderLayout.SOUTH);

        this.mainFrame.addKeyListener(control);
        this.mainFrame.pack();
        this.mainFrame.setResizable(false);
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mainFrame.setVisible(true);
    }

    void repaint() {
        Graphics g = this.paintCanvas.getGraphics();

        //draw background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, canvasWidth, canvasHeight);

        // draw the snake
        g.setColor(Color.BLACK);
        LinkedList<Node> na = this.model.nodeArray;
        Iterator<Node> it = na.iterator();
        while (it.hasNext()) {
            Node n = it.next();
            this.drawNode(g, n);
        }

        // draw the food
        g.setColor(Color.RED);
        Node n = this.model.food;
        this.drawNode(g, n);

        this.updateScore();
    }

    private void drawNode(Graphics g, Node n) {
        g.fillRect(n.x * nodeWidth, n.y * nodeHeight, nodeWidth - 1,
                nodeHeight - 1);
    }

    public void updateScore() {
        String s = "Score: " + this.model.score;
        this.labelScore.setText(s);
    }

    @Override
    public void update(Observable o, Object arg) {
        this.repaint();
    }
}
