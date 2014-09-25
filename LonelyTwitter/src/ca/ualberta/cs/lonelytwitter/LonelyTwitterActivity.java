package ca.ualberta.cs.lonelytwitter;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.cs.lonelytwitter.data.GsonDataManager;
import ca.ualberta.cs.lonelytwitter.data.IDataManager;

public class LonelyTwitterActivity extends Activity {

	private IDataManager dataManager;

	private EditText bodyText;

	private ListView oldTweetsList;

	private ArrayList<Tweet> tweets;

	private ArrayAdapter<Tweet> tweetsViewAdapter;
	
	private Summary mySummary;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		dataManager = new GsonDataManager(this);
	    //Summary mySummary = new Summary();

		bodyText = (EditText) findViewById(R.id.body);
		oldTweetsList = (ListView) findViewById(R.id.oldTweetsList);
	}

	@Override
	protected void onStart() {
		super.onStart();

		tweets = dataManager.loadTweets();
		tweetsViewAdapter = new ArrayAdapter<Tweet>(this,
				R.layout.list_item, tweets);
		oldTweetsList.setAdapter(tweetsViewAdapter);
	}

	public void save(View v) {

		String text = bodyText.getText().toString();

		Tweet tweet = new Tweet(new Date(), text);
		tweets.add(tweet);

		tweetsViewAdapter.notifyDataSetChanged();

		bodyText.setText("");
		dataManager.saveTweets(tweets);
	}

	public void clear(View v) {

		tweets.clear();
		tweetsViewAdapter.notifyDataSetChanged();
		dataManager.saveTweets(tweets);
	}
	
	public void summary(View v) {
		//createSummary();
		long summaryNumber = getAverageNumber();
		long summaryLength = getAverageLength();
		String summaryNum = Long.toString(summaryNumber);
		String summaryLen = Long.toString(summaryLength);
		Intent intent = new Intent(LonelyTwitterActivity.this, SummayActivity.class);
	    //intent.putExtra(EXTRA_MESSAGE, message);
	    startActivity(intent);
		Toast.makeText(this, "Number of tweets: "+summaryNum+"\nAverage Length: "+summaryLen, Toast.LENGTH_LONG).show();
	}
	
	private void createSummary() {
		mySummary.setAvgNumber(getAverageNumber());
		mySummary.setAvgLength(getAverageLength());

	}
	
	private long getAverageNumber() {
		long tweetsNumber = tweets.size();
		return tweetsNumber;
	}
	
	private long getAverageLength() {
		long tweetsLength = 0;
		for(int i = 1; i < tweets.size(); i++) {
			tweetsLength += tweets.get(i).getTweetBody().length();
		}
		tweetsLength = tweetsLength/tweets.size();
		return tweetsLength;
	}

}