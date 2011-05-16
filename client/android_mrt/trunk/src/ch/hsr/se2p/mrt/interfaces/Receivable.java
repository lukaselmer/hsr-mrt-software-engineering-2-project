package ch.hsr.se2p.mrt.interfaces;

import java.sql.Timestamp;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Confirms a time entry on the server. Makes sure, that object is transmitted successfully and can be removed on client.
 */
public interface Receivable {
	public int getIdOnServer();

	public Timestamp getUpdatedAt();

	/**
	 * @param jsonObject
	 *            the object obtained by the request
	 * @return whether response was valid or not
	 * @throws JSONException
	 */
	public boolean fromJSON(JSONObject jsonObject) throws JSONException;
}


/**
4.1.4.2	Receivable
Das Interfaces Receivable stellt Methoden zur Verfügung um die Übertragung von Elementen vom Server an den Client zu ermöglichen.
Methode	Beschreibung
getIdOnServer()	Gibt die ID zurück, welche vom Server gesetzt wurde.
getUpdatedAt()	Gibt den Zeitpunkt der letzten Änderung in Form eines Timestamps zurück.
fromJSON(JSONObject jsonObject)	Dem erhaltenen JSONObject können die Attribute der implementierenden Klasse entnommen werden. Die Methode returniert ob dies erfolgreich verlaufen ist.

*/