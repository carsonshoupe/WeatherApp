import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherModel {
	//Instance Variables:
	private String precipitation;
	private float temp;
	private float tempMin;
	private float tempMax;
	private float windSpeed;
	private String country;
	private String city;
	
	//Constructor:
	
	//Methods:
	public String getPrecipitation() {return this.precipitation;}
	public float getTemp() {return this.temp;}
	public float getTempMin() {return this.tempMin;}
	public float getTempMax() {return this.tempMax;}
	public float getWindSpeed() {return this.windSpeed;}
	public String getCountry() {return this.country;}
	@JsonGetter("name")
	public String getCity() {return this.city;}
	
	@JsonProperty("weather")
	private void unpackWeather(Map<String, String>[] weather) {
	    this.precipitation = weather[0].get("main");
	}
	
	@JsonProperty("main")
	private void unpackMain(Map<String, Float> main) {
	    this.temp = main.get("temp");
	    this.tempMin = main.get("temp_min");
	    this.tempMax = main.get("temp_max");
	}
	
	@JsonProperty("wind")
	private void unpackWind(Map<String, Float> wind) {
	    this.windSpeed = wind.get("speed");
	}
	
	@JsonProperty("sys")
	private void unpackSys(Map<String, String> sys) {
	    this.country = sys.get("country");
	}
	
	@Override
	public String toString() {
		return this.getCity() + ", " + this.getCountry() + ": \n" 
				+ "Current Temp: " + kelvinToFahrenheit(this.getTemp()) + "F \n"
				+ "High: " + kelvinToFahrenheit(this.getTempMax()) + "F \n"
				+ "Low: " + kelvinToFahrenheit(this.getTempMin()) + "F \n"
				+ "Precipitation: " + this.getPrecipitation() + "\n"
				+ "Wind Speed: " + this.getWindSpeed() + "\n";
	}
	
	
	public int kelvinToFahrenheit(float kelvin) {return (int) ((kelvin - 273.15) * 9.0/5) + 32;}


}
