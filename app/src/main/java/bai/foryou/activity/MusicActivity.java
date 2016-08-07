package bai.foryou.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.net.URL;
import java.util.List;
import java.util.logging.Handler;

import bai.foryou.R;
import bai.foryou.model.LrcContent;
import bai.foryou.model.LrcView;
import bai.foryou.util.HttpCallbackListener;
import bai.foryou.util.HttpUtil;
import bai.foryou.util.Utility;

public class MusicActivity extends Activity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener{
    private TextView mTitle;
    private TextView mTexting;
    private TextView mTextDuration;

    private ImageView mPlay;
    private SeekBar   mSeekBar;
    public  static MediaPlayer  mediaPlayer;
    private Boolean isPlay=false;//是否正在播放
    private Boolean isThread=true;//是否让子线程继续

    private LrcView mLrcView;
    private List<LrcContent>lrclist;
    private int index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_music);


        mPlay=(ImageView)findViewById(R.id.music_play);
        mPlay.setOnClickListener(this);

        mSeekBar=(SeekBar)findViewById(R.id.m_seekBar);
        mSeekBar.setOnSeekBarChangeListener(this);

        mTexting=(TextView)findViewById(R.id.texting);
        mTextDuration=(TextView)findViewById(R.id.text_duration);
        mTitle=(TextView)findViewById(R.id.text_title);
        mTitle.setText(Utility.songTitle);


        mLrcView=(LrcView)findViewById(R.id.lrc_View);

       new LooperThread().start();



        String lrc_address=Utility.lrcUrl;
        if (lrc_address!=null){
            initLrc(lrc_address);
        }

        String song_address=Utility.songUrl;
        if (song_address!=null){
            initMediaPlayer(song_address);
        }


        mLrcView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_grow_fade_in_from_bottom));

    }

    public void initMediaPlayer(String songUrl){
        try{
            if (songUrl!=null){
                Uri uri=Uri.parse(songUrl);
                mediaPlayer=new MediaPlayer();
                mediaPlayer.setDataSource(this, uri);
                mediaPlayer.prepare();
                long duration=mediaPlayer.getDuration();
                String text_current=Utility.formatTime(duration);
                mTextDuration.setText(text_current);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public int lrcIndex(){
        if (mediaPlayer.isPlaying()){
            long currentTime=mediaPlayer.getCurrentPosition();
            long duration=mediaPlayer.getDuration();
            if(currentTime<duration){
                for (int i=0;i<lrclist.size();i++){
                    if(i<lrclist.size()-1){
                        if(currentTime<lrclist.get(i).getLrcTime()&&i==0){
                            index=i;
                        }
                        if(currentTime>lrclist.get(i).getLrcTime()&&currentTime<lrclist.get(i+1).getLrcTime()){
                            index=i;
                        }
                    }
                    if (i==lrclist.size()-1&&currentTime>lrclist.get(i).getLrcTime()){
                        index=i;
                    }
                }
            }
        }
        return index;
    }
    public void initLrc(String address){
        HttpUtil.setHttpRequest(address,new  HttpCallbackListener(){
            @Override
            public void onFinish(String response){
                lrclist= Utility.readLRC(response);
                mLrcView.setmLrcList(lrclist);
            }
            @Override
            public void onFinish(Bitmap bitmap){

            }
            @Override
            public void onError(Exception e){

            }

        });

    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.music_play:
                if(!isPlay){
                    isPlay=true;
                    Message message=Message.obtain();
                    handler.sendMessage(message);
                    mediaPlayer.start();
                }else{
                    isPlay=false;
                    mediaPlayer.pause();
                    Message message=Message.obtain();
                    handler.sendMessage(message);
                }
        }
    }
    private android.os.Handler handler=new android.os.Handler(){

        public void handleMessage(Message msg) {
            if (isPlay){
                mPlay.setImageResource(R.drawable.lock_suspend);
            }else{
                mPlay.setImageResource(R.drawable.lock_play);

            }
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser){

    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar){

    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar){
        int progress=seekBar.getProgress();
        long duration=mediaPlayer.getDuration();
        String text= Utility.formatTime(duration);
        mTextDuration.setText(text);
        int to=progress*(int)duration/100;
        mediaPlayer.seekTo(to);
    }

    android.os.Handler handler2=new android.os.Handler(){
        public void handleMessage(Message msg){

                long current=mediaPlayer.getCurrentPosition();

                String text_current=Utility.formatTime(current);
                mTexting.setText(text_current);
                long duration=mediaPlayer.getDuration();
                long a=100L*current/duration;
                int progress=new Long(a).intValue();
                mSeekBar.setProgress(progress);

        }
    };

    android.os.Handler handler3=new android.os.Handler(){
        public void handleMessage(Message msg){
            mLrcView.setIndex(lrcIndex());
            mLrcView.invalidate();
        }
    };

    class LooperThread extends Thread {

        @Override
        public void run() {

            while (isThread) {
                try {
                    handler2.sendMessage(new Message());
                    handler3.sendMessage(new Message());
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }







}
