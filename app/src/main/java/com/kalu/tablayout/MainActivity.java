package com.kalu.tablayout;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import java.util.Random;

import lib.kalu.tab.TabLayout;
import lib.kalu.tab.listener.OnTabChangeListener;
import lib.kalu.tab.model.TabModel;
import lib.kalu.tab.model.TabModelImage;
import lib.kalu.tab.model.TabModelText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Fragment> list0 = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            MainFragment fragment = new MainFragment();
            fragment.setText("fragment => " + i);
            list0.add(fragment);
        }

        ArrayList<String> list1 = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            int nextInt1 = new Random().nextInt(10);
            int nextInt2 = new Random().nextInt(10);
            if (nextInt1 == nextInt2) {
                nextInt2 = nextInt2 + 1;
            }
            String substring = "哈哈世纪初开始了解从".substring(Math.min(nextInt1, nextInt2), Math.max(nextInt1, nextInt2));
            list1.add(substring);
        }

        ArrayList<TabModel> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            TabModel temp;
            String s = list1.get(i);
            if (i == 4) {
                temp = new TabModelImage() {
                    @Override
                    public String[] initImageSrcUrls() {
                        return new String[]{"http://129.211.42.21:80/img/public/2021/e7cffa9ddf154e4b95092f8fdc84a798.png", "http://129.211.42.21:80/img/public/2021/4884e1f436b84f3fb767b0eff425ce45.png", "http://129.211.42.21/img/public/2021/6079d10f913240ae8458bc68530fba11.png"};
                    }

//                    @Override
//                    public int[][] initImageBackgroundColors() {
//                        return new int[][]{new int[]{Color.GREEN, Color.BLUE, Color.WHITE}, new int[]{Color.GREEN, Color.BLUE, Color.YELLOW}, new int[]{Color.GREEN, Color.BLUE, Color.BLACK}};
//                    }

                    @Override
                    public String[] initImageBackgroundAssets() {
                        return new String[]{null, "2/test.9.png", null};
                    }

//                    @Override
//                    public int[] initImageBackgroundResources() {
//                        return new int[]{R.drawable.module_tablayout_ic_shape_background_normal, R.drawable.ic_test, R.drawable.module_tablayout_ic_shape_background_select};
//                    }
                };
            } else {
                temp = new TabModelText() {
                    @Override
                    public String initText() {
                        return s;
                    }

                    @Override
                    public int[] initTextColors() {
                        return new int[]{Color.BLACK, Color.WHITE, Color.GREEN};
                    }

//                    @Override
//                    public int[][] initTextBackgroundColors() {
//                        return new int[][]{new int[]{Color.GREEN, Color.BLUE, Color.WHITE}, new int[]{Color.GREEN, Color.BLUE, Color.YELLOW}, new int[]{Color.GREEN, Color.BLUE, Color.BLACK}};
//                    }


                    @Override
                    public String[] initTextBackgroundAssets() {
                        return new String[]{null, "2/test.9.png", null};
                    }

//                    @Override
//                    public int[] initTextBackgroundResources() {
//                        return new int[]{R.drawable.module_tablayout_ic_shape_background_normal, R.drawable.ic_test, R.drawable.module_tablayout_ic_shape_background_select};
//                    }
                };
            }
            list.add(temp);
        }

        TabLayout tabLayout = findViewById(R.id.tab_plus);
        tabLayout.update(list);
        tabLayout.setOnTabChangeListener(new OnTabChangeListener() {
            @SuppressLint("CommitTransaction")
            @Override
            public void onSelect(int index) {
                Log.e("MAINAA", "onSelect => index = " + index);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content, list0.get(index));
                fragmentTransaction.commit();
            }

            @Override
            public void onBefore(int index) {
                Log.e("MAINAA", "onBefore => index = " + index);
            }

            @Override
            public void onRepeat(int index) {
                Log.e("MAINAA", "onRepeat => index = " + index);
            }

            @Override
            public void onLeave(int index) {
                Log.e("MAINAA", "onLeave => index = " + index);
            }
        });

        findViewById(R.id.four).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabLayout tabLayout = findViewById(R.id.tab_plus);
                tabLayout.select(3, true, true);
            }
        });

        findViewById(R.id.five).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabLayout tabLayout = findViewById(R.id.tab_plus);
                tabLayout.select(4, false, true);
            }
        });

        findViewById(R.id.left1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabLayout tabLayout = findViewById(R.id.tab_plus);
                tabLayout.left();
            }
        });

        findViewById(R.id.left2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabLayout tabLayout = findViewById(R.id.tab_plus);
                tabLayout.left(2);
            }
        });

        findViewById(R.id.right1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabLayout tabLayout = findViewById(R.id.tab_plus);
                tabLayout.right();
            }
        });

        findViewById(R.id.right2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabLayout tabLayout = findViewById(R.id.tab_plus);
                tabLayout.right(2);
            }
        });
    }
}