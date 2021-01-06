package at.modoo.triplem.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import at.modoo.triplem.R;

public class ResultActivity extends AppCompatActivity implements OnMapReadyCallback {
    private FirebaseAuth auth;
    private TextView tv_result;
    private ImageView iv_profile;
    private FragmentManager fragmentManager;
    private SupportMapFragment mapFragment;
    private  GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        auth = FirebaseAuth.getInstance();
        tv_result = findViewById(R.id.tv_result);
        iv_profile = findViewById((R.id.iv_profile));
        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);


            }
        });

        tv_result.setText(auth.getCurrentUser().getDisplayName());
        Glide.with(this).load(String.valueOf(auth.getCurrentUser().getPhotoUrl())).into(iv_profile);


        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        LatLng location = new LatLng(37,126);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("sjkim");
        markerOptions.snippet("I'm the king");
        markerOptions.position(location);
        googleMap.addMarker(markerOptions);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,16));
    }
}
