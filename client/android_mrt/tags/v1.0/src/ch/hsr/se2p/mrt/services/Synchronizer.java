package ch.hsr.se2p.mrt.services;

/**
 * The interface services passes down the method synchronize() to all its implementing classes.
 */
interface Synchronizer {

	/**
	 * Must be overridden by a method handling the synchronization.
	 */
	public void synchronize();
}