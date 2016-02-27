package nl.han.ica.OOPDProcessingEngineHAN.Engine;

import ddf.minim.Minim;
import nl.han.ica.OOPDProcessingEngineHAN.Dashboard.Dashboard;
import nl.han.ica.OOPDProcessingEngineHAN.Dashboard.FPSCounter;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.GameObject;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.Tile;
import nl.han.ica.OOPDProcessingEngineHAN.UserInput.IKeyInput;
import nl.han.ica.OOPDProcessingEngineHAN.UserInput.IMouseInput;
import nl.han.ica.OOPDProcessingEngineHAN.View.View;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.event.MouseEvent;

import java.awt.*;
import java.util.Vector;

/**
 * GameEngine is the core of the game. Extending this class is required to make use of the GameEngine.
 * 
 * This engine is created by: Bram Heijmink, Jeffrey Haen, Joost Elshof, Kenny Ligthart, Mark Vaesen & Nico Smolders.
 */
public abstract class GameEngine extends PApplet {

    /**
     * A vectorlist that holds all GameObjects.
     */
    private Vector<GameObject> gameObjects = new Vector<>();
    
    /**
     * A vectorlist that holds all Dashboards, which are of type GameObject.
     */
    private Vector<Dashboard> dashboards = new Vector<>();

    /**
     * Instance of TileMap.
     */
    protected TileMap tileMap;

    /**
     * Thread is used to keep the update method running.
     */
    private GameThread gameThread = new GameThread(this);
 
    /**
     * Creates an instance of minim that helps with loading music in the Sound class.
     */
    public Minim soundLibrary = new Minim(this);
    
    /**
     * The View is the main canvas that is been drawn by the GameEngine.
     */
    private View view;

    /**
     * The FPSCounter shows the Frames Per Second on the screen.
     */
    private FPSCounter fpsCounter;

    private static GameEngine engine;

    /**
     * Creates a new GameEngine object, use a static main method and implement the following: 
     * PApplet.main(new String[]{"{YOUR.PACKAGENAME}.{YOUR.CLASSNAME}"});
     */
    public GameEngine() {
    	
        GameEngine.engine = this;
    }

    /**
     * The actual setup of the game-engine.
     * Used to make sure the gameThread is started after setup has finished.
     * This method is invoked by processing.
     */
    public void setup() {
        setupGame();
        gameThread.start();
    }

    /**
     * Implement this method to setup your game.
     */
    public abstract void setupGame();

    /**
     * Sets the View which will be drawn by the GameEngine.
     * @param view
     */
    public void setView(View view) {
    	this.view = view;
    }
    
    /**
     * Gets the View which is drawn by the GameEngine.
     * @return
     */
    public View getView() {
    	return view;
    }
    
    /**
     * Add a GameObject to the GameEngine.
     * @param gameObject
     *            The GameObject that will be added to the game. Should have either
     *            GameObject or MovableGameObject as it's parent.
     */
    public void addGameObject(GameObject gameObject) {
    	gameObjects.add(gameObject);    	
    }
    
    /**
     * Add a Dashboard to the GameEngine.
     * @param dashboard
     */
    public void addDashboard(Dashboard dashboard) {
    	dashboards.add(dashboard);
    }

    /**
     * Add a GameObject to the GameEngine.
     * @param gameObject
     * @param x
     * @param y
     */
    public void addGameObject(GameObject gameObject, float x, float y) {
    	addGameObject(gameObject);
    	
        gameObject.setX(x);
        gameObject.setY(y);
    }
    
    /**
     * Add a Dashboard to te GameEngine.
     * @param dashboard
     * @param x
     * @param y
     */
    public void addDashboard(Dashboard dashboard, float x, float y) {
    	addDashboard(dashboard);
    	
    	dashboard.setX(x);
    	dashboard.setY(y);
    }

