package at.modoo.triplem.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.View;
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

import at.modoo.triplem.Presenter.LoginContract;
import at.modoo.triplem.Presenter.LoginPresenter;
import at.modoo.triplem.R;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, LoginContract.View {
    public static final String TAG = "modoo.at";
    private LoginPresenter loginPresenter;
    private TextView imageBackground, imageLogo;
    private CheckBox loginCheckbox;
    private EditText editEmail,editPassword;
    private Button btnSignUp;
    private ImageButton btnLogin;
    private Button btn_google;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
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

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setStatusBarColor(getResources().getColor(R.color.login_status_bar));

        //Init Views
        btn_google = findViewById(R.id.btn_google);
        editEmail = findViewById(R.id.editId);
        editPassword = findViewById(R.id.editPassword);
        imageLogo = findViewById(R.id.imageLogo);
        btnLogin = findViewById(R.id.btn_login);
        btnSignUp = findViewById(R.id.btn_signUp);
        loginCheckbox=findViewById(R.id.cb_loginInfo);
        auth =FirebaseAuth.getInstance();

        //Init
        loginPresenter = new LoginPresenter();
        loginPresenter.attachView(this);
        googleInit();


        loadLoginInfo(editEmail,editPassword,loginCheckbox);
        enableAutoLogin();
        permissionRequest();


        //event
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPresenter.onLogin(editEmail.getText().toString(),editPassword.getText().toString());
                clickLogin();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.detachView();
    }

    private void googleInit(){
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        googleApiClient= new GoogleApiClient.Builder(this).enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions).build();

    }

    private void enableAutoLogin() {
        if (auth.getCurrentUser() != null&&loginCheckbox.isChecked()){
            // User is signed in (getCurrentUser() will be null if not signed in)
            permissionRequest();
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            startActivity(intent);

        }
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
    public void clickLogin() {

        SharedPreferences preferences = getSharedPreferences("save", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Log.i(TAG, "saveLoginInfo");
        if (loginCheckbox.isChecked()) {
            Log.i(TAG, "saveLoginInfo:if statement");
            editor.putString(getString(R.string.ID), editEmail.getText().toString());
            editor.putString(getString(R.string.PASSWORD), editPassword.getText().toString());
            editor.apply();
        } else {
            editor.putString(getString(R.string.ID), null);
            editor.putString(getString(R.string.PASSWORD), null);
            editor.apply();
        }

    }



    @SuppressLint("MissingPermission")
    private void permissionRequest() {
        if (    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = { Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                    ,Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};

                requestPermissions(permissions, 21);


        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
                    resultGoogleLogin(account);
                }
            case REQ_SIGN_UP:
                if(resultCode==RES_SIGN_UP_S){
                    Toast.makeText(getApplicationContext(),"Registered!",Toast.LENGTH_SHORT).show();
                }else if(resultCode == RES_SIGN_UP_F){
                    Toast.makeText(getApplicationContext(),"Failed to register",Toast.LENGTH_SHORT).show();
                }
        }

    }

    private void resultGoogleLogin(final GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                    loginPresenter.onGoogleLogin(isNew);
                    Log.d(TAG, "onComplete: " + (isNew ? "new user" : "old user"));
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




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),connectionResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
    }


    @Override
    public void loginResult(boolean isLogedin) {
        if(isLogedin){
            Intent intent = new Intent(getApplicationContext(),ResultActivity.class);

        }else{
            Toast.makeText(getApplicationContext(),"Login Failed, check your password once again",Toast.LENGTH_SHORT).show();
        }
    }
}
