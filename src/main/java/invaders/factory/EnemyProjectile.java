package invaders.factory;

import invaders.engine.GameEngine;
import invaders.physics.Collider;
import invaders.physics.Vector2D;
import invaders.score.Scoring;
import invaders.strategy.ProjectileStrategy;
import javafx.scene.image.Image;

import java.io.File;

public class EnemyProjectile extends Projectile{
    private ProjectileStrategy strategy;
    private Scoring score;

    public EnemyProjectile(Vector2D position, ProjectileStrategy strategy, Image image) {
        super(position,image);
        this.strategy = strategy;
        if(strategy.getStrategy().equals("SlowProjectile")){
            this.score = Scoring.SLOW_PROJECTILE;
        }
        else if(strategy.getStrategy().equals("FastProjectile")){
            this.score = Scoring.FAST_PROJECTILE;
        }
        else{
            this.score = Scoring.OTHER;
        }
    }

    @Override
    public void update(GameEngine model) {
        strategy.update(this);

        if(this.getPosition().getY()>= model.getGameHeight() - this.getImage().getHeight()){
            this.takeDamage(1);
        }
    }
    @Override
    public String getRenderableObjectName() {
        return "EnemyProjectile";
    }

    public Scoring getScore(){
        return score;
    }

    public String getStrategy(){
        return strategy.getStrategy();
    }
    @Override
    public Object clone() {
        return super.clone();
    }

}
