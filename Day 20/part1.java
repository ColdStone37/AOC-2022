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
			ArrayList<Number> numbers = new ArrayList<Number>();

			while(sc.hasNextLine()){
				numbers.add(new Number(sc.nextLine()));
			}

			//scramble numbers
			for(int i=0; i<numbers.size(); i++){
				Number n = getNumberByIndex(numbers, i);
				int oldIndex = numbers.indexOf(n);
				int newIndex = oldIndex + n.number;
				while(newIndex < 0){
					newIndex += numbers.size()-1;
				}
				while(newIndex >= numbers.size()){
					newIndex -= numbers.size()-1;
				}
				newIndex %= numbers.size();
				numbers.remove(n);
				numbers.add(newIndex, n);
				/*for(int j=0; j<numbers.size(); j++){
					System.out.print(numbers.get(j).number+" ");
				}
				System.out.println("");*/
			}

			//find 0
			int indexZero = -1;
			for(int i=0; i<numbers.size(); i++){
				Number n = numbers.get(i);
				if(n.number == 0){
					indexZero = i;
					break;
				}
			}

			//add next 3 thousanth numbers together
			int added = 0;
			for(int i=0; i<3; i++){
				indexZero += 1000;
				while(indexZero >= numbers.size())
					indexZero -= numbers.size();
				added += numbers.get(indexZero).number;
			}
			System.out.println(added);
		} catch(FileNotFoundException e){
			System.out.println("Gibts nicht");
		}
	}

	public Number getNumberByIndex(ArrayList<Number> numbers, int index){
		for(Number n:numbers){
			if(n.index == index){
				return n;
			}
		}
		return null;
	}

	public class Number {
		public int number;
		public int index;

		private static int index_counter = 0;

		Number(String num) {
			number = Integer.parseInt(num);
			index = index_counter;
			index_counter++;
		}

		public String toString(){
			return "Number: "+number+" Index: "+index;
		}
	}
}