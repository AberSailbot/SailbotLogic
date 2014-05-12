/**
 * 
 */
package hardware;

import java.io.IOException;

import main.Position;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * @author kamilmrowiec
 *
 */
public class BoatdDriver implements BoatDriver {

	private String server = "http://localhost:2222";
	
	HttpClient client = HttpClients.createDefault();
	
	private JSONObject doGet(String path){
		HttpGet get = new HttpGet(server + path);
		try {
			
			return (JSONObject) JSONValue.parse(EntityUtils.toString(client.execute(get).getEntity()));
					
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private JSONObject doPost(String path, int value){
		
		HttpPost post = new HttpPost(server + path);
		
		try {
			StringEntity entity = new StringEntity("{\"value\": "+value+"}");
			post.setEntity(entity);
			post.setHeader("Content-Type", "application/json");
			return (JSONObject) JSONValue.parse(EntityUtils.toString(client.execute(post).getEntity()));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	private Integer readInteger(JSONObject obj){
		if(obj != null && obj.get("response") != null){
			try{
				return Integer.parseInt(obj.get("response").toString());
			}catch(NumberFormatException ex){
				return null;
			}
			
		}
		return null;
	}
	
	public BoatdDriver() {
	}

	@Override
	public Integer getHeading() {
		return readInteger(this.doGet("/heading"));
	}

	@Override
	public Integer getWind() {
		return readInteger(this.doGet("/wind"));
	}

	@Override
	public Position getPosition() {
		this.doGet("/position");
		return null;
	}

	@Override
	public void setRudder(int value) {
		System.out.println(this.doPost("/rudder", value));
	}

	@Override
	public void setSail(int value) {
		System.out.println(this.doPost("/sail", value));

	}

}
