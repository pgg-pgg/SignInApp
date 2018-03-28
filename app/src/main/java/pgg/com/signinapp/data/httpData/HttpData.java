package pgg.com.signinapp.data.httpData;

import java.io.File;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import io.rx_cache.internal.RxCache;
import okhttp3.MultipartBody;
import pgg.com.signinapp.common.Constant;
import pgg.com.signinapp.data.api.CacheProviders;
import pgg.com.signinapp.data.api.FaceService;
import pgg.com.signinapp.data.api.UserService;
import pgg.com.signinapp.service.domain.FaceCompareInfo;
import pgg.com.signinapp.service.domain.Results;
import pgg.com.signinapp.service.domain.User;
import pgg.com.signinapp.util.FileUtils;
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
    private static File cacheDirectory= FileUtils.getChaheDirectory();

    protected UserService userService=getRetrofit(Constant.BASE_URL).create(UserService.class);
    protected FaceService faceService=getRetrofit(Constant.faceApi).create(FaceService.class);

    private static final CacheProviders providers=new RxCache.Builder()
            .persistence(cacheDirectory)
            .using(CacheProviders.class);

    private static class SingletonHolder{
        private static final HttpData INSTANCE=new HttpData();
    }

    public static HttpData getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public void register(Observer<Results<User>> observable, User user){
        Observable<Results<User>> dataResults = userService.register(user);
        setSubscribe(dataResults,observable);
    }
    public void getUserDatas(Observer<Results<User>> observable, String id,String password ){
        Observable<Results<User>> dataResults = userService.login(id,password);
        Observable listObservable = providers.getUserResults(dataResults, new DynamicKey("user"), new EvictDynamicKey(false)).map(new HttpCacheHandler<User>());
        setSubscribe(listObservable,observable);
    }

    public void getCompareFace(Observer<FaceCompareInfo> observable,String face_token,String image_base64_2){
        Observable<FaceCompareInfo> data=faceService.compareFace(Constant.key,Constant.secret,face_token,image_base64_2);
        setSubscribe(data,observable);
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
