package yahoo.finance.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public class YahooFinanceAPI {

	/**
	 * 
	 * @param symbol Valid trading symbol of the stock as per yahoo finance website
	 * @param interval Valid intervals: [1m, 2m, 5m, 15m, 30m, 60m, 90m, 1h, 1d, 5d, 1wk, 1mo, 3mo]
	 * @param range Valid Ranges: [1d, 5d, 1mo, 3mo, 6mo, 1y, 2y, 5y, 10y, ytd, max]
	 * @param fromDate Start date
	 * @param toDate End Date
	 * @return JSONArray JSONArray containing candle data. Each jsonobject representing each candle and respective parameters can be fetched using "open", "high", "low", "close", "volume", "time" key
	 */
	public JSONArray getCandleData(String symbol, String interval, String range, Date fromDate, Date toDate) {
		JSONArray jsonArray = new JSONArray();
		String url = "https://query1.finance.yahoo.com/v8/finance/chart/"+symbol+"?interval="+interval;
		if(range == null && fromDate != null && toDate != null) {
			url += "&start="+fromDate.getTime()+"&end="+toDate.getTime();
		}else {
			url += "&range="+range;
		}
		URL obj;
		try {
			obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				try(BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));){
					String inputLine;
					StringBuffer response = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					JSONObject jobj = new JSONObject(response.toString());
					JSONArray timestampArray = jobj.getJSONObject("chart").getJSONArray("result").getJSONObject(0).getJSONArray("timestamp");
					JSONArray openArray = jobj.getJSONObject("chart").getJSONArray("result").getJSONObject(0).getJSONObject("indicators").getJSONArray("quote").getJSONObject(0).getJSONArray("open");
					JSONArray highArray = jobj.getJSONObject("chart").getJSONArray("result").getJSONObject(0).getJSONObject("indicators").getJSONArray("quote").getJSONObject(0).getJSONArray("high");
					JSONArray lowArray = jobj.getJSONObject("chart").getJSONArray("result").getJSONObject(0).getJSONObject("indicators").getJSONArray("quote").getJSONObject(0).getJSONArray("low");
					JSONArray closeArray = jobj.getJSONObject("chart").getJSONArray("result").getJSONObject(0).getJSONObject("indicators").getJSONArray("quote").getJSONObject(0).getJSONArray("close");
					JSONArray volumeArray = jobj.getJSONObject("chart").getJSONArray("result").getJSONObject(0).getJSONObject("indicators").getJSONArray("quote").getJSONObject(0).getJSONArray("volume");
					try {
						for(int i = 0; i < timestampArray.length(); i++) {
							JSONObject newObj = new JSONObject();
							newObj.put("time", timestampArray.getLong(i)*1000);
							newObj.put("open", openArray.isNull(i) ? 0.0 : openArray.getBigDecimal(i).setScale(2, RoundingMode.HALF_EVEN));
							newObj.put("high", highArray.isNull(i) ? 0.0 : highArray.getBigDecimal(i).setScale(2, RoundingMode.HALF_EVEN));
							newObj.put("low", lowArray.isNull(i) ? 0.0 : lowArray.getBigDecimal(i).setScale(2, RoundingMode.HALF_EVEN));
							newObj.put("close", closeArray.isNull(i) ? 0.0 : closeArray.getBigDecimal(i).setScale(2, RoundingMode.HALF_EVEN));
							newObj.put("volume", volumeArray.isNull(i) ? 0.0 : volumeArray.get(i));
							jsonArray.put(newObj);
						}
					} catch(Exception e) {
						System.out.println("YahooFinanceAPIExceptionLoop: " + e);
					}
				}
			}
			con.disconnect();
		} catch (Exception e) {			
			System.out.println("YahooFinanceAPIExceptionLoop: " + e);
		}
		return jsonArray;
	}
}
