package com.example.eason.gotcha;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.PopupMenu;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class MainActivity extends AppCompatActivity  {

    Handler aHandler;
    int timer = 11;
    int mx , my , fx , fy , using;
    //定義layout來放New View
    RelativeLayout layPlace;
    /*新增按鈕的陣列*/
    ImageButton[] newBotton = new ImageButton[10];
    int number = 0;
    int needcount = 0;
    /*新增按鈕的陣列  以及記錄按鈕數量的變數*/
    int[] bx = new int[10];
    int[] by = new int[10];
    /*這是用來記錄按鈕位置的*/
    Double[] placeX = new Double[10];
    Double[] placeY = new Double[10];
    String[] title = new String[10];
    int[] type = new int[10];
    /*用來記錄回傳的按鈕資料*/
    int[] stack = new int[10];
    /*用來記錄目前的排列組合*/
    Boolean[] b = new Boolean[10];
    int c,ip;
    int[] temp = new int[10];
    /*遞迴用*/
    double smallest = 0 ;
    int[] bestDistanceWay = new int[10];
    /*計算距離用變數*/
    int[] spacePlace = new int[4];
    int[] spacePlaceIndex = new int[4];
    int outNumber ;
    /*記錄行程中景點用*/

    private SQLiteDatabase db = null;
    private ListView list;
    private WebView webView;
    private double myLat = 25.066481, myLng = 121.521266;
    /** Called when the activity is first created. */

    private ImageButton img;
    private ImageButton img1;
    private ImageButton img2;
    private ImageButton img3;
    private ImageButton img4;
    private ImageButton img5;
    private TextView textView1;
    private int intScreenX, intScreenY; /* 宣告儲存螢幕的解析度變數 */
    private int picturesizeH, picturesizeW; /* 宣告圖片大小 */

    //長案部分試作
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layPlace = (RelativeLayout)findViewById(R.id.viewObj);

        /*測試用
                newBotton[0] = (ImageButton) findViewById(R.id.imageButton6);
                newBotton[0].setTag(0);
                newBotton[0].setOnTouchListener(imgListener);
                newBotton[0].setOnClickListener(simiCkick);  //註冊偽裝的按鈕事件
                placeX[0] = 121.499118;
                placeY[0] = 25.138127;
                type[0] = 1;
                number++;
                needcount++;
                測試listener用*/


        /*********************/


        img1 = (ImageButton)findViewById(R.id.imageButton1);
        img2 = (ImageButton)findViewById(R.id.imageButton2);
        img3 = (ImageButton)findViewById(R.id.imageButton3);
        img4 = (ImageButton)findViewById(R.id.imageButton4);
        img5 = (ImageButton)findViewById(R.id.imageButton5);
        img1.setOnClickListener(Img1OnClick1);
        img2.setOnClickListener(Img1OnClick2);
        img3.setOnClickListener(Img1OnClick3);
        img4.setOnClickListener(Img1OnClick4);
        img5.setOnClickListener(Img1OnClick5);


		/* 取得螢幕解析像素 */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        intScreenX = dm.widthPixels;
        intScreenY = dm.heightPixels;
        //Log.e("intScreenX", String.valueOf(intScreenX) + "~~" + String.valueOf(intScreenY));

        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher);
        picturesizeH=bmp.getHeight();
        picturesizeW=bmp.getWidth();
        Log.e("bmp", String.valueOf(bmp.getHeight()) + "~~" + String.valueOf(bmp.getWidth()));
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//menu監聽
        if (item.getItemId() == R.id.menu_grade)//分數menu
        {
            Toast.makeText(this, "grade menu", Toast.LENGTH_SHORT).show();
            smallest = 0;
            getPlaceSpace();
            findAllTeam(needcount);
            //BigWindBlow();
            placeNewXY();
            placeAll();
            Toast.makeText(MainActivity.this, "修改完成" , Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);

    }

    private OnTouchListener imgListener = new OnTouchListener() {
        private float x, y; // ­ì¥»¹Ï¤ù¦s¦bªºX,Y¶b¦ì¸m
        //private int mx, my; // ¹Ï¤ù³Q©ì¦²ªºX ,Y¶b¶ZÂ÷ªø«×
        private float rowx,rowy;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            // Log.e("View", v.toString());
            int tag ;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:// «ö¤U¹Ï¤ù®É
                    //Log.e("getTop", String.valueOf(img.getTop()) + "~~" + String.valueOf
                    // (picturesizeW));
                    rowx=event.getRawX();
                    rowy=event.getRawY() ;
                    x = event.getX();
                    y = rowy-v.getTop(); //event.getY();
                    fx = (int) ((event.getRawX()-x)*0.47) ;
                    fy = (int) (event.getRawY()-y );
                    mx = fx ;
                    my = fy ;
                    timer = 1;
                    using = (Integer) v.getTag();
                    aHandler = new Handler();
                    aHandler.post(runnable);
                    break;
                case MotionEvent.ACTION_MOVE:// ²¾°Ê¹Ï¤ù®É
                    mx = (int) ((event.getRawX()-x)*0.47) ;
                    my = (int) (event.getRawY()-y );
                    v.layout(mx, my, mx + v.getWidth(), my + v.getHeight());
                    //img.layout(300, 300, 300+100, 300 + 100);
                    tag = (Integer) v.getTag();
                    bx[tag] = mx ;
                    by[tag] = my ;
                    break;
                case MotionEvent.ACTION_UP:
                    layPlace = (RelativeLayout)findViewById(R.id.viewObj);
                    break;
            }
            return true;
        }
    };
    //用來重擺所有view的function
    private void placeAll()
    {
        for(int counter = 0;counter < number;counter++)
        {
            //newBotton[counter].layout(bx[counter], by[counter], bx[counter] + newBotton[counter].getWidth(), by[counter] + newBotton[counter].getHeight());
            newBotton[counter].addOnLayoutChangeListener(layoutChangeListener);
            //newBotton[counter].layout(bx[counter], by[counter], bx[counter] + newBotton[counter].getWidth(), by[counter] + newBotton[counter].getHeight());
            //newBotton[counter].layout(500, 500, 500 + newBotton[counter].getWidth(), 500 + newBotton[counter].getHeight());
            newBotton[counter].requestLayout();
        }
        Toast.makeText(MainActivity.this, "恢復正常位置", Toast.LENGTH_SHORT).show();
    }

    View.OnLayoutChangeListener layoutChangeListener = new View.OnLayoutChangeListener()
    {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
        {
            int tag = (Integer) v.getTag();
            //v.layout(bx[tag], by[tag], bx[tag] + newBotton[tag].getWidth(), by[tag] + newBotton[tag].getHeight());
            v.layout(bx[tag], by[tag], bx[tag] + newBotton[tag].getWidth(), by[tag] + newBotton[tag].getHeight());
            //Toast.makeText(MainActivity.this,"x=" + bx[tag] +"y=" + by[tag] +"Xend="+ (bx[tag] + newBotton[tag].getWidth()) +"Yend" + (by[tag] + newBotton[tag].getHeight()), Toast.LENGTH_SHORT).show();
            v.removeOnLayoutChangeListener(this);
        }
    };

    //計時器
    final Runnable runnable = new Runnable() {
        public void run() {
            // TODO Auto-generated method stub

            if (timer > 0) {
                timer--;
                aHandler.postDelayed(runnable, 1000);
            }else{
                if((fx-20<mx && fx +20 > mx) && (fy-30<my && fy+30 > my)) {
                    setVibrate(100);
                    newBotton[using].performClick();
                }
            }
        }
    };

    //選完按鈕的回呼
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    type[number] = extras.getInt("type");
                    if(type[number] != 5)
                    {
                        title[number] = extras.getString("title");
                        placeX[number] = extras.getDouble("placeX");
                        placeY[number] = extras.getDouble("placeY");
                        int imgId = extras.getInt("imgId");
                        /************新增按鈕開始*/
                        newBotton[number] = new ImageButton(MainActivity.this);
                        newBotton[number].setImageResource(imgId);
                        layPlace.addView(newBotton[number]);
                        RelativeLayout.LayoutParams layoutParams =
                                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        newBotton[number].setLayoutParams(layoutParams);
                        newBotton[number].setAdjustViewBounds(true);
                        newBotton[number].setMaxWidth(412);
                        newBotton[number].setMaxHeight(275);
                        newBotton[number].setTag(number);
                        newBotton[number].setOnTouchListener(imgListener);
                        newBotton[number].setOnClickListener(simiCkick);
                        placeAll();
                        if (type[number] == 1 || type[number] == 3)
                            needcount++;
                        number++;
                    }
                    else //信仰按鈕
                    {

                    }
                    /************新增按鈕結束*/
                }
            }
        }
    }

    //開始做按鈕
    private OnClickListener Img1OnClick1 = new OnClickListener() {

        public void onClick(View v) {
            PopupMenu popup = new PopupMenu(MainActivity.this,img1);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.popupmenu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.item1)//熱門地點
                    {
                        //Toast.makeText(MainActivity.this, "點地圖", Toast.LENGTH_SHORT).show();
                        /***開始嘗試放選項**/
                        Intent newIntent = new Intent();
                        //利用Bundle將資訊帶到第二頁
                        Bundle bundle = new Bundle();
                        bundle.putString("KEY_TEXT","Attend");
                        newIntent.putExtras(bundle);
                        newIntent.setClass(MainActivity.this,chooseAttend.class);
                        startActivityForResult(newIntent, 0);
                        /*嘗試結束*/
                    }
                    if (item.getItemId() == R.id.item2)//熱門地點
                    {
                        //Toast.makeText(MainActivity.this, "點地圖", Toast.LENGTH_SHORT).show();
                        /***開始嘗試放選項**/
                        Intent newIntent = new Intent();
                        //利用Bundle將資訊帶到第二頁
                        Bundle bundle = new Bundle();
                        bundle.putString("KEY_TEXT","Attend");
                        newIntent.putExtras(bundle);
                        newIntent.setClass(MainActivity.this,chooseAttend.class);
                        startActivityForResult(newIntent, 0);
                        /*嘗試結束*/
                    }
                    if (item.getItemId() == R.id.item3)//熱門地點
                    {
                        Toast.makeText(MainActivity.this, "點", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });

            popup.show(); //showing popup menu

        }

    };
    private OnClickListener Img1OnClick2 = new OnClickListener() {

        public void onClick(View v) {
            PopupMenu popup = new PopupMenu(MainActivity.this,img2);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.popupmenu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.item1)//熱門地點
                    {
                        //Toast.makeText(MainActivity.this, "點熱門", Toast.LENGTH_SHORT).show();
                        /***開始嘗試放選項**/
                        Intent newIntent = new Intent();
                        //利用Bundle將資訊帶到第二頁
                        Bundle bundle = new Bundle();
                        bundle.putInt("KEY_TEXT",2);
                        newIntent.putExtras(bundle);
                        newIntent.setClass(MainActivity.this,chooseBetter.class);
                        startActivityForResult(newIntent, 0);
                        /*嘗試結束*/
                    }
                    if (item.getItemId() == R.id.item2)//熱門地點
                    {
                        //Toast.makeText(MainActivity.this, "點地圖", Toast.LENGTH_SHORT).show();
                    }
                    if (item.getItemId() == R.id.item3)//熱門地點
                    {
                        Toast.makeText(MainActivity.this, "點", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });

            popup.show(); //showing popup menu

        }

    };

    private OnClickListener Img1OnClick3 = new OnClickListener() {

        public void onClick(View v) {
            PopupMenu popup = new PopupMenu(MainActivity.this,img3);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.popupmenu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.item1)//熱門地點
                    {
                        Toast.makeText(MainActivity.this, "點熱門", Toast.LENGTH_SHORT).show();
                        /***開始嘗試放選項**/
                        Intent newIntent = new Intent();
                        //利用Bundle將資訊帶到第二頁
                        Bundle bundle = new Bundle();
                        bundle.putInt("KEY_TEXT",3);
                        newIntent.putExtras(bundle);
                        newIntent.setClass(MainActivity.this,chooseBetter.class);
                        startActivityForResult(newIntent, 0);
                        /*嘗試結束*/
                    }
                    if (item.getItemId() == R.id.item2)//熱門地點
                    {
                        Toast.makeText(MainActivity.this, "點地圖", Toast.LENGTH_SHORT).show();
                    }
                    if (item.getItemId() == R.id.item3)//熱門地點
                    {
                        Toast.makeText(MainActivity.this, "點", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });

            popup.show(); //showing popup menu

        }

    };
    private OnClickListener Img1OnClick4 = new OnClickListener() {

        public void onClick(View v) {
            PopupMenu popup = new PopupMenu(MainActivity.this,img4);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.popupmenu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.item1)//熱門地點
                    {
                        Toast.makeText(MainActivity.this, "點熱門", Toast.LENGTH_SHORT).show();
                        /***開始嘗試放選項**/
                        Intent newIntent = new Intent();
                        //利用Bundle將資訊帶到第二頁
                        Bundle bundle = new Bundle();
                        bundle.putInt("KEY_TEXT",4);
                        newIntent.putExtras(bundle);
                        newIntent.setClass(MainActivity.this,chooseBetter.class);
                        startActivityForResult(newIntent, 0);
                        /*嘗試結束*/
                    }
                    if (item.getItemId() == R.id.item2)//熱門地點
                    {
                        Toast.makeText(MainActivity.this, "點地圖", Toast.LENGTH_SHORT).show();
                    }
                    if (item.getItemId() == R.id.item3)//熱門地點
                    {
                        Toast.makeText(MainActivity.this, "點", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });

            popup.show(); //showing popup menu

        }

    };
    private OnClickListener Img1OnClick5 = new OnClickListener() {

        public void onClick(View v) {
            PopupMenu popup = new PopupMenu(MainActivity.this,img5);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.popupmenu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.item1)//熱門地點
                    {
                        Toast.makeText(MainActivity.this, "點熱門", Toast.LENGTH_SHORT).show();
                        /***開始嘗試放選項**/
                        Intent newIntent = new Intent();
                        //利用Bundle將資訊帶到第二頁
                        Bundle bundle = new Bundle();
                        bundle.putInt("KEY_TEXT",5);
                        newIntent.putExtras(bundle);
                        newIntent.setClass(MainActivity.this,chooseBetter.class);
                        startActivityForResult(newIntent, 0);
                        /*嘗試結束*/

                    }
                    if (item.getItemId() == R.id.item2)//熱門地點
                    {
                        Toast.makeText(MainActivity.this, "點地圖", Toast.LENGTH_SHORT).show();
                    }
                    if (item.getItemId() == R.id.item3)//熱門地點
                    {
                        Toast.makeText(MainActivity.this, "點", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });

            popup.show(); //showing popup menu
        }

    };
    //景點被點擊

    private OnClickListener simiCkick = new OnClickListener() {

        public void onClick(View v) {
            PopupMenu popup = new PopupMenu(MainActivity.this,newBotton[using]);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.buttomenu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.item1)//熱門地點
                    {
                        Toast.makeText(MainActivity.this, "修改地點", Toast.LENGTH_SHORT).show();
                    }
                    if (item.getItemId() == R.id.item2)//熱門地點
                    {
                        Toast.makeText(MainActivity.this, "調整時間", Toast.LENGTH_SHORT).show();
                    }
                    if (item.getItemId() == R.id.item3)//熱門地點
                    {
                        Toast.makeText(MainActivity.this,"ID " + (newBotton[using].getTag()).toString() + "x " + bx[using] + "y " +by[using] +"type" + type[using], Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
            popup.show(); //showing popup menu
        }

    };

    //震動
    public void setVibrate(int time){
        Vibrator myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        myVibrator.vibrate(time);
    }
    //找出全部組合

    public void findAllTeam(int n)
    {
        if(n == needcount)
        {
            for(int counter = 0 ; counter< needcount ; counter++)
            {
                b[counter] = false;
                stack[counter] = counter;
                temp[counter] = counter;
                c = n;
                ip = 0;
            }//初始化完成
        }
        if(ip == needcount)
        {
            findBest();
            //每次更改排列完成
        }
        else
        {
            for(int i=0;i<c;i++)
            {
                if(b[i]==false)
                {
                    stack[ip] = temp[i];
                    b[i] = true;
                    ip+=1;
                    findAllTeam(n-1);
                    b[i] = false;
                    ip-=1;
                }
            }
        }

    }
    //比較並記錄最小距離的Function
    public void findBest()
    {
        Toast.makeText(MainActivity.this, "原本" + Arrays.toString(stack), Toast.LENGTH_SHORT).show();
        int[] temp1 = new int[10];
        for(int counter1 = 0;counter1 < needcount; counter1++)
        {
            temp1[counter1] = stack[counter1] ;
        }
        //先修正插入餐飲跟住宿
        if(outNumber > 0)
        {

            for(int counter = 0;counter < outNumber; counter++)
            {
                //先把數字正常化再右移
                for(int inside1 =0;inside1<number;inside1++)
                {
                    if(temp1[inside1] >= spacePlaceIndex[counter])
                        temp1[inside1]++;
                }
                //開始右移
                for(int inside = 9;inside > spacePlace[counter] ; inside-- )
                {
                    temp1[inside] = temp1[inside-1];
                }
                temp1[spacePlace[counter]] = spacePlaceIndex[counter];
            }
        }
        Toast.makeText(MainActivity.this, "修正後的排序" + Arrays.toString(temp1), Toast.LENGTH_SHORT).show();
        double sum = 0;
        for(int counter = 0; counter < number-1  ; counter ++)
        {
            sum += (Math.pow(placeX[temp1[counter]] - placeX[temp1[counter+1]],2)*1.21 + Math.pow(placeY[temp1[counter]] - placeY[temp1[counter+1]],2));
        }
        if(smallest == 0 || sum < smallest)
        {
            smallest = sum ;
            for(int counter = 0 ; counter < number ; counter++)
                bestDistanceWay[counter] = temp1[counter];
            Toast.makeText(MainActivity.this, "更新距離" , Toast.LENGTH_SHORT).show();
        }

        return ;

    }
    //比較並記錄最小距離的Function
    public void BigWindBlow()
    {
        /*大風吹  換位置*/
        //ImageButton[] newBotton = new ImageButton[10];
        /*新增按鈕的陣列  以及記錄按鈕數量的變數*/
        //int[] bx = new int[10];
        //int[] by = new int[10];
        /*這是用來記錄按鈕位置的*/
        //Double[] placeX = new Double[10];
        //Double[] placeY = new Double[10];
        //String[] title = new String[10];
        //String[] type = new String[10];
        //以上是要交換位置的變數
        ImageButton[] temp = new ImageButton[10];
        int[] temp1 = new int[10];
        int[] temp2 = new int[10];
        Double[] temp3 = new Double[10];
        Double[] temp4 = new Double[10];
        String[] temp5 = new String[10];
        int[] temp6 = new int[10];
        for(int counter = 0; counter < number  ; counter ++)
        {
            temp[counter] = newBotton[stack[counter]];
            temp1[counter] = bx[stack[counter]];
            temp2[counter] = by[stack[counter]];
            temp3[counter] = placeX[stack[counter]];
            temp4[counter] = placeY[stack[counter]];
            temp5[counter] = title[stack[counter]];
            temp6[counter] = type[stack[counter]];
        }
        /******要插入的話理論上在這裡插*******/
        //好像不是在這裡插
        /******預留空間*****/
        for(int counter = 0; counter < number  ; counter ++)
        {
            newBotton[counter] = temp[counter];
            bx[counter] = temp1[counter];
            by[counter] = temp2[counter];
            placeX[counter] = temp3[counter];
            placeY[counter] = temp4[counter];
            title[counter] = temp5[counter];
            type[counter] = temp6[counter];
            newBotton[counter].setTag(counter);
        }
        Toast.makeText(MainActivity.this, "全體賦值完成" , Toast.LENGTH_SHORT).show();

    }
    public void placeNewXY()
    {
        for(int counter = 0; counter < number  ; counter ++)
        {
            bx[bestDistanceWay[counter]] = 100;
            if(counter == 0)
                by[bestDistanceWay[counter]] = 5;
            else
                by[bestDistanceWay[counter]] = by[bestDistanceWay[counter-1]] + newBotton[bestDistanceWay[counter-1]].getHeight() + 8 ;
        }
        Toast.makeText(MainActivity.this, "全體重新設定xy完成" , Toast.LENGTH_SHORT).show();
    }
    public void getPlaceSpace()
    {   //概念  先排序再來找是否為餐飲
        int[] Yaxis = new int[10];
        int[] buttonTag = new int[10];
        spacePlace[0]=999;
        spacePlace[1]=999;
        spacePlace[2]=999;
        spacePlace[3]=999;

        for(int counter = 0; counter < number  ; counter ++)
        {
            Yaxis[counter] = by[counter];
            buttonTag[counter] = (Integer) newBotton[counter].getTag();
        }//初始化完成 開始排序
        for(int counter = 0; counter < number-1  ; counter ++)
        {
            for(int inside = 0;inside<number-counter-1;inside++)
            {
                if(Yaxis[inside]>Yaxis[inside+1])
                {
                    Yaxis[inside] = (Yaxis[inside] + Yaxis[inside + 1]) - (Yaxis[inside + 1] = Yaxis[inside]);
                    buttonTag[inside] = (buttonTag[inside] + buttonTag[inside + 1]) - (buttonTag[inside + 1] = buttonTag[inside]);
                }
            }
        }//排序完成  取得從上到下的Button在陣列中的順序
        Toast.makeText(MainActivity.this, "排序為" + Arrays.toString(buttonTag) +"index為"+Arrays.toString(spacePlaceIndex), Toast.LENGTH_SHORT).show();
        outNumber = 0;
        for(int counter = 0; counter < number  ; counter ++)
        {
            if( type[buttonTag[counter]] == 2 || type[buttonTag[counter]] == 4)
            {
                spacePlace[outNumber] = counter ;
                spacePlaceIndex[outNumber] = buttonTag[counter] ;
                outNumber++;
            }
        }// 做完了

        Toast.makeText(MainActivity.this, "例外有" + Arrays.toString(spacePlace) +"index為"+Arrays.toString(spacePlaceIndex), Toast.LENGTH_SHORT).show();
    }

}