package com.xlm.meishichina.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;

import org.holoeverywhere.HoloEverywhere;
import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.ThemeManager;
import org.holoeverywhere.app.Application;
import org.holoeverywhere.preference.PreferenceManager;
import org.holoeverywhere.preference.SharedPreferences;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.xlm.meishichina.R;
import com.xlm.meishichina.bean.RandomRecipeInfo;
import com.xlm.meishichina.bean.UserInfo;
import com.xlm.meishichina.db.FinalDb;
import com.xlm.meishichina.http.HttpRequest;
import com.xlm.meishichina.image.cache.disc.impl.UnlimitedDiscCache;
import com.xlm.meishichina.image.cache.memory.MemoryCacheAware;
import com.xlm.meishichina.image.cache.memory.impl.LRULimitedMemoryCache;
import com.xlm.meishichina.image.cache.memory.impl.LruMemoryCache;
import com.xlm.meishichina.image.core.ImageLoader;
import com.xlm.meishichina.image.core.ImageLoaderConfiguration;
import com.xlm.meishichina.image.core.assist.QueueProcessingType;
import com.xlm.meishichina.image.core.download.HttpClientImageDownloader;
import com.xlm.meishichina.image.utils.StorageUtils;
import com.xlm.meishichina.util.MeishiConfig;
import com.xlm.meishichina.util.StringUtil;
import com.xlm.meishichina.widget.imageviewtouch.ImageViewTouch;
import com.xlm.meishichina.widget.viewpagerindicator.TitlePageIndicator;

