package bai.foryou.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baiyaozhong on 2016-08-03.
 * 自定义绘画歌词，滚动效果
 */
public class LrcView extends TextView {
    public float width; //视图宽度，高度
    public float height;
    private Paint currentPaint;//当前画笔对象
    private Paint notCurrentPaint;//非当前画笔对象
    private float textHeight=65;//文本高度，大小
    private float textSize=30;
    private int index=0;

    private List<LrcContent>mLrcList=new ArrayList<LrcContent>();

    public void setmLrcList(List<LrcContent> mLrcList){
        this.mLrcList=mLrcList;
    }

    public LrcView(Context context){
        super(context);
        init();
    }

    public LrcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LrcView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    private void init(){
        setFocusable(true);
        //高亮部分
        currentPaint=new Paint();
        currentPaint.setAntiAlias(true);//设置抗锯齿
        currentPaint.setTextAlign(Paint.Align.CENTER);//文本对齐方式

        //非高亮部分
        notCurrentPaint=new Paint();
        notCurrentPaint.setAntiAlias(true);
        notCurrentPaint.setTextAlign(Paint.Align.CENTER);
    }
    /*
    *
     */
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(canvas==null){
            return;
        }

        currentPaint.setColor(Color.argb(210, 251, 248, 29));
        notCurrentPaint.setColor(Color.argb(140, 255, 255, 255));

        currentPaint.setTextSize(40);
        currentPaint.setTypeface(Typeface.SERIF);

        notCurrentPaint.setTextSize(textSize);
        notCurrentPaint.setTypeface(Typeface.DEFAULT);

        try {
            setText("");
            canvas.drawText(mLrcList.get(index).getLrcStr(),this.getWidth()/2,this.getHeight()/2,currentPaint);//绘出高亮部分
            //画出本句之前的句子
            float tempY=this.getHeight()/2;
            for (int i=index-1;i>=0;i--){
                //向上推移
                tempY=tempY-textHeight;
                canvas.drawText(mLrcList.get(i).getLrcStr(),this.getWidth()/2,tempY,notCurrentPaint);
            }
            tempY=this.getHeight()/2;
            //画出本句之后的句子
            for(int i=index+1;i<mLrcList.size();i++){
                //向下推移
                tempY=tempY+textHeight;
                canvas.drawText(mLrcList.get(i).getLrcStr(),this.getWidth()/2,tempY,notCurrentPaint);
            }
        }catch (Exception e){
            setText("无歌词");
        }
    }

    public void setIndex(int index){
        this.index=index;
    }
}
