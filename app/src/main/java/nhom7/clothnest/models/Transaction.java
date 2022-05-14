package nhom7.clothnest.models;

public class Transaction {
    public static final String COLLECTION_TRANSACTION = "transactions";
    private String idTransaction;
    private String nameTransaction;
    private double priceTransaction;
    private String  dateTransaction;
    private String stateTransaction;
    private String districtTransaction;
    private String provinceTransaction;
    private String streetnameTransaction;
    private String wardTransaction;

    public String getDistrictTransaction() {
        return districtTransaction;
    }

    public void setDistrictTransaction(String districtTransaction) {
        this.districtTransaction = districtTransaction;
    }

    public String getProvinceTransaction() {
        return provinceTransaction;
    }

    public void setProvinceTransaction(String provinceTransaction) {
        this.provinceTransaction = provinceTransaction;
    }

    public String getStreetnameTransaction() {
        return streetnameTransaction;
    }

    public void setStreetnameTransaction(String streetnameTransaction) {
        this.streetnameTransaction = streetnameTransaction;
    }

    public String getWardTransaction() {
        return wardTransaction;
    }

    public void setWardTransaction(String wardTransaction) {
        this.wardTransaction = wardTransaction;
    }

    public Transaction()
    {

    }

    public Transaction(String idTransaction, String nameTransaction, double priceTransaction, String dateTransaction, String stateTransaction, String districtTransaction, String provinceTransaction, String streetnameTransaction, String wardTransaction) {
        this.idTransaction = idTransaction;
        this.nameTransaction = nameTransaction;
        this.priceTransaction = priceTransaction;
        this.dateTransaction = dateTransaction;
        this.stateTransaction = stateTransaction;
        this.districtTransaction = districtTransaction;
        this.provinceTransaction = provinceTransaction;
        this.streetnameTransaction = streetnameTransaction;
        this.wardTransaction = wardTransaction;
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
