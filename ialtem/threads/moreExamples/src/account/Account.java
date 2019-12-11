package account;

public class Account {

	int balance;
	
	public Account() {
		this.balance=0;
	}
	
	synchronized public void virer(int v) {
		int temp=balance;
		for(int i=0;i<(int)Math.random()*100; i++);
		balance=temp+v;
	}
	
	public int getBalance() {
		return balance;
	}
	
	
}
