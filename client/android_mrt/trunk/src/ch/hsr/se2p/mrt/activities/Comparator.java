package ch.hsr.se2p.mrt.activities;

import ch.hsr.se2p.mrt.models.Customer;

public class Comparator {

	public static Comparator distanceComparator() {
		return new Comparator() {

			public int compare(Customer one, Customer another) {
				if (one.getDistance() == null) {
					if (another.getDistance() == null) 
						return one.getLastName().compareTo(another.getLastName());
					return 1;
				}
				if (another.getDistance() == null) {
					return -1;
				}
				if (one.getDistance() > another.getDistance())
					return 1;
				if (one.getDistance().equals(another.getDistance())) 
					return one.getLastName().compareTo(another.getLastName());
				return -1;
			}
		};
	}
}
