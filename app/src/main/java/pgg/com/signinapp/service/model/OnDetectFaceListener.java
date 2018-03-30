package pgg.com.signinapp.service.model;

/**
 * Created by PDD on 2018/3/24.
 */

public interface OnDetectFaceListener<T> {

    void onDetectSuccess(T data);
    void onDetectRequestFail(Throwable e);
}
