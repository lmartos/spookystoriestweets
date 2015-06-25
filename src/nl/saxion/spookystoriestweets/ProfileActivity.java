package nl.saxion.spookystoriestweets;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;





import nl.saxion.spookystoriestweets.model.Model;
import nl.saxion.spookystoriestweets.model.Tweet;
import nl.saxion.spookystoriestweets.views.TwitterAdapter;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ProfileActivity extends Activity implements Observer {
	
	private Twitter twitter;
	private TextView tvScreenName, tvTag, tvTweetCount, tvFollowingCount, tvFollowersCount;
	private ImageView ivProfileAvatar;
	private String url = "";
	private ListView lvTimeline;
	private ResponseList<Status> timeline;
	private Model model;
	private TwitterAdapter adapter;
	private LinearLayout llBanner;
	private Button buttonPostTweet;
	private EditText etPostTweet;
	SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		
		
		tvScreenName = (TextView) findViewById(R.id.tvProfileScreenName);
		tvTag = (TextView) findViewById(R.id.tvProfileTag);
		tvTweetCount = (TextView) findViewById(R.id.tvTweetCount);
		tvFollowingCount = (TextView) findViewById(R.id.tvFollowingCount);
		tvFollowersCount = (TextView) findViewById(R.id.tvFollowersCount);
		ivProfileAvatar = (ImageView) findViewById(R.id.ivProfileAvatar);
		lvTimeline = (ListView) findViewById(R.id.lvTimeLine);
		llBanner = (LinearLayout) findViewById(R.id.llBanner);
		buttonPostTweet = (Button) findViewById(R.id.buttonPostTweet);
		etPostTweet = (EditText) findViewById(R.id.etPostTweet);
		
		
		model = ((SpookyStoriesTweetsApplication) getBaseContext().getApplicationContext()).getModel();	
		model.clearTimeline();
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		
		AccessToken a = new AccessToken(token,secret);
		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);
		
		adapter = new TwitterAdapter(this, model.getTimeline());
		lvTimeline.setAdapter(adapter);
		
		
		buttonPostTweet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String tweet = ""+etPostTweet.getText();
				if(tweet.equals("")){
					return;
				}else{
					try {
						twitter.updateStatus(tweet);
						model.clearTimeline();
						for(Status s : twitter.getUserTimeline()){
							model.addToTimeline(new Tweet(s));
						}
						adapter.notifyDataSetChanged();
						etPostTweet.setText("");
					} catch (TwitterException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
	
		try {
			GetTweetsFromTimeline task = new GetTweetsFromTimeline();
			task.execute();
			
			User user = twitter.showUser(twitter.getId());
			url = user.getBiggerProfileImageURL();
			
		
			
			
			ivProfileAvatar.setImageBitmap(getImage(url));
			ivProfileAvatar.setBackgroundColor(Color.WHITE);
			
			tvScreenName.setText(user.getName());
			tvTag.setText("@" + twitter.getScreenName() );
			Bitmap bitmap = getImage(user.getProfileBannerURL());
			Drawable drawable = new BitmapDrawable(getResources(), bitmap);
			llBanner.setBackgroundDrawable(drawable);
			tvTweetCount.setText("" + user.getStatusesCount() +  "\nTweets");
			tvFollowingCount.setText("" + user.getFriendsCount() + "\nFollowing");
			tvFollowersCount.setText("" + user.getFollowersCount() + "\nFollowers");
		} catch (Exception e) {
			Log.d("Twitter Exception", e.getMessage());
			e.printStackTrace();
		} 
		
		
	}
	
	public class GetTweetsFromTimeline extends AsyncTask<Void, Integer, String> {
		
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		
		AccessToken a = new AccessToken(token,secret);
		private HttpResponse response;
		@Override
		protected String doInBackground(Void... params) {
			String timelineJSON = "";
			try {
				HttpClient client = new DefaultHttpClient();
				
				HttpGet httpGet = new HttpGet(
							"https://api.twitter.com/1.1/statuses/home_timeline.json");
				
				

		        // sign the request
				OAuthConsumer consumer = model.getConsumer();
		
		        consumer.sign(httpGet);

				ResponseHandler<String> handler = new BasicResponseHandler();
				response = client.execute(httpGet);
				Log.d("hallo daar", "ik heb geen idee wat mis gaat maar dit niet");
				timelineJSON = "{ \"statuses\":" + handler.handleResponse(response) + "}";
				System.out.println(timelineJSON);
			
			} catch (Exception e){
				e.printStackTrace();
			}
			return timelineJSON;		
		}

		@Override
		protected void onPostExecute(String result) {
			model.setJsonTimeline(result);
			adapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}

	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.Timeline) {
			Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			return true;
		}
		if (id == R.id.Profile) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    public static Bitmap getImage(String url) {
        try {
        	
        	
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	@Override
	public void update(Observable observable, Object data) {
		adapter.notifyDataSetChanged();
		
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
