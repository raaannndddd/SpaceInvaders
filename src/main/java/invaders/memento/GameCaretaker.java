package invaders.memento;

import invaders.engine.GameEngine;
import invaders.engine.MyTimerTask;

import java.util.*;

public class GameCaretaker {
    GameMemento oldMemento;
    public GameMemento getMemento() {
        return oldMemento;
    }
    public void addMemento(GameMemento memento) {
        oldMemento = memento;
        System.out.println(oldMemento);
    }
}
