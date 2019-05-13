package sample;

import java.io.Serializable;

public class TxIn implements Serializable{
    private Outpoint outpoint;
    private int scriptLength;
    private String signatureScript;
    //private int sequence;

    public TxIn(Outpoint outpoint, String signatureScript) {
        this.outpoint = outpoint;
        this.signatureScript = signatureScript;
        scriptLength = signatureScript.length();
    }

    public String getSignatureScript() {
        return signatureScript;
    }

    public Outpoint getOutpoint() {
        return outpoint;
    }
}