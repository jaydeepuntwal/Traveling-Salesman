import java.io.DataInputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// KM update

public class GoogleMapsAPI {
	@SuppressWarnings("deprecation")
	public int[][] getDistance(String[] locations, String key) throws Exception {

		URL url;
		URLConnection urlConnection;
		DataInputStream inStream;

		StringWriter out = new StringWriter();
		LinkedHashMap m1 = new LinkedHashMap();
		JSONObject objwrite = new JSONObject();

		List<String> cities = new LinkedList<String>();
		int[][] dm = new int[locations.length][locations.length];

		for (String c : locations) {
			cities.add(c);
		}

		m1.put("allToAll", "true");

		objwrite.put("locations", cities);
		objwrite.put("options", m1);

		objwrite.writeJSONString(out);

		String request = objwrite.toJSONString();

		url = new URL(
				"http://www.mapquestapi.com/directions/v2/routematrix?&key="
						+ key + "&json=" + request);

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

		for (int i = 0; i < locations.length; i++) {

			JSONObject elements = (JSONObject) metrics.get(i);
			JSONArray em = (JSONArray) elements.get("elements");

			for (int j = 0; j < locations.length; j++) {

				JSONObject d = (JSONObject) em.get(j);
				JSONObject d1 = (JSONObject) d.get("distance");

				Object[] arr = d1.values().toArray();

				dm[i][j] = Integer.parseInt(arr[1].toString());
			}

		}

		return dm;

	}
}
