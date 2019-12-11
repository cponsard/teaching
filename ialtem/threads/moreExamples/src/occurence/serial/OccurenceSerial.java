package occurence.serial;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class OccurenceSerial {
	
	private static int countKeywordInFile(String keyword, Path file) {
		String data;
		try {
			data = new String(Files.readAllBytes(file));
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		} 
		
		int n=0;
	    StringTokenizer st = new StringTokenizer(data);
	    while (st.hasMoreTokens()) {
	    	String current=st.nextToken();
	        if (keyword.equals(current)) n++;
	    }
	    return n;
	}

	public static void main(String[] args) {
		if (args.length<2) {
			System.out.println("Usage...");
			return;
		}
		
		String keyword=args[0];
		List<Path> filelist=new ArrayList<Path>();
		
		for(int i=1; i<args.length; i++) {
			filelist.add(Paths.get("resources/occurence",args[i]));
		}
		
		// process all keywords sequentially
		int total=0;
		for(Path path: filelist) {
			int n=countKeywordInFile(keyword, path);
			total+=n;
			System.out.println(path+" "+n);
		}
		System.out.println("TOTAL: "+total);
	}
	
}
