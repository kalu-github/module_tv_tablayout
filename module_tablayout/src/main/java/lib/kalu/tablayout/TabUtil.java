package lib.kalu.tablayout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Arrays;

import lib.kalu.tablayout.model.TabModel;
import lib.kalu.tablayout.model.TabModelImage;
import lib.kalu.tablayout.model.TabModelText;
import lib.kalu.tablayout.ninepatch.NinePatchChunk;

/**
 * utils
 */
class TabUtil {

    public static final void logE(@NonNull String message) {

        if (null == message || message.length() == 0)
            return;

        Log.e("module-tablayout", message);
    }

    public static final <T extends TabModel> void updateImageUI(@NonNull ImageView view, @NonNull T t, @NonNull float radius) {

        if (null == t || null == view)
            return;

        updateImageSrc(view, t);
        updateImageBackground(view, t, radius);
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

    private static final <T extends TabModel> void updateImageBackground(@NonNull ImageView view, @NonNull T t, @NonNull float radius) {

        if (null == t || null == view)
            return;

        boolean focus = view.hasFocus();
        boolean activated = view.isActivated();

        String[] urls = t.initImageBackgroundUrls();
        logE("updateImageBackground => urls = " + Arrays.toString(urls));
        int[] resources = t.initImageBackgroundResources();
        logE("updateImageBackground => resources = " + Arrays.toString(resources));
        int[][] colors = t.initImageBackgroundColors();
        logE("updateImageBackground => colors = " + Arrays.toString(colors));

        // 背景 => 渐变背景色
        if (null != colors && colors.length >= 3) {
            int[] color = activated ? colors[2] : (focus ? colors[1] : colors[0]);
            logE("updateImageBackground[colors]=> color = " + Arrays.toString(color));
            loadColor(view, color, radius);
        }
        // 背景 => 网络图片
        else if (null != urls && urls.length >= 3) {
            String url = activated ? urls[2] : (focus ? urls[1] : urls[0]);
            logE("updateImageBackground[urls]=> url = " + url);
            loadImageUrl(view, url, true);
        }
        // 背景 => 资源图片
        else if (null != resources && resources.length >= 3) {
            int resId = activated ? resources[2] : (focus ? resources[1] : resources[0]);
            logE("updateImageBackground[resources]=> resId = " + resId);
            loadImageResource(view, resId, true);
        }
        // 背景 => 默认图片
        else {
            int resId = activated ? R.drawable.module_tablayout_ic_shape_background_select : (focus ? R.drawable.module_tablayout_ic_shape_background_focus : R.drawable.module_tablayout_ic_shape_background_normal);
            logE("updateImageBackground[defaults]=> resId = " + resId);
            loadImageResource(view, resId, true);
        }
    }

    public static final <T extends TabModel> void updateTextUI(@NonNull TextView view, @NonNull T t, @NonNull float radius) {

        if (null == t || null == view)
            return;

        view.setGravity(t.initTextGravity());
        view.setText(t.initText());
        view.setTextSize(t.initTextSize());

        updateTextColor(view, t);
        updateTextBackground(view, t, radius);
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

    private static final <T extends TabModel> void updateTextBackground(@NonNull TextView view, @NonNull T t, @NonNull float radius) {

        if (null == t)
            return;

        boolean focus = view.hasFocus();
        boolean activated = view.isActivated();
        logE("updateTextBackground => ************************");
        logE("updateTextBackground => focus = " + focus + ", activated = " + activated);

        int[][] colors = t.initTextBackgroundColors();
        logE("updateTextBackground => colors = " + Arrays.toString(colors));
        String[] urls = t.initTextBackgroundUrls();
        logE("updateTextBackground => urls = " + Arrays.toString(urls));
        String[] files = t.initTextBackgroundFiles();
        logE("updateTextBackground => files = " + Arrays.toString(files));
        String[] assets = t.initTextBackgroundAssets();
        logE("updateTextBackground => assets = " + Arrays.toString(assets));
        int[] resources = t.initTextBackgroundResources();
        logE("updateTextBackground => resources = " + Arrays.toString(resources));

        // 背景 => 渐变背景色
        if (null != colors && colors.length >= 3) {
            int[] color = activated ? colors[2] : (focus ? colors[1] : colors[0]);
            logE("updateTextBackground[colors]=> color = " + Arrays.toString(color) + ", text = " + view.getText());
            loadColor(view, color, radius);
        }
        // 背景 => 网络图片
        else if (null != urls && urls.length >= 3) {
            String url = activated ? urls[2] : (focus ? urls[1] : urls[0]);
            logE("updateTextBackground[urls]=> url = " + url + ", text = " + view.getText());
            loadImageUrl(view, url, true);
        }
        // 背景 => 本地图片
        else if (null != files && files.length >= 3) {
            String file = activated ? files[2] : (focus ? files[1] : files[0]);
            logE("updateTextBackground[files]=> file = " + file + ", text = " + view.getText());
            loadImageFile(view, file, true);
        }
        // 背景 => Assets图片
        else if (null != assets && assets.length >= 3) {
            String path = activated ? assets[2] : (focus ? assets[1] : assets[0]);
            logE("updateTextBackground[assets]=> assets = " + path + ", text = " + view.getText());
            loadImageAssets(view, path, true);
        }
        // 背景 => 资源图片
        else if (null != resources && resources.length >= 3) {
            int resId = activated ? resources[2] : (focus ? resources[1] : resources[0]);
            logE("updateTextBackground[resources]=> resource = " + resId + ", text = " + view.getText());
            loadImageResource(view, resId, true);
        }
        // 背景 => 默认图片
        else {
            int resId = activated ? R.drawable.module_tablayout_ic_shape_background_select : (focus ? R.drawable.module_tablayout_ic_shape_background_focus : R.drawable.module_tablayout_ic_shape_background_normal);
            logE("updateTextBackground[defaults]=> resId = " + resId + ", text = " + view.getText());
            loadImageResource(view, resId, true);
        }
        logE("updateTextBackground => ************************");
    }

    public final static void loadImageResource(@NonNull final View view, @NonNull final int resId, final boolean isBackground) {
        try {

            String resourceName = view.getResources().getResourceName(resId);
            logE("loadImageResource => resourceName = " + resourceName);

            // img1
            if (null != view && view instanceof ImageView && isBackground) {
                ImageView imageView = (ImageView) view;
                imageView.setBackgroundResource(resId);
            }
            // img2
            else if (null != view && view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                imageView.setImageResource(resId);
            }
            // view
            else if (null != view) {
                view.setBackgroundResource(resId);
            }
        } catch (Exception e) {
            logE("loadImageResource => " + e.getMessage());
        }
    }

    public final static void loadColor(@NonNull final View view, @NonNull final int[] colors, float radius) {

        if (null == colors || colors.length == 0)
            return;

        try {
            GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
            drawable.setCornerRadius(radius);
            view.setBackground(drawable);
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

    private final static void loadImageAssets(@NonNull final View view, @NonNull final String path, final boolean isBackground) {
        try {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        Drawable drawable = loadDrawable(view, path, true);
                        loadImageDrawable(view, drawable, isBackground);
                    }
                });
            } else {
                Drawable drawable = loadDrawable(view, path, true);
                loadImageDrawable(view, drawable, isBackground);
            }
        } catch (Exception e) {
        }
    }

