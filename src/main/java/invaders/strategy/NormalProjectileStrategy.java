package invaders.strategy;

import invaders.factory.Projectile;
import invaders.score.Scoring;

public class NormalProjectileStrategy implements ProjectileStrategy{
    @Override
    public void update(Projectile p) {
        double newYPos = p.getPosition().getY() - 2;
        p.getPosition().setY(newYPos);
    }

    @Override
    public String getStrategy() {
        return "normal";
    }

    @Override
    public int getScore(Scoring score) {
        return Scoring.OTHER.getScore();
    }
}
