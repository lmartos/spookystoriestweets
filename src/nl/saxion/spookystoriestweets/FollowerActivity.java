package nl.saxion.spookystoriestweets;


import java.util.Observable;
import java.util.Observer;

import nl.saxion.spookystoriestweets.model.Model;
import nl.saxion.spookystoriestweets.views.FollowerAdapter;
import oauth.signpost.OAuthConsumer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FollowerActivity extends Activity implements Observer {
	
	private Model model;
	private OAuthConsumer consumer;
	private ListView lvFollowers;
	private FollowerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_follower);
		
		model = ((SpookyStoriesTweetsApplication) getBaseContext().getApplicationContext()).getModel();
		model.addObserver(this);
		consumer = model.getConsumer();
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        
        lvFollowers = (ListView) findViewById(R.id.lvFollowers);
		
        adapter = new FollowerAdapter(this, model.getFollowerList());
        
        lvFollowers.setAdapter(adapter);
        lvFollowers.setOnItemClickListener(new FollowerClickListener());
		
		GetFollowersTask  task = new GetFollowersTask();
		task.execute();
	
	
	}
	
	public class FollowerClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(FollowerActivity.this, FollowerTweetActivity.class);
			intent.putExtra("position", position);
			startActivity(intent);
			
			
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.follower, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
	
		return super.onOptionsItemSelected(item);
	}
	
	
	
public class GetFollowersTask extends AsyncTask<Void, Integer, String> {
		
	
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		

		private HttpResponse response;
		@Override
		protected String doInBackground(Void... params) {
			String followersJSON = "";
			try {
				HttpClient client = new DefaultHttpClient();
				
				HttpGet httpGet = new HttpGet(
							"https://api.twitter.com/1.1/followers/list.json");
				
				

		        // sign the request
				
		
		        consumer.sign(httpGet);

				ResponseHandler<String> handler = new BasicResponseHandler();
				response = client.execute(httpGet);
			
				followersJSON = handler.handleResponse(response);
				//System.out.println(followersJSON);
				
			
			} catch (Exception e){
				e.printStackTrace();
			}
			return followersJSON;
			
			
		}

		@Override
		protected void onPostExecute(String result) {
				model.handleFollowers(result);
			}

	}	
	
	@Override
	public void onBackPressed(){
		finish();
	}

	@Override
	public void update(Observable observable, Object data) {
		adapter.notifyDataSetChanged();
		//System.out.println("Updated");
		
	}
}
