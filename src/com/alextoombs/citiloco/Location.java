
public class Location {
	
	private String name;
	private double latitude;
	private double longtitude;
	private double price;
	
	public Location(String name, double latitude, double longtitude, double price) {
		this.name = name;
		this.latitude = latitude;
		this.longtitude = longtitude;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
}
