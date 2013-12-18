package cn.count.easydriver366.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

public abstract class HttpExecuteGetTask extends AsyncTask<String, Void, String> {
	private static final String DEBUG_TAG = "Http.Get";	
	@Override
	protected String doInBackground(String... urls) {
		try{
			return httpGetUrl(urls[0]);
		}catch(Exception e){
			
			return "Unable to retrieve the url. URL may be invalid. return:"+e.getMessage();
		}
	}

	@Override
	abstract protected void onPostExecute(String result); 
	private String httpGetUrl(final String urlString){
		try {
			
			String httpUrl = AppSettings.ServerUrl+urlString;
			Log.d(AppSettings.AppTile, httpUrl);
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(httpUrl);
			HttpResponse response = client.execute(get);
			int status_code = response.getStatusLine().getStatusCode();
			if (status_code >= 300) {
				return null;
			}
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity);
		} catch (Exception e) {
			return null;
		}
	}
	private String httpGetUrl_old(final String urlString) throws IOException
	{
		InputStream is = null;
		try
		{
			URL url = new URL(AppSettings.ServerUrl+urlString);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setReadTimeout(1000*AppSettings.READ_TIMEOUT);
			conn.setConnectTimeout(1000*AppSettings.CONNECT_TIMEOUT);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.connect();
			int response = conn.getResponseCode();			
			is = conn.getInputStream();
			String result = AppSettings.readInputStream(is);
			AppSettings.log(result);
			return result;
		}
		finally
		{
			if (is!=null)
				is.close();
			
		}
	}
	
}
