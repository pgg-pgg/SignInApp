package pgg.com.signinapp.service.view;

import pgg.com.signinapp.service.domain.Location;
import pgg.com.signinapp.service.domain.Results;

/**
 * Created by PDD on 2018/3/28.
 */

public interface ISignInView {

    void showProgress();

    void showOnFailMsg();//请求网络失败

    void hideProgress();

    void showSuccessMsg(Results<Location> data);//请求网络成功，并且获取到了正确的数据

    void showOnResponseError(Results<Location> data);//请求网络成功，但是没有获取正确的数据
}
