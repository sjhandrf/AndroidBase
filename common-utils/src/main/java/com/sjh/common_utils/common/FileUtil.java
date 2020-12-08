package com.sjh.common_utils.common;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lin on 2016/9/26.
 */
public class FileUtil {


    public static final String IMAGE = "image";

    /**
     * 获取文件
     *
     * @return
     */
    public static File getSaveFile(String path) {
        return getSaveFile(path, true);
    }

    /**
     * 获取文件
     *
     * @return
     */
    public static File getSaveFile(String path, boolean cache) {
        String savePath = "";
        if (cache) {
            savePath = getCacheDir(path);
        } else {
            savePath = getExternalDir(path);
        }
        File file = new File(savePath);
        if (file.exists()) {
            return file;
        } else {
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return file;
    }

    public static File getImageFile(String filename) {
        return getSaveFile(IMAGE + File.separator + filename, false);
    }

    /**
     * 获取应用名
     *
     * @return
     */
    public static String getApplicationName() {
        PackageManager manager = null;
        ApplicationInfo info = null;
        try {
            manager = Utils.getContext().getApplicationContext().getPackageManager();
            info = manager.getApplicationInfo(Utils.getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            info = null;
        }
        return (String) manager.getApplicationLabel(info);
    }

    /**
     * 获取存储地址(缓存)
     *
     * @return
     */
    public static String getCacheDir(String path) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = Utils.getContext().getExternalCacheDir().getPath();
        } else {
            cachePath = Utils.getContext().getCacheDir().getPath();
        }
        return cachePath + File.separator + getApplicationName() + File.separator + path;
    }

    /**
     * 获取存储地址
     *
     * @return
     */
    public static String getExternalDir(String path) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            cachePath = Utils.getContext().getFilesDir().getAbsolutePath();
        }
        return cachePath + File.separator + getApplicationName() + File.separator + path;
    }

    public static File getFileWithUri(Uri uri, Context context) {
        String picturePath = null;
        String scheme = uri.getScheme();
        if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            //从系统表中查询指定Uri对应的照片
            Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
            if (cursor == null) {
                return null;
            }
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            if (columnIndex >= 0) {
                picturePath = cursor.getString(columnIndex);  //获取照片路径
            } else {
                picturePath = parseOwnUri(uri, uri.getAuthority());
            }
            cursor.close();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            picturePath = uri.getPath();
        }
        return TextUtils.isEmpty(picturePath) ? null : new File(picturePath);
    }

    private static String parseOwnUri(Uri uri, String provider) {
        if (uri == null) {
            return null;
        }
        return new File(uri.getPath().replace("camera_photos/", "")).getAbsolutePath();
    }

    /**
     * 将得到的一个bitmap保存到sd上，得到一个文件
     */
    public static File saveBitmap(Bitmap bitmap, String folderName, String filename) {
        File image = getImageFile(folderName + File.separator + filename + ".jpg");
//        if (!folder.exists()) {
//            folder.mkdirs();
//        }
//        File image = new File(folder.getAbsolutePath() + File.separator + filename + ".jpg");
        try {
            FileOutputStream outputStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            outputStream.flush();
            outputStream.close();
            return image;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            bitmap.recycle();
        }
    }
}
