package lib.kalu.tab;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lib.kalu.tab.ninepatch.NinePatchChunk;
import lib.kalu.tab.model.TabModel;

/**
 * utils
 */
class TabUtil {

    // 最多可能会先后下载3张图片
    private static final ExecutorService ES = Executors.newFixedThreadPool(3);

    public static final void logE(@NonNull String message) {

        if (!BuildConfig.DEBUG)
            return;

        if (null == message || message.length() == 0)
            return;

        Log.e("module-tablayout", message);
    }

    public static final <T extends TabModel> void updateImageUI(@NonNull ImageView view, @NonNull T t, @NonNull float radius, boolean focus, boolean stay) {

        if (null == t || null == view)
            return;

        updateImageSrc(view, t, focus, stay);
        updateImageBackground(view, t, radius, focus, stay);
    }

    private static final <T extends TabModel> void updateImageSrc(@NonNull ImageView view, @NonNull T t, boolean focus, boolean stay) {

        if (null == t || null == view)
            return;

        int placeholder = t.initImagePlaceholder();
        try {
            view.setImageResource(placeholder);
        } catch (Exception e) {
        }

        String[] urls = t.initImageSrcUrls();
        if (null == urls || urls.length < 3)
            return;

        String url = stay ? urls[2] : (focus ? urls[1] : urls[0]);
        loadImageUrl(view, url, false);
    }

    private static final <T extends TabModel> void updateImageBackground(@NonNull ImageView view, @NonNull T t, @NonNull float radius, boolean focus, boolean stay) {

        if (null == t || null == view)
            return;

        logE("updateImageBackground => ************************");
        String[] urls = t.initImageBackgroundUrls();
        logE("updateImageBackground => urls = " + Arrays.toString(urls));
        String[] files = t.initImageBackgroundFiles();
        logE("updateImageBackground => files = " + Arrays.toString(files));
        String[] assets = t.initImageBackgroundAssets();
        logE("updateImageBackground => assets = " + Arrays.toString(assets));
        int[] resources = t.initImageBackgroundResources();
        logE("updateImageBackground => resources = " + Arrays.toString(resources));
        int[][] colors = t.initImageBackgroundColors();
        logE("updateImageBackground => colors = " + Arrays.toString(colors));

        // 背景 => 渐变背景色
        if (null != colors && colors.length >= 3) {
            int[] color = stay ? colors[2] : (focus ? colors[1] : colors[0]);
            logE("updateImageBackground[colors]=> color = " + Arrays.toString(color));
            setBackgroundGradient(view, color, radius);
        }
        // 背景 => 网络图片
        else if (null != urls && urls.length >= 3 && (null != urls[0] || null != urls[1] || null != urls[2])) {
            String url = stay ? urls[2] : (focus ? urls[1] : urls[0]);
            logE("updateImageBackground[urls]=> url = " + url);
            loadImageUrl(view, url, true);
        }
        // 背景 => 本地图片
        else if (null != files && files.length >= 3 && (null != files[0] || null != files[1] || null != files[2])) {
            String file = stay ? files[2] : (focus ? files[1] : files[0]);
            logE("updateImageBackground[files]=> file = " + file);
            setBackgroundFile(view, file, true);
        }
        // 背景 => Assets图片
        else if (null != assets && assets.length >= 3 && (null != assets[0] || null != assets[1] || null != assets[2])) {
            String asset = stay ? assets[2] : (focus ? assets[1] : assets[0]);
            logE("updateImageBackground[assets]=> asset = " + asset);
            setBackgroundAssets(view, asset, true);
        }
        // 背景 => 资源图片
        else if (null != resources && resources.length >= 3) {
            int resId = stay ? resources[2] : (focus ? resources[1] : resources[0]);
            logE("updateImageBackground[resources]=> resId = " + resId);
            setBackgroundResource(view, resId, true);
        }
        // 背景 => 默认图片
        else {
            int resId = stay ? R.drawable.module_tablayout_ic_shape_background_select : (focus ? R.drawable.module_tablayout_ic_shape_background_focus : R.drawable.module_tablayout_ic_shape_background_normal);
            logE("updateImageBackground[defaults]=> resId = " + resId);
            setBackgroundResource(view, resId, true);
        }
        logE("updateImageBackground => ************************");
    }

