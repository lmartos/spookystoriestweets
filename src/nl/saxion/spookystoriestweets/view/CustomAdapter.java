package nl.saxion.spookystoriestweets.view;

import java.util.List;

import nl.saxion.spookystoriestweets.model.Tweet;
import android.content.Context;
import android.widget.ArrayAdapter;

public class CustomAdapter extends ArrayAdapter<Tweet>{

	public CustomAdapter(Context context, int resource, List<Tweet> objects) {
		super(context, resource, objects);
	}

}
