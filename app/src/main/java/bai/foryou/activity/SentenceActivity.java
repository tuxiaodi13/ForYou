package bai.foryou.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;

import android.os.Bundle;
import android.util.Log;
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

public class SentenceActivity extends Activity{
    private TextView engText;
    private TextView chiText;
    private ImageView sentenceImageView;
    private String imgUrl;
    private Button dialogBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sentence);
        //初始化控件
        engText=(TextView)findViewById(R.id.eng_content);
        chiText=(TextView)findViewById(R.id.chi_content);
        sentenceImageView=(ImageView)findViewById(R.id.sentence_img);
        dialogBtn=(Button)findViewById(R.id.dialogbtn);

        showText();

        showPicture();

        /*String message="每个人都有属于自己的一篇森林。然而朝菌蝼蛄，人生百年，这片森林的风景终究太少了。" +"\n"+
                "“小醉”就是一张这样的门票，让你直接看到别人森林中最高大的树，最漂亮的鲜花，可能还有最貌美的姑娘和最靓的小伙。\n"+
                "快把你收藏的好文章、音乐、图片、或者那些优美的句子分享出来吧。" +
                "\n"+
                "啊呀发型又乱了，先不说了我去整理一下发型。"+"\n"+
                "更新时间：每晚21:00-22:00"+"\n"+
                "751041873@qq.com";*/
        String message="我有一个酒坛子，就差你一壶酒。"+"\n"+
                "朝菌蝼蛄，人生百年，愿你常来这里，讲最好的故事，听最美的声音。可别忘了带上你的好酒哦"+"\n"+
                "“小醉”是一款内容类App，每天会更新一段文字，一首歌曲，一篇文章"+"\n"+
                "客官们快把收藏的好文章、音乐、图片、或者那些优美的句子分享出来吧"+"\n"+
                "我的发型又乱了，先不说了我去整理一下"+"\n"+
                "内容更新时间：每晚21:00-22:30"+"\n"+
                "酒坛子地址：751041873@qq.com";
        verifyDialog(message);

    }

    private void showText(){


        engText.setText(Utility.engStr);
        chiText.setText(Utility.chiStr);


    }

    private void showPicture() {


        imgUrl = Utility.imgUrl;

        //如果返回的图片地址不为空，则访问这个地址，否则会出现网络异常，闪退
        if (imgUrl != null) {

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

                    e.printStackTrace();
                }
            });
        }

    }
    private void verifyDialog(String msg)
    {   Log.d("SentenceActivity", "dialog");
        final Dialog dialog = new Dialog(this,R.style.popuDialog);
        dialog.setContentView(R.layout.verify_dialog);
        dialog.setCanceledOnTouchOutside(true);//点击空白消失
        dialog.setCancelable(true);
        TextView message = (TextView)dialog.getWindow().findViewById(R.id.messageTxt);
        message.setText(msg);
        dialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.show();
                } else if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();

        finish();
    }


}
