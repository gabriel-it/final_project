package MainGame;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/*
Make new bot that weights its guesses around previous hits
*/

public class BotEasy {

	//stores points already guessed
	private ArrayList<Point> guess;
	private Grid grid;

	public BotEasy(){
		guess = new ArrayList<>();
	}

	public Point getGuess(){
		Point guessPoint;
		int x, y;
		Random random = new Random();

		do {
			x = random.nextInt(10);
			y = random.nextInt(10);
		}while(!newGuess(x,y));

		guessPoint = new Point(x,y);
		this.guess.add(guessPoint);

		return guessPoint;
	}

	public boolean newGuess(int x, int y){
		for (int i = 0; i < guess.size(); i++) {
			if(guess.get(i).getX() == x && guess.get(i).getY() == y){
				return false;
			}
		}
		return true;
	}

	public void placeShips(){
		//place ships randomly
		//assuming no overlap
	}

	//generates a new set of ships randomly onto a backend-grid
	public void generateShips(Grid gameGrid){
		Stack<Ship> shipStack = new Stack();

		ShipBattleship batShip = new ShipBattleship();
		ShipCarrier carShip = new ShipCarrier();
		ShipCruiser cruShip = new ShipCruiser();
		ShipDestroyer desShip = new ShipDestroyer();
		ShipSubmarine subShip = new ShipSubmarine();

		shipStack.push(batShip);
		shipStack.push(carShip);
		shipStack.push(desShip);
		shipStack.push(cruShip);
		shipStack.push(subShip);

		Random rand1 = new Random();
		Random rand2 = new Random();
		Random rand3 = new Random();

		Ship currentShip;

		while(!shipStack.empty()){
            currentShip = shipStack.pop();
			boolean placingShip = true;
			while(placingShip){
				int shipLength = currentShip.getLength();

				int shipX = rand1.nextInt(10);
				int shipY = rand2.nextInt(10);
				int rotation = rand3.nextInt(2); //0 = horizontal, 1 = vertical

				if (canPlaceShip(gameGrid, shipLength, shipX, shipY, rotation, currentShip)){
					placingShip = false;
				}

			}
		}
	}

	//checks if a ship is allowed to be there
	public boolean canPlaceShip(Grid gameGrid, int shipLength, int x, int y, int rotation, Ship currentShip){
		boolean canPlace = true;
		for (int i = 0; i < shipLength; i++){

			int xFromCenter = i + (x - shipLength / 2);
			int yFromCenter = i + (y - shipLength / 2);

			if (rotation == 0) {
				if (xFromCenter < 0 || xFromCenter > 9){
					canPlace = false;
				}
				else {
					GridContents gridData = gameGrid.getGridContents(xFromCenter, y);
					if (gridData.getContainsShip()) {
						canPlace = false;
					}
				}
			}

			if (rotation == 1) {
				if (yFromCenter < 0 || yFromCenter > 9){
					canPlace = false;
				}
				else {
					GridContents gridData = gameGrid.getGridContents(x, i + (y - shipLength / 2));
					if (gridData.getContainsShip()) {
						canPlace = false;
					}
				}
			}
		}
		//only change tiles to contain a ship, if none of them had a former ship
		if (canPlace == true){
			for (int i = 0; i < shipLength; i++){
				int xFromCenter = i + (x - shipLength / 2);
				int yFromCenter = i + (y - shipLength / 2);

				if (rotation == 0) {
					GridContents gridData = gameGrid.getGridContents(xFromCenter ,y);
					gridData.setContainsShip(true);
					gridData.setShip(currentShip);
				}
				if (rotation == 1) {
					GridContents gridData = gameGrid.getGridContents(x, yFromCenter);
					gridData.setContainsShip(true);
					gridData.setShip(currentShip);
				}
			}
		}

		return canPlace;
	}
}
