package account;

public class RunTransactions {

	static final int N=5;
	static final int M=10;
	
	static void inc(Integer i1) {
		i1=i1+1;
		System.out.println(i1);
	}
	
	public static void main(String args[]) {
		Account account1=new Account();
		Account account2=new Account();
		
		Contributeur[] t=new Contributeur[N];
		
		// start
		for(int i=0; i<N; i++) {
			t[i]=new Contributeur("Count-"+i,M,account1,account2);
			t[i].start();
		}
		
		// join
		for(int i=0; i<N; i++) {
			try {
				t[i].join();
				System.out.println("Joined "+i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
		
		// test Integer are passed by value (like int)
		/*
		System.out.println("==================");
		Integer i1=10;
		System.out.println(i1);
		inc(i1);
		System.out.println(i1); */
	}

}
