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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.logging.Handler;

import bai.foryou.R;
import bai.foryou.model.LrcContent;
import bai.foryou.model.LrcView;
import bai.foryou.util.HttpCallbackListener;
import bai.foryou.util.HttpUtil;
import bai.foryou.util.Utility;

public class MusicActivity extends Activity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener,
        MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener{
    private TextView dateMusic;
    private TextView mTitle;
    private TextView mTexting;
    private TextView mTextDuration;

    private ImageView mPlay;
    private ImageView musicLast;
    private ImageView musicNext;

    private SeekBar   mSeekBar;

    public  static MediaPlayer  mediaPlayer;

    private Boolean isPlay=false;//是否正在播放
    private Boolean isThread=true;//是否让子线程继续

    private LrcView mLrcView;
    private List<LrcContent>lrclist;
    private int index=0;

    public static Integer idMusic;
    public long duration=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_music);


        mPlay=(ImageView)findViewById(R.id.music_play);
        mPlay.setOnClickListener(this);

        musicLast=(ImageView)findViewById(R.id.music_last);
        musicLast.setOnClickListener(this);

        musicNext=(ImageView)findViewById(R.id.music_next);
        musicNext.setOnClickListener(this);

        mSeekBar=(SeekBar)findViewById(R.id.m_seekBar);
        mSeekBar.setOnSeekBarChangeListener(this);

        mTexting=(TextView)findViewById(R.id.texting);
        mTextDuration=(TextView)findViewById(R.id.text_duration);
        dateMusic=(TextView)findViewById(R.id.date_music);

        mTitle=(TextView)findViewById(R.id.text_title);
        mTitle.setText(Utility.songTitle);
        dateMusic.setText(Utility.date);


        mLrcView=(LrcView)findViewById(R.id.lrc_View);
        mLrcView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_grow_fade_in_from_bottom));

        new LooperThread().start();

        mediaPlayer=new MediaPlayer();

        String song_address=Utility.songUrl;

        if (song_address!=null){
            initMediaPlayer(song_address);
            //初始化歌词
            String lrc_address=Utility.lrcUrl;
            if (lrc_address!=null){
                initLrc(lrc_address);
            }
        }
    }


    public void initMediaPlayer(String songUrl){
        try{
            if (songUrl!=null){
                Uri uri=Uri.parse(songUrl);
                mediaPlayer.reset();
                mediaPlayer.setDataSource(this, uri);
                mediaPlayer.prepareAsync();//prepare()是线程阻塞的，prepareAsync()是异步的
                mediaPlayer.setOnPreparedListener(this);

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onPrepared(MediaPlayer mediaPlayer){
        duration=mediaPlayer.getDuration();
        String text_current=Utility.formatTime(duration);
        mTextDuration.setText(text_current);
    }
    @Override
    public void onCompletion(MediaPlayer mediaPlayer){

    }


//根据MediaPlayer当前播放时间，得到应该绘制哪一句。
    public int lrcIndex(){
        if (mediaPlayer.isPlaying()){
            long currentTime=mediaPlayer.getCurrentPosition();
            duration=mediaPlayer.getDuration();
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
                    mediaPlayer.start();
                    handler.sendMessage(message);
                }else{
                    isPlay=false;
                    mediaPlayer.pause();
                    Message message=Message.obtain();
                    handler.sendMessage(message);
                }
                break;
            case R.id.music_last:
                if (mediaPlayer.isPlaying()){
                    Toast.makeText(MusicActivity.this,"嘿嘿，先暂停再切换歌曲",Toast.LENGTH_SHORT).show();
                }else{
                    if (idMusic>1){
                        idMusic=idMusic-1;

                        NumberFormat nf=new DecimalFormat("00000000");
                        String str=nf.format(idMusic);//将int类型的数字再次变回“00000001”格式的数据。
                        String address="http://bai-foryou.sinacloud.net/"+str+".txt";


                        HttpUtil.setHttpRequest(address, new HttpCallbackListener() {
                            @Override
                            public void onFinish(String response) {

                                Utility.handleResponse(MusicActivity.this, response);
                                //Intent intent_last=new Intent();
                                //intent_last.setAction("bai.foryou.action_last");
                                //sendBroadcast(intent_last);
                                Message msg = new Message();
                                handler4.sendMessage(msg);
                                Log.d("MusicActivity",Utility.songUrl);
                            }

                            @Override
                            public void onFinish(Bitmap bitmap) {

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                    }else {
                        Toast.makeText(this,"没有更多内容了",Toast.LENGTH_SHORT).show();
                    }

                }

                break;
            case R.id.music_next:
                if (mediaPlayer.isPlaying()){
                    Toast.makeText(MusicActivity.this,"先暂停，嘿嘿嘿",Toast.LENGTH_SHORT).show();
                }else {

                    if (idMusic<Utility.idToday){
                        idMusic=idMusic+1;

                        NumberFormat nf1=new DecimalFormat("00000000");
                        String str1=nf1.format(idMusic);//将int类型的数字再次变回“00000001”格式的数据。
                        Log.d("SentenceActivity", str1);
                        String address1="http://bai-foryou.sinacloud.net/"+str1+".txt";
                        HttpUtil.setHttpRequest(address1, new HttpCallbackListener() {
                            @Override
                            public void onFinish(String response) {
                                Utility.handleResponse(MusicActivity.this, response);
                                Message msg = new Message();
                                handler4.sendMessage(msg);
                            }

                            @Override
                            public void onFinish(Bitmap bitmap) {

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                    }else {
                        Toast.makeText(this,"已经是最新一期",Toast.LENGTH_SHORT).show();
                    }

                }

                break;
            default:
                break;
        }
    }
    //重置mediaPlayer
   private android.os.Handler handler4=new android.os.Handler(){
        public void handleMessage(Message msg){
            initMediaPlayer(Utility.songUrl);
            initLrc(Utility.lrcUrl);
            mTitle.setText(Utility.songTitle);}
    };

    private android.os.Handler handler=new android.os.Handler(){

        public void handleMessage(Message msg) {
            if (isPlay){
                mPlay.setImageResource(R.drawable.lock_suspend);
            }else{
                mPlay.setImageResource(R.drawable.lock_play);

            }
        }
    };
//OnSeekBarChangeListener的三个方法
    @Override
    public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser){

    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar){

    }
    //进度条停止拖动时调用
    @Override
    public void onStopTrackingTouch(SeekBar seekBar){
        int progress=seekBar.getProgress();
        duration=mediaPlayer.getDuration();
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
                duration=mediaPlayer.getDuration();
                long a=100L*current/duration;
                int progress=new Long(a).intValue();
                mSeekBar.setProgress(progress);

        }
    };

    android.os.Handler handler3=new android.os.Handler(){
        public void handleMessage(Message msg){
            mLrcView.setIndex(lrcIndex());
            mLrcView.invalidate();//重绘
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
