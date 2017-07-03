package com.gl.meipudemo.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gl.meipudemo.R;

public class PopupMenuUtil {

    public static PopupMenuUtil getInstance() {
        return MenuUtilHolder.INSTANCE;
    }

    private static class MenuUtilHolder {
        public static PopupMenuUtil INSTANCE = new PopupMenuUtil();
    }

    private View rootVew;
    private PopupWindow popupWindow;

    private RelativeLayout rlClick;
    private ImageView ivBtn;
    private LinearLayout llTest1, llTest2, llTest3;

    private int screenWidth;// 屏幕宽度

    /**
     * 创建 popupWindow 内容
     *
     * @param context context
     */
    private void _createView(final Context context) {
        rootVew = LayoutInflater.from(context).inflate(R.layout.popup_menu, null);
        popupWindow = new PopupWindow(rootVew,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        //设置为失去焦点 方便监听返回键的监听
        popupWindow.setFocusable(false);

        // 如果想要popupWindow 遮挡住状态栏可以加上这句代码
        //popupWindow.setClippingEnabled(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);

        initLayout(context);
    }

    /**
     * dp转化为px
     *
     * @param context  context
     * @param dipValue dp value
     * @return 转换之后的px值
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 初始化 view
     */
    private void initLayout(Context context) {
        rlClick = (RelativeLayout) rootVew.findViewById(R.id.pop_rl_click);
        ivBtn = (ImageView) rootVew.findViewById(R.id.pop_iv_img);
        llTest1 = (LinearLayout) rootVew.findViewById(R.id.a_one);
        llTest2 = (LinearLayout) rootVew.findViewById(R.id.a_two);
        llTest3 = (LinearLayout) rootVew.findViewById(R.id.a_three);

        rlClick.setOnClickListener(new MViewClick(0, context));

        llTest1.setOnClickListener(new MViewClick(1, context));
        llTest2.setOnClickListener(new MViewClick(2, context));
        llTest3.setOnClickListener(new MViewClick(3, context));

    }

    /**
     * 点击事件
     */
    private class MViewClick implements View.OnClickListener {

        public int index;
        public Context context;

        public MViewClick(int index, Context context) {
            this.index = index;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            if (index == 0) {
                //加号按钮点击之后的执行
                _rlClickAction();
            } else {
                showToast(context, "index=" + index);
            }
        }
    }

    Toast toast = null;

    /**
     * 防止toast 多次被创建
     *
     * @param context context
     * @param str     str
     */
    private void showToast(Context context, String str) {
        if (toast == null) {
            toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        } else {
            toast.setText(str);
        }
        toast.show();
    }

    /**
     * 刚打开popupWindow 执行的动画
     */
    private void _openPopupWindowAction() {

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivBtn, "rotation", 0f, 135f);
        objectAnimator.setDuration(200);
        objectAnimator.start();

        //左边控件的动画
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(llTest1, "translationY",
                0.0F, -screenWidth / 3f).setDuration(300);//Y方向移动距离
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(llTest1, "translationX",
                0.0F, -screenWidth / 4f).setDuration(300);//X方向移动距离
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(llTest1, "scaleX", 1f, 3f).setDuration(300);//X方向放大
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(llTest1, "scaleY", 1f, 3f).setDuration(300);//Y方向放大
        AnimatorSet animSet1 = new AnimatorSet();
        animSet1.setInterpolator(new OvershootInterpolator());//到达指定位置后继续向前移动一定的距离然后弹回指定位置,达到颤动的特效
        animSet1.playTogether(animator1, animator2, animator3, animator4);//四个动画同时执行

        //中间控件的动画,因需要设置监听所以与
        final ObjectAnimator animator5 = ObjectAnimator.ofFloat(llTest2, "translationY",
                0.0F, -screenWidth / 3f).setDuration(300);
        ObjectAnimator animator6 = ObjectAnimator.ofFloat(llTest2, "scaleX", 1f, 3f).setDuration(300);
        ObjectAnimator animator7 = ObjectAnimator.ofFloat(llTest2, "scaleY", 1f, 3f).setDuration(300);
        final AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.setInterpolator(new OvershootInterpolator());
        animatorSet2.playTogether(animator5, animator6, animator7);
        animatorSet2.setStartDelay(100);//监听第一个动画开始之后50ms开启第二个动画,达到相继弹出的效果

        //右侧控件的动画
        ObjectAnimator animator8 = ObjectAnimator.ofFloat(llTest3, "translationY",
                0.0F, -screenWidth / 3f).setDuration(300);
        ObjectAnimator animator9 = ObjectAnimator.ofFloat(llTest3,
                "translationX",
                0.0F, screenWidth / 4f).setDuration(300);
        ObjectAnimator animator10 = ObjectAnimator.ofFloat(llTest3, "scaleX", 1f, 3f).setDuration(300);
        ObjectAnimator animator11 = ObjectAnimator.ofFloat(llTest3, "scaleY", 1f, 3f).setDuration(300);
        final AnimatorSet animatorSet3 = new AnimatorSet();
        animatorSet3.setInterpolator(new OvershootInterpolator());
        animatorSet3.playTogether(animator8, animator9, animator10, animator11);
        animatorSet3.setStartDelay(200);


//        三个动画结束之后设置boss按键可点击,点击即收回动画
        animatorSet3.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
//                a.setClickable(true);
            }
        });

        //第二个开始之后再开启第三个
        animatorSet2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                animatorSet3.start();
            }

        });
