package org.bletchley;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Text extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_text);
        
        Button btn = (Button) findViewById(R.id.message_btn_done);
        btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Text.this, NfcEncryptCollector.class);
				TextView t = (TextView)findViewById(R.id.message_txt);
				
				intent.putExtra(android.content.Intent.EXTRA_TEXT, t.getText().toString());
				startActivity(intent);
			}
		});
    }
}