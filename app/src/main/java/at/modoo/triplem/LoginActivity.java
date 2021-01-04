package at.modoo.triplem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
        loginCheckbox = findViewById(R.id.cb_loginInfo);
        btn_google = findViewById(R.id.btn_google);
        editEmail = findViewById(R.id.editId);
        editPassword = findViewById(R.id.editPassword);
        imageLogo = findViewById(R.id.imageLogo);
        btnLogin = findViewById(R.id.btn_login);
        btnSignUp = findViewById(R.id.btn_signUp);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLoginInfo(editEmail.getText().toString(),editPassword.getText().toString());
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
        loadLoginInfo(editEmail,editPassword,loginCheckbox);
        googleInit();
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
        if(requestCode == REQ_SIGN_GOOGLE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                resultLogin(account);
            }
        }
        if(resultCode==-999){
            Toast.makeText(getApplicationContext(),"Quit!",Toast.LENGTH_SHORT).show();
        }
    }

    private void resultLogin(final GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Success",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    intent.putExtra("nickName",account.getDisplayName());
                    intent.putExtra("photoUrl",String.valueOf(account.getPhotoUrl()));

                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this,"Fail",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    // Load the Saved Login info
    private void loadLoginInfo(EditText id, EditText password, CheckBox cb) {
        SharedPreferences preferences = getSharedPreferences("save", MODE_PRIVATE);
        if(preferences.getString("id","") != null) {
            id.setText(preferences.getString("id", ""));
            password.setText(preferences.getString("pw", ""));
            cb.setChecked(true);
        }

    }

    private void saveLoginInfo(String email, String pw){
        SharedPreferences preferences = getSharedPreferences("save",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Log.i(TAG,"saveLoginInfo");
        if(loginCheckbox.isChecked()){
            Log.i(TAG,"saveLoginInfo:if statement");
            editor.putString(getString(R.string.ID), email);
            editor.putString(getString(R.string.PASSWORD),pw);
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
