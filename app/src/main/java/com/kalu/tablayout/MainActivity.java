package com.kalu.tablayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import lib.kalu.tablayout.TabModel;
import lib.kalu.tablayout.TabModelImage;
import lib.kalu.tablayout.TabModelText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        return new String[]{"http://129.211.42.21:80/img/public/2021/e7cffa9ddf154e4b95092f8fdc84a798.png", "http://129.211.42.21:80/img/public/2021/4884e1f436b84f3fb767b0eff425ce45.png", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F011d425c98e2e0a801214168588459.jpg%401280w_1l_2o_100sh.jpg&refer=http%3A%2F%2Fimg.zcool.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1640452367&t=d1f620e8a51f97f52602f0be1452562b"};
                    }
                };
            } else {
                temp = new TabModelText() {
                    @Override
                    public String initText() {
                        return s;
                    }
                };
            }
            list.add(temp);
        }

        lib.kalu.tablayout.TabLayout tabLayout = findViewById(R.id.tab_plus);
        tabLayout.update(list);

        findViewById(R.id.four).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lib.kalu.tablayout.TabLayout tabLayout = findViewById(R.id.tab_plus);
                tabLayout.select(3, true);
            }
        });

        findViewById(R.id.five).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lib.kalu.tablayout.TabLayout tabLayout = findViewById(R.id.tab_plus);
                tabLayout.select(4, true);
            }
        });
    }
}