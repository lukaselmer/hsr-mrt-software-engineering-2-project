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
	private ImageButton button;

	public MRTAutocompleteSpinner(Context context) {
		super(context);
		this.createChildControls(context);
	}

	public MRTAutocompleteSpinner(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		this.createChildControls(context);
	}

	private void createChildControls(Context context) {
		this.setOrientation(HORIZONTAL);
		this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		textView = new AutoCompleteTextView(context);
		textView.setSingleLine();
		textView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
				| InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
		textView.setRawInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		this.addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
		button = new ImageButton(context);
		button.setImageResource(android.R.drawable.arrow_down_float);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				textView.showDropDown();
			}
		});
		this.addView(button, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}

	public void setArrayAdapter(ArrayAdapter<Customer> customers) {
		textView.setAdapter(customers);
	}

	public String getText() {
		return textView.getText().toString();
	}

	public void setText(String text) {
		textView.setText(text);
	}
}
