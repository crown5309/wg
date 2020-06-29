package com.heeexy.example.util;

import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

public class HttpClientUtils {

	public static String doGet(String url, Map<String, String> param) {

		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();

		String resultString = "";
		CloseableHttpResponse response = null;
		try {
			// 创建uri
			URIBuilder builder = new URIBuilder(url);
			if (param != null) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();

			// 创建http GET请求
			HttpGet httpGet = new HttpGet(uri);

			// 执行请求
			response = httpclient.execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultString;
	}

	public static String doGet(String url) {
		return doGet(url, null);
	}

	public static String doPost(String url, Map<String, String> param) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建参数列表
			if (param != null) {
				List<NameValuePair> paramList = new ArrayList<>();
				for (String key : param.keySet()) {
					paramList.add(new BasicNameValuePair(key, param.get(key)));
				}
				// 模拟表单
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
				httpPost.setEntity(entity);
			}
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultString;
	}

	public static String doPost(String url) {
		return doPost(url, null);
	}
	
	public static String doPostJson(String url, String json) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容
			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultString;
	}


	/**
	 * @Author 张满
	 * @Description post请求发送xml
	 * @Date 2019/8/7  12:19
	 * @Param [url, requestDataXml]
	 * @return java.lang.String
	 **/
	public static String doPostByXml(String url,String requestDataXml){
		CloseableHttpClient httpClient=null;
		CloseableHttpResponse httpResponse=null;

		//创建httpClient连接对象
		//httpClient = HttpClients.createDefault();
		httpClient = HttpClientUtils.createSSLClientDefault();
		//创建post请求连接对象
		HttpPost httpPost = new HttpPost(url);
		//创建连接请求参数对象，并设置连接参数
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(15000)		//连接服务器主机超时时间
				.setConnectionRequestTimeout(60000)  //连接请求超时时间
				.setSocketTimeout(6000)				//设置读取响应数据超时时间
				.build();

		//为httpPost请求设置参数
		httpPost.setConfig(requestConfig);
		//将上传参数存放到entity属性中
		httpPost.setEntity(new StringEntity(requestDataXml,"UTF-8"));
		//添加头信息
		httpPost.setHeader("Content-Type","text/xml");

		String result="";
		try {
			//发送请求
			httpResponse = httpClient.execute(httpPost);
			//获取返回内容
			HttpEntity httpEntity = httpResponse.getEntity();
			result = EntityUtils.toString(httpEntity, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}


	/**
	 * 创建一个SSL信任所有证书的httpClient对象
	 *
	 * @return
	 */
	public static CloseableHttpClient createSSLClientDefault() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

				@Override
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					// TODO Auto-generated method stub
					return true;
				}
				// 信任所有
		
				
			}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return HttpClients.createDefault();
	}


	/**
	 * @param reqURL
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String sendHttpPostRequest(String reqURL, Map<String, String> params, String data, String reqType, int timeOut) throws Exception {
		String responseContent = null;
		CloseableHttpResponse response = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(reqURL);
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeOut).setConnectTimeout(timeOut).setConnectionRequestTimeout(timeOut).build();
			httpPost.setConfig(requestConfig);
			// 绑定到请求 Entry
			if (reqType.equalsIgnoreCase("map")) {
				List<NameValuePair> formParams = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(formParams, Consts.UTF_8));
			} else if (reqType.equalsIgnoreCase("xml")) {
				StringEntity entity = new StringEntity(data, Consts.UTF_8);
				httpPost.setEntity(entity);
			} else if (reqType.equalsIgnoreCase("json")) {
				StringEntity entity = new StringEntity(data, Consts.UTF_8);
				entity.setContentType("application/json");
				httpPost.setEntity(entity);
			}
			response = httpclient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() != 200) {
				httpPost.abort();
				throw new Exception("渠道方接口通讯状态异常");
			}
			// 执行POST请求
			HttpEntity entity = response.getEntity(); // 获取响应实体
			if (null != entity) {
				responseContent = EntityUtils.toString(entity, Consts.UTF_8);
			}
			if (entity != null) {
				EntityUtils.consume(entity);
			}
		} catch (Exception e) {
			httpPost.abort();
			throw new Exception("渠道方接口通讯异常");
		} finally {
			if (response != null) {
				response.close();
			}
			httpPost.releaseConnection();
		}
		return responseContent;
	}



}
