package invaders.memento;

import invaders.gameobject.GameObject;
import invaders.rendering.Renderable;
import invaders.state.BunkerState;

import java.util.ArrayList;
import java.util.List;

public class GameMemento {
    private List<Renderable> renderables;
    private List<GameObject> gameObjects;
    private List<Double> health;
    private List<Integer> timer; // minute, second, millisecond
    private int score;
    private List<Double> xPositions;
    private List<Double> yPositions;
    private List<BunkerState> state;
    private List<Integer> xVel;

    public GameMemento(
            List<Renderable> renderables,
            List<GameObject> gameObjects,
            List<Integer> timer,
            int score,
            List<Double> health,
            List<Double> xPositions,
            List<Double> yPositions,
            List<BunkerState> state,
            List<Integer> xVel
    ) {
        this.renderables = new ArrayList<>(renderables);
        this.gameObjects = new ArrayList<>(gameObjects);
        this.timer = timer;
        this.score = score;
        this.health = new ArrayList<>(health);
        this.xPositions = new ArrayList<>(xPositions);
        this.yPositions = new ArrayList<>(yPositions);
        this.state = state;
        this.xVel = new ArrayList<>(xVel);
    }

    public List<Renderable> getRenderables() {
        return renderables;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public List<Integer> getTimers() { return timer; }

    public int getScore() {return score;}
    public List<Double> getHealth() {
        return health;
    }

    public List<Double> getXPosition() {return xPositions;}
    public List<Double> getYPosition() {return yPositions;}

    public List<Integer> getXVel() {
        return xVel;
    }

    public List<BunkerState> getState() {
        return state;
    }
}
