package in.qrstaff.manager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
public class SettingsActivity extends Activity {
 public void onCreate(Bundle b){ super.onCreate(b); LinearLayout l=new LinearLayout(this); l.setOrientation(LinearLayout.VERTICAL); l.setPadding(32,32,32,32); TextView t=new TextView(this); t.setText("QR Staff - Server Setup"); t.setTextSize(26); l.addView(t); EditText e=new EditText(this); e.setHint("Enter server URL"); l.addView(e); Button x=new Button(this); x.setText("Save & Open"); l.addView(x); x.setOnClickListener(v->{String u=e.getText().toString().trim(); if(u.length()==0)return; getSharedPreferences("qrstaff",0).edit().putString("url",u).apply(); startActivity(new Intent(this,MainActivity.class)); finish();}); setContentView(l); }
}
