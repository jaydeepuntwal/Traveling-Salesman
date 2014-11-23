import java.io.DataInputStream;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// KM updat

public class GoogleMapsAPI {
	@SuppressWarnings("deprecation")
	public int[][] getDistance(String[] locations, String key) throws Exception {

		URL url;
		URLConnection urlConnection;
		DataInputStream inStream;

		String cities = "";
		int[][] dm = new int[locations.length][locations.length];

		for (int i = 0; i < locations.length; i++) {

			cities += locations[i].replaceAll(" ", "%20");

			if (i != locations.length - 1) {
				cities += "|";
			}
		}

		url = new URL(
				"http://maps.googleapis.com/maps/api/distancematrix/json?&origins="
						+ cities + "&destinations=" + cities + "&key=" + key);
		
		System.out.println(url);
		
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

		for (int i = 0; i < metrics.size(); i++) {

			JSONObject elements = (JSONObject) metrics.get(i);
			JSONArray em = (JSONArray) elements.get("elements");

			for (int j = 0; j < metrics.size(); j++) {

				JSONObject d = (JSONObject) em.get(j);
				JSONObject d1 = (JSONObject) d.get("distance");

				Object[] arr = d1.values().toArray();

				dm[i][j] = Integer.parseInt(arr[1].toString());
			}

		}

		return dm;

	}
}
