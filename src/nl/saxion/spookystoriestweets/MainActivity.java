package nl.saxion.spookystoriestweets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;

import nl.saxion.spookystoriestweets.model.Model;
import nl.saxion.spookystoriestweets.model.Tweet;
import nl.saxion.spookystoriestweets.view.CustomAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class MainActivity extends Activity implements Observer {
	
	private CustomAdapter adapter;
	private ListView tweets;
	private Model model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TweetApplication app = (TweetApplication) getApplicationContext();
		model = app.getModel();
		model.addObserver(this);
		adapter = new CustomAdapter(this.getBaseContext(), R.layout.tweet, model.getData());
		tweets = (ListView) findViewById(R.id.listView1);
		tweets.setAdapter(adapter);
		
		try {
			String json = readAssetIntoString("json.txt");
			JSONObject jObject = new JSONObject(json);
			JSONArray jArray = jObject.getJSONArray("statuses");
			
			for(int i = 0; i < jArray.length(); i++){
				JSONObject temp;
				temp = jArray.getJSONObject(i);
				Tweet parsedTweet = new Tweet(temp.getString("name") ," ", temp.getString("text"), temp.getString("created_at"), 0 ,0);
				model.addTweet(parsedTweet);

				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private String readAssetIntoString(String filename) throws IOException {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
 
		String line;
		try {
			InputStream is = getAssets().open(filename, AssetManager.ACCESS_BUFFER);
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
            throw e;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void update(Observable observable, Object data) {
		adapter.notifyDataSetChanged();
		tweets.setBackgroundColor(Color.BLACK);
		
	}
}