public class MeishiApplication extends Application implements MeishiConfig
{
    private static MeishiApplication mApplication = null;
    private File cacheDir;
    private UserInfo mUser;
    private FinalDb mFinalDb;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mApplication = this;
        initHoloEvery();
        initImageLoader(getApplicationContext());
        mFinalDb = FinalDb.create(getApplicationContext(), IS_DEBUG);
    }

    public static MeishiApplication getApplication()
    {
        return mApplication;
    }

    public FinalDb getFinalDb()
    {
        return mFinalDb;
    }

    /**
     * 
     * @Title: initImageLoader
     * @Description: ��ʼ��ImageLoader
     * @param @param context
     */
    public void initImageLoader(Context context)
    {
        cacheDir = StorageUtils.getCacheDirectory(mApplication);
        int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        MemoryCacheAware<String, Bitmap> memoryCache;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
        {
            memoryCache = new LruMemoryCache(memoryCacheSize);
        }
        else
        {
            memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
        }
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .discCache(new UnlimitedDiscCache(cacheDir))
                .imageDownloader(
                        new HttpClientImageDownloader(context, HttpRequest
                                .getInstance().httpClient.getHttpClient()))
                .memoryCache(memoryCache).denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .enableLogging(IS_DEBUG).build();

        ImageLoader.getInstance().init(config);
    }

    public void initHoloEvery()
    {
        HoloEverywhere.DEBUG = IS_DEBUG;
        LayoutInflater.remap(ImageViewTouch.class);
        LayoutInflater.remap(TitlePageIndicator.class);
        String value = PreferenceManager.getDefaultSharedPreferences(
                MeishiApplication.getApplication()).getString(CONFIG_SP_THEME,
                "1");
        if (value.equals("1"))
        {
            ThemeManager.setDefaultTheme(ThemeManager.LIGHT);
        }
        else if (value.equals("2"))
        {
            ThemeManager.setDefaultTheme(ThemeManager.DARK);
        }
        ThemeManager.map(ThemeManager.LIGHT, R.style.Holo_meishichina_Theme);
        ThemeManager
                .map(ThemeManager.DARK, R.style.Holo_meishichina_Theme_dark);
    }

    public UserInfo getUserInfo()
    {
        return mUser;
    }

    public void setUser(UserInfo user)
    {
        mUser = user;
    }

    public void clearUser()
    {
        if (mUser != null)
        {
            mUser.clearUserInfo();
        }
    }

    /**
     * 
     * @Title: getSharedPreferences
     * @Description: ��ȡͳһ��SharedPreferences
     * @param @param context
     * @param @return
     */
    public SharedPreferences getSharedPreferences()
    {
        return PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
    }

    public boolean isShowPic()
    {
        return !getSharedPreferences().getBoolean(
                MeishiConfig.CONFIG_SP_DOWN_PIC, false);
    }

    /**
     * ��ȡAppΨһ��ʶ
     * 
     * @return
     */
    public String getAppId()
    {
        String uniqueID = getProperty(CONFIG_UNIQUEID);
        if (StringUtil.isEmpty(uniqueID))
        {
            uniqueID = UUID.randomUUID().toString();
            setProperty(CONFIG_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }

    /**
     * 
     * @Title: getTelphoneModels
     * @Description: ��ȡ�ֻ���model
     * @param @param context
     * @param @return
     */
    public String getTelphoneModels()
    {
        String model = android.os.Build.MODEL;
        return model;
    }

    /**
     * ת���ļ���С
     * 
     * @param fileS
     * @return B/KB/MB/GB
     */
    public String formatFileSize(long fileS)
    {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024)
        {
            fileSizeString = df.format((double) fileS) + "B";
        }
        else if (fileS < 1048576)
        {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        }
        else if (fileS < 1073741824)
        {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        }
        else
        {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * ��ȡĿ¼�ļ���С
     * 
     * @param dir
     * @return
     */
    public long getDirSize(File dir)
    {
        if (dir == null)
        {
            return 0;
        }
        if (!dir.isDirectory())
        {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files)
        {
            if (file.isFile())
            {
                dirSize += file.length();
            }
            else if (file.isDirectory())
            {
                dirSize += file.length();
                dirSize += getDirSize(file); // �ݹ���ü���ͳ��
            }
        }
        return dirSize;
    }

    /**
     * ��ȡĿ¼�ļ�����
     * 
     * @param f
     * @return
     */
    public long getFileList(File dir)
    {
        long count = 0;
        File[] files = dir.listFiles();
        count = files.length;
        for (File file : files)
        {
            if (file.isDirectory())
            {
                count = count + getFileList(file);// �ݹ�
                count--;
            }
        }
        return count;
    }

    /**
     * ���app����
     */
    public void clearAppCache()
    {
        MeishiConfig.mImageLoader.clearMemoryCache();
        MeishiConfig.mImageLoader.clearDiscCache();
        // ������ݻ���
        clearCacheFolder(getFilesDir(), System.currentTimeMillis());
        mFinalDb.deleteByWhere(RandomRecipeInfo.class, "");
    }

    /**
     * �������Ŀ¼
     * 
     * @param dir
     *            Ŀ¼
     * @param numDays
     *            ��ǰϵͳʱ��
     * @return
     */
    private int clearCacheFolder(File dir, long curTime)
    {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory())
        {
            try
            {
                for (File child : dir.listFiles())
                {
                    if (child.isDirectory())
                    {
                        deletedFiles += clearCacheFolder(child, curTime);
                    }
                    if (child.lastModified() < curTime)
                    {
                        if (child.delete())
                        {
                            deletedFiles++;
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    /**
     * ��������Ƿ����
     * 
     * @return
     */
    public boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }


    /**
     * �жϻ����Ƿ����
     * 
     * @param cachefile
     * @return
     */
    public boolean isExistDataCache(String cachefile)
    {
        boolean exist = false;
        File data = getFileStreamPath(cachefile);
        if (data.exists())
            exist = true;
        return exist;
    }


    /**
     * �������
     * 
     * @param ser
     * @param file
     * @throws IOException
     */
    public boolean saveObject(Serializable ser, String file)
    {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try
        {
            fos = openFileOutput(file, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            try
            {
                oos.close();
            }
            catch (Exception e)
            {
            }
            try
            {
                fos.close();
            }
            catch (Exception e)
            {
            }
        }
    }

    /**
     * ��ȡ����
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public Serializable readObject(String file)
    {
        if (!isExistDataCache(file))
            return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try
        {
            fis = openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        }
        catch (FileNotFoundException e)
        {
        }
        catch (Exception e)
        {
            e.printStackTrace();
            // �����л�ʧ�� - ɾ�������ļ�
            if (e instanceof InvalidClassException)
            {
                File data = getFileStreamPath(file);
                data.delete();
            }
        }
        finally
        {
            try
            {
                ois.close();
            }
            catch (Exception e)
            {
            }
            try
            {
                fis.close();
            }
            catch (Exception e)
            {
            }
        }
        return null;
    }

    public boolean containsProperty(String key)
    {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public void setProperties(Properties ps)
    {
        set(ps);
    }

    public Properties getProperties()
    {
        return get();
    }

    public void setProperty(String key, String value)
    {
        set(key, value);
    }

    public String getProperty(String key)
    {
        return get(key);
    }

    public void removeProperty(String... key)
    {
        remove(key);
    }

    public String get(String key)
    {
        Properties props = get();
        return (props != null) ? props.getProperty(key) : null;
    }

    public Properties get()
    {
        FileInputStream fis = null;
        Properties props = new Properties();
        try
        {
            // ��ȡapp_configĿ¼�µ�config
            File dirConf = getApplicationContext().getDir(CONFIG,
                    Context.MODE_PRIVATE);
            fis = new FileInputStream(dirConf.getPath() + File.separator
                    + CONFIG);

            props.load(fis);
        }
        catch (Exception e)
        {
        }
        finally
        {
            try
            {
                fis.close();
            }
            catch (Exception e)
            {
            }
        }
        return props;
    }

    private void setProps(Properties p)
    {
        FileOutputStream fos = null;
        try
        {
            // ��config����filesĿ¼��
            // fos = activity.openFileOutput(APP_CONFIG, Context.MODE_PRIVATE);

            // ��config����(�Զ���)app_config��Ŀ¼��
            File dirConf = getApplicationContext().getDir(CONFIG,
                    Context.MODE_PRIVATE);
            File conf = new File(dirConf, CONFIG);
            fos = new FileOutputStream(conf);

            p.store(fos, null);
            fos.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                fos.close();
            }
            catch (Exception e)
            {
            }
        }
    }

    public void set(Properties ps)
    {
        Properties props = get();
        props.putAll(ps);
        setProps(props);
    }

    public void set(String key, String value)
    {
        Properties props = get();
        props.setProperty(key, value);
        setProps(props);
    }

    public void remove(String... key)
    {
        Properties props = get();
        for (String k : key)
            props.remove(k);
        setProps(props);
    }
}
