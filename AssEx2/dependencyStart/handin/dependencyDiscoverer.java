import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
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
	//private final BlockingQueue<T> workQueue; // http://stackoverflow.com/questions/2233561/producer-consumer-work-queues
	
	public static void main(String[] args) {
		dependencyDiscoverer discoverer = new dependencyDiscoverer(args);
		System.out.println("Search Paths -> " + discoverer.paths);
		System.out.println("Work Queue -> " + discoverer.workQueue);
		
		Integer threadNum = 2;
		
		String crawlerpath = System.getenv("CRAWLER_THREADS");
		if (crawlerpath != null) {
			threadNum =  Integer.parseInt(crawlerpath);
		}
		
		//Worker worker = new Worker("Test/*.c");
		//Thread thread = new Thread(worker);
		//thread.start();
		System.out.println(discoverer);
	}
	
	public dependencyDiscoverer(String[] args) {
		paths = getPaths(args); // check args correct here
		workQueue = getWorkQueue(args);
		master = getMaster(workQueue);
	}

	public ConcurrentLinkedQueue<String> getWorkQueue(String[] args){
		ConcurrentLinkedQueue<String> workQueue = new ConcurrentLinkedQueue<String>();
		for (int i=1; i<args.length; i++) {
			workQueue.add(args[i]);
		}
		return workQueue;
	}

	/**
	 * return ArrayList<String>    takes command line args
	 */
	private ArrayList<String> getPaths(String[] args) {
		ArrayList<String> paths = new ArrayList<String>();
		if (args.length <= 1) {
			throw new IllegalArgumentException("Usage: java -classpath . dependancyDiscoverer [-Idir] extension");
		}
		paths.add("./");     							// check working directory
		paths.add(args[0].substring(2) + "/");				// add -Idir strip off -I		
		String cpath = System.getenv("CPATH");
		String[] slicedcpath = null;
		
		if (cpath != null) {
			slicedcpath = cpath.split(":");  		// args[2].split(":");
		} else {
			return paths;
		}
		
		for (String element : slicedcpath) {
			paths.add(element + "/");
		}
		return paths;
	}

	public String getExtension(String path) {
		String[] name_split = path.split("\"");
		String extension = name_split[name_split.length -1];
		extension = extension.substring(extension.length() -2, extension.length());
		return extension;
	}
	/*
	* ConcurrentHashMap Key = file Value = include files
	*/
	//@SuppressWarnings("resource")
	private ConcurrentHashMap<String,ArrayList<String>> getMaster(ConcurrentLinkedQueue<String> workQueue) {
		
		ConcurrentHashMap<String,ArrayList<String>> master = new ConcurrentHashMap<String,ArrayList<String>>();

		for (String file_name : workQueue) {

			ArrayList<String> file_includes = getIncludes(file_name);
			for (String item : file_includes) {
				System.out.println("Includes " + file_includes);
				if(getExtension(item).equals(".h")) {
					workQueue.add(item);
				}
			}
			/*
			if (!getExtension(file_name).equals(".h")) {
				master.put(file_name, file_includes);
				continue;
			}

			if (getExtension(header).equals(".h")) {
				file_includes.add(header);
				workQueue.add(header);
				ArrayList<String> list = this.master.get(file_name);
				list.add(header);						// should check if item already exists

			}
			*/
			System.out.println("Filename ->>> " + file_name);
			System.out.println("FILE_INCLUDES " + file_includes);
			}
		return master;
	}

	public ArrayList<String> getIncludes(String file_name) {
		ArrayList<String> file_includes = new ArrayList<String>();
		try {
			BufferedReader reader = null;
			
			File file = new File(file_name);
			reader = new BufferedReader(new FileReader(file));

			System.out.println("Looking for " + file_name);
			
			String line;
			String header;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("#include \"")) {     		// if we find include then search??
					header = line.split("\"")[1];        		// eg a.h
					file_includes.add(header); 
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("file not found");
		} catch (IOException e) {
			e.printStackTrace(); 	
		}
		return file_includes;
	}
	
	@Override
	public String toString() {
		System.out.println("BELOW IS THE TABLE");
		StringBuffer sb = new StringBuffer();
		Iterator<Entry<String, ArrayList<String>>> it = this.master.entrySet().iterator();
		String[] split_file;
		String file;
		String raw_file;
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        split_file = ((String) pair.getKey()).split("/"); //get /Test/.../x.y to x.y
	        file = split_file[split_file.length -1];
	        raw_file = file.substring(0, file.length() -2);
	        sb.append(raw_file + ".o ");
	        sb.append(file);
	        sb.append(pair.getValue());
	        sb.append("\n");
	        //System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
		return sb.toString();
	}
}