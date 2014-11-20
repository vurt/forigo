package com.vurt.syncthing;

import java.io.IOException;

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
import com.vurt.syncthing.bean.SystemInfo;
import com.vurt.syncthing.exception.SyncthingGetException;
import com.vurt.syncthing.exception.SyncthingUpdateException;

/**
 * 文件同步进程访问客户端，不支持自定义接口url和apikey
 * 
 * @author Vurt
 * 
 */
public class SyncthingClient {
	private String url;
	private String apiKey;

	private HttpClient httpClient;

	private static SyncthingClient instance;

	private static final String URL = "http://127.0.0.1:8765";

	private static final String API_KEY = "vph619gjv08qng1qmh44omsc6j2g1g";

	public static final SyncthingClient getInstance() {
		if (instance == null) {
			instance = new SyncthingClient(URL, API_KEY);
		}
		return instance;
	}

	private SyncthingClient(String url, String apiKey) {
		this.url = url;
		this.apiKey = apiKey;
		this.httpClient = HttpClients.createDefault();
	}

	/**
	 * 检测服务器当前是否可用
	 */
	public boolean isValid() {
		HttpGet head = new HttpGet(this.url + "/rest/ping");
		try {
			HttpResponse response = httpClient.execute(head);
			return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
		} catch (HttpHostConnectException e) {
		} catch (IOException e) {
		}
		return false;
	}

	/**
	 * 测试与文件同步进程的连接，并阻塞当前线程直到改进程可用
	 */
	public void waitForValid() {
		int status = 0;
		HttpGet head = new HttpGet(this.url + "/rest/ping");
		do {
			try {
				Thread.sleep(50);
				HttpResponse response = httpClient.execute(head);
				status = response.getStatusLine().getStatusCode();
			} catch (HttpHostConnectException e) {
			} catch (IOException e) {
			} catch (InterruptedException e) {
			}
		} while (status != HttpStatus.SC_OK);
	}

	/**
	 * 获取文件同步进程的完整配置
	 * 
	 * @return
	 */
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

	/**
	 * 修改文件同步进程的配置
	 * 
	 * @param config
	 *            新的配置
	 */
	public void setConfig(SyncthingConfig config)
			throws SyncthingUpdateException {
		HttpPost post = new HttpPost(this.url + "/rest/config");
		post.setHeader("X-API-Key", this.apiKey);

		try {
			post.setEntity(new StringEntity(JSON.toJSONString(config)));
			HttpResponse response = httpClient.execute(post);
			int status = response.getStatusLine().getStatusCode();
			if (status != HttpStatus.SC_OK) {
				throw new SyncthingUpdateException(response.getStatusLine()
						.getReasonPhrase());
			}
		} catch (ClientProtocolException e) {
			throw new SyncthingUpdateException(e);
		} catch (IOException e) {
			throw new SyncthingUpdateException(e);
		}
	}

	/**
	 * 重启同步服务器，并等待连接成功
	 */
	public void restart() {
		HttpPost post = new HttpPost(this.url + "/rest/restart");

		try {
			httpClient.execute(post);
			waitForValid();
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
	}

	/**
	 * 获取同步进程系统信息
	 */
	public SystemInfo getSystemInfo() {
		HttpGet httpget = new HttpGet(this.url + "/rest/system");
		HttpResponse response;
		try {
			response = httpClient.execute(httpget);
			SystemInfo info = JSON.parseObject(
					EntityUtils.toString(response.getEntity()),
					SystemInfo.class);
			return info;
		} catch (ClientProtocolException e) {
			throw new SyncthingGetException(e);
		} catch (IOException e) {
			throw new SyncthingGetException(e);
		}
	}
}
