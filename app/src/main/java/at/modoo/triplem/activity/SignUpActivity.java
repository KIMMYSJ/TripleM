package at.modoo.triplem.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import at.modoo.triplem.Presenter.SignupContract;
import at.modoo.triplem.Presenter.SignupPresenter;
import at.modoo.triplem.R;

public class SignUpActivity extends AppCompatActivity implements SignupContract.View {
    private TextView textEmailValidate,textPasswordValidate;
    private EditText editTextSignPassword,editTextSignEmail,editTextName;
    private Button btn_signup;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private  SignupPresenter signupPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signupPresenter = new SignupPresenter();
        signupPresenter.attachView(this);


        //init
        textEmailValidate=findViewById(R.id.text_email_valiate);
        textPasswordValidate=findViewById(R.id.text_password_validate);
        editTextName=findViewById(R.id.editText_signName);
        editTextSignEmail=findViewById(R.id.editText_signEmail);
        editTextSignPassword = findViewById(R.id.editText_signPassword);
        btn_signup = findViewById(R.id.btn_signUp);


        //event
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupPresenter.clickSignUp(editTextName.getText().toString(),editTextSignEmail.getText().toString(),editTextSignPassword.getText().toString());
            }
        });

        editTextSignEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signupPresenter.validateEmail(editTextSignEmail.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextSignPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signupPresenter.validatePassword(editTextSignPassword.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        signupPresenter.detachView();
    }

    public void onClickExitSignUp(View view) {
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        setResult(-999,intent);
        finish();
    }

    @Override
    public void emailValidateResult(boolean s) {
        if (!s){
            textEmailValidate.setTextColor(getResources().getColor(R.color.red));
            textEmailValidate.setText(R.string.INVALID_EMAIL);
            textEmailValidate.setVisibility(View.VISIBLE);
        }else{
            textEmailValidate.setVisibility(View.VISIBLE);
            textEmailValidate.setText(R.string.VALID_EMAIL);
            textEmailValidate.setTextColor(getResources().getColor(R.color.yellow));
        }
    }

    @Override
    public void passwordValidateResult(int result, boolean success) {
            textPasswordValidate.setVisibility(View.VISIBLE);
            textPasswordValidate.setText(result);
            if(success) textPasswordValidate.setTextColor(getResources().getColor(R.color.yellow));
            else textPasswordValidate.setTextColor(getResources().getColor(R.color.red));
    }
}
