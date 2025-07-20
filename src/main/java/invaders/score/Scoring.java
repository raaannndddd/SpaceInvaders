package invaders.score;

public enum Scoring {
    SLOW_PROJECTILE(1),
    FAST_PROJECTILE(2),
    SLOW_ALIEN(3),
    FAST_ALIEN(4),
    OTHER(0);

    private final int score;

    Scoring(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
