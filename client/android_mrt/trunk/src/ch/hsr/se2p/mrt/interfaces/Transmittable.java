package ch.hsr.se2p.mrt.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Confirms a time entry on the server. Makes sure, that object is transmitted successfully and can be removed on client.
 */
public interface Transmittable {
	public JSONObject toJSONObject();

	public boolean isTransmitted();

	/**
	 * @param jsonObject
	 *            the object obtained by the request
	 * @return whether response was valid or not
	 * @throws JSONException
	 */
	public boolean processTransmission(JSONObject jsonObject) throws JSONException;
}

/**
4.1.4.3	Transmittable
Das Interfaces Transmittable ermöglicht es den implementierenden Klassen Objekte an den Server zu schicken. Zudem wird sichergestellt, dass Objekte nicht mehr als einmal auf dem Server gespeichert werden.
Methode	Beschreibung
toJSONObject()	Erstellt und ein neues JSONObject und setzt dort die jeweiligen Attribute der implementierenden Klasse. Danach wird dieses zurück gegeben.
isTransmitted()	Vergleicht die gespeicherte ID des 
processTransmission(JSONObject jsonObject)	Durch das erhaltene JSONObject wird der Hashcode verglichen und die ID gesetzt. Stimmt der Hashcode auf Server und Client Seite überein, wird dieser danach serverseitig entfernt. Es wird zurückgegeben ob dies erfolgreich war.
*/