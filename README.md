# WELCOME TO THE SPACE INVADERS GAME 2.0!!!

In this updated version of the game, you can do more than ever before. Most importantly, you can **cheat**, **undo**, and **choose a difficulty level**.
Run using "gradle clean build run"

---

## Cheat Functionality

In cheat mode, you can remove all aliens of the same type or all projectiles of the same speed.

### Shortcuts:
- **A**: Kill all fast projectiles  
- **D**: Kill all slow projectiles  
- **Q**: Kill all slow aliens  
- **W**: Kill all fast aliens  

> Your score will reflect these changes

---

## Undo Feature

To undo an action:
- Press **S** to save the state.
- Press **Z** to revert back to that state.

All game conditions like time, score, and enemy movement will reset to the saved state.

### Design Pattern Used: Memento Pattern
- `GameMemento`: Memento  
- `GameCaretaker`: Caretaker  
- `GameEngine`: Originator  
- `KeyboardInputHandler`: Client  
> _The client informs the originator when to save or restore the memento._

---

## Difficulty Selection

You can switch between difficulty levels at any point in the game:

- **E**: Easy  
- **M**: Medium  
- **H**: Hard

If the game ends and you choose a new difficulty, it will restart with the new setting—but your **time** and **score will continue** accumulating.

### Design Pattern Used: Singleton
- `ConfigReader`: Singleton class used to read configuration values.

---

## Timer & Score Display

- **Timer**: Shown in `minutes : seconds`, pauses when the game ends, resumes after difficulty selection.
- **Score**: Based on the type of entity eliminated.

| Entity            | Points |
|-------------------|--------|
| Slow projectile   | 1      |
| Fast projectile   | 2      |
| Slow alien        | 3      |
| Fast alien        | 4      |

### Design Pattern Used: Observer Pattern
- `Observer`: Observer  
- `GameEngine`: Concrete Observer  
- `Score`: Concrete Subject  
- `Subject`: Subject

---

## Enjoy Playing!