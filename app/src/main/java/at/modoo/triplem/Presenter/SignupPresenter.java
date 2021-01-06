package at.modoo.triplem.Presenter;

import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.modoo.triplem.R;

public class SignupPresenter implements SignupContract.Presenter {
    private SignupContract.View view;
    private FirebaseFirestore firebaseFirestore;


    @Override
    public void attachView(SignupContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void validatePassword(String s) {
        boolean b_digit = Pattern.compile(".*[0-9].*").matcher(s).matches();
        boolean b_lower = Pattern.compile(".*[a-z].*").matcher(s).matches();
        boolean b_upper = Pattern.compile(".*[A-Z].*").matcher(s).matches();
        boolean b_special = Pattern.compile(".*[@#$%^&+=*!].*").matcher(s).matches();
        boolean b_space = Pattern.compile(".*\\S.*").matcher(s).matches();
        boolean b_length = Pattern.compile(".{8,}.*").matcher(s).matches();

        if(!b_digit){
            view.passwordValidateResult(R.string.INVALID_PASSWORD_DIGIT,false);
        }else if(!b_lower){

            view.passwordValidateResult(R.string.INVALID_PASSWORD_LOWER,false);

        }else if(!b_upper){
            view.passwordValidateResult(R.string.INVALID_PASSWORD_UPPER,false);

        }else if(!b_special){
            view.passwordValidateResult(R.string.INVALID_PASSWORD_SPECIAL,false);

        }else if(!b_space){
            view.passwordValidateResult(R.string.INVALID_PASSWORD_SPACE,false);

        }else if(!b_length){
            view.passwordValidateResult(R.string.INVALID_PASSWORD_LENGTH,false);

        }else{
            view.passwordValidateResult(R.string.VALID_PASSWORD,true);

        }
    }

    @Override
    public void validateEmail(String s) {

        Pattern p = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
        Matcher m = p.matcher(s);

        view.emailValidateResult(m.matches());
    }

    @Override
    public void clickSignUp(String name, String email, String password) {

    }


}
