package invaders.engine;

import invaders.score.Score;

public interface Observer {
    public void updateScore(Score score);
}
