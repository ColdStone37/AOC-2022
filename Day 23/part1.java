import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class part1 {
	public static void main(String[] args) {
		part1 main = new part1();
		main.mainNonStatic(args);
	}

	public void mainNonStatic(String[] args){
		try{
			File file = new File(args[0]);
			Scanner sc = new Scanner(file);

			ArrayList<String> input = new ArrayList<String>();

			while(sc.hasNextLine())
				input.add(sc.nextLine().replace("\n",""));

			int buffer = 10;
			int width = input.get(0).length()+buffer*2;
			int height = input.size()+buffer*2;
			
			boolean[][] elfMap = new boolean[width][height];
			for(int y=0; y<input.size(); y++){
				char[] line = input.get(y).toCharArray();
				for(int x=0; x<input.get(0).length(); x++)
					if(line[x] == '#')
						elfMap[x+buffer][y+buffer] = true;
			}
			
			Direction[] dirOrder = new Direction[4];
			dirOrder[0] = Direction.NORTH;
			dirOrder[1] = Direction.SOUTH;
			dirOrder[2] = Direction.WEST;
			dirOrder[3] = Direction.EAST;

			Direction[][] consideredDirections = new Direction[width][height];
			int[][] moveCount;

			//simulate 
			for(int i=0; i<10; i++){
				//consider directions
				for(int x=0; x<width; x++){
					for(int y=0; y<height; y++){
						if(elfMap[x][y]){
							consideredDirections[x][y] = considerDirection(elfMap, x, y, dirOrder);
						}
					}
				}

				//count how many want to go to each space
				moveCount = new int[width][height];
				for(int x=0; x<width; x++){
					for(int y=0; y<height; y++){
						if(elfMap[x][y] && consideredDirections[x][y] != Direction.NO_MOVE){
							moveCount[x+consideredDirections[x][y].getXOff()][y+consideredDirections[x][y].getYOff()]++;
						}
					}
				}

				//remove duplicate moves
				for(int x=0; x<width; x++){
					for(int y=0; y<height; y++){
						if(elfMap[x][y] && consideredDirections[x][y] != Direction.NO_MOVE){
							if(moveCount[x+consideredDirections[x][y].getXOff()][y+consideredDirections[x][y].getYOff()] >= 2){
								consideredDirections[x][y] = Direction.NO_MOVE;
							}
						}
					}
				}

				//move
				boolean[][] newElfMap = new boolean[width][height];
				for(int x=0; x<width; x++){
					for(int y=0; y<height; y++){
						if(elfMap[x][y]){
							newElfMap[x+consideredDirections[x][y].getXOff()][y+consideredDirections[x][y].getYOff()] = true;
						}
					}
				}
				elfMap = newElfMap;
				for(int j=0; j<4; j++){
					dirOrder[j] = dirOrder[j].getNext();
				}
			}

			int minX = Integer.MAX_VALUE;
			int minY = Integer.MAX_VALUE;
			int maxX = Integer.MIN_VALUE;
			int maxY = Integer.MIN_VALUE;

			for(int x=0; x<width; x++){
				for(int y=0; y<height; y++){
					if(elfMap[x][y]){
						minX = Integer.min(x, minX);
						minY = Integer.min(y, minY);
						maxX = Integer.max(x, maxX);
						maxY = Integer.max(y, maxY);
					}
				}
			}

			int count = 0;
			for(int x=minX; x<=maxX; x++){
				for(int y=minY; y<=maxY; y++){
					if(!elfMap[x][y]){
						count++;
					}
				}
			}
			System.out.println(count);
		} catch(FileNotFoundException e){
			System.out.println("Gibts nicht");
		}
	}

	public Direction considerDirection(boolean[][] elfMap, int x, int y, Direction[] dirOrder){
		if(!(elfMap[x-1][y-1] || elfMap[x][y-1] || elfMap[x+1][y-1] || elfMap[x+1][y] || elfMap[x+1][y+1] || elfMap[x][y+1] || elfMap[x-1][y+1] || elfMap[x-1][y])){
			return Direction.NO_MOVE;
		}
		for(Direction d:dirOrder){
			if(!d.checkDir(elfMap, x, y))
				return d;
		}
		return Direction.NO_MOVE;
	}

	public enum Direction {
		NORTH,
		SOUTH,
		WEST,
		EAST,
		NO_MOVE;

		public boolean checkDir(boolean[][] map, int x, int y){
			switch(this){
			case NORTH:
				return (map[x-1][y-1] || map[x][y-1] || map[x+1][y-1]);
			case SOUTH:
				return (map[x-1][y+1] || map[x][y+1] || map[x+1][y+1]);
			case WEST:
				return (map[x-1][y-1] || map[x-1][y] || map[x-1][y+1]);
			case EAST:
				return (map[x+1][y-1] || map[x+1][y] || map[x+1][y+1]);
			default:
				return true;
			}
		}

		public int getXOff(){
			switch(this){
			case NORTH:
				return 0;
			case SOUTH:
				return 0;
			case WEST:
				return -1;
			case EAST:
				return 1;
			default:
				return 0;
			}
		}

		public int getYOff(){
			switch(this){
			case NORTH:
				return -1;
			case SOUTH:
				return 1;
			case WEST:
				return 0;
			case EAST:
				return 0;
			default:
				return 0;
			}
		}

		public Direction getNext(){
			switch(this){
			case NORTH:
				return SOUTH;
			case SOUTH:
				return WEST;
			case WEST:
				return EAST;
			case EAST:
				return NORTH;
			default:
				return NO_MOVE;
			}
		}
	}
}