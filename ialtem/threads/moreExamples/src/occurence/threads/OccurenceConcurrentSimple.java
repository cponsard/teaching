package occurence.threads;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class OccurenceConcurrentSimple extends Thread {
	
	String keyword;
	Path file;
	int n;

	private long getThreadCpuTime() {
		System.out.println("SUPPORT: "+ManagementFactory.getThreadMXBean().isCurrentThreadCpuTimeSupported());
		return ManagementFactory.getThreadMXBean().getThreadCpuTime(getId());
	}

	
	public OccurenceConcurrentSimple(String keyword, Path file) {
		this.keyword=keyword;
		this.file=file;
		this.n=0;
	}
	
	public int getOccurences() {
		return n;
	}
	
	public void run() {
		System.out.println(getThreadCpuTime());
		System.out.println("Scanning "+file);
		String data;
		try {
			data = new String(Files.readAllBytes(file));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} 
		
	    StringTokenizer st = new StringTokenizer(data);
	    while (st.hasMoreTokens()) {
	    	String current=st.nextToken();
	        if (keyword.equals(current)) n++;
	    }
		System.out.println("FIN: "+getThreadCpuTime());
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
		
		// starting and keeping track
		OccurenceConcurrentSimple[] runners=new OccurenceConcurrentSimple[filelist.size()];
		int i=0;
		for(Path file: filelist) {
			OccurenceConcurrentSimple runner=new OccurenceConcurrentSimple(keyword, file);
			runners[i]=runner;
			runner.start();
			i++;
		}

		//joining and consolidating
		int total=0;
		i=0;
		for(Path file: filelist) {
			OccurenceConcurrentSimple runner=runners[i];
			try {
				runner.join();
				System.out.println(file+" "+runner.getOccurences());
				total+=runner.getOccurences();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
		System.out.println("TOTAL: "+total);
	}
	
}
