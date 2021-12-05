package android.androidlib.mvp.view;

/**
 * Created by liuml on 3/8/21 15:07
 */
public interface LoginView {
    void loginSuccess(String userInfo);

    void loginFail(String msg);
}
