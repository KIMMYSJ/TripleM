package at.modoo.triplem.Presenter;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface LoginContract {
    interface  View{

        void loginResult(boolean isLogedin);
    }

    interface Presenter{
        void attachView(View view);
        void detachView();

        void onLogin(String toString, String toString1);
        void onGoogleLogin(boolean isNew);
    }

}
