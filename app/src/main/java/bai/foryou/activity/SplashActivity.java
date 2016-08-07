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
    //������ݽӿڵĹ̶���ַ
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
        //����������������ִ�д������ȡ���ݲ���
        if (HttpUtil.isNetConnected(this)){
            initData(address);
        }



        iv_start=(ImageView)findViewById(R.id.iv_start);
        iv_start.setImageResource(R.drawable.look_around);
        initAnim();
        setListener();
        iv_start.setAnimation(mFadeIn);

    }

    //��������õ���ʼ������
    private void initData(String address){
        HttpUtil.setHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response){
                Log.d("SplashActivity",response);
                String dataUrl = response;
                if(dataUrl!=null){
                    //�������ʵõ������ݽӿ�
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

                Log.d("SplashActivity", "�����쳣");
            }
        });
    }
    //��ʼ������
    private void initAnim(){
        mFadeIn= AnimationUtils.loadAnimation(this,R.anim.guide_welcome_fade_in);
        mFadeOut=AnimationUtils.loadAnimation(this,R.anim.guide_welcome_fade_out);
        mFadeInScale=AnimationUtils.loadAnimation(this,R.anim.guide_welcome_fade_in_scale);
        mFadeInScale2=AnimationUtils.loadAnimation(this,R.anim.guide_welcome_fade_in_scale2);

    }
    //Ϊÿ����������ִ������¼�
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
         finish();//����finish������ϵͳ��û�е���onDestory()��������ǰ��Activity���Ƴ���ջ,���ǲ�û���Ƴ���Դ�������½����Activityʱ����ִ��onStart()������

    }



}
