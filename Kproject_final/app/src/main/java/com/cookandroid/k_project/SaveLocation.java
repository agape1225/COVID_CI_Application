package com.cookandroid.k_project;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.cookandroid.k_project.Header.closeDrawer;
import static com.cookandroid.k_project.Header.openDrawer;
import static com.cookandroid.k_project.Header.redirectActivity;

public class SaveLocation extends AppCompatActivity implements OnMapReadyCallback {
    DrawerLayout drawerLayout;
    Button first,second,third,fourth;
    Button btnStart, btnSearch, btnStop, btnUpdate;
    EditText edtX , edtY;
    //TextView tvResult, tvTest;
    SQLiteDatabase sqlDB;
    myDBHelper myHelper;
    String username=((Header) Header.header).username;;
    TextView tvuser;
    static TimerTask timerTask;
    private GoogleMap mMap;
    private GpsTracker gpsTracker;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savelocation);
        drawerLayout = findViewById(R.id.drawer_layout);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {

            checkRunTimePermission();
        }

        btnStart = (Button) findViewById(R.id.btnStart);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        //tvResult = (TextView) findViewById(R.id.tvResult);
        //tvTest = (TextView) findViewById(R.id.tvTest);
        gpsTracker = new GpsTracker(SaveLocation.this);

        myHelper = new myDBHelper(this);



        timerTask = timerTaskMaker();

        final Timer timer = new Timer();
        timer.schedule(timerTask,0,5000);

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerTask.cancel();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerTask = timerTaskMaker();
                Log.e(".",".");
                timer.schedule(timerTask,0,5000);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = myHelper.getReadableDatabase();
                Cursor cursor;
                cursor = sqlDB.rawQuery("SELECT * FROM locationTBL;",null);

                String date = "";
                String locateX = "";
                String locateY = "";
                String result = "";

                while(cursor.moveToNext()){
                    date = cursor.getString(0);
                    locateX = cursor.getString(1);
                    locateY = cursor.getString(2);
                    result += date + "   " + locateX + "   " + locateY + "\r\n";
                }

                //tvResult.setText(result);

                cursor.close();
                sqlDB.close();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = myHelper.getReadableDatabase();
                Cursor cursor;
                cursor = sqlDB.rawQuery("SELECT * FROM locationTBL;",null);

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat mFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String time = mFormat.format(date);
                int currentMonth = Integer.parseInt(time.substring(5,7));
                int currentDay = Integer.parseInt(time.substring(8,10));

                while(cursor.moveToNext()){
                    String dateName = cursor.getString(0);
                    int buffMonth = Integer.parseInt(dateName.substring(5,7));
                    int buffDay = Integer.parseInt(dateName.substring(8,10));
                    int stateMonth = currentMonth;
                    int stateDay = currentDay;

                    if(currentMonth > buffMonth || currentDay < buffDay){
                        if(buffMonth == 2 || buffMonth == 4 || buffMonth == 6 || buffMonth == 9 || buffMonth == 11 )
                            stateMonth += 30;
                        else
                            stateDay += 31;
                    }

                    if(stateDay - buffDay == 14){
                        sqlDB.execSQL("DELETE FROM locationTBL WHERE gName = '" + dateName + "';");
                    }else if(stateDay - buffDay < 14)
                        break;


                }




            }
        });


        first=(Button)findViewById(R.id.btn_first);
        second=(Button)findViewById(R.id.btn_second);
        third=(Button)findViewById(R.id.btn_third);
        fourth=(Button)findViewById(R.id.btn_fourth);

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Header.class);
                startActivity(intent);
                finish();
            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoadingActivity.class);
                startActivity(intent);
                finish();
            }
        });
        fourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Header.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public TimerTask timerTaskMaker(){
        TimerTask tempTask = new TimerTask() {
            @Override
            public void run() {
                Log.e(".",".");
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat mFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String time = mFormat.format(date);

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

//                tvTest.setText(Double.toString(latitude) + Double.toString(longitude));

                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT INTO locationTBL VALUES ('" + time + "'," + latitude + "," + longitude + ");");
            }
        };
        return tempTask;
    }

    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;

        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();

        LatLng Current = new LatLng(latitude, longitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(Current);
        markerOptions.title("현재 위치");
        markerOptions.snippet("현재 위치");
        mMap.addMarker(markerOptions);

        sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM locationTBL;",null);

        String locateX = "";
        String locateY = "";
        String result = "";

        while(cursor.moveToNext()){

            locateX = cursor.getString(1);
            locateY = cursor.getString(2);

            double doubleX = Double.parseDouble(locateX);
            double doubleY = Double.parseDouble(locateY);

            MarkerOptions makerOptions = new MarkerOptions();
            makerOptions // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                    .position(new LatLng(doubleX , doubleY))
                    .title(".");


            // 2. 마커 생성 (마커를 나타냄)
            mMap.addMarker(makerOptions);

        }


        cursor.close();
        sqlDB.close();


        // 기존에 사용하던 다음 2줄은 문제가 있습니다.

        // CameraUpdateFactory.zoomTo가 오동작하네요.
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Current, 10));


    }

    /*public class myDBHelper extends SQLiteOpenHelper {

        public myDBHelper(Context context) {
            super(context, "weloDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            //sqLiteDatabase.execSQL("CREATE TABLE locationTBL(gName CHAR(30) PRIMARY KEY, locationX NUMERIC, locationY NUMERIC);");

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS locationTBL");
            onCreate(sqLiteDatabase);

        }
    }*/

    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                //위치 값을 가져올 수 있음
                ;
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(SaveLocation.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                }else {

                    Toast.makeText(SaveLocation.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(SaveLocation.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(SaveLocation.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(SaveLocation.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(SaveLocation.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(SaveLocation.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(SaveLocation.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }


    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }



        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SaveLocation.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void ClickMenu(View view){
        Header.openDrawer(drawerLayout);
        tvuser = findViewById(R.id.tvuser);
        tvuser.setText(username+"님,");
    }

    public void ClickLogo(View view){
        Header.closeDrawer(drawerLayout);
        //Header.redirectActivity(this, Chart.class);
        Intent intent = new Intent(getApplicationContext(), Chart.class);
        startActivity(intent);
        finish();
    }

    public void ClickSelfDiagnosis(View view){
        //Header.redirectActivity(this,Selfdiagnosis.class);
        Intent intent = new Intent(getApplicationContext(), Selfdiagnosis.class);
        startActivity(intent);
        finish();
    }

    public void ClickSetting(View view){
        //Header.redirectActivity(this, Setting.class);
        Intent intent = new Intent(getApplicationContext(), Setting.class);
        startActivity(intent);
        finish();
    }

    public void ClickLogout(View view){
        Header.logout(this);
    }
    protected void onPause(){
        super.onPause();
        Header.closeDrawer(drawerLayout);
    }

}