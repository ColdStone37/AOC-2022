import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class part1{
	public static void main(String[] args){
		part1 main = new part1();
		main.mainNonStatic(args);
	}

	public void mainNonStatic(String[] args){
		try{
			File file = new File(args[0]);
			Scanner sc = new Scanner(file);
			ArrayList<Vector> vectors = new ArrayList<Vector>();
			while(sc.hasNextLine()){
				vectors.add(new Vector(sc.nextLine()));
			}
			Vector minVector = new Vector(1,1,1);
			Vector maxVector = new Vector(0,0,0);
			for(Vector v:vectors){
				minVector = minVector.min(minVector, v);
				maxVector = maxVector.max(maxVector, v);
			}

			Vector offset = new Vector(-(minVector.x-1), -(minVector.y-1), -(minVector.z-1));
			Vector size = new Vector(maxVector.x-minVector.x+3, maxVector.y-minVector.y+3,maxVector.z-minVector.z+3);

			boolean[][][] map = new boolean[size.x][size.y][size.z];

			for(Vector v:vectors){
				v.add(offset);
				map[v.x][v.y][v.z] = true;
			}

			Vector[] directions = new Vector[6];
			directions[0] = new Vector(1,0,0);
			directions[1] = new Vector(0,1,0);
			directions[2] = new Vector(0,0,1);
			directions[3] = new Vector(-1,0,0);
			directions[4] = new Vector(0,-1,0);
			directions[5] = new Vector(0,0,-1);
			
			int surfaceArea = 0;
			for(Vector v:vectors){
				for(Vector dir:directions){
					if(!map[v.x+dir.x][v.y+dir.y][v.z+dir.z]){
						surfaceArea++;
					}
				}
			}

			System.out.println(surfaceArea);
		} catch(FileNotFoundException e){
			System.out.println("Gibts nicht");
		}
	}

	public class Vector{
		public int x, y, z;

		public Vector(String s){
			String[] split = s.replace("\n","").split(",");
			x = Integer.parseInt(split[0]);
			y = Integer.parseInt(split[1]);
			z = Integer.parseInt(split[2]);
		}

		public Vector(int X, int Y, int Z){
			x = X;
			y = Y;
			z = Z;
		}

		public Vector max(Vector v1, Vector v2){
			Vector max = new Vector(0,0,0);
			max.x = Integer.max(v1.x, v2.x);
			max.y = Integer.max(v1.y, v2.y);
			max.z = Integer.max(v1.z, v2.z);
			return max;
		}

		public Vector min(Vector v1, Vector v2){
			Vector min = new Vector(0,0,0);
			min.x = Integer.min(v1.x, v2.x);
			min.y = Integer.min(v1.y, v2.y);
			min.z = Integer.min(v1.z, v2.z);
			return min;
		}

		public void add(Vector v){
			x += v.x;
			y += v.y;
			z += v.z;
		}

		public String toString(){
			return x+","+y+","+z;
		}
	}
}