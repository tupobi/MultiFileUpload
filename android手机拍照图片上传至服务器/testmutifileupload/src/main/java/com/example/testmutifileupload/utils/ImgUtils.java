package com.example.testmutifileupload.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Administrator on 2017/10/15.
 */

public class ImgUtils {
    //保存文件到指定路径
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory() + File.separator + "183" + File.separator + "img";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            if (fos == null || bmp == null) {
                return false;
            }
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //保存文件到指定路径
    public static boolean saveImageToFile(final Context context, String sourcePhotoPath) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory() + File.separator + "183" + File.separator + "img";
        final File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        File sourcePhotoFile = new File(sourcePhotoPath);
        final String fileName = "compress_" + System.currentTimeMillis() + ".jpg";
        final File photo = new File(appDir, fileName);

        final boolean[] isSuccess = {false};
        Luban.with(context)
                .load(sourcePhotoFile)                                     // 传人要压缩的图片列表
                .ignoreBy(100)                                            // 忽略不压缩图片的大小
                .setTargetDir(appDir.getAbsolutePath())                   // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() {             //设置回调
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        LogUtil.e("OK!");
                        isSuccess[0] = true;
                        file.renameTo(photo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        isSuccess[0] = false;
                        LogUtil.e(e.getMessage());
                    }
                }).launch();    //启动压缩

        //保存图片后发送广播通知更新数据库
        Uri uri = Uri.fromFile(new File(photo.getAbsolutePath()));
        LogUtil.e("----------------保存至系统图库" + photo.getAbsolutePath());
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        return isSuccess[0];
    }

    public static File compressImageAndSave2Local(final Context context, String sourcePhotoPath, String savePhotoPath) {
        final File savePhoto = new File(savePhotoPath);
        if (!savePhoto.exists()) {
            savePhoto.mkdirs();
        }
        final String photoName = "compress_" + System.currentTimeMillis() + ".jpg";
        final File photoFile = new File(savePhoto, photoName);
        Luban.with(context)
                .load(new File(sourcePhotoPath))                          // 传人要压缩的图片列表
                .ignoreBy(100)                                            // 忽略不压缩图片的大小
                .setTargetDir(savePhoto.getAbsolutePath())                   // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() {             //设置回调
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        LogUtil.e("OK!");
                        file.renameTo(photoFile);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                    }
                }).launch();    //启动压缩
        return photoFile;
    }

    public static void saveToAlbum(Context context, File photoFile){
        Uri uri = Uri.fromFile(photoFile);
        LogUtil.e("----------------保存至系统图库" + photoFile.getAbsolutePath());
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
    }
}
