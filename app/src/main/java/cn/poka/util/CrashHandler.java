package cn.poka.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.application.GApplication;

/**
 * 异常捕捉器
 */
public class CrashHandler implements UncaughtExceptionHandler {
    //	/**
//	 * Debug Log Tag
//	 */
//	public static final String TAG = "CrashHandler";
//	/**
//	 * CrashHandler实例
//	 */
//	private static CrashHandler INSTANCE;
//	/**
//	 * 程序的Context对象
//	 */
//	private Context mContext;
//
//	private UncaughtExceptionHandler mDefaultHandler;
//
//	private StringBuffer mErrorLogBuffer = new StringBuffer();
//
//	private static final String SINGLE_RETURN = "\n";
//
//	private static final String SINGLE_LINE = "----------------split line---------------";
//
//	/**
//	 * 保证只有一个CrashHandler实例
//	 */
//	private CrashHandler() {
//	}
//
//	/**
//	 * 获取CrashHandler实例 ,单例模式
//	 *
//	 * @return
//	 */
//	public static CrashHandler getInstance() {
//		if (INSTANCE == null) {
//			synchronized (CrashHandler.class) {
//				INSTANCE = new CrashHandler();
//			}
//		}
//		return INSTANCE;
//	}
//
//	/**
//	 * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
//	 *
//	 * @param ctx
//	 */
//	public void init(Context ctx) {
//		mContext = ctx;
//		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
//		Thread.setDefaultUncaughtExceptionHandler(this);
//	}
//
//	/**
//	 * 当UncaughtException发生时会转入该函数来处理
//	 */
//	public void uncaughtException(Thread thread, Throwable ex) {
//		final String msg = ex.getLocalizedMessage();
//		// begin ：注释此段代码，不使用toast提示异常改为log文件方式 modify by liuchang 2017-04-29
//		// // 使用Toast来显示异常信息
//		// new Thread() {
//		// @Override
//		// public void run() {
//		// // Toast 显示需要出现在一个线程的消息队列中
//		// Looper.prepare();
//		// Toast.makeText(mContext, "系统异常:" + msg, Toast.LENGTH_SHORT)
//		// .show();
//		// Looper.loop();
//		// }
//		// }.start();
//		//
//		// try {
//		// Thread.sleep(3000);
//		// } catch (InterruptedException e) {
//		// System.out.println(e.getLocalizedMessage());
//		// }
//		// android.os.Process.killProcess(android.os.Process.myPid());
//		// System.exit(10);
//		// end
//		if (!handleException(ex) && mDefaultHandler != null) {
//			// 如果用户没有处理异常就由系统默认的异常处理器来处理
//			mDefaultHandler.uncaughtException(thread, ex);
//		} else {
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			System.exit(1);
//			// android.os.Process.killProcess(android.os.Process.myPid());
//		}
//
//	}
//
//	/**
//	 *
//	 * @param ex
//	 * @return
//	 */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉,现钞管理系统停止运行,即将退出.", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();

//		// 收集设备参数信息
//		collectDeviceInfo(mContext);
//		// 收集错误日志
//		collectCrashInfo(ex);
//		// 保存错误日志
//		saveErrorLog();

        return true;
    }

    //
