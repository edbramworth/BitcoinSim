package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalDataHandler {
    //list of transactions
    public static List<Tx> transactions;
    //maps private keys to public keys for unlocking UTXOs
    public static Map<String, String> keyPairs;
    //list of users
    public static List<User> users;
    //maps hash values to transactions
    public static Map<String, Tx> hashToTx;
    //maps transactions to hash values
    public static Map<Tx, String> txToHash;

    public GlobalDataHandler(){
        transactions = new ArrayList<>();
        keyPairs = new HashMap<>();
        users = new ArrayList<>();
        hashToTx = new HashMap<>();
        txToHash = new HashMap<>();
    }

    public static User findUserByName(String name){
        for(User user:users){
            if(user.getName().equals(name)){
                return user;
            }
        }
        return null;
    }

    public static boolean checkForDuplicateName(String name){
        for(User user:users){
            if(name.equals(user.getName())){
                return true;
            }
        }
        return false;
    }
}
