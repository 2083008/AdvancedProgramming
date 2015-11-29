import java.util.ArrayList;
import java.util.Hashtable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class dependencyDiscoverer {
	
	private ArrayList<String> paths;
	private Hashtable<String,ArrayList<String>> master;
	//private final BlockingQueue<T> workQueue; // http://stackoverflow.com/questions/2233561/producer-consumer-work-queues
	
	public static void main(String[] args) {
		dependencyDiscoverer discoverer = new dependencyDiscoverer(args);
		System.out.println(discoverer);

	}
	
	public dependencyDiscoverer(String[] args) {
		paths = getPaths(args); // check args correct here
		System.out.println("Succesfully got paths" + paths);
		
		ArrayList<String> files = new ArrayList<String>();
		for (int i = 1; i<args.length; i++) {
			files.add(args[i]);
		}
		master = getMaster(paths,files);
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
		paths.add("./");     							// check working directory
		paths.add(args[0].substring(2) + "/");				// add -Idir strip off -I		
		System.out.println("PATHS ->" + paths);
		String cpath = System.getenv("CPATH");
		if (cpath != null) {
			System.out.println("WE are in here");
			String[] slicedcpath = cpath.split(":");  		// args[2].split(":");
		
			for (String element : slicedcpath) {
				paths.add(element + "/");
			}
		}
		return paths;
	}
	
	/*
	* Hashmap Key = file Value = include files
	*/
	private Hashtable<String,ArrayList<String>> getMaster(ArrayList<String> paths, ArrayList<String> files) {
		Hashtable<String,ArrayList<String>> master = new Hashtable<String, ArrayList<String>>();
		for (int i =0; i<paths.size(); i++) {
			System.out.println("Paths [i] -> " + paths.get(i));
			BufferedReader reader = null;
			ArrayList<String> file_includes = new ArrayList<String>();
			
				for(int j=0; j<files.size(); j++) {
					System.out.println("Looking for file ... " +paths.get(i) + files.get(j));
					try {
					    File file = new File(paths.get(i) + files.get(j)); // "x.y"
					    reader = new BufferedReader(new FileReader(file));
					    String line;
					    while ((line = reader.readLine()) != null) {
					        if (line.startsWith("#include \"")) {
					    		file_includes.add(line.split("\"")[1]);
					    		
					    	}
					 	}
					} catch (FileNotFoundException e) {
					    	continue;
					} catch (IOException e) {
				   			e.printStackTrace(); 	
					}
				    master.put(paths.get(i), file_includes);
				    System.out.println("FILE_INCLUDES " + file_includes);
				}
			
		}
		return master;
	}
	
	
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (String item : this.paths) {
			sb.append(item + "\n");
		}
		return sb.toString();
	}
}
