package at.modoo.triplem.Presenter;

import android.content.Intent;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;
    @Override
    public void attachView(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {

        this.view = null;
    }

    @Override
    public void onLogin(String email, String password) {
       boolean isLogedin = FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).isSuccessful();
       view.loginResult(isLogedin);

    }

    @Override
    public void onGoogleLogin(boolean isNew) {
        if(isNew){
                //to do
        }

    }
}
