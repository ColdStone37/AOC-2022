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
				String line = sc.nextLine();
				if(line.length() > 14){
					numbers.add(new Operation(line));
				} else {
					numbers.add(new Number(line));
				}
			}

			for(Number n:numbers){
				n.init(numbers);
			}

			Operation root = (Operation)Operation.getNumberByName(numbers, "root");
			Number human = Operation.getNumberByName(numbers, "humn");

			human.value = 0;
			human.isStatic = false;
			root.isStatic();

			long change = 1024L * 1024L * 1024L;
			while(!root.isEqual()){
				while(getSlope(root, human) > 0){
					human.value-=change;
				}
				change/=2L;
				while(getSlope(root, human) < 0){
					human.value+=change;
				}
				change/=2L;
				if(change == 1){
					break;
				}
			}
			while(!root.isEqual()){
				human.value+=1;
			}
			System.out.println(human.value);
		} catch(FileNotFoundException e){
			System.out.println("Gibts nicht");
		}
	}

	public long getSlope(Operation root, Number variable){
		long current = Math.abs(root.getDiff());
		variable.value+=5;
		long next = Math.abs(root.getDiff());
		variable.value-=5;
		return next - current;
	}

	public class Number {
		String name;
		public long value;
		protected boolean hasValue = false;
		public boolean isStatic = true;

		Number() {
			
		}

		Number(String num) {
			name = num.split(" ")[0].replace(":","");
			value = Integer.parseInt(num.split(" ")[1]);
			hasValue = true;
		}

		public void init(ArrayList<Number> numbers){

		}

		public long eval(){
			return value;
		}

		public String toString(){
			return ""+value;
		}

		public boolean isStatic(){
			return isStatic;
		}
	}

	public class Operation extends Number {
		String operation;

		Number operand1;
		Number operand2;
		Operator operator;

		Operation(String operation){
			super();
			this.operation = operation;
			name = operation.split(":")[0];
		}

		public void init(ArrayList<Number> numbers){
			String[] split = operation.split(" ");

			operand1 = getNumberByName(numbers, split[1]);
			operator = Operator.getOperatorBySymbol(split[2].toCharArray()[0]);
			operand2 = getNumberByName(numbers, split[3]);
		}

		public static Number getNumberByName(ArrayList<Number> numbers, String name){
			for(Number n:numbers){
				if(n.name.equals(name))
					return n;
			}
			return null;
		}

		public long eval(){
			if(isStatic && hasValue){
				return value;
			}
			long v1 = operand1.eval();
			long v2 = operand2.eval();
			value = operator.doOperation(v1, v2);
			hasValue = true;
			return value;
		}

		public boolean isEqual(){
			return operand1.eval() == operand2.eval();
		}

		public long getDiff(){
			return operand1.eval() - operand2.eval();
		}

		public String toString(){
			return "("+operand1.toString()+operator.getSymbol()+operand2.toString()+")";
		}

		public boolean isStatic(){
			isStatic = operand1.isStatic() && operand2.isStatic();
			return isStatic;
		}

		public enum Operator {
			PLUS,
			MINUS,
			MULTIPLY,
			DIVIDE;

			public static Operator getOperatorBySymbol(char c){
				switch(c){
				case '+':
					return PLUS;
				case '-':
					return MINUS;
				case '*':
					return MULTIPLY;
				case '/':
					return DIVIDE;
				default:
					return null;
				}
			}

			public char getSymbol(){
				switch(this){
				case PLUS:
					return '+';
				case MINUS:
					return '-';
				case MULTIPLY:
					return '*';
				case DIVIDE:
					return '/';
				default:
					return 0;
				}
			}

			public static long doOperation(Operator op, long v1, long v2){
				return op.doOperation(v1, v2);
			}

			public long doOperation(long v1, long v2){
				switch(this){
				case PLUS:
					return v1 + v2;
				case MINUS:
					return v1 - v2;
				case MULTIPLY:
					return v1 * v2;
				case DIVIDE:
					return v1 / v2;
				default:
					return 0;
				}
			}
		}
	}
}