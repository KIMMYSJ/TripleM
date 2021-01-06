package at.modoo.triplem.Presenter;

public interface SignupContract {
    interface  View{
        void emailValidateResult(boolean s);
        void passwordValidateResult(int result, boolean success);

    }

    interface Presenter{
        void attachView(View view);
        void detachView();
        void validatePassword(String s);
        void validateEmail(String s);
        void clickSignUp(String name, String email, String password);
    }
}
