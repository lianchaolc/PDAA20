package com.main.pda;

import hdjc.rfid.operator.RFID_Device;
import okhttp3.OkHttpClient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.*;

import com.application.GApplication;
import com.clearadmin.pda.ClearAddMoneyOutDo;
import com.example.pda.BuildConfig;
import com.example.pda.R;
import com.golbal.pda.GolbalView;
import com.hjq.permissions.XXPermissions;
import com.loginsystem.biz.PermissionUtil;
import com.loginsystem.biz.PictureFileHelp;
import com.manager.classs.pad.ManagerClass;
import com.messagebox.Loading;
import com.messagebox.Runing;
import com.online.update.biz.GetPDA;
import com.online.update.biz.LoadInfo;
import com.online.update.biz.Online;
import com.online.update.biz.VersionInfo;
import com.online.update.service.PDAVersionInfoPathService;
import com.service.FixationValue;
import com.sql.SQL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class VersionCheck extends Activity {
    private static final int REQ_CODE_PERMISSION = 0x1111;//  新增200200727
    TextView versionNum, tvscan;
    Button btn;
    private GetPDA Pda;
    private ManagerClass managerClass;
    Loading on = new Loading();
    Handler handler = null; // 获取版本号
    public static boolean stopupdate = true;
    private Runing runing;
    SharedPreferences sharepre;
    String space; // 空间
    String webservice; // webservice地址
    private boolean install;
    private GolbalView g;
    GolbalView getG() {
        if (g == null) {
            g = new GolbalView();
        }
        return g;
    }
    GetPDA getPda() {
        if (Pda == null) {
            Pda = new GetPDA();
        }
        return Pda;
    }

    Online online = new Online();
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 禁止休睡眠
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_version_check);
        btn = (Button) findViewById(R.id.checkupdate);
        versionNum = (TextView) findViewById(R.id.version);
        tvscan = (TextView) findViewById(R.id.tvscan);
        runing = new Runing();
        versionNum.setText("当前版本" + getVersion());
        managerClass = new ManagerClass();
        MainActivity.list_version.add(this);
        tvscan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getPda().getpathONlineyuan(handler, VersionCheck.this);
            }
        });
        // 更新按钮
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent even) {
                switch (even.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn.setBackgroundResource(R.drawable.gray_btn_bg_press);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn.setBackgroundResource(R.drawable.gray_btn_bg);

//					runing.runding(VersionCheck.this, "此功能暂未开发");
//					new Thread(new Runnable() {
//						@Override
//						public void run() {
//							SystemClock.sleep(150anyType0);
//							runing.remove();
//						}
//					}).start();
//					if (false){
                        // LR_TODO: 2020/4/3 16:18 liu_rui 版本更新暂未开发所以删掉
                        runing.runding(VersionCheck.this, "正在获取新版本");
                        getPda().getpath(handler, VersionCheck.this);
//					}
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        btn.setBackgroundResource(R.drawable.gray_btn_bg);
                        break;

                }

                return true;
            }
        });

        // 通知安装新版本
        final Handler h_load = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // 取消计时器
                new Online().t.cancel();
                if (msg.what == 100) {
                    if (!install) {
                        install = true;
                        Timer t = new Timer();
                        t.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                installAPK();
                            }
                        }, 2500);
                    }
                }
            }

        };

        progressDialog =new ProgressDialog(VersionCheck.this);
        // 获取版本号并更新
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                runing.remove();
                switch (msg.what) {
                    case -1:
                        managerClass.getSureCancel().makeSuerCancel(VersionCheck.this, "获取版本号失败,是否重新获取？",
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        managerClass.getSureCancel().remove();
                                        runing.runding(VersionCheck.this, "正在获取新版本");
                                        getPda().getpath(handler, VersionCheck.this);
                                    }
                                }, false);
                        break;
                    case -2:
                        managerClass.getSureCancel().makeSuerCancel(VersionCheck.this, "获取版本号失败,是否重新获取？",
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        managerClass.getSureCancel().remove();
                                        runing.runding(VersionCheck.this, "正在获取新版本");
                                        final DownloadTask downloadTask = new DownloadTask(VersionCheck.this);
                                        //execute 执行一个异步任务，通过这个方法触发异步任务的执行。这个方法要在主线程调用。
                                        String   path="";// 下载文件方法   path = new PDAVersionInfoPathService().getpath();

                                        try {
                                            downloadTask.execute(VersionInfo.URL);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialog) {
                                                downloadTask.cancel(true);
                                            }
                                        });

                                    }
                                }, false);
                        break;
                    case 99:
                        managerClass.getSureCancel().makeSuerCancel(VersionCheck.this, "发现新版本，是否现在更新？",
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {

                                        managerClass.getSureCancel().remove();

                                        final DownloadTask downloadTask = new DownloadTask(VersionCheck.this);
                                        //execute 执行一个异步任务，通过这个方法触发异步任务的执行。这个方法要在主线程调用。
                                        String   path="";// 下载文件方法   path = new PDAVersionInfoPathService().getpath();

                                        try {
                                                downloadTask.execute(VersionInfo.URL);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialog) {
                                                downloadTask.cancel(true);
                                            }
                                        });

