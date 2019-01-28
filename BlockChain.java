import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
import java.util.List;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

public class BlockChain { 

	// ---------------------------- INSTANCE VARIABLES ------------------------
	
	//The fabled blockchain
	private List<Block> chain;
	
	//Current highest index
	private int peak;

	
	// ---------------------------- CLASS METHODS ------------------------------
	
	//Constructors
	public BlockChain() {
		chain = new ArrayList<Block>();
	}

	public BlockChain(int size) {
		chain = new ArrayList<Block>(size);
	}
	
	//Reads blocks from a text file and returns them as a blockchain
	public static BlockChain fromFile(String fileName) throws FileNotFoundException {
		BlockChain blockChain = new BlockChain();
		File file = new File(fileName);
		Scanner scanner = new Scanner(file);
		
		String line;
		Integer index;
		Long timestamp;
		String sender;
		String receiver;
		Integer amount;
		String nonce;
		String previousHash = "00000";
		String hash = null;
		
		while(scanner.hasNextLine()) {			
			index = nextInt(scanner);
			if (index == null) {
				System.out.println("Could not read index from file, abort!");
				break;
			}
						
			timestamp = nextLong(scanner);
			if (timestamp == null) {
				System.out.println("Could not read timestamp from file, abort!");
				break;
			}

			sender = scanner.nextLine();
			receiver = scanner.nextLine();
			
			amount = nextInt(scanner);
			if (amount == null) {
				System.out.println("Could not read amount from file, abort!");
				break;
			}
			
			nonce = scanner.nextLine();
			
			if (index > 0) {
				previousHash = hash;
			}

			hash = scanner.nextLine();
			
			Transaction transaction = new Transaction(sender, receiver, amount);
			Block block = new Block(index, timestamp, transaction, nonce, previousHash, hash);
			
			blockChain.add(block);
		}
		
		return blockChain;
	}
	
	// Helper method for fromFile()
	private static Integer nextInt(Scanner scanner) {
		Integer value = null;
		String line = scanner.nextLine();
		
		try {
			value = Integer.valueOf(line);
		} catch (NumberFormatException e) {
			System.out.println(String.format("Is this an int, really? %s", line));
		}
		
		return value;
	}
	
	// Helper method for fromFile()
	private static Long nextLong(Scanner scanner) {
		Long value = null;
		String line = scanner.nextLine();
		
		try {
			value = Long.valueOf(line);
		} catch (NumberFormatException e) {
			System.out.println(String.format("Is this a long, really? %s", line));
		}
		
		return value;
	}
	
