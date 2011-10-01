package org.bletchley;

import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bletchley.cypher.EncryptMgr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class TweetDecoder extends Activity{

	private TextView txt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_decoder);
		txt = (TextView)findViewById(R.id.message_txt);
	}
	
	@Override
	protected void onResume() {
		super.onRestart();
		Bundle extras = getIntent().getExtras();  
		Set<String> keys = extras.keySet();  
		Iterator<String> iterate = keys.iterator();  
		while (iterate.hasNext()) {  
		    String key = iterate.next();  
		    Log.v("TAG", key + "[" + extras.get(key) + "]");  
		}  
		
		String msgToEncrypt = getIntent().getStringExtra(android.content.Intent.EXTRA_TEXT);
//		msgToEncrypt = hackishlySanatiseTweet(msgToEncrypt);
//		String t = EncryptMgr.decrypt("secretcode", msgToEncrypt);
		txt.setText(msgToEncrypt);
	}

}
