package nl.saxion.spookystoriestweets.views;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import oauth.signpost.OAuthConsumer;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import nl.saxion.spookystoriestweets.ProfileActivity;
import nl.saxion.spookystoriestweets.R;
import nl.saxion.spookystoriestweets.SpookyStoriesTweetsApplication;
import nl.saxion.spookystoriestweets.model.Entity;
import nl.saxion.spookystoriestweets.model.Hashtag;
import nl.saxion.spookystoriestweets.model.Media;
import nl.saxion.spookystoriestweets.model.Model;
import nl.saxion.spookystoriestweets.model.Tweet;
import nl.saxion.spookystoriestweets.model.Url;
import nl.saxion.spookystoriestweets.model.User;

public class TwitterAdapter extends ArrayAdapter<Tweet> implements Observer {

	private SpannableStringBuilder sbTweet;
	private Context context;
	private Button buttonRetweet, buttonFollow;
	private OAuthConsumer consumer;
	private Model model;

	public TwitterAdapter(Context context, ArrayList<Tweet> listOfTweets) {
		super(context, R.layout.listviewitem, listOfTweets);
		this.context = context;
		model = ((SpookyStoriesTweetsApplication) context.getApplicationContext()).getModel();	
		consumer = model.getConsumer();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Tweet currentTweet = getItem(position);
		User currentUser = currentTweet.getUser();

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.listviewitem, parent, false);
		}
		
		buttonRetweet = (Button) convertView.findViewById(R.id.buttonRetweet);
		buttonFollow = (Button) convertView.findViewById(R.id.buttonFollow);
		TextView user = (TextView) convertView.findViewById(R.id.tvUser);
		TextView tweetText = (TextView) convertView.findViewById(R.id.tvTweet);
		ImageView ivSmallAvatar = (ImageView) convertView.findViewById(R.id.ivUserImage);
		ivSmallAvatar.setImageBitmap(ProfileActivity.getImage(currentUser.getPictureURL()));
		
		buttonFollow.setOnClickListener(new OnClickListener() {
			private User cUser = currentTweet.getUser();
			@Override
			public void onClick(View v) {
				String screenName = ""+cUser.getUserId();
				FollowTask followTask = new FollowTask();
				followTask.execute(screenName);
				notifyDataSetChanged();
			}
		});
		
		buttonRetweet.setOnClickListener(new OnClickListener() {
			private Tweet cTweet = currentTweet;
			@Override
			public void onClick(View v) {
				String tweetId = ""+cTweet.getId();
				RetweetTask retweetTask = new RetweetTask();
				retweetTask.execute(tweetId);
				notifyDataSetChanged();
			}
		});
		
		LinearLayout ll = (LinearLayout) convertView
				.findViewById(R.id.llDisplayTweet);
		sbTweet = new SpannableStringBuilder(currentTweet.getTweetText());
		int hashtagC = Color.rgb(26, 13, 131);

		for (Entity ent : currentTweet.getEntities()) {
			if (ent instanceof Hashtag) {
				sbTweet.setSpan(new ForegroundColorSpan(hashtagC),
						ent.getIndex(1), ent.getIndex(2),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			if (ent instanceof Url) {
				final Url url = (Url) ent;
				ClickableSpan clickableSpan = new ClickableSpan() {

					@Override
					public void onClick(View widget) {
						Intent browser = new Intent(Intent.ACTION_VIEW,
								Uri.parse(url.getUrl()));
						context.startActivity(browser);
					}
				};
				sbTweet.setSpan(clickableSpan, ent.getIndex(1),
						ent.getIndex(2), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			}

			for (int i = 0; i < ll.getChildCount(); i++) {
				if (ll.getChildAt(i) instanceof ImageView) {
					ImageView imageView = (ImageView) ll.getChildAt(i);
					ll.removeView(imageView);
				}
			}
			if (ent instanceof Media) {
				Bitmap image = ((Media) ent).getPicture();
				if (image == null) {
					GetImages task = new GetImages(ent);
					task.execute(((Media) ent).getMediaURL());
				} else {
					ImageView view = new ImageView(context);
					view.setImageBitmap(((Media) ent).getPicture());
					ll.addView(view);

				}
			}
		}

		user.setText("@"+currentUser.getScreenName());
		tweetText.setText(sbTweet);
		Log.d("ADAPTER!!!", sbTweet.toString());
		tweetText.setMovementMethod(LinkMovementMethod.getInstance());

		return convertView;
	}
	
	private class GetImages extends AsyncTask<String, Void, Bitmap> {

		private Media media;

		public GetImages(Entity ent) {
			media = (Media) ent;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			String imageURL = params[0];
			Bitmap picture;
			try {
				InputStream input = new URL(imageURL).openStream();
				picture = BitmapFactory.decodeStream(input);
				input.close();
			} catch (Exception e) {
				picture = null;
				e.printStackTrace();
			}
			return picture;
		}

		protected void onPostExecute(Bitmap result) {
			media.setPicture(result);
			notifyDataSetChanged();
		}
	}

	
	@Override
	public void update(Observable observable, Object data) {
		notifyDataSetChanged();
	}

	private class FollowTask extends AsyncTask<String, Void, String>{
		private HttpResponse response;
		@Override
		protected String doInBackground(String... params) {
			HttpClient client = new DefaultHttpClient();
			HttpPost follow = new HttpPost("https://api.twitter.com/1.1/friendships/create.json?user_id="+params[0]+"&follow=true");
			try {
				consumer.sign(follow);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				response = client.execute(follow);
			} catch (ClientProtocolException e) {
				int statusCode = response.getStatusLine().getStatusCode();
				return "" + statusCode;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
		}
	}
	
	private class RetweetTask extends AsyncTask<String, Void, String>{
		private HttpResponse response;
		@Override
		protected String doInBackground(String... params) {
			HttpClient client = new DefaultHttpClient();
			HttpPost retweet = new HttpPost("https://api.twitter.com/1.1/statuses/retweet/"+params[0]+".json");
			try {
				consumer.sign(retweet);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				response = client.execute(retweet);
			} catch (ClientProtocolException e) {
				int statusCode = response.getStatusLine().getStatusCode();
				return "" + statusCode;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
		}
	}
}
