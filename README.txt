Carl Durfee
Miner id: cdurf051

Class: Block.java

Method: Block(index, transaction, nonce, previousHash, hash)
This method is used to create an instance of class block from a text file.

Method: Block(index, transaction, previousHash)
This method is used to create an instance of class block from user input.

Method: nonceGenerator(block)
This is the proof of work method. It works by creating an array of ints from a
randomized intstream created by java.random.ints(size, lowerbound, upperbound) and
then it converts each element of the array of ints into a character and adds it to a new
nonce which is then manually set to the parameter block. This will loop until the nonce
manually set to the block results in the block producing a hash that begins with five 0’s.
After which it sets the hash and the nonce of the block and terminates.

Method: setHash(hash)
Manually sets the hash of the block. This is used by the proof of work method.

Method: setNonce(nonce)
Manually sets the nonce of the block. This is used by the proof of work method.

Method: getIndex()
Getter for the index attribute of class block.

Method: getTimestamp()
Getter for the timestamp attribute of class block.

Method: getTransaction()
Getter for the transaction attribute of class block.

Method: getNonce()
Getter for the nonce attribute of class block

Method: getPreviousHash()
Getter for the previousHash attribute of class block.

Method: getHash()
Getter for the hash attribute of class block
Class: Transaction.java

Method: Transaction(sender, receiver, amount)
Initializes an instance of class Transaction given a sender, receiver, and amount.

Method: getSender()
Getter for sender attribute of class Transaction.

Method: getReceiver()
Getter for receiver attribute of class Transaction.

Method: getAmount()
Getter for amount attribute of class Transaction.

Method:toString()
Returns a string representation of class Transaction.


Class: BlockChain.java

Method: BlockChain()
Default constructor, creates arraylist with default size (10).

Method: BlockChain(size)
Constructor that initializes the arraylist with the parameter size.

Method: fromFile(fileName)

Method that reads a given text file and returns an instance of class BlockChain
with the blocks that are in that text file.

Method: nextInt(scanner)
Helper method that reads the next int from a text file. Used by fromFile.

Method: nextLong(scanner)
Helper method that reads the next long from a text file. Used by fromFile.

Method: toFile(blockchain, fileName)
Writes an instance of blockchain to a text file.

Method: validateBlockchain()
Validates current instance of blockchain by checking for inconsistencies with the
hash, previousHash, and index values of the blockchain.

Method: getBalance(username)
Returns as an integer the bitcoin balance of user specified by parameter username.

Method: add(block)
Adds a block to this instance of class Blockchain

Method: getPeak()
Returns the index of the current front of the blockchain

Method: main(args)
Creates a blockchain from a text file, validates it, and then prompts the user to add
more blocks to the chain until finally writing the instance of blockchain to a new
text file.