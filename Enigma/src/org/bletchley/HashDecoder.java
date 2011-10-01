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

public class HashDecoder extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_decoder);
		TextView txt = (TextView)findViewById(R.id.message_txt);
		
		Bundle extras = getIntent().getExtras();  
		Set<String> keys = extras.keySet();  
		Iterator<String> iterate = keys.iterator();  
		while (iterate.hasNext()) {  
		    String key = iterate.next();  
		    Log.v("TAG", key + "[" + extras.get(key) + "]");  
		}  
		
		
		
		String msgToEncrypt = getIntent().getStringExtra(android.content.Intent.EXTRA_TEXT);

		
		Matcher m = Pattern.compile("\"(?:[^\\\\\"]+|\\\\.)*\"").matcher(msgToEncrypt);
		if (m.find()){
			msgToEncrypt = m.group(0);
		}
		Log.i("TAG", msgToEncrypt);
		
		
		m = Pattern.compile(":.*").matcher(msgToEncrypt);
		if (m.find()){
			msgToEncrypt = m.group(0);
		}
		
//		String s = (String) msgToEncrypt.subSequence(1, msgToEncrypt.lastIndexOf(msgToEncrypt));
		
		msgToEncrypt = msgToEncrypt.substring(1);
		msgToEncrypt = msgToEncrypt.substring(0, msgToEncrypt.length() -1);
		
		
		String t = EncryptMgr.decrypt("secretcode", msgToEncrypt);
		
		txt.setText(t);
	}
}
