package occurence.sharedQueue;


import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Version with some errors to debug in lecture
 * 
 * @author cp
 *
 */
public class OccurrenceMain {

	public static int NRUNNERS=2;
	
	public static int process(String[] args) {
		String keyword=args[0];
		List<Task> tasks=new ArrayList<Task>();
		for(int i=1; i<args.length; i++) {
			tasks.add(new Task(keyword,Paths.get("resources/occurence",args[i])));
		}
	

		// setup
		SharedQueue<Task> queue=new SharedQueue<Task>();
		// adding work
		for(Task task:tasks) {
			queue.addItem(task);
		}
		
		
		Runner[] runners=new Runner[NRUNNERS];
		for(int i=0; i<NRUNNERS; i++) {
			Runner runner=new Runner(queue,i);
			runners[i]=runner;
			runner.start();
		}
	

		// waiting for end of work
		queue.waitEmpty();

		// stopping workers
		for(int i=0; i<NRUNNERS; i++) {
//			runners[i].requestStop();  // not necessary
			try {
				runners[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		
		// print result
		int total=0;
		for(Task task:tasks) {
			System.out.println(task.getFile()+" "+task.getResult());
			total+=task.getResult();
		}
		System.out.println("TOTAL: "+total);
		

		
		return total;
	}

	public static void main(String[] args) {
		if (args.length<2) {
			System.out.println("Usage...");
			return;
		}
		
		int res=0;
		for(int i=0; i<1000; i++) {
			System.out.println("************* ITERATION "+i);
			res=process(args);
			if (res!=6) {
				System.out.println("==> FAILED");
				break;
			}
		}
		
		if (res==6) System.out.println("==> SUCCESS");
	}
	
}
