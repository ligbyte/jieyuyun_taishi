package com.stkj.cashier.common.storage;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;


import com.stkj.cashier.common.constants.AppCommonConstants;
import com.stkj.cashier.common.core.AppManager;
import com.stkj.cashier.common.storage.callback.CopyCacheFileCallback;
import com.stkj.cashier.common.storage.callback.GetPublicDirFileCallback;
import com.stkj.cashier.common.storage.model.CacheFileInfo;
import com.stkj.cashier.common.storage.model.PublicDirFileInfo;
import com.stkj.cashier.common.utils.AndroidUtils;
import com.stkj.cashier.common.utils.FileUtils;
import com.stkj.cashier.util.rxjava.DefaultObserver;
import com.stkj.cashier.util.rxjava.RxTransformerUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;

public class StorageHelper {

    public static String PUB_DIR_DCIM_PREFIX_TAG = Environment.DIRECTORY_DCIM + "/";
    public static String PUB_DIR_DOWNLOAD_PREFIX_TAG = Environment.DIRECTORY_DOWNLOADS + "/";

    public static String CONTENT_DOCUMENT_BASE_PATH = "content://com.android.externalstorage.documents/document/primary:";

    //缓存文件目录
    public static String getDirCachePath() {
        return "/" + AppCommonConstants.APP_PREFIX_TAG + "cache/";
    }

    //分享文件目录
    public static String getDirSharePath() {
        return "/" + AppCommonConstants.APP_PREFIX_TAG + "share/";
    }

    //下载文件目录
    public static String getDirDownloadPath() {
        return "/" + AppCommonConstants.APP_PREFIX_TAG + "download/";
    }

    //下载目录名称(android 10 +)
    public static String getRelativeExternalDirPath() {
        return "/" + AppCommonConstants.APP_PREFIX_TAG + "/";
    }

    /**
     * 创建分享文件
     *
     * @param shareFileName 文件名字
     */
    public static File createShareFile(String shareFileName) {
        return createExternalFile(getDirSharePath(), shareFileName);
    }

    /**
     * 创建缓存文件
     *
     * @param cacheFileName 文件名字
     */
    public static File createCacheFile(String cacheFileName) {
        return createExternalFile(getDirCachePath(), cacheFileName);
    }

    /**
     * 创建下载文件
     *
     * @param downloadFileName 文件名字
     */
    public static File createDownloadFile(String downloadFileName) {
        return createExternalFile(getDirDownloadPath(), downloadFileName);
    }

    /**
     * 创建外置存储app内的文件 不需要外置存储权限
     *
     * @param parentFilePath 父目录
     * @param fileName       文件名字
     */
    private static File createExternalFile(String parentFilePath, String fileName) {
        String storageFileRootPath = getExternalFileRootPath();
        File parentDir = new File(storageFileRootPath + parentFilePath);
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        File cacheFile = new File(parentDir, fileName);
        return cacheFile;
    }

    /**
     * 获取指定外置目录路径
     */
    public static String getExternalCustomDirPath(String dirName) {
        return getExternalDirPath("/" + dirName + "/");
    }

    /**
     * 获取缓存目录路径
     */
    public static String getExternalCacheDirPath() {
        return getExternalDirPath(getDirCachePath());
    }

    /**
     * 获取分享目录路径
     */
    public static String getExternalShareDirPath() {
        return getExternalDirPath(getDirSharePath());
    }

    /**
     * 获取下载目录路径
     */
    public static String getExternalDownloadDirPath() {
        return getExternalDirPath(getDirDownloadPath());
    }

