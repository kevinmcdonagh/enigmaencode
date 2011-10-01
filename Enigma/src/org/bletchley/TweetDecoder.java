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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TweetDecoder extends Activity{

	private TextView txt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_decoder);
		txt = (TextView)findViewById(R.id.message_txt);
		
		Button btn = (Button)findViewById(R.id.message_btn_reply);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(TweetDecoder.this, Text.class));
			}
		});
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
		if(msgToEncrypt!=null){
			txt.setText(msgToEncrypt);
		}else{
			txt.setText("You don't have the right secret key!");
		}
	}

}
