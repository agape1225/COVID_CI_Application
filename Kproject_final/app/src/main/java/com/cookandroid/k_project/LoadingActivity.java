package com.cookandroid.k_project;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LoadingActivity extends AppCompatActivity {
    private GpsTracker gpsTracker;
    public static Context LoadingActivity;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    Context context = this;
    final String TAG = "LoadingActivity";
    String addresstext;
    InputStream inputStream;
    double latitude;
    double longitude;
    public static Activity loadingactivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadingactivity=LoadingActivity.this;
        LoadingActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadingactivity);
        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {

            checkRunTimePermission();
        }
        gpsTracker = new GpsTracker(com.cookandroid.k_project.LoadingActivity.this);

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        String address = getCurrentAddress(latitude, longitude);
        //textview_address.setText(address);
//address=address.substring(6,8);
        Toast.makeText(com.cookandroid.k_project.LoadingActivity.this, "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
        String addressarray[]=address.split(" ");
        switch (addressarray[1]){
            case "서울특별시":
                inputStream = getResources().openRawResource(R.raw.seoul1);
                break;
            case "인천광역시":
                inputStream = getResources().openRawResource(R.raw.incheon);
                break;
            case "충청북도":
                inputStream = getResources().openRawResource(R.raw.chungbuk);
                break;
            case "충청남도":
                inputStream = getResources().openRawResource(R.raw.chungnam);
                break;
            case "경상북도":
                inputStream = getResources().openRawResource(R.raw.gyeongbuk);
                break;
            case "경상남도":
                inputStream = getResources().openRawResource(R.raw.gyeongbuk);
                break;
            case "경기도":
                inputStream = getResources().openRawResource(R.raw.gyeonggi);
                break;
            case "전라남도":
                inputStream = getResources().openRawResource(R.raw.jeonnam);
                break;
            case "전라북도":
                inputStream = getResources().openRawResource(R.raw.jeonbuk);
                break;
            case "강원도":
                inputStream = getResources().openRawResource(R.raw.gangwon);
                break;
            case "제주특별자치도":
                inputStream = getResources().openRawResource(R.raw.jeju);
                break;
            case "부산광역시":
                inputStream = getResources().openRawResource(R.raw.busan);
                break;
            case "대구광역시":
                inputStream = getResources().openRawResource(R.raw.daegu);
                break;
            case "대전광역시":
                inputStream = getResources().openRawResource(R.raw.daejeon);
                break;
            case "울산광역시":
                inputStream = getResources().openRawResource(R.raw.ulsan);
                break;
            case "세종특별자치시":
                inputStream = getResources().openRawResource(R.raw.sejong);
                break;
            case "광주광역시":
                inputStream = getResources().openRawResource(R.raw.gwangju);
                break;
        }

        Toast.makeText(com.cookandroid.k_project.LoadingActivity.this, "주소 " + addressarray[1], Toast.LENGTH_LONG).show();
        Log.d(TAG, "onCreate");

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Hospital_Clinic> clinics = xml_parse();
                ArrayList<Location> clinic_address = new ArrayList<Location>();
                for(int i = 0 ; i < clinics.size(); i++) {
                    Log.d(TAG, "convert");
                    clinic_address.add(addrToPoint(context, clinics.get(i).getAddress()));
                } // 병원 주소만 위도경보로 변환하여 모아놓음
                Intent intent = new Intent(com.cookandroid.k_project.LoadingActivity.this, Hospital_MainActivity.class);
                intent.putExtra("clinic", clinics);
                intent.putExtra("clinic_addr", clinic_address);
                startActivity(intent);
            }
        }).start();
    }

    private ArrayList<Hospital_Clinic> xml_parse() {
        ArrayList<Hospital_Clinic> clinicsList = new ArrayList<Hospital_Clinic>();
        //InputStream inputStream = getResources().openRawResource(R.raw.selectiveclinic_20201125);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        XmlPullParserFactory xmlPullParserFactory = null;
        XmlPullParser xmlPullParser = null;

        try {
            xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setInput(reader);

            Hospital_Clinic clinic = null;
            int eventType = xmlPullParser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        Log.i(TAG, "xml START");
                        break;
                    case XmlPullParser.START_TAG:
                        String startTag = xmlPullParser.getName();
                        Log.i(TAG, "Start TAG :" + startTag);
                        if(startTag.equals("Row")) {
                            clinic = new Hospital_Clinic();
                            Log.d(TAG, "clinic 추가");
                        }
                        else if(startTag.equals("연번")) {
                            clinic.setNumber(xmlPullParser.nextText());
                            Log.d(TAG, clinic.getNumber());
                            Log.d(TAG, "clinic 연변");
                        }
                        else if(startTag.equals("검체채취가능여부")) {
                            clinic.setSample(xmlPullParser.nextText());
                        }
                        /*else if(startTag.equals("시도")) {
                            clinic.setCity(xmlPullParser.nextText());
                        }
                        else if(startTag.equals("시군구")) {
                            clinic.setDistrict(xmlPullParser.nextText());
                        }*/
                        else if(startTag.equals("의료기관명")) {
                            clinic.setName(xmlPullParser.nextText());
                        }
                        else if(startTag.equals("주소")) {
                            clinic.setAddress(xmlPullParser.nextText());
                        }
                        else if(startTag.equals("대표전화번호")) {
                            clinic.setPhoneNumber(xmlPullParser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = xmlPullParser.getName();
                        Log.i(TAG,"End TAG : "+ endTag);
                        if (endTag.equals("Row")) {
                            clinicsList.add(clinic);
                        }
                        break;
                }
                try {
                    eventType = xmlPullParser.next();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try{
                if(reader !=null) reader.close();
                if(inputStreamReader !=null) inputStreamReader.close();
                if(inputStream !=null) inputStream.close();
            }catch(Exception e2){
                e2.printStackTrace();
            }
        }
        return clinicsList;
    }
    public static Location addrToPoint(Context context, String addr) {
        Location location = new Location("");
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocationName(addr,3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses != null) {
            for(int i = 0 ; i < addresses.size() ; i++) {
                Address lating = addresses.get(i);
                location.setLatitude(lating.getLatitude());
                location.setLongitude(lating.getLongitude());
            }
        }
        return location;
    } // 주소명으로 위도 경도를 구하는 메소드

    @Override
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

                    Toast.makeText(com.cookandroid.k_project.LoadingActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                }else {

                    Toast.makeText(com.cookandroid.k_project.LoadingActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }
    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(com.cookandroid.k_project.LoadingActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(com.cookandroid.k_project.LoadingActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(com.cookandroid.k_project.LoadingActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(com.cookandroid.k_project.LoadingActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(com.cookandroid.k_project.LoadingActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(com.cookandroid.k_project.LoadingActivity.this, REQUIRED_PERMISSIONS,
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
        Toast.makeText(com.cookandroid.k_project.LoadingActivity.this, "주소 : " + address.getAddressLine(0).toString(), Toast.LENGTH_LONG).show();

        return address.getAddressLine(0).toString()+"\n";

    }
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(com.cookandroid.k_project.LoadingActivity.this);
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
    @Override
    public void onBackPressed() {
        finish();

        super.onBackPressed();
    }
}
