package invaders.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import invaders.ConfigReader;
import invaders.builder.BunkerBuilder;
import invaders.builder.Director;
import invaders.builder.EnemyBuilder;
import invaders.factory.EnemyProjectile;
import invaders.factory.PlayerProjectile;
import invaders.factory.Projectile;
import invaders.gameobject.Bunker;
import invaders.gameobject.Enemy;
import invaders.gameobject.GameObject;
import invaders.entities.Player;
//import invaders.memento.GameMemento;
import invaders.memento.GameCaretaker;
import invaders.memento.GameMemento;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import invaders.score.Subject;
import invaders.score.Score;
import invaders.state.BunkerState;
import invaders.state.GreenState;
import invaders.state.RedState;
import invaders.state.YellowState;
import javafx.scene.image.Image;
import org.json.simple.JSONObject;

/**
 * This class manages the main loop and logic of the game
 */
public class GameEngine implements Observer {
	private List<GameObject> gameObjects = new ArrayList<>(); // A list of game objects that gets updated each frame
	private List<GameObject> pendingToAddGameObject = new ArrayList<>();
	private List<GameObject> pendingToRemoveGameObject = new ArrayList<>();
	private List<Renderable> pendingToAddRenderable = new ArrayList<>();
	private List<Renderable> pendingToRemoveRenderable = new ArrayList<>();
	private List<Integer> timerTasks = new ArrayList<>();
	private List<Renderable> renderables =  new ArrayList<>();
	private Score score = new Score();
	private boolean mementoPressed = false;

	private Player player;

	private boolean left;
	private boolean right;
	private int gameWidth;
	private int gameHeight;
	private int timer = 45;
	private MyTimerTask timerTask = MyTimerTask.getInstance();
	private boolean readConfig;

	public GameEngine(){
		// Read the config here
		ConfigReader configReader = ConfigReader.getInstance();
		ConfigReader.parse();

		// Get game width and height
		gameWidth = ((Long)((JSONObject) ConfigReader.getGameInfo().get("size")).get("x")).intValue();
		gameHeight = ((Long)((JSONObject) ConfigReader.getGameInfo().get("size")).get("y")).intValue();

		//Get player info
		this.player = new Player(ConfigReader.getPlayerInfo());
		renderables.add(player);

		Director director = new Director();
		BunkerBuilder bunkerBuilder = new BunkerBuilder();
		//Get Bunkers info
		for(Object eachBunkerInfo:ConfigReader.getBunkersInfo()){
			Bunker bunker = director.constructBunker(bunkerBuilder, (JSONObject) eachBunkerInfo);
			gameObjects.add(bunker);
			renderables.add(bunker);
		}


		EnemyBuilder enemyBuilder = new EnemyBuilder();
		//Get Enemy info
		for(Object eachEnemyInfo:ConfigReader.getEnemiesInfo()){
			Enemy enemy = director.constructEnemy(this,enemyBuilder,(JSONObject)eachEnemyInfo);
			gameObjects.add(enemy);
			renderables.add(enemy);
		}

		readConfig = false;
	}

	public boolean isMementoPressed() {
		return mementoPressed;
	}

	public void removeAllRenderables(){
		for(Renderable renderable: renderables){
			renderable.takeDamage(1);
			renderable.takeDamage(1);
			renderable.takeDamage(1);
		}
	}
	public void configReader(){
		System.out.println(renderables.size());
		removeAllRenderables();
		ConfigReader.parse();

		//Get player info
		this.player = new Player(ConfigReader.getPlayerInfo());
		renderables.add(player);

		Director director = new Director();
		BunkerBuilder bunkerBuilder = new BunkerBuilder();
		//Get Bunkers info
		for(Object eachBunkerInfo:ConfigReader.getBunkersInfo()){
			Bunker bunker = director.constructBunker(bunkerBuilder, (JSONObject) eachBunkerInfo);
			gameObjects.add(bunker);
			renderables.add(bunker);
		}

		EnemyBuilder enemyBuilder = new EnemyBuilder();
		//Get Enemy info
		for(Object eachEnemyInfo:ConfigReader.getEnemiesInfo()){
			Enemy enemy = director.constructEnemy(this,enemyBuilder,(JSONObject)eachEnemyInfo);
			gameObjects.add(enemy);
			renderables.add(enemy);
		}

		readConfig = false;
		System.out.println(renderables.size());
	}


	/**
	 * Updates the game/simulation
	 */


	public void update(){
		timerTasks.clear();
		timerTasks.add(timerTask.getMinutes());
		timerTasks.add(timerTask.getSeconds());
		timerTasks.add(timerTask.getMilliseconds());

		timer+=1;
		movePlayer();

		if(readConfig){
			configReader();
		}

		for(GameObject go: gameObjects){
			go.update(this);
		}

		for (int i = 0; i < renderables.size(); i++) {
			Renderable renderableA = renderables.get(i);
			for (int j = i+1; j < renderables.size(); j++) {
				Renderable renderableB = renderables.get(j);
				if((renderableA.getRenderableObjectName().equals("Enemy") && renderableB.getRenderableObjectName().equals("EnemyProjectile"))
						||(renderableA.getRenderableObjectName().equals("EnemyProjectile") && renderableB.getRenderableObjectName().equals("Enemy"))||
						(renderableA.getRenderableObjectName().equals("EnemyProjectile") && renderableB.getRenderableObjectName().equals("EnemyProjectile"))){
				}else{
					if(renderableA.isColliding(renderableB) && (renderableA.getHealth()>0 && renderableB.getHealth()>0)) {
						renderableA.takeDamage(1);
						renderableB.takeDamage(1);
						score.notify(renderableA);
						updateScore(score);
					}
				}
			}
			if(!renderableA.isAlive()){pendingToRemoveRenderable.add(renderableA);}
		}

		// ensure that renderable foreground objects don't go off-screen
		int offset = 1;
		for(Renderable ro: renderables){
			if(!ro.getLayer().equals(Renderable.Layer.FOREGROUND)){
				continue;
			}
			if(ro.getPosition().getX() + ro.getWidth() >= gameWidth) {
				ro.getPosition().setX((gameWidth - offset) -ro.getWidth());
			}

			if(ro.getPosition().getX() <= 0) {
				ro.getPosition().setX(offset);
			}

			if(ro.getPosition().getY() + ro.getHeight() >= gameHeight) {
				ro.getPosition().setY((gameHeight - offset) -ro.getHeight());
			}

			if(ro.getPosition().getY() <= 0) {
				ro.getPosition().setY(offset);
			}
		}
		for(Renderable r : renderables){
			System.out.println(r.isAlive() + " " + r.getRenderableObjectName());
		}
	}

