package wouter.vandamme.robbe.roels.flashcard.views;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import wouter.vandamme.robbe.roels.flashcard.R;

public class RegisterActivity extends CustomActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

}
