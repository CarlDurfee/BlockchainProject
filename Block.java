import java.sql.Timestamp;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.stream.*;
import java.util.Arrays;

public class Block {

	// The index of the block in the list
	private int index ;
	
	// The time at which the transaction has been processed
	private java.sql.Timestamp timestamp;
	
	// The transaction object
	private Transaction transaction;
	
	// Random string for proof of work
	private String nonce;
	
	// Previous hash (set to "00000" in first block)
	private String previousHash;
	
	// Hash of the block
	private String hash; 

	
	// Constructor for blocks in text files
	public Block(int index, long timestamp, Transaction transaction, String nonce, String previousHash, String hash) {
		this.index = index;
		this.timestamp = new Timestamp(timestamp);
		this.transaction = transaction;
		this.nonce = nonce;
		this.previousHash = previousHash;
		this.hash = hash;
	}
	
	// Constructor for user inputted blocks
	public Block(int index, Transaction transaction, String previousHash){
		timestamp = new Timestamp(System.currentTimeMillis());
		this.index = index;
		this.transaction = transaction;
		this.previousHash = previousHash;
		this.hash = "     ";
	}
	
	// Proof of work algorithm
	public void nonceGenerator(Block block) {
		Random random = new Random();
		int size = 20;
		int trials = 0;
		String nonce = "";
		String hash = block.getHash();
		
		while(!(hash.substring(0,5).equals("00000"))) {
			IntStream intStream = random.ints(size,33,127);
			int[] asciiCharacters = intStream.toArray();
			
			for(int i = 0; i < size; i++) {
				nonce += ((char)asciiCharacters[i]);
				block.setNonce(nonce);
				try {
					hash = Sha1.hash(block.toString());
				} catch (UnsupportedEncodingException e) {
					System.out.println("This should probably never happen.");
				}
			}
			trials++;
			nonce = "";
		}
		block.setHash(hash);
		System.out.println(String.format("Block #%d hashed in %d trials!", block.getIndex()+1, trials));
	}
	
	// Sets the hash of a block
	// Necessary for the proof of work method
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	// Sets the nonce of a block
	// Necessary for the proof of work method
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	
	// Returns index
	public int getIndex() {
		return index;
	}

	// Returns timestamp
	public long getTimestamp() {
		return timestamp.getTime();
	}
	
	// Returns transaction
	public Transaction getTransaction() {
		return transaction;
	}

	// Returns nonce
	public String getNonce() {
		return nonce;
	}
	
	// Returns previousHash
	public String getPreviousHash() {
		return previousHash;
	}
	
	// Returns hash
	public String getHash() {
		return hash;
	}

	// Returns a string representation of an instance of class block
	public String toString() {
		return timestamp.toString() + ":" + transaction.toString()
			+ "." + nonce + previousHash;
	}

}