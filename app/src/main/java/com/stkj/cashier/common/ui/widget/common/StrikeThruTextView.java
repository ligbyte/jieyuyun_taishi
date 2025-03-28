package com.stkj.cashier.common.ui.widget.common;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class StrikeThruTextView extends AppCompatTextView {

    public StrikeThruTextView(Context context) {
        super(context);
        init(context);
    }

    public StrikeThruTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StrikeThruTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setStrikeThruText(true);
    }

    public void setStrikeThruText(boolean enable) {
        TextPaint paint = getPaint();
        if (paint != null) {
            paint.setFlags(enable ? Paint.STRIKE_THRU_TEXT_FLAG : Paint.ANTI_ALIAS_FLAG);
        }
    }

}
