package nhom7.clothnest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import nhom7.clothnest.interfaces.ActivityConstants;
import nhom7.clothnest.models.Address;
import nhom7.clothnest.models.District;
import nhom7.clothnest.interfaces.OpenProvincesAPI;
import nhom7.clothnest.models.Province;
import nhom7.clothnest.R;
import nhom7.clothnest.util.StringNormalizer;
import nhom7.clothnest.models.Ward;
import nhom7.clothnest.util.customizeComponent.CustomProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddAddressActivity extends AppCompatActivity {
    ImageView btnBack;
    EditText fullName, detail, phoneNumber;
    MaterialButton btnAdd, btnDelete, btnSave;
    MaterialAutoCompleteTextView province, district, ward;
    private ArrayList<Province> provinces;
    private ArrayList<District> districts;
    private ArrayList<Ward> wards;
    private int provinceCode = -1, districtCode = -1;
    Retrofit retrofit;
    OpenProvincesAPI openProvincesAPI;
    CustomProgressBar customProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        // Thiet lap button back
        btnBack = findViewById(R.id.imageview_back_addAddress);
        btnBack.setOnClickListener(btnBackListener);

        // Init view
        initView();

        // Xac dinh hanh vi khi start activity
        determineOnCreateBehavior();

        // Khoi tao de call API
        initForApiCalling();

        // Khoi tao du lieu cho combobox
        initDataForComboboxes();
    }

    private void initDataForComboboxes() {
        customProgressBar.show();
        initDataForProvinceCombobox();
    }

    private void initForApiCalling() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://provinces.open-api.vn/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openProvincesAPI = retrofit.create(OpenProvincesAPI.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void determineOnCreateBehavior() {
        int callingActivity = getIntent().getIntExtra("calling-activity", 0);
        switch (callingActivity) {
            // Khi bam nut add address
            case ActivityConstants.ADD_ADDRESS:
                btnSave.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                btnAdd.setVisibility(View.VISIBLE);

                // Setup add address button
                setupBtnAdd();
                break;

            // Khi bam nut edit address
            case ActivityConstants.EDIT_ADDRESS:
                btnAdd.setVisibility(View.GONE);
                btnDelete.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.VISIBLE);
                setAddressData((Address) getIntent().getExtras().getSerializable("address"));

                // Setup delete and save button
                setupBtnDelete();
                setupBtnSave();
                break;
        }
    }

    private void initDataForProvinceCombobox() {
        // province
        Call<ArrayList<Province>> call = openProvincesAPI.getProvince();
        call.enqueue(new Callback<ArrayList<Province>>() {
            @Override
            public void onResponse(Call<ArrayList<Province>> call, Response<ArrayList<Province>> response) {
                customProgressBar.dismiss();
                if (!response.isSuccessful()) {
                    Log.d("addAddressActivity", "Response <province> is unsuccessful: " + response.code());
                    return;
                }

                provinces = response.body();

                // sort provinces
                Collections.sort(provinces, new Comparator<Province>() {
                    @Override
                    public int compare(Province p1, Province p2) {
                        return p1.getName().compareTo(p2.getName());
                    }
                });

                ArrayList<String> provinceStrings = new ArrayList<>();
                for (Province p : provinces) {
                    p.setName(StringNormalizer.removeAccent(p.getName()));
                    provinceStrings.add(p.getName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.combobox_item_address, provinceStrings);
                province.setAdapter(adapter);

                // Khoi tao du lieu cho district khi start Activity nay voi muc dich la Edit Address
                for (Province p: provinces) {
                    if (province.getText().toString().compareTo(p.getName()) == 0) {
                        provinceCode = p.getCode();
                        customProgressBar.show();
                        initDataForDistrictCombobox();
                        break;
                    }
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Province>> call, Throwable t) {
                customProgressBar.dismiss();
                Log.d("addAddressActivity", "Call API <province> failed: " + t.toString());
            }
        });

        // set on province selection
        province.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                provinceCode = provinces.get(i).getCode();
                district.getText().clear();
                ward.getText().clear();
                customProgressBar.show();
                initDataForDistrictCombobox();
            }
        });
    }

    private void initDataForDistrictCombobox() {
        // Neu chua chon Tinh/Thanh pho thi return
        if (provinceCode == -1) {
            return;
        }

        Call<Province> call = openProvincesAPI.getDistrict(provinceCode);
        call.enqueue(new Callback<Province>() {
            @Override
            public void onResponse(Call<Province> call, Response<Province> response) {
                customProgressBar.dismiss();
                if (!response.isSuccessful()) {
                    Log.d("addAddressActivity", "Response <district> is unsuccessful: " + response.code());
                    return;
                }

                districts = response.body().getDistricts();

                ArrayList<String> districtStrings = new ArrayList<>();
                for (District d : districts) {
                    d.setName(StringNormalizer.removeAccent(d.getName()));
                    districtStrings.add(d.getName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.combobox_item_address, districtStrings);
                district.setAdapter(adapter);

                // Khoi tao du lieu cho ward khi start Activity nay voi muc dich la Edit Address
                for (District d: districts) {
                    if (district.getText().toString().compareTo(d.getName()) == 0) {
                        districtCode = d.getCode();
                        customProgressBar.show();
                        initDataForWardCombobox();
                        break;
                    }
                }

            }

            @Override
            public void onFailure(Call<Province> call, Throwable t) {
                customProgressBar.dismiss();
                Log.d("addAddressActivity", "Call API <district> failed: " + t.toString());
            }
        });

        // set on district selection
        district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                districtCode = districts.get(i).getCode();
                ward.getText().clear();
                customProgressBar.show();
                initDataForWardCombobox();
            }
        });
    }

    private void initDataForWardCombobox() {
        // Neu chua chon Quan/Huyen thi return
        if (districtCode == -1) {
            return;
        }

        Call<District> call = openProvincesAPI.getWard(districtCode);
        call.enqueue(new Callback<District>() {
            @Override
            public void onResponse(Call<District> call, Response<District> response) {
                customProgressBar.dismiss();
                if (!response.isSuccessful()) {
                    Log.d("addAddressActivity", "Response <ward> is unsuccessful: " + response.code());
                    return;
                }

                wards = response.body().getWards();

                ArrayList<String> wardStrings = new ArrayList<>();
                for (Ward w : wards) {
                    w.setName(StringNormalizer.removeAccent(w.getName()));
                    wardStrings.add(w.getName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.combobox_item_address, wardStrings);
                ward.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<District> call, Throwable t) {
                customProgressBar.dismiss();
                Log.d("addAddressActivity", "Call API <ward> failed: " + t.toString());
            }
        });
    }

    private void initView() {
        fullName = findViewById(R.id.edittext_fullname_addAddress);
        detail = findViewById(R.id.edittext_detail_addAddress);
        phoneNumber = findViewById(R.id.edittext_phoneNumber_addAddress);
        btnAdd = findViewById(R.id.button_add_addAddress);
        province = findViewById(R.id.combobox_province_addAddress);
        district = findViewById(R.id.combobox_district_addAddress);
        ward = findViewById(R.id.combobox_ward_addAddress);
        btnDelete = findViewById(R.id.button_delete_addAddress);
        btnSave = findViewById(R.id.button_save_addAddress);
        customProgressBar = new CustomProgressBar(this);
    }

    private String getViewText(EditText view) {
        return view.getText().toString();
    }

    private void setupBtnDelete() {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("action-code", 0);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void setupBtnSave() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("action-code", 1);
                intent.putExtra("new-address", getNewAddress());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private Address getNewAddress() {
        Address address = new Address(getViewText(fullName),
                province.getText().toString(),
                district.getText().toString(),
                ward.getText().toString(),
                getViewText(detail),
                getViewText(phoneNumber));
        return address;
    }

    private void setupBtnAdd() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Address address = new Address(getViewText(fullName),
                        province.getText().toString(),
                        district.getText().toString(),
                        ward.getText().toString(),
                        getViewText(detail),
                        getViewText(phoneNumber));

                // Tra data ve cho AddressBookActivity
                Intent intent = new Intent();
                //intent.putExtra("data", "testData");
                intent.putExtra("data", address);
                setResult(RESULT_OK, intent);
                Log.d("addAddressActivity", "Send data back successfully ");
                finish();
            }
        });
    }

    private View.OnClickListener btnBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AddAddressActivity.super.onBackPressed();
        }
    };

    private void setAddressData(Address address) {
        if (address == null) {
            Log.d("addAddressActivity", "Address received in edit mode is null ");
            return;
        }

        fullName.setText(address.getFullName());
        province.setText(address.getProvince());
        district.setText(address.getDistrict());
        ward.setText(address.getWard());
        detail.setText(address.getDetail());
        detail.setText(address.getPhoneNumber());
    }
}