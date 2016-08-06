package bai.foryou.activity;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bai.foryou.R;
import bai.foryou.util.HttpCallbackListener;
import bai.foryou.util.HttpUtil;
import bai.foryou.util.Utility;

public class MainActivity extends Activity {
    private ViewPager pager=null;
    LocalActivityManager manager=null;
    Context context;
    private boolean doubleBackToExitPressedOnce=false;//双击退出程序的标志

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=MainActivity.this;
        manager=new LocalActivityManager(this,true);
        manager.dispatchCreate(savedInstanceState);
        initPagerViewer();
    }

    private void initPagerViewer(){
        pager=(ViewPager)findViewById(R.id.viewpage);
        final ArrayList<View> list=new ArrayList<View>();
        Intent intent=new Intent(context,SentenceActivity.class);
        list.add(getView("A", intent));
        Intent intent2=new Intent(context,MusicActivity.class);
        list.add(getView("B", intent2));
        Intent intent3=new Intent(context,ContentActivity.class);
        list.add(getView("C", intent3));


        pager.setAdapter(new MyPagerAdapter(list));
        pager.setCurrentItem(0);
        pager.setOnPageChangeListener(new MyOnPageChangeListener());
    }
    /*
    *通过activity获取视图
     */
    private View getView(String id,Intent intent){
        return manager.startActivity(id, intent).getDecorView();
    }

    public class MyPagerAdapter extends PagerAdapter{
        List<View> list=new ArrayList<View>();
        public MyPagerAdapter(ArrayList<View> list){
            this.list=list;
        }
        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            ViewPager pViewPager = ((ViewPager) container);
            pViewPager.removeView(list.get(position));
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return list.size();
        }
        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ViewPager pViewPager = ((ViewPager) arg0);
            pViewPager.addView(list.get(arg1));
            return list.get(arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageSelected(int position){
           // currIndex=position;
        }
        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }
    //双击退出程序
    @Override
    public void onBackPressed(){
        if(doubleBackToExitPressedOnce){
            super.onBackPressed();
            Log.d("MusicActivity", "232");
            MusicActivity.mediaPlayer.pause();
            MusicActivity.mediaPlayer.release();
            return;
        }
        this.doubleBackToExitPressedOnce=true;//按第一次后标志位变为true
        Toast.makeText(this,"再按一次返回键退出",Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;//两秒内没按第二次，标志位重置为false
            }
        }, 2000);

    }

}
