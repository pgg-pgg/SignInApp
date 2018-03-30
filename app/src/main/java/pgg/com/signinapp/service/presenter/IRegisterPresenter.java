package pgg.com.signinapp.service.presenter;

import okhttp3.MultipartBody;
import pgg.com.signinapp.service.domain.AddFaceInfo;
import pgg.com.signinapp.service.domain.Results;
import pgg.com.signinapp.service.domain.User;
import pgg.com.signinapp.service.model.OnDetectFaceListener;
import pgg.com.signinapp.service.model.OnLoadDataListener;
import pgg.com.signinapp.service.model.UserModel;
import pgg.com.signinapp.service.view.IRegisterView;

/**
 * Created by PDD on 2018/3/24.
 */

public class IRegisterPresenter implements OnLoadDataListener<Results<User>>,OnDetectFaceListener<AddFaceInfo>{

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

    public void addFaceInfo(String image_base64){
        mView.showProgress();
        mModel.addFaceInfo(this,image_base64);
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

    @Override
    public void onDetectSuccess(AddFaceInfo data) {
        mView.hideProgress();
        mView.onShowSuccessMsg(data);
    }

    @Override
    public void onDetectRequestFail(Throwable e) {
        mView.hideProgress();
        mView.onShowFailMsg("选取的照片没有人脸");
    }
}
