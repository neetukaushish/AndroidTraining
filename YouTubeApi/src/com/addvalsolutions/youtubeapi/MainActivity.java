package com.addvalsolutions.youtubeapi;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	private static String url="https://gdata.youtube.com/feeds/api/videos?q=football+-soccer&alt=json";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
		
		/*HttpClient client=new DefaultHttpClient();
		HttpPost post=new HttpPost(url);
		HttpResponse response=client.execute(post);
		HttpEntity entity=response.getEntity().getContent();*/
		
	}

}