    private final static void loadImageFile(@NonNull final View view, @NonNull final String path, final boolean isBackground) {
        try {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        Drawable drawable = loadDrawable(view, path, false);
                        loadImageDrawable(view, drawable, isBackground);
                    }
                });
            } else {
                Drawable drawable = loadDrawable(view, path, false);
                loadImageDrawable(view, drawable, isBackground);
            }
        } catch (Exception e) {
        }
    }

    private final static Drawable loadDrawable(@NonNull View view, @NonNull String path, boolean isAssets) {

        try {

            InputStream is = null;

            // Assets
            if (isAssets) {
                try {
                    is = view.getContext().getAssets().open(path);
                } catch (Exception e) {
                }
            }
            // File
            else {
                is = new FileInputStream(path);
            }

            Bitmap bitmap = BitmapFactory.decodeStream(is);
            if (null != is) {
                is.close();
            }

            // .9
            if (path.endsWith(".9.png")) {
                NinePatchDrawable drawable = NinePatchChunk.create9PatchDrawable(view.getContext(), bitmap, null);
                logE("loadDrawable[assets=.9] => path = " + path + ", drawable = " + drawable);
                return drawable;
            }
            // img
            else {
                BitmapDrawable drawable = new BitmapDrawable(view.getResources(), bitmap);
                logE("loadDrawable[file] => path = " + path + ", drawable = " + drawable);
                return drawable;
            }
        } catch (Exception e) {
            logE("loadDrawable[Exception] => path = " + path + ", error = " + e.getMessage());
            return null;
        }
    }

    private final static void loadImageDrawable(@NonNull View view, @NonNull Drawable drawable, final boolean isBackground) {
        try {
            if (isBackground) {
                view.setBackground(drawable);
            } else if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                imageView.setImageDrawable(drawable);
            }
        } catch (Exception e) {
            logE("loadImageDrawable => " + e.getMessage());
        }
    }
}