    /**
     * Add a GameObject to the GameEngine.
     * @param gameObject
     * @param x
     * @param y
     * @param layerposition
     */
    public void addGameObject(GameObject gameObject, float x, float y, float layerposition) {
    	addGameObject(gameObject);
    	
        gameObject.setX(x);
        gameObject.setY(y);
        gameObject.setZ(layerposition);
    }
    
    /**
     * Add a Dashboard to the GameEngine.
     * @param dashboard
     * @param x
     * @param y
     * @param layerposition
     */
    public void addDashboard(Dashboard dashboard, float x, float y, float layerposition) {
    	addDashboard(dashboard);
    	
    	dashboard.setX(x);
    	dashboard.setY(y);
    	dashboard.setZ(layerposition);
    }

    /**
     * Add a GameObject to the GameEngine.
     * @param gameObject
     * @param layerposition
     */
    public void addGameObject(GameObject gameObject, float layerposition) {
    	addGameObject(gameObject);
    	
        gameObject.setZ(layerposition);
    }
    
    /**
     * Add a Dashboard to the GameEngine.
     * @param dashboard
     * @param layerposition
     */
    public void addDashboard(Dashboard dashboard, float layerposition) {
    	addDashboard(dashboard);
    	
        dashboard.setZ(layerposition);
    }

    /**
     * Get a list of all the GameObjects inside the GameEngine.
     *  @return Vector<GameObject>
     */
    public Vector<GameObject> getGameObjectItems() {
        return gameObjects;
    }
    
    /**
     * Get a list of all the Dashboards inside the GameEngine.
     *  @return Vector<Dashboard>
     */
    public Vector<Dashboard> getDashboards() {
        return dashboards;
    }

