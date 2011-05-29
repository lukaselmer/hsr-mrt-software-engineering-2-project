package ch.hsr.se2p.mrt.network;

public class SynchronizationException extends Exception {
	private static final long serialVersionUID = -8213618553457784800L;

	public SynchronizationException(Exception e) {
		super(e);
	}
}
