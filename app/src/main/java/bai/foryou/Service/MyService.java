package bai.foryou.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
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

    public void startMusic(){
        mediaPlayer.start();
    }
    public void pauseMusic(){
        mediaPlayer.pause();
        current=mediaPlayer.getCurrentPosition();
    }
}
