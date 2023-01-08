import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class part2{
	public static void main(String[] args){
		part2 main = new part2();
		main.mainNonStatic(args);
	}

	public void mainNonStatic(String[] args){
		int size = 4000000;

		try{
			File file = new File(args[0]);
			Scanner sc = new Scanner(file);

			ArrayList<Sensor> sensorsList = new ArrayList<Sensor>();

			while(sc.hasNextLine()){
				String line = sc.nextLine();
				String[] splitLine = line.split(" ");

				int sensorX = Integer.parseInt(splitLine[2].split("=")[1].replace(",",""));
				int sensorY = Integer.parseInt(splitLine[3].split("=")[1].replace(":",""));

				int beaconX = Integer.parseInt(splitLine[8].split("=")[1].replace(",",""));
				int beaconY = Integer.parseInt(splitLine[9].split("=")[1].replace("\n",""));

				int manhattenDist = Math.abs(sensorX - beaconX) + Math.abs(sensorY - beaconY);

				sensorsList.add(new Sensor(sensorX, sensorY, manhattenDist));
			}

			Sensor[] sensorsArray = new Sensor[sensorsList.size()];
			for(int i=0; i<sensorsList.size(); i++)
				sensorsArray[i] = sensorsList.get(i);
			sensorsList = null;

			int lastIndex = 0;
			for(int x=0; x<size; x++){
				for(int y=0; y<size; y++){
					int range = sensorsArray[lastIndex].inRange(x,y);
					if(range >= 0){
						y += range;
						continue;
					}

					boolean found = true;
					
					for(int i=0; i<sensorsArray.length; i++){
						range = sensorsArray[i].inRange(x,y);
						if(range >= 0){
							lastIndex = i;
							y += range;
							found = false;
							break;
						}
					}
					if(found){
						long X = (long) x;
						long Y = (long) y;
						System.out.println(x+" "+y+" "+X*40000L+Y);
					}
				}
			}
		} catch(FileNotFoundException e){
			System.out.println("Gibts nicht");
		}
	}

	class Sensor{
		public int x, y;
		public int range;

		public Sensor(int X, int Y, int r){
			x=X;
			y=Y;
			range = r;
		}

		public int inRange(int X, int Y){
			int dist = Math.abs(X-x)+Math.abs(Y-y);
			return range - dist;
		}
	}
}