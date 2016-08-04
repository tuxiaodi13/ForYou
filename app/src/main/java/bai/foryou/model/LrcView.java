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
 * �Զ���滭��ʣ�����Ч��
 */
public class LrcView extends TextView {
    public float width; //��ͼ��ȣ��߶�
    public float height;
    private Paint currentPaint;//��ǰ���ʶ���
    private Paint notCurrentPaint;//�ǵ�ǰ���ʶ���
    private float textHeight=65;//�ı��߶ȣ���С
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
        //��������
        currentPaint=new Paint();
        currentPaint.setAntiAlias(true);//���ÿ����
        currentPaint.setTextAlign(Paint.Align.CENTER);//�ı����뷽ʽ

        //�Ǹ�������
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
            canvas.drawText(mLrcList.get(index).getLrcStr(),this.getWidth()/2,this.getHeight()/2,currentPaint);//�����������
            //��������֮ǰ�ľ���
            float tempY=this.getHeight()/2;
            for (int i=index-1;i>=0;i--){
                //��������
                tempY=tempY-textHeight;
                canvas.drawText(mLrcList.get(i).getLrcStr(),this.getWidth()/2,tempY,notCurrentPaint);
            }
            tempY=this.getHeight()/2;
            //��������֮��ľ���
            for(int i=index+1;i<mLrcList.size();i++){
                //��������
                tempY=tempY+textHeight;
                canvas.drawText(mLrcList.get(i).getLrcStr(),this.getWidth()/2,tempY,notCurrentPaint);
            }
        }catch (Exception e){
            setText("�޸��");
        }
    }

    public void setIndex(int index){
        this.index=index;
    }
}