    /**
     * 获取存储目录
     *
     * @param dirPath /目录名称/
     */
    private static String getExternalDirPath(String dirPath) {
        String storageFileRootPath = getExternalFileRootPath();
        File dir = new File(storageFileRootPath + dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    /**
     * 获取文件 (先获取外置目录[/Android/data/x.x.x/files/]，失败获取app安装文件目录[data/data/x.x.x/files])
     */
    private static String getExternalFileRootPath() {
        File externalFile = AppManager.INSTANCE.getApplication().getExternalFilesDir(null);
        if (externalFile != null) {
            return externalFile.getAbsolutePath();
        } else {
            return AppManager.INSTANCE.getApplication().getFilesDir().getAbsolutePath();
        }
    }

    // ----------- api < android 10  start-------------

    /**
     * 获取系统常用文件夹（下载目录）(api < android 10)
     */
    public static String getPublicDownloadsDirPath() {
        return getPublicDirPath(Environment.DIRECTORY_DOWNLOADS);
    }

    /**
     * 获取系统常用文件夹（相册目录）(api < android 10)
     */
    public static String getPublicDCIMDirPath() {
        return getPublicDirPath(Environment.DIRECTORY_DCIM);
    }

    /**
     * 获取系统常用文件夹
     *
     * @param dirType Environment.DIRECTORY_xxxx
     */
    private static String getPublicDirPath(String dirType) {
        File dcimFile = Environment.getExternalStoragePublicDirectory(dirType);
        File realDir = new File(dcimFile.getAbsolutePath() + getRelativeExternalDirPath());
        if (!realDir.exists()) {
            realDir.mkdirs();
        }
        return realDir.getAbsolutePath();
    }

    // ----------- api < android 10  end-------------


    // ----------- api >= android 10  start-------------

    /**
     * 获取系统常用文件夹（下载目录）(api >= android 10)
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static String getRelativeDownloadsDirPath() {
        return Environment.DIRECTORY_DOWNLOADS + getRelativeExternalDirPath();
    }

    /**
     * 获取系统常用文件夹（相册目录）(api >= android 10)
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static String getRelativeDCIMDirPath() {
        return Environment.DIRECTORY_DCIM + getRelativeExternalDirPath();
    }

    // ----------- api >= android 10  end-------------

    public static String getContentPubDownloadDirPath() {
        return CONTENT_DOCUMENT_BASE_PATH + Environment.DIRECTORY_DOWNLOADS + Uri.encode("/" + AppCommonConstants.APP_PREFIX_TAG);
    }

    public static String getContentPubDCIMDirPath() {
        return CONTENT_DOCUMENT_BASE_PATH + Environment.DIRECTORY_DCIM + Uri.encode("/" + AppCommonConstants.APP_PREFIX_TAG);
    }

    /**
     * 列出保存在公共目录的文件名字(READ_EXTERNAL_STORAGE权限可选，未申请时之前app卸载的文件无法获取)
     * targetsdk 29 android 10以上只能获取本应用的媒体文件，非媒体文件不可以获取（部分厂商手机可以,原生安卓系统不可以）; 可通过SAF框架授权后获取，或者降低targetsdk
     */
    public static int TYPE_PUBLIC_DIR_ALL = 0;
    public static int TYPE_PUBLIC_DIR_DOWNLOAD = 1;
    public static int TYPE_PUBLIC_DIR_DCIM = 2;

    public static void listPubDirAllFile(GetPublicDirFileCallback fileCallback) {
        Observable.create(new ObservableOnSubscribe<List<PublicDirFileInfo>>() {

                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<PublicDirFileInfo>> emitter) {
                        try {
                            List<PublicDirFileInfo> publicDirFile = listPublicDirFile(TYPE_PUBLIC_DIR_ALL);
                            emitter.onNext(publicDirFile);
                            emitter.onComplete();
                        } catch (Throwable e) {
                            Log.e("TAG", "limeException 240: " + e.getMessage());
                            emitter.onError(e);
                        }
                    }
                }).compose(RxTransformerUtils.mainSchedulers())
                .subscribe(new DefaultObserver<List<PublicDirFileInfo>>() {
                    @Override
                    protected void onSuccess(List<PublicDirFileInfo> publicDirFileInfos) {
                        if (fileCallback != null) {
                            fileCallback.onSuccess(publicDirFileInfos);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (fileCallback != null) {
                            fileCallback.onError(e.getMessage());
                        }
                    }
                });
    }

    public static void listPubDownloadDirFile(GetPublicDirFileCallback fileCallback) {
        Observable.create(new ObservableOnSubscribe<List<PublicDirFileInfo>>() {

                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<PublicDirFileInfo>> emitter) {
                        try {
                            List<PublicDirFileInfo> publicDirFile = listPublicDirFile(TYPE_PUBLIC_DIR_DOWNLOAD);
                            emitter.onNext(publicDirFile);
                            emitter.onComplete();
                        } catch (Throwable e) {
                            Log.e("TAG", "limeException 272: " + e.getMessage());
                            emitter.onError(e);
                        }
                    }
                }).compose(RxTransformerUtils.mainSchedulers())
                .subscribe(new DefaultObserver<List<PublicDirFileInfo>>() {
                    @Override
                    protected void onSuccess(List<PublicDirFileInfo> publicDirFileInfos) {
                        if (fileCallback != null) {
                            fileCallback.onSuccess(publicDirFileInfos);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (fileCallback != null) {
                            fileCallback.onError(e.getMessage());
                        }
                    }
                });
    }

    public static void listPubDCIMDirFile(GetPublicDirFileCallback fileCallback) {
        Observable.create(new ObservableOnSubscribe<List<PublicDirFileInfo>>() {

                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<PublicDirFileInfo>> emitter) {
                        try {
                            List<PublicDirFileInfo> publicDirFile = listPublicDirFile(TYPE_PUBLIC_DIR_DCIM);
                            emitter.onNext(publicDirFile);
                            emitter.onComplete();
                        } catch (Throwable e) {
                            Log.e("TAG", "limeException 304: " + e.getMessage());
                            emitter.onError(e);
                        }
                    }
                }).compose(RxTransformerUtils.mainSchedulers())
                .subscribe(new DefaultObserver<List<PublicDirFileInfo>>() {
                    @Override
                    protected void onSuccess(List<PublicDirFileInfo> publicDirFileInfos) {
                        if (fileCallback != null) {
                            fileCallback.onSuccess(publicDirFileInfos);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (fileCallback != null) {
                            fileCallback.onError(e.getMessage());
                        }
                    }
                });
    }

    public static List<PublicDirFileInfo> listPublicDirFile(int publicDirType) {
        List<PublicDirFileInfo> pubDirFileList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver contentResolver = AppManager.INSTANCE.getApplication().getContentResolver();
            Uri publicDirUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
            String[] projection = new String[]{MediaStore.Files.FileColumns._ID,
                    MediaStore.Files.FileColumns.DISPLAY_NAME,
                    MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Files.FileColumns.RELATIVE_PATH,
                    MediaStore.Files.FileColumns.DATA};
            Cursor cursor = contentResolver.query(
                    publicDirUri,
                    projection,
                    null,
                    null, null
            );
            if (cursor != null) {
                int fileIdIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID);
                int fileNameIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME);
                int dirNameIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME);
                int relativePathIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.RELATIVE_PATH);
                int dataIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
                while (cursor.moveToNext()) {
                    //文件目录名
                    String dirName = cursor.getString(dirNameIndex);
                    if (TextUtils.equals(dirName, AppCommonConstants.APP_PREFIX_TAG)) {
                        //文件名
                        String fileName = cursor.getString(fileNameIndex);
                        //文件id
                        long fileId = cursor.getLong(fileIdIndex);
                        //文件相对路径
                        String relativePath = cursor.getString(relativePathIndex);
                        //文件真实路径
                        String data = cursor.getString(dataIndex);
                        if (publicDirType == TYPE_PUBLIC_DIR_DCIM) {
                            if (relativePath != null && relativePath.contains(PUB_DIR_DCIM_PREFIX_TAG)) {
                                pubDirFileList.add(new PublicDirFileInfo(fileId, fileName, relativePath, data));
                            } else if (data != null && data.contains(PUB_DIR_DCIM_PREFIX_TAG)) {
                                pubDirFileList.add(new PublicDirFileInfo(fileId, fileName, relativePath, data));
                            }
                        } else if (publicDirType == TYPE_PUBLIC_DIR_DOWNLOAD) {
                            if (relativePath != null && relativePath.contains(PUB_DIR_DOWNLOAD_PREFIX_TAG)) {
                                pubDirFileList.add(new PublicDirFileInfo(fileId, fileName, relativePath, data));
                            } else if (data != null && data.contains(PUB_DIR_DOWNLOAD_PREFIX_TAG)) {
                                pubDirFileList.add(new PublicDirFileInfo(fileId, fileName, relativePath, data));
                            }
                        } else {
                            pubDirFileList.add(new PublicDirFileInfo(fileId, fileName, relativePath, data));
                        }
                    }
                }
                cursor.close();
            }
        } else {
            if (publicDirType == TYPE_PUBLIC_DIR_DCIM) {
                pubDirFileList.addAll(listPublicDirFileBelowQ(TYPE_PUBLIC_DIR_DCIM));
            } else if (publicDirType == TYPE_PUBLIC_DIR_DOWNLOAD) {
                pubDirFileList.addAll(listPublicDirFileBelowQ(TYPE_PUBLIC_DIR_DOWNLOAD));
            } else {
                pubDirFileList.addAll(listPublicDirFileBelowQ(TYPE_PUBLIC_DIR_DCIM));
                pubDirFileList.addAll(listPublicDirFileBelowQ(TYPE_PUBLIC_DIR_DOWNLOAD));
            }
        }
        Log.i("StorageHelper", "PublicDirFileList size: " + pubDirFileList.size() + " fileNameList: " + Arrays.toString(pubDirFileList.toArray()));
        return pubDirFileList;
    }

    public static List<PublicDirFileInfo> listPublicDirFileBelowQ(int publicDirType) {
        List<PublicDirFileInfo> pubDirFileList = new ArrayList<>();
        String publicDirPath;
        String relativePath;
        if (publicDirType == TYPE_PUBLIC_DIR_DCIM) {
            publicDirPath = getPublicDCIMDirPath();
            relativePath = PUB_DIR_DCIM_PREFIX_TAG + AppCommonConstants.APP_PREFIX_TAG;
        } else {
            publicDirPath = getPublicDownloadsDirPath();
            relativePath = PUB_DIR_DOWNLOAD_PREFIX_TAG + AppCommonConstants.APP_PREFIX_TAG;
        }
        File downloadDir = new File(publicDirPath);
        if (downloadDir.exists() && downloadDir.isDirectory()) {
            File[] listFiles = downloadDir.listFiles();
            if (listFiles != null) {
                for (File file : listFiles) {
                    pubDirFileList.add(new PublicDirFileInfo(-1, file.getName(), relativePath, file.getAbsolutePath()));
                }
            }
        }
        return pubDirFileList;
    }

    public static void copyUriToCacheFile(final List<Uri> fileUriList, final CopyCacheFileCallback copyCacheFileCallback) {
        if (fileUriList == null || fileUriList.isEmpty() || copyCacheFileCallback == null) {
            return;
        }
        Observable.create(new ObservableOnSubscribe<List<CacheFileInfo>>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<CacheFileInfo>> emitter) {
                        final List<CacheFileInfo> cacheFileInfoList = new ArrayList<>();
                        for (int i = 0; i < fileUriList.size(); i++) {
                            Uri viewFileUri = fileUriList.get(i);
                            String cacheFileName = "cache_" + System.currentTimeMillis();
                            File cacheFile = StorageHelper.createCacheFile(cacheFileName);
                            String fileRealNameFromUri = AndroidUtils.getFileRealNameFromUri(viewFileUri);
                            if (TextUtils.isEmpty(fileRealNameFromUri)) {
                                fileRealNameFromUri = cacheFileName;
                            }
                            try {
                                FileInputStream fileInputStream = (FileInputStream) AppManager.INSTANCE.getApplication().getContentResolver().openInputStream(viewFileUri);
                                boolean copyFileToOtherFile = FileUtils.copyFileToOtherFile(fileInputStream, cacheFile);
                                if (copyFileToOtherFile) {
                                    CacheFileInfo cacheFileInfo = new CacheFileInfo(cacheFile);
                                    cacheFileInfo.setOriginFileUri(viewFileUri);
                                    cacheFileInfo.setOriginFileName(fileRealNameFromUri);
                                    cacheFileInfoList.add(cacheFileInfo);
                                } else {
                                }
                                emitter.onNext(cacheFileInfoList);
                            } catch (Throwable e) {
                                Log.e("TAG", "limeException 444: " + e.getMessage());
                                //delete all cache files
                                try {
                                    cacheFile.delete();
                                    for (int j = 0; j < cacheFileInfoList.size(); j++) {
                                        File file = cacheFileInfoList.get(i).getFile();
                                        file.delete();
                                    }
                                } catch (Throwable delError) {
                                    Log.e("TAG", "limeException 453: " + e.getMessage());
                                }
                                emitter.onError(e);
                            }
                        }
                    }
                }).compose(RxTransformerUtils.mainSchedulers())
                .subscribe(new DefaultObserver<List<CacheFileInfo>>() {
                    @Override
                    protected void onSuccess(List<CacheFileInfo> files) {
                        copyCacheFileCallback.onSuccess(files);
                    }

                    @Override
                    public void onError(Throwable e) {
                        copyCacheFileCallback.onError(e.getMessage());
                    }
                });
    }

}
