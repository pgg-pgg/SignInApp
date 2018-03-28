package pgg.com.signinapp.data.api;

import pgg.com.signinapp.service.domain.FaceCompareInfo;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by PDD on 2018/3/28.
 */

public interface FaceService {

    @FormUrlEncoded
    @POST("compare")
    Observable<FaceCompareInfo> compareFace(@Field("api_key") String api_key, @Field("api_secret") String api_secret, @Field("face_token1") String face_token1, @Field("image_base64_2") String image_base64_2);

}