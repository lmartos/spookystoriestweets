package nl.saxion.spookystoriestweets;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;





import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;




import nl.saxion.spookystoriestweets.model.Model;
import nl.saxion.spookystoriestweets.model.Tweet;
import nl.saxion.spookystoriestweets.model.User;
import nl.saxion.spookystoriestweets.views.TwitterAdapter;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
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
	private OAuthConsumer consumer;
	
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
		consumer = model.getConsumer();
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
						postTweetTask postTask = new postTweetTask();
						postTask.execute(tweet);
						model.clearTimeline();
						GetTweetsFromTimeline task = new GetTweetsFromTimeline();
						task.execute();
						adapter.notifyDataSetChanged();
						etPostTweet.setText("");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		
	
		try {
			GetTweetsFromTimeline task = new GetTweetsFromTimeline();
			task.execute();
			
			GetLoggedInUser userTask = new GetLoggedInUser();
			userTask.execute();
			
			
		} catch (Exception e) {
			//Log.d("Twitter Exception", e.getMessage());
			e.printStackTrace();
		} 
		
		
	}
	
	public void updateView(){
		User user = model.getLoggedInUser();
		
		
		ivProfileAvatar.setImageBitmap(getImage(user.getPictureURL()));
		ivProfileAvatar.setBackgroundColor(Color.WHITE);
		
		tvScreenName.setText(user.getName());
		tvTag.setText("@" + user.getScreenName() );
		Bitmap bitmap = getImage(user.getProfileBannerURL());
		Drawable drawable = new BitmapDrawable(getResources(), bitmap);
		llBanner.setBackgroundDrawable(drawable);
		tvTweetCount.setText("" + user.getSend_tweets() +  "\nTweets");
		tvFollowingCount.setText("" + user.getFollowing() + "\nFollowing");
		tvFollowersCount.setText("" + user.getFollowers() + "\nFollowers");
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
				
		
		        consumer.sign(httpGet);

				ResponseHandler<String> handler = new BasicResponseHandler();
				response = client.execute(httpGet);
				Log.d("hallo daar", "ik heb geen idee wat mis gaat maar dit niet");
				timelineJSON = "{ \"statuses\":" + handler.handleResponse(response) + "}";
				//System.out.println(timelineJSON);
				
			
			} catch (Exception e){
				e.printStackTrace();
			}
			return timelineJSON;
			
			
		}

		@Override
		protected void onPostExecute(String result) {
			if(!result.equals(null)){
			model.setJsonTimeline(result);
			adapter.notifyDataSetChanged();
			super.onPostExecute(result); 
			}}

	}	
	
	public class GetLoggedInUser extends AsyncTask<Void, Integer, String> {
		
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		
		AccessToken a = new AccessToken(token,secret);
		private HttpResponse response;
		@Override
		protected String doInBackground(Void... params) {
			String userJSON = "";
			try {
				HttpClient client = new DefaultHttpClient();
				
				HttpGet httpGet = new HttpGet(
							"https://api.twitter.com/1.1/account/verify_credentials.json");
				
				

		        // sign the request
				
		
		        consumer.sign(httpGet);

				ResponseHandler<String> handler = new BasicResponseHandler();
				response = client.execute(httpGet);
				Log.d("hallo daar", "ik heb geen idee wat mis gaat maar dit niet");
				
				
				System.out.println(userJSON);
				model.createLoggedInUser(handler.handleResponse(response));
			} catch (Exception e){
				e.printStackTrace();
			}
			return userJSON;		
		}
		
		@Override
		protected void onPostExecute(String result) {
				updateView();
			}

	

	}	
	
	private class postTweetTask extends AsyncTask<String, Void, String>{
		private HttpResponse response;
		private String returnJSON;
		@Override
		protected String doInBackground(String... params) {
			HttpClient client = new DefaultHttpClient();
			
			HttpPost post = new HttpPost("https://api.twitter.com/1.1/statuses/update.json");
			
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("status", params[0]));
			try {
				post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				consumer.sign(post);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			ResponseHandler<String> handler = new BasicResponseHandler();
			try {
				response = client.execute(post);
				returnJSON = handler.handleResponse(response);
			} catch (ClientProtocolException e) {
				int statusCode = response.getStatusLine().getStatusCode();
				return "" + statusCode;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			return null;
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
