package test.freelancer.com.fltest.rest;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Lem on 6/16/2015.
 */
public interface TvgService {

    public final static String ENDPOINT = "http://whatsbeef.net";

    @GET("/wabz/guide.php")
    void listTvPrograms(@Query("start") int start, Callback<ResultModel> cb);


}
