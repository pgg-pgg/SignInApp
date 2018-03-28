package pgg.com.signinapp.service.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.umeng.message.PushAgent;

import butterknife.ButterKnife;
import pgg.com.signinapp.global.MyApplication;
import pgg.com.signinapp.util.MPermissionUtils;

/**
 * Created by PDD on 2018/3/23.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    private Context context;
    private ConnectivityManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivityContext();
        initView(savedInstanceState);
        ButterKnife.bind(this);
        initData();
        MyApplication.getInstance().addActivity(this);
        PushAgent.getInstance(context).onAppStart();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        MyApplication.getInstance().finishActivity(this);
    }


    private void initData() {
        findViewById();
        setListener();
        processLogic();
    }




    private void initView(Bundle savedInstanceState) {
        loadViewLayout(savedInstanceState);
    }

    /**
     * 加载页面
     * @param savedInstanceState
     */
    protected abstract void loadViewLayout(Bundle savedInstanceState);

    /**
     * 加载页面元素
     */
    protected abstract void findViewById();

    /**
     * 设置监听器
     */
    protected abstract void setListener();

    /**
     * 与服务端进行交互，处理业务逻辑
     */
    protected abstract void processLogic();

    /**
     * 获取子Activity的context
     * @return
     */
    protected abstract Context getActivityContext();


    /**
     * 弹出Toast
     *
     * @param text
     */
    public void showToast(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 获取屏幕宽度(px)
     *
     * @param
     * @return
     */
    public int getMobileWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        return width;
    }

    /**
     * 获取屏幕高度(px)
     *
     * @param
     * @return
     */
    public int getMobileHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        return height;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /*
    返回版本api
     */
    public boolean SdkApi() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.KITKAT) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkNetworkState() {
        boolean flag = false;
        //得到网络连接信息
        manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
        return flag;
    }

    public String getVersionName() {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1.0";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public Bundle getBundle(Bundle bundle){
        return bundle;
    }
}
