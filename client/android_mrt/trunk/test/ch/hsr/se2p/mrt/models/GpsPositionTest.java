package ch.hsr.se2p.mrt.models;

import android.test.AndroidTestCase;

public class GpsPositionTest extends AndroidTestCase {
	private GpsPosition positionB, positionZ;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		positionB = new GpsPosition(46.949722, 7.439444);
		positionZ = new GpsPosition(47.377778, 8.540278);
	}
	
	public void testDistanceTo() {
		assertEquals(95800, positionB.distanceTo(positionZ), 100);
	}
}
