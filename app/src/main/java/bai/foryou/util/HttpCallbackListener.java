package bai.foryou.util;

import android.graphics.Bitmap;

/**
 * Created by Baiyaozhong on 2016-08-02.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onFinish(Bitmap bitmap);
    void onError(Exception e);
}
