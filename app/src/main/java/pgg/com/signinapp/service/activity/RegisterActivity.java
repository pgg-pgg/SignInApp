package pgg.com.signinapp.service.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Base64;
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
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import pgg.com.signinapp.R;
import pgg.com.signinapp.common.Constant;
import pgg.com.signinapp.service.base.BaseActivity;
import pgg.com.signinapp.service.domain.AddFaceInfo;
import pgg.com.signinapp.service.domain.Results;
import pgg.com.signinapp.service.domain.User;
import pgg.com.signinapp.service.presenter.IRegisterPresenter;
import pgg.com.signinapp.service.view.IRegisterView;
import pgg.com.signinapp.util.GlideImageLoader;
import pgg.com.signinapp.util.MPermissionUtils;
import pgg.com.signinapp.util.SPUtils;
import pgg.com.signinapp.weiget.JellyInterpolator;

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
    private static boolean isHasFace = false;
    private Bitmap mBitmap;
    private int degree;


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
                if (base64 != null && isHasFace) {
                    User user = new User();
                    user.setHead_icon(base64);
                    user.setId(student_id);
                    user.setName(name);
                    user.setPassword(pwd);
                    user.setSex(0);
                    presenter.registerToServer(user);
                } else {
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
                intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, images);
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
                File file = new File(images.get(0).path);
                // 从指定路径下读取图片，并获取其EXIF信息
                ExifInterface exifInterface = null;
                try {
                    exifInterface = new ExifInterface(images.get(0).path);
                    // 获取图片的旋转信息
                    int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            degree = 90;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            degree = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            degree = 270;
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (file.exists()) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize =6;
                    mBitmap = BitmapFactory.decodeFile(images.get(0).path, options);
                    Matrix matrix = new Matrix();
                    matrix.postRotate(degree);
                    mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                    iv_circle.setImageBitmap(mBitmap);
                    byte[] bytes = baos.toByteArray();
                    base64 = new String(Base64.encode(bytes, Base64.DEFAULT));
                    presenter.addFaceInfo(base64);
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

    @Override
    public void onShowSuccessMsg(AddFaceInfo results) {
        iv_circle.setImageBitmap(mBitmap);
        isHasFace = true;
        Toast.makeText(this, "检测成功" + results.getFaceset_token(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowFailMsg(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
