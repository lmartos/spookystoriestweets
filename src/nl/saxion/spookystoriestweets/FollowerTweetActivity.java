package nl.saxion.spookystoriestweets;

/**	
 * 
 * @author Doron Hartog & Laurens Martos
 *
 */
import java.util.Observable;

import java.util.Observer;

import oauth.signpost.OAuthConsumer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import nl.saxion.spookystoriestweets.model.Model;
import nl.saxion.spookystoriestweets.model.User;
import nl.saxion.spookystoriestweets.views.FollowerAdapter;
import nl.saxion.spookystoriestweets.views.TwitterAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

public class FollowerTweetActivity extends Activity implements Observer {

	private Model model;
	private ImageView ivFollowerAvatar;
	private ListView lvFollowersTweets;
	private User currentUser;
	private TwitterAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_follower_tweet);

		model = ((SpookyStoriesTweetsApplication) getBaseContext()
				.getApplicationContext()).getModel();
		model.addObserver(this);
		ivFollowerAvatar = (ImageView) findViewById(R.id.ivFollowerAvatarLarge);
		lvFollowersTweets = (ListView) findViewById(R.id.lvFollowerTweets);

		adapter = new TwitterAdapter(this, model.getFollowerTimeline());
		lvFollowersTweets.setAdapter(adapter);

		Intent intent = getIntent();

		currentUser = model.getFollowerList().get(
				intent.getIntExtra("position", 0));

		ivFollowerAvatar.setImageBitmap(FollowerAdapter.getImage(currentUser
				.getPictureURL()));

		GetTweetsFromTimeline task = new GetTweetsFromTimeline();
		task.execute();

	}

	public class GetTweetsFromTimeline extends AsyncTask<Void, Integer, String> {

		private HttpResponse response;

		/**
		 * Another thread will retrieve the response when the client executes
		 * the httpGet and return the timeline in JSONObject
		 */
		@Override
		protected String doInBackground(Void... params) {
			String timelineJSON = "";
			try {
				HttpClient client = new DefaultHttpClient();

				HttpGet httpGet = new HttpGet(
						"https://api.twitter.com/1.1/statuses/user_timeline.json?user_id="
								+ currentUser.getUserId()
								+ "&exclude_replies=t");

				OAuthConsumer consumer = model.getConsumer();
				consumer.sign(httpGet);

				ResponseHandler<String> handler = new BasicResponseHandler();
				response = client.execute(httpGet);

				timelineJSON = "{ \"statuses\":"
						+ handler.handleResponse(response) + "}";

			} catch (Exception e) {
				e.printStackTrace();
			}
			return timelineJSON;
		}

		@Override
		protected void onPostExecute(String result) {
			if (!result.equals(null)) {
				model.buildFollowerTimeline(result);
				adapter.notifyDataSetChanged();
				super.onPostExecute(result);
			}
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
	 * updates the adapter
	 */
	@Override
	public void update(Observable observable, Object data) {
		adapter.notifyDataSetChanged();

	}
}
