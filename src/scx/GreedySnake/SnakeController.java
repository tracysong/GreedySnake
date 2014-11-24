package scx.GreedySnake;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Controller interface.
 * 
 * @author Chenxi Song
 * 
 * @mathmodel <pre>
 * {@code type SnakeController is modeled by
 *   (model: SnakeModel,
 *    view: SnakeView)}
 * </pre>
 * @initially <pre>
 * {@code (SnakeModel model, SnakeView view):
 *   ensures
 *     this.model = model  and
 *     this.view = view}
 * </pre>
 */
public class SnakeController implements KeyListener {
    SnakeModel model;

    public SnakeController(SnakeModel model) {
        this.model = model;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        /*
         * response to the user keyboard input during the game
         */
        if (this.model.running) {
            switch (keyCode) {
                case KeyEvent.VK_UP:
                    this.model.changeDirection(SnakeModel.UP);
                    break;
                case KeyEvent.VK_DOWN:
                    this.model.changeDirection(SnakeModel.DOWN);
                    break;
                case KeyEvent.VK_LEFT:
                    this.model.changeDirection(SnakeModel.LEFT);
                    break;
                case KeyEvent.VK_RIGHT:
                    this.model.changeDirection(SnakeModel.RIGHT);
                    break;
                case KeyEvent.VK_ADD:
                case KeyEvent.VK_PAGE_UP:
                    this.model.speedUp();
                    break;
                case KeyEvent.VK_SUBTRACT:
                case KeyEvent.VK_PAGE_DOWN:
                    this.model.speedDown();
                    break;
                case KeyEvent.VK_SPACE:
                case KeyEvent.VK_P:
                    this.model.changePauseState();
                    break;
                default:
            }
        }

        /*
         * restart the game if any button is pressed
         */
        if (keyCode == KeyEvent.VK_R || keyCode == KeyEvent.VK_S
                || keyCode == KeyEvent.VK_ENTER) {
            this.model.reset();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}