//第一个动画开始之后再开启第二个
        animSet1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                animatorSet2.start();
            }
        });

        animSet1.start();//放在最后是为了初始化完毕所有的动画之后才触发第一个控件的动画

    }


    /**
     * 关闭 popupWindow执行的动画
     */
    public void _rlClickAction() {
        if (ivBtn != null && rlClick != null) {

            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivBtn, "rotation", 135f, 0f);
            objectAnimator.setDuration(300);
            objectAnimator.start();

            //第一个收回动画
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(llTest1, "translationY",
                    -screenWidth / 3f, 0.0F).setDuration(300);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(llTest1, "translationX",
                    -screenWidth / 4f, 0.0F).setDuration(300);
            ObjectAnimator animator3 = ObjectAnimator.ofFloat(llTest1, "scaleX", 3f, 1f).setDuration(300);
            ObjectAnimator animator4 = ObjectAnimator.ofFloat(llTest1, "scaleY", 3f, 1f).setDuration(300);
            final AnimatorSet animSet = new AnimatorSet();
            animSet.setInterpolator(new DecelerateInterpolator());
            animSet.playTogether(animator1, animator2, animator3, animator4);
            animSet.setStartDelay(200);

            //第二个收回动画
            final ObjectAnimator animator5 = ObjectAnimator.ofFloat(llTest2, "translationY",
                    -screenWidth / 3f, 0.0F).setDuration(300);
            ObjectAnimator animator6 = ObjectAnimator.ofFloat(llTest2, "scaleX", 3f, 1f).setDuration(300);
            ObjectAnimator animator7 = ObjectAnimator.ofFloat(llTest2, "scaleY", 3f, 1f).setDuration(300);
            animator3.setInterpolator(new OvershootInterpolator());
            final AnimatorSet animatorSet2 = new AnimatorSet();
            animatorSet2.setInterpolator(new DecelerateInterpolator());
            animatorSet2.playTogether(animator5, animator6, animator7);
            animatorSet2.setStartDelay(80);

            //第三个收回动画
            ObjectAnimator animator8 = ObjectAnimator.ofFloat(llTest3, "translationY",
                    -screenWidth / 3, 0.0F).setDuration(300);
            ObjectAnimator animator9 = ObjectAnimator.ofFloat(llTest3, "translationX",
                    screenWidth / 4f, 0.0F).setDuration(300);
            ObjectAnimator animator10 = ObjectAnimator.ofFloat(llTest3, "scaleX", 3f, 1f).setDuration(300);
            ObjectAnimator animator11 = ObjectAnimator.ofFloat(llTest3, "scaleY", 3f, 1f).setDuration(300);
            final AnimatorSet animatorSet3 = new AnimatorSet();
            animatorSet3.setInterpolator(new DecelerateInterpolator());
            //四个动画同时执行
            animatorSet3.playTogether(animator8, animator9, animator10, animator11);

            animatorSet2.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {

                    animSet.start();
                    animSet.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {

                        }
                    });
                }

            });

            animatorSet3.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    animatorSet2.start();
                }
            });

            animatorSet3.start();

            rlClick.postDelayed(new Runnable() {
                @Override
                public void run() {
                    _close();
                }
            }, 300);

        }
    }


    /**
     * 弹起 popupWindow
     *
     * @param context context
     * @param parent  parent
     */
    public void _show(Context context, View parent, int screenWidth) {
        this.screenWidth = screenWidth;
        _createView(context);
        if (popupWindow != null && !popupWindow.isShowing()) {
            popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, 0, 0);
            _openPopupWindowAction();
        }
    }

    /**
     * 关闭popupWindow
     */

    public void _close() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    /**
     * @return popupWindow 是否显示了
     */
    public boolean _isShowing() {
        if (popupWindow == null) {
            return false;
        } else {
            return popupWindow.isShowing();
        }

    }
}