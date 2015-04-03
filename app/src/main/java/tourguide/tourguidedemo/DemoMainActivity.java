package tourguide.tourguidedemo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import tourguide.tourguide.AnimateTutorial;


public class DemoMainActivity extends ActionBarActivity {
    public AnimateTutorial mTutorialHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_demo_main);
        TextView textView = (TextView)findViewById(R.id.text);
        mTutorialHandler = AnimateTutorial.init(this).with(AnimateTutorial.Technique.Click)
                .duration(700)
                .disableClick(true)
//                .gravity(Gravity.TOP | Gravity.RIGHT)
                .motionType(AnimateTutorial.MotionType.ClickOnly)
                .title("Hello Tour Guide")
                .description("This is a tutorial library")
                .playOn(textView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_demo_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
