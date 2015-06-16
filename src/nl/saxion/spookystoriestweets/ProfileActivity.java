package nl.saxion.spookystoriestweets;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import nl.saxion.spookystoriestweets.model.Model;
import nl.saxion.spookystoriestweets.model.Tweet;
import nl.saxion.spookystoriestweets.views.TwitterAdapter;
import oauth.signpost.OAuth;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
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
		
		model = ((SpookyStoriesTweetsApplication) getBaseContext().getApplicationContext()).getModel();	
		
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		
		AccessToken a = new AccessToken(token,secret);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);
		
		adapter = new TwitterAdapter(this, model.getTimeline());
		lvTimeline.setAdapter(adapter);
		
		
		
		
	
		try {
			User user = twitter.showUser(twitter.getId());
			url = user.getProfileImageURL();
			
			
			for(Status s : twitter.getHomeTimeline()){
				model.addToTimeline(new Tweet(s));
			}
			
			
			ivProfileAvatar.setImageBitmap(getImage(url));
			
			tvScreenName.setText(twitter.getScreenName());
			tvTag.setText("@" + twitter.getScreenName() );
			
			tvTweetCount.setText("" + user.getStatusesCount() +  " Tweets");
			tvFollowingCount.setText("" + twitter.getFriendsIDs(-1).getIDs().length + " Following");
			tvFollowersCount.setText("" + twitter.getFollowersIDs(-1).getIDs().length + " Followers");
			Log.d("PROFILE", twitter.getScreenName());
			Log.d("PROFILE", "" + twitter.getUserTimeline().size());
			Log.d("PROFILE", "" + twitter.getFavorites().size());
			Log.d("PROFILE", "" + twitter.getFollowersIDs(twitter.getId()).getIDs().length);
		} catch (Exception e) {
			Log.d("Twitter Exception", e.getMessage());
			e.printStackTrace();
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
	
    public Bitmap getImage(String url) {
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
}
