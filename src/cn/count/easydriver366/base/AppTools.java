package cn.count.easydriver366.base;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

public final class AppTools {
	public static void log(Exception e){
		Log.e("Application Log", e.getMessage());
	}
	public static boolean isSuccess(final Object jsonobj){
		boolean result= false;
		if (jsonobj==null){
			return result;
		}
		if (jsonobj instanceof JSONObject){
			try{
				final String status = ((JSONObject)jsonobj).getString("status");
				result = status.equals("success");
			}catch(Exception e){
				log(e);
			}
			 
		}else if (jsonobj instanceof JSONArray){
			try{
				result =  ((JSONArray)jsonobj).length()>0;
			}catch(Exception e){
				log(e);
			}
		}
		return result;
	}
	public static Intent getPhoneAction(final String phone){
		Intent intent=null;
		try {
			intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:" + phone));
			//startActivity(intent);
			
		} catch (Exception e) {
			log(e);
		}
		return intent;
	}
	public static Intent getBrowserAction(final String url){
		//Uri uri = Uri.parse("http://www.google.com"); //浏览器 
		//Uri uri =Uri.parse("tel:1232333"); //拨号程序 
		//Uri uri=Uri.parse("geo:39.899533,116.036476"); //打开地图定位 
		//Intent it = new Intent(Intent.ACTION_VIEW,uri); 
		
		Intent intent = null;
		intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
		return intent;
	}
	public static void loadImageFromUrl(ImageView imageView,final String url){
		if (imageView==null)
			return;
		if (url ==null)
			return;
		if (url.isEmpty())
			return;
		com.koushikdutta.urlimageviewhelper.UrlImageViewHelper.setUrlDrawable(imageView, url);
	}
	public static void loadImageFromUrl(ImageView imageView,final String url,int resourceId){
		if (imageView==null)
			return;
		if (url ==null)
			return;
		if (url.isEmpty())
			return;
		com.koushikdutta.urlimageviewhelper.UrlImageViewHelper.setUrlDrawable(imageView, url,resourceId);
	}
}
