import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class part1{
	int bestRelease = 0;

	public static void main(String[] args){
		part1 main = new part1();
		main.mainNonStatic(args);
	}

	public void mainNonStatic(String[] args){
		try{
			File file = new File(args[0]);
			Scanner sc = new Scanner(file);
			ArrayList<String> input = new ArrayList<String>();
			while(sc.hasNextLine()){
				input.add(sc.nextLine());
			}

			ArrayList<Valve> valves = new ArrayList<Valve>();
			for(String s:input){
				valves.add(new Valve(Integer.parseInt(s.split(" ")[4].split("=")[1].replace(";","")),s.split(" ")[1]));
			}

			for(int i=0; i<input.size(); i++){
				String[] splitLine = input.get(i).split(" ");
				for(int j=9; j<splitLine.length; j++){
					valves.get(i).addConnectedValve(getValveByName(splitLine[j].replace(",","").replace("\n",""), valves));
				}
			}

			for(Valve v:valves){
				v.generateTimeToOtherValves(valves);
				v.printValve();
			}
			ArrayList<Valve> stack = new ArrayList<Valve>();
			solve(valves, 30, valves.get(0), 0, stack, true);
			System.out.println(bestRelease);
		} catch(FileNotFoundException e){
			System.out.println("Gibts nicht");
		}
	}

	public void solve(ArrayList<Valve> valves, int minutesLeft, Valve current, int releasedPressure, ArrayList<Valve> stack, boolean first){
		if(minutesLeft<0){
			return;
		}

		int newPressureAfterOpening = releasedPressure + releasedPressure(valves);
		current.isOpened = true;

		boolean valvesLeft = false;
		stack.add(current);
		for(int i=0; i<valves.size(); i++){
			Valve v = valves.get(i);
			if(v != current){
				if(!v.isOpened && v.flowRate > 0){
					valvesLeft = true;
					int newPressure = newPressureAfterOpening + releasedPressure(valves) * current.timeToOtherValve[i];
					//System.out.println(newPressure+" "+releasedPressure(valves));
					solve(valves, minutesLeft - current.timeToOtherValve[i] - 1, v, newPressure, stack, false);
				}
			}
		}
		int newPressure = newPressureAfterOpening + releasedPressure(valves) * (minutesLeft);
		if(newPressure > bestRelease){
			bestRelease = newPressure;
			System.out.println(bestRelease);
			for(Valve v:stack){
				System.out.print(v.name+" ");
			}
			System.out.println();
		}
		stack.remove(stack.size()-1);
		current.isOpened = false;
	}

	public int releasedPressure(ArrayList<Valve> valves){
		int pressure = 0;
		for(Valve v:valves){
			if(v.isOpened)
				pressure+=v.flowRate;
		}
		return pressure;
	}

	public Valve getValveByName(String name, ArrayList<Valve> valves){
		for(Valve v:valves){
			if(v.name.equals(name))
				return v;
		}
		return null;
	}

	public class Valve{
		public int flowRate;
		public String name;
		public ArrayList<Valve> connectedValves;
		public boolean isOpened = false;
		public int[] timeToOtherValve;

		public Valve(int fr, String n){
			flowRate = fr;
			name = n;
			connectedValves = new ArrayList<Valve>();
		}

		public void addConnectedValve(Valve v){
			connectedValves.add(v);
		}

		public void generateTimeToOtherValves(ArrayList<Valve> valves){
			int myIndex = valves.indexOf(this);
			timeToOtherValve = new int[valves.size()];
			timeToOtherValve[myIndex] = -1;
			/*for(int i=0; i<valves.size(); i++){
				if(i==myIndex){
					timeToOtherValve[i] = -1;
					continue;
				}
				Valve v = valves.get(i);
				if(v.timeToOtherValve != null){
					timeToOtherValve[i] == v.timeToOtherValve[myIndex];
				}
			}*/
			ArrayList<Valve> openValves = new ArrayList<Valve>();
			ArrayList<Valve> closedValves = new ArrayList<Valve>();
			openValves.add(this);
			int step = 1;
			while(openValves.size() > 0){
				ArrayList<Valve> newOpenValves = new ArrayList<Valve>();
				for(Valve open:openValves){
					for(Valve v:open.connectedValves){
						if(!openValves.contains(v) && !closedValves.contains(v) && !newOpenValves.contains(v)){
							timeToOtherValve[valves.indexOf(v)] = step;
							newOpenValves.add(v);
						}
					}
					closedValves.add(open);
				}
				openValves = newOpenValves;
				step++;
			}
		}

		public void printValve(){
			System.out.println("Valve: "+name);
			System.out.println(" flow rate: "+flowRate);
			System.out.print(" connectedValves: ");
			for(Valve v:connectedValves){
				System.out.print(v.name+" ");
			}
			System.out.println();
			if(timeToOtherValve != null){
				for(int i=0; i<timeToOtherValve.length; i++){
					System.out.print(timeToOtherValve[i]+" ");
				}
			}
			System.out.println();
		}
	}
}