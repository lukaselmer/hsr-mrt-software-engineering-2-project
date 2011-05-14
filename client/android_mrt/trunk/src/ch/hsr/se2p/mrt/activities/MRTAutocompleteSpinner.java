package ch.hsr.se2p.mrt.activities;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import ch.hsr.se2p.mrt.models.Customer;

/**
 * Creates a combobox with a autocomplete text and a button.
 */
public class MRTAutocompleteSpinner extends LinearLayout {
	private AutoCompleteTextView textView;

	protected MRTAutocompleteSpinner(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		initControls();
	}

	private void initControls() {
		setOrientation(HORIZONTAL);
		setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		initTextView(getContext());
		initButton();
	}

	private void initButton() {
		ImageButton button = new ImageButton(getContext());
		button.setImageResource(android.R.drawable.arrow_down_float);
		button.setOnClickListener(getButtonListener());
		this.addView(button, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}

	private OnClickListener getButtonListener() {
		return new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				textView.showDropDown();
			}
		};
	}

	private void initTextView(Context context) {
		textView = new AutoCompleteTextView(context);
		textView.setSingleLine();
		textView.setInputType(getInputTypeForTextView());
		textView.setRawInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
	}

	private int getInputTypeForTextView() {
		return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
				| InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT;
	}

	protected void setAdapter(ArrayAdapter<Customer> customers) {
		textView.setAdapter(customers);
	}

	protected String getText() {
		return textView.getText().toString();
	}

	protected void resetText() {
		setText("");
	}

	protected void setText(String text) {
		textView.setText(text);
	}
}
