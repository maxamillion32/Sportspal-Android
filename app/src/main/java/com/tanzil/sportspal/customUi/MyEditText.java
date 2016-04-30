package com.tanzil.sportspal.customUi;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;

import com.tanzil.sportspal.R;

public class MyEditText extends EditText {

	public MyEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}

	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);

	}

	public MyEditText(Context context) {
		super(context);
		init(null);
	}

	private void init(AttributeSet attrs) {
		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs,
					R.styleable.MyTextView);
			String fontName = a.getString(R.styleable.MyTextView_fontName);
			if (fontName != null) {
				// Typeface myTypeface = Typeface.createFromAsset(getContext()
				// .getAssets(), "fonts/" + fontName);
				//
				// setTypeface(myTypeface);
			}
			a.recycle();
		}
	}

}
