package invaders.engine;

import invaders.ConfigReader;
import invaders.memento.GameCaretaker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class KeyboardInputHandler {
    private final GameEngine model;
    private boolean left = false;
    private boolean right = false;
    private Set<KeyCode> pressedKeys = new HashSet<>();

    private Map<String, MediaPlayer> sounds = new HashMap<>();
    private ConfigReader configReader = ConfigReader.getInstance();
    private boolean sPressed = false;
    private GameCaretaker caretaker = new GameCaretaker();

    KeyboardInputHandler(GameEngine model) {
        this.model = model;

        // TODO (longGoneUser): Is there a better place for this code?
        URL mediaUrl = getClass().getResource("/shoot.wav");
        String jumpURL = mediaUrl.toExternalForm();

        Media sound = new Media(jumpURL);
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        sounds.put("shoot", mediaPlayer);
    }

    void handlePressed(KeyEvent keyEvent) {
        if (pressedKeys.contains(keyEvent.getCode())) {
            return;
        }
        pressedKeys.add(keyEvent.getCode());

        if (keyEvent.getCode().equals(KeyCode.SPACE)) {
            if (model.shootPressed()) {
                MediaPlayer shoot = sounds.get("shoot");
                shoot.stop();
                shoot.play();
            }
        }

        if (keyEvent.getCode().equals(KeyCode.LEFT)) {
            left = true;
        }
        if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
            right = true;
        }

        if (left) {
            model.leftPressed();
        }

        if(right){
            model.rightPressed();
        }

        if(keyEvent.getCode().equals(KeyCode.D)){
            model.cheat("SlowProjectile");
        }
        else if(keyEvent.getCode().equals(KeyCode.A)){
            model.cheat("FastProjectile");
        } else if (keyEvent.getCode().equals(KeyCode.Q)) {
            model.cheat("FastAlien");
        } else if (keyEvent.getCode().equals(KeyCode.W)) {
            model.cheat("SlowAlien");
        }

        if(keyEvent.getCode().equals(KeyCode.S)){
            sPressed = true;
            caretaker.addMemento(model.createMemento());
        }
        if(keyEvent.getCode().equals(KeyCode.Z) && sPressed) {
            model.setMemento(caretaker.getMemento());
            sPressed = false;
        }

        if(keyEvent.getCode().equals(KeyCode.E)){
            configReader.setConfigPath("src/main/resources/config_easy.json");
            model.setReadConfig(true);
        }
        if(keyEvent.getCode().equals(KeyCode.M)){
            configReader.setConfigPath("src/main/resources/config_medium.json");
            model.setReadConfig(true);
        }
        if(keyEvent.getCode().equals(KeyCode.H)){
            configReader.setConfigPath("src/main/resources/config_hard.json");
            model.setReadConfig(true);
        }
    }

    void handleReleased(KeyEvent keyEvent) {
        pressedKeys.remove(keyEvent.getCode());

        if (keyEvent.getCode().equals(KeyCode.LEFT)) {
            left = false;
            model.leftReleased();
        }
        if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
            model.rightReleased();
            right = false;
        }
    }
}
