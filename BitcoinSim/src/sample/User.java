package sample;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String publicKey;
    private String privateKey;
    private List<TxIn> unspentOutputs;

    //generates the public and private keys for each user
    private static String generateKey() {
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder builder = new StringBuilder();
        int charNum = 10;

        while (charNum-- != 0) {
            int character = (int)(Math.random()*charSet.length());
            builder.append(charSet.charAt(character));
        }
        return builder.toString();
    }

    //returns a sub-list of transaction inputs to add up to the total
    private List<TxIn> getSpendableInputs(int requiredAmount){
        int total = 0;
        int count = 0;
        List<TxIn> spendableTxIns = new ArrayList<>();

        while(total < requiredAmount && count < unspentOutputs.size()){
            TxIn currOutput = unspentOutputs.get(count);
            int transactionOutIndex = currOutput.getOutpoint().getIndex();
            total += currOutput.getOutpoint().getTxRef().getOutput().get(transactionOutIndex).getValue();
            spendableTxIns.add(currOutput);
            count ++;
        }

        if(total < requiredAmount){
            System.out.println("Sender does not have enough funds");
            return null;
        } else{
            return spendableTxIns;
        }
    }

    public User(String name){
        this.name = name;
        this.publicKey = generateKey();
        this.privateKey = generateKey();
        this.unspentOutputs = new ArrayList<>();
        Main.gdh.keyPairs.put(privateKey,publicKey);
        Main.gdh.users.add(this);
    }

    public Tx send(User receiver, int amount){
        List<TxIn> inputs = getSpendableInputs(amount);
        int fullAmount = 0;
        if(inputs == null){
            return null;
        }

        for(TxIn input: inputs){
            int outputValue = input.getOutpoint().getTxRef().unlockOutput(input);
            if(outputValue == -1){
                System.out.println("One of the transactions could not be unlocked");
                return null;
            }
            fullAmount += outputValue;
        }
        //removing the UTXOs that are being spent
        unspentOutputs.removeAll(inputs);

        List<TxOut> outputs = new ArrayList<>();
        outputs.add(new TxOut(amount, receiver.publicKey));
        if(fullAmount-amount > 0){
            outputs.add(new TxOut(fullAmount-amount, publicKey));
        }

        Tx transaction = new Tx(1, inputs.size(), inputs, outputs.size(), outputs, 1);
        //Adding the UTXOs to their wallets
        receiver.unspentOutputs.add(new TxIn(new Outpoint(transaction, 0), receiver.privateKey));
        unspentOutputs.add(new TxIn(new Outpoint(transaction, 1), privateKey));

        return transaction;
    }

    public void addUtxo(Tx txRef, int outputIndex){
        unspentOutputs.add(new TxIn(new Outpoint(txRef, outputIndex), privateKey));
    }

    public String getName() {
        return name;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public List<TxIn> getUnspentOutputs(){
        return unspentOutputs;
    }

    public static String getNameByPrivKey(String privKey){
        for(User user:Main.gdh.users){
            if(user.privateKey.equals(privKey)){
                return user.name;
            }
        }
        return null;
    }

    public static String getNameByPubKey(String pubKey){
        for(User user:Main.gdh.users){
            if(user.publicKey.equals(pubKey)){
                return user.name;
            }
        }
        return null;
    }

    public int getTotalFunds(){
        int total = 0;
        for(TxIn output:unspentOutputs){
            int transactionOutIndex = output.getOutpoint().getIndex();
            total += output.getOutpoint().getTxRef().getOutput().get(transactionOutIndex).getValue();
        }
        return total;
    }
}