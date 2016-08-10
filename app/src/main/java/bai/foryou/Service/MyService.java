package bai.foryou.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import bai.foryou.activity.MusicActivity;
import bai.foryou.util.Utility;

public class MyService extends Service implements MediaPlayer.OnPreparedListener{

    public static int current;
    public MyBroadcastReceiver receiver;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate(){
        super.onCreate();
        regFilter();
        Log.d("Myservice", "onCreate");
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        Log.d("Myservice", "onStart");
        return super.onStartCommand(intent,flags,startId);
    }
    /*
    *ע��㲥
     */
    private void regFilter(){
        IntentFilter filter=new IntentFilter();
        filter.addAction("");
        filter.addAction("");
        receiver=new MyBroadcastReceiver();
        registerReceiver(receiver, filter);//ע��㲥�������Ľ�����Ϊ
    }

    /*
    *�����Զ���Ĺ㲥������
     */
    public class MyBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context,Intent intent){
            if (intent.getAction().equals("bai.foryou.action_last")){
                Log.d("Myservice","service");
                prepareMediaPlayer(Utility.songUrl);
            }else if (intent.getAction().equals("bai.foryou.action_next")){

            }
        }
    }

    private void prepareMediaPlayer(String songUrl){
        MusicActivity.mediaPlayer.reset();
        try{
            Uri uri=Uri.parse(songUrl);
            MusicActivity.mediaPlayer.setDataSource(this, uri);
        }catch (Exception e){

        }
        MusicActivity.mediaPlayer.setOnPreparedListener(this);
        MusicActivity.mediaPlayer.prepareAsync();//prepare�������ģ�prepareAsync���첽��

    }
    /*
    *����׼����ɵĻص�������
     */
    @Override
    public void onPrepared(MediaPlayer mediaPlayer){
        mediaPlayer.start();
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        if (receiver!=null){
            unregisterReceiver(receiver);//������ֹʱ���
        }
        if (MusicActivity.mediaPlayer!=null){
            MusicActivity.mediaPlayer.release();
            MusicActivity.mediaPlayer=null;
        }
    }
}
