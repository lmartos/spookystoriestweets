package nl.saxion.spookystoriestweets;

/**	
 * 
 * @author Doron Hartog & Laurens Martos
 *
 */

import nl.saxion.spookystoriestweets.model.Model;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class PrepareRequestTokenActivity extends Activity {

	final String TAG = getClass().getName();

	private OAuthConsumer consumer;
	private OAuthProvider provider;
	private Model model;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			this.consumer = new CommonsHttpOAuthConsumer(
					Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
			this.provider = new CommonsHttpOAuthProvider(Constants.REQUEST_URL,
					Constants.ACCESS_URL, Constants.AUTHORIZE_URL);
		} catch (Exception e) {
			e.printStackTrace();
		}

		new OAuthRequestTokenTask(this, consumer, provider).execute();
	}

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		final Uri uri = intent.getData();
		if (uri != null
				&& uri.getScheme().equals(Constants.OAUTH_CALLBACK_SCHEME)) {
			new RetrieveAccessTokenTask(this, consumer, provider, prefs)
					.execute(uri);
			finish();
		}
	}

	public class RetrieveAccessTokenTask extends AsyncTask<Uri, Void, Void> {

		private Context context;
		private OAuthProvider provider;
		private OAuthConsumer consumer;
		private SharedPreferences prefs;

		public RetrieveAccessTokenTask(Context context, OAuthConsumer consumer,
				OAuthProvider provider, SharedPreferences prefs) {
			this.context = context;
			this.consumer = consumer;
			this.provider = provider;
			this.prefs = prefs;
		}

		@Override
		protected Void doInBackground(Uri... params) {
			final Uri uri = params[0];
			final String oauth_verifier = uri
					.getQueryParameter(OAuth.OAUTH_VERIFIER);

			try {
				provider.retrieveAccessToken(consumer, oauth_verifier);

				final Editor edit = prefs.edit();
				edit.putString(OAuth.OAUTH_TOKEN, consumer.getToken());
				edit.putString(OAuth.OAUTH_TOKEN_SECRET,
						consumer.getTokenSecret());
				edit.commit();

				String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
				String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");

				consumer.setTokenWithSecret(token, secret);

				model = ((SpookyStoriesTweetsApplication) getBaseContext()
						.getApplicationContext()).getModel();
				model.setConsumer(consumer);
				model.setProvider(provider);
				finish();


			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

	}

}
