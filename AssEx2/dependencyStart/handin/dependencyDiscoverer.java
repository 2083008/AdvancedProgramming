import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class dependencyDiscoverer {
	
	private ArrayList<String> paths;
	private ArrayList<String> includes;
	//private final BlockingQueue<T> workQueue; // http://stackoverflow.com/questions/2233561/producer-consumer-work-queues
	
	public static void main(String[] args) {
		dependencyDiscoverer discoverer = new dependencyDiscoverer(args);
		System.out.println(discoverer);

	}
	
	public dependencyDiscoverer(String[] args) {
		paths = getPaths(args); // check args correct here
		System.out.println("Succesfully got paths");

		includes = getIncludes(args);
	}
	/*
	* Hashmap Key = file Value = include files
	*/
	private ArrayList getIncludes(String[] args) {
		ArrayList includes = new ArrayList();
		for (int i =1; i<args.length; i++) {
			BufferedReader reader = null;
			try {
				System.out.println("Args[i] -->" + args[i]);
			    File file = new File(args[i]); // "x.y"
			    reader = new BufferedReader(new FileReader(file));

			    String line;
			    while ((line = reader.readLine()) != null) {
			        if (line.startsWith("#include \"")) {
			    		includes.add(line.split("\"")[1]); // get string between quotes
			    	}
			    }

			} catch (IOException e) {
			    e.printStackTrace(); 	
			}
		}
		return includes;
	}
	
	/*
	 * return ArrayList<String>    takes command line args
	 */
	private ArrayList<String> getPaths(String[] args) {
		ArrayList<String> paths = new ArrayList<String>();
		System.out.format("args.length = %d\n", args.length);
		if (args.length <= 1) {
			throw new IllegalArgumentException("Usage: java -classpath . dependancyDiscoverer [-Idir] extension");
		}
		paths.add(args[0].substring(2));				// add -Idir strip off -I		
		System.out.println("PATHS ->" + paths);
		String cpath = System.getenv("CPATH");
		if (cpath == null) {
			System.out.println("no paths");
			return paths;
		}

		String[] slicedcpath = cpath.split(":");  		// args[2].split(":");
		
		for (String element : slicedcpath) {
			paths.add(element);
		}
		
		
		return paths;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (String item : this.paths) {
			sb.append(item + "\n");
		}
		sb.append("Below are the includes!\n");
		for (String item : this.includes) {
			sb.append(item + "\n");
		}
		return sb.toString();
	}
}
