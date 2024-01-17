package com.example.webnovelreader;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
/*
Taken from: https://stackoverflow.com/questions/16511535/creating-a-three-states-checkbox-on-android/40939367#40939367
 */
public class TriStateCheckbox extends androidx.appcompat.widget.AppCompatCheckBox {
    static private final int NEGATIVE = -1;
    static private final int UNCHECKED = 0;
    static private final int POSITIVE = 1;
    private int state;

    public TriStateCheckbox(@NonNull Context context) {
        super(context);
        init();
    }

    public TriStateCheckbox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TriStateCheckbox(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        state = UNCHECKED;
        updateButton();

        setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (state) {
                    default:
                    case UNCHECKED:
                        state = POSITIVE;
                        break;
                    case POSITIVE:
                        state = NEGATIVE;
                        break;
                    case NEGATIVE:
                        state = UNCHECKED;
                        break;
                }
                updateButton();
            }
        });
    }

    private void updateButton() {
        int buttonDrawable = R.drawable.white_checkbox_unchecked;
        switch (state) {
            default:
            case UNCHECKED:
                buttonDrawable = R.drawable.white_checkbox_unchecked;
                break;
            case POSITIVE:
                buttonDrawable = R.drawable.white_checkbox_positive;
                break;
            case NEGATIVE:
                buttonDrawable = R.drawable.white_checkbox_negative;
                break;
        }
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        updateButton();
    }
}
