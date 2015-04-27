package interfaces;

/**
 * Created by egor on 20.04.15.
 */

public interface GameField {

    public void play();

    public void pause();

    public void doLeftDown(int sender);

    public void doLeftUp(int sender);

    public void doRightDown(int sender);

    public void doRightUp(int sender);


}
