package nl.saxion.spookystoriestweets.views;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

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
import oauth.signpost.OAuthConsumer;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FollowerAdapter extends ArrayAdapter<User> implements Observer {

	
	private Context context;
	private TextView tvFollowerName, tvFollowerScreenName;
	private ImageView ivFollowerAvatar;
	private Model model;

	public FollowerAdapter(Context context, ArrayList<User> listOfFollowers) {
		super(context, R.layout.listviewitem, listOfFollowers);
		this.context = context;
		model = ((SpookyStoriesTweetsApplication) context.getApplicationContext()).getModel();
		model.addObserver(this);
	
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final User currentUser = getItem(position);
		

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.follower_item, parent, false);
		}
		
		tvFollowerName = (TextView) convertView.findViewById(R.id.tvFollowerName);
		tvFollowerScreenName = (TextView) convertView.findViewById(R.id.tvFollowerScreenName);
		ivFollowerAvatar = (ImageView) convertView.findViewById(R.id.ivFollowerAvatar);
		
		tvFollowerName.setText(currentUser.getName());
		System.out.println(currentUser.getName() + " " + currentUser.getScreenName());
		tvFollowerScreenName.setText("@" + currentUser.getScreenName());
		ivFollowerAvatar.setImageBitmap(getImage(currentUser.getPictureURL()));

		return convertView;
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
		notifyDataSetChanged();
	} 

}
