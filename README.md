# MoSubtitleView

![enter description here][1]

### 引入

在build.gradle引入  `implementation 'io.github.zhangqinhao:MoSubtitleView:1.0.1'`

``` stylus
<com.monke.mosubtitleviewlib.MoSubtitleView
        android:id="@+id/msv"
        android:layout_width="300dp"
        android:layout_height="400dp"
        android:layout_gravity="center_horizontal"
        android:layout_marginTop="20dp"
        android:background="#c1c1c1"
        app:mosubtitle_childCount="3"
        android:textColor="@android:color/holo_green_dark"
        app:mosubtitle_item_width="220dp" />
        
        sv.setSubtitleData(subtitleBean);  //初始化塞入字幕数据
        
        sv.updateTime(t + 1000); //更新进度
```


具体用法参照Sample代码

  


  [1]: ./images/1.gif "1.gif"
