package scx.GreedySnake;

/*
 * This is a simple GreedySnake game
 */
public class SnakeGame {
    public static void main(String[] args) {
        SnakeModel model = new SnakeModel(20, 30);
        SnakeController control = new SnakeController(model);
        SnakeView view = new SnakeView(model, control);
        model.addObserver(view);
        (new Thread(model)).start();
    }
}