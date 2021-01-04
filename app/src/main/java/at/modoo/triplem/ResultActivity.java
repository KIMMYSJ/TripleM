package at.modoo.triplem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import at.modoo.triplem.R;

public class ResultActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private TextView tv_result;
    private ImageView iv_profile;
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
    }
}
