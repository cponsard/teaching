package occurence.threadPool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class OccurenceConcurrentThreadPool implements Callable<Integer> {
	
	static int MAX_T=2;
	
	String keyword;
	Path file;
	
	public OccurenceConcurrentThreadPool(String keyword, Path file) {
		this.keyword=keyword;
		this.file=file;
	}
	
	public Integer call() {
		System.out.println("Scanning "+file);
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
	    System.out.println("DONE "+file);
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

		List<Future<Integer>> results=new ArrayList<Future<Integer>>();
        ExecutorService pool = Executors.newFixedThreadPool(MAX_T);       
		
        // launching tasks
		int i=0;
		for(Path file: filelist) {
			OccurenceConcurrentThreadPool runner=new OccurenceConcurrentThreadPool(keyword, file);
			results.add(pool.submit(runner));
			i++;
		}

		// using futures for implicit join
		int total=0;
		i=0;
		for(Future<Integer> future: results) {
			int n;
			try {
				n = future.get(); // will block until result is available
				System.out.println(filelist.get(i)+" "+n);
				total+=n;
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			i++;
		}

		System.out.println("TOTAL: "+total);

        pool.shutdown(); // release resource
	}
	
}
