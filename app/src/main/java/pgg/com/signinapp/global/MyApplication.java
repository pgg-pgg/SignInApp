package pgg.com.signinapp.global;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import com.lzy.imagepicker.ImagePicker;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by PDD on 2018/3/23.
 */

public class MyApplication extends Application {

    private static MyApplication instance;

    //记录栈中所有的Activity
    private List<Activity> activities_all = new ArrayList<>();
    //记录需要关闭的Activity
    private List<Activity> activities_close = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        UMConfigure.init(this, "5ab50ac9f29d98548e000046", "SignIn", UMConfigure.DEVICE_TYPE_PHONE, "aa9c5524e11b45c43e5f5fdf7615522c");

        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.e("sssssssssssssssss", deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE); //声音
        mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);//呼吸灯
        mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);//振动

        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(false);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
    }

    /**
     * 获得实例
     *
     * @return
     */
    public static MyApplication getInstance() {
        return instance;
    }

    /**
     * 添加当前Activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activities_all.add(activity);
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            this.activities_all.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 给临时列表添加当前Activity
     *
     * @param activity
     */
    public void addTemActivity(Activity activity) {
        activities_close.add(activity);
    }

    /**
     * 在临时列表中结束指定的Activity
     *
     * @param activity
     */
    public void finishTemActivity(Activity activity) {
        if (activity != null) {
            this.activities_close.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束关闭的Activity列表中的所有Activity
     */
    public void exitActivity() {
        for (Activity activity : activities_close) {
            if (activity != null) {
                activity.finish();
                activity = null;
            }
        }
    }

    /**
     * 退出应用
     */
    public void exitApp() {
        for (Activity activity : activities_all) {
            if (activity != null) {
                activity.finish();
            }
        }
        System.exit(0);
    }

}
