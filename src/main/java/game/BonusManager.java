package game;

import main.Main;
import model.Bonus.Bonus;
import model.Bonus.Effects.*;
import model.Snake.Snake;
import utils.MathUtils;
import websocket.message.EatBonusMessage;
import websocket.message.NewBonusMessage;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by egor on 22.04.15.
 */
public class BonusManager {
    public static final int DEFAULT_BONUS_JITTER = 2 * 60;
    public static final int BONUS_SPAWN_PADDING = 60;

    private List<TemporaryEffect> activeEffects = new LinkedList<>();
    private List<Snake> snakes;
    private List<Bonus> bonuses = new LinkedList<>();
    private Room room;
    private int bonusProbSum = 0;
    private int[] bonusProbabilities = new int[Bonus.Kind.values().length];

    private int bonusSpawnProbability;

    public BonusManager(List<Snake> snakes, Room room) {
        this.room = room;
        this.snakes = snakes;

        bonusSpawnProbability = Integer.valueOf(Main.mechanicsConfig.bonusSpawnProbability);

        bonusProbSum = 0;
        for (int i = 0; i < Main.mechanicsConfig.bonusProbabilities.size(); ++i) {
            int prob = Integer.parseInt(Main.mechanicsConfig.bonusProbabilities.get(i));
            bonusProbSum += prob;
            bonusProbabilities[i] = bonusProbSum;
        }
    }

    public void addBonus(Bonus bonus) {
        bonuses.add(bonus);
        room.broadcastMessage(new NewBonusMessage(bonus));
    }

    private void applyTempEffect(TemporaryEffect effect) {
        effect.activate();
        activeEffects.add(effect);
    }

    private void applyBonus(Bonus bonus, Snake snake) {

        switch (bonus.getKind()) {
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
            case SHARP_CORNERS_SELF:
                applyTempEffect(new SharpCornersSelfEffect(snake));
                break;
            case SPEED_ENEMY:
                for (Snake other : snakes) {
                    if (other != snake) applyTempEffect(new SpeedSelfEffect(other));
                }
                break;
            case THICK_ENEMY:
                applyTempEffect(new ThickEnemyEffect(snake, snakes));
                break;
            case SLOW_ENEMY:
                applyTempEffect(new SlowEnemyEffect(snake, snakes));
                break;
            case BIG_TURNS_ENEMY:
                applyTempEffect(new BigTurnsEnemyEffect(snake, snakes));
                break;
            case TRAVERSE_WALLS_ALL:
                applyTempEffect(new TraverseWallsAllEffect(snakes));
                break;
            case DEATH_ALL:
                room.getGameField().killSnake(snakes.get(MathUtils.randInt(0, snakes.size() - 1)));
                break;
            case REVERSE_ENEMY:
                applyTempEffect(new ReverseEnemyEffect(snake, snakes));
                break;
        }
    }

    private int c = 0;

    public void timeStep() {
        for (Iterator<TemporaryEffect> iter = activeEffects.iterator(); iter.hasNext(); ) {
            TemporaryEffect effect = iter.next();
            if (effect.timeStep()) {
                effect.deactivate();
                iter.remove();
            }
        }
        for (Snake snake : snakes) {
            for (Iterator<Bonus> bonusIter = bonuses.iterator(); bonusIter.hasNext(); ) {
                Bonus bonus = bonusIter.next();
                if (bonus.isReachableBy(snake)) {
                    applyBonus(bonus, snake);
                    room.broadcastMessage(new EatBonusMessage(bonus.getId(), snake));
                    bonusIter.remove();
                }
            }
        }

        c++;
        if (c % MathUtils.randInt(bonusSpawnProbability, bonusSpawnProbability + DEFAULT_BONUS_JITTER) == 0) {
            spawnBonus();
        }
    }

    private void spawnBonus() {
        int x = MathUtils.randInt(BONUS_SPAWN_PADDING,
                Integer.valueOf(Main.mechanicsConfig.gameFieldWidth) - 2 * BONUS_SPAWN_PADDING);

        int y = MathUtils.randInt(BONUS_SPAWN_PADDING,
                Integer.valueOf(Main.mechanicsConfig.gameFieldHeight) - 2 * BONUS_SPAWN_PADDING);

        addBonus(new Bonus(x, y, getRandomBonusKind()));
    }

    private Bonus.Kind getRandomBonusKind() {
        // TODO: probabilities
//        int random = MathUtils.rand.nextInt(bonusProbSum);
//        for(int i = 0; i < bonusProbabilities.length; ++i) {
//            if (random >= bonusProbabilities[i]) {
//                return Bonus.Kind.values()[i];
//            }
//            i++;
//        }
        return Bonus.Kind.values()[MathUtils.rand.nextInt(Bonus.Kind.values().length)];
    }
}