	//Converts current BlockChain instance into a .txt file
	public void toFile(BlockChain blockchain, String fileName) {
		File file = new File(fileName);
		Writer writer;
		try {
			writer = new BufferedWriter( new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			System.out.println("WHA!" + e.getMessage());
			return;
		}
		
		try {
			for( int i = 0; i < chain.size(); i++) {
				Block b = chain.get(i);
				Transaction t = b.getTransaction();
				writer.write(String.valueOf(b.getIndex())+ "\n");
				writer.write(String.valueOf(b.getTimestamp()) + "\n");
				writer.write(t.getSender() + "\n");
				writer.write(t.getReceiver() + "\n");
				writer.write(String.valueOf(t.getAmount()) + "\n");
				writer.write(b.getNonce() + "\n");
				if(i == chain.size()-1) {
					writer.write(b.getHash());
				} else {
					writer.write(b.getHash() + "\n");
				}
			}
		
			writer.flush();
			writer.close();

		} catch (IOException e) {
			System.out.println("Something bad happened :(" + e.getMessage());
		}
	}
	
	//Checks that the hash values are good
	public boolean validateBlockchain(){
		boolean valid = true;
		
		Block previous = null;
		Block current = null;

		for (int i = 0; i < chain.size(); i++) {
			current = chain.get(i);
			
			try {	
				// Verify hashes for all blocks
				String blockHash = Sha1.hash(current.toString());
				
				if (current.getHash().equals(blockHash)) {
					//System.out.println(String.format("Block #%d is valid!", current.getIndex()));
				} else {
					System.out.println(String.format("Block #%d's hash is not correct!", current.getIndex()));
					valid = false;
				}
				
				if (current.getIndex() != i) {
					System.out.println(String.format("Block #%d OUT OF INDEX!", current.getIndex()));
					valid = false;
				}
				
				// Verify current block's previous hash matches previous block's hash (say that five times fast)
				if (previous != null) {
					if (current.getPreviousHash().equals(previous.getHash())) {
						//System.out.println(String.format("Block #%d's previous hash matches Block #%d's hash!", current.getIndex(), previous.getIndex()));
					} else {
						System.out.println(String.format("Block #%d's previous hash does NOT match Block #%d's hash!", current.getIndex(), previous.getIndex()));
						valid = false;
					}
				}
				
				// Checks that users have not spent more bitcoins than they have
				if (getBalance(current.getTransaction().getSender()) < 0) {
					System.out.println("User " + current.getTransaction().getSender() + " has spent bitcoin that they do not have!");
					valid = false;
				}
				
				previous = current;
				
				
			} catch (UnsupportedEncodingException e) {
				System.out.println(String.format("Block #%d isn't playing nice!!", current.getIndex()));
				valid = false;
			}
		}
		
		
		
		return valid;
	}
	
	//Returns a users balance as an integer
	public int getBalance(String username) {
		
		if(username.equals("bitcoin")) {
			return 1000000;
		} else {
		
			int balance = 0;
			
		
			for(int i = 0; i < chain.size(); i++) {
				Transaction t = chain.get(i).getTransaction();
				
				if(t.getReceiver().equals(username)) {
					balance += t.getAmount();
				}
				
				if(t.getSender().equals(username)) {
					balance -= t.getAmount();
				}
				
				
			}
			
			return balance;
		}
	}
	
	//Adds a block to the block chain
	public void add(Block block) {
		chain.add(block.getIndex(), block);
		peak = block.getIndex();
	}
	
	public int getPeak() {
		return peak;
	}
	
	/** 
	main executes according to the following sequence:
	
	1) Reads the blockchain from a given file
	   after reading the 3 blocks it adds them to the ArrayList
		
		Uses:
		>fromFile()
		>toFile()
		
	2) Validates the blockchain
		-Checks all hashes correspond to the values in the corresponding block
		-Checks all **index** and **previousHash** attributes are consistent
		-Checks that users have not spent bitcoins that they do not have
		
		Uses:
		>validateBlockchain()
	
	3) Prompts the user for a new transaction and validates it
		-Specify sender, receiver, and bitcoin amount of transaction
		-Verify that sender has enough money to proceed with the transaction
		-( + assigned bitcoins - spent bitcoins > 0 )
		
		Uses:
		>getBalance()
	
	4) Adds the transaction to the blockchain
		-If it's valid, it is added to the block chain
		-each inserted block chain must start with **5 ZEROS**
		
		Uses:
		>add(Block block)
		
	5) Asks for more transactions
		Y -> back to step 3
		N -> proceed to step 6
		
	6) Saves the blockchain to a file with a specific filename
		(even if no new block has been created)
		blockchain_cdurf051.txt
	**/
	
	public static void main(String[] args) {
	
		BlockChain blockchain;
		
		//1 - reads blocks from file and adds them to the chain
		String r = System.console().readLine("Please enter name of text file to be read: ");
		try {
			blockchain = BlockChain.fromFile(r);
		} catch(FileNotFoundException e) {
			System.out.println("uh oh" + e.getMessage());
			return;
		}
		
		// 2 - validates blocks from the text 
		boolean valid = blockchain.validateBlockchain();
		
		if (valid) {
			System.out.println("This Blockchain is valid!");
		} else {
			System.out.println("This Blockchain is not valid!");
		}
		
		boolean flag = false;
		
		// 3 -> 5
		transactionloop:
		while (flag == false) {
			
			// 5 - asks user if they would like to add another block
			
			System.out.println("Would you like to add another block to the chain? ");
			
			boolean goodInput = false;
			while (goodInput == false) {
				String continu = System.console().readLine("Enter (without quotes) 'y' for yes or 'n' for no: ");
				
				if (continu.equals("y")) {
					flag = false;
					goodInput = true;
				} else if (continu.equals("n")) {
					flag = true;
					goodInput = true;
					break transactionloop;
				} else {
					goodInput = false;
					System.out.println("Please input the letter 'y' or the letter 'n'" + "\n");
				}
			}
			
			// 3 - accepts new transaction
			String sender = System.console().readLine("Please enter sender of transaction: ");
			String receiver = System.console().readLine("Please enter receiver of transaction: ");
			String a = System.console().readLine("Please enter amount of transaction: ");
			int amount = Integer.parseInt(a);
			
			if (sender.equals(receiver)) {
				
				System.out.println("Miners may not send bitcoins to themselves! ");
				break;
				
			} else if ( blockchain.getBalance(sender) > 0 ) {
				
				Transaction transaction = new Transaction(sender, receiver, amount);
				
				String previousHash = blockchain.chain.get(blockchain.getPeak()).getHash();
				
				Block b = new Block(blockchain.getPeak()+1, transaction, previousHash);
				b.nonceGenerator(b);
				
				// 4 - adds new block to the chain and validates
				blockchain.add(b);
				
				blockchain.validateBlockchain();
				
			} else {
				System.out.println(String.format("Miner %s does not have enough bitcoin to complete this transaction!", sender));
			}
			
			
		}
		
		// 6 - writes text file
		String w = System.console().readLine("Please enter name of text file to be written: ");
		blockchain.toFile(blockchain, w);
	}
}