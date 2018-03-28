package pgg.com.signinapp.service.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Base64;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.megvii.cloud.http.CommonOperate;
import com.megvii.cloud.http.Response;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import pgg.com.signinapp.MainActivity;
import pgg.com.signinapp.R;
import pgg.com.signinapp.common.Constant;
import pgg.com.signinapp.service.base.BaseActivity;
import pgg.com.signinapp.service.domain.Results;
import pgg.com.signinapp.service.domain.User;
import pgg.com.signinapp.service.presenter.IRegisterPresenter;
import pgg.com.signinapp.service.view.IRegisterView;
import pgg.com.signinapp.util.GlideImageLoader;
import pgg.com.signinapp.util.LogUtils;
import pgg.com.signinapp.util.MPermissionUtils;
import pgg.com.signinapp.util.SPUtils;
import pgg.com.signinapp.weiget.JellyInterpolator;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static pgg.com.signinapp.common.Constant.key;
import static pgg.com.signinapp.common.Constant.secret;

/**
 * Created by PDD on 2018/3/23.
 */

public class RegisterActivity extends BaseActivity implements IRegisterView {

    TextView tv_right;
    ImageView iv_back;
    EditText input_id;
    EditText input_name;
    EditText input_password;
    AppCompatButton btn_signup;
    TextView link_login;
    View progress;
    ImageView iv_circle;
    private String student_id;
    private String name;
    private String pwd;
    private IRegisterPresenter presenter;

    private ImagePicker imagePicker;
    ArrayList<ImageItem> images = null;

    private final String TAG = this.getClass().toString();
    private static String base64;
    private static boolean isHasFace=false;


    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
        boolean is_first = (boolean) SPUtils.get(this, Constant.IS_FIRST, true);
        if (!is_first) {
            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
            finish();
        }
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
    }

    @Override
    protected void findViewById() {
        tv_right = (TextView) findViewById(R.id.tv_right);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        input_id = (EditText) findViewById(R.id.input_id);
        input_name = (EditText) findViewById(R.id.input_name);
        input_password = (EditText) findViewById(R.id.input_password);
        btn_signup = (AppCompatButton) findViewById(R.id.btn_signup);
        link_login = (TextView) findViewById(R.id.link_login);
        progress = findViewById(R.id.layout_progress);
        iv_circle = (ImageView) findViewById(R.id.iv_camera);
        iv_back.setVisibility(View.GONE);
    }

    @Override
    protected void setListener() {
        btn_signup.setOnClickListener(this);
        link_login.setOnClickListener(this);
        iv_circle.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        presenter = new IRegisterPresenter(this);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    public void showProgress() {
        iv_circle.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        progressAnimator(progress);
    }

    @Override
    public void hideProgress() {
        iv_circle.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onShowFailMsg() {
        Toast.makeText(RegisterActivity.this, "连接服务器失败，请检查网络...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ToLoginActivity() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void showOnResponseError(Results<User> data) {
        Toast.makeText(RegisterActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowSuccessMsg(Results<User> results) {
        Toast.makeText(RegisterActivity.this, results.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void clearEditText() {
        input_id.setText("");
        input_name.setText("");
        input_password.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signup:
                student_id = input_id.getText().toString();
                name = input_name.getText().toString();
                pwd = input_password.getText().toString();
                if (base64 !=null&&isHasFace){
                    User user = new User();
                    user.setHead_icon(base64);
                    user.setId(student_id);
                    user.setName(name);
                    user.setPassword(pwd);
                    user.setSex(0);
                    presenter.registerToServer(user);
                }else {
                    Toast.makeText(RegisterActivity.this, "请上传你的头像", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.link_login:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.iv_camera:
                ChooseHeaderMethod();
                break;
        }
    }

    private void ChooseHeaderMethod() {
        MPermissionUtils.requestPermissionsResult(this, 1, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new MPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                imagePicker.setMultiMode(false);
                imagePicker.setStyle(CropImageView.Style.RECTANGLE);
                Integer width = Integer.valueOf(400);
                Integer height = Integer.valueOf(400);
                width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
                height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
                imagePicker.setFocusWidth(width);
                imagePicker.setFocusHeight(height);
                imagePicker.setOutPutX(Integer.valueOf(128));
                imagePicker.setOutPutY(Integer.valueOf(128));
                Intent intent = new Intent(RegisterActivity.this, ImageGridActivity.class);
                intent.putExtra(ImageGridActivity.EXTRAS_IMAGES,images);
                startActivityForResult(intent, 100);
            }

            @Override
            public void onPermissionDenied() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                File file=new File(images.get(0).path);
                if (file.exists()){
                    BitmapFactory.Options options=new BitmapFactory.Options();
                    options.inSampleSize=4;
                    final Bitmap mBitmap =BitmapFactory.decodeFile(images.get(0).path,options);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] bytes = baos.toByteArray();
                    base64 = new String(Base64.encode(bytes, Base64.DEFAULT));
                    Observable.create(new Observable.OnSubscribe<String>() {
                        @Override
                        public void call(Subscriber<? super String> subscriber) {
                            CommonOperate commonOperate = new CommonOperate(key, secret, false);
                            //detect first face by local file use base64
                            Response response3 = null;
                            try {
                                response3 = commonOperate.detectBase64(base64, 0, null);
                                String faceToken = getFaceToken(response3);
                                subscriber.onStart();
                                subscriber.onNext(faceToken);
                                subscriber.onCompleted();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<String>() {
                                @Override
                                public void onStart() {
                                    progress.setVisibility(View.VISIBLE);
                                    iv_circle.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCompleted() {
                                    progress.setVisibility(View.GONE);
                                    iv_circle.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onNext(String sa) {
                                    if (sa!=null&&!sa.isEmpty()&&!sa.equals("")){
                                        isHasFace =true;
                                        iv_circle.setImageBitmap(mBitmap);
                                        SPUtils.put(RegisterActivity.this,Constant.FACE_TOKEN,sa);
                                    }else {
                                        isHasFace=false;
                                        iv_circle.setImageResource(R.drawable.camera);
                                        Toast.makeText(RegisterActivity.this,"选取的照片没有检测出人脸照，请重新选取",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
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

    private String getFaceToken(Response response) throws JSONException {
        if(response.getStatus() != 200){
            return null;
        }
        String res = new String(response.getContent());
        LogUtils.e("response",res);
        JSONObject json = new JSONObject(res);
        String s = json.optJSONArray("faces").optJSONObject(0).optString("face_token");
        return s;
    }
}
