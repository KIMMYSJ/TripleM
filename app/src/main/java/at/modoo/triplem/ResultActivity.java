package at.modoo.triplem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import at.modoo.triplem.R;

public class ResultActivity extends AppCompatActivity {
    private TextView tv_result;
    private ImageView iv_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tv_result = findViewById(R.id.tv_result);
        iv_profile = findViewById((R.id.iv_profile));

        Intent intent = getIntent();
        String nickName = intent.getStringExtra("nickName");
        String photoUrl = intent.getStringExtra("photoUrl");
        tv_result.setText(nickName);
        Glide.with(this).load(photoUrl).into(iv_profile);
    }
}
