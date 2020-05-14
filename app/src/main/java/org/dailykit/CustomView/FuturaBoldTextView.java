package org.dailykit.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class FuturaBoldTextView extends androidx.appcompat.widget.AppCompatTextView {

    public FuturaBoldTextView(Context context) {
        super(context);
        setFont();
    }

    public FuturaBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public FuturaBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Futura Heavy font.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
