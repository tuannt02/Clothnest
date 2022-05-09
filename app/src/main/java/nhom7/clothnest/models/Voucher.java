package nhom7.clothnest.models;

public class Voucher {

    public static final String COLLECTION_NAME = "vouchers";

    private String code;
    private int discount;

    public Voucher() {
    }

    public Voucher(String code, int discount) {
        this.code = code;
        this.discount = discount;
    }

    public String getCode() {
        return code;
    }

    public int getDiscount() {
        return discount;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Voucher{" +
                "code='" + code + '\'' +
                ", discount=" + discount +
                '}';
    }
}
