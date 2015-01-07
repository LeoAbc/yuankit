import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class HttpClientWapper {

	private CloseableHttpClient httpClient;
	
	private int soTimeout = 5000;
	private int connectTimeout = 8000;
	private int connectRequestTimeout = 10000;
	
	private static HttpClientWapper _Wapper;
	
	static {
		_Wapper = new HttpClientWapper();
	}
	
	private HttpClientWapper(){
		PoolingHttpClientConnectionManager connectPool = new PoolingHttpClientConnectionManager();
		connectPool.setDefaultMaxPerRoute(20);
		connectPool.setMaxTotal(200);		
		SocketConfig sConfig = SocketConfig.copy(SocketConfig.DEFAULT)
				.setSoKeepAlive(true).setSoTimeout(soTimeout).build();
		connectPool.setDefaultSocketConfig(sConfig);
		
		RequestConfig requstCfg = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
				.setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectRequestTimeout).build();
		
		httpClient = HttpClientBuilder.create().setConnectionManager(connectPool)
			.setDefaultCookieStore(new BasicCookieStore())
			.setDefaultRequestConfig(requstCfg).build();
	}

	private Registry<ConnectionSocketFactory> initSSL() {
		X509TrustManager xtm = new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
				
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
				
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
			
		};
		try{
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, new TrustManager[] { xtm }, null);
			SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(ctx,
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			return RegistryBuilder.<ConnectionSocketFactory>create()
		        .register("http", PlainConnectionSocketFactory.getSocketFactory())
		        .register("https", socketFactory).build();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HttpClientWapper getHttp(){
		return _Wapper;
	}
	
	public synchronized String execute(String url, RequestPre prepare, final RequestPost post) throws IOException{
		RequestBuilder reqBuilder = prepare.prepare();
		reqBuilder.setUri(url);
		addCommonHeaders(reqBuilder);
		final HttpRequestBase httpRequest = (HttpRequestBase)reqBuilder.build();
		try{
			HttpResponse httpResp = httpClient.execute(httpRequest);
			if(post != null){
				post.post(httpResp);
				return "";
			}
			if(httpResp.getEntity() != null){
				String html = EntityUtils.toString(httpResp.getEntity(),"utf8");
				EntityUtils.consume(httpResp.getEntity());
				return html;
			}
			return "";
		}finally{
			httpRequest.releaseConnection();
		}
	}

	public static void addCommonHeaders(RequestBuilder reqBuilder) {
		reqBuilder.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");		
		reqBuilder.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		reqBuilder.addHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		URI uri = reqBuilder.getUri();
		if(uri != null){
			reqBuilder.addHeader("Host",getHost(uri.toString()));
		}
	}
	
	public static String getHost(String tourUrl) {
		if (isNotEmpty(tourUrl))
		{
			try {
				URL url = new URL(tourUrl);
				return url.getHost();
			} catch (MalformedURLException e) {
				return "";
			}
		}		
		return "";		
	}
	
	static boolean isNotEmpty(String str){
		return str != null && str.trim().length() > 0;
	}
	
	public static interface RequestPre{
		RequestBuilder prepare() throws IOException;
	}
	
	public static interface RequestPost{
		void post(HttpResponse resp) throws IOException;
	}
}
