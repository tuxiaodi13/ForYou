package bai.foryou.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

public class MyService extends Service {
    public MediaPlayer mediaPlayer;
    public static int current;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        mediaPlayer=new MediaPlayer();
        return super.onStartCommand(intent,flags,startId);
    }
    /*
    *注册广播
     */
    private void regFilter(){
        IntentFilter filter=new IntentFilter();
        filter.addAction("");
        filter.addAction("");

    }

    /*
    *创建自定义的广播接收器
     */
    public class MyBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context,Intent intent){
            if (intent.getAction().equals("")){

            }else if (intent.getAction().equals("")){

            }
        }
    }

    private void prepareMediaPlayer(String songUrl){
        mediaPlayer.reset();
        try{

            Uri uri=Uri.parse(songUrl);
            mediaPlayer.setDataSource(this,uri);
        }catch (Exception e){

        }
        mediaPlayer.prepareAsync();//prepare是阻塞的，prepareAsync是异步的
    }


    public void startMusic(){
        mediaPlayer.start();
    }
    public void pauseMusic(){
        mediaPlayer.pause();
        current=mediaPlayer.getCurrentPosition();
    }
}
