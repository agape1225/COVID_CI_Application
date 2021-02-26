package com.cookandroid.k_project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

import static com.cookandroid.k_project.Header.closeDrawer;
import static com.cookandroid.k_project.Header.header;
import static com.cookandroid.k_project.Header.openDrawer;
import static com.cookandroid.k_project.Header.redirectActivity;


public class Hospital_MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    LoadingActivity loadingActivity = ((LoadingActivity)LoadingActivity.loadingactivity);
    private GoogleMap mgoogleMap;
    private ClusterManager<Hospital_MyItem> clusterManager;
    ArrayList<Hospital_Clinic> clinics;
    ArrayList<Location> clinic_address;
    Context context = this;
    final String TAG = "LogMainActivity";
    String tel;
    DrawerLayout drawerLayout;
    String username=((Header) header).username;;
    TextView tvuser;
    Button first,second,third,fourth;
    double latitude=((LoadingActivity)LoadingActivity.LoadingActivity).latitude;
    double longitude=((LoadingActivity)LoadingActivity.LoadingActivity).longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital__main);

        loadingActivity.finish();
        first=(Button)findViewById(R.id.btn_first_h);
        second=(Button)findViewById(R.id.btn_second_h);
        third=(Button)findViewById(R.id.btn_third_h);
        fourth=(Button)findViewById(R.id.btn_fourth_h);
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Header.class);
                startActivity(intent);
            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SaveLocation.class);
                startActivity(intent);
                finish();
            }
        });
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        drawerLayout = findViewById(R.id.drawer_layout);

        clinics = (ArrayList<Hospital_Clinic>)getIntent().getSerializableExtra("clinic");
        clinic_address = (ArrayList<Location>)getIntent().getSerializableExtra("clinic_addr");
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mgoogleMap = googleMap;
        clusterManager = new ClusterManager<>(this, mgoogleMap);

        mgoogleMap.setOnCameraIdleListener(clusterManager);
        mgoogleMap.setOnMarkerClickListener(clusterManager);

        LatLng location = new LatLng(latitude, longitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(location);
        markerOptions.title("현위치");
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.google_maps);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        mgoogleMap.addMarker(markerOptions);
        mgoogleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mgoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));

        mgoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                Log.d(TAG, "Load");
                LatLng latLng = new LatLng(latitude, longitude);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
                mgoogleMap.animateCamera(cameraUpdate);

                for(int i = 0 ; i < clinics.size(); i++) {
                    Hospital_MyItem clinicItem = new Hospital_MyItem(clinic_address.get(i).getLatitude(), clinic_address.get(i).getLongitude(),
                            clinics.get(i).getName());
                    clusterManager.addItem(clinicItem);
                } // 병원 개수만큼 item 추가
            }
        });
        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<Hospital_MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<Hospital_MyItem> cluster) {
                LatLng latLng = new LatLng(cluster.getPosition().latitude, cluster.getPosition().longitude);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                mgoogleMap.moveCamera(cameraUpdate);
                return false;
            }
        });
        mgoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String marker_number = null;
                for (int i = 0; i < clinics.size(); i++) {
                    if (clinics.get(i).findIndex(marker.getTitle()) != null) {
                        marker_number = clinics.get(i).findIndex(marker.getTitle());
                        Log.d(TAG, "marker_number " + marker_number);
                    }
                } // marker title로 clinic을 검색하여 number 반환받아옴
                final int marker_ID_number = Integer.parseInt(marker_number);
                Log.d(TAG, "marker number = " + String.valueOf(marker_ID_number));
                Log.d(TAG, "marker clinic name = " + clinics.get(marker_ID_number).getName());
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("병원정보");
                builder.setMessage(
                        "이름 : " + clinics.get(marker_ID_number - 1).getName() +
                                "\n주소 : " + clinics.get(marker_ID_number - 1).getAddress() +
                                "\n병원전화번호 : " +  (tel=(clinics.get(marker_ID_number - 1).getPhoneNumber())) +
                                "\n검체채취가능여부 : " + clinics.get(marker_ID_number - 1).getSample()
                );
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("전화걸기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tel)));
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });// 마커 클릭 시 Alert Dialog가 나오도록 설정
    }

    public void ClickMenu(View view){
        openDrawer(drawerLayout);
        tvuser = findViewById(R.id.tvuser);
        tvuser.setText(username+"님,");
    }

    public void ClickLogo(View view){
        closeDrawer(drawerLayout);
        redirectActivity(this,Header.class);
    }

    public void ClickSelfDiagnosis(View view){
        redirectActivity(this,Header.class);
    }

    public void ClickSetting(View view){
        redirectActivity(this,Setting.class);
    }

    public void ClickLogout(View view){
    }

    protected void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
    }
}
