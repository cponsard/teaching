package occurence.sharedQueue;

public class Runner extends Thread {
	
	SharedQueue<Task> queue;
	boolean running;
	
	public Runner(SharedQueue<Task> queue, int i) {
		this.queue=queue;
		this.running=false;
		this.setName("Runner-"+i);
	}
		
	public void requestStop() {
		System.out.println("Request stop "+getName());
		running=false;
	}
	
	public void run() {
		System.out.println("Runner "+getName()+" started");
		running=true;
		while(running) {
			Task task;
			synchronized(queue) {
			if (queue.size()>0) {
				task=queue.getItem();					
			} else task=null;
			}
			if (task!=null) {
			task.execute();
			} else {
			running=false;
			}
		}
		System.out.println("Runner "+getName()+" finished");
	}
	
}






/*
 			if (queue.size()>0) {
				Task task=queue.getItem();
				task.execute();
			} else { 
				running=false;
			}
 */

/*
Task task;
synchronized(queue) {
if (queue.size()>0) {
	task=queue.getItem();					
} else task=null;
}
if (task!=null) {
task.execute();
} else {
running=false;
}
*/