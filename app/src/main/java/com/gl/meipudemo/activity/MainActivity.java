package com.gl.meipudemo.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gl.meipudemo.R;
import com.gl.meipudemo.adapter.MyAdapter;
import com.gl.meipudemo.info.ListEntity;
import com.gl.meipudemo.utils.PopupMenuUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView ivImg;

    private RelativeLayout rlClick;
    private Context context;

    private RecyclerView recyclerView;

    MyAdapter adapter;
    List<ListEntity> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initListData();
    }

    private void initViews() {
        context = this;
        ivImg = (ImageView) findViewById(R.id.iv_img);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        rlClick = (RelativeLayout) findViewById(R.id.rl_click);
        rlClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取屏幕宽度
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int screenWidth = dm.widthPixels;

                PopupMenuUtil.getInstance()._show(context, ivImg, screenWidth);
            }
        });
    }

    private void initListData() {
        data = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        data.add(new ListEntity());

        for(int i=0;i<20;i++){
            ListEntity listEntity=new ListEntity();
            listEntity.name="八千米海岸";
            listEntity.date="10月25日12:25";
            listEntity.content="追求随性的路上还不够洒脱。记得所有的好，感谢所遇到的一切";

            listEntity.avatarUrl="http://img1.imgtn.bdimg.com/it/u=1382558918,1988337407&fm=23&gp=0.jpg";
            listEntity.descUrl="http://pic.xitek.com/album/004008005/7/52245/_141549491.jpg";

            listEntity.layoutType=1;
            data.add(listEntity);
        }
        setAdapter();
    }

    public void setAdapter() {
        if (adapter == null) {
            adapter = new MyAdapter(context, data);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        // 当popupWindow 正在展示的时候 按下返回键 关闭popupWindow 否则关闭activity
        if (PopupMenuUtil.getInstance()._isShowing()) {
            PopupMenuUtil.getInstance()._rlClickAction();
        } else {
            super.onBackPressed();
        }
    }

    public void animRun(final View view1, final View view2, final View view3) {
        //获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int screenWidth = dm.widthPixels;

        //左边控件的动画
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view1, "translationY",
                0.0F, -screenWidth / 3f).setDuration(300);//Y方向移动距离
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view1, "translationX",
                0.0F, -screenWidth / 4f).setDuration(300);//X方向移动距离
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(view1, "scaleX", 0.8f, 1.5f).setDuration(300);//X方向放大
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(view1, "scaleY", 0.8f, 1.5f).setDuration(300);//Y方向放大
        AnimatorSet animSet1 = new AnimatorSet();
        animSet1.setInterpolator(new OvershootInterpolator());//到达指定位置后继续向前移动一定的距离然后弹回指定位置,达到颤动的特效
        animSet1.playTogether(animator1, animator2, animator3, animator4);//四个动画同时执行

        //中间控件的动画,因需要设置监听所以与
        final ObjectAnimator animator5 = ObjectAnimator.ofFloat(view2, "translationY",
                0.0F, -screenWidth / 3f).setDuration(300);
        ObjectAnimator animator6 = ObjectAnimator.ofFloat(view2, "scaleX", 0.8f, 1.5f).setDuration(300);
        ObjectAnimator animator7 = ObjectAnimator.ofFloat(view2, "scaleY", 0.8f, 1.5f).setDuration(300);
        final AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.setInterpolator(new OvershootInterpolator());
        animatorSet2.playTogether(animator5, animator6, animator7);
        animatorSet2.setStartDelay(100);//监听第一个动画开始之后50ms开启第二个动画,达到相继弹出的效果

        //右侧控件的动画
        ObjectAnimator animator8 = ObjectAnimator.ofFloat(view3, "translationY",
                0.0F, -screenWidth / 3f).setDuration(300);
        ObjectAnimator animator9 = ObjectAnimator.ofFloat(view3,
                "translationX",
                0.0F, screenWidth / 4f).setDuration(300);
        ObjectAnimator animator10 = ObjectAnimator.ofFloat(view3, "scaleX", 0.8f, 1.5f).setDuration(300);
        ObjectAnimator animator11 = ObjectAnimator.ofFloat(view3, "scaleY", 0.8f, 1.5f).setDuration(300);
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

    //收回动画,相当于反向执行展开动画,此处不做更详细的注释
    public void animRunBack(final View view1, final View view2, final View view3) {
        //获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int screenWidth = dm.widthPixels;

        //第一个收回动画
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view1, "translationY",
                -screenWidth / 3f, 0.0F).setDuration(300);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view1, "translationX",
                -screenWidth / 4f, 0.0F).setDuration(300);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(view1, "scaleX", 1.5f, 0.8f).setDuration(300);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(view1, "scaleY", 1.5f, 0.8f).setDuration(300);
        final AnimatorSet animSet = new AnimatorSet();
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.playTogether(animator1, animator2, animator3, animator4);
        animSet.setStartDelay(200);

        //第二个收回动画
        final ObjectAnimator animator5 = ObjectAnimator.ofFloat(view2, "translationY",
                -screenWidth / 3f, 0.0F).setDuration(300);
        ObjectAnimator animator6 = ObjectAnimator.ofFloat(view2, "scaleX", 1.5f, 0.8f).setDuration(300);
        ObjectAnimator animator7 = ObjectAnimator.ofFloat(view2, "scaleY", 1.5f, 0.8f).setDuration(300);
        animator3.setInterpolator(new OvershootInterpolator());
        final AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.setInterpolator(new DecelerateInterpolator());
        animatorSet2.playTogether(animator5, animator6, animator7);
        animatorSet2.setStartDelay(100);

        //第三个收回动画
        ObjectAnimator animator8 = ObjectAnimator.ofFloat(view3, "translationY",
                -screenWidth / 3, 0.0F).setDuration(300);
        ObjectAnimator animator9 = ObjectAnimator.ofFloat(view3, "translationX",
                screenWidth / 4f, 0.0F).setDuration(300);
        ObjectAnimator animator10 = ObjectAnimator.ofFloat(view3, "scaleX", 1.5f, 0.8f).setDuration(300);
        ObjectAnimator animator11 = ObjectAnimator.ofFloat(view3, "scaleY", 1.5f, 0.8f).setDuration(300);
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
    }
}
