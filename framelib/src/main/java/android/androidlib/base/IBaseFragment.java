package android.androidlib.base;

import android.app.Fragment;
import android.os.Bundle;

/**
 * @author liuml
 * @explain
 * @time 2017/12/8 10:46
 */

public abstract class IBaseFragment extends Fragment implements ICallback{


    @Override
    public int setLayoutId() {
        return 0;
    }

    @Override
    public void initVariables() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {

    }

    @Override
    public void doBusiness() {

    }
}
