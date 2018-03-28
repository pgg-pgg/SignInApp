package pgg.com.signinapp.service.view;

import pgg.com.signinapp.service.domain.Results;
import pgg.com.signinapp.service.domain.User;

/**
 * Created by PDD on 2018/3/24.
 */

public interface ILoginView {

    void showProgress();

    void showOnFailMsg();//请求网络失败

    void hideProgress();

    void showSuccessMsg(Results<User> data);//请求网络成功，并且获取到了正确的数据

    void showOnResponseError(Results<User> data);//请求网络成功，但是没有获取正确的数据

    void clearEditText();

    void ToMainActivity();
}
