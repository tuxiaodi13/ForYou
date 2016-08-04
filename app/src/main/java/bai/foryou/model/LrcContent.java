package bai.foryou.model;

/**
 * Created by Baiyaozhong on 2016-08-03.
 * 歌词实体类
 */
public class LrcContent {
    private String lrcStr;//歌词内容
    private int lrcTime;//本句歌词对应时间
    public  String getLrcStr(){
        return lrcStr;
    }
    public void setLrcStr(String lrcStr){
        this.lrcStr=lrcStr;
    }
    public int getLrcTime(){
        return lrcTime;
    }
    public void setLrcTime(int lrcTime){
        this.lrcTime=lrcTime;
    }
}
