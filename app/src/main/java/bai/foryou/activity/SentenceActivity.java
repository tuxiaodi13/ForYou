package bai.foryou.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import bai.foryou.R;
import bai.foryou.util.HttpCallbackListener;
import bai.foryou.util.HttpUtil;
import bai.foryou.util.HttpUtilForBitmap;
import bai.foryou.util.Utility;

public class SentenceActivity extends Activity{
    private TextView engText;
    private TextView chiText;
    private ImageView sentenceImageView;
    private String imgUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sentence);
        //³õÊ¼»¯¿Ø¼þ
        engText=(TextView)findViewById(R.id.eng_content);
        chiText=(TextView)findViewById(R.id.chi_content);
        sentenceImageView=(ImageView)findViewById(R.id.sentence_img);
        showText();

        showPicture();
    }

    private void showText(){
        Log.d("SentenceActivity","showText");
        Log.d("SentenceActivity",Utility.engStr);
                        engText.setText(Utility.engStr);
                        chiText.setText(Utility.chiStr);


    }

    private void showPicture(){


        imgUrl=Utility.imgUrl;


        HttpUtilForBitmap.setHttpRequest(imgUrl, new HttpCallbackListener() {
            @Override
            public void onFinish(final Bitmap bitmap) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sentenceImageView.setImageBitmap(bitmap);

                    }
                });
            }
            @Override
            public void onFinish(String response) {

            }

            @Override
            public void onError(Exception e) {

                e.printStackTrace();
            }
        });
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();

        finish();
    }


}
