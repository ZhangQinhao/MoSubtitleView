package com.monke.mosubtitleview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.monke.mosubtitleviewlib.MoSubtitleView;
import com.monke.mosubtitleviewlib.SubtitleBean;
import com.monke.mosubtitleviewlib.SubtitleItemBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSubtitle();
    }

    private MoSubtitleView sv;

    private void initSubtitle() {
        sv = findViewById(R.id.msv);
        SubtitleBean subtitleBean = new SubtitleBean();
        List<SubtitleItemBean> subtitles = new ArrayList<>();
        subtitleBean.setSubtitles(subtitles);
        SubtitleItemBean s1 = new SubtitleItemBean(2000, "1My哈哈哈哈1");
        subtitles.add(s1);
        SubtitleItemBean s2 = new SubtitleItemBean(3000, "2My哈哈哈哈哈2");
        subtitles.add(s2);
        SubtitleItemBean s3 = new SubtitleItemBean(4000, "3My哈哈哈哈3");
        subtitles.add(s3);
        SubtitleItemBean s4 = new SubtitleItemBean(5000, "4My哈哈哈哈4");
        subtitles.add(s4);
        SubtitleItemBean s5 = new SubtitleItemBean(6000, "5My哈哈哈5");
        subtitles.add(s5);
        SubtitleItemBean s6 = new SubtitleItemBean(7000, "6My哈哈哈哈哈哈哈哈哈6");
        subtitles.add(s6);
        SubtitleItemBean s7 = new SubtitleItemBean(8000, "7哈哈哈哈7");
        subtitles.add(s7);
        SubtitleItemBean s8 = new SubtitleItemBean(9000, "8哈哈哈哈哈哈哈哈8");
        subtitles.add(s8);
        SubtitleItemBean s9 = new SubtitleItemBean(10000, "9哈哈哈9");
        subtitles.add(s9);
        SubtitleItemBean s10 = new SubtitleItemBean(11000, "10哈哈哈哈哈哈哈哈哈哈10");
        subtitles.add(s10);
        SubtitleItemBean s11 = new SubtitleItemBean(12000, "11哈哈哈哈哈哈哈哈哈哈哈11");
        subtitles.add(s11);
        SubtitleItemBean s12 = new SubtitleItemBean(13000, "12My哈哈哈哈哈哈哈哈哈哈哈12");
        subtitles.add(s12);
        SubtitleItemBean s13 = new SubtitleItemBean(14000, "13My哈哈哈哈哈哈哈哈哈哈哈13");
        subtitles.add(s13);
        SubtitleItemBean s14 = new SubtitleItemBean(15000, "14My哈哈哈哈哈哈哈哈哈哈哈14");
        subtitles.add(s14);

        sv.setSubtitleData(subtitleBean);

        f(0); //模拟字幕测试代码
    }

    private void f(final long t) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (t + 1000 <= 14000) {
                    sv.updateTime(t + 1000);
                    f(t + 1000);
                }
            }
        }, 1000);
    }
}