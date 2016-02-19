package nl.retaliation.logic;

import java.util.ArrayList;
import java.util.Collections;

import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
//import nl.retaliation.level.TerrainTile;

import java.lang.Math;

/**
 * This class calculates the shortest path between different coordinates
 * 
 * @author Luke Nooteboom
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
	public ArrayList<Vector2> calcPath(Vector2 pos, Vector2 desiredPos, TileMap terrain, boolean canStepOnLand, boolean canStepOnWater) {
		ArrayList<PathNode> openList = new ArrayList<PathNode>();
		ArrayList<PathNode> closedList = new ArrayList<PathNode>();
		PathNode desiredNode = new PathNode(pos, 0);

		openList.add(new PathNode(pos, 0));

		while (openList.size() > 0) {
			PathNode current = calcLowestFCost(openList);
			if (current.getPos().equal(desiredPos)) { //doel bereikt
				desiredNode = current;
				break;
			} else {
				openList = removeWithValues(current, openList);
				closedList.add(current);

				ArrayList<PathNode> neighbors = calcNeighbors(current, desiredPos, terrain.getMapWidth(), terrain.getMapHeight());
				for (PathNode currentNeighbor : neighbors) {
					if (presentInList(currentNeighbor, closedList) || place_free(currentNeighbor.getPos(), terrain, canStepOnLand, canStepOnWater)) { //als de neighbor in de closedlist staat of niet walkable is
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

		//start met reconstrueren path
		ArrayList<Vector2> path = new ArrayList<Vector2>();
		PathNode currentNode = desiredNode;
		while (currentNode.getPos().equal(pos) == false) {
			path.add(currentNode.getPos());
			currentNode = closedList.get(findInList(currentNode.getParent(), closedList));
		}
		Collections.reverse(path);
		return path;
	}
	private ArrayList<PathNode> calcNeighbors(PathNode currentNode, Vector2 desiredPos, int levelWidth, int levelHeight) {
		ArrayList<PathNode> neighBors = new ArrayList<PathNode>();
		for (int xOffset = -1; xOffset <= 1; xOffset++) {
			for (int yOffset = -1; yOffset <=1; yOffset++) {
				Vector2 position = new Vector2(currentNode.getPos().getX() + xOffset, currentNode.getPos().getY() + yOffset);
				if ((xOffset != 0 || yOffset != 0) && position.getX() >= 0 && position.getY() >= 0 && position.getX() < levelWidth && position.getY() < levelHeight) { //TODO: add more boundary checks
					//int xPos = currentNode.pos.x + xOffset;
					//int yPos = currentNode.pos.y + yOffset;

					double gCost = currentNode.getgCost() + calcDistance(currentNode.getPos(), position);
					double hCost = heuristic(position, desiredPos);
					PathNode currentNeighbor = new PathNode(position, gCost, gCost + hCost, currentNode.getPos());
					neighBors.add(currentNeighbor);
				}
			}
		}
		return neighBors;
	}
	private PathNode calcLowestFCost(ArrayList<PathNode> candidates) {
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
	private ArrayList<PathNode> removeWithValues(PathNode input, ArrayList<PathNode> list) {
		for (int i = 0; i < list.size(); i++) {
			PathNode currentEntry = list.get(i);
			if (input.getPos().equal(currentEntry.getPos())) {
				list.remove(i);
				break;
			}
		}
		return list;
	}
	private boolean presentInList(PathNode input, ArrayList<PathNode> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getPos().equal(input.getPos())) {
				return true;
			}
		}
		return false;
	}
	private int findInList(Vector2 input, ArrayList<PathNode> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getPos().equal(input)) {
				return i;
			}
		}
		return -1;
	}
	private double calcDistance(Vector2 pos1, Vector2 pos2) {
		return (Math.sqrt(Math.pow(pos1.getX() * 10 - pos2.getX() * 10, 2) + Math.pow(pos1.getY() * 10 - pos2.getY() * 10, 2)));
	}
	private double heuristic(Vector2 node, Vector2 desiredPos) {
		int dx = Math.abs(node.getX() - desiredPos.getX());
		int dy = Math.abs(node.getY() - desiredPos.getY());
		return (1.0 * (dx + dy) + (1.4 - 2.0) * Math.min(dx, dy));
		//return 0;
	}
	private boolean place_free(Vector2 position, TileMap tilemap, boolean canStepOnLand, boolean canStepOnWater) {
		//TODO: update this
		int currentTile = tilemap.getTileMap()[position.getX()][position.getY()];
		if ((currentTile == 0 && canStepOnWater == true) || (currentTile != 0 && canStepOnLand == true)) {
			return true;
		} else {
			return false;
		}
	}
}
