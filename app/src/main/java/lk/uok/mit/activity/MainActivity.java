package lk.uok.mit.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import lk.uok.mit.fragment.AccelerometerDemoFragment;
import lk.uok.mit.fragment.EnvironmentSensorsDemoFragment;
import lk.uok.mit.fragment.GyroscopeDemoFragment;
import lk.uok.mit.fragment.MainFragment;
import lk.uok.mit.fragment.OrientationSensorDemoFragment;
import lk.uok.mit.fragment.ProximitySensorDemoFragment;
import lk.uok.mit.fragment.RotationalVectorSensorDemoFragment;
import lk.uok.mit.helloworld.HelloWorldActivity;
import lk.uok.mit.helloworld.R;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get a reference to the "android.support.v7.widget.Toolbar"
        Toolbar toolbar = findViewById(R.id.toolbar);
        //set the toolbar as the action bar
        setSupportActionBar(toolbar);
        //get a reference to "android.support.v7.app.ActionBar"
        ActionBar actionbar = getSupportActionBar();
        //Enable the app bar's "home" button
        actionbar.setDisplayHomeAsUpEnabled(true);
        //set the icon we added above as the button
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        //initialize the context
        this.context = getApplicationContext();
        //get a reference to the drawer layout
        this.mDrawerLayout = findViewById(R.id.drawer_layout);

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.fragment_content, new MainFragment());
        // or ft.add(R.id.content_frame, new MainFragment());
        // Complete the changes added above
        ft.commit();

        //get a reference to the navigation view
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        // Add code here to update the UI based on the item selected
                        //to determine which menu item is clicked, add a switch
                        Intent intent = null;
                        switch (menuItem.getItemId()) {
                            case R.id.nav_send_message:
                                //navigate to SendMessageActivity
                                intent = new Intent(context, SendMessageActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.nav_hello_world:
                                //Navigate to HelloWorldActivity
                                intent = new Intent(context, HelloWorldActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.nav_accelerometer_demo:
                                //Open the  AccelerometerDemoFragment
                                // Begin the transaction
                                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                // Replace the contents of the container with the new fragment
                                ft.replace(R.id.fragment_content, new AccelerometerDemoFragment());
                                // or ft.add(R.id.content_frame, new MainFragment());
                                // Complete the changes added above
                                ft.commit();
                                break;
                            case R.id.nav_rotational_vector_sensor_demo:
                                //Open the  RotationalVectorSensorDemoFragment
                                // Begin the transaction
                                ft = getSupportFragmentManager().beginTransaction();
                                // Replace the contents of the container with the new fragment
                                ft.replace(R.id.fragment_content, new RotationalVectorSensorDemoFragment());
                                // or ft.add(R.id.content_frame, new MainFragment());
                                // Complete the changes added above
                                ft.commit();
                                break;
                            case R.id.nav_orientation_sensor_demo:
                                //Open the  OrientationSensorDemoFragment
                                // Begin the transaction
                                ft = getSupportFragmentManager().beginTransaction();
                                // Replace the contents of the container with the new fragment
                                ft.replace(R.id.fragment_content, new OrientationSensorDemoFragment());
                                // or ft.add(R.id.content_frame, new MainFragment());
                                // Complete the changes added above
                                ft.commit();
                                break;
                            case R.id.nav_proximity_sensor_demo:
                                //Open the  ProximitySensorDemoFragment
                                // Begin the transaction
                                ft = getSupportFragmentManager().beginTransaction();
                                // Replace the contents of the container with the new fragment
                                ft.replace(R.id.fragment_content, new ProximitySensorDemoFragment());
                                // or ft.add(R.id.content_frame, new MainFragment());
                                // Complete the changes added above
                                ft.commit();
                                break;
                            case R.id.nav_environment_sensor_demo:
                                //Open the  EnvironmentSensorsDemoFragment
                                // Begin the transaction
                                ft = getSupportFragmentManager().beginTransaction();
                                // Replace the contents of the container with the new fragment
                                ft.replace(R.id.fragment_content, new EnvironmentSensorsDemoFragment());
                                // or ft.add(R.id.content_frame, new MainFragment());
                                // Complete the changes added above
                                ft.commit();
                                break;
                            case R.id.nav_gyroscope_demo:
                                //Open the  GyroscopeDemoFragment
                                // Begin the transaction
                                ft = getSupportFragmentManager().beginTransaction();
                                // Replace the contents of the container with the new fragment
                                ft.replace(R.id.fragment_content, new GyroscopeDemoFragment());
                                // or ft.add(R.id.content_frame, new MainFragment());
                                // Complete the changes added above
                                ft.commit();
                                break;
                        }
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
