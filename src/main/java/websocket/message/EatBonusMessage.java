package websocket.message;

import model.Snake.Snake;

/**
 * Created by egor on 23.04.15.
 */
public class EatBonusMessage extends Message {
    private int bonusId;
    private Snake eater;
    public EatBonusMessage(int bonusId, Snake eater) {
        this.bonusId = bonusId;
        this.eater = eater;
    }

    public int getBonusId() {
        return bonusId;
    }

    public Snake getEater() {
        return eater;
    }
}
