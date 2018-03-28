package pgg.com.signinapp.weiget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import pgg.com.signinapp.R;

/**
 * Created by PDD on 2018/3/23.
 */

public class PullDoorView extends RelativeLayout {

    private Context mcontext;
    private Scroller scroller;
    private ImageView imageView;
    private int CY;
    private int DY;
    private int LDY = 0;
    private int screenWidth = 0;
    private int screenHeigh = 0;
    private boolean closeFlag = false;

    public PullDoorView(Context context) {
        super(context);
        mcontext = context;
        setupView();
    }

    public PullDoorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mcontext = context;
        setupView();
    }

    @SuppressLint("NewApi")
    private void setupView() {

        // 将Interpolator设置成有弹跳效果
        Interpolator polator = new BounceInterpolator();
        scroller = new Scroller(mcontext, polator);

        // 获取屏幕分辨率
        WindowManager windowManager = (WindowManager) (mcontext
                .getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        screenHeigh = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        // 设置成透明背景
        this.setBackgroundColor(Color.argb(0, 0, 0, 0));
        imageView = new ImageView(mcontext);
        imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);// 填充整个屏幕
        imageView.setImageResource(R.drawable.welcome); // 默认背景
        addView(imageView);
    }

    // 设置推动门背景
    public void setBgImage(int id) {
        imageView.setImageResource(id);
    }

    // 设置推动门背景
    public void setBgImage(Drawable drawable) {
        imageView.setImageDrawable(drawable);
    }

    // 设置推动门的动画
    public void startBounceAnim(int startY, int dy, int duration) {
        scroller.startScroll(0, startY, 0, dy, duration);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                LDY = (int) event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                CY = (int) event.getY();
                DY = CY - LDY;
                // 上滑有效
                if (DY < 0) {
                    scrollTo(0, -DY);
                }

                break;
            case MotionEvent.ACTION_UP:
                CY = (int) event.getY();
                DY = CY - LDY;
                if (DY < 0) {

                    if (Math.abs(DY) > screenHeigh / 2) {

                        // 向上滑动超过半个屏幕高度时动画消失
                        startBounceAnim(this.getScrollY(), screenHeigh, 450);
                        closeFlag = true;
                    } else {
                        // 向上滑动未超过半个屏幕高度时开启向下弹动动画
                        startBounceAnim(this.getScrollY(), -this.getScrollY(), 1000);

                    }
                }

                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {

        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            // 更新界面
            postInvalidate();
        } else {
            if (closeFlag) {
                this.setVisibility(View.GONE);
            }
        }
    }
}
