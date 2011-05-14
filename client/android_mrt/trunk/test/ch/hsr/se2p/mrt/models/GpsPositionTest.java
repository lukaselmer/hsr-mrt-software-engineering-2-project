package ch.hsr.se2p.mrt.models;

import android.test.AndroidTestCase;

public class GpsPositionTest extends AndroidTestCase {
	private GpsPosition positionA, positionB, positionZ;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		positionA = new GpsPosition(46.949720, 7.439440);
		positionB = new GpsPosition(46.949722, 7.439444);
		positionZ = new GpsPosition(47.377778, 8.540278);
	}

	public void testDistanceTo() {
		assertEquals(0.0, positionA.distanceTo(positionA));
		assertEquals(1.0, positionA.distanceTo(positionB), 1);
		assertEquals(95800.0, positionB.distanceTo(positionZ), 100);
	}
}
