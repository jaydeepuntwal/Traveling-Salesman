import java.io.DataInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

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


		Iterator iter = metrics.iterator();
		int i=0;
		int j =0;
		
		while(iter.hasNext()){
			
			JSONObject elements = (JSONObject) iter.next();
			JSONArray em = (JSONArray) elements.get("elements");
			
			
			Iterator iter2 = em.iterator();
			
			while(iter2.hasNext()){
				JSONObject d = (JSONObject) iter2.next();
				JSONObject d1 = (JSONObject) d.get("distance");

				Object[] arr = d1.values().toArray();

				dm[i][j] = Integer.parseInt(arr[1].toString());
				
				j++;
				
			}
			
			i++;
			
			
			
		}

		return dm;

	}
}
