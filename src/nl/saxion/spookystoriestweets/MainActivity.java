package nl.saxion.spookystoriestweets;

import java.net.URLEncoder;



import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import nl.saxion.spookystoriestweets.model.Model;
import nl.saxion.spookystoriestweets.views.TwitterAdapter;

public class MainActivity extends Activity {

	private static final String APIKEY = "D7eeHiP0mPYd1RCHdu3aDsoFT";
	private static final String APISECRET = "DL9Wju1bRMdllWsbKAOSH4WlgZ1AA04hw48JjdSlrk0JRpSKb9";
	private static String bearerToken = "";
	
	private Model model;
	private EditText etSearchTerm;
	private ListView lvTweets;
	Button btnSearch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		model = ((SpookyStoriesTweetsApplication) getBaseContext().getApplicationContext()).getModel();		
	
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		etSearchTerm = (EditText) findViewById(R.id.etSearchTerm);
	
		OauthToken();
	
		TwitterAdapter adapter = new TwitterAdapter(this, model.getTweets());
		model.addObserver(adapter);
		lvTweets.setAdapter(adapter);
		
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RetrieveTweets getTweets = new RetrieveTweets();
				String searchString = etSearchTerm.getText() + "";
				btnSearch.setEnabled(false);
				getTweets.execute(URLEncoder.encode(searchString));
				lvTweets.setSelectionAfterHeaderView();
			}
		});
	}
	

	private void OauthToken() {
		String authString = APIKEY + ":" + APISECRET;
		String base64 = Base64.encodeToString(authString.getBytes(),
				Base64.NO_WRAP);

		GenerateTokenTask task = new GenerateTokenTask();
		task.execute(base64);
	}
	
	public class GenerateTokenTask extends AsyncTask<String, Void, String> {
		private HttpResponse response;

		@Override
		protected String doInBackground(String... params) {
			Log.d("json", "doInBackground GenerateTokenTask");
			HttpPost request = new HttpPost(
					"https://api.twitter.com/oauth2/token");
			request.setHeader("Authorization", "Basic " + params[0]);
			request.setHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			String token = "";
			try {
				request.setEntity(new StringEntity(
						"grant_type=client_credentials"));
				HttpClient client = new DefaultHttpClient();
				response = client.execute(request);

				String responseString = new BasicResponseHandler()
						.handleResponse(response);
				
				JSONObject jsonO = new JSONObject(responseString);
				token = jsonO.getString("access_token");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return token;
		}

		@Override
		protected void onPostExecute(String result) {
			bearerToken = result;
		

			super.onPostExecute(result);
		}

	}
	
	public class RetrieveTweets extends AsyncTask<String, Void, String> {
		private HttpResponse response;
		@Override
		protected String doInBackground(String... params) {
			Log.d("json", "doInBackground GetTweetsFromInputTask");
			String searchJSON = "";
			if (!params[0].equals("")) {
				HttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(
						"https://api.twitter.com/1.1/search/tweets.json?q="
								+ params[0]);
				httpGet.setHeader("Authorization", "Bearer " + bearerToken);				
				try {
					ResponseHandler<String> handler = new BasicResponseHandler();
					response = client.execute(httpGet);
					searchJSON = handler.handleResponse(response);
					Log.d("json", searchJSON);
				} catch (Exception e){
					e.printStackTrace();
				}
			} 
			return searchJSON;		
		}

		@Override
		protected void onPostExecute(String result) {
			model.setJson(result);
			btnSearch.setEnabled(true);
			super.onPostExecute(result);
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();

		if (id == R.id.mainTimeline) {
			return true;
		}

		else if (id == R.id.mainProfile) {
			Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
