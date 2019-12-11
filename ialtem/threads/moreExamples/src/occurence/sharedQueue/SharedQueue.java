package occurence.sharedQueue;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author UNamur
 * Delivers items randomly
 *
 */
public class SharedQueue<E>  {
	
	private List<E> queue=new LinkedList<>();
	
	public synchronized void addItem(E item){
		assert item!=null;
		
		queue.add(item);		
		notifyAll();
	}
	
	public synchronized E getItem(){
		while (queue.isEmpty()){
			try {
				wait(1000); // added some maximal time to see status
				System.out.println("waiting for item");
			} catch (InterruptedException e) {
				assert false;
				throw new UnknownError();
			}
		}
		notifyAll();
		return queue.remove(0);
	}

	/**
	 * 
	 * @return the size of the queue
	 */
	public synchronized int size() {
		return queue.size();
	}

	public synchronized void waitEmpty() {
		System.out.println("Waiting for empty queue");
		while(queue.size()>0) {
			try {
				System.out.println("Still Waiting");
				wait(1000);  // added some maximal time to see status
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Empty queue");
	}

}
