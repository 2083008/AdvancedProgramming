import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Worker implements Runnable{

	private String file_string;
	private ConcurrentHashMap<String,ArrayList<String>> master;
	private String found_include;
	
	public Worker(String file_string, ConcurrentHashMap<String,ArrayList<String>> master){// , ConcurrentHashMap<String,ArrayList<String>> master) {
		this.file_string = file_string;
		this.master = master;
	}
	
	
	@Override
	public void run() {
		ArrayList<String> file_includes = new ArrayList<String>();
		System.out.println("From the worker THREAD");
		try {
			
			BufferedReader reader = null;
			File file = new File(file_string);
			reader = new BufferedReader(new FileReader(file));
			
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains("#include \"")) {     // if we find include then search??
					file_includes.add(line.split("\"")[1]);
				}
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("file not found");
		} catch (IOException e) {
			e.printStackTrace(); 	
		}
		master.put(file_string, file_includes);
		System.out.println("FILE_INCLUDES " + file_includes);
	}
	
}