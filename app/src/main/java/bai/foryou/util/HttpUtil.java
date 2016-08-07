package bai.foryou.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Baiyaozhong on 2016-08-01.
 */
public class HttpUtil {
    public static void setHttpRequest(final String address,final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try{
                    URL url=new URL(address);
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in=connection.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line+"\n");
                    }
                    if (listener!=null){
                        listener.onFinish(response.toString());
                    }
                }catch (Exception e){
                    Log.d("HTTP","网络异常");

                    if (listener!=null){
                        listener.onError(e);
                    }

                }finally {
                    if (connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static boolean isNetConnected(Context context){
        boolean isNetConnected;
        //获得网络连接服务
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=connectivityManager.getActiveNetworkInfo();
        if (info!=null&&info.isAvailable()){
            isNetConnected=true;
        }else {
            Log.i("SplashActivity", "没有可用网络");
            isNetConnected = false;
        }
        return isNetConnected;
    }

}
