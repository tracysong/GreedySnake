package scx.GreedySnake;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Random;

import javax.swing.JOptionPane;

/**
 * Model interface.
 * 
 * @author Chenxi Song
 */
class SnakeModel extends Observable implements Runnable {
    boolean[][] matrix; // to place the food
    LinkedList<Node> nodeArray = new LinkedList<Node>(); // the snake
    Node food;
    int maxX;
    int maxY;
    int direction = 2; // direction of the snake
    boolean running = false;

    int timeInterval = 200; // timeInterval: 200ms
    double speedChangeRate = 0.75;
    boolean paused = false; // stop sign

    int score = 0;
    int countMove = 0; // steps before eat the food

    // UP and DOWN should be even
    // RIGHT and LEFT should be odd
    public static final int UP = 2;
    public static final int DOWN = 4;
    public static final int LEFT = 1;
    public static final int RIGHT = 3;

    public SnakeModel(int maxX, int maxY) {
        this.maxX = maxX;
        this.maxY = maxY;

        this.reset();
    }

    public void reset() {
        this.direction = SnakeModel.UP;
        this.timeInterval = 200;
        this.paused = false;
        this.score = 0;
        this.countMove = 0;

        // initial matrix, all set to 0
        this.matrix = new boolean[this.maxX][];
        for (int i = 0; i < this.maxX; ++i) {
            this.matrix[i] = new boolean[this.maxY];
            Arrays.fill(this.matrix[i], false);
        }

        /*
         * initial the snake
         */

        int initArrayLength = this.maxX > 20 ? 10 : this.maxX / 2;
        this.nodeArray.clear();
        for (int i = 0; i < initArrayLength; ++i) {
            int x = this.maxX / 2 + i;
            int y = this.maxY / 2;
            this.nodeArray.addLast(new Node(x, y));
            this.matrix[x][y] = true;
        }

        this.food = this.createFood();
        this.matrix[this.food.x][this.food.y] = true;
    }

    public void changeDirection(int newDirection) {
        // Constraints of direction change 
        if (this.direction % 2 != newDirection % 2) {
            this.direction = newDirection;
        }
    }

    public boolean moveOn() {
        Node n = this.nodeArray.getFirst();
        int x = n.x;
        int y = n.y;

        switch (this.direction) {
            case UP:
                y--;
                break;
            case DOWN:
                y++;
                break;
            case LEFT:
                x--;
                break;
            case RIGHT:
                x++;
                break;
        }

        // when the new node is in the map
        if ((0 <= x && x < this.maxX) && (0 <= y && y < this.maxY)) {

            if (this.matrix[x][y]) { // if the new pixel is filled
                if (x == this.food.x && y == this.food.y) { // get the food
                    this.nodeArray.addFirst(this.food); // snake grows
                    /*
                     * the score is related to both the times of direction
                     * changes and the speed of the snake
                     */

                    int scoreGet = (10000 - 200 * this.countMove)
                            / this.timeInterval;
                    this.score += scoreGet > 0 ? scoreGet : 10;
                    this.countMove = 0;

                    this.food = this.createFood();
                    this.matrix[this.food.x][this.food.y] = true; // set place for food
                    return true;
                } else {
                    return false;
                }

            } else { // the new spot is empty, move the snake
                this.nodeArray.addFirst(new Node(x, y));
                this.matrix[x][y] = true;
                n = this.nodeArray.removeLast();
                this.matrix[n.x][n.y] = false;
                this.countMove++;
                return true;
            }
        }
        return false; // hit the wall, failed
    }

    @Override
    public void run() {
        this.running = true;
        while (this.running) {
            try {
                Thread.sleep(this.timeInterval);
            } catch (Exception e) {
                break;
            }

            if (!this.paused) {
                if (this.moveOn()) {
                    this.setChanged();
                    this.notifyObservers();
                } else {
                    JOptionPane.showMessageDialog(null, "you failed",
                            "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
        }
        this.running = false;
    }

    private Node createFood() {
        int x = 0;
        int y = 0;
        // random a spot to put the food
        do {
            Random r = new Random();
            x = r.nextInt(this.maxX);
            y = r.nextInt(this.maxY);
        } while (this.matrix[x][y]);

        return new Node(x, y);
    }

    public void speedUp() {
        this.timeInterval *= this.speedChangeRate;
    }

    public void speedDown() {
        this.timeInterval /= this.speedChangeRate;
    }

    public void changePauseState() {
        this.paused = !this.paused;
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < this.nodeArray.size(); ++i) {
            Node n = this.nodeArray.get(i);
            result += "[" + n.x + "," + n.y + "]";
        }
        return result;
    }
}

class Node {
    int x;
    int y;

    Node(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
