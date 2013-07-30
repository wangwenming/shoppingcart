package com.baiyjk.shopping.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.CookieStore;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class HttpClientUtil implements HttpApi {
	private static final String DEBUG_TAG = "HttpClientUtils";
	private static final String CHARSET_UTF8 = "UTF-8";
//	private final String host = "http://www.bytest.com";
//	public final String PHPSESSIONKEY = "BAIYANG";
	private final String host = "http://www.baiyjk.com";
	public final String PHPSESSIONKEY = "baiyang";//正式环境
	public static String BAIYANG = null;
	public final String PHPSESSIONID = "PHPSESSIONID";

	private HttpClientUtil() {
	} // 单例模式中，封闭创建实例接口

	private static HttpClientUtil httpClientUtils = null;

	/**
	 * 提供了一个可以访问到它自己的全局访问点
	 * 
	 * @return
	 */
	public static HttpClientUtil get() {
		if (httpClientUtils == null)
			httpClientUtils = new HttpClientUtil();
		return httpClientUtils;
	}

	@Override
	public String getUrlContext(String strUrl, Context context) {
		// TODO Auto-generated method stub
		strUrl = host + strUrl;
		Log.d("Http", strUrl);
		String responseStr = null;// 发送请求，得到响应
		DefaultHttpClient httpClient = null;
		HttpGet httpGet = null;
		try {
			strUrl = urlEncode(strUrl.trim(), CHARSET_UTF8);
			httpClient = getDefaultHttpClient(null);
			httpGet = new HttpGet(strUrl);
			SharedPreferences pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
																				
			Editor editor = pref.edit();
			BAIYANG = pref.getString(PHPSESSIONID, "");
			Log.d(PHPSESSIONID, BAIYANG);
			if (null != BAIYANG) {
//				httpGet.setHeader("Cookie", "BAIYANG=" + BAIYANG);
				httpGet.setHeader("Cookie", PHPSESSIONKEY + "=" + BAIYANG);
			}
			responseStr = httpClient.execute(httpGet, strResponseHandler);
			CookieStore mCookieStore = httpClient.getCookieStore();
			List<Cookie> cookies = mCookieStore.getCookies();

			for (int i = 0; i < cookies.size(); i++) {
				// 这里是读取Cookie['PHPSESSID']的值存在静态变量中，保证每次都是同一个值
				// 保存到sharedPreference最好，不管什么时候都是取到同一个值()
				Log.d("cookie", cookies.get(i).getName() + ":"
						+ cookies.get(i).getValue());
				if (PHPSESSIONKEY.equals(cookies.get(i).getName())) {
					// BAIYANG = cookies.get(i).getValue();
					String newSessionId = cookies.get(i).getValue();
					
					if (!BAIYANG.equals(newSessionId)) {
						editor.putString(PHPSESSIONID, newSessionId);
						BAIYANG = newSessionId;
						editor.commit();
					}

					break;
				}
			}
		} catch (ClientProtocolException e) {
			Log.e(DEBUG_TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(DEBUG_TAG, e.getMessage());
		} catch (Exception e) {
			Log.e(DEBUG_TAG, e.getMessage());
//			Log.e(DEBUG_TAG, e.toString());
		} finally {
			abortConnection(httpGet, httpClient);
		}
		return responseStr;
	}

	/**
	 * 转码http的网址，只对中文进行转码
	 * 
	 * @param str
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String urlEncode(String str, String charset)
			throws UnsupportedEncodingException {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]+");
		Matcher m = p.matcher(str);
		StringBuffer b = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(b, URLEncoder.encode(m.group(0), charset));
		}
		m.appendTail(b);
		return b.toString();
	}

	/**
	 * 设置重连机制和异常自动恢复处理
	 */
	private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
		// 自定义的恢复策略
		public boolean retryRequest(IOException exception, int executionCount,
				HttpContext context) {
			// 设置恢复策略，在Http请求发生异常时候将自动重试3次
			if (executionCount >= 3) {
				// Do not retry if over max retry count
				return false;
			}
			if (exception instanceof NoHttpResponseException) {
				// Retry if the server dropped connection on us
				return true;
			}
			if (exception instanceof SSLHandshakeException) {
				// Do not retry on SSL handshake exception
				return false;
			}
			HttpRequest request = (HttpRequest) context
					.getAttribute(ExecutionContext.HTTP_REQUEST);
			boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
			if (!idempotent) {
				// Retry if the request is considered idempotent
				return true;
			}
			return false;
		}
	};

	// 使用ResponseHandler接口处理响应，HttpClient使用ResponseHandler会自动管理连接的释放，
	// 解决对连接的释放管理
	private static ResponseHandler<String> strResponseHandler = new ResponseHandler<String>() {
		// 自定义响应处理
		public String handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String charset = EntityUtils.getContentCharSet(entity) == null ? CHARSET_UTF8
						: EntityUtils.getContentCharSet(entity);
				return new String(EntityUtils.toByteArray(entity), charset);
			} else {
				return null;
			}
		}
	};

	/**
	 * 获取DefaultHttpClient实例
	 * 
	 * @param charset
	 * @return
	 */
	private static DefaultHttpClient getDefaultHttpClient(final String charset) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		// httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
		// HttpVersion.HTTP_1_1);
		// 模拟浏览器(有些服务器只支持浏览器访问，这个可以模拟下~~~)
		// httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT,"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
		// httpclient.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE,Boolean.FALSE);

		// 请求超时
		httpclient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		// 读取超时
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				5000);
		httpclient.getParams().setParameter(
				CoreProtocolPNames.HTTP_CONTENT_CHARSET,
				charset == null ? CHARSET_UTF8 : charset);
		httpclient.setHttpRequestRetryHandler(requestRetryHandler);
		return httpclient;
	}

	/**
	 * 释放HttpClient连接
	 * 
	 * @param hrb
	 * @param httpclient
	 */
	private static void abortConnection(final HttpRequestBase httpRequestBase,
			final HttpClient httpclient) {
		if (httpRequestBase != null) {
			httpRequestBase.abort();
		}
		if (httpclient != null) {
			httpclient.getConnectionManager().shutdown();
		}
	}

}
