package occurence.sharedQueue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringTokenizer;

public class Task {

	Path file;
	String keyword;
	int n;
		
	public Task( String keyword, Path file) {
		this.file=file;
		this.keyword=keyword;
	}
	
	Path getFile() {
		return file;
	}
	
	public int getResult() {
		return n;
	}
	
	public void execute() {
		System.out.println("Looking for "+keyword+" into "+file+" on "+Thread.currentThread().getName());
		String data;
		try {
			data = new String(Files.readAllBytes(file));
		} catch (IOException e) {
			e.printStackTrace();
			n=0;
			return;
		} 
		
	    n=0;
	    StringTokenizer st = new StringTokenizer(data);
	    while (st.hasMoreTokens()) {
	    	String current=st.nextToken();
	        if (keyword.equals(current)) n++;
	    }
		System.out.println("Done looking for "+keyword+" into "+file+" on "+Thread.currentThread().getName());
	}

}