	public List<Renderable> getRenderables(){
		return renderables;
	}

	public List<GameObject> getGameObjects() {
		return gameObjects;
	}
	public List<GameObject> getPendingToAddGameObject() {
		return pendingToAddGameObject;
	}

	public List<GameObject> getPendingToRemoveGameObject() {
		return pendingToRemoveGameObject;
	}

	public List<Renderable> getPendingToAddRenderable() {
		return pendingToAddRenderable;
	}

	public List<Renderable> getPendingToRemoveRenderable() {
		return pendingToRemoveRenderable;
	}


	public void leftReleased() {
		this.left = false;
	}

	public void rightReleased(){
		this.right = false;
	}

	public void leftPressed() {
		this.left = true;
	}
	public void rightPressed(){
		this.right = true;
	}

	public boolean shootPressed(){
		if(timer>45 && player.isAlive()){
			Projectile projectile = player.shoot();
			gameObjects.add(projectile);
			renderables.add(projectile);
			timer=0;
			return true;
		}
		return false;
	}

	private void movePlayer(){
		if(left){
			player.left();
		}

		if(right){
			player.right();
		}
	}

	public int getGameWidth() {
		return gameWidth;
	}

	public int getGameHeight() {
		return gameHeight;
	}

	public Player getPlayer() {
		return player;
	}

	public int getScore() {
		return score.getScore();
	}

	public void cheat(String type) {
		for(Renderable renderable : renderables){
			if(renderable.getStrategy().equals(type)){
				renderable.takeDamage(1);
				score.notify(renderable);
				updateScore(score);
				pendingToRemoveGameObject.add((GameObject) renderable);
				pendingToRemoveRenderable.add(renderable);
			}
		}
	}

	public GameMemento createMemento() {
		System.out.println(renderables.size());
		List<Integer> xVel = new ArrayList<>();
		List<Double> health = new ArrayList<>();
		List<Double> xPosition = new ArrayList<>();
		List<Double> yPosition = new ArrayList<>();
		List<BunkerState> state = new ArrayList<>();
		for(Renderable renderable : renderables){
			health.add(renderable.getHealth());
			xPosition.add(renderable.getPosition().getX());
			yPosition.add(renderable.getPosition().getY());
			if (renderable instanceof Bunker) {
				Bunker bunker = (Bunker) renderable;
				state.add(bunker.getState());}
			else if (renderable instanceof Enemy){
				Enemy enemy = (Enemy) renderable;
				xVel.add(enemy.getxVel());
			}
		}

		return new GameMemento(
				new ArrayList<>(renderables),
				new ArrayList<>(gameObjects),
				new ArrayList<>(timerTasks),
				score.getScore(),
				new ArrayList<>(health),
				new ArrayList<>(xPosition),
				new ArrayList<>(yPosition),
				new ArrayList<>(state),
				new ArrayList<>(xVel)
		);
	}

	public void setMemento(GameMemento memento) {
		removeAllRenderables();

			int countBunker = 0;
			int countEnemy = 0;
			mementoPressed = true;

			this.timerTasks = new ArrayList<>(memento.getTimers());
			this.renderables = new ArrayList<>(memento.getRenderables());
			this.gameObjects = new ArrayList<>(memento.getGameObjects());
			timerTask.setMinutes(timerTasks.get(0));
			timerTask.setSeconds(timerTasks.get(1));
			timerTask.setMilliseconds(timerTasks.get(2));
			score.setScore(memento.getScore());
			for(int i = 0; i < renderables.size(); i++) {
				renderables.get(i).setHealth(memento.getHealth().get(i));
				renderables.get(i).getPosition().setX(memento.getXPosition().get(i));
				renderables.get(i).getPosition().setY(memento.getYPosition().get(i));
				if (renderables.get(i) instanceof Bunker) {
					((Bunker) renderables.get(i)).setState(memento.getState().get(countBunker));
					if (memento.getState().get(countBunker) instanceof GreenState) {
						((Bunker) renderables.get(i)).setImage(new Image(new File("src/main/resources/bunkerGreen.png").toURI().toString()));
					} else if (memento.getState().get(countBunker) instanceof YellowState) {
						((Bunker) renderables.get(i)).setImage(new Image(new File("src/main/resources/bunkerYellow.png").toURI().toString()));
					}
					if (memento.getState().get(countBunker) instanceof RedState) {
						((Bunker) renderables.get(i)).setImage(new Image(new File("src/main/resources/bunkerRed.png").toURI().toString()));
					}
					countBunker++;
			}
				if(renderables.get(i) instanceof Enemy){
					((Enemy) renderables.get(i)).setxVel(memento.getXVel().get(countEnemy));
					countEnemy++;
				}
		}
	}

	public void updateScore(Score score){
		this.score = score;
	}

	public void setReadConfig(boolean readConfig) {
		this.readConfig = readConfig;
	}
}