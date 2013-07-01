/*package com.addvalsolutions.mymap;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ShowRoute extends Activity{
	EditText  textfrom;
	EditText  textto;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.showroute);
            textfrom=(EditText)findViewById(R.id.textfrom);
            textto=(EditText)findViewById(R.id.textto);
	} 
	
	 public void getText(View v){
		 String origin=textfrom.getText().toString();
		 String destination=textto.getText().toString();
		 
		 Intent result = new Intent();
		 result.putExtra("origin",origin);
		 result.putExtra("dest",destination);
		 setResult(Activity.RESULT_OK, result);		 
		 finish();
	 } 
}	 


		 
            */