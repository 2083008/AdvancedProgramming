import java.util.ArrayList;

public class dependencyDiscoverer {
	
	ArrayList<String> paths;
	
	
	public static void main(String[] args) {
		//BlockingQueue<T> workQueue = new BlockingQueue<T>();
		//ArrayList<String> paths = getPaths(args);
		dependencyDiscoverer discoverer = new dependencyDiscoverer(args);
		System.out.println(discoverer);
	}
	
	public dependencyDiscoverer(String[] args){
		paths = getPaths(args);
	}
	
	/*
	 * return ArrayList<String>    takes command line args
	 */
	private ArrayList<String> getPaths(String[] args) {
		ArrayList<String> paths = new ArrayList<String>();
		System.out.format("args.length = %d\n", args.length);
		if (args.length < 1) {
			throw new IllegalArgumentException("Usage: java -classpath . dependancyDiscoverer [-Idir] extension");
		}
		paths.add(args[0].substring(2));				// add -Idir strip off -I		
		
		String Cpath = System.getenv("CPATH");
		String[] slicedCpath = Cpath.split(":");  		// args[2].split(":");
		
		if (Cpath.length() == 0) return paths;
 
		for (String element : slicedCpath) {
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
		return sb.toString();
	}
}
