package ch.hsr.se2p.mrt.services;

/**
 * The interface services passes down the method synchronize() to all its itself implementing classes. 
 */

interface Synchronizer {
	
	/**
	 * Must be overriden by a method handling the database synchronization. 
	 */
	public void synchronize();
}