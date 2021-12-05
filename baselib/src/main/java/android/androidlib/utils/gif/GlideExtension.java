package android.androidlib.utils.gif;


import android.support.rastermill.FrameSequenceDrawable;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.annotation.GlideType;
import com.bumptech.glide.request.RequestOptions;


@com.bumptech.glide.annotation.GlideExtension
public class GlideExtension {

    private GlideExtension() {

    }

    final static RequestOptions DECODE_TYPE = RequestOptions
            .decodeTypeOf(FrameSequenceDrawable.class)
            .lock();

    @GlideType(FrameSequenceDrawable.class)
    public static RequestBuilder<FrameSequenceDrawable> asGif2(RequestBuilder<FrameSequenceDrawable> requestBuilder) {
//        RequestBuilder<android.support.rastermill.FrameSequenceDrawable
        return requestBuilder.apply(DECODE_TYPE);

    }

}
