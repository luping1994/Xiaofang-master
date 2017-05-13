package com.suntrans.xiaofang;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.suntrans.xiaofang.utils.LogUtil;
import com.tencent.bugly.Bugly;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.suntrans.xiaofang.BuildConfig.ENABLE_DEBUG;

/**
 * Created by Looney on 2016/8/11.
 * Des:代表当前应用.
 */
public class App extends MultiDexApplication {

    private static App application;
    private static int mainTid;
    private static Handler mHandler;
    private static SharedPreferences msharedPreferences;
    @Override
    public void onCreate() {
        super.onCreate();
        Bugly.init(getApplicationContext(), "7d01f61d8c", ENABLE_DEBUG);

////        PgyCrashManager.register(this);
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
//        // Normal app init code...
//        CrashReport.initCrashReport(getApplicationContext(), "7d01f61d8c", true);//初始化腾讯bug分析工具
        application=this;
        mainTid=android.os.Process.myTid();
        mHandler=new Handler();
        new Thread(){//copy assets目录下的开关信息数据库
            @Override
            public void run() {
                try {
                    CopySqliteFileFromRawToDatabases("Fire");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static SharedPreferences getSharedPreferences(){
        if (msharedPreferences==null){
            msharedPreferences = getApplication().getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return msharedPreferences;
    }
    public static Context getApplication() {
        return application;
    }

    public static int getMainTid() {
        return mainTid;
    }
    public static Handler getHandler() {
        return mHandler;
    }
    /**
     * 见assets目录下的文件拷贝到sd上
     * @return 存储数据库的地址
     */

  // 复制和加载区域数据库中的数据
    public String CopySqliteFileFromRawToDatabases(String SqliteFileName) throws IOException {

        // 第一次运行应用程序时，加载数据库到data/data/当前包的名称/database/<db_name>

        File dir = new File("data/data/" + App.getApplication().getPackageName() + "/databases");
        LogUtil.i("!dir.exists()=" + !dir.exists());
        LogUtil.i("!dir.isDirectory()=" + !dir.isDirectory());

        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }

        File file= new File(dir, SqliteFileName);
        InputStream inputStream = null;
        OutputStream outputStream =null;

        //通过IO流的方式，将assets目录下的数据库文件，写入到SD卡中。
        if (!file.exists()) {
            try {
                file.createNewFile();

                inputStream = App.getApplication().getClass().getClassLoader().getResourceAsStream("assets/" + SqliteFileName);
                outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int len ;

                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer,0,len);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            finally {

                if (outputStream != null) {

                    outputStream.flush();
                    outputStream.close();

                }
                if (inputStream != null) {
                    inputStream.close();
                }

            }

        }
        return file.getPath();
    }

    /** * 分割 Dex 支持 * @param base */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
