package websocket.message;

import model.Snake.Snake;

/**
 * egor, 18.04.15.
 */
public class SnakeUpdateMessage extends Message {
    private Snake snake;

    public SnakeUpdateMessage(Snake snake) {

        this.snake = snake;
    }

    public Snake getSnake() {
        return snake;
    }


}
