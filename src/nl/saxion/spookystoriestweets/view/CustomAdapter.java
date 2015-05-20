package nl.saxion.spookystoriestweets.view;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import nl.saxion.spookystoriestweets.R;
import nl.saxion.spookystoriestweets.model.Tweet;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<Tweet>{
	
	private ArrayList<Tweet> data;

	public CustomAdapter(Context context, int resource, List<Tweet> objects) {
		super(context, resource, objects);
		data = (ArrayList<Tweet>) objects;
	}
	
	 @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View v = convertView;
	        ViewHolder holder; 
	        if (v == null) {
	            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            v = vi.inflate(R.layout.tweet, parent, false);

	      
	            holder = new ViewHolder();
	            holder.tvTweeter = (TextView) v.findViewById(R.id.tvTweeter);
	            holder.tvText = (TextView) v.findViewById(R.id.tvTweetedText);
	            holder.tvTimeStamp = (TextView) v.findViewById(R.id.tvDateOfTweet);
	            holder.tvTweeterTag = (TextView) v.findViewById(R.id.tvTweeterTag);
	            holder.avatar = (ImageView) v.findViewById(R.id.ivAvatar);
	            // hier moeten nog meer textviews ed. bij

	        
	            v.setTag(holder);
	        }
	        else {
	            holder = (ViewHolder)v.getTag();
	        }
	        Tweet temp = data.get(position);
	        holder.tvTweeter.setText(temp.getName());
	        holder.tvText.setText(temp.getText());
	        holder.tvTimeStamp.setText(temp.getTimeStamp());
	        holder.tvTweeterTag.setText(temp.getTag());
	        
	        holder.avatar.setImageBitmap(getImageBitmap(temp.getAvatarURL()));
	        
	        return v;
	        }
	 
	 private Bitmap getImageBitmap(String url) {
	        Bitmap bm = null;
	        try {
	            URL aURL = new URL(url);
	            URLConnection conn = aURL.openConnection();
	            conn.connect();
	            InputStream is = conn.getInputStream();
	            BufferedInputStream bis = new BufferedInputStream(is);
	            bm = BitmapFactory.decodeStream(bis);
	            bis.close();
	            is.close();
	       } catch (IOException e) {
	           Log.d("error", "error downloading image");
	       }
	       return bm;
	    } 
	 
	 static class ViewHolder{
		 TextView tvTweeter;
		 TextView tvText;
		 TextView tvTimeStamp;
		 TextView tvTweeterTag;
		 ImageView avatar;
	
		 
	 }
}
