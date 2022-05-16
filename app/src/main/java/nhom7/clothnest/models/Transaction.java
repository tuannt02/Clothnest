package nhom7.clothnest.models;

public class Transaction {
    public static final String COLLECTION_TRANSACTION = "transactions";
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

    public void setIdTransaction(String idTransaction) {
        this.idTransaction = idTransaction;
    }

    public void setNameTransaction(String nameTransaction) {
        this.nameTransaction = nameTransaction;
    }

    public void setPriceTransaction(double priceTransaction) {
        this.priceTransaction = priceTransaction;
    }

    public void setDateTransaction(String dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public void setStateTransaction(String stateTransaction) {
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


    public Double getPriceTransaction() {
        return priceTransaction;
    }


    public String getDateTransaction() {
        return dateTransaction;
    }

}