    /**
     * @param view
     * @param t
     * @param radius
     * @param focus  焦点
     * @param stay   驻留
     * @param <T>
     */
    public static final <T extends TabModel> void updateTextUI(@NonNull TabTextView view, @NonNull T t, @NonNull float radius, boolean focus, boolean stay) {

        if (null == t || null == view)
            return;

        view.setText(t.initText());

        updateTextColor(view, t, focus, stay);
        updateTextBackground(view, t, radius, focus, stay);
    }

    private static final <T extends TabModel> void updateTextColor(@NonNull TabTextView view, @NonNull T t, boolean focus, boolean stay) {

        if (null == t || null == view)
            return;

        int[] colors = t.initTextColors();
        if (null == colors || colors.length < 3)
            return;

        view.updateTextColor(stay ? colors[2] : (focus ? colors[1] : colors[0]), stay);
    }

    private static final <T extends TabModel> void updateTextBackground(@NonNull TextView view, @NonNull T t, @NonNull float radius, boolean focus, boolean stay) {

        if (null == t)
            return;

        logE("updateTextBackground => ************************");
        logE("updateTextBackground => focus = " + focus + ", stay = " + stay);

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
            int[] color = stay ? colors[2] : (focus ? colors[1] : colors[0]);
            logE("updateTextBackground[colors]=> color = " + Arrays.toString(color) + ", text = " + view.getText());
            setBackgroundGradient(view, color, radius);
        }
        // 背景 => 网络图片
        else if (null != urls && urls.length >= 3 && (null != urls[0] || null != urls[1] || null != urls[2])) {
            String url = stay ? urls[2] : (focus ? urls[1] : urls[0]);
            logE("updateTextBackground[urls]=> url = " + url + ", text = " + view.getText());
            loadImageUrl(view, url, true);
        }
        // 背景 => 本地图片
        else if (null != files && files.length >= 3 && (null != files[0] || null != files[1] || null != files[2])) {
            String file = stay ? files[2] : (focus ? files[1] : files[0]);
            logE("updateTextBackground[files]=> file = " + file + ", text = " + view.getText());
            setBackgroundFile(view, file, true);
        }
        // 背景 => Assets图片
        else if (null != assets && assets.length >= 3 && (null != assets[0] || null != assets[1] || null != assets[2])) {
            String path = stay ? assets[2] : (focus ? assets[1] : assets[0]);
            logE("updateTextBackground[assets]=> assets = " + path + ", text = " + view.getText());
            setBackgroundAssets(view, path, true);
        }
        // 背景 => 资源图片
        else if (null != resources && resources.length >= 3) {
            int resId = stay ? resources[2] : (focus ? resources[1] : resources[0]);
            logE("updateTextBackground[resources]=> resource = " + resId + ", text = " + view.getText());
            setBackgroundResource(view, resId, true);
        }
        // 背景 => 默认图片
        else {
            int resId = stay ? R.drawable.module_tablayout_ic_shape_background_select : (focus ? R.drawable.module_tablayout_ic_shape_background_focus : R.drawable.module_tablayout_ic_shape_background_normal);
            logE("updateTextBackground[defaults]=> resId = " + resId + ", text = " + view.getText());
            setBackgroundResource(view, resId, true);
        }
        logE("updateTextBackground => ************************");
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
                setBackgroundFile(view, path, isBackground);
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

