package com.example.eason.gotcha;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.data;

public class chooseBetter extends Activity {
    /** Called when the activity is first created. */
    Double[] placeX = new Double[20];
    Double[] placeY = new Double[20];
    int[] imageId = new int[20]; // 定义并初始化保存图片id的数组
    int lengthAll;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);
        /*引入資料*/
        Bundle extras ;
        extras = this.getIntent().getExtras();
        final int EnterType = extras.getInt("KEY_TEXT");
        /*引入完成  開始設變數*/
        String[] title = new String[20];
        String[] contain = new String[20];

        ListView listview = (ListView) findViewById(R.id.listView1); // 获取列表视图
        /*變數設定完成  開始按照類型設定內容*/

        if(EnterType == 2)
        {
            String[] temp = new String[] { "85度C台中黎明店", "士林夜市", "金色三麥(高雄義大店)", "Gudetama Chef-蛋黃哥五星主廚餐廳", "春水堂",
                    "逢甲夜市"};
            String[] temp2 = new String[] { "408台中市南屯區黎明路一段1130號", "111台北市士林區基河路101號", "高雄市大樹區學城路一段12號 義大世界購物廣場A區5樓", "106台北市大安區敦化南路一段236巷12號", "403台中市西區四維街30號",
                    "407台中市西屯區文華路"};
            Double[] temp3 = new Double[] { 120.637425, 121.524065, 120.4023278, 121.5456446, 120.5866798,
                    120.6418906};
            Double[] temp4 = new Double[] { 24.180151, 25.087875, 22.7299258, 25.0404809, 24.192037,
                    24.1785452};
            int[] temp5 = new int[] { R.drawable.eat0, R.drawable.eat1,
                    R.drawable.eat2, R.drawable.eat3, R.drawable.eat4,
                    R.drawable.eat5 };

            lengthAll = temp.length;
            for(int counter=0;counter<lengthAll;counter++)
            {
                title[counter]=temp[counter];
                contain[counter]=temp2[counter];
                placeX[counter]=temp3[counter];
                placeY[counter]=temp4[counter];
                imageId[counter] = temp5[counter];

            }
        }
        else if(EnterType == 3)
        {
            String[] temp = new String[] { "代購資策會的肝", "義大世界購物廣場", "日曜天地奧特萊"};
            String[] temp2 = new String[] { "105台北市松山區民生東路四段133號", "840高雄市大樹區學城路一段12號", "400台中市中區自由路二段94號"};
            Double[] temp3 = new Double[] { 121.543993, 120.4042737, 120.6830699};
            Double[] temp4 = new Double[] { 25.042738, 22.7295748 ,24.1427695};
            int[] temp5 = new int[] { R.drawable.eat0, R.drawable.eat1,
                    R.drawable.eat2};
            lengthAll = temp.length;
            for(int counter=0;counter<lengthAll;counter++)
            {
                title[counter]=temp[counter];
                contain[counter]=temp2[counter];
                placeX[counter]=temp3[counter];
                placeY[counter]=temp4[counter];
                imageId[counter] = temp5[counter];

            }
        }
        else if(EnterType == 4)
        {
            String[] temp = new String[] { "圓山大飯店","義大皇家酒店", "長榮桂冠酒店" };
            String[] temp2 = new String[] { "104台北市中山區中山北路四段1號", "840高雄市大樹區學城路一段153號", "407台中市西屯區台灣大道二段666號"};
            Double[] temp3 = new Double[] { 121.526128, 120.4000483, 120.641143};
            Double[] temp4 = new Double[] { 25.078308, 22.729572 ,24.6459408};
            int[] temp5 = new int[] { R.drawable.stay0, R.drawable.stay1,
                    R.drawable.stay2};
            lengthAll = temp.length;
            for(int counter=0;counter<lengthAll;counter++)
            {
                title[counter]=temp[counter];
                contain[counter]=temp2[counter];
                placeX[counter]=temp3[counter];
                placeY[counter]=temp4[counter];
                imageId[counter] = temp5[counter];

            }
        }
        else
        {
            String[] temp = new String[] { "信仰活動" };
            String[] temp2 = new String[] { "發自內心"};
            Double[] temp3 = new Double[] { 121.526128};
            Double[] temp4 = new Double[] { 25.078308};
            int[] temp5 = new int[] { R.drawable.belive };
            lengthAll = temp.length;

            for(int counter=0;counter<lengthAll;counter++)
            {
                title[counter]=temp[counter];
                contain[counter]=temp2[counter];
                placeX[counter]=temp3[counter];
                placeY[counter]=temp4[counter];
                imageId[counter] = temp5[counter];

            }
        }


        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>(); // 创建一个list集合
        // 通过for循环将图片id和列表项文字放到Map中，并添加到list集合中
        for (int i = 0; i < lengthAll; i++) {
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
                Toast.makeText(chooseBetter.this,"ID：" + arg3 +"   標題："+ titleGet + " 內文：" + containGet ,Toast.LENGTH_LONG).show();
                //利用Bundle將資訊帶到第二頁
                Intent newIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("type",EnterType );//傳遞String
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