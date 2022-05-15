package nhom7.clothnest.notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationAPI {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAA2XnsEwk:APA91bHXZJiVYuvpqO2JWpaMBh_wMNPIPNBs6dwjc_8PKAozATeeIUz2SL_sIqplqJV48PV9lmNb5OGCkdoMDNoLokYetH6NdqIvYNomjaSCu1rJwoiUTNrJlF-ttFRvxHqynpJmJqiC"
    })
    @POST("fcm/send")
    Call<MyResponse> sendNotification(
            @Body Sender body
    );
}
