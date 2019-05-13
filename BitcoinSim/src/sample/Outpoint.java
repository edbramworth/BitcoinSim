package sample;

import java.io.Serializable;

public class Outpoint implements Serializable{
    //the hash (ID) of the referenced transaction
    //made this a char array instead of string to enforce size of 32
    private String txHash;

    //the index of the specific output of the referenced transaction
    private int index;

    public Outpoint(Tx txRef, int index){
        this.index = index;

        try{
            this.txHash = txRef.createTxHash();
        } catch(Exception ex){
            System.out.println("A problem occurred while hashing the transaction");
        }
        Main.gdh.hashToTx.put(this.txHash,txRef);
        Main.gdh.txToHash.put(txRef,this.txHash);
    }

    public Tx getTxRef(){
        return Main.gdh.hashToTx.get(txHash);
    }

    public int getIndex() {
        return index;
    }

    public String getHash() {
        return txHash;
    }
}