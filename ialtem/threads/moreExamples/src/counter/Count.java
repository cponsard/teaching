package counter;

public class Count extends Thread {
	
	static int c1=0;
	static Integer c2=0; // need an object !
	static final int N=5;
	static final int M=10;
	
	String name;
	
	Count(String name) {
		this.name=name;
	}
	
	static synchronized void increment1() {
			int temp1=c1;
			for(int i=0;i<(int)Math.random()*100; i++);
			c1=temp1+1;
	}

	void increment2() {
		synchronized (c2) {
			int temp2=c2;
			for(int i=0;i<(int)Math.random()*100; i++);
			c2=temp2+1;			
		}
	}

	
	@Override
	public void run() {
		for (int i=0; i<M; i++) {
			increment1();
			increment2();
			System.out.println(name+"  "+i+" "+c1+" "+c2);
		}
		System.out.println("Finished "+name);
	}
	
	
	public static void main(String args[]) {
		Count[] t=new Count[N];
		
		// start
		for(int i=0; i<N; i++) {
			t[i]=new Count("Count-"+i);
			t[i].start();
		}
		
		// join
		/*
		for(int i=0; i<N; i++) {
			try {
				t[i].join();
				System.out.println("Joined "+i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} */

		
	}
}
