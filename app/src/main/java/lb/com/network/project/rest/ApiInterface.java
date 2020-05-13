package lb.com.network.project.rest;

import lb.com.network.project.model.ApiBaseResponse;
import lb.com.network.project.model.ContactDistance;
import lb.com.network.project.model.State;
import lb.com.network.project.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("user/create.php")
    Call<ApiBaseResponse> createUser(@Body User user);

    @POST("user/updatestate.php")
    Call<ApiBaseResponse> updateState(@Body State state);

    @POST("distancetable/create.php")
    Call<ApiBaseResponse> createContactDistance(@Body ContactDistance user);

}
