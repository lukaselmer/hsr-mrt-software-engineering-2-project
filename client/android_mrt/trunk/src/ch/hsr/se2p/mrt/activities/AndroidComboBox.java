package ch.hsr.se2p.mrt.activities;

import ch.hsr.se2p.mrt.models.Customer;
import android.content.Context;
import android.database.Cursor;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

public class AndroidComboBox extends LinearLayout {

	private AutoCompleteTextView textView;
	private ImageButton button;

	public AndroidComboBox(Context context) {
		super(context);
		this.createChildControls(context);
	}

	public AndroidComboBox(Context context, AttributeSet attributeSet) {
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
		this.addView(button, new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
	}
	
	 /**
	    * Sets the source for DDLB suggestions.
	    * Cursor MUST be managed by supplier!!
	    * @param source Source of suggestions.
	    * @param column Which column from source to show.
	    */
	   public void setSuggestionSource(ArrayAdapter<Customer> customers, String column) {
	       String[] from = new String[] { column };
	       int[] to = new int[] { android.R.id.text1 };
	       
//	       SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this.getContext(),
//	                       android.R.layout.simple_dropdown_item_1line, source, from, to);
	       // this is to ensure that when suggestion is selected
	       // it provides the value to the textbox
//	       cursorAdapter.setStringConversionColumn(source.getColumnIndex(column));
	       textView.setAdapter(customers);
	   }

	   /**
	    * Gets the text in the combo box.
	    *
	    * @return Text.
	    */
	   public String getText() {
	       return textView.getText().toString();
	   }

	   /**
	    * Sets the text in combo box.
	    */
	   public void setText(String text) {
	       textView.setText(text);
	   }
}