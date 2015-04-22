package game;

import model.Bonus.Bonus;
import model.Bonus.BonusEffect;
import model.Bonus.Effects.TemporaryEffect;
import model.Bonus.Effects.SpeedSelfEffect;
import model.Snake.Snake;

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

    public BonusManager(List<Snake> snakes){
        this.snakes = snakes;
        bonuses.add(new Bonus(375, 400, Bonus.Kind.SPEED_SELF));
    }

    public void applyBonus(Bonus bonus, Snake snake){

        switch(bonus.getKind()){
            case SPEED_SELF:
                TemporaryEffect effect = new SpeedSelfEffect(snake);
                effect.activate();
                activeEffects.add(effect);
                break;
            case THIN_SELF:
                //activeEffects.add(new BonusEffect(bonus, snake, 3));
        }
    }
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
                    System.out.println("nom");
                    applyBonus(bonus, snake);
                    bonusIter.remove();
                }
            }
        }
    }
}
