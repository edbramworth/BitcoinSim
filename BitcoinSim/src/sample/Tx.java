package sample;

import java.io.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class Tx implements Serializable{
    private int version;
    private int inputCount;
    private List<TxIn> input;
    private int outputCount;
    private List<TxOut> output;
    private int lockTime;

    //transaction constructor
    //input count will be 0 if it's a coinbase tx and the input list will be empty
    public Tx(int version, int inputCount, List<TxIn> input, int outputCount, List<TxOut> output, int lockTime){
        this.version = version;
        this.inputCount = inputCount;
        this.input = input;
        this.outputCount = outputCount;
        this.output = output;
        this.lockTime = lockTime;
        Main.gdh.transactions.add(this);
    }

    public static Tx createCoinbaseTx(User receiver, int amount){
        List<TxIn> inputs = new ArrayList<>();
        List<TxOut> outputs = new ArrayList<>();
        outputs.add(new TxOut(amount, receiver.getPublicKey()));

        //create transaction
        Tx transaction = new Tx(1, 0, inputs, 1, outputs, 1);
        //adding the UTXO to the user's list of UTXOs as it belongs to them
        receiver.addUtxo(transaction, 0);

        return transaction;
    }

    //take a TxIn containing data which should satisfy unlocking conditions
    //returns -1 if transaction couldn't be unlocked
    //returns any other number to indicate the amount unlocked
    public int unlockOutput(TxIn input){
        String pkScript = output.get(input.getOutpoint().getIndex()).getPkScript();
        String signatureScript = input.getSignatureScript();

        if(Main.gdh.keyPairs.get(signatureScript).equals(pkScript)){
            return output.get(input.getOutpoint().getIndex()).getValue();
        }
        return 0;
    }

    public List<TxOut> getOutput() {
        return output;
    }

    public String getSenderName(){
        if(input.isEmpty()){
            return "COINBASE";
        }else{
            return User.getNameByPrivKey(input.get(0).getSignatureScript());
        }
    }

    public String getReceiverName(){
        return User.getNameByPubKey(output.get(0).getPkScript());
    }

    public int getAmountSent(){
        int total = 0;
        for(TxOut currOutput:output){
            total += currOutput.getValue();
        }
        return total;
    }

    public int getAmountReceived(){
        return output.get(0).getValue();
    }

    public int getChangeAmount(){
        if(outputCount == 1){
            return 0;
        }
        return output.get(1).getValue();
    }

    public String createTxHash() throws Exception{
        String hash;
        byte[] arr;

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(this);
            arr = bos.toByteArray();

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            arr = digest.digest(digest.digest(arr));
            hash = bytesToHex(arr);
        } catch(Exception e){
            throw new Exception(e);
        }

        return hash;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return String.valueOf(hexChars);
    }
}