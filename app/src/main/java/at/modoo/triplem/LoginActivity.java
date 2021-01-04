package at.modoo.triplem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG = "modoo.at";
    private TextView imageBackground, imageLogo;
    private CheckBox loginCheckbox;
    private EditText editEmail,editPassword;
    private Button btnSignUp;
    private ImageButton btnLogin;
    private Button btn_google;
    private FirebaseAuth auth;
    private GoogleApiClient googleApiClient;

//    private LoginScreenViewModel mScreenViewModel;

    private static final int REQ_SIGN_GOOGLE = 100;
    private static final int REQ_SIGN_UP = 200;
    private static final int RES_SIGN_UP_S = 201;
    private static final int RES_SIGN_UP_F = 202;
//    private static final int REQ_


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
       // updateUI(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        googleInit();
        btn_google = findViewById(R.id.btn_google);
        editEmail = findViewById(R.id.editId);
        editPassword = findViewById(R.id.editPassword);
        imageLogo = findViewById(R.id.imageLogo);
        btnLogin = findViewById(R.id.btn_login);
        btnSignUp = findViewById(R.id.btn_signUp);
        loginCheckbox=findViewById(R.id.cb_loginInfo);
        loadLoginInfo(editEmail,editPassword,loginCheckbox);
        enableAutoLogin();
        permissionRequest();







        getWindow().setStatusBarColor(getResources().getColor(R.color.login_status_bar));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent,REQ_SIGN_UP);
            }
        });
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent((googleApiClient));
                startActivityForResult(intent, REQ_SIGN_GOOGLE);
            }
        });

    }

    @SuppressLint("MissingPermission")
    private void permissionRequest() {
        if (    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = { Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.INTERNET
                    ,Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, 21);
            }
        }
    }

    private void enableAutoLogin() {
        if (auth.getCurrentUser() != null&&loginCheckbox.isChecked()){
            // User is signed in (getCurrentUser() will be null if not signed in)
            permissionRequest();
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void googleInit(){
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        googleApiClient= new GoogleApiClient.Builder(this).enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions).build();
        auth =FirebaseAuth.getInstance();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //For google login auth intent
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case REQ_SIGN_GOOGLE:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if(result.isSuccess()){
                    GoogleSignInAccount account = result.getSignInAccount();
                    resultLogin(account);
                }
            case REQ_SIGN_UP:
                if(resultCode==RES_SIGN_UP_S){
                    Toast.makeText(getApplicationContext(),"Registered!",Toast.LENGTH_SHORT).show();
                }else if(resultCode == RES_SIGN_UP_F){
                    Toast.makeText(getApplicationContext(),"Failed to register",Toast.LENGTH_SHORT).show();
                }
        }

    }

    private void resultLogin(final GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    startActivity(intent);
                }else{
                    task.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this,"Fail",Toast.LENGTH_SHORT).show();
                            Log.i(TAG,e.getMessage());
                        }
                    });
                }
            }
        });

    }

    // Load the Saved Login info
    private void loadLoginInfo(EditText id, EditText password, CheckBox cb) {
        SharedPreferences preferences = getSharedPreferences("save", MODE_PRIVATE);
        if(preferences.getString("id","") != null) {
            id.setText(preferences.getString(getString(R.string.ID), ""));
            password.setText(preferences.getString(getString(R.string.PASSWORD), ""));
            Log.i(TAG,"LOADING INFO ...");
            cb.setChecked(true);
        }

    }
    public void saveLoginInfo(View view) {
        SharedPreferences preferences = getSharedPreferences("save",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Log.i(TAG,"saveLoginInfo");
        if(loginCheckbox.isChecked()){
            Log.i(TAG,"saveLoginInfo:if statement");
            editor.putString(getString(R.string.ID), editEmail.getText().toString());
            editor.putString(getString(R.string.PASSWORD),editPassword.getText().toString());
            editor.apply();
        }else{
            editor.putString(getString(R.string.ID), null);
            editor.putString(getString(R.string.PASSWORD),null);
            editor.apply();
        }
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
