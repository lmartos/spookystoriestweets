package nl.saxion.spookystoriestweets.views;

/**	
 * 
 * @author Doron Hartog & Laurens Martos
 *
 */
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import nl.saxion.spookystoriestweets.R;
import nl.saxion.spookystoriestweets.SpookyStoriesTweetsApplication;
import nl.saxion.spookystoriestweets.model.Model;
import nl.saxion.spookystoriestweets.model.User;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FollowerAdapter extends ArrayAdapter<User> implements Observer {

	private TextView tvFollowerName, tvFollowerScreenName;
	private ImageView ivFollowerAvatar;
	private Model model;

	public FollowerAdapter(Context context, ArrayList<User> listOfFollowers) {
		super(context, R.layout.listviewitem, listOfFollowers);
		model = ((SpookyStoriesTweetsApplication) context
				.getApplicationContext()).getModel();
		model.addObserver(this);
	}

	/**
	 * the getView method of the adapter
	 *  sets the different views
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final User currentUser = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.follower_item, parent, false);
		}

		tvFollowerName = (TextView) convertView
				.findViewById(R.id.tvFollowerName);
		tvFollowerScreenName = (TextView) convertView
				.findViewById(R.id.tvFollowerScreenName);
		ivFollowerAvatar = (ImageView) convertView
				.findViewById(R.id.ivFollowerAvatar);

		tvFollowerName.setText(currentUser.getName());
		tvFollowerScreenName.setText("@" + currentUser.getScreenName());
		ivFollowerAvatar.setImageBitmap(getImage(currentUser.getPictureURL()));

		return convertView;
	}

	/**
	 * the getter for the image of a user
	 * 
	 * @param url
	 *            The url of the image
	 * @return returns the bitmap if succesfully created
	 */
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

	/**
	 * updates the data
	 */
	@Override
	public void update(Observable observable, Object data) {
		notifyDataSetChanged();
	}

}
