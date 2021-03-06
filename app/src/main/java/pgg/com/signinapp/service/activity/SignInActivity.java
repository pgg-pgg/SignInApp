package pgg.com.signinapp.service.activity;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import butterknife.BindView;
import pgg.com.signinapp.R;
import pgg.com.signinapp.common.Constant;
import pgg.com.signinapp.service.base.BaseActivity;
import pgg.com.signinapp.service.domain.Location;
import pgg.com.signinapp.service.domain.Results;
import pgg.com.signinapp.service.presenter.ISignInPresenter;
import pgg.com.signinapp.service.view.ISignInView;
import pgg.com.signinapp.util.MPermissionUtils;
import pgg.com.signinapp.util.SPUtils;
import pgg.com.signinapp.weiget.CameraViewNew;

/**
 * Created by PDD on 2018/3/28.
 */

public class SignInActivity extends BaseActivity implements ISignInView{
    private static String TAG = "CameraObserver";
    @BindView(R.id.button_capture)
    Button buttonCapture;
    @BindView(R.id.progressBar_camera)
    ProgressBar progressBar_camera;
    private Camera mCamera;
    private CameraViewNew mPreview;
    private ISignInPresenter presenter;
    private static double latitude;
    private static double longitude;


    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_signin);
    }

    @Override
    protected void findViewById() {
        MPermissionUtils.requestPermissionsResult(this, 100, new String[]{Manifest.permission.CAMERA}, new MPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                mCamera = getCameraInstance();
            }

            @Override
            public void onPermissionDenied() {

            }
        });
        // 创建预览类，并与Camera关联，最后添加到界面布局中
        mPreview = new CameraViewNew(this, mCamera);
        RelativeLayout preview = (RelativeLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }

    /** 打开一个Camera */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(1);
        } catch (Exception e) {
            Log.d(TAG, "打开Camera失败失败");
        }
        return c;
    }

    private Camera.PictureCallback myPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            BitmapFactory.Options options = new BitmapFactory.Options();
            try {
                options.inSampleSize = 1;
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] bytes = baos.toByteArray();
                String base64 = new String(Base64.encode(bytes, Base64.DEFAULT));
                Location location=new Location();
                location.setId(SPUtils.get(SignInActivity.this,Constant.STUDENT_ID,"")+"");
                location.setLatitude(latitude+"");
                location.setLongitude(longitude+"");
                presenter.signInToServer(SPUtils.get(SignInActivity.this, Constant.FACE_TOKEN,"")+"",base64,location);
            } catch (Exception e) {
            }
            camera.stopPreview();

        }
    };

    @Override
    protected void setListener() {
        buttonCapture.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        latitude=getIntent().getExtras().getDouble(Constant.LATITUDE);
        longitude=getIntent().getExtras().getDouble(Constant.LONGITUDE);
        presenter = new ISignInPresenter(this);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_capture:
                // TODO Auto-generated method stub
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        // TODO Auto-generated method stub
                        mCamera.takePicture(null, null, myPicture);
                    }
                });
                break;
        }
    }

    @Override
    public void showProgress() {
        progressBar_camera.setVisibility(View.VISIBLE);
        buttonCapture.setVisibility(View.GONE);
    }

    @Override
    public void showOnFailMsg() {
        Toast.makeText(SignInActivity.this,"连接Face++服务器失败，请检查网络...",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgress() {
        progressBar_camera.setVisibility(View.GONE);
        buttonCapture.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSuccessMsg(Results<Location> data) {
        finish();
        Toast.makeText(SignInActivity.this,data.getMessage()+"",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showOnResponseError(Results<Location> data) {
        Toast.makeText(SignInActivity.this,data.getMessage()+"",Toast.LENGTH_SHORT).show();
    }
}