//                                        on.loading(VersionCheck.this, h_load);
                                    }
                                }, false);
                        break;
                    case 44:
                        GolbalView.toastShow(VersionCheck.this, "目前已是最高版本");
                        break;
                        case 98:
                            managerClass.getSureCancel().makeSuerCancel(VersionCheck.this, "发现新版本，是否现在更新？",
                                    new OnClickListener() {
                                        @Override
                                        public void onClick(View arg0) {
                                            managerClass.getSureCancel().remove();
                                            on.loading(VersionCheck.this, h_load);
                                        }
                                    }, false);
                            break;
                }

            }

        };

    }

    // 安装应用程序
    public void installAPK() {
        String paths = Environment.getExternalStorageDirectory() + "/PDA_Version" + "/" + VersionInfo.APKNAME;
//		String paths = Environment.getExternalStorageDirectory()+ "/" +VersionInfo.APKNAME;
        // String paths =
        // Environment.getExternalStorageDirectory()+"/PDA_Version"+"/PDAA20.apk";
        File file = new File(paths);
        Uri uri;
        if (file.exists()) {
            if (XXPermissions.isHasPermission(VersionCheck.this, PermissionUtil.Group.STORAGE)) {
//				if (Build.VERSION.SDK_INT >= 24) {
////				uri = FileProvider.getUriForFile(VersionCheck.this, VersionCheck.this.getPackageName() + ".FileProvider", file);
////					installapktest();
//					File file2= new File(paths);
//					Uri apkUri = FileProvider.getUriForFile(VersionCheck.this, "com.example.pda.FileProvider", file2);//在AndroidManifest中的android:authorities值
//					Intent install = new Intent(Intent.ACTION_VIEW);
//					install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
//					install.setDataAndType(apkUri, "application/vnd.android.package-archive");
//					VersionCheck.this.startActivity(install);
//				}else{
//
//					Intent intent = new Intent(Intent.ACTION_VIEW);
//					try {
//						intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//						startActivity(intent);
//						VersionCheck.this.finish();
//						exit();
//					} catch (Exception e) {
//						e.getCause();
//						System.out.println(e.getMessage());
//					}
//				}


//			    File dir = new File(paths);
//				File file1 = new File(dir, "PDA.apk");
                if (!file.exists()) {
                    try {
                        file.createNewFile(); //创建文件
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("gdchent", "文件存在");
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    //小于7.0
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    //大于7.0
                    // 声明需要的临时权限
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    // 第二个参数，即第一步中配置的authorities
                    String packageName = VersionCheck.this.getApplication().getPackageName();
                    Log.i("gdchent", "package:" + packageName);
                    Log.i("gdchent", "ab_path:" + file.getAbsolutePath());
                    Uri contentUri = FileProvider.getUriForFile(VersionCheck.this, getPackageName()+".FileProvider", file);
//					FileProvider.getUriForFile(VersionCheck.this,"com.example.pda.FileProvider",file1);//在AndroidManifest中的andr
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                }
                VersionCheck.this.startActivity(intent);
            } else {
                PictureFileHelp.requestPermissionSave(VersionCheck.this, REQ_CODE_PERMISSION);
                XXPermissions.gotoPermissionSettings(this);

            }


        }


    }


    /***
     * 7.0 设备安装
     */
    private void install1(Activity activity, String name) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // 打开安装包方式
        intent.setDataAndType(getUri1(activity, name), "application/vnd.android.package-archive");
        activity.startActivity(intent);
    }

    /***
     * 转url
     */
    private Uri getUri1(Activity activity, String name) {
        Uri uri;
        // 获取下载apk，存储地址
//		File fileSave = new File(getDownloadPath() + File.separator + String.valueOf(BuildConfig.APPLICATION_ID) + ".apk");
        File fileSave = new File(getDownloadPath() + "/PDA_Version" + "/" + File.separator + String.valueOf(BuildConfig.APPLICATION_ID) + ".apk");
        if (Build.VERSION.SDK_INT >= 24) {
            // 适配android7.0 ，不能直接访问原路径
            // 需要对intent 授权
            uri = FileProvider.getUriForFile(activity, "com.example.pda.FileProvider", fileSave);
        } else {
            Log.d("----------------------", fileSave + "");
            uri = Uri.fromFile(fileSave);
        }
        return uri;
    }


    /***
     * /**
     * 获取下载apk，存储地址
     *
     * @return
     */
    private String getDownloadPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }


    public void installapktest() {
        install1(VersionCheck.this, "PDA.apk");
    }


    public void exit() {
        for (int i = 0; i < MainActivity.list_version.size(); i++) {
            MainActivity.list_version.get(i).finish();
        }
    }

    public String getVersion() {
        String versioncode = "";
        PackageManager packageManager = VersionCheck.this.getPackageManager();
        PackageInfo info;
        try {
            info = packageManager.getPackageInfo(VersionCheck.this.getPackageName(), 0);
            // 当前版本号
            versioncode = info.versionName;
            Log.i("versioncode当前版本号", versioncode + "");
        } catch (NameNotFoundException e) {

            e.printStackTrace();
        }

        return versioncode;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopupdate = false;
        online.t.cancel();
        on.remove();
    }

    @Override
    protected void onStart() {
        super.onStart();
        stopupdate = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopupdate = false;
        online.t.cancel();
        on.remove();
    }



    private View v;
    ProgressBar bar;
    TextView percentText;
    public class DownloadTask extends AsyncTask<String,Integer,String> {
        private Context context;
        private PowerManager.WakeLock mWakeLock;
        public DownloadTask(Context context) {
            this.context = context;
        }
        //onPreExecute(),在execute(Params... params)方法被调用后立即执行，执行在ui线程，
        // 一般用来在执行后台任务前会UI做一些标记
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
//            progressDialog.show();
            getG().createFloatView(VersionCheck.this, v);
        }
        // doInBackground这个方法在onPreExecute()完成后立即执行，
        // 用于执行较为耗时的操作，
        // 此方法接受输入参数
        // 和返回计算结果（返回的计算结果将作为参数在任务完成是传递到onPostExecute(Result result)中），
        // 在执行过程中可以调用publishProgress(Progress... values)来更新进度信息
        //后台任务的代码块
        long total = 0;
        int fileLength=0;
        @Override
        protected String doInBackground(String... url) {
            if (v == null) {
                v = GolbalView.getLF(VersionCheck.this).inflate(R.layout.loading, null);
            }
            bar = (ProgressBar) v.findViewById(R.id.progress_version);
            percentText = (TextView) v.findViewById(R.id.percentText);
            System.out.println(percentText.getText().toString()+"");
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL urll=new URL(url[0]);
                Log.d("upgrade","url1:"+urll+"////url:"+url);
                connection = (HttpURLConnection) urll.openConnection();
                connection.connect();
                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }
                // this will be useful to display download percentage
                // might be -1: server did not report the length
                 fileLength = connection.getContentLength();
                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(getDownloadPath() + "/PDA_Version" + "/"  +"PDA" + ".apk");
                byte data[] = new byte[4096];

                int count;
                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        //在调用这个方法后，执行onProgressUpdate(Progress... values)，
                        //运行在主线程，用来更新pregressbar
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
//                    percentText.setText((int) (total * 100 / fileLength)+"");
//                    bar.setProgress((int) (total * 100 / fileLength));
                    System.out.println((int) (total * 100 / fileLength)+"进度条");

                }
            } catch (Exception e) {
                System.out.println(e.toString()+"异常");
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();

            }

            return null;
        }
        //onProgressUpdate(Progress... values),
        // 执行在UI线程，在调用publishProgress(Progress... values)时，此方法被执行。
        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
