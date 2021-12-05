package android.androidlib.mvp.model;


import android.androidlib.mvp.contract.LoginContract;

/**
 * @author liuml
 * @explain
 * @time 2021/3/7 21:14
 */
public class LoginModel implements LoginContract.LoginModel {
    @Override
    public String getUsers(String token) {
        return "数据";
    }
}
