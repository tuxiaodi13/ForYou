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
        //��ʼ���ؼ�
        engText=(TextView)findViewById(R.id.eng_content);
        chiText=(TextView)findViewById(R.id.chi_content);
        sentenceImageView=(ImageView)findViewById(R.id.sentence_img);
        dialogBtn=(Button)findViewById(R.id.dialogbtn);

        showText();

        showPicture();

        /*String message="ÿ���˶��������Լ���һƪɭ�֡�Ȼ�������������������꣬��Ƭɭ�ֵķ羰�վ�̫���ˡ�" +"\n"+
                "��С������һ����������Ʊ������ֱ�ӿ�������ɭ������ߴ��������Ư�����ʻ������ܻ�����ò���Ĺ����������С�\n"+
                "������ղصĺ����¡����֡�ͼƬ��������Щ�����ľ��ӷ�������ɡ�" +
                "\n"+
                "��ѽ���������ˣ��Ȳ�˵����ȥ����һ�·��͡�"+"\n"+
                "����ʱ�䣺ÿ��21:00-22:00"+"\n"+
                "751041873@qq.com";*/
        String message="����һ����̳�ӣ��Ͳ���һ���ơ�"+"\n"+
                "�����������������꣬Ը�㳣���������õĹ��£����������������ɱ����˴�����ĺþ�Ŷ"+"\n"+
                "��С����һ��������App��ÿ������һ�����֣�һ�׸�����һƪ����"+"\n"+
                "�͹��ǿ���ղصĺ����¡����֡�ͼƬ��������Щ�����ľ��ӷ��������"+"\n"+
                "�ҵķ��������ˣ��Ȳ�˵����ȥ����һ��"+"\n"+
                "���ݸ���ʱ�䣺ÿ��21:00-22:30"+"\n"+
                "��̳�ӵ�ַ��751041873@qq.com";
        verifyDialog(message);

    }

    private void showText(){


        engText.setText(Utility.engStr);
        chiText.setText(Utility.chiStr);


    }

    private void showPicture() {


        imgUrl = Utility.imgUrl;

        //������ص�ͼƬ��ַ��Ϊ�գ�����������ַ���������������쳣������
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
        dialog.setCanceledOnTouchOutside(true);//����հ���ʧ
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
