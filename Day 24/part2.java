import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class part2 {
	int[][] globalMap;

	public static void main(String[] args) {
		part2 main = new part2();
		main.mainNonStatic(args);
	}

	public void mainNonStatic(String[] args){
		try{
			File file = new File(args[0]);
			Scanner sc = new Scanner(file);

			ArrayList<String> input = new ArrayList<String>();

			while(sc.hasNextLine())
				input.add(sc.nextLine().replace("\n",""));

			int width = input.get(0).length();
			int height = input.size();

			int[][] map = new int[width][height];

			for(int x = 0; x < width; x++){
				for(int y = 0; y < height; y++){
					char c = input.get(y).charAt(x);
					map[x][y] = getValueFromChar(c);
				}
			}
			int steps = 0;
			steps += simulatePath(map, 1, 0, width-2, height-1);
			steps += simulatePath(globalMap, width-2, height-1, 1, 0);
			steps += simulatePath(globalMap, 1, 0, width-2, height-1);
			System.out.println(steps);
		} catch(FileNotFoundException e){
			System.out.println("Gibts nicht");
		}
	}

	public int simulatePath(int[][] map, int startX, int startY, int endX, int endY){
		int width = map.length;
		int height = map[0].length;

		boolean[][] reachMap = new boolean[width][height];
		reachMap[startX][startY] = true;
		int steps = 1;

		while(true){
			//simulate blizzard
			map = updateBlizzard(map);

			//propagate reachmap
			boolean[][] nextReachMap = new boolean[width][height];
			nextReachMap[startX][startY] = true;
			for(int x = 1; x < width-1; x++){
				for(int y = 1; y < height-1; y++){
					if(reachMap[x+1][y] || reachMap[x-1][y] || reachMap[x][y-1] || reachMap[x][y+1] || reachMap[x][y])
						nextReachMap[x][y] = true;
				}
			}
			reachMap = nextReachMap;

			//remove spaces with blizzard
			for(int x = 0; x < width; x++){
				for(int y = 0; y < height; y++){
					if(reachMap[x][y] && map[x][y] != 0){
						reachMap[x][y] = false;
					}
				}
			}

			steps++;

			//break if the goal will be reached in the next iteration
			if(endY == 0 && reachMap[endX][1])
				break;
			if(endY == height-1 && reachMap[endX][height-2])
				break;
		}
		globalMap = updateBlizzard(map);
		return steps;
	}

	public int[][] updateBlizzard(int[][] map){
		int width = map.length;
		int height = map[0].length;

		int[][] nextMap = new int[width][height];
		for(int x = 0; x < width; x++){
			nextMap[x][0] = map[x][0];
			nextMap[x][height-1] = map[x][height-1];
		}
		for(int y = 0; y < height; y++){
			nextMap[0][y] = map[0][y];
			nextMap[width-1][y] = map[width-1][y];
		}

		for(int x = 1; x < width-1; x++){
			for(int y = 1; y < height-1; y++){
				int spaceCount = 0;
				for(int i = 1; i <= 8; i *= 2){
					if((map[x][y] & i) == i){
						Direction d = Direction.getDirFromValue(i);
						int nextX = x + d.getXOff();
						int nextY = y + d.getYOff();
						if(nextX == 0)
							nextX = width-2;
						if(nextX == width-1)
							nextX = 1;
						if(nextY == 0)
							nextY = height-2;
						if(nextY == height-1)
							nextY = 1;
						nextMap[nextX][nextY] += i;
						spaceCount++;
					}
				}
			}
		}
		return nextMap;
	}

	public void printMap(int[][] map){
		for(int y = 0; y < map[0].length; y++){
			for(int x = 0; x < map.length; x++){
				if(map[x][y] == 16){
					System.out.print("#");
					continue;
				}
				if(map[x][y] == 0){
					System.out.print(" ");
					continue;
				}
				switch(map[x][y]){
				case 16:
					System.out.print("#");
					continue;
				case 0:
					System.out.print(' ');
					continue;
				case 8:
					System.out.print('>');
					continue;
				case 4:
					System.out.print('<');
					continue;
				case 1:
					System.out.print('^');
					continue;
				case 2:
					System.out.print('v');
					continue;
				default:
					int spaceCount = 0;
					for(int i = 1; i <= 8; i *= 2){
						if((map[x][y] & i) == i){
							spaceCount++;
						}
					}
					System.out.print(spaceCount);
				}
			}
			System.out.println();
		}
	}

	public void printBooleanMap(boolean[][] map){
		for(int y = 0; y < map[0].length; y++){
			for(int x = 0; x < map.length; x++){
				if(map[x][y] == true){
					System.out.print("x");
				}else{
					System.out.print("o");
				}
			}
			System.out.println();
		}
	}

	public int getValueFromChar(char c){
		switch(c){
		case '#':
			return 16;
		case '.':
			return 0;
		case '>':
			return Direction.EAST.getValue();
		case '<':
			return Direction.WEST.getValue();
		case '^':
			return Direction.NORTH.getValue();
		case 'v':
			return Direction.SOUTH.getValue();
		default:
			return 0;
		}
	}

	public enum Direction {
		NORTH,
		SOUTH,
		WEST,
		EAST,
		NO_MOVE;

		public static Direction getDirFromValue(int dir){
			switch(dir){
			case 1:
				return NORTH;
			case 2:
				return SOUTH;
			case 4:
				return WEST;
			case 8:
				return EAST;
			default:
				return NO_MOVE;
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

		public int getValue(){
			switch(this){
			case NORTH:
				return 1;
			case SOUTH:
				return 2;
			case WEST:
				return 4;
			case EAST:
				return 8;
			default:
				return 0;
			}
		}
	}
}