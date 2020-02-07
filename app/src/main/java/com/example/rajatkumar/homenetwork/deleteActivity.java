package com.example.rajatkumar.homenetwork;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class deleteActivity extends Activity {
    Button buttonDelete;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        buttonDelete=findViewById(R.id.deleteBtn);
        textView= findViewById(R.id.deleteTV);

        Bundle b = getIntent().getExtras();
        textView.setText(b.getString("mac"));
        Log.i("mac", textView.getText().toString());

        buttonDelete.setOnClickListener(e->{
            Intent intent = new Intent(this, RouterQueryServiceTest.class);
            intent.putExtra("action", "deleteVlan");
            intent.putExtra("ssid", textView.getText().toString());
            setResult(600, intent);
            finish();
        });
    }

}
