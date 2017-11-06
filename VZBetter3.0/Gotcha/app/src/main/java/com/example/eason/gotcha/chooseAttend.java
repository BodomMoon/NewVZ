package com.example.eason.gotcha;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class chooseAttend extends AppCompatActivity {
    /** Called when the activity is first created. */

    Double[] placeX = new Double[] { 121.548508, 121.787189, 121.564477, 121.5293995, 120.4046204,
            120.6616824, 120.6933353 ,120.6009333};
    Double[] placeY = new Double[] { 25.102358, 25.192803, 25.033967, 25.0441303, 22.7308282,
            24.1512188, 24.3230546 ,24.1815869};
    int[] imageId = new int[] { R.drawable.attend0, R.drawable.attend1,
            R.drawable.attend2, R.drawable.attend3, R.drawable.attend4,
            R.drawable.attend5, R.drawable.attend6, R.drawable.attend7  }; // 定义并初始化保存图片id的数组

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);
        ListView listview = (ListView) findViewById(R.id.listView1); // 获取列表视图

        String[] title = new String[] { "台北故宮博物院", "基隆嶼", "台北101", "華山文化園區", "義大遊樂世界",
                "勤美誠品綠園道", "麗寶樂園" ,"東海大學"}; // 定义并初始化保存列表项文字的数组
        String[] contain = new String[] { "111台北市士林區至善路二段221號", "基隆市中正區基隆嶼", "110台北市信義區信義路五段7號", "100台北市中正區八德路一段1號", "840高雄市大樹區學城路一段10號",
                "403台中市西區公益路68號", "42145台中市后里區福容路8號" ,"407台中市西屯區台灣大道四段1727號"}; // 定义并初始化保存列表项文字的数组


        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>(); // 创建一个list集合
        // 通过for循环将图片id和列表项文字放到Map中，并添加到list集合中
        for (int i = 0; i < imageId.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>(); // 实例化Map对象
            map.put("image", imageId[i]);
            map.put("title", title[i]);
            map.put("contain", contain[i]);
            listItems.add(map); // 将map对象添加到List集合中
        }

        SimpleAdapter adapter = new SimpleAdapter(this, listItems,
                R.layout.items, new String[] { "title", "image", "contain" }, new int[] {
                R.id.title, R.id.image ,  R.id.contain }); // 创建SimpleAdapter

        listview.setAdapter(adapter); // 将适配器与ListView关联

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                ListView listView = (ListView) arg0;
                Map<String, Object> map = (Map<String, Object>)listView.getItemAtPosition(arg2);
                String titleGet = (String) map.get("title");
                String containGet = (String) map.get("contain");
                Toast.makeText(chooseAttend.this,"ID：" + arg3 +"   標題："+ titleGet + " 內文：" + containGet ,Toast.LENGTH_LONG).show();
                //利用Bundle將資訊帶到第二頁
                Intent newIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("type",1 );//傳遞String
                bundle.putString("title",titleGet);//傳遞String
                bundle.putDouble("placeX",placeX[arg2]);//傳遞Double
                bundle.putDouble("placeY",placeY[arg2]);//傳遞Double
                bundle.putInt("imgId",imageId[arg2]);
                newIntent.putExtras(bundle);
                setResult(RESULT_OK,newIntent);
                finish();
                /*嘗試結束*/

            }
        });

    }
}