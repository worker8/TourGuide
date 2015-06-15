package tourguide.tourguidedemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import tourguide.tourguide.AnimateTutorial;


public class DemoMainActivity extends ActionBarActivity {
    public AnimateTutorial mTutorialHandler;
    public Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        mActivity = this;
        setContentView(R.layout.activity_demo_main);
        Button button = (Button)findViewById(R.id.button);
        Button button_dont_touch = (Button)findViewById(R.id.button_dont_touch);
        mTutorialHandler = AnimateTutorial.init(this).with(AnimateTutorial.Technique.Click)
                .duration(700)
                .disableClick(true)
                .gravity(Gravity.CENTER)
                .motionType(AnimateTutorial.MotionType.ClickOnly)
                .title("Welcome!")
                .description("Click on the start button to begin")
                .playOn(button);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mTutorialHandler.cleanUp();
            }
        });
        button_dont_touch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, "Booom!",Toast.LENGTH_LONG).show();
            }
        });

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
