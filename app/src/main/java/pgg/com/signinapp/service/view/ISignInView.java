package pgg.com.signinapp.service.view;

import pgg.com.signinapp.service.domain.FaceCompareInfo;

/**
 * Created by PDD on 2018/3/28.
 */

public interface ISignInView {

    void showProgress();

    void showOnFailMsg();//请求网络失败

    void hideProgress();

    void showSuccessMsg(FaceCompareInfo data);//请求网络成功，并且获取到了正确的数据
}
