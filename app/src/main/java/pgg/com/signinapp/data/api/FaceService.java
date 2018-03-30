package pgg.com.signinapp.data.api;

import pgg.com.signinapp.service.domain.AddFaceInfo;
import pgg.com.signinapp.service.domain.FaceCompareInfo;
import pgg.com.signinapp.service.domain.FaceInfo;
import pgg.com.signinapp.service.domain.FaceSetInfo;
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

    @FormUrlEncoded
    @POST("faceset/create")
    Observable<FaceSetInfo> createFaceSet(@Field("api_key") String api_key, @Field("api_secret") String api_secret, @Field("display_name") String display_name, @Field("outer_id") String outer_id);

    @FormUrlEncoded
    @POST("faceset/addface")
    Observable<AddFaceInfo> addFaceToSet(@Field("api_key") String api_key, @Field("api_secret") String api_secret,@Field("faceset_token") String faceset_token,@Field("face_tokens") String face_tokens);

    @FormUrlEncoded
    @POST("detect")
    Observable<FaceInfo> detectFaceInfo(@Field("api_key") String api_key, @Field("api_secret") String api_secret,@Field("image_base64") String image_base64);
}
