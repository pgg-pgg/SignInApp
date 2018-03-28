package pgg.com.signinapp.service.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;

import butterknife.Bind;
import pgg.com.signinapp.MainActivity;
import pgg.com.signinapp.R;
import pgg.com.signinapp.service.base.BaseActivity;
import pgg.com.signinapp.service.domain.Results;
import pgg.com.signinapp.service.domain.User;
import pgg.com.signinapp.service.presenter.ILoginPresenter;
import pgg.com.signinapp.service.view.ILoginView;
import pgg.com.signinapp.weiget.JellyInterpolator;

/**
 * Created by PDD on 2018/3/23.
 */

public class LoginActivity extends BaseActivity implements ILoginView{

    @Bind(R.id.tv_right)
    TextView tv_right;
    @Bind(R.id.main_btn_login)
    TextView main_btn_login;
    @Bind(R.id.layout_progress)
    View progress;
    @Bind(R.id.input_layout)
    View mInputLayout;
    @Bind(R.id.edit_id)
    EditText edit_id;
    @Bind(R.id.edit_password)
    EditText edit_password;
    @Bind(R.id.input_layout_name)
    LinearLayout mName;
    @Bind(R.id.input_layout_psw)
    LinearLayout mPsw;

    @Bind(R.id.ll_head)
    LinearLayout ll_head;

    private ILoginPresenter presenter;

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void findViewById() {
        tv_right.setText("Sign In");
    }

    @Override
    protected void setListener() {
        main_btn_login.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        presenter=new ILoginPresenter(LoginActivity.this,this);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();

    }

    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
        progressAnimator(progress);
        mInputLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showOnFailMsg() {
        Toast.makeText(LoginActivity.this,"连接服务器失败，请检查网络...",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgress() {
        progress.setVisibility(View.GONE);
        mInputLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSuccessMsg(Results<User> data) {
        Toast.makeText(LoginActivity.this,data.getMessage(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void clearEditText() {
        edit_id.setText("");
        edit_password.setText("");
    }

    @Override
    public void ToMainActivity() {
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public void showOnResponseError(Results<User> data) {
        Toast.makeText(LoginActivity.this,data.getMessage(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        presenter.login(edit_id.getText().toString(),edit_password.getText().toString());
    }
}
