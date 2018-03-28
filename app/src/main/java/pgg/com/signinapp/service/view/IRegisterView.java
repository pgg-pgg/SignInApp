package pgg.com.signinapp.service.view;

import pgg.com.signinapp.service.domain.Results;
import pgg.com.signinapp.service.domain.User;

/**
 * Created by PDD on 2018/3/24.
 */

public interface IRegisterView {
    void showProgress();

    void hideProgress();

    void onShowFailMsg();

    void ToLoginActivity();

    void onShowSuccessMsg(Results<User> results);

    void showOnResponseError(Results<User> data);//请求网络成功，但是没有获取正确的数据

    void clearEditText();

}
