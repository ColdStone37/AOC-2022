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

			while(sc.hasNextLine()){
				numbers.add(new Number(sc.nextLine()));
			}

			for(int j=0; j<numbers.size(); j++){
				System.out.print(numbers.get(j).number+" ");
			}
			System.out.println("");

			//scramble numbers
			for(int scrambleCount = 0; scrambleCount < 10; scrambleCount++){
				for(int i=0; i<numbers.size(); i++){
					Number n = getNumberByIndex(numbers, i);
					long oldIndex = (long)numbers.indexOf(n);
					long newIndex = oldIndex + n.number;

					if(newIndex < 0L){
						newIndex += (numbers.size()-1) * (10+(Math.abs(newIndex) / (numbers.size()-1)));
					}

					newIndex %= (long)(numbers.size()-1);

					newIndex %= numbers.size();
					numbers.remove(n);
					numbers.add((int)newIndex, n);
				}
				for(int j=0; j<numbers.size(); j++){
					System.out.print(numbers.get(j).number+" ");
				}
				System.out.println("");
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

			System.out.println(indexZero);

			//add next 3 thousanth numbers together
			long added = 0;
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
		public long number;
		public int index;

		private static int index_counter = 0;

		Number(String num) {
			number = (long)Integer.parseInt(num) * 811589153L;
			index = index_counter;
			index_counter++;
		}

		public String toString(){
			return "Number: "+number+" Index: "+index;
		}
	}
}