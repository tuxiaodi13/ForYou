package bai.foryou.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;

import android.os.Bundle;
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

public class SentenceActivity extends Activity implements View.OnClickListener{
    private TextView engText;
    private TextView chiText;
    private ImageView sentenceImageView;
    private Button  sentencePlayButton;
    private Button  button2;
    private String  musicUrl;
    private String imgUrl;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sentence);
        //初始化控件
        engText=(TextView)findViewById(R.id.eng_content);
        chiText=(TextView)findViewById(R.id.chi_content);
        sentenceImageView=(ImageView)findViewById(R.id.sentence_img);
        sentencePlayButton=(Button)findViewById(R.id.sentencePlay_btn);
        sentencePlayButton.setOnClickListener(this);
        button2=(Button)findViewById(R.id.toMusicActivity_btn);
        button2.setOnClickListener(this);
        //初始化MediaPlayer
        try{
            //Uri uri=Uri.parse("http://m2.music.126.net/quMEYwWS1XUziUg4J6Nn6w==/1030242395233370.mp3");
            Uri uri=Uri.parse("http://m2.music.126.net/3TLW28ieIgYKUYhe-ZFODQ==/2026399930004856.mp3");

            mediaPlayer=new MediaPlayer();
            mediaPlayer.setDataSource(this, uri);

            mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }

        showText();
        showPicture();
    }
    private void showText(){
        String address="http://open.iciba.com/dsapi/";
        HttpUtil.setHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                Utility.handleSentenceResponse(SentenceActivity.this, response);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SentenceActivity.this);
                        engText.setText(prefs.getString("eng_text", ""));
                        chiText.setText(prefs.getString("chi_text", ""));

                        musicUrl = (prefs.getString("music_url", ""));

                    }
                });

            }

            @Override
            public void onFinish(Bitmap bitmap) {

            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    private void showPicture(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SentenceActivity.this);
        imgUrl=prefs.getString("img_url","");

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

            }
        });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.sentencePlay_btn:
                mediaPlayer.start();
                break;
            case R.id.toMusicActivity_btn:
                Intent intent=new Intent(this,MusicActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


}
