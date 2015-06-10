package nl.saxion.spookystoriestweets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ProfileActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
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
}
