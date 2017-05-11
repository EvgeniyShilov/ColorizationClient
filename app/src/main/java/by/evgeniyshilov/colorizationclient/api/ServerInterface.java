package by.evgeniyshilov.colorizationclient.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ServerInterface {

    @POST("load")
    Call<String> loadImage(@Header("token") String token, @Header("id") String id, @Body String base64);

    @GET("get")
    Call<String> getImage(@Header("id") String id);
}
