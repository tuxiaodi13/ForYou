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
    *ע��㲥
     */
    private void regFilter(){
        IntentFilter filter=new IntentFilter();
        filter.addAction("");
        filter.addAction("");

    }

    /*
    *�����Զ���Ĺ㲥������
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
        mediaPlayer.prepareAsync();//prepare�������ģ�prepareAsync���첽��
    }


    public void startMusic(){
        mediaPlayer.start();
    }
    public void pauseMusic(){
        mediaPlayer.pause();
        current=mediaPlayer.getCurrentPosition();
    }
}
