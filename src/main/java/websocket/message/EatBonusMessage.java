package websocket.message;

import model.Snake.Snake;

/**
 * Created by egor on 23.04.15.
 */
public class EatBonusMessage extends Message {
    private int bonus_id;
    private Snake eater;
    public EatBonusMessage(int bonus_id, Snake eater) {
        this.bonus_id = bonus_id;
        this.eater = eater;
    }

    public int getBonus_id() {
        return bonus_id;
    }

    public Snake getEater() {
        return eater;
    }
}
