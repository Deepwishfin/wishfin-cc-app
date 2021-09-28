package com.wishfin_credit_card.FontClasses;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class PoppinsRegular extends androidx.appcompat.widget.AppCompatTextView {

    public PoppinsRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public PoppinsRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PoppinsRegular(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),
                "Poppins-Regular.ttf");
        setTypeface(typeface, Typeface.NORMAL);

    }
}