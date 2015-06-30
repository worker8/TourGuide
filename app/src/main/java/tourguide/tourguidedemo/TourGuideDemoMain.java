package tourguide.tourguidedemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TourGuideDemoMain extends ActionBarActivity {

    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_tour_guide_demo_main);
        ListView listview = (ListView)findViewById(R.id.listview);
        listview.setAdapter(new CustomAdapter());

    }

    class CustomAdapter extends BaseAdapter {

        public Object getItem(int arg0) { return null;}
        public long getItemId(int position) { return position; }
        public int getCount() {
            return 13;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            /* setup views */
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.row, null);
            final TextView text = (TextView) row.findViewById(R.id.text);

            /* just some styling */
            if ((position % 2) == 0){
                row.setBackgroundColor(Color.parseColor("#3498db"));
            } else {
                row.setBackgroundColor(Color.parseColor("#2980b9"));
            }
            Intent intent = null;

            /* setup activities to be launched */
            if (position == 0){
                intent = new Intent(mActivity, BasicActivity.class);
                text.setText("Basic Activity");
            } else if (position == 1){
                intent = new Intent(mActivity, BasicActivity.class);
                intent.putExtra(BasicActivity.COLOR_DEMO, true);
                text.setText("Pointer: color");
            } else if (position == 2){
                intent = new Intent(mActivity, BasicActivity.class);
                intent.putExtra(BasicActivity.GRAVITY_DEMO, true);
                text.setText("Pointer: gravity");
            } else if (position == 3){
                intent = new Intent(mActivity, ToolbarActivity.class);
                intent.putExtra(ToolbarActivity.STATUS_BAR, true);
                text.setText("Toolbar Example");
            } else if (position == 4){
                intent = new Intent(mActivity, ToolbarActivity.class);
                intent.putExtra(ToolbarActivity.STATUS_BAR, false);
                text.setText("Toolbar Example\n(no status bar)");
            } else if (position == 5){
                intent = new Intent(mActivity, ToolTipGravityActivity.class);
                intent.putExtra(ToolTipGravityActivity.TOOLTIP_NUM, 1);
                text.setText("ToolTip Gravity I");
            } else if (position == 6){
                intent = new Intent(mActivity, ToolTipGravityActivity.class);
                intent.putExtra(ToolTipGravityActivity.TOOLTIP_NUM, 2);
                text.setText("ToolTip Gravity II");
            } else if (position == 7){
                intent = new Intent(mActivity, ToolTipGravityActivity.class);
                intent.putExtra(ToolTipGravityActivity.TOOLTIP_NUM, 3);
                text.setText("ToolTip Gravity III");
            } else if (position == 8){
                intent = new Intent(mActivity, ToolTipGravityActivity.class);
                intent.putExtra(ToolTipGravityActivity.TOOLTIP_NUM, 4);
                text.setText("ToolTip Gravity IV");
            } else if (position == 9){
                intent = new Intent(mActivity, CustomizationActivity.class);
                text.setText("ToolTip Customization");
            } else if (position == 10){
                intent = new Intent(mActivity, NoPointerActivity.class);
                text.setText("ToolTip & Overlay only, no Pointer");
            } else if (position == 11){
                intent = new Intent(mActivity, NoPointerNoToolTipActivity.class);
                text.setText("ToolTip only, no Overlay, no Pointer");
            } else if (position == 12){
                intent = new Intent(mActivity, NoOverlayActivity.class);
                text.setText("ToolTip & Pointer only, no Overlay");
            }

            /* launch the activity */
            final Intent finalIntent = intent;
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(finalIntent);
                }
            });

            return (row);
        }
    }

// Unused code for menu
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_tour_guide_demo_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
