import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.math.BigInteger;

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

			long sum = 0L;
			while(sc.hasNextLine()){
				String line = sc.nextLine().replace("\n","");
				sum += SNAFU_to_decimal(line);
			}

			System.out.println(decimal_to_SNAFU(sum));

		} catch(FileNotFoundException e){
			System.out.println("Gibts nicht");
		}
	}

	public long SNAFU_to_decimal(String snafu){
		long place = 1L;
		long sum = 0L;
		for(int i=0; i<snafu.length(); i++){
			char c = snafu.charAt(snafu.length()-1-i);
			sum += ((long)place) * ((long)getValueFromChar(c));
			place *= 5L;
		}
		return sum;
	}

	public String decimal_to_SNAFU(long decimal){
		decimal = Math.abs(decimal);
		long place = 1L;
		String snafu = "";
		while(decimal > 1L){
			decimal += 2L;
			snafu = valueToChar(decimal%5L) + snafu;
			decimal /= 5L;
		}
		return snafu;
	}

	public char valueToChar(long val){
		switch((int)val){
		case 0:
			return '=';
		case 1:
			return '-';
		case 2:
			return '0';
		case 3:
			return '1';
		case 4:
			return '2';
		default:
			System.out.println("This shouldn't happen");
			return '0';
		}
	}

	public int getValueFromChar(char c){
		switch(c){
		case '0':
			return 0;
		case '1':
			return 1;
		case '2':
			return 2;
		case '-':
			return -1;
		case '=':
			return -2;
		default:
			System.out.println("This shouldn't happen");
			return 0;
		}
	}
}