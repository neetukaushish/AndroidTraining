/*package com.addvalsolutions.mymap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class FindPlace extends Activity {
	EditText text_place_name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.searcharound);
            text_place_name = (EditText)findViewById(R.id.text_place_name);
            
	} 
	 public void getPlace(View v){
		 String location=text_place_name.getText().toString();
		 Intent result = new Intent();
		 result.putExtra("location",location);
		
		 setResult(Activity.RESULT_OK, result);		 
		 finish();
	 
			
		 
	 }

}*/