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
			ArrayList<Blueprint> blueprints = new ArrayList<Blueprint>();

			int multValues = 1;
			for(int i=0; i<3 && sc.hasNextLine(); i++){
				blueprints.add(new Blueprint(sc.nextLine()));
				multValues *= blueprints.get(blueprints.size()-1).eval();
				System.out.println(blueprints.get(blueprints.size()-1).blueprint_num);
			}
			System.out.println(multValues);
		} catch(FileNotFoundException e){
			System.out.println("Gibts nicht");
		}
	}

	public class Blueprint {
		public int blueprint_num;

		int o_robot_cost_o;
		int c_robot_cost_o;
		int O_robot_cost_o;
		int O_robot_cost_c;
		int g_robot_cost_o;
		int g_robot_cost_O;

		int geodesMax = 0;

		Blueprint(String blueprint) {
			String[] split = blueprint.split(" ");

			//parse input
			blueprint_num = Integer.parseInt(split[1].replace(":",""));

			o_robot_cost_o = Integer.parseInt(split[6]);
			c_robot_cost_o = Integer.parseInt(split[12]);
			O_robot_cost_o = Integer.parseInt(split[18]);
			O_robot_cost_c = Integer.parseInt(split[21]);
			g_robot_cost_o = Integer.parseInt(split[27]);
			g_robot_cost_O = Integer.parseInt(split[30]);
		}

		public String toString(){
			return "Robot Nr."+blueprint_num+" Costs: "+o_robot_cost_o+"; "+c_robot_cost_o+"; "+O_robot_cost_o+", "+O_robot_cost_c+"; "+g_robot_cost_o+", "+g_robot_cost_O;
		}

		public int eval(){
			return maxGeodeOutput(32, 0, 0, 0, 0, 1, 0, 0, 0, true, true, true);
		}

		private int maxGeodeOutput(int minutesLeft, int ore, int clay, int obsidian, int geodes, int robots_o, int robots_c, int robots_O, int robots_g, boolean buy_o, boolean buy_c, boolean buy_O){
			//System.out.println(ore);
			if(minutesLeft == 0){
				geodesMax = Integer.max(geodes, geodesMax);
				return geodes;
			}

			int maxGeodesHere = geodes;
			for(int i = 0; i<minutesLeft; i++){
				maxGeodesHere += robots_g + i;
			}
			if(maxGeodesHere < geodesMax)
				return geodesMax;

			int maxOutput = 0;
			if(ore >= o_robot_cost_o && buy_o){
				maxOutput = Integer.max(maxOutput, maxGeodeOutput(minutesLeft - 1, ore + robots_o - o_robot_cost_o, clay + robots_c,  obsidian + robots_O, geodes + robots_g, robots_o + 1, robots_c, robots_O, robots_g, true, true, true));
				buy_o = false;
			}
			if(ore >= c_robot_cost_o && buy_c){
				maxOutput = Integer.max(maxOutput, maxGeodeOutput(minutesLeft - 1, ore + robots_o - c_robot_cost_o, clay + robots_c,  obsidian + robots_O, geodes + robots_g, robots_o, robots_c + 1, robots_O, robots_g, true, true, true));
				buy_c = false;
			}
			if(ore >= O_robot_cost_o && clay >= O_robot_cost_c && buy_O){
				maxOutput = Integer.max(maxOutput, maxGeodeOutput(minutesLeft - 1, ore + robots_o - O_robot_cost_o, clay + robots_c - O_robot_cost_c,  obsidian + robots_O, geodes + robots_g, robots_o, robots_c, robots_O + 1, robots_g, true, true, true));
				buy_O = false;
			}

			boolean couldBuyGeode = false;
			if(ore >= g_robot_cost_o && obsidian >= g_robot_cost_O){
				maxOutput = Integer.max(maxOutput, maxGeodeOutput(minutesLeft - 1, ore + robots_o - g_robot_cost_o, clay + robots_c,  obsidian + robots_O - g_robot_cost_O, geodes + robots_g, robots_o, robots_c, robots_O, robots_g + 1, true, true, true));
				couldBuyGeode = true;
			}
			
			if(buy_o || buy_c || buy_O || !couldBuyGeode)
				maxOutput = Integer.max(maxOutput, maxGeodeOutput(minutesLeft - 1, ore + robots_o, clay + robots_c,  obsidian + robots_O, geodes + robots_g, robots_o, robots_c, robots_O, robots_g, buy_o, buy_c, buy_O));

			return maxOutput;
		}
	}
}