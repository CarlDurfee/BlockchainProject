

public class Transaction {

	private String sender;
	private String receiver;
	private int amount;
	
	// Constructs and instance of class Transaction
	public Transaction(String sender, String receiver, int amount) {
		this.sender = sender;
		this.receiver = receiver;
		this.amount = amount;
	}

	// Getter for sender attribute of class Transaction
	public String getSender(){
		return sender;
	}
	
	// Getter for receiver attribute of class Transaction
	public String getReceiver(){
		return receiver;
	}
	
	// Getter for amount attribute of class Transaction
	public int getAmount(){
		return amount;
	}
	
	// Returns a string representation of class Transaction
	public String toString() {
		return sender + ":" + receiver + "=" + amount; 
	}
	
}