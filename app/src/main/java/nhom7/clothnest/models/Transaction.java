package nhom7.clothnest.models;

import java.util.Date;

public class Transaction {
    private String idTransaction;
    private String nameTransaction;
    private double priceTransaction;
    private String  dateTransaction;
    private String stateTransaction;

    public Transaction()
    {

    }


    public Transaction(String idTransaction, String nameTransaction, double priceTransaction, String dateTransaction, String stateTransaction) {
        this.idTransaction = idTransaction;
        this.nameTransaction = nameTransaction;
        this.priceTransaction = priceTransaction;
        this.dateTransaction = dateTransaction;
        this.stateTransaction = stateTransaction;
    }


    public String getStateTransaction() {
        return stateTransaction;
    }


    public String getIdTransaction() {
        return idTransaction;
    }


    public String getNameTransaction() {
        return nameTransaction;
    }


    public double getPriceTransaction() {
        return priceTransaction;
    }


    public String getDateTransaction() {
        return dateTransaction;
    }

}
