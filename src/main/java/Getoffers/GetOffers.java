package Getoffers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.UUID;
import org.json.JSONObject;



public class GetOffers {
	public static Properties prop;
	public static String category=null;

	public static void main(String[] args) throws IOException, InterruptedException {
		loadPropertyfile();
		TransactionIdGeneration();
		getOfferHTTPCall("Social");
		Thread.sleep(5000);
		getOfferHTTPCall("Voice");
		Thread.sleep(5000);
		getOfferHTTPCall("Data");
		Thread.sleep(5000);
		getOfferHTTPCall("Integrated");
		Thread.sleep(5000);
		getOfferHTTPCall("Hourly");
		Thread.sleep(5000);
		getOfferHTTPCall("MorningOffer");
		
	}


	public static void getOfferHTTPCall(String category) {
		try {
			URL url = new URL(prop.getProperty("url"));


			// Create the HttpURLConnection object
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// Set the request method to POST
			connection.setRequestMethod("POST");

			// Set the request content type
			connection.setRequestProperty("Content-Type", "application/json");

			// Enable output and input streams
			connection.setDoOutput(true);
			connection.setDoInput(true);

			// Create the JSON payload
			String jsonPayload = "{  \"username\" : \"comj4u\",\r\n" + " \"password\" : \"j4u@456\",\r\n"
					+ " \"MSISDN\" : 243123456789,\r\n" + " \"Category\": \"Hourly\",\r\n"
					+ " \"Channel\" : \"EVC\",\r\n" + " \"Language\" : \"en\",\r\n"
					+ " \"RefNum\" : \"MPESA_RF_2023_07_12_001\"\r\n" + "}";

			// Write the JSON payload to the request body
			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			//outputStream.writeBytes(jsonPayload);//
			outputStream.writeBytes(getOfferJson(category));
			outputStream.flush();
			outputStream.close();

			// Get the response code
			int responseCode = connection.getResponseCode();
			System.out.println("Response Code: " + responseCode);

			// Read the response from the input stream
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuilder response = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();			
			System.out.println("Get offer Response: "+category+" "+ response.toString());
			System.out.println("============================================================================");
			
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String TransactionIdGeneration() {
		UUID uuid = UUID.randomUUID();

		String TransactionId = uuid.toString();
		//System.out.println(TransactionId);
		return TransactionId;
	}

	public static Properties loadPropertyfile() throws IOException {
		FileInputStream fileinput=new FileInputStream(System.getProperty("user.dir")+"//config//Apioffersconfig.properties");
		prop=new Properties();
		prop.load(fileinput);
		return prop;

	}

	public static String getOfferJson(String category) {
		JSONObject jsonobject=new JSONObject();
		jsonobject.put("username", "comj4u");
		jsonobject.put("password","j4u@456");
		jsonobject.put("Language", prop.getProperty("language"));
		jsonobject.put("Channel", prop.getProperty("channel"));
		jsonobject.put("Category", category);
		jsonobject.put("MSISDN", prop.getProperty("msisdn"));
		jsonobject.put("RefNum", TransactionIdGeneration());
		//System.out.println("=======================================");
		System.out.println("Get offer Request : "+category+" "+jsonobject.toString());		
		String GetOfferRequest=jsonobject.toString();
		return GetOfferRequest;
	}
	
	public static void poolIdCacheClear() {
		JSONObject jsonobj=new JSONObject();
		jsonobj.put("op", "reload");
		jsonobj.put("username", "comj4u");
		jsonobj.put("password", "j4u@456");
		jsonobj.put("cache", "cellpoolidcache");
		jsonobj.put("MSISDN", "1234");
		jsonobj.put("RefNum", TransactionIdGeneration());
	
			
		
	}

}