//	/**
//	 * 保存日志到/sdcard/PokaAppLog/目录下，文件名已日期的形式保存
//	 */
//	private void saveErrorLog() {
//		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//			String format = sdf.format(new Date());
//			format += ".log";
//			String path = Environment.getExternalStorageDirectory().getPath() + "/PokaAppLog/";
//			File file = new File(path);
//			if (!file.exists()) {
//				file.mkdirs();
//			}
//
//			FileOutputStream fos = null;
//			try {
//				fos = new FileOutputStream(path + format, true);
//				fos.write(mErrorLogBuffer.toString().getBytes());
//				fos.flush();
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				if (fos != null) {
//					try {
//						fos.close();
//						fos = null;
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//	}
//
//	/**
//	 * 收集错误信息
//	 *
//	 * @param ex
//	 */
//	private void collectCrashInfo(Throwable ex) {
//		Writer info = new StringWriter();
//		PrintWriter printWriter = new PrintWriter(info);
//		ex.printStackTrace(printWriter);
//
//		Throwable cause = ex.getCause();
//		while (cause != null) {
//			cause.printStackTrace(printWriter);
//			cause = cause.getCause();
//		}
//
//		String result = info.toString();
//		printWriter.close();
//
//		// 将错误信息加入mErrorLogBuffer中
//		append("", result);
//		mErrorLogBuffer.append(SINGLE_RETURN + SINGLE_LINE + SINGLE_RETURN);
//
//		Log.d(TAG, "saveCrashInfo2File:" + mErrorLogBuffer.toString());
//	}
//
//	/**
//	 * 收集应用和设备信息
//	 *
//	 * @param context
//	 */
//	private void collectDeviceInfo(Context context) {
//		// 每次使用前，清掉mErrorLogBuffer里的内容
//		mErrorLogBuffer.setLength(0);
//		mErrorLogBuffer.append(SINGLE_RETURN + SINGLE_LINE + SINGLE_RETURN);
//
//		// 获取应用的信息
//		PackageManager pm = context.getPackageManager();
//		try {
//			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
//			if (pi != null) {
//				append("versionCode", pi.versionCode);
//				append("versionName", pi.versionName);
//				append("packageName", pi.packageName);
//			}
//		} catch (NameNotFoundException e) {
//			e.printStackTrace();
//		}
//
//		mErrorLogBuffer.append(SINGLE_RETURN + SINGLE_LINE + SINGLE_RETURN);
//
//		// 获取设备的信息
//		Field[] fields = Build.class.getDeclaredFields();
//		getDeviceInfoByReflection(fields);
//
//		fields = Build.VERSION.class.getDeclaredFields();
//		getDeviceInfoByReflection(fields);
//
//		mErrorLogBuffer.append(SINGLE_RETURN + SINGLE_LINE + SINGLE_RETURN);
//	}
//
//	/**
//	 * 获取设备的信息通过反射方式
//	 *
//	 * @param fields
//	 */
//	private void getDeviceInfoByReflection(Field[] fields) {
//		for (Field field : fields) {
//			try {
//				// 对private成员变量设置可访问
//				field.setAccessible(true);
//				append(field.getName(), field.get(null));
//			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	// mErrorLogBuffer添加友好的log信息
//	private void append(String key, Object value) {
//		mErrorLogBuffer.append("" + key + ":" + value + SINGLE_RETURN);
//	}
    private static CrashHandler INSTANCE;
    private static Context mContext;

    private static PendingIntent restartIntent;

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private CrashUploader crashUploader;

    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    public static final String EXCEPTION_INFO = "EXCEPTION_INFO";
    public static final String PACKAGE_INFO = "PACKAGE_INFO";
    public static final String DEVICE_INFO = "DEVICE_INFO";
    public static final String SYSTEM_INFO = "SYSTEM_INFO";
    public static final String SECURE_INFO = "SECURE_INFO";
    public static final String MEM_INFO = "MEM_INFO";


    private String mExceptionInfo;
    private String mMemInfo;

    private ConcurrentHashMap<String, String> mPackageInfo = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, String> mDeviceInfo = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, String> mSystemInfo = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, String> mSecureInfo = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, Object> totalInfo = new ConcurrentHashMap<>();


    private CrashHandler() {

    }

    public static CrashHandler getInstance() {
        if (INSTANCE == null) {
            synchronized (CrashHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CrashHandler();
                }
            }
        }
        return INSTANCE;
    }

    public void init(Context context, CrashUploader crashUploader, PendingIntent pendingIntent) {
        mContext = context;
        this.crashUploader = crashUploader;
        this.restartIntent = pendingIntent;
        //保存一份系统默认的CrashHandler
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //使用我们自定义的异常处理器替换程序默认的
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * @param t 出现未捕获异常的线程
     * @param e 未捕获的异常，有了这个ex，我们就可以得到异常信息
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!catchCrashException(e) && mDefaultHandler != null) {
            //没有自定义的CrashHandler的时候就调用系统默认的异常处理方式
            mDefaultHandler.uncaughtException(t, e);
            if (!handleException(e) && mDefaultHandler != null) {
//			// 如果用户没有处理异常就由系统默认的异常处理器来处理
                mDefaultHandler.uncaughtException(t, e);
            } else {
                try {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            }
        } else {
            //退出应用
            killProcess();
        }
    }


    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex 未捕获的异常
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean catchCrashException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉,现钞管理系统停止运行,即将退出.", 500).show();
                Looper.loop();
            }
        }.start();

        collectInfo(ex);
        //保存日志文件
        saveCrashInfo2File();
        //上传崩溃信息
//        uploadCrashMessage(totalInfo);

        return true;
    }

    /**
     * 退出应用
     */
    private static void killProcess() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Log.e("application", "error : ", e);
        }
        // 退出程序
