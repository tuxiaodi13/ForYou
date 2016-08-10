package bai.foryou.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import bai.foryou.R;
import bai.foryou.util.HttpCallbackListener;
import bai.foryou.util.HttpUtil;
import bai.foryou.util.Utility;

public class ContentActivity extends Activity implements View.OnClickListener{
    TextView dateContent;
    TextView textView;
    String content;
    Button contentLast;
    Button contentNext;

    public static Integer idContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        dateContent=(TextView)findViewById(R.id.date_content);
        textView=(TextView)findViewById(R.id.tv1);

        contentLast=(Button)findViewById(R.id.content_last);
        contentNext=(Button)findViewById(R.id.content_next);

        contentLast.setOnClickListener(this);
        contentNext.setOnClickListener(this);

        dateContent.setText(Utility.date);
        String contentAddress=Utility.contentUrl;
        if (contentAddress!=null){
            initContent(contentAddress);
        }


    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            dateContent.setText(Utility.date);
            initContent(Utility.contentUrl);
        }
    };

    public void initContent(String contentUrl){
        HttpUtil.setHttpRequest(contentUrl, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {

                    content=response;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ScrollView sv=(ScrollView)findViewById(R.id.content_scrollview);//更新内容时，ScrollView滚回顶部
                            sv.scrollTo(0, 0);
                            textView.setText(content);
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

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.content_last:
                if (idContent>1) {
                    idContent=idContent-1;

                    NumberFormat nf=new DecimalFormat("00000000");
                    String str=nf.format(idContent);//将int类型的数字再次变回“00000001”格式的数据。
                    String address="http://bai-foryou.sinacloud.net/"+str+".txt";
                    HttpUtil.setHttpRequest(address, new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            Log.d("SentenceActivity", response);
                            Utility.handeleResponse(ContentActivity.this, response);
                            handler.sendMessage(new Message());


                        }

                        @Override
                        public void onFinish(Bitmap bitmap) {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }else {
                    Toast.makeText(this, "没有更多内容了", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.content_next:
                if (idContent<Utility.idToday){
                   idContent=idContent+1;

                    NumberFormat nf1=new DecimalFormat("00000000");
                    String str1=nf1.format(idContent
                    );//将int类型的数字再次变回“00000001”格式的数据。
                    String address1="http://bai-foryou.sinacloud.net/"+str1+".txt";
                    HttpUtil.setHttpRequest(address1, new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            Log.d("SentenceActivity", response);
                            Utility.handeleResponse(ContentActivity.this, response);
                            handler.sendMessage(new Message());
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
                break;
            default:
                break;
        }
    }


}
