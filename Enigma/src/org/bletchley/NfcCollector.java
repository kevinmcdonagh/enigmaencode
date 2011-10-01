package org.bletchley;

import java.util.List;

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

public class NfcCollector extends Activity{

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
        Log.i("TAG", "collecting text = " + msgToEncrypt);
        if(msgToEncrypt!=null && msgToEncrypt!=""){
        	this.msg = msgToEncrypt;
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
            Log.i("TAG", "Secret key is" + t.getText());
            
            
            String text = EncryptMgr.encrypt("secretcode", this.msg);
            
        	Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        	shareIntent.setType("text/plain");
        	shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Secret Code");
        	shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        	
        	Log.i("TAG", "code:" + text);
        	
        	startActivity(Intent.createChooser(shareIntent, "Share your message how?"));
        	
        } else {
            Log.e(TAG, "Unknown intent " + intent);
            finish();
            return;
        }
    }

}
