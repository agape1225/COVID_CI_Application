package com.cookandroid.k_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Header extends AppCompatActivity {

    private final long FINISH_INTERVAL_TIME = 2000;
    private long   backPressedTime = 0;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    LinearLayout buttongroup;
    DrawerLayout drawerLayout;
    Chart chartfragment;
    LoadingActivity loadingActivityfragment;
    SaveLocation savelocationfragment;
    Hospital hospitalfragment;
    Article articlefragment;
    Login login;
    Setting settingfragment;
    Selfdiagnosis  selfdiagnosisfragment;
    Hospital_MainActivity Hospital_MainActivityfragment;
    TextView tvuser;
    String username;
    SQLiteDatabase sqlDB;
    myDBHelper myHelper;
    int data, yesterday, today;
    String sname = "", sid= "", spw = "", snumber = "", semail = "", shomead = "", sjobad = "", sbirth = "", result = "";
    public static Context header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        header = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header);

        drawerLayout = findViewById(R.id.drawer_layout);
        chartfragment = new Chart();
        savelocationfragment = new SaveLocation();
        hospitalfragment=new Hospital();
        articlefragment=new Article();
        selfdiagnosisfragment=new Selfdiagnosis();
        settingfragment=new Setting();
        loadingActivityfragment = new LoadingActivity();
        login=new Login();
        tvuser = findViewById(R.id.tvuser);

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, chartfragment).commitAllowingStateLoss();

        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"값 : "+Integer.toString(data),Toast.LENGTH_LONG).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, chartfragment).commit();
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SaveLocation.class);
                startActivity(intent);
            }
        });

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LoadingActivity.class);
                startActivity(intent);
            }
        });

        Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, articlefragment).commit();
            }
        });


        myHelper = new myDBHelper(this);
        sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM loginTBL;",null);

        cursor.moveToPosition(Login.cursorindex);
        sid = cursor.getString(0);
        spw = cursor.getString(1);
        sname = cursor.getString(2);
        snumber = cursor.getString(3);
        semail = cursor.getString(4);
        shomead = cursor.getString(5);
        sjobad = cursor.getString(6);
        sbirth = cursor.getString(7);

        result += sid + "   " + spw + "   " + sname + "   " + snumber + "   " + semail + "   " + shomead + "   " + sjobad + "   " + sbirth +"\r\n";

        tvuser.setText((username=sname)+"님,");
        cursor.close();
        sqlDB.close();
    }

    public void onBackPressed() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, chartfragment).commit();
        long tempTime        = System.currentTimeMillis();
        long intervalTime    = tempTime - backPressedTime;
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            super.onBackPressed();
            moveTaskToBack(true);
            finish();
        }
        else
        {
            backPressedTime = tempTime;
            //밑에 줄 주석처리 꼭하기 안하면 안돼
            Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 누르면 꺼버린다.", Toast.LENGTH_SHORT).show();
        }
    }

    public class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context) {
            super(context, "weloDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE loginTBL ( ID CHAR(20) PRIMARY KEY,PW CHAR(20),NAME CHAR(20),NUMBER CHAR(20),EMAIL CHAR(50),HOMEADDRESS CHAR(200),JOBADDRESS CHAR(200),BIRTH CHAR(20));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS loginTBL");
            onCreate(db);

        }
    }

    public void onChangeFragment(int index) {
        if (index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, chartfragment).commit();
        }
        else if (index == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, articlefragment).commit();
        }
        else if (index == 3) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, articlefragment).commit();
        }
    }

    public void ClickMenu(View view){
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, chartfragment).commit();
        drawerLayout.closeDrawer(GravityCompat.START);
        buttongroup=(LinearLayout)findViewById(R.id.buttongroup);
        buttongroup.setVisibility(View.VISIBLE);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);

        }
    }
    public void ClickSelfDiagnosis(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, selfdiagnosisfragment).commit();
        drawerLayout.closeDrawer(GravityCompat.START);
        buttongroup=(LinearLayout)findViewById(R.id.buttongroup);
        buttongroup.setVisibility(View.GONE);
    }

    public void ClickSetting(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, settingfragment).commit();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void ClickLogout(View view){
        drawerLayout.closeDrawer(GravityCompat.START);
        logout(this);
    }

    public static void logout(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("로그아웃 하시겠습니까?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(activity,Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity,aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}