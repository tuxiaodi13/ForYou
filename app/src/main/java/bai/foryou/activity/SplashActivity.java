package bai.foryou.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import bai.foryou.R;
import bai.foryou.util.HttpCallbackListener;
import bai.foryou.util.HttpUtil;
import bai.foryou.util.Utility;

public class SplashActivity extends Activity {
    private ImageView iv_start;

    private Animation mFadeIn;
    private Animation mFadeOut;
    private Animation mFadeInScale;
    private Animation mFadeInScale2;
    //存放数据接口的固定地址
    private static String address="http://bai-foryou.sinacloud.net/address.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        /*//String address1="https://bai-foryou.sinacloud.net/20160806.txt";
        String address1="https://bai-foryou.sinacloud.net/";
        String timStr=Utility.StringData();
        String address=address1+timStr+".txt";*/
        //网络连接正常，则执行从网络获取数据操作
        if (HttpUtil.isNetConnected(this)){
            initData(address);
        }



        iv_start=(ImageView)findViewById(R.id.iv_start);
        iv_start.setImageResource(R.drawable.look_around);
        initAnim();
        setListener();
        iv_start.setAnimation(mFadeIn);

    }

    //访问网络得到初始化数据
    private void initData(String address){
        HttpUtil.setHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response){
                Log.d("SplashActivity",response);
                String dataUrl = response;
                if(dataUrl!=null){
                    //继续访问得到的数据接口
                    HttpUtil.setHttpRequest(dataUrl, new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {

                            Utility.handeleResponse(SplashActivity.this, response);
                            Log.d("Splash", "onCreate");
                            Log.d("Splash", Utility.chiStr);
                            Log.d("Splash", Utility.imgUrl);
                        }
                        @Override
                        public void onFinish(Bitmap bitmap) {

                        }
                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }
            }


            @Override
            public void onFinish(Bitmap bitmap) {

            }

            @Override
            public void onError(Exception e) {

                Log.d("SplashActivity", "网络异常");
            }
        });
    }
    //初始化动画
    private void initAnim(){
        mFadeIn= AnimationUtils.loadAnimation(this,R.anim.guide_welcome_fade_in);
        mFadeOut=AnimationUtils.loadAnimation(this,R.anim.guide_welcome_fade_out);
        mFadeInScale=AnimationUtils.loadAnimation(this,R.anim.guide_welcome_fade_in_scale);
        mFadeInScale2=AnimationUtils.loadAnimation(this,R.anim.guide_welcome_fade_in_scale2);

    }
    //为每个动画设置执行完的事件
    public void setListener(){

        mFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv_start.startAnimation(mFadeInScale);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mFadeInScale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

               iv_start.setAnimation(mFadeInScale2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mFadeInScale2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity();
                //iv_start.setAnimation(mFadeOut);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    private void startActivity(){
        Intent intent=new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);
        //onDestroy();
         finish();//调用finish方法后，系统并没有调用onDestory()方法，当前的Activity被移除了栈,但是并没有移除资源。当重新进入此Activity时，会执行onStart()方法。

    }



}
