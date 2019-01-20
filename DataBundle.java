public class DataBundle{
	private String medallion; 
	private String hack_license;
	private String pickup_datetime;
	private String dropoff_datetime;	
	private int trip_time_in_secs;	
	private double trip_distance;
	private double pickup_longitude;
	private double pickup_latitude;
	private double dropoff_longitude;
	private double dropoff_latitude;
	private String payment_type;
	private double fare_amount;
	private double surcharge;
	private  double mta_tax;
	private double tip_amount;
	private double tolls_amount;
	private double total_amount;

	public DataBundle(String data) {
		extractAndSave(data);
	}
	
	/**
	 * Given a line of data, extract the relevant and save for future reference
	 * @param data - a String representation of a single line of data
	 */
	private void extractAndSave(String data) {
		String[] splitArray = data.split(",");
		
		this.medallion = splitArray[0];
		this.hack_license = splitArray[1];
		this.pickup_datetime = splitArray[2];
		this.dropoff_datetime = splitArray[3];
		this.trip_time_in_secs = Integer.parseInt(splitArray[4]);
		this.trip_distance = Double.parseDouble(splitArray[5]);
		this.pickup_longitude = Double.parseDouble(splitArray[6]);
		this.pickup_latitude = Double.parseDouble(splitArray[7]);
		this.dropoff_longitude = Double.parseDouble(splitArray[8]);
		this.dropoff_latitude = Double.parseDouble(splitArray[9]);
		this.payment_type =splitArray[10];
		this.fare_amount = Double.parseDouble(splitArray[11]);
		this.surcharge = Double.parseDouble(splitArray[12]);
		this.mta_tax = Double.parseDouble(splitArray[13]);
		this.tip_amount = Double.parseDouble(splitArray[14]);
		this.tolls_amount = Double.parseDouble(splitArray[15]);
		this.total_amount = Double.parseDouble(splitArray[16]);
		
	}
	
	@Override
	public String toString() {
		return  this.hack_license +"\t" + this.trip_distance + "\t" 
				+ this.payment_type + "\t" + this.total_amount;
	}
	
	/**
	 * Checks if the record has a payment of type:
	 * Cash (CSH)
	 * @return  true if payment is of type CSH,
	 * 			false otherwise
	 */
	public boolean hasCSHpayment() {
		return this.payment_type.equals("CSH");
	}
	
	/**
	 * Checks if the record has the distance greater 
	 * than 0.6 miles
	 * @return true if it does, false otherwise
	 */
	public boolean hasDistanceGreaterThan() {
		return this.trip_distance > 0.6;
	}

}