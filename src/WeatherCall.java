import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherCall implements Runnable {
	//Instance Variables:
	private String cityName;
	private String weather;
	private boolean isReady;
	
	//Constructors:
	WeatherCall(String cityName) {
		this.cityName = cityName;
	}
	
	//Methods:
	 
	public void run() {
		this.isReady = false;
		try {Thread.sleep(2000);}
		catch (InterruptedException ie) {}
		this.weather = this.getWeather(this.cityName);
		this.isReady = true;
		synchronized(this) {notifyAll();}
	}
	
	public String getWeather(String cityName) {
		if (cityName.equals("")) {return "";}
		try {
			URL myUrl = new URL("http://api.openweathermap.org/data/2.5/weather?q=" 
						+ cityName + "&APPID=9bd2aa0babc702ba77ad2b4828dfc8b2");

			InputStream is = myUrl.openStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = rd.readLine();
			//System.out.println(jsonText);
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			WeatherModel wm = mapper.readValue(jsonText, WeatherModel.class);

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}

			return wm.toString(); 


		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (FileNotFoundException fnfe) {
			return cityName + " is not a city.";
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} 
		return null; //Should never get here
	}
	
	public String getWeather() {return this.weather;}
	public boolean getIsReady() {return this.isReady;}
	
	public synchronized void waitToGetLock() {
		try {
			this.wait();
		} catch (InterruptedException ie) {}
	}

}


//"http://api.openweathermap.org/data/2.5/forecast?id=524901&APPID=9bd2aa0babc702ba77ad2b4828dfc8b2"

//api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=9bd2aa0babc702ba77ad2b4828dfc8b2