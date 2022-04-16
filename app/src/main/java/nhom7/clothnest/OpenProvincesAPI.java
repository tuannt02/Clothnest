package nhom7.clothnest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OpenProvincesAPI {
    @GET("p")
    Call<ArrayList<Province>> getProvince();

    @GET("p/{code}?depth=2")
    Call<Province> getDistrict(
            @Path("code") int code
    );

    @GET("d/{code}?depth=2")
    Call<District> getWard(
            @Path("code") int code
    );
}
