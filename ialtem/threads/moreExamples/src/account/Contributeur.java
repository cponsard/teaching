package account;

public class Contributeur extends Thread {
	
	String name;
	int nbre;
	Account account1;
	Account account2;
	
	Contributeur(String name, int nbre, Account account1, Account account2) {  // don't try Integer
		this.name=name;
		this.nbre=nbre;
		this.account1=account1;
		this.account2=account2;
	}
	
	void increment2() {
		synchronized (account2) {
			int temp2=account2.balance;
			for(int i=0;i<(int)Math.random()*100; i++);
			account2.balance=temp2+1;			
		}
	}
	
	@Override
	public void run() {
		for (int i=0; i<nbre; i++) {
			account1.virer(1);
			increment2();
			System.out.println(name+"  "+i+" "+account1.getBalance()+" "+account2.getBalance());
		}
		System.out.println("Finished "+name);
	}
	
}