        ES.submit(new Runnable() {
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
                    setBackgroundFile(view, filePath, isBackground);
                } catch (Exception e) {
                }
            }
        });
    }

    private final static void setBackgroundAssets(@NonNull final View view, @NonNull final String path, final boolean isBackground) {
        try {
            Drawable drawable = decodeDrawable(view, path, true);
            setBackgroundDrawable(view, drawable, isBackground);
        } catch (Exception e) {
        }
    }

    private final static void setBackgroundFile(@NonNull final View view, @NonNull final String path, final boolean isBackground) {
        try {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        Drawable drawable = decodeDrawable(view, path, false);
                        setBackgroundDrawable(view, drawable, isBackground);
                    }
                });
            } else {
                Drawable drawable = decodeDrawable(view, path, false);
                setBackgroundDrawable(view, drawable, isBackground);
            }
        } catch (Exception e) {
        }
    }

    private final static Drawable decodeDrawable(@NonNull View view, @NonNull String absolutePath, boolean isAssets) {

        Drawable drawable = null;
        InputStream inputStream = null;

        try {

            if (null != absolutePath && absolutePath.length() > 0) {

                Resources resources = view.getResources();
                if (isAssets) {
                    inputStream = resources.getAssets().open(absolutePath);
                } else {
                    inputStream = new FileInputStream(absolutePath);
                }

                // .9
                if (absolutePath.endsWith(".9.png")) {
                    Context context = view.getContext().getApplicationContext();
                    drawable = NinePatchChunk.create9PatchDrawable(context, inputStream, null);
                }
                // not .9
                else {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    drawable = new BitmapDrawable(resources, bitmap);
                }
            }
        } catch (Exception e) {
            logE("decodeDrawable[exception] => " + e.getMessage());
        }

        try {
            if (null != inputStream) {
                inputStream.close();
            }
        } catch (Exception e) {
        }

        if (view instanceof TextView) {
            logE("decodeDrawable => absolutePath = " + absolutePath + ", drawable = " + drawable + ", text = " + ((TextView) view).getText());
        } else {
            logE("decodeDrawable => absolutePath = " + absolutePath + ", drawable = " + drawable);
        }
        return drawable;
    }

    /*********************************/

    public final static void setBackgroundGradient(@NonNull View view, @NonNull final int[] colors, float radius) {

        if (null == colors || colors.length == 0)
            return;

        try {
            GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
            drawable.setCornerRadius(radius);
            view.setBackground(drawable);
        } catch (Exception e) {
        }
    }

    public final static void setBackgroundResource(@NonNull final View view, @NonNull final int resId, final boolean isBackground) {
        try {
//            String resourceName = view.getResources().getResourceName(resId);
//            logE("loadImageResource => resourceName = " + resourceName);

            // img1
            if (null != view && view instanceof ImageView && isBackground) {
                ImageView imageView = (ImageView) view;
                imageView.setBackground(null);
                imageView.setBackgroundResource(resId);
                logE("setBackgroundResource => status = succ, view = " + view);
            }
            // img2
            else if (null != view && view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                imageView.setImageDrawable(null);
                imageView.setImageResource(resId);
                logE("setBackgroundResource => status = succ, view = " + view);
            }
            // view
            else if (null != view) {
                view.setBackground(null);
                view.setBackgroundResource(resId);
                logE("setBackgroundResource => status = succ, view = " + view);
            }
            // fail
            else {
                logE("setBackgroundResource => status = fail, view = " + view);
            }
        } catch (Exception e) {
            logE("setBackgroundResource => " + e.getMessage());
        }
    }

    private final static void setBackgroundDrawable(@NonNull View view, @NonNull Drawable drawable, final boolean isBackground) {
        try {
            // img1
            if (null != view && view instanceof ImageView && isBackground) {
                ImageView imageView = (ImageView) view;
                imageView.setBackground(null);
                imageView.setBackground(drawable);
                logE("setBackgroundDrawable => status = succ, view = " + view);
            }
            // img2
            else if (null != view && view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                imageView.setImageDrawable(null);
                imageView.setImageDrawable(drawable);
                logE("setBackgroundDrawable => status = succ, view = " + view);
            }
            // view
            else if (null != view) {
                view.setBackground(null);
                view.setBackground(drawable);
                logE("setBackgroundDrawable => status = succ, view = " + view);
            }
            // fail
            else {
                logE("setBackgroundDrawable => status = fail, view = " + view);
            }
        } catch (Exception e) {
            logE("setBackgroundDrawable => " + e.getMessage());
        }
    }
}
