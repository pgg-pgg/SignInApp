package pgg.com.signinapp.service.model;

import okhttp3.MultipartBody;
import pgg.com.signinapp.data.httpData.HttpData;
import pgg.com.signinapp.data.retrofit.ApiException;
import pgg.com.signinapp.service.domain.Results;
import pgg.com.signinapp.service.domain.User;
import rx.Observer;

/**
 * Created by PDD on 2018/3/24.
 */

public class UserModel {

    public void registerToServer(final OnLoadDataListener listener,User user) {
        HttpData.getInstance().register(new Observer<Results<User>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onRequestFail(e);
            }

            @Override
            public void onNext(Results<User> results) {
                if (results.getCode()==0){
                    listener.onSuccess(results);
                }else {
                    listener.onRequestCodeFail(results);
                }
            }
        }, user);
    }

    public void loginToServer(final OnLoadDataListener listener, String id,String password){
        HttpData.getInstance().getUserDatas(new Observer<Results<User>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onRequestFail(e);
            }

            @Override
            public void onNext(Results<User> results) {
                if (results.getCode()==0){
                    listener.onSuccess(results);
                }else {
                    listener.onRequestCodeFail(results);
                }

            }
        },id,password);
    }
}
