package ch.hsr.se2p.mrt.activities;

import java.sql.Timestamp;
import java.util.List;

import android.widget.Spinner;
import android.widget.TextView;
import ch.hsr.se2p.mrt.models.Customer;
import ch.hsr.se2p.mrt.models.TimeEntry;
import ch.hsr.se2p.mrt.models.TimeEntryType;

class Measurement {
	private TimeEntry timeEntry;
	private boolean started = false;

	public Measurement() {
		this(true);
	}

	public Measurement(boolean start) {
		if (start)
			start();
	}

	private void start() {
		started = true;
		timeEntry = new TimeEntry(new Timestamp(System.currentTimeMillis()));
	}

	public TimeEntry getTimeEntry() {
		return timeEntry;
	}

	public TimeEntry stop(Spinner spinnerTimeEntryTypes, TextView textView, Integer gpsPositionId, MRTAutocompleteSpinner comboboxCustomers,
			List<Customer> customers) {
		started = false;
		timeEntry.setTimeStop(new Timestamp(System.currentTimeMillis()));
		setTimeEntryType(spinnerTimeEntryTypes);
		timeEntry.setDescription(textView.getText().toString());
		timeEntry.setGpsPositionId(gpsPositionId);
		setCustomer(comboboxCustomers, customers);
		return timeEntry;
	}

	private void setCustomer(MRTAutocompleteSpinner comboboxCustomers, List<Customer> customers) {
		if (comboboxCustomers.getText().length() != 0 && getCustomer(comboboxCustomers, customers) != null)
			getTimeEntry().setCustomerId(getCustomer(comboboxCustomers, customers).getId());
	}

	private Customer getCustomer(MRTAutocompleteSpinner comboboxCustomers, List<Customer> customers) {
		String textCustomer = comboboxCustomers.getText();
		if (textCustomer.length() == 0)
			return null;
		for (Customer customer : customers) {
			if (customer.toString().equals(textCustomer)) {
				return customer;
			}
		}
		return null;
	}

	private void setTimeEntryType(Spinner spinnerTimeEntryTypes) {
		if (spinnerTimeEntryTypes.getSelectedItemPosition() != 0)
			timeEntry.setTimeEntryTypeId(((TimeEntryType) spinnerTimeEntryTypes.getSelectedItem()).getId());
	}

	public boolean isStarted() {
		return started;
	}
}
