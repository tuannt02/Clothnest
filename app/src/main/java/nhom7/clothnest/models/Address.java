package nhom7.clothnest.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class Address implements Serializable {
    private String province, district, ward;

    @Exclude
    public String addressId;

    @PropertyName("name")
    public String fullName;

    @PropertyName("street_name")
    public String detail;

    @PropertyName("phone_num")
    public String phoneNumber;

    public Address() {
    }

    public Address(String fullName, String province, String district, String ward, String detail, String phoneNumber) {
        this.fullName = fullName;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.detail = detail;
        this.phoneNumber = phoneNumber;
    }

    public String getProvince() {
        return province;
    }

    public String getDistrict() {
        return district;
    }

    public String getWard() {
        return ward;
    }
}
