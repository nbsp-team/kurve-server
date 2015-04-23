package websocket.message;

import model.Bonus.Bonus;

/**
 * Created by egor on 23.04.15.
 */
public class EatBonusMessage extends Message {
    private int bonus_id;
    public EatBonusMessage(int bonus_id){
        this.bonus_id = bonus_id;
    }

    public int getBonus_id() {
        return bonus_id;
    }

}
