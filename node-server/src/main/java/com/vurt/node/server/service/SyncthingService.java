package com.vurt.node.server.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class SyncthingService {
	private CloseableHttpClient httpclient ;
	public SyncthingService(){
		httpclient = HttpClients.createDefault();
	}
	
	public String getConfig(){
		 HttpGet httpget = new HttpGet("http://localhost:8765/rest/config");  
         CloseableHttpResponse response;
		try {
			response = httpclient.execute(httpget);
			 return EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		return "";
	}
}
