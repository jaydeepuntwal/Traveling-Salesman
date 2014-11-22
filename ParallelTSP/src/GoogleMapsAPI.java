import java.io.DataInputStream;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// KM updat

public class GoogleMapsAPI {
	@SuppressWarnings("deprecation")
	public int getDistance(String locationA, String locationB) throws Exception {

		try {
			URL url;
			URLConnection urlConnection;
			DataInputStream inStream;

			url = new URL(
					"http://maps.googleapis.com/maps/api/distancematrix/json?&origins="
							+ locationA.replaceAll(" ", "%20")
							+ "&destinations="
							+ locationB.replaceAll(" ", "%20"));
			urlConnection = url.openConnection();
			inStream = new DataInputStream(urlConnection.getInputStream());

			String buffer;
			String json = "";
			while ((buffer = inStream.readLine()) != null) {
				json += buffer;
			}

			json = json.trim();
			inStream.close();

			JSONParser parser = new JSONParser();

			Object obj = parser.parse(json);
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray metrics = (JSONArray) jsonObject.get("rows");
			JSONObject elements = (JSONObject) metrics.get(0);
			JSONArray em = (JSONArray) elements.get("elements");
			JSONObject d = (JSONObject) em.get(0);
			JSONObject d1 = (JSONObject) d.get("distance");

			Object[] arr = d1.values().toArray();

			String temp = arr[1].toString();

			return Integer.parseInt(temp);

		} catch (Exception ex) {
			throw new Exception("Illegal City! " + locationA + " OR " + locationB);
		}
	}
}
