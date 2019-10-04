package io.github.namakurio.pemilos.connection;

import io.github.namakurio.pemilos.connection.callbacks.CallbackGetCandidate;
import io.github.namakurio.pemilos.connection.callbacks.CallbackLogin;
import io.github.namakurio.pemilos.data.Constant;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface API {

    String CACHE = "Cache-Control: max-age=0";
    String AGENT = "User-Agent: Pemilos";
    String ACCEPT = "Accept: application/json";
    String BEARER = "Authorization: Bearer " + Constant.SECURITY_CODE;

    /* Login API ----------------------------------------------------------- */
    @Headers({CACHE, AGENT, ACCEPT})
    @FormUrlEncoded
    @POST("auth/login")
    Call<CallbackLogin> login(
            @Field("code") String code,
            @Field("password") String password
            );

    /* Get Candidate API -------------------------------------------------- */
    @Headers({CACHE, AGENT, ACCEPT, BEARER})
    @FormUrlEncoded
    @GET("candidate/getCandidate")
    Call<CallbackGetCandidate> getCandidate();

}
