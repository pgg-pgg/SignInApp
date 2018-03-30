package pgg.com.signinapp.service.presenter;

import pgg.com.signinapp.service.domain.Location;
import pgg.com.signinapp.service.domain.Results;
import pgg.com.signinapp.service.model.FaceCompareModel;
import pgg.com.signinapp.service.model.OnLoadDataListener;
import pgg.com.signinapp.service.view.ISignInView;

/**
 * Created by PDD on 2018/3/28.
 */

public class ISignInPresenter implements OnLoadDataListener<Results<Location>>{

    private FaceCompareModel mModel;
    private ISignInView mView;

    public ISignInPresenter(ISignInView mView) {
        this.mView=mView;
        mModel=new FaceCompareModel();
    }

    public void signInToServer(String face_token, String image_base64_2, Location location){
        mView.showProgress();
        mModel.getCompareFaceInfo(this,face_token,image_base64_2,location);
    }

    @Override
    public void onSuccess(Results<Location> data) {
        mView.hideProgress();
        mView.showSuccessMsg(data);
    }

    @Override
    public void onRequestFail(Throwable e) {
        mView.hideProgress();
        mView.showOnFailMsg();
    }

    @Override
    public void onRequestCodeFail(Results<Location> data) {
        mView.showOnResponseError(data);
    }
}
