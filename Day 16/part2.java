import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class part2{
	int bestRelease = 0;

	public static void main(String[] args){
		part2 main = new part2();
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
			
			int valveCount = 0;
			for(Valve v:valves){
				if(v.flowRate == 0){
					v.isOpened = true;
				}else{
					valveCount++;
				}
			}
			ArrayList<Object> stack = new ArrayList<Object>();
			solve(valves, 26, valves.get(0), valves.get(0), 0, 0, 0, valveCount, stack);
			System.out.println(bestRelease);
		} catch(FileNotFoundException e){
			System.out.println("Gibts nicht");
		}
	}

	public void solve(ArrayList<Valve> valves, int minutesLeft, Valve currentMe, Valve currentElephant, int meRemainingSteps, int elephantRemainingSteps,  int releasedPressure, int remainingValves, ArrayList<Object> stack){
		//System.out.println(currentMe.name+ " "+ currentElephant.name);

		stack.add(currentMe);
		stack.add(currentElephant);
		stack.add(Integer.valueOf(releasedPressure(valves)));

		if(remainingValves <= 0){
			int oldMinutesLeft = minutesLeft;
			while(meRemainingSteps > 0 || elephantRemainingSteps > 0){
				meRemainingSteps -= 1;
				elephantRemainingSteps -= 1;
				minutesLeft --;
				releasedPressure += releasedPressure(valves);
			}
			boolean meBefore = currentMe.isOpened;
			boolean elephantBefore = currentElephant.isOpened;

			currentMe.isOpened = true;
			currentElephant.isOpened = true;
			releasedPressure += releasedPressure(valves) * minutesLeft;
			/*System.out.println(releasedPressure);
			for(int i=0; i<stack.size(); i++){
				System.out.print(stack.get(i).name+" ");
			}*/
			//System.out.println("");
			if(releasedPressure > bestRelease){
				System.out.println("Finished " + releasedPressure+" "+oldMinutesLeft);
				for(int i=0; i<stack.size()/3; i++){
					System.out.print(";"+((Valve)stack.get(i*3)).name+" "+((Valve)stack.get(i*3)).name+" "+stack.get(i*3+2));
				}
				System.out.println("");
				bestRelease = releasedPressure;
			}
			currentMe.isOpened = meBefore;
			currentElephant.isOpened = elephantBefore;
			stack.remove(stack.size()-1);
			stack.remove(stack.size()-1);
			stack.remove(stack.size()-1);
			return;
		}

		releasedPressure += releasedPressure(valves);


		if(meRemainingSteps <= 0){
			if(elephantRemainingSteps <= 0){
				//both reached valve
				currentMe.isOpened = true;
				currentElephant.isOpened = true;
				if(remainingValves >= 2){
					for(int v1 = 0; v1 < valves.size(); v1++){
						if(valves.get(v1).isOpened)
							continue;
						for(int v2 = 0; v2 < valves.size(); v2++){
							if(v1 != v2 && !valves.get(v2).isOpened){
								valves.get(v1).willBeOpened = true;
								valves.get(v2).willBeOpened = true;
								solve(valves, minutesLeft-1, valves.get(v1), valves.get(v2), currentMe.timeToOtherValve[v1], currentElephant.timeToOtherValve[v2], releasedPressure, remainingValves-2, stack);
								valves.get(v1).willBeOpened = false;
								valves.get(v2).willBeOpened = false;
							}
						}
					}
				}else{
					int remaining = -1;
					for(int v1 = 0; v1 < valves.size(); v1++){
						if(valves.get(v1).isOpened)
							continue;
						remaining = v1;
						break;
					}
					valves.get(remaining).willBeOpened = true;
					if(currentMe.timeToOtherValve[remaining] < currentElephant.timeToOtherValve[remaining]){
						solve(valves, minutesLeft-1, valves.get(remaining), currentElephant, currentMe.timeToOtherValve[remaining], currentMe.timeToOtherValve[remaining], releasedPressure, remainingValves-1, stack);
					}else{
						solve(valves, minutesLeft-1, currentMe, valves.get(remaining), currentElephant.timeToOtherValve[remaining], currentElephant.timeToOtherValve[remaining], releasedPressure, remainingValves-1, stack);
					}
					valves.get(remaining).willBeOpened = false;
				}
				currentMe.isOpened = false;
				currentElephant.isOpened = false;
			}else{
				//me reached valve
				currentMe.isOpened = true;
				for(int v = 0; v < valves.size(); v++){
					if(valves.get(v).isOpened == false && valves.get(v).willBeOpened == false){
						valves.get(v).willBeOpened = true;
						solve(valves, minutesLeft-1, valves.get(v), currentElephant, currentMe.timeToOtherValve[v], elephantRemainingSteps-1, releasedPressure, remainingValves-1, stack);
						valves.get(v).willBeOpened = false;
					}
				}
				currentMe.isOpened = false;
			}
		}else{
			if(elephantRemainingSteps <= 0){
				//elephant reached valve
				currentElephant.isOpened = true;
				for(int v = 0; v < valves.size(); v++){
					if(valves.get(v).isOpened == false && valves.get(v).willBeOpened == false){
						valves.get(v).willBeOpened = true;
						solve(valves, minutesLeft-1, currentMe, valves.get(v), meRemainingSteps-1, currentElephant.timeToOtherValve[v], releasedPressure, remainingValves-1, stack);
						valves.get(v).willBeOpened = false;
					}
				}
				currentElephant.isOpened = false;
			}else{
				//both still moving
				solve(valves, minutesLeft-1, currentMe, currentElephant, meRemainingSteps-1, elephantRemainingSteps-1, releasedPressure, remainingValves, stack);
			}
		}

		stack.remove(stack.size()-1);
		stack.remove(stack.size()-1);
		stack.remove(stack.size()-1);
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
		public boolean willBeOpened = false;
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
							timeToOtherValve[valves.indexOf(v)] = step-1;
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