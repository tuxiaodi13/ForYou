package bai.foryou.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import bai.foryou.model.LrcContent;

/**
 * Created by Baiyaozhong on 2016-08-02.
 */
public class Utility {
    public static String engStr=null;
    public static String chiStr=null;
    public static String imgUrl=null;
    public static String songTitle=null;
    public static String lrcUrl=null;
    public static String songUrl=null;
    public static String songRecommend=null;
    public static String contentUrl=null;
    public static String contentRecommend=null;
    public static String date=null;
    public static Integer id=null;//第几期
    public static Integer idToday=null;//今天是第几期
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
    public  synchronized static void handeleResponse(Context context,String response){
        try{
            JSONObject json=new JSONObject(response);
            engStr=json.getString("engSentence");
            chiStr=json.getString("chiSentence");
            imgUrl=json.getString("imgSentenceUrl");
            songTitle=json.getString("songTitle");
            lrcUrl=json.getString("lrcUrl");
            songUrl=json.getString("songUrl");
            songRecommend=json.getString("songRecommend");
            contentUrl=json.getString("contentUrl");
            contentRecommend=json.getString("contentRecommend");
            date=json.getString("date");
            id=Integer.parseInt(json.getString("id"));

            Log.d("Utility","running");

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Utility",Log.getStackTraceString(e));
            Log.d("Utility", "error");
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
    //获得当前日期
    public static String StringData(){
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份

        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        if (mMonth.length()==1){
            mMonth="0"+mMonth;
        }
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        if (mDay.length()==1){
            mDay="0"+mDay;
        }
       /*
       String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if("1".equals(mWay)){
            mWay ="天";
        }else if("2".equals(mWay)){
            mWay ="一";
        }else if("3".equals(mWay)){
            mWay ="二";
        }else if("4".equals(mWay)){
            mWay ="三";
        }else if("5".equals(mWay)){
            mWay ="四";
        }else if("6".equals(mWay)){
            mWay ="五";
        }else if("7".equals(mWay)){
            mWay ="六";
        }
        return mYear +  + mMonth + "月" + mDay+"日"+"/星期"+mWay;
        */
        return mYear+mMonth+mDay;
    }

}
