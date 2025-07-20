package invaders.strategy;

import invaders.factory.Projectile;
import invaders.physics.Vector2D;
import invaders.score.Scoring;

public interface ProjectileStrategy {
   public void update(Projectile p);
   public String getStrategy();
   public int getScore(Scoring score);
}
