package com.example.testmutifileupload;

import android.Manifest;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.testmutifileupload.bean.Goods;
import com.example.testmutifileupload.utils.ImgUtils;
import com.example.testmutifileupload.utils.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;

    private EditText etGoodsName;
    private EditText etGoodsPrice;
    private EditText etGoodsDes;
    private ImageView ivGoods;
    private Button btnUploadPhoto;
    private Button btnSubmit;
    private Goods goods;

    private Uri imageUri;
    private PopupWindow popupWindow;
    private float alpha = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    backgroundAlpha((float) msg.obj);
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (v == btnUploadPhoto) {
            bottomwindow(btnUploadPhoto);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (alpha > 0.5f) {
                        try {
                            //4是根据弹出动画时间和减少的透明度计算
                            Thread.sleep(4);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        //每次减少0.01，精度越高，变暗的效果越流畅
                        alpha -= 0.01f;
                        msg.obj = alpha;
                        mHandler.sendMessage(msg);
                    }
                }
            }).start();
        } else if (v == btnSubmit) {
            goods.setGoodsName(etGoodsName.getText().toString());
            goods.setGoodsDes(etGoodsDes.getText().toString());
            try {
                goods.setGoodsPrice(Double.parseDouble(etGoodsPrice.getText().toString()));
            } catch (Exception e) {
                Toast.makeText(this, "请检查价格输入是否符合标准！", Toast.LENGTH_SHORT).show();
                return;
            }

            if (goods.getGoodsName() != null && goods.getGoodsDes() != null && goods.getGoodsPrice() != 0.0 && goods.getGoodsImgUri() != null) {
                Map params = new HashMap();
                params.put("goodsName", goods.getGoodsName());
                params.put("goodsDes", goods.getGoodsDes());
                params.put("goodsPirce", String.valueOf(goods.getGoodsPrice()));
                OkHttpUtils.post()
                        .addFile("mFile", goods.getGoodsImgUri().substring(goods.getGoodsImgUri().lastIndexOf("\\") + 1), new File(goods.getGoodsImgUri()))
                        .url("http://192.168.1.162:8080/TestMyService/servlet/MultiFileUpload")
                        .params(params)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Toast.makeText(MainActivity.this, "提交失败，请检查网络！", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Toast.makeText(MainActivity.this, "提交成功,请等待审核！", Toast.LENGTH_SHORT).show();

                            }
                        });

            } else {
                Toast.makeText(this, "请检查您的输入是否标准！", Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goods = new Goods();
        initWidget();
    }

    private void initWidget() {
        etGoodsName = (EditText) findViewById(R.id.et_goods_name);
        etGoodsPrice = (EditText) findViewById(R.id.et_goods_price);
        etGoodsDes = (EditText) findViewById(R.id.et_goods_des);
        ivGoods = (ImageView) findViewById(R.id.iv_goods);
        btnUploadPhoto = (Button) findViewById(R.id.btn_upload_photo);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        btnUploadPhoto.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    void bottomwindow(View view) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        RelativeLayout layout = (RelativeLayout) getLayoutInflater().inflate(R.layout.popup_window, null);
        popupWindow = new PopupWindow(layout,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //防止popupWindow被虚拟导航栏遮挡住
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        //添加弹出、弹入的动画
        popupWindow.setAnimationStyle(R.style.Popupwindow);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.showAtLocation(view, Gravity.LEFT | Gravity.BOTTOM, 0, -location[1]);
        //添加按键事件监听
        setButtonListeners(layout);
        //添加pop窗口关闭事件，主要是实现关闭时改变背景的透明度
        popupWindow.setOnDismissListener(new poponDismissListener());
        backgroundAlpha(1f);
    }

    private void setButtonListeners(RelativeLayout layout) {
        Button btn_pop_cancel = layout.findViewById(R.id.btn_pop_cancel);
        btn_pop_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });

        Button btn_pop_camera = layout.findViewById(R.id.btn_pop_camera);
        btn_pop_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }

                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.testmutifileupload.fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                Toast.makeText(MainActivity.this, "请横屏拍摄！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });

        Button btn_pop_album = layout.findViewById(R.id.btn_pop_album);
        btn_pop_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "您拒绝应用使用该项权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.e("进入onActivityResult");
        switch (requestCode) {
            case TAKE_PHOTO:
                LogUtil.e("resultCode == " + resultCode);
                if (resultCode == RESULT_OK) {
                    try {
                        final Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        ivGoods.setImageBitmap(bitmap);
                        LogUtil.e("imageUri == " + imageUri);
//                        Snackbar.make(MainActivity.this.findViewById(android.R.id.content), "已保存至相册！", Snackbar.LENGTH_LONG).setAction("不保存", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                saveToAlbum = false;1
//                            }
//                        }).show();
//
//                        if (saveToAlbum) {
//                            LogUtil.e("保存至相册！！");
//                            ImgUtils.saveImageToGallery(MainActivity.this, bitmap);
//                        }
                        //撤销逻辑不好实现，所以用dialog
                        final File photoFile = ImgUtils.compressImageAndSave2Local(MainActivity.this, getExternalCacheDir() + File.separator + "output_image.jpg", Environment.getExternalStorageDirectory() + File.separator + "183" + File.separator + "img");
                        goods.setGoodsImgUri(photoFile.getAbsolutePath());
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("保存至相册");
                        builder.setMessage("是否保存至相册？");
                        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ImgUtils.saveToAlbum(MainActivity.this, photoFile);
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.show();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case CHOOSE_PHOTO:
                LogUtil.e("resultCode == " + resultCode);
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4以上才能用该方法
                        handleImageOnkitKat(data);
                    } else {
                        //4.4以下用这个
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                LogUtil.e("default");
                break;
        }
    }

    private void handleImageOnkitKat(Intent data) {
        String imagePath = null;
        if (data == null || data.getData() == null) {
            return;
        }
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                imagePath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                imagePath = uri.getPath();
            }
            displayImage(imagePath);
        }
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            LogUtil.e("---" + imagePath);
            ivGoods.setImageBitmap(bitmap);
            goods.setGoodsImgUri(imagePath);
        } else {
            Toast.makeText(this, "获取图片失败！", Toast.LENGTH_SHORT).show();
        }
    }

    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //此处while的条件alpha不能<= 否则会出现黑屏
                    while (alpha < 1f) {
                        try {
                            Thread.sleep(4);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        alpha += 0.01f;
                        msg.obj = alpha;
                        mHandler.sendMessage(msg);
                    }
                }
            }).start();
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

}
