package com.weather.model;

public class Weather {
	private Long pincode;
	private String areaName;
	private Double maxTemp;
	private Double minTemp;
	private String humidity;
	private String rainfall;
	private String wind;
	private boolean danger=Boolean.FALSE;

	public Weather(Long pincode, String areaName, Double maxTemp, Double minTemp, String humidity, String rainfall,
			String wind, boolean danger) {
		super();
		this.pincode = pincode;
		this.areaName = areaName;
		this.maxTemp = maxTemp;
		this.minTemp = minTemp;
		this.humidity = humidity;
		this.rainfall = rainfall;
		this.wind = wind;
		this.danger = danger;
	}

	public Long getPincode() {
		return pincode;
	}

	public void setPincode(Long pincode) {
		this.pincode = pincode;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Double getMaxTemp() {
		return maxTemp;
	}

	public void setMaxTemp(Double maxTemp) {
		this.maxTemp = maxTemp;
	}

	public Double getMinTemp() {
		return minTemp;
	}

	public void setMinTemp(Double minTemp) {
		this.minTemp = minTemp;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getRainfall() {
		return rainfall;
	}

	public void setRainfall(String rainfall) {
		this.rainfall = rainfall;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}

	public boolean isDanger() {
		return danger;
	}

	public void setDanger(boolean danger) {
		this.danger = danger;
	}
}


