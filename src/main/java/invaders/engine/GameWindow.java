package invaders.engine;

import java.util.List;
import java.util.ArrayList;

import invaders.ConfigReader;
import invaders.entities.EntityViewImpl;
import invaders.entities.SpaceBackground;
import invaders.factory.Projectile;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import invaders.entities.EntityView;
import invaders.rendering.Renderable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import org.json.simple.JSONObject;

public class GameWindow {
	private final int width;
    private final int height;
	private Scene scene;
    private Pane pane;
    private GameEngine model;
    private List<EntityView> entityViews =  new ArrayList<EntityView>();
    private Renderable background;
    private MyTimerTask task;
    private GraphicsContext gc;
    private double xViewportOffset = 0.0;
    private double yViewportOffset = 0.0;
    int counter = 0;
    // private static final double VIEWPORT_MARGIN = 280.0;

	public GameWindow(GameEngine model){
        this.model = model;
		this.width =  model.getGameWidth();
        this.height = model.getGameHeight();

        pane = new Pane();
        scene = new Scene(pane, width, height);
        this.background = new SpaceBackground(model, pane);

        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler(this.model);

        Canvas canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
        pane.getChildren().add(canvas);

        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);
        scene.setOnKeyReleased(keyboardInputHandler::handleReleased);

        task = MyTimerTask.getInstance();
    }

	public void run() {
         Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17), t -> this.draw()));

         timeline.setCycleCount(Timeline.INDEFINITE);
         timeline.play();
    }


    private void draw(){
        gc.clearRect(0, 0, width, height);

        gc.fillText("Score: " + model.getScore(), 5, 25);
        gc.fillText("Time: " + task.getTime(), 5, 50);

        gc.setFill(Color.WHITE);
        Font font = new Font("Arial", 16);
        gc.setFont(font);

        model.update();

        boolean found = false;
        for(Renderable renderable : model.getRenderables()){
            if(renderable.getRenderableObjectName().equals("Enemy")){
                found = true;
                break;
            }
        }

//        System.out.println("----------");
//        System.out.println(model.isMementoPressed());
//        System.out.println(model.getPlayer().isAlive());
//        System.out.println("----------");
        if(!model.getPlayer().isAlive()){
            gc.fillText("GAME OVER", (width/2)-75, height/2);
        } else if(!found){
            gc.fillText("YOU WON", (width/2)-75, height/2);
        }
        else{
            task.run();
                List<Renderable> renderables = model.getRenderables();

            if(model.isMementoPressed()){
                for(EntityView entityView : entityViews){
                    entityView.markForDelete();
                }}

                for (Renderable entity : renderables) {
                    boolean notFound = true;
                    for (EntityView view : entityViews) {
                        if (view.matchesEntity(entity)) {
                            notFound = false;
                            view.update(xViewportOffset, yViewportOffset);
                            break;
                        }
                    }
                    if (notFound) {
                        EntityView entityView = new EntityViewImpl(entity);
                        entityViews.add(entityView);
                        pane.getChildren().add(entityView.getNode());
                    }
                }

                for (Renderable entity : renderables) {
                    if (!entity.isAlive()) {
                        for (EntityView entityView : entityViews) {
                            if (entityView.matchesEntity(entity)) {
                                entityView.markForDelete();
                            }
                        }
                    }
                }

//                    for(EntityView current : entityViews){
//                        boolean render = false;
//                        for(Node old : pane.getChildren()){
//                            if(current.getNode() == old){render = true;}
//                        }
//                        if(!render){
//                            pane.getChildren().remove(current.getNode());
//                        }
//                    }

                for (EntityView entityView : entityViews) {
                    if (entityView.isMarkedForDelete()) {
                        pane.getChildren().remove(entityView.getNode());
                    }
                }

        }

        model.getGameObjects().removeAll(model.getPendingToRemoveGameObject());
        model.getGameObjects().addAll(model.getPendingToAddGameObject());
        model.getRenderables().removeAll(model.getPendingToRemoveRenderable());
        model.getRenderables().addAll(model.getPendingToAddRenderable());

        model.getPendingToAddGameObject().clear();
        model.getPendingToRemoveGameObject().clear();
        model.getPendingToAddRenderable().clear();
        model.getPendingToRemoveRenderable().clear();

        entityViews.removeIf(EntityView::isMarkedForDelete);
    }

	public Scene getScene() {
        return scene;
    }
}