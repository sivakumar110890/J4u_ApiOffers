package activateOffers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class activateOfferTest {
	public static Properties prop;
	public static String category=null;
	public static String offerId;

	public static void main(String[] args) throws IOException, InterruptedException {
		loadPropertyfile();
		TransactionIdGeneration();
		//getOfferHTTPCall("Voice");
		//Thread.sleep(5000);
		activateOfferHTTPCall();
		Thread.sleep(5000);		

	}


	public static String getOfferHTTPCall(String category) throws IOException {
		URL url = new URL(prop.getProperty("url"));
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
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
		return OutputofGetoffer;
	}
	
	public static void activateOfferHTTPCall() throws IOException {
		URL activateofferUrl=new URL(prop.getProperty("activateOfferurl"));
		HttpURLConnection actconnection=(HttpURLConnection) activateofferUrl.openConnection();
		actconnection.setRequestMethod("POST");
		actconnection.setRequestProperty("Content-Type", "application/json");
		actconnection.setDoOutput(true);
		actconnection.setDoInput(true);
		DataOutputStream outputstreamact=new DataOutputStream(actconnection.getOutputStream());
		outputstreamact.writeBytes(activateOfferJSON(category));
		outputstreamact.flush();
		outputstreamact.close();
		
		int ResponseCodeOfActivateoffer=actconnection.getResponseCode();
		System.out.println(" Response code of the activate offer : "+ResponseCodeOfActivateoffer);
		
		BufferedReader activateOfferReader =new BufferedReader(new InputStreamReader(actconnection.getInputStream()));
		String linesact;
		StringBuilder actResponse=new StringBuilder();
		while((linesact=activateOfferReader.readLine()) !=null){
			actResponse.append(linesact);
		}
		activateOfferReader.close();
		System.out.println("Get offer response : "+category+actResponse.toString());
		actconnection.disconnect();
		
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
	
	
	public static String activateOfferJSON(String category) throws JSONException, IOException {
		JSONObject activateOfferJSON=new JSONObject(getOfferHTTPCall(category));
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
		return finalActivateOfferJSON;
	}



}