//		AlarmManager mgr = (AlarmManager)GApplication.getApplication().getSystemService(Context.ALARM_SERVICE);// 加入后报错ANR
//		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, restartIntent); // 2秒钟后重启应用
//        Process.killProcess(Process.myPid());
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }


    /**
     * 获取异常信息和设备参数信息
     */
    private void collectInfo(Throwable ex) {
        mExceptionInfo = collectExceptionInfo(ex);
        collectPackageInfo();
        collectBuildInfos();
        collectSystemInfos();
        collectSecureInfo();
        mMemInfo = collectMemInfo();

        totalInfo.put(EXCEPTION_INFO, mExceptionInfo);
        totalInfo.put(PACKAGE_INFO, mPackageInfo);
        totalInfo.put(DEVICE_INFO, mDeviceInfo);
        totalInfo.put(SYSTEM_INFO, mSecureInfo);
        totalInfo.put(SECURE_INFO, mSecureInfo);
        totalInfo.put(MEM_INFO, MEM_INFO);
    }

    /**
     * 获取捕获异常的信息
     */
    private String collectExceptionInfo(Throwable ex) {
        Writer mWriter = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(mWriter);
        ex.printStackTrace(mPrintWriter);
        ex.printStackTrace();
        Throwable mThrowable = ex.getCause();
        // 迭代栈队列把所有的异常信息写入writer中
        while (mThrowable != null) {
            mThrowable.printStackTrace(mPrintWriter);
            // 换行 每个个异常栈之间换行
            mPrintWriter.append("\r\n");
            mThrowable = mThrowable.getCause();
        }
        // 记得关闭
        mPrintWriter.close();
        return mWriter.toString();
    }

    /**
     * 获取应用包参数信息
     */
    private void collectPackageInfo() {
        try {
            // 获得包管理器
            PackageManager mPackageManager = mContext.getPackageManager();
            // 得到该应用的信息，即主Activity
            PackageInfo mPackageInfo = mPackageManager.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (mPackageInfo != null) {
                String versionName = mPackageInfo.versionName == null ? "null" : mPackageInfo.versionName;
                String versionCode = mPackageInfo.versionCode + "";
                this.mPackageInfo.put("VersionName", versionName);
                this.mPackageInfo.put("VersionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从系统属性中提取设备硬件和版本信息
     */
    private void collectBuildInfos() {
        // 反射机制
        Field[] mFields = Build.class.getDeclaredFields();
        // 迭代Build的字段key-value 此处的信息主要是为了在服务器端手机各种版本手机报错的原因
        for (Field field : mFields) {
            try {
                field.setAccessible(true);
                mDeviceInfo.put(field.getName(), field.get("").toString());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取系统常规设定属性
     */
    private void collectSystemInfos() {
        Field[] fields = Settings.System.class.getFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Deprecated.class)
                    && field.getType() == String.class) {
                try {
                    String value = Settings.System.getString(mContext.getContentResolver(), (String) field.get(null));
                    if (value != null) {
                        mSystemInfo.put(field.getName(), value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 获取系统安全设置信息
     */
    private void collectSecureInfo() {
        Field[] fields = Settings.Secure.class.getFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Deprecated.class)
                    && field.getType() == String.class
                    && field.getName().startsWith("WIFI_AP")) {
                try {
                    String value = Settings.Secure.getString(mContext.getContentResolver(), (String) field.get(null));
                    if (value != null) {
                        mSecureInfo.put(field.getName(), value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 获取内存信息
     */
    private String collectMemInfo() {
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();

        ArrayList<String> commandLine = new ArrayList<>();
        commandLine.add("cat");
        commandLine.add("/proc/meminfo");
//        commandLine.add(Integer.toString(Process.myPid()));

        try {
            java.lang.Process process = Runtime.getRuntime()
                    .exec(commandLine.toArray(new String[commandLine.size()]));
            br = new BufferedReader(new InputStreamReader(process.getInputStream()), 1024);

            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 将崩溃日志信息写入本地文件
     */
    private String saveCrashInfo2File() {
        StringBuffer mStringBuffer = getInfoStr(mPackageInfo);
        mStringBuffer.append(mExceptionInfo);
        // 保存文件，设置文件名
        String mTime = formatter.format(new Date());
        String mFileName = "crash-" + mTime + ".txt";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                File mDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/hive-crash");
                if (!mDirectory.exists()) {
                    boolean success = mDirectory.mkdirs();
                }
                FileOutputStream mFileOutputStream = new FileOutputStream(mDirectory + File.separator + mFileName);
                mFileOutputStream.write(mStringBuffer.toString().getBytes());
                mFileOutputStream.close();
                return mFileName;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将HashMap遍历转换成StringBuffer
     */
    @NonNull
    private static StringBuffer getInfoStr(ConcurrentHashMap<String, String> info) {
        StringBuffer mStringBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : info.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            mStringBuffer.append(key + "=" + value + "\r\n");
        }
        return mStringBuffer;
    }

    /**
     * 上传崩溃信息到服务器
     */
    private void uploadCrashMessage(ConcurrentHashMap<String, Object> info) {
        crashUploader.uploadCrashMessage(info);
    }

    /**
     * 崩溃信息上传接口回调
     */
    public interface CrashUploader {
        void uploadCrashMessage(ConcurrentHashMap<String, Object> info);
    }
}