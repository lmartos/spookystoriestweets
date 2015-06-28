package nl.saxion.spookystoriestweets;

/**	
 * 
 * @author Doron Hartog & Laurens Martos
 *
 */

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

		model = ((SpookyStoriesTweetsApplication) getBaseContext()
				.getApplicationContext()).getModel();
		model.addObserver(this);
		consumer = model.getConsumer();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		lvFollowers = (ListView) findViewById(R.id.lvFollowers);

		adapter = new FollowerAdapter(this, model.getFollowerList());

		lvFollowers.setAdapter(adapter);
		lvFollowers.setOnItemClickListener(new FollowerClickListener());

		GetFollowersTask task = new GetFollowersTask();
		task.execute();

	}

	public class FollowerClickListener implements OnItemClickListener {
		/**
		 * when clicked on a follower, the follower tweet activity will start
		 * where you can see the follower' tweets
		 */
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(FollowerActivity.this,
					FollowerTweetActivity.class);
			intent.putExtra("position", position);
			startActivity(intent);

		}

	}

	/**
	 * The getFollowTask class for retrieving the followers in a JSONObject
	 *
	 */
	public class GetFollowersTask extends AsyncTask<Void, Integer, String> {

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		private HttpResponse response;

		/**
		 * Another thread will handle the httpGet and retrieve the response
		 */
		@Override
		protected String doInBackground(Void... params) {
			String followersJSON = "";
			try {
				HttpClient client = new DefaultHttpClient();

				HttpGet httpGet = new HttpGet(
						"https://api.twitter.com/1.1/followers/list.json");

				consumer.sign(httpGet);

				ResponseHandler<String> handler = new BasicResponseHandler();
				response = client.execute(httpGet);

				followersJSON = handler.handleResponse(response);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return followersJSON;

		}

		/**
		 * updates the followers in Model
		 */
		@Override
		protected void onPostExecute(String result) {
			model.handleFollowers(result);
		}

	}

	/**
	 * finishes the activity on back press
	 */
	@Override
	public void onBackPressed() {
		finish();
	}

	/**
	 * Updates the adapter view
	 */
	@Override
	public void update(Observable observable, Object data) {
		adapter.notifyDataSetChanged();

	}
}
