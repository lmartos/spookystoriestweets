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

import android.app.ActionBar;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import nl.saxion.spookystoriestweets.controller.TwitterApplication;
import nl.saxion.spookystoriestweets.model.Model;
import nl.saxion.spookystoriestweets.view.TwitterAdapter;

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
		hideActionBar();
		// Haal het model op vanuit de applicationContext
		model = ((TwitterApplication) getBaseContext().getApplicationContext()).getModel();		
		// Koppel de juiste controls aan de variabelen
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		etSearchTerm = (EditText) findViewById(R.id.etSearchTerm);
		// Genereer het bearertoken
		generateOAUTHToken();
		// Maak een instantie van de adapter en koppel deze aan de listview
		TwitterAdapter adapter = new TwitterAdapter(this, model.getTweets());
		model.addObserver(adapter);
		lvTweets.setAdapter(adapter);
		
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				GetTweetsFromInputTask getTweets = new GetTweetsFromInputTask();
				String searchString = etSearchTerm.getText() + "";
				btnSearch.setText("Searching ...");
				btnSearch.setEnabled(false);
				getTweets.execute(URLEncoder.encode(searchString));
				lvTweets.setSelectionAfterHeaderView();
			}
		});
	}
	

	private void generateOAUTHToken() {
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
	
	public class GetTweetsFromInputTask extends AsyncTask<String, Void, String> {
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
			btnSearch.setText("Search");
			btnSearch.setEnabled(true);
			super.onPostExecute(result);
		}

	}
	
	private void hideActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.hide();
	}
	
	
}
