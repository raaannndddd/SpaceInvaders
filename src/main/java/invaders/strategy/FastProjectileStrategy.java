package invaders.strategy;

import invaders.factory.Projectile;
import invaders.score.Scoring;

public class FastProjectileStrategy implements ProjectileStrategy{
    @Override
    public void update(Projectile p) {
        double newYPos = p.getPosition().getY() + 3;
        p.getPosition().setY(newYPos);
    }
    @Override
    public String getStrategy() {
        return "FastProjectile";
    }
    @Override
    public int getScore(Scoring score) {
        return Scoring.FAST_PROJECTILE.getScore();
    }
}
