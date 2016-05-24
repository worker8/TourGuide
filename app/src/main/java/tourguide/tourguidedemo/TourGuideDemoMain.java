package tourguide.tourguidedemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
        Intent mIntent;
        public Object getItem(int arg0) { return null;}
        public long getItemId(int position) { return position; }
        public int getCount() {
            return 18;
//            return 19;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            /* setup views */
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.row, null);
            final TextView text = (TextView) row.findViewById(R.id.text);
            final ImageView infoIcon = (ImageView) row.findViewById(R.id.info_icon);
            /* just some styling */
            if ((position % 2) == 0){
                row.setBackgroundColor(Color.parseColor("#3498db"));
            } else {
                row.setBackgroundColor(Color.parseColor("#2980b9"));
            }
            /* setup activities to be launched */
            if (position == 0){
                text.setText("Basic Activity");
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIntent = new Intent(mActivity, BasicActivity.class);
                        startActivity(mIntent);
                    }
                });
            } else if (position == 1){
                text.setText("Pointer: color");
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIntent = new Intent(mActivity, BasicActivity.class);
                        mIntent.putExtra(BasicActivity.COLOR_DEMO, true);
                        startActivity(mIntent);
                    }
                });
            } else if (position == 2){
                text.setText("Pointer: gravity");
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIntent = new Intent(mActivity, BasicActivity.class);
                        mIntent.putExtra(BasicActivity.GRAVITY_DEMO, true);
                        startActivity(mIntent);
                    }
                });
            } else if (position == 3){
                text.setText("Toolbar Example");row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIntent = new Intent(mActivity, ToolbarActivity.class);
                        mIntent.putExtra(ToolbarActivity.STATUS_BAR, true);
                        startActivity(mIntent);
                    }
                });
            } else if (position == 4){
                text.setText("Toolbar Example\n(no status bar)");
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIntent = new Intent(mActivity, ToolbarActivity.class);
                        mIntent.putExtra(ToolbarActivity.STATUS_BAR, false);
                        startActivity(mIntent);
                    }
                });
            } else if (position == 5){
                text.setText("ToolTip Gravity I");
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIntent = new Intent(mActivity, ToolTipGravityActivity.class);
                        mIntent.putExtra(ToolTipGravityActivity.TOOLTIP_NUM, 1);
                        startActivity(mIntent);
                    }
                });
            } else if (position == 6){
                text.setText("ToolTip Gravity II");
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIntent = new Intent(mActivity, ToolTipGravityActivity.class);
                        mIntent.putExtra(ToolTipGravityActivity.TOOLTIP_NUM, 2);
                        startActivity(mIntent);
                    }
                });
            } else if (position == 7){
                text.setText("ToolTip Gravity III");
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIntent = new Intent(mActivity, ToolTipGravityActivity.class);
                        mIntent.putExtra(ToolTipGravityActivity.TOOLTIP_NUM, 3);
                        startActivity(mIntent);
                    }
                });
            } else if (position == 8){
                text.setText("ToolTip Gravity IV");
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIntent = new Intent(mActivity, ToolTipGravityActivity.class);
                        mIntent.putExtra(ToolTipGravityActivity.TOOLTIP_NUM, 4);
                        startActivity(mIntent);
                    }
                });
            } else if (position == 9){
                text.setText("ToolTip Customization");
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIntent = new Intent(mActivity, ToolTipCustomizationActivity.class);
                        startActivity(mIntent);
                    }
                });
            } else if (position == 10){
                text.setText("Multiple ToolTip");
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIntent = new Intent(mActivity, MultipleToolTipActivity.class);
                        startActivity(mIntent);
                    }
                });
            } else if (position == 11){
                text.setText("Overlay Customization");
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIntent = new Intent(mActivity, OverlayCustomizationActivity.class);
                        startActivity(mIntent);
                    }
                });
            } else if (position == 12){
                text.setText("ToolTip & Overlay only, no Pointer");
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIntent = new Intent(mActivity, NoPointerActivity.class);
                        startActivity(mIntent);
                    }
                });
            } else if (position == 13){
                text.setText("Overlay only, no Tooltip, no Pointer");
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIntent = new Intent(mActivity, NoPointerNoToolTipActivity.class);
                        startActivity(mIntent);
                    }
                });
            } else if (position == 14){
                text.setText("ToolTip & Pointer only, no Overlay");
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIntent = new Intent(mActivity, NoOverlayActivity.class);
                        startActivity(mIntent);
                    }
                });
            } else if (position == 15) {
                text.setText("Button Tour (Manual)");
                infoIcon.setVisibility(View.VISIBLE);
                infoIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                        builder.setTitle("Button Tour").setMessage("- Button Tour example shows a sequence of TourGuide running on different buttons. \n- The method of proceeding to the next TourGuide is to press on the button itself. \n- This is suitable when you actually want the user to click on the button during the Tour.\n");
                        builder.create().show();
                    }
                });
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIntent = new Intent(mActivity, ManualSequenceActivity.class);
                        startActivity(mIntent);
                    }
                });
            } else if (position == 16) {
                // setup row
                text.setText("Overlay Tour (with Sequence class)");
                infoIcon.setVisibility(View.VISIBLE);
                infoIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                        builder.setTitle("Overlay Tour").setMessage("- Overlay Tour example shows a sequence of TourGuide running on different buttons. \n- The method of proceeding to the next TourGuide is to click on the Overlay instead of the button itself. \n- This is suitable when you just want to explain the usage of each buttons, but don't actually want the user to click on them.\n- This example also shows how to use the Sequence class to do a series of TourGuide, while the outcome looks the same as Button Tour, the code is more readable.");
                        builder.create().show();
                    }
                });

                // setup dialog
                final Dialog dialog = new Dialog(mActivity, R.style.Base_Theme_AppCompat_Dialog);
                dialog.setContentView(R.layout.sequence_dialog);
                dialog.setTitle(null);
                TextView overlay = (TextView)dialog.findViewById(R.id.overlay);
                TextView overlayListener = (TextView)dialog.findViewById(R.id.overlay_listener);
                overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mIntent = new Intent(mActivity, OverlaySequenceTourActivity.class);
                        mIntent.putExtra(OverlaySequenceTourActivity.CONTINUE_METHOD, OverlaySequenceTourActivity.OVERLAY_METHOD);
                        startActivity(mIntent);
                    }
                });
                overlayListener.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mIntent = new Intent(mActivity, OverlaySequenceTourActivity.class);
                        mIntent.putExtra(OverlaySequenceTourActivity.CONTINUE_METHOD, OverlaySequenceTourActivity.OVERLAY_LISTENER_METHOD);
                        startActivity(mIntent);
                    }
                });
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.show();
                    }
                });
            } else if (position == 17){
                text.setText("Navigational Drawer");
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIntent = new Intent(mActivity, NavDrawerActivity.class);
                        startActivity(mIntent);
                    }
                });
            }
//            else if (position == 17){
//                mIntent = new Intent(mActivity, MemoryLeakTestActivity.class);
//                text.setText("Memory Leak Test");
//            }

            return row;
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
