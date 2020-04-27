package myapp.com.mapsactivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
{
    private GoogleMap mMap;
    private double[] latidudes,longidudes;
    private String[] category,name;
    private int K;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        K = getIntent().getIntExtra("K",0);
        latidudes = new double[K]; longidudes = new double[K];
        category = new String[K]; name = new String[K];
        for(int i=0; i<K; i++)
        {
            latidudes[i]= getIntent().getDoubleExtra("lat"+i,0.0);
            longidudes[i]= getIntent().getDoubleExtra("lon"+i,0.0);
            category[i]= getIntent().getStringExtra("cat"+i);
            name[i]= getIntent().getStringExtra("nm"+i);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        LatLng poi;
        for(int i=0; i<K; i++)
        {
            poi = new LatLng(latidudes[i], longidudes[i]);
            mMap.addMarker(new MarkerOptions().position(poi).title(name[i]).snippet(category[i]));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(poi));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12),2000,null);
            mMap.setTrafficEnabled(true);
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {return;}
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        final Button food = findViewById(R.id.food);
        final Button arts = findViewById(R.id.arts);
        final Button bars = findViewById(R.id.bar);
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                LatLng Poi;
                for(int i=0; i<K; i++)
                {
                    if(category[i].equals("Food"))
                    {Poi = new LatLng(latidudes[i], longidudes[i]);
                    mMap.addMarker(new MarkerOptions().position(Poi).title(name[i]).snippet(category[i]));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(Poi));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(12),2000,null);
                    mMap.setTrafficEnabled(true);}
                }
            }
        });
        arts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                LatLng Poi;
                for(int i=0; i<K; i++)
                {
                    if(category[i].equals("Arts & Entertainment"))
                    {Poi = new LatLng(latidudes[i], longidudes[i]);
                        mMap.addMarker(new MarkerOptions().position(Poi).title(name[i]).snippet(category[i]));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(Poi));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(12),2000,null);
                        mMap.setTrafficEnabled(true);}
                }
            }
        });
        bars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                LatLng Poi;
                for(int i=0; i<K; i++)
                {
                    if(category[i].equals("Bars"))
                    {Poi = new LatLng(latidudes[i], longidudes[i]);
                        mMap.addMarker(new MarkerOptions().position(Poi).title(name[i]).snippet(category[i]));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(Poi));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(12),2000,null);
                        mMap.setTrafficEnabled(true);}
                }
            }
        });
    }

}
