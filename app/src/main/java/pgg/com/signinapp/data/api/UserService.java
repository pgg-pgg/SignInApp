package pgg.com.signinapp.data.api;

import pgg.com.signinapp.service.domain.FaceCompareInfo;
import pgg.com.signinapp.service.domain.Results;
import pgg.com.signinapp.service.domain.User;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by PDD on 2018/3/23.
 */

public interface UserService {

    @POST("User/register")
    Observable<Results<User>> register(@Body User user);


//    @POST("User/register")
//    Observable<Results<User>> register(@Field("id") String id, @Field("name")String name, @Field("password") String password, @Field("sex") int sex, @Field("head_icon") String head_icon);

    @GET("User/login")
    Observable<Results<User>> login(@Query("id")String id, @Query("password") String password);


}
