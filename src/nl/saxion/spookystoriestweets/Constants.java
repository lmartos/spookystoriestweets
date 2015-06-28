package nl.saxion.spookystoriestweets;

/**
 * 
 * @author Doron Hartog & Laurens Martos
 *
 */
public class Constants {

	public static final String CONSUMER_KEY = "D7eeHiP0mPYd1RCHdu3aDsoFT";
	public static final String CONSUMER_SECRET = "DL9Wju1bRMdllWsbKAOSH4WlgZ1AA04hw48JjdSlrk0JRpSKb9";

	public static final String REQUEST_URL = "https://api.twitter.com/oauth/request_token";
	public static final String ACCESS_URL = "https://api.twitter.com/oauth/access_token";
	public static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";

	public static final String OAUTH_CALLBACK_SCHEME = "x-oauthflow-twitter";
	public static final String OAUTH_CALLBACK_HOST = "callback";
	public static final String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME
			+ "://" + OAUTH_CALLBACK_HOST;

}
