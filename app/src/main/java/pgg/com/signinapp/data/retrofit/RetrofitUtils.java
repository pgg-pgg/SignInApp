package pgg.com.signinapp.data.retrofit;

import java.io.File;
import java.util.List;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import io.rx_cache.internal.RxCache;
import okhttp3.OkHttpClient;
import pgg.com.signinapp.common.Constant;
import pgg.com.signinapp.data.api.UserService;
import pgg.com.signinapp.data.httpData.HttpData;
import pgg.com.signinapp.util.FileUtils;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 所有的请求数据的方法集中地
 * 根据MovieService的定义编写合适的方法
 * 其中observable是获取API数据
 * observableCahce获取缓存数据
 * new EvictDynamicKey(false) false使用缓存  true 加载数据不使用缓存
 * Created by PDD on 2018/3/20.
 */

public class RetrofitUtils {


    private static Retrofit retrofit;
    private static OkHttpClient client;


    public static Retrofit getRetrofit(String baseUrl) {

        if (client==null){
            client=OkHttp3Utils.getOkHttpClient();
        }
        retrofit=new Retrofit.Builder().baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}
