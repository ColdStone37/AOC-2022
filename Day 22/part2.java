import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class part2 {
	public static void main(String[] args) {
		part2 main = new part2();
		main.mainNonStatic(args);
	}

	public void mainNonStatic(String[] args){
		try{
			File file = new File(args[0]);
			Scanner sc = new Scanner(file);
			ArrayList<Number> numbers = new ArrayList<Number>();

			ArrayList<String> input = new ArrayList<String>();
			
			int width = 0;

			while(sc.hasNextLine()){
				String line = sc.nextLine().replace("\n","");
				if(line.length() == 0)
					break;
				input.add(line);
				width = Integer.max(line.length(), width);
			}

			char[] instructions = sc.nextLine().toCharArray();

			int height = input.size();

			short[][] map = new short[width+2][height+2];
			for(int y=0; y<height; y++){
				String row = input.get(y);
				for(int x=0; x<width; x++){
					if(x >= row.length()){
						map[x+1][y+1] = 0;
					} else {
						switch(row.charAt(x)){
						case '.':
							map[x+1][y+1] = 1;
							break;
						case '#':
							map[x+1][y+1] = 2;
							break;
						default:
							System.out.println("Weird Data in text File");
						case ' ':
							map[x+1][y+1] = 0;
							break;
						}
					}
				}
			}

			printMap(map);
			
			boolean lastWasDigit = isDigit(instructions[0]);
			String buffer = "";

			ArrayList<Instruction> parsedInstructions = new ArrayList<Instruction>();
			for(char c:instructions){
				boolean isDigit = isDigit(c);
				if(isDigit != lastWasDigit){
					if(lastWasDigit){
						parsedInstructions.add(new MoveForward(Integer.parseInt(buffer)));
					} else{
						parsedInstructions.add(new Rotate(buffer.charAt(0)));
					}
					buffer = "";
					lastWasDigit = isDigit;
				}
				buffer += c;
			}
			printInstructions(parsedInstructions);

			int startX;
			for(startX = 1; startX < map.length; startX++){
				if(map[startX][1] != 0)
					break;
			}

			Me me = new Me(startX, 1, 0);

			for(Instruction i:parsedInstructions){
				i.doInstruction(me, map);
			}

			System.out.println(me.getPassword());

		} catch(FileNotFoundException e){
			System.out.println("Gibts nicht");
		}
	}

	public void printMap(short[][] map){
		for(int y=0; y<map[0].length; y++){
			for(int x=0; x<map.length; x++){
				System.out.print(map[x][y]);
			}
			System.out.println();
		}
	}

	public void printMapWithMe(short[][] map, Me me){
		for(int y=0; y<map[0].length; y++){
			for(int x=0; x<map.length; x++){
				if(x==me.x && y==me.y){
					System.out.print(""+me.getDirectionChar());
				} else {
					System.out.print(map[x][y]);
				}
			}
			System.out.println();
		}
	}

	public void printInstructions(ArrayList<Instruction> instructions){
		for(Instruction i:instructions){
			System.out.print(i);
		}
		System.out.println();
	}

	public boolean isDigit(char c){
		return c>=48 && c<=57;
	}

	public abstract class Instruction{
		Instruction(){

		}

		public String toString(){
			return "this shouldn't have happened";
		}

		public void doInstruction(Me me, short[][] map){

		}
	}

	public class Rotate extends Instruction{
		private boolean rotateLeft = true;

		Rotate(char dir){
			rotateLeft = (dir == 'L');
		}

		public String toString(){
			if(rotateLeft){
				return "L";
			} else {
				return "R";
			}
		}

		public void doInstruction(Me me, short[][] map){
			if(rotateLeft){
				me.rotateLeft();
			} else {
				me.rotateRight();
			}
		}
	}

	public class MoveForward extends Instruction{
		private int steps;

		MoveForward(int steps){
			this.steps = steps;
		}

		public String toString(){
			return ""+steps;
		}

		public void doInstruction(Me me, short[][] map){
			for(int i=0; i<steps; i++){
				int lastX = me.x;
				int lastY = me.y;

				me.doStep();

				if(map[me.x][me.y] == 0){
					printMapWithMe(map, me);
					me.rotateLeft();
					int turns = 0;
					boolean firstSide = true;
					int stepsFirst = 0;
					while(turns != 1){
						
						if(me.getFront(map) != 0){
							me.rotateRight();
							turns++;
							firstSide = false;
						}else{
							me.doStep();
							if(me.getLeft(map) == 0){
								me.rotateLeft();
								turns--;
								firstSide = false;
							}
							if(firstSide){
								stepsFirst++;
							}
						}
						turns = (turns+6)%6;
					}

					for(int j=0; j<stepsFirst; j++){
						me.doStep();
					}
					me.rotateLeft();
					me.doStep();

					printMapWithMe(map, me);
				}

				if(map[me.x][me.y] == 2){
					me.x = lastX;
					me.y = lastY;
					return;
				}
			}
		}
	}

	public class Me{
		public int x, y;
		private short dir;

		Me(int X, int Y, int Dir){
			x = X;
			y = Y;
			dir = (short)Dir;
		}

		public void rotateLeft(){
			dir = (short)((dir + 3) % 4);
		}

		public void rotateRight(){
			dir = (short)((dir + 1) % 4);
		}

		public short getLeft(short[][] map){
			switch(dir){
			case 0:
				return map[x][y-1];
			case 1:
				return map[x+1][y];
			case 2:
				return map[x][y+1];
			case 3:
				return map[x-1][y];
			default:
				return 0;
			}
		}

		public short getFront(short[][] map){
			switch(dir){
			case 0:
				return map[x+1][y];
			case 1:
				return map[x][y+1];
			case 2:
				return map[x-1][y];
			case 3:
				return map[x][y-1];
			default:
				return 0;
			}
		}

		public int getPassword(){
			return 1000 * y + 4 * x + (int)dir;
		}

		public void doStep(){
			switch(dir){
			case 0:
				x++;
				return;
			case 1:
				y++;
				return;
			case 2:
				x--;
				return;
			case 3:
				y--;
				return;
			default:
				return;	
			}
		}

		public void goBackwards(){
			switch(dir){
			case 0:
				x--;
				return;
			case 1:
				y--;
				return;
			case 2:
				x++;
				return;
			case 3:
				y++;
				return;
			default:
				return;	
			}
		}

		public char getDirectionChar(){
			switch(dir){
			case 0:
				return '>';
			case 1:
				return 'v';
			case 2:
				return '<';
			case 3:
				return '^';
			default:
				return 'F';
			}
		}
	}
}