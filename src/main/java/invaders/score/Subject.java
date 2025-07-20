package invaders.score;

import invaders.rendering.Renderable;

public interface Subject {
    void notify(Renderable renderable);
}
