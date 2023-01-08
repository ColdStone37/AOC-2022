import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class part2{
	final static int buffer_height = 1000;
	final static int buffer_max = 980;
	final static int buffer_move_down = 900;

	public static void main(String[] args){
		try{
			File file = new File(args[0]);
			Scanner sc = new Scanner(file);
			char[] input = sc.nextLine().replace("\n","").toCharArray();
			boolean[] goLeft = new boolean[input.length];
			for(int i=0; i<input.length; i++){
				if(input[i] == '<'){
					goLeft[i] = true;
				}
			}
			int[][] rocks = getRocks();

			int inputIndex = 0;
			int[] map = new int[buffer_height];
			int highest = 1;
			long moveDownTotal = 1;

			map[0] = 0B111111111;
			for(int i=1; i<buffer_height; i++){
				map[i] = 0B100000001;
			}

			long iterations = Long.parseLong(args[1]);
			long aThousanth = iterations / 1000L;

			long startNano = System.nanoTime();

			for(long i=0; i<iterations; i++){
				/*if(i%aThousanth == 0L && i != 0L){
					System.out.println(((float)(i/aThousanth)/10.0f)+"%");
					long dif = (System.nanoTime() - startNano) / 1000000000L;
					double percentage = (double)(i/aThousanth)/1000.0;
					double totalTime = ((double)dif)/percentage;
					double left = totalTime - dif;
					System.out.println((int)left+" Seconds left");
				}*/

				int[] currentRock = rocks[(int)(i % 5L)].clone();
				int rockY = highest+3;
				boolean falling = true;
				
				while(falling){
					//wind left
					if(goLeft[inputIndex]){
						shiftLeft(currentRock);
						if(doesOverlap(currentRock,map,rockY))
							shiftRight(currentRock);
					} else {
						shiftRight(currentRock);
						if(doesOverlap(currentRock,map,rockY))
							shiftLeft(currentRock);
					}
					rockY--;
					if(doesOverlap(currentRock,map,rockY)){
						rockY++;
						addRockToMap(currentRock,map,rockY);
						falling = false;
						for(highest=highest; highest<map.length; highest++){
							if(map[highest] == 0B100000001){
								break;
							}
						}
						if(highest > buffer_max){
							for(int j=0; j<buffer_height-buffer_move_down; j++){
								map[j] = map[j+buffer_move_down];
							}
							for(int j=buffer_height-buffer_move_down; j<buffer_height; j++){
								map[j] =  0B100000001;
							}
							highest-=buffer_move_down;
							moveDownTotal+=buffer_move_down;
						}
					}
					inputIndex=(inputIndex+1) % input.length;
				}
			}
			System.out.println(moveDownTotal+(long)highest-2L);
			System.out.println((float)(System.nanoTime() - startNano) / 1000000000f);

		} catch(FileNotFoundException e){
			System.out.println("Gibts nicht");
		}
	}

	public static int[] shiftLeft(int[] array){
		for(int i=0; i<array.length; i++){
			array[i] *= 2;
		}
		return array;
	}

	public static int[] shiftRight(int[] array){
		for(int i=0; i<array.length; i++){
			array[i] /= 2;
		}
		return array;
	}

	public static boolean doesOverlap(int[] current, int[] map, int off){
		for(int i=0; i<current.length; i++){
			if((current[i] & map[i+off]) > 0){
				return true;
			}
		}
		return false;
	}

	public static void addRockToMap(int[] current, int[] map, int off){
		for(int i=0; i<current.length; i++){
			map[i+off] = current[i] | map[i+off];
		}
	}

	public static void printString(int[] s){
		for(int j=s.length-1; j>=0; j--){
			int c=s[j];
			String bits = Integer.toBinaryString(c);
			for(int i=0; i<9; i++){
				int index = i-(9-bits.length());
				if(index<0 || bits.charAt(index) == '0'){
					System.out.print(".");
				}else{
					System.out.print("#");
				}
			}
			System.out.println("");
		}
	}

	public static void printStringWithRock(int[] s){
		for(int j=s.length-1; j>=0; j--){
			int c=s[j];
			String bits = Integer.toBinaryString(c);
			for(int i=0; i<9; i++){
				int index = i-(9-bits.length());
				if(index<0 || bits.charAt(index) == '0'){
					System.out.print(".");
				}else{
					System.out.print("#");
				}
			}
			System.out.println("");
		}
	}

	public static int[][] getRocks(){
		int[][] rocks = new int[5][4];
		rocks[0][0] = 0B000111100;
		rocks[1][0] = 0B000010000;
		rocks[1][1] = 0B000111000;
		rocks[1][2] = 0B000010000;
		rocks[2][2] = 0B000001000;
		rocks[2][1] = 0B000001000;
		rocks[2][0] = 0B000111000;
		rocks[3][3] = 0B000100000;
		rocks[3][2] = 0B000100000;
		rocks[3][1] = 0B000100000;
		rocks[3][0] = 0B000100000;
		rocks[4][0] = 0B000110000;
		rocks[4][1] = 0B000110000;
		return rocks;
	}
}