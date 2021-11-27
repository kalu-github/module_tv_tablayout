package lib.kalu.tablayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.os.BuildCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

/**
 * utils
 */
class TabUtil {

    public static final void logE(@NonNull String message) {

        if (null == message || message.length() == 0)
            return;

        Log.e("module-tablayout", message);
    }

    public static final <T extends TabModel> void updateImageUI(@NonNull ImageView view, @NonNull T t) {

        if (null == t || null == view)
            return;

        updateImageSrc(view, t);
        updateImageBackground(view, t);
    }

    private static final <T extends TabModel> void updateImageSrc(@NonNull ImageView view, @NonNull T t) {

        if (null == t || null == view)
            return;

        String[] urls = t.initImageSrcUrls();
        if (null == urls || urls.length < 3)
            return;

        boolean focus = view.hasFocus();
        boolean activated = view.isActivated();

        String url = activated ? urls[2] : (focus ? urls[1] : urls[0]);
        loadImageUrl(view, url, false);
    }

    private static final <T extends TabModel> void updateImageBackground(@NonNull ImageView view, @NonNull T t) {

        if (null == t || null == view)
            return;

        boolean focus = view.hasFocus();
        boolean activated = view.isActivated();

        String[] urls = t.initImageBackgroundUrls();
        int[] resources = t.initImageBackgroundResources();
        int[] defaults = t.initImageBackgroundDefaults();
        logE("updateImageBackground => urls = " + urls + ", resources = " + resources + ", defaults = " + defaults);

        // 背景 => 网络图片
        if (null != urls && urls.length >= 3) {
            String url = activated ? urls[2] : (focus ? urls[1] : urls[0]);
            loadImageUrl(view, url, true);
        }
        // 背景 => 资源图片
        else if (null != resources && resources.length >= 3) {
            int resource = activated ? resources[2] : (focus ? resources[1] : resources[0]);
            loadImageResource(view, resource, true);
        }
        // 背景 => 默认图片
        else if (null != defaults && defaults.length >= 3) {
            int def = activated ? defaults[2] : (focus ? defaults[1] : defaults[0]);
            loadImageResource(view, def, true);
        }
    }

    public static final <T extends TabModel> void updateTextUI(@NonNull TextView view, @NonNull T t) {

        if (null == t || null == view)
            return;

        view.setGravity(t.initTextGravity());
        view.setText(t.initText());
        view.setTextSize(t.initTextSize());

        updateTextColor(view, t);
        updateTextBackground(view, t);
    }

    private static final <T extends TabModel> void updateTextColor(@NonNull TextView view, @NonNull T t) {

        if (null == t || null == view)
            return;

        int[] colors = t.initTextColors();
        if (null == colors || colors.length < 3)
            return;

        boolean focus = view.hasFocus();
        boolean activated = view.isActivated();
        view.setTextColor(activated ? colors[2] : (focus ? colors[1] : colors[0]));
    }

    private static final <T extends TabModel> void updateTextBackground(@NonNull TextView view, @NonNull T t) {

        if (null == t)
            return;

        boolean focus = view.hasFocus();
        boolean activated = view.isActivated();

        String[] urls = t.initTextBackgroundUrls();
        int[] resources = t.initTextBackgroundResources();
        int[] defaults = t.initTextBackgroundDefaults();

        // 背景 => 网络图片
        if (null != urls && urls.length >= 3) {
            String url = activated ? urls[2] : (focus ? urls[1] : urls[0]);
            loadImageUrl(view, url, true);
        }
        // 背景 => 资源图片
        else if (null != resources && resources.length >= 3) {
            int resource = activated ? resources[2] : (focus ? resources[1] : resources[0]);
            loadImageResource(view, resource, true);
        }
        // 背景 => 默认图片
        else if (null != defaults && defaults.length >= 3) {
            int def = activated ? defaults[2] : (focus ? defaults[1] : defaults[0]);
            loadImageResource(view, def, true);
        }
    }

    public final static void loadImageResource(@NonNull final View view, @NonNull final int resource, final boolean isBackground) {
        try {
            if (isBackground) {
                view.setBackgroundResource(resource);
            } else if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                if (isBackground) {
                    imageView.setImageResource(resource);
                } else {
                    imageView.setBackgroundResource(resource);
                }
            }
        } catch (Exception e) {
        }
    }

    public final static void loadImageUrl(@NonNull final View view, @NonNull final String imageUrl, final boolean isBackground) {

        if (null == imageUrl || imageUrl.length() == 0 || !imageUrl.startsWith("http"))
            return;

        // 加密缓存文件名
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(imageUrl.getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                builder.append(temp);
            }

            Context context = view.getContext();
            File filesDir = context.getFilesDir();
            if (!filesDir.exists()) {
                filesDir.mkdir();
            }

            File tempDir = new File(filesDir, "temp_tl");
            if (!tempDir.exists()) {
                tempDir.mkdir();
            }

            String fileName = builder.toString();
            File tempFile = new File(tempDir, fileName);
            String path = tempFile.getAbsolutePath();

            // 缓存
            if (tempFile.exists()) {
                loadImageFile(view, path, isBackground);
            }
            // 下载
            else {
                downloadImage(view, path, imageUrl, isBackground);
            }

        } catch (Exception e) {
        }
    }

    /**
     * 分线程下载
     *
     * @param view
     * @param url
     * @param isBackground
     */
    private final static void downloadImage(@NonNull final View view, @NonNull final String filePath, @NonNull final String url, final boolean isBackground) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    // 1. 下载
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(input);
                    input.close();

                    // 2.保存
                    File temp = new File(filePath);
                    if (temp.exists()) {
                        temp.delete();
                    }
                    temp.createNewFile();
                    FileOutputStream out = new FileOutputStream(temp);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 60, out);
                    out.flush();
                    out.close();
                    bitmap.recycle();

                    // 3.主线程更新
                    loadImageFile(view, filePath, isBackground);
                } catch (Exception e) {
                }
            }
        }).start();
    }

    private final static void loadImageFile(@NonNull final View view, @NonNull final String path, final boolean isBackground) {
        try {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        loadImageBitmap(view, bitmap, isBackground);
                    }
                });
            } else {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                loadImageBitmap(view, bitmap, isBackground);
            }
        } catch (Exception e) {
        }
    }

    private final static void loadImageBitmap(@NonNull View view, @NonNull Bitmap bitmap, final boolean isBackground) {
        try {
            BitmapDrawable drawable = new BitmapDrawable(view.getResources(), bitmap);
            if (isBackground) {
                view.setBackground(drawable);
            } else if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                imageView.setImageDrawable(drawable);
            }
        } catch (Exception e) {
        }
    }
}
