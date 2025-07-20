package invaders.strategy;

import invaders.factory.Projectile;
import invaders.score.Scoring;

public class SlowProjectileStrategy implements ProjectileStrategy{
    @Override
    public void update(Projectile p) {
        double newYPos = p.getPosition().getY() + 1;
        p.getPosition().setY(newYPos);
    }

    @Override
    public String getStrategy() {
        return "SlowProjectile";
    }

    @Override
    public int getScore(Scoring score) {
        return Scoring.SLOW_PROJECTILE.getScore();
    }
}
