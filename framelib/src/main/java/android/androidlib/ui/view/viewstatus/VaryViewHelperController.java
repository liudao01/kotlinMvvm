package android.androidlib.ui.view.viewstatus;


import android.androidlib.R;
import android.androidlib.utils.StringUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * @author ddnosh
 * @website http://blog.csdn.net/ddnosh
 */
public class VaryViewHelperController {

    private IVaryViewHelper helper;

    public VaryViewHelperController(View view) {
        this(new VaryViewHelper(view));
    }

    public VaryViewHelperController(IVaryViewHelper helper) {
        super();
        this.helper = helper;
    }

    public void showNetworkError(View.OnClickListener onClickListener) {
        View layout = helper.inflate(R.layout.view_status);
        TextView textView = (TextView) layout.findViewById(R.id.message_info);
        textView.setText(helper.getContext().getResources().getString(R.string.empty_view_hint));

        ImageView imageView = (ImageView) layout.findViewById(R.id.message_icon);
        imageView.setImageResource(R.drawable.ic_exception);

        if (null != onClickListener) {
            layout.setOnClickListener(onClickListener);

        }

        helper.showLayout(layout);
    }

    public void showError(String errorMsg, View.OnClickListener onClickListener) {
        View layout = helper.inflate(R.layout.view_status);
        TextView textView = (TextView) layout.findViewById(R.id.message_info);
        if (!StringUtils.isEmpty(errorMsg)) {
            textView.setText(errorMsg);
        } else {
            textView.setText(helper.getContext().getResources().getString(R.string.common_error_msg));
        }

        ImageView imageView = (ImageView) layout.findViewById(R.id.message_icon);
        imageView.setImageResource(R.drawable.ic_exception);

        if (null != onClickListener) {
            layout.setOnClickListener(onClickListener);

        }

        helper.showLayout(layout);
    }

    public void showEmpty(String emptyMsg, View.OnClickListener onClickListener) {
        View layout = helper.inflate(R.layout.view_status);
        TextView textView = (TextView) layout.findViewById(R.id.message_info);
        if (!StringUtils.isEmpty(emptyMsg)) {
            textView.setText(emptyMsg);
        } else {
            textView.setText(helper.getContext().getResources().getString(R.string.common_empty_msg));
        }

        ImageView imageView = (ImageView) layout.findViewById(R.id.message_icon);
        imageView.setImageResource(R.drawable.ic_exception);

        if (null != onClickListener) {
            layout.setOnClickListener(onClickListener);
        }

        helper.showLayout(layout);
    }

    public void showEmpty(int image, String emptyMsg, View.OnClickListener onClickListener) {
        View layout = helper.inflate(R.layout.view_status);
        TextView textView = (TextView) layout.findViewById(R.id.message_info);
        if (!StringUtils.isEmpty(emptyMsg)) {
            textView.setText(emptyMsg);
        } else {
            textView.setText(helper.getContext().getResources().getString(R.string.common_empty_msg));
        }

        ImageView imageView = (ImageView) layout.findViewById(R.id.message_icon);
        if (image != 0) {
            imageView.setImageResource(image);
        }

        if (null != onClickListener) {
            layout.setOnClickListener(onClickListener);

        }

        helper.showLayout(layout);
    }

    public void showEmpty(int image, String emptyMsg, String button, View.OnClickListener onClickListener) {

        View layout = helper.inflate(R.layout.view_web_status_error);
        TextView textView = (TextView) layout.findViewById(R.id.message_info);
        Button btEmpty;
        btEmpty = layout.findViewById(R.id.bt_empty);
        if (!StringUtils.isEmpty(emptyMsg)) {
            textView.setText(emptyMsg);
        } else {
            textView.setText(helper.getContext().getResources().getString(R.string.common_empty_msg));
        }
        if (!StringUtils.isEmpty(button)) {
            btEmpty.setText(button);
            btEmpty.setVisibility(View.VISIBLE);
        } else {
            btEmpty.setVisibility(View.GONE);
        }

        ImageView imageView = (ImageView) layout.findViewById(R.id.message_icon);
        if (image != 0) {
            imageView.setImageResource(image);
        }

        if (null != onClickListener) {
//            layout.setOnClickListener(onClickListener);
            btEmpty.setOnClickListener(onClickListener);
        }

        helper.showLayout(layout);
    }

    public void restore() {
        helper.restoreView();
    }

    public void showLoading(String msg) {
        View layout = helper.inflate(R.layout.view_status);
        if (!StringUtils.isEmpty(msg)) {
            TextView textView = (TextView) layout.findViewById(R.id.message_info);
            textView.setText(msg);
        }
        helper.showLayout(layout);
    }

    public void showLoading(int image, String msg) {
        View layout = helper.inflate(R.layout.view_status);
        if (!StringUtils.isEmpty(msg)) {
            TextView textView = (TextView) layout.findViewById(R.id.message_info);
            textView.setText(msg);
        }
        if (image != 0) {
            ImageView messageIcon;
            messageIcon = layout.findViewById(R.id.message_icon);
            messageIcon.setBackgroundResource(image);
        }

        helper.showLayout(layout);
    }

}
