
import java.util.ArrayList;
import java.util.Hashtable;
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


/**
*	Alex Smith 2083008s APH Excercise 2
*	This is my own work as defined in the Academic Ethics agreement
*/


public class dependencyDiscoverer {
	
	private ArrayList<String> paths;
	private ConcurrentHashMap<String,ArrayList<String>> master;
	private ConcurrentLinkedQueue<String> workQueue;
	
	public static void main(String[] args) {
		dependencyDiscoverer discoverer = new dependencyDiscoverer(args);
		
		Integer threadNum = 2;
		
		String crawlerpath = System.getenv("CRAWLER_THREADS");
		if (crawlerpath != null) {
			threadNum =  Integer.parseInt(crawlerpath);
		}

		discoverer.workQueue = discoverer.getWorkQueue(args);

		while (discoverer.workQueue.peek() != null) {
			Worker newThread = new Worker(discoverer.workQueue.poll(), discoverer.master);
			Thread t1 = new Thread(newThread);
			t1.start();
			try {
				t1.join();
			} catch (InterruptedException e ){

			}
		}	
		System.out.println(discoverer);
	}
	
	public dependencyDiscoverer(String[] args) {
		paths = getPaths(args); 									// check args correct here
		//workQueue = new ConcurrentLinkedQueue<String>(); //getWorkQueue(args);
		workQueue = getWorkQueue(args);
		master = new ConcurrentHashMap<String,ArrayList<String>>();
		//master = getMaster(workQueue);
	}

	public ConcurrentLinkedQueue<String> getWorkQueue(String[] args){
		ConcurrentLinkedQueue<String> workQueue = new ConcurrentLinkedQueue<String>();
		for (int i=1; i<args.length; i++) {
			workQueue.add(args[i]);
			//workQueue.add(args[i]);
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
		String file_name;
		while (workQueue.peek() != null) {								// go through workQueue

			//System.out.println("workQueue = " + workQueue );
			file_name = workQueue.poll();
			ArrayList<String> file_includes;
			if(getExtension(file_name).equals(".h")) {
				file_includes = getIncludes(fileExists(file_name));
			} else {
				file_includes = getIncludes(file_name);
			}

			String header_path;
			for (String item : file_includes) {
				//System.out.println("Item in file_includes " + item);
				if(getExtension(item).equals(".h")) {
					if (fileExists(item) != null) {
						workQueue.add(item);
					}
				}
			}
			master.put(file_name, file_includes);
			workQueue.remove(file_name);

		}
		return master;
	}
	/**
	* Returns path to file if file found in paths, otherwise null
	*/
	public String fileExists(String file) {
		for (String path : this.paths) {
			try {
				BufferedReader reader = null;
				
				File file_read = new File(path + file);
				reader = new BufferedReader(new FileReader(file_read));
				//System.out.println("FILE EXISTS -> path -> " + path + file);
				return path + file;
			} catch (FileNotFoundException e) {
				continue;
			}
		}
		return null;
	}
	public ArrayList<String> getIncludes(String file_name) {
		ArrayList<String> file_includes = new ArrayList<String>();
		try {
			BufferedReader reader = null;
			
			File file = new File(file_name);
			reader = new BufferedReader(new FileReader(file));

			//System.out.println("Looking for " + file_name);
			
			String line;
			String header;
			while ((line = reader.readLine()) != null) {
				if (line.contains("#include \"")) {     			// if we find include then search??
					header = line.split("\"")[1];        			// eg a.h
					if (getExtension(header).equals(".h")) {
						file_includes.add(header);
					}
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			//System.out.println("file not found ->" + file_name);
		} catch (IOException e) {
			e.printStackTrace(); 	
		}
		return file_includes;
	}
	
	@Override
	public String toString() {

		//System.out.println("BELOW IS THE TABLE");
		StringBuffer sb = new StringBuffer();
		String[] split_file;
		String file;
		String raw_file;
		ArrayList<String> extra_includes;
		
		for (Map.Entry<String, ArrayList<String>> each : this.master.entrySet()) {
			String file_name = each.getKey();
			ArrayList<String> includes = each.getValue();
			if (!getExtension(file_name).equals(".h")) {
				split_file = file_name.split("/"); //get /Test/.../x.y to x.y
				file = split_file[split_file.length -1];
				raw_file = file.substring(0, file.length() -2);
				
				sb.append(raw_file + ".o : ");
				sb.append(file + " ");
				for (String item : includes) {
					extra_includes = this.master.get(item);
					if (extra_includes != null) {
						for (String ea : extra_includes) {
							sb.append(ea);
							sb.append(" ");
						}
					}
					sb.append(item);
					sb.append(" ");
				}
				sb.append("\n");
			} 
	    }
		return sb.toString();
	}
}