package pgg.com.signinapp.service.presenter;

import okhttp3.MultipartBody;
import pgg.com.signinapp.service.domain.Results;
import pgg.com.signinapp.service.domain.User;
import pgg.com.signinapp.service.model.OnLoadDataListener;
import pgg.com.signinapp.service.model.UserModel;
import pgg.com.signinapp.service.view.IRegisterView;

/**
 * Created by PDD on 2018/3/24.
 */

public class IRegisterPresenter implements OnLoadDataListener<Results<User>>{

    private IRegisterView mView;
    private UserModel mModel;

    public IRegisterPresenter(IRegisterView mView){
        this.mView=mView;
        mModel=new UserModel();
    }

    public void registerToServer(User user){
        mView.showProgress();
        mModel.registerToServer(this,user);
    }

    @Override
    public void onSuccess(Results<User> data) {
        mView.hideProgress();
        mView.onShowSuccessMsg(data);
        mView.ToLoginActivity();
        mView.clearEditText();
    }

    @Override
    public void onRequestCodeFail(Results<User> data) {
        mView.hideProgress();
        mView.showOnResponseError(data);
    }

    @Override
    public void onRequestFail(Throwable e) {
        mView.hideProgress();
        mView.onShowFailMsg();
    }
}
