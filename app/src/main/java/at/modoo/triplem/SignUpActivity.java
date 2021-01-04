package at.modoo.triplem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.modoo.triplem.R;

public class SignUpActivity extends AppCompatActivity {
    private TextView textEmailValidate,textPasswordValidate;
    private EditText editTextSignPassword,editTextSignEmail,editTextName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        textEmailValidate=findViewById(R.id.text_email_valiate);
        textPasswordValidate=findViewById(R.id.text_password_validate);
        editTextName=findViewById(R.id.editText_signName);
        editTextSignEmail=findViewById(R.id.editText_signEmail);
        editTextSignPassword = findViewById(R.id.editText_signPassword);
        validate(editTextSignEmail,editTextSignPassword);


    }

    private void validate(final EditText editTextSignEmail,final EditText editTextSignPassword) {
        editTextSignEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Pattern p = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
                Matcher m = p.matcher((editTextSignEmail).getText().toString());
                if ( !m.matches()){
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
            public void afterTextChanged(Editable s) { }
        });
        editTextSignPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                boolean b_digit = Pattern.compile(".*[0-9].*").matcher((editTextSignPassword).getText().toString()).matches();
                boolean b_lower = Pattern.compile(".*[a-z].*").matcher((editTextSignPassword).getText().toString()).matches();
                boolean b_upper = Pattern.compile(".*[A-Z].*").matcher((editTextSignPassword).getText().toString()).matches();
                boolean b_special = Pattern.compile(".*[@#$%^&+=*!].*").matcher((editTextSignPassword).getText().toString()).matches();
                boolean b_space = Pattern.compile(".*\\S.*").matcher((editTextSignPassword).getText().toString()).matches();
                boolean b_length = Pattern.compile(".{8,}.*").matcher((editTextSignPassword).getText().toString()).matches();


                if(!b_digit){
                    textPasswordValidate.setTextColor(getResources().getColor(R.color.red));
                    textPasswordValidate.setText(R.string.INVALID_PASSWORD_DIGIT);
                    textPasswordValidate.setVisibility(View.VISIBLE);
                }else if(!b_lower){
                    textPasswordValidate.setTextColor(getResources().getColor(R.color.red));
                    textPasswordValidate.setText(R.string.INVALID_PASSWORD_LOWER);
                    textPasswordValidate.setVisibility(View.VISIBLE);
                }else if(!b_upper){
                    textPasswordValidate.setTextColor(getResources().getColor(R.color.red));
                    textPasswordValidate.setText(R.string.INVALID_PASSWORD_UPPER);
                    textPasswordValidate.setVisibility(View.VISIBLE);
                }else if(!b_special){
                    textPasswordValidate.setTextColor(getResources().getColor(R.color.red));
                    textPasswordValidate.setText(R.string.INVALID_PASSWORD_SPECIAL);
                    textPasswordValidate.setVisibility(View.VISIBLE);
                }else if(!b_space){
                    textPasswordValidate.setTextColor(getResources().getColor(R.color.red));
                    textPasswordValidate.setText(R.string.INVALID_PASSWORD_SPACE);
                    textPasswordValidate.setVisibility(View.VISIBLE);
                }else if(!b_length){
                    textPasswordValidate.setTextColor(getResources().getColor(R.color.red));
                    textPasswordValidate.setText(R.string.INVALID_PASSWORD_LENGTH);
                    textPasswordValidate.setVisibility(View.VISIBLE);
                }else{
                    textPasswordValidate.setText(R.string.VALID_PASSWORD);
                    textPasswordValidate.setTextColor(getResources().getColor(R.color.yellow));
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    public void onClickExitSignUp(View view) {
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        setResult(-999,intent);
        finish();
    }
}
