package activateOffers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;



public class activateOffers {
	public static Properties prop;
	public static String category=null;
	public static String offerId;

	public static void main(String[] args) throws IOException, InterruptedException {
		loadPropertyfile();
		TransactionIdGeneration();
		getOfferHTTPCall("Voice");
		Thread.sleep(5000);		

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
			System.out.println("Get offer Response : "+category+" "+ response.toString());
			String OutputofGetoffer=response.toString();
			System.out.println("============================================================================");
			connection.disconnect();

			JSONObject activateOfferJSON=new JSONObject(OutputofGetoffer);
			long MSISDN=activateOfferJSON.getLong("MSISDN");
			String Language=activateOfferJSON.getString("Language");
			String Channel=prop.getProperty("channel");
			String RefTransactionID=activateOfferJSON.getString("TransactionID");
			String RefNum=activateOfferJSON.getString("RefNum");
			
			JSONArray offersArray= activateOfferJSON.getJSONArray("Offers");
			for(int i=0;i<offersArray.length();i++) {
				JSONObject offer = offersArray.getJSONObject(i);
				offerId = offer.getString("OfferID");				
				System.out.println("OffOfferI " + offerId);
				break;
			}
			
			JSONObject setActivateOfferJSON = new JSONObject();
			//String dateTime = Utils.getDateAsString(new Date(), "yyyymmddHHMMssSSS");
			setActivateOfferJSON.put("ThirdPartyRef", RefNum);
			setActivateOfferJSON.put("username", "comj4u");
			setActivateOfferJSON.put("password", "j4u@456");
			setActivateOfferJSON.put("MSISDN", MSISDN);
			setActivateOfferJSON.put("Language", Language);
			setActivateOfferJSON.put("RefTransactionID", RefTransactionID);
			setActivateOfferJSON.put("RefNum", RefNum);
			setActivateOfferJSON.put("OfferID", offerId);
			setActivateOfferJSON.put("IsPurchased", prop.getProperty("IsPurchased"));
			String finalActivateOfferJSON=setActivateOfferJSON.toString();
			System.out.println(setActivateOfferJSON.toString());
			
			/*
			 * activateOfferJSON.put("OfferID", activateOfferJSON.get("OfferID"));
			 * activateOfferJSON.put("username", "comj4u");
			 * activateOfferJSON.put("password", "j4u@456");
			 * activateOfferJSON.put("Language", activateOfferJSON.getString("Language"));
			 * activateOfferJSON.put("Channel", activateOfferJSON.get("Channel"));
			 * activateOfferJSON.put("IsPurchased", prop.getProperty("IsPurchased"));
			 * activateOfferJSON.put("RefTransactionID",
			 * activateOfferJSON.get("TransactionID")); activateOfferJSON.put("RefNum",
			 * activateOfferJSON.get("RefNum")); activateOfferJSON.put("MSISDN",
			 * activateOfferJSON.get("MSISDN"));
			 */
			//System.out.println(activateOfferJSON.toString());
			
			
			DataOutputStream outputStream1 = new DataOutputStream(connection.getOutputStream());
			//outputStream.writeBytes(jsonPayload);//
			outputStream1.writeBytes(finalActivateOfferJSON);
			outputStream1.flush();
			outputStream1.close();
			
			
			int responseCodeactiateoffer = connection.getResponseCode();
			System.out.println("Response Code for activate offer : " + responseCodeactiateoffer);

			// Read the response from the input stream
			BufferedReader readerActivateOffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line1;
			StringBuilder responseActivateOffer = new StringBuilder();

			while ((line1 = readerActivateOffer.readLine()) != null) {
				responseActivateOffer.append(line);
			}
			readerActivateOffer.close();			
			System.out.println("Activate offer Response: "+category+" "+ responseActivateOffer.toString());
			String OutputofActivateOffer=responseActivateOffer.toString();
			System.out.println("============================================================================");
			connection.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	public static String Timestamp() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
		String formattedTime=now.format(formatter);
		return formattedTime;




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
		jsonobject.put("RefNum", Timestamp());
		//System.out.println("=======================================");
		System.out.println("Get offer Request : "+category+" "+jsonobject.toString());		
		String GetOfferRequest=jsonobject.toString();
		return GetOfferRequest;
	}



}