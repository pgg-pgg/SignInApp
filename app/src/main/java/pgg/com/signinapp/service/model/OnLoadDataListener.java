package pgg.com.signinapp.service.model;

/**
 * Created by PDD on 2018/3/24.
 */

public interface OnLoadDataListener<T> {

    void onSuccess(T data);
    void onRequestFail(Throwable e);
    void onRequestCodeFail(T data);


}
