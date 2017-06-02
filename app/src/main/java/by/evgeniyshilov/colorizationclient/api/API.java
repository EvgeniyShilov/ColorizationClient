package by.evgeniyshilov.colorizationclient.api;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public final class API {

    private static final String SERVER_ADDRESS = "http://192.168.0.104:8080/";

    private static ServerInterface server;

    private API() {

    }

    public static ServerInterface getInterface() {
        if (server == null) server = new Retrofit.Builder()
                .baseUrl(SERVER_ADDRESS)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(ServerInterface.class);
        return server;
    }
}