    /**
     * Delete a GameObject from the GameEngine.
     * @param gameObject
     */
    public void deleteGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
    }
    
    /**
     * Delete a Dashboard from the GameEngine.
     * @param dashboard
     */
    public void deleteDashboard(Dashboard dashboard) {
        dashboards.remove(dashboard);
    }

    /**
     * Delete all GameObjects from the GameEngine.
     */
    public void deleteAllGameOBjects() {
        gameObjects.removeAllElements();
    }
    
    /**
     * Delete all Dashboards from the GameEngine.
     */
    public void deleteAllDashboards() {
        dashboards.removeAllElements();
    }

    /**
     * Delete all GameObjects of a given type from the GameEngine.
     *
     * Example paramater: Player.class
     *
     * @param type
     * @param <T>
     */
    public <T extends GameObject> void deleteAllGameObjectsOfType(Class<T> type) {
        gameObjects.removeIf(p -> type.equals(p.getClass()));
    }

    /**
     * Updates every GameObject inside the GameEngine.
     * Calls move method and checks for collision.
     * 
     * This method is called by the GameThread.
     */
    public void updateGame() {
    	
        updateGameObjects();
        updateDashboards();
        
//        Vector<GameObject> tempCollision = (Vector<GameObject>) gameObjects.clone();
//        for(int i = 0; i < tempCollision.size(); i++) {
//
//            if (tempCollision.get(i) instanceof ICollidableWithGameObjects) {
//                Vector collidedGameobjects = CollidingHelper.calculateGameObjectCollisions(tempCollision.get(i), tempCollision);
//                if (!collidedGameobjects.isEmpty())
//                    ((ICollidableWithGameObjects) tempCollision.get(i)).gameObjectCollisionOccurred(collidedGameobjects);
//            }
//            if (tempCollision.get(i) instanceof ICollidableWithTiles) {
//                Vector collidedTiles = CollidingHelper.calculateTileCollision(tempCollision.get(i), tileMap);
//                if (!collidedTiles.isEmpty())
//                    ((ICollidableWithTiles) tempCollision.get(i)).tileCollisionOccurred(collidedTiles);
//
//            }
//        }
        
        //gameObjects.sort((obj1, obj2) -> Float.compare(obj1.getZ(), obj2.getZ())); // Sort the list by Z index...
    }

    /**
     * Updates every Dashboard inside the GameEngine.
     */
    private void updateDashboards() {
    	
        for(int i = 0; i < dashboards.size(); i++) {
            dashboards.get(i).update();
        }
    }

    /**
     * Updates every GameObject inside the GameEngine.
     */
    private void updateGameObjects() {
    	
        for(int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).setySpeed(gameObjects.get(i).getySpeed() + gameObjects.get(i).getGravity());
            gameObjects.get(i).move();
            gameObjects.get(i).update();
        }
    }

    /**
     * Implement this method to make constant updates in your game
     */
    public abstract void update();
        
    /**
     * Draws the view.
     * 
     * (non-Javadoc) This method is used by Processing to draw on the canvas.
     * @see processing.core.PApplet#draw()
     */
    public void draw() {
    	view.draw(g, tileMap, gameObjects, dashboards);
    }
    
	/**
	 * Fires a keyPressed event to every GameObject inside the GameEngine.
	 * 
	 * (non-Javadoc) This event is fired by Processing when registers key input.
	 * @see processing.core.PApplet#keyPressed()
	 */
    public void keyPressed() {
    	for (int i = 0; i < gameObjects.size(); i++) {

            if(gameObjects.get(i) instanceof IKeyInput) {
                ((IKeyInput)gameObjects.get(i)).keyPressed(keyCode, key);
            }
        }
    }

	/**
	 * Fires a keyReleased event to every GameObject inside the GameEngine.
	 * 
	 * (non-Javadoc) This event is fired by Processing when registers key release.
 	 * @see processing.core.PApplet#keyReleased()
	 */
    public void keyReleased() {
        for (int i = 0; i < gameObjects.size(); i++) {

            if(gameObjects.get(i) instanceof IKeyInput) {
                ((IKeyInput)gameObjects.get(i)).keyReleased(keyCode, key);
            }
        }
    }
    
    /**
     * Fires a mousePressed event to every GameObject inside the GameEngine.
     * 
     * (non-Javadoc)
     * @see processing.core.PApplet#mousePressed()
     */
    public void mousePressed() {
    	
    	PVector location = calculateRelativeMouseLocation(mouseX, mouseY);

    	for (int i = 0; i < gameObjects.size(); i++) {

            if(gameObjects.get(i) instanceof IMouseInput)
            {
                ((IMouseInput)gameObjects.get(i)).mousePressed((int)location.x, (int)location.y, mouseButton);
            }
        }
    }
    
    /**
     * Fires a mouseReleased event to every GameObject inside the GameEngine.
     *     
     * (non-Javadoc)
     * @see processing.core.PApplet#mouseReleased()
     */
    public void mouseReleased() {
    	
    	PVector location = calculateRelativeMouseLocation(mouseX, mouseY);

    	for (int i = 0; i < gameObjects.size(); i++) {

            if(gameObjects.get(i) instanceof IMouseInput)
            {
                ((IMouseInput)gameObjects.get(i)).mouseReleased((int)location.x, (int)location.y, mouseButton);
            }
        }
    }
    
    /**
     * Fires a mouseClicked event to every GameObject inside the GameEngine.
     * 
     * (non-Javadoc)
     * @see processing.core.PApplet#mouseClicked()
     */
    public void mouseClicked() {
    	
    	PVector location = calculateRelativeMouseLocation(mouseX, mouseY);
    	
    	for (int i = 0; i < gameObjects.size(); i++) {

            if(gameObjects.get(i) instanceof IMouseInput)
            {
                ((IMouseInput)gameObjects.get(i)).mouseClicked((int)location.x, (int)location.y, mouseButton); 
            }
        }
    }
    
    /**
     * Fires a mouseMoved event to every GameObject inside the GameEngine.
     * 
     * (non-Javadoc)
     * @see processing.core.PApplet#mouseMoved()
     */
    public void mouseMoved() {
    	
    	PVector location = calculateRelativeMouseLocation(mouseX, mouseY);
    	
    	for (int i = 0; i < gameObjects.size(); i++) {

            if(gameObjects.get(i) instanceof IMouseInput)
            {
                ((IMouseInput)gameObjects.get(i)).mouseMoved((int)location.x, (int)location.y);
            }
        }
    }
    
    /**
     * Fires a mouseDragged event to every GameObject inside the GameEngine.
     * 
     * (non-Javadoc)
     * @see processing.core.PApplet#mouseDragged()
     */
    public void mouseDragged() {
    	
    	PVector location = calculateRelativeMouseLocation(mouseX, mouseY);
    	
    	for (int i = 0; i < gameObjects.size(); i++) {

            if(gameObjects.get(i) instanceof IMouseInput)
            {	
                ((IMouseInput)gameObjects.get(i)).mouseDragged((int)location.x, (int)location.y, mouseButton);
            }
        }
    }
    
    /**
     * Fires a mouseWheel event to every GameObject inside the GameEngine.
     * 
     * (non-Javadoc)
     * @see processing.core.PApplet#mouseWheel(processing.event.MouseEvent)
     */
    public void mouseWheel(MouseEvent event) {
    	
    	for (int i = 0; i < gameObjects.size(); i++) {

            if(gameObjects.get(i) instanceof IMouseInput)
            {
                ((IMouseInput)gameObjects.get(i)).mouseWheel((int) mouseEvent.getAmount());
            }
        }
    }

    /**
     * Sets the amount of updates per second for the GameThread.
     * @param updatesPerSecond
     * @throws Exception
     */
    public void setGameSpeed(int updatesPerSecond) throws IllegalArgumentException {
    	
        if(updatesPerSecond < 1) 
            throw new IllegalArgumentException("Updates per second must be equal or higher than 1");
        else 
            gameThread.setGameSpeed(updatesPerSecond);
    }

    /**
     * Gets and calculates the relative mouse position with the location of world (TileMap).
     * A negative value means the mouse position is detected outside the world map.
     * @param x
     * @param y
     * @return PVector
     */
    public PVector calculateRelativeMouseLocation(int x, int y) {
    	
    	x += view.getViewport().getX();
    	y += view.getViewport().getY();
    	
    	return new PVector(x, y);
    }
    
    /**
     * Gets the amount of updates per second from the GameThread.
     * @return int
     */
    public int getGameSpeed() {
    	return (int)gameThread.getGameSpeed();
    }

    /**
     * Pauses the Game.
     */
    public void pauseGame() {
        gameThread.pauseGame();
    }

    /**
     * Resumes the Game.
     */
    public void resumeGame() {
        gameThread.resumeGame();
    }

    /**
     * Returns if the game thread is paused or not
     *
     * @return isGamePaused or !isGamePaused
     */
    public boolean getThreadState() {
        return gameThread.getThreadState();
    }

    /**
     * Sets the TileMap which will be drawn by the View.
     * @param tileMap
     */
    public void setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
    }

    /**
     * Gets the TileMap which is drawn by the View.
     * @return TileMap
     */
    public TileMap getTileMap() {
        return tileMap;
    }

    /**
     * Creates a new PGraphics (canvas) object which can only made by the PApplet. (this method is used by the GameEngine)
     * @param width
     * @param height
     * @return PGraphics
     */
    public static PGraphics createPGraphics(int width, int height) {
        return engine.createGraphics(width, height);
    }

    /**
     * Sets whether the FPSCounter has to be shown or not.
     * @param status
     */
    public void setFPSCounter(boolean status) {
        if (status && fpsCounter == null) {
            fpsCounter = new FPSCounter(0, 12);
            addDashboard(fpsCounter);
        } else if (!status){
            dashboards.remove(fpsCounter);
            fpsCounter = null;
        }
    }
}
