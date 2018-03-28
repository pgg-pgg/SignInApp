package pgg.com.signinapp.service.presenter;

import android.content.Context;

import pgg.com.signinapp.common.Constant;
import pgg.com.signinapp.service.domain.Results;
import pgg.com.signinapp.service.domain.User;
import pgg.com.signinapp.service.model.OnLoadDataListener;
import pgg.com.signinapp.service.model.UserModel;
import pgg.com.signinapp.service.view.ILoginView;
import pgg.com.signinapp.util.SPUtils;

/**
 * Created by PDD on 2018/3/24.
 */

public class ILoginPresenter implements OnLoadDataListener<Results<User>> {

    private ILoginView mView;
    private UserModel mModel;
    private Context context;

    public ILoginPresenter(Context context,ILoginView mView) {
        this.mView=mView;
        this.context=context;
        mModel=new UserModel();
    }

    public void login(String id,String password){
        mView.showProgress();
        mModel.loginToServer(this,id,password);
    }


    @Override
    public void onSuccess(Results<User> data) {
        mView.hideProgress();
        mView.showSuccessMsg(data);
        SPUtils.put(context, Constant.STUDENT_ID,data.getData().getId());
        SPUtils.put(context,Constant.NAME,data.getData().getName());
        SPUtils.put(context,Constant.SEX,data.getData().getSex());
        SPUtils.put(context,Constant.PASSWORD,data.getData().getPassword());
        SPUtils.put(context,Constant.HEAD_ICON,data.getData().getHead_icon());
        SPUtils.put(context,Constant.IS_FIRST,false);
        mView.ToMainActivity();
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
        mView.showOnFailMsg();
    }
}
