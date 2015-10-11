package tweetmap;
import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.json.JSONException;
import org.json.JSONObject;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;

@ServerEndpoint("/TwitterEmitter")
public class TwitterEmitter {
	
	DynamoManager dynamoMngr = new DynamoManager();
	
	/**
     * @throws JSONException 
	 * @OnOpen allows us to intercept the creation of a new session.
     * The session class allows us to send data to the user.
     * In the method onOpen, we'll let the user know that the handshake was 
     * successful.
     */

	
    @OnOpen
    public void onOpen(Session session) throws JSONException {
        System.out.println(session.getId() + " has opened a connection"); 
        try {
        	Iterator<Item> iterator 
        		= this.dynamoMngr.getPoints("2015-10-10 00:00:00", "2015-10-11 10:18:14").iterator();
        	while (iterator.hasNext()) {
        		Item item = iterator.next();
        		JSONObject jo = new JSONObject();
        		jo.put("lon", item.getJSON("lon"));
        		jo.put("lat", item.getJSON("lat"));
        		session.getBasicRemote().sendText(jo.toString());
        	}
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException jx) {
        	jx.printStackTrace();
        }
    }
 
    /**
     * When a user sends a message to the server, this method will intercept the message
     * and allow us to react to it. For now the message is read as a String.
     */
    @OnMessage
    public void onMessage(String message, Session session){
    	
    	System.out.println(message);
    	try {
    		session.getBasicRemote().sendText("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
 
    /**
     * The user closes the connection.
     * 
     * Note: you can't send messages to the client from this method
     */
    @OnClose
    public void onClose(Session session){
        System.out.println("Session " +session.getId()+" has ended");
    }

}
