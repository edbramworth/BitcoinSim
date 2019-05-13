package sample;

import java.io.Serializable;

public class TxOut implements Serializable{
    private int value;
    private int pkScriptLength;
    private String pkScript;

    public TxOut(int value, String pkScript){
        this.value = value;
        this.pkScriptLength = pkScript.length();
        this.pkScript = pkScript;
    }

    public int getValue() {
        return value;
    }

    public int getPkScriptLength() {
        return pkScriptLength;
    }

    public String getPkScript() {
        return pkScript;
    }
}