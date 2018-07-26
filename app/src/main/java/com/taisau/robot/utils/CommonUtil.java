package com.taisau.robot.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Devin on 2018/6/1.
 */
public class CommonUtil {

    /**
     * 向SD卡写入数据
     *
     * @param str
     */
    public static void writeToSDCard(Context context, String str) {
        try {
            // 判断是否存在SD卡
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // 获取SD卡的目录
                File sdDire = Environment.getExternalStorageDirectory();
                FileOutputStream outFileStream = new FileOutputStream(sdDire.getCanonicalPath() + "/test.txt");
                outFileStream.write(str.getBytes());
                outFileStream.close();
                Toast.makeText(context, "储存成功！", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
