package invaders.score;

import invaders.rendering.Renderable;

public class Score implements Subject{
    private int score;

    public Score() {
        score = 0;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public void notify(Renderable renderable) {
        score += renderable.getScore().getScore();
    }
}
