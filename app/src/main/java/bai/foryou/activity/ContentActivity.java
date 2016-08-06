package bai.foryou.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.URLEncoder;

import bai.foryou.R;
import bai.foryou.util.HttpCallbackListener;
import bai.foryou.util.HttpUtil;
import bai.foryou.util.Utility;

public class ContentActivity extends Activity {
    TextView tv1;
    String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        tv1=(TextView)findViewById(R.id.tv1);

        String address1=Utility.contentUrl;



        HttpUtil.setHttpRequest(address1, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {

                    content=response;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv1.setText(content);
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
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
