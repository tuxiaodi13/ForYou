package bai.foryou.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import bai.foryou.model.LrcContent;

/**
 * Created by Baiyaozhong on 2016-08-02.
 */
public class Utility {
    /*
     *解析返回的每日一句
     */
    public synchronized static void handleSentenceResponse(Context context,String response){
        try{
            JSONObject json=new JSONObject(response);
            String engStr=json.getString("content");
            String chiStr=json.getString("note");
            String imgUrl=json.getString("picture2");
            String musicUrl=json.getString("tts");
            SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putString("eng_text",engStr);
            editor.putString("chi_text",chiStr);
            editor.putString("img_url",imgUrl);
            editor.putString("mucic_url",musicUrl);
            editor.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String formatTime(long time) {
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";
        if (min.length() < 2) {
            min = "0" + time / (1000 * 60) + "";
        } else {
            min = time / (1000 * 60) + "";
        }
        if (sec.length() == 4) {
            sec = "0" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 3) {
            sec = "00" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 2) {
            sec = "000" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 1) {
            sec = "0000" + (time % (1000 * 60)) + "";
        }
        return min + ":" + sec.trim().substring(0, 2);
    }

    public static List<LrcContent> readLRC(String response){
        String mLrc="";
        LrcContent mLrcContent;
        List<LrcContent> lrcList=new ArrayList<LrcContent>();
        try{
            JSONObject json=new JSONObject(response);
            JSONObject lrcJson=new JSONObject(json.getString("lrc"));
            mLrc=lrcJson.getString("lyric");
            mLrc= mLrc.replace("[", "");
            mLrc=mLrc.replace("]","@");

            String[] splitLrcData=mLrc.split("\n");
            if (splitLrcData!=null&&splitLrcData.length>0){
                for(String lrc:splitLrcData){
                    String array[]=lrc.split("@");
                    if (array.length>1){
                        mLrcContent=new LrcContent();
                        int lrcTime=time2Str(array[0]);
                        mLrcContent.setLrcStr(array[1]);
                        mLrcContent.setLrcTime(lrcTime);

                        lrcList.add(mLrcContent);
                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return lrcList;
    }
    /**
     * 解析歌词时间
     * 歌词内容格式如下：
     * [00:02.32]陈奕迅
     * [00:03.43]好久不见
     */
    public static int time2Str(String timeStr){
        timeStr=timeStr.replace(":",".");
        timeStr=timeStr.replace(".", "@");

        String timeData[]=timeStr.split("@");//将一个时间字符串分解为，分，秒，毫秒

        int minute=Integer.parseInt(timeData[0]);
        int second=Integer.parseInt(timeData[1]);
        int millisecond=Integer.parseInt(timeData[2]);

        int currentTime=(minute*60+second)*1000+millisecond*10;
        return currentTime;
    }

}
