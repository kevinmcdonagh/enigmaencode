package org.bletchley;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Text extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_text);
        
        Button btn = (Button) findViewById(R.id.message_btn_done);
        btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Text.this, Nfc.class));
			}
		});
    }
}