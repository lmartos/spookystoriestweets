package nl.saxion.spookystoriestweets;

/**	
 * 
 * @author Doron Hartog & Laurens Martos
 *
 */

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;


public class OAuthRequestTokenTask extends AsyncTask<Void, Void, Void> {

	final String TAG = getClass().getName();
	private Context context;
	private OAuthProvider provider;
	private OAuthConsumer consumer;

	public OAuthRequestTokenTask(Context context, OAuthConsumer consumer,
			OAuthProvider provider) {

		this.context = context;
		this.consumer = consumer;
		this.provider = provider;
	}

	protected Void doInBackground(Void... params) {

		try {
			final String url = provider.retrieveRequestToken(consumer,
					Constants.OAUTH_CALLBACK_URL);
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url))
					.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
							| Intent.FLAG_ACTIVITY_NO_HISTORY
							| Intent.FLAG_FROM_BACKGROUND);
			context.startActivity(intent);
		} catch (Exception e) {
		}

		return null;
	}

}