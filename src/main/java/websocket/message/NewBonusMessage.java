package websocket.message;

import model.Bonus.Bonus;

/**
 * Created by egor on 23.04.15.
 */
public class NewBonusMessage extends Message {
    Bonus bonus;

    public NewBonusMessage(Bonus bonus) {
        this.bonus = bonus;
    }

    public Bonus getBonus() {
        return bonus;
    }

}