//            progressDialog.setIndeterminate(false);
//            progressDialog.setMax(100);
//            progressDialog.setProgress(progress[0]);
            if (v == null) {
                v = GolbalView.getLF(VersionCheck.this).inflate(R.layout.loading, null);
            }
            bar = (ProgressBar) v.findViewById(R.id.progress_version);
            percentText = (TextView) v.findViewById(R.id.percentText);
            percentText.setText( ""+total * 100 / fileLength+"%");
            bar.setProgress((int) total * 100 / fileLength);
            getG().createFloatView(VersionCheck.this, v);
        }

        //onPostExecute(Result result),
        // 执行在UI线程，当后台操作结束时，此方法将会被调用。
        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            progressDialog.dismiss();
            if (result != null){
                System.out.println(result);
                Toast.makeText(context,"下载异常: "+result, Toast.LENGTH_LONG).show();
            }else

            {
//                Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
            }

//            installapktest();
////这里主要是做下载后自动安装的处理
//            File file=new File(getDownloadPath() + "/PDA_Version" + "/"+"PDA" + ".apk");
//            Intent installIntent = new Intent(Intent.ACTION_VIEW);
//            installIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//            installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(installIntent);
            getG().removeV(v);
            installAPK();
        }

    }

    private  String downpath="";
    public String  downloadAPK() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(GApplication.netState.equals("13")){//  是3G

                    try {
                    String     downxml = new PDAVersionInfoPathService().getpath();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if (GApplication.netState.equals("11")){//  shi是wifi
                    try {
                        String     downxml  = new PDAVersionInfoPathService().getpathBYWiFi();
                        LoadInfo loadInfo=new LoadInfo();
                    if(loadInfo.loadInfo(downxml)){
                        downpath=   VersionInfo.URL;
                    }else{

                    };

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        }).start();
        return downpath;
    }

}