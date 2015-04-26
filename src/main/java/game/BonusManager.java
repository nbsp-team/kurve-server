package game;

import model.Bonus.Bonus;
import model.Bonus.Effects.*;
import model.Snake.Snake;
import websocket.message.NewBonusMessage;
import websocket.message.EatBonusMessage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by egor on 22.04.15.
 */
public class BonusManager {
    List<TemporaryEffect> activeEffects = new LinkedList<>();
    List<Snake> snakes;
    List<Bonus> bonuses = new LinkedList<>();
    Room room;

    public BonusManager(List<Snake> snakes, Room room){
        this.room = room;
        this.snakes = snakes;
    }

    private void addBonus(Bonus bonus){
        bonuses.add(bonus);
        room.broadcastMessage(new NewBonusMessage(bonus));
    }

    private void applyTempEffect(TemporaryEffect effect){
        effect.activate();
        activeEffects.add(effect);
    }

    private void applyBonus(Bonus bonus, Snake snake){

        switch(bonus.getKind()){
            case SPEED_SELF:
                applyTempEffect(new SpeedSelfEffect(snake));
                break;
            case THIN_SELF:
                applyTempEffect(new ThinSelfEffect(snake));
                break;
            case ERASE_SELF:
                snake.eraseSelf();
                break;
            case SLOW_SELF:
                applyTempEffect(new SlowSelfEffect(snake));
                break;
            case BIG_HOLE_SELF:
                applyTempEffect(new BigHoleSelfEffect(snake));
                break;
            case TRAVERSE_WALLS_SELF:
                applyTempEffect(new TravWallsSelfEffect(snake));
                break;
        }
    }

    private int c = 0;
    public void timeStep(){
        for(Iterator<TemporaryEffect> iter = activeEffects.iterator(); iter.hasNext();){
            TemporaryEffect effect = iter.next();
            if(effect.timeStep()){
                effect.deactivate();
                iter.remove();
            }
        }
        for(Snake snake : snakes){
            for(Iterator<Bonus> bonusIter = bonuses.iterator();bonusIter.hasNext();){
                Bonus bonus = bonusIter.next();
                if (bonus.isReachableBy(snake)){

                    applyBonus(bonus, snake);
                    room.broadcastMessage(new EatBonusMessage(bonus.getId()));
                    bonusIter.remove();
                }
            }
        }
        c++;
        if(c == 60*3) {
            addBonus(new Bonus(100, 100, Bonus.Kind.TRAVERSE_WALLS_SELF));
            addBonus(new Bonus(100, 200, Bonus.Kind.SPEED_SELF));
            addBonus(new Bonus(100, 300, Bonus.Kind.THIN_SELF));
        }
    }
}
