package nl.saxion.spookystoriestweets.view;

import java.util.ArrayList;
import java.util.List;

import nl.saxion.spookystoriestweets.R;
import nl.saxion.spookystoriestweets.model.Tweet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
	            // hier moeten nog meer textviews ed. bij

	        
	            v.setTag(holder);
	        }
	        else {
	            holder = (ViewHolder)v.getTag();
	        }
	        Tweet temp = data.get(position);
	        holder.tvTweeter.setText(temp.getName());
	        holder.tvText.setText(temp.getText());
	        
	        return v;
	        }
	 
	 static class ViewHolder{
		 TextView tvTweeter;
		 TextView tvText;
	
		 
	 }
}
