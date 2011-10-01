package org.bletchley;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bletchley.cypher.EncryptMgr;
import org.bletchley.nfc.NdefMessageParser;
import org.bletchley.nfc.record.ParsedNdefRecord;
import org.bletchley.nfc.record.TextRecord;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NfcDecryptRevealer extends Activity{

	String msg = "";
    static final String TAG = "ViewTag";

    /**
     * This activity will finish itself in this amount of time if the user
     * doesn't do anything.
     */
    static final int ACTIVITY_TIMEOUT_MS = 1 * 1000;

    TextView mTitle;

    LinearLayout mTagContent;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_nfc);
	}
	
    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		
		Intent i = getIntent();
        String msgToEncrypt = i.getStringExtra(android.content.Intent.EXTRA_TEXT);
        if(msgToEncrypt!=null && msgToEncrypt!=""){
        	this.msg = hackishlySanatiseTweet(msgToEncrypt);
        }
	}
	
    void resolveIntent(Intent intent) {
        // Parse the intent
        String action = intent.getAction();
        String msgToEncrypt = intent.getStringExtra(android.content.Intent.EXTRA_TEXT);
        
        if ("android.nfc.action.NDEF_DISCOVERED".equals(action)) {
        	
            // When a tag is discovered we send it to the service to be save. We
            // include a PendingIntent for the service to call back onto. This
            // will cause this activity to be restarted with onNewIntent(). At
            // that time we read it from the database and view it.
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[] {};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
                msgs = new NdefMessage[] {msg};
            }
            
            List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);        	
            TextRecord t = (TextRecord)records.get(0);
            Log.i("TAG", "DECRYPT: Secret key is" + t.getText());
            
            String text = EncryptMgr.decrypt(t.getText(), this.msg);
        	Intent revealer = new Intent(NfcDecryptRevealer.this, TweetDecoder.class);
        	
        	Log.i("TAG", "DECRYPTING: passing text:" + text);
        	revealer.putExtra(android.content.Intent.EXTRA_TEXT, text);
        	startActivity(revealer);
        	
        } else {
            Log.e(TAG, "Unknown intent " + intent);
            finish();
            return;
        }
    }
    
	private String hackishlySanatiseTweet(String msgToEncrypt) {
		Matcher m = Pattern.compile("\"(?:[^\\\\\"]+|\\\\.)*\"").matcher(msgToEncrypt);
		if (m.find()){
			msgToEncrypt = m.group(0);
		}
		Log.i("TAG", msgToEncrypt);
		
		
		m = Pattern.compile(":.*").matcher(msgToEncrypt);
		if (m.find()){
			msgToEncrypt = m.group(0);
		}
		
		msgToEncrypt = msgToEncrypt.substring(1);
		msgToEncrypt = msgToEncrypt.substring(0, msgToEncrypt.length() -1);
//		String s = (String) msgToEncrypt.subSequence(1, msgToEncrypt.lastIndexOf(msgToEncrypt));
		return msgToEncrypt;
	}

}
