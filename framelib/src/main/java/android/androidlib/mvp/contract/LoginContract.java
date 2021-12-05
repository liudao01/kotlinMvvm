package android.androidlib.mvp.contract;

/**
 * @author liuml
 * @explain
 * @time 2021/3/7 21:14
 */
public class LoginContract {
    public interface LoginModel{
        String getUsers(String token);
    }

    public interface LoginView {

        void loginSuccess(String userInfo);

        void loginFail(String msg);
    }

    public interface LoginPresenter {
        void getUsers(String token);
    }
}
