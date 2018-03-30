package pgg.com.signinapp.data.httpData;

import android.content.Context;

import java.io.File;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import io.rx_cache.internal.RxCache;
import okhttp3.MultipartBody;
import pgg.com.signinapp.common.Constant;
import pgg.com.signinapp.data.api.CacheProviders;
import pgg.com.signinapp.data.api.FaceService;
import pgg.com.signinapp.data.api.LocationService;
import pgg.com.signinapp.data.api.UserService;
import pgg.com.signinapp.data.retrofit.ApiException;
import pgg.com.signinapp.global.MyApplication;
import pgg.com.signinapp.service.activity.RegisterActivity;
import pgg.com.signinapp.service.domain.AddFaceInfo;
import pgg.com.signinapp.service.domain.FaceCompareInfo;
import pgg.com.signinapp.service.domain.FaceInfo;
import pgg.com.signinapp.service.domain.FaceSetInfo;
import pgg.com.signinapp.service.domain.Location;
import pgg.com.signinapp.service.domain.Results;
import pgg.com.signinapp.service.domain.User;
import pgg.com.signinapp.service.model.OnLoadDataListener;
import pgg.com.signinapp.util.FileUtils;
import pgg.com.signinapp.util.SPUtils;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static pgg.com.signinapp.data.retrofit.RetrofitUtils.getRetrofit;

/**
 * Created by PDD on 2018/3/23.
 */

public class HttpData {
    private static File cacheDirectory = FileUtils.getChaheDirectory();

    protected UserService userService = getRetrofit(Constant.BASE_URL).create(UserService.class);
    protected FaceService faceService = getRetrofit(Constant.faceApi).create(FaceService.class);
    protected LocationService locationService = getRetrofit(Constant.BASE_URL).create(LocationService.class);

    private static final CacheProviders providers = new RxCache.Builder()
            .persistence(cacheDirectory)
            .using(CacheProviders.class);

    private static class SingletonHolder {
        private static final HttpData INSTANCE = new HttpData();
    }

    public static HttpData getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void register(Observer<Results<User>> observable, final User user) {
        Observable<Results<User>> data = userService.register(user);
        setSubscribe(data, observable);
    }

    public void getUserDatas(Observer<Results<User>> observable, String id, String password) {
        Observable<Results<User>> dataResults = userService.login(id, password);
        Observable listObservable = providers.getUserResults(dataResults, new DynamicKey("user"), new EvictDynamicKey(false)).map(new HttpCacheHandler<User>());
        setSubscribe(listObservable, observable);
    }

    public void getCompareFace(Observer<Results<Location>> observable, String face_token, String image_base64_2, final Location location) {
        Observable<Results<Location>> data = faceService.compareFace(Constant.key, Constant.secret, face_token, image_base64_2)
                .flatMap(new Func1<FaceCompareInfo, Observable<Results<Location>>>() {
                    @Override
                    public Observable<Results<Location>> call(FaceCompareInfo info) {
                        if (info.getConfidence() > 70) {
                            return locationService.addLocation(location);
                        }
                        return Observable.error(new ApiException("不是同一个人"));
                    }
                });
        setSubscribe(data, observable);
    }

    public void addFaceToSet(Observer<AddFaceInfo> observer,String image_base64) {
        Observable<AddFaceInfo> data = faceService.detectFaceInfo(Constant.key, Constant.secret, image_base64)
                .flatMap(new Func1<FaceInfo, Observable<AddFaceInfo>>() {
                    @Override
                    public Observable<AddFaceInfo> call(FaceInfo faceInfo) {
                        if (faceInfo.getFaces().get(0).getFace_token() != null && !faceInfo.getFaces().get(0).getFace_token().isEmpty()) {
                            SPUtils.put(MyApplication.getInstance().getApplicationContext(),Constant.FACE_TOKEN,faceInfo.getFaces().get(0).getFace_token());
                            return faceService.addFaceToSet(Constant.key, Constant.secret, Constant.faceset_token, faceInfo.getFaces().get(0).getFace_token());
                        } else {
                            return Observable.error(new ApiException("选取的照片没有人脸"));
                        }

                    }
                });
        setSubscribe(data, observer);
    }

    private static <T> void setSubscribe(Observable<T> listObservable, Observer<T> observable) {
        listObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observable);
    }

    private class HttpCacheHandler<T> implements Func1<Reply<T>, T> {
        @Override
        public T call(Reply<T> tReply) {
            return tReply.getData();
        }
    }

}
