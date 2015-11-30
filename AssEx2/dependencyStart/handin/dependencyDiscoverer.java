import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;


public class dependencyDiscoverer {
	
	private ArrayList<String> paths;
	private ConcurrentHashMap<String,ArrayList<String>> master;
	private ConcurrentLinkedQueue<String> workQueue;
	private Hashtable<String,ArrayList<String>> master;
	//private final BlockingQueue<T> workQueue; // http://stackoverflow.com/questions/2233561/producer-consumer-work-queues
	
	public static void main(String[] args) {
		dependencyDiscoverer discoverer = new dependencyDiscoverer(args);
		System.out.println("Search Paths -> " + discoverer.paths);
		System.out.println("Work Queue -> " + discoverer.workQueue);
		System.out.println(discoverer);
	}
	
	public dependencyDiscoverer(String[] args) {
		paths = getPaths(args); // check args correct here
		workQueue = getWorkQueue(args);
		System.out.println("Succesfully got paths" + paths);
		master = getMaster(workQueue);
	}

	public ConcurrentLinkedQueue<String> getWorkQueue(String[] args){
		ConcurrentLinkedQueue<String> workQueue = new ConcurrentLinkedQueue();
		for (int i=1; i<args.length; i++) {
			workQueue.add(args[i]);
		}
		return workQueue;
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
		return paths;
	}
	/*
	* ConcurrentHashMap Key = file Value = include files
	*/
	private ConcurrentHashMap<String,ArrayList<String>> getMaster(ConcurrentLinkedQueue workQueue) {
		ConcurrentHashMap<String,ArrayList<String>> master = new ConcurrentHashMap();

		for (String file_name : workQueue) {
			try {
				BufferedReader reader = null;
				ArrayList file_includes = new ArrayList();
				File file = new File(paths.get(file_name));
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
			master.put(file_name, file_includes);
			System.out.println("FILE_INCLUDES " + file_includes);
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
