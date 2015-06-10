package nl.saxion.spookystoriestweets.view;

import java.io.InputStream;

import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.sax.StartElementListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import nl.saxion.spookystoriestweets.Entity;
import nl.saxion.spookystoriestweets.Hashtag;
import nl.saxion.spookystoriestweets.MainActivity;
import nl.saxion.spookystoriestweets.Media;
import nl.saxion.spookystoriestweets.R;
import nl.saxion.spookystoriestweets.Tweet;
import nl.saxion.spookystoriestweets.Url;
import nl.saxion.spookystoriestweets.User;

public class TwitterAdapter extends ArrayAdapter<Tweet> implements Observer {

	private SpannableStringBuilder sbTweet;
	private Context context;
	
	public TwitterAdapter(Context context, ArrayList<Tweet> listOfTweets) {
		super(context, 0, listOfTweets);
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tweet currentTweet = getItem(position);
		User currentUser = currentTweet.getUser();
		
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.listviewitem, parent, false);
		}
		
		TextView user = (TextView) convertView.findViewById(R.id.tvUser);
		TextView tweetText = (TextView) convertView.findViewById(R.id.tvTweet);
		LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.llDisplayTweet);
		sbTweet = new SpannableStringBuilder(currentTweet.getTweetText());
		int hashtagC = Color.rgb(26, 13, 131);
		
		
		for (int i = 0; i < ll.getChildCount(); i++) {
			if (ll.getChildAt(i) instanceof ImageView) {
				ImageView imageView = (ImageView) ll.getChildAt(i);
				ll.removeView(imageView);
			}
		}
		
		for (Entity ent : currentTweet.getEntities()) {
			if (ent instanceof Hashtag) {
				sbTweet.setSpan(new ForegroundColorSpan(hashtagC), ent.getIndex(1), ent.getIndex(2), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			if (ent instanceof Url) {
				final Url url = (Url) ent;
				ClickableSpan clickableSpan = new ClickableSpan() {
					
					@Override
					public void onClick(View widget) {
						Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(url.getUrl()));
						context.startActivity(browser);
					}
				};
				sbTweet.setSpan(clickableSpan, ent.getIndex(1), ent.getIndex(2), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				
			}
			if (ent instanceof Media)  {
				Bitmap image = ((Media) ent).getPicture();
				if (image == null) {
					GetImages task = new GetImages(ent);
					task.execute(((Media) ent).getMediaURL());
				} else {
					ImageView view = new ImageView(context);
					//view.setImageBitmap(getRoundedShape(((Media) ent).getPicture()));
					view.setImageBitmap(((Media)ent).getPicture());
					ll.addView(view);
					
				}
			}
		}
		
		user.setText(currentUser.getScreenName());
		tweetText.setText(sbTweet);
		tweetText.setMovementMethod(LinkMovementMethod.getInstance());
		
		return convertView;
	}
	
	@Override
	public void update(Observable observable, Object data) {
		notifyDataSetChanged();		
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
			updateList();
		}
	}
	
	public void updateList() {
		notifyDataSetChanged();
	}

	public Bitmap scaleBitmap(Bitmap scaleBitmapImage) {
	    int targetWidth = 120;
	    int targetHeight = 120;
	    Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, 
	                        targetHeight,Bitmap.Config.ARGB_8888);

	    Canvas canvas = new Canvas(targetBitmap);
	    Path path = new Path();
	    path.addCircle(((float) targetWidth - 1) / 2,
	        ((float) targetHeight - 1) / 2,
	        (Math.min(((float) targetWidth), 
	        ((float) targetHeight)) / 2),
	        Path.Direction.CCW);

	    canvas.clipPath(path);
	    Bitmap sourceBitmap = scaleBitmapImage;
	    canvas.drawBitmap(sourceBitmap, 
	        new Rect(0, 0, sourceBitmap.getWidth(),
	        sourceBitmap.getHeight()), 
	        new Rect(0, 0, targetWidth, targetHeight), null);
	    return targetBitmap;
	}

}
