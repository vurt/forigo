package com.vurt.syncthing;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.vurt.syncthing.bean.SyncthingConfig;

public class SyncthingClient {

	private String url;
	private String apiKey;

	private HttpClient httpClient;

	public SyncthingClient(String url, String apiKey) {
		this.url = url;
		this.apiKey = apiKey;
		this.httpClient = HttpClients.createDefault();
	}

	public void waitForValid() {
		int status = 0;
		HttpGet head = new HttpGet(this.url + "/rest/ping");
		do {
			try {
				Thread.sleep(300);
				HttpResponse response = httpClient.execute(head);
				status = response.getStatusLine().getStatusCode();
			} catch (HttpHostConnectException e) {
			} catch (IOException e) {
			} catch (InterruptedException e) {
			}
		} while (status != HttpStatus.SC_OK);
		System.out.println("同步服务连接成功");
	}

	public SyncthingConfig getConfig() {
		HttpGet httpget = new HttpGet(this.url + "/rest/config");
		HttpResponse response;
		try {
			response = httpClient.execute(httpget);
			SyncthingConfig config = JSON.parseObject(
					EntityUtils.toString(response.getEntity()),
					SyncthingConfig.class);
			return config;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setConfig(SyncthingConfig config) {
		HttpPost post = new HttpPost(this.url + "/rest/config");
		post.setHeader("X-API-Key", this.apiKey);

		try {
			post.setEntity(new StringEntity(JSON.toJSONString(config)));
			HttpResponse response = httpClient.execute(post);
			int status = response.getStatusLine().getStatusCode();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
