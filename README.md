[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.kedarkulk7/yahooFinanceAPI/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.github.kedarkulk7/yahooFinanceAPI)

# YahooFinanceAPI
This provides API to fetch historical data from Yahoo. 

# Usage

	public static void main(String[] args) {
		YahooFinanceAPI yahooFinanceAPI = new YahooFinanceAPI();
		JSONArray array = yahooFinanceAPI.getCandleData("APPL", "15m", "5d", null, null);
		
		for(int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			System.out.println("Time: "+ new Date(obj.getLong("time"))+", open:"+ obj.getDouble("open")+", high: "+ 
		obj.getDouble("high")+", low: "+obj.getDouble("low")+", close: "+ obj.getDouble("close")+", volume: "+ obj.getLong("volume"));
		}
	}

