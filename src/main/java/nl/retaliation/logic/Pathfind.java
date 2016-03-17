package nl.retaliation.logic;

import java.util.ArrayList;
import java.util.Collections;

import nl.han.ica.OOPDProcessingEngineHAN.Objects.GameObject;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.Tile;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import nl.retaliation.IRTSObject;
import nl.retaliation.level.WaterTile;
import nl.retaliation.unit.AirUnit;
import nl.retaliation.unit.GroundUnit;
import nl.retaliation.unit.Unit;

import java.lang.Math;

/**
 * This class calculates the shortest path between 2 different coordinates while avoiding obstacles
 * 
 * @author Luke Nooteboom
 * @author Jonathan Vos
 *
 */
public class Pathfind {
	
	/**
	 * Calculates the shortest path
	 * 
	 * @param pos the position where the path starts
	 * @param desiredPos the position where the path ends
	 * @param currentLevel map of obstacles
	 * @return List of path nodes
	 */
	public static ArrayList<Vector2> calcPath(Vector2 pos, Vector2 desiredPos, TileMap terrain, Unit currentUnit, ArrayList<IRTSObject> gameobjects, float targetRadius) {
		if(!place_free(desiredPos, gameobjects, currentUnit, terrain)){
			return new ArrayList<Vector2>(0);
		}
		
		ArrayList<PathNode> openList = new ArrayList<PathNode>();
		ArrayList<PathNode> closedList = new ArrayList<PathNode>();
		PathNode desiredNode = new PathNode(pos, 0);

		openList.add(new PathNode(pos, 0));

		while (openList.size() > 0) {
			PathNode current = calcLowestFCost(openList);
			if (current.getPos().withinRadius(desiredPos, targetRadius)) { //doel bereikt
				desiredNode = current;
				break;
			} else {
				openList = removeWithValues(current, openList);
				closedList.add(current);
				ArrayList<PathNode> neighbors = calcNeighbors(current, desiredPos, terrain.getMapWidth() / terrain.getTileSize(), terrain.getMapHeight() / terrain.getTileSize());
				for (PathNode currentNeighbor : neighbors) {
					if (presentInList(currentNeighbor, closedList) || !place_free(currentNeighbor.getPos(), gameobjects, currentUnit, terrain)) { //als de neighbor in de closedlist staat of niet walkable is
						continue;
					}
					int duplicateIndex = findInList(currentNeighbor.getPos(), openList);
					if (presentInList(currentNeighbor, openList) == false || openList.get(duplicateIndex).getgCost() > currentNeighbor.getgCost()) { //nieuw pad naar neighbor korter is of neighbor niet in openlist staat
						//todo?
						if (presentInList(currentNeighbor, openList) == false) {
							openList.add(currentNeighbor);
						} else {
							openList.set(duplicateIndex, currentNeighbor);
						}
					}
				}
			}
		}
		return reconstruct_path(pos, desiredNode, closedList);
	}
	private static ArrayList<Vector2> reconstruct_path(Vector2 currentPos, PathNode desiredNode, ArrayList<PathNode> closedList) {
		ArrayList<Vector2> path = new ArrayList<Vector2>();
		PathNode currentNode = desiredNode;
		while (currentNode.getPos().equal(currentPos) == false) {
			path.add(currentNode.getPos());
			currentNode = closedList.get(findInList(currentNode.getParent(), closedList));
		}
		Collections.reverse(path);
		return path;
	}
	private static ArrayList<PathNode> calcNeighbors(PathNode currentNode, Vector2 desiredPos, int levelWidth, int levelHeight) {
		ArrayList<PathNode> neighBors = new ArrayList<PathNode>();
		for (int xOffset = -1; xOffset <= 1; xOffset++) {
			for (int yOffset = -1; yOffset <=1; yOffset++) {
				Vector2 position = new Vector2(currentNode.getPos().getX() + xOffset, currentNode.getPos().getY() + yOffset);
				if ((xOffset != 0 || yOffset != 0) && position.getX() >= 0 && position.getY() >= 0 && position.getX() < levelWidth && position.getY() < levelHeight) {

					double gCost = currentNode.getgCost() + calcDistance(currentNode.getPos(), position);
					double hCost = heuristic(position, desiredPos);
					PathNode currentNeighbor = new PathNode(position, gCost, gCost + hCost, currentNode.getPos());
					neighBors.add(currentNeighbor);
				}
			}
		}
		return neighBors;
	}
	private static PathNode calcLowestFCost(ArrayList<PathNode> candidates) {
		double lowestFCost = 0;
		double lowestGCost = 0;
		PathNode finalPos = new PathNode(new Vector2(0,0), 0);
		for (PathNode currentCandidate: candidates) {
			if (currentCandidate.getfCost() < lowestFCost || lowestFCost == 0 || (currentCandidate.getfCost() == lowestFCost && currentCandidate.getgCost() < lowestGCost)) {
				lowestFCost = currentCandidate.getfCost();
				lowestGCost = currentCandidate.getgCost();
				finalPos = currentCandidate;
			}
		}
		return finalPos;
	}
	private static ArrayList<PathNode> removeWithValues(PathNode input, ArrayList<PathNode> list) {
		for (int i = 0; i < list.size(); i++) {
			PathNode currentEntry = list.get(i);
			if (input.getPos().equal(currentEntry.getPos())) {
				list.remove(i);
				break;
			}
		}
		return list;
	}
	private static boolean presentInList(PathNode input, ArrayList<PathNode> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getPos().equal(input.getPos())) {
				return true;
			}
		}
		return false;
	}
	private static int findInList(Vector2 input, ArrayList<PathNode> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getPos().equal(input)) {
				return i;
			}
		}
		return -1;
	}
	private static float calcDistance(Vector2 pos1, Vector2 pos2) {
		//return (Math.sqrt(Math.pow(pos1.getX() - pos2.getX(), 2) + Math.pow(pos1.getY() - pos2.getY(), 2)));
		if (Math.abs(pos1.getX() - pos2.getX()) == Math.abs(pos1.getY() - pos2.getY())) {
			return 1.4f;
		} else {
			return 1.0f;
		}
	}
	private static double heuristic(Vector2 node, Vector2 desiredPos) {
		int dx = Math.abs(node.getX() - desiredPos.getX());
		int dy = Math.abs(node.getY() - desiredPos.getY());
		return (1.0 * (dx + dy) + (1.4 - 2.0) * Math.min(dx, dy));
		//return 0; //for dijkstra's algorithm (slower)
	}
	public static boolean place_free(Vector2 position, ArrayList<IRTSObject> allObjects, Unit currentUnit, TileMap terrain) {
		//Tile currentTile = tilemap.getTileOnIndex(position.getX(), position.getY());

		if(currentUnit instanceof AirUnit){
			for(IRTSObject object : allObjects){
				if(object.getPos().equal(position) && object instanceof AirUnit){
					return false;
				}
			}
		}
		if(currentUnit instanceof GroundUnit){
			for(IRTSObject object : allObjects){
				if(object.getPos().equal(position) && object instanceof GroundUnit){
					return false;
				}
				if(terrain.getTileMap()[position.getY()][position.getX()] == 0 && !((GroundUnit)currentUnit).canStepOnLand()){ // GrassTile
					return false;
				}
				else if(terrain.getTileMap()[position.getY()][position.getX()] == 1 && !((GroundUnit)currentUnit).canStepOnWater()){ //WaterTile
					return false;
				}
			}
		}
		
		return true;
		
		/*if ((currentTile instanceof WaterTile && currentUnit.canStepOnWater()) || (!(currentTile instanceof WaterTile) && currentUnit.canStepOnLand()) //controleert of de unit over dit type terrain kan (boten kunnen niet over land etc)
			&& !posInObject(position, gameobjects, currentUnit)){
			return true;
		} else {
			return false;
		}*/
	}
	/*private static boolean posInObject(Vector2 pos, GameObject[] gameobjects, GameObject currentUnit) {
		for (GameObject currentGameObject : gameobjects) {
			int goX = (int) (currentGameObject.getX() / currentUnit.getWidth());
			int goY = (int) (currentGameObject.getY() / currentUnit.getHeight());
			int width = (int) (currentGameObject.getWidth() / currentUnit.getWidth());
			int height = (int) (currentGameObject.getHeight() / currentUnit.getHeight());
			if (pos.getX() >= goX && pos.getX() < goX + width && pos.getY() >= goY && pos.getY() < goY + height && currentGameObject.equals(currentUnit) == false) {
				return true;
			}
		}
		return false;
	}*/
}
