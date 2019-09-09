package com.example.cse331pathfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import hw7.Path;
import hw8.CampusBuilding;
import hw8.CampusModel;
import hw8.Coordinate;

/**
 * GUI for Campus Path Finder app on UW campus
 */

public class MainActivity extends AppCompatActivity {

    private DrawView view;
    private CampusModel model;
    private ListView origBuildingView;
    private ListView destBuildingView;
    private String destBuildingName = null;
    private String origBuildingName = null;
    private TextView MSGView;
    private Button resetButton;
    private Button findPathButton;

    /**
     * Launches the app from the Saved State
     * @param savedInstanceState Saved State of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        InputStream pathInputStream = this.getResources().openRawResource(R.raw.campus_paths);
        InputStream buildingInputStream =
                this.getResources().openRawResource(R.raw.campus_buildings_new);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            model = new CampusModel(pathInputStream, buildingInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        view = (DrawView) findViewById(R.id.imageView);
        findPathButton = (Button) findViewById(R.id.PathFindButton);
        findPathButton.setOnClickListener(findPathListener);
        origBuildingView = (ListView) findViewById(R.id.OrigBuildings);
        destBuildingView = (ListView) findViewById(R.id.DestBuildings);
        MSGView = (TextView) findViewById(R.id.MSG);
        resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(resetButtonListener);
        ArrayList<CampusBuilding> buildingList = model.getBuildings();
        ArrayList<String> buildingShortNames = new ArrayList<>();
        for (CampusBuilding b : buildingList) {
            buildingShortNames.add(b.getShortName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, buildingShortNames);
        destBuildingView.setAdapter(adapter);
        origBuildingView.setAdapter(adapter);
        origBuildingView.setOnItemClickListener(origBuildingClickListener);
        destBuildingView.setOnItemClickListener(destBuildingClickListener);
    }

    /**
     * Listener for find path button
     */
    private View.OnClickListener findPathListener = new View.OnClickListener() {
        /**
         * Finds path between two buildings on click of Find Path Button
         * @param v the view the listener is attatched to
         */
        public void onClick(View v) {
            if (origBuildingName != null && destBuildingName != null) {
                MSGView.setText("");
                CampusBuilding orig = null;
                CampusBuilding dest = null;
                for (CampusBuilding b : model.getBuildings()) {
                    if (b.getShortName().equals(origBuildingName)) {
                        orig = b;
                    }
                    if (b.getShortName().equals(destBuildingName)) {
                        dest = b;
                    }
                }
                Path<Coordinate, Double> path = model.shortestPath(orig, dest);
                view.displayPath(path);
                resetButton.setVisibility(View.VISIBLE);
                destBuildingView.setVisibility(View.INVISIBLE);
                origBuildingView.setVisibility(View.INVISIBLE);
                MSGView.setVisibility(View.VISIBLE);
                MSGView.setText(String.format("Distance: %d feet", Math.round(path.getWeight())));
                findPathButton.setVisibility(View.INVISIBLE);
                findViewById(R.id.From).setVisibility(View.INVISIBLE);
                findViewById(R.id.To).setVisibility(View.INVISIBLE);
            } else {
                if (destBuildingName == null) {
                    MSGView.setVisibility(View.VISIBLE);
                    MSGView.setText("Select a destination");
                }
                if (origBuildingName == null) {
                    MSGView.setVisibility(View.VISIBLE);
                    MSGView.setText("Select an origin");
                }
            }
        }
    };

    /**
     * Listener object for use on Dest Building button
     */
    private ListView.OnItemClickListener destBuildingClickListener = new ListView.OnItemClickListener() {
        /**
         * Sets destBuilding and prints message with dest building for user
         * @param adapter array adapter for the view
         * @param v view this is attached to
         * @param pos position of item in list clicked
         * @param id id of item in list clicked
         */
        public void onItemClick(AdapterView<?> adapter, View v, int pos, long id) {
            destBuildingName = (String) destBuildingView.getItemAtPosition(pos);
            Toast.makeText(getApplicationContext(), "Path to " + destBuildingName, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * Listener object for use on Reset button
     */
    private View.OnClickListener resetButtonListener = new View.OnClickListener() {
        /**
         * resets app functionality on click
         * @param v view this is attached to
         */
        public void onClick(View v) {
            view.toggleDrawPath();
            view.invalidate();
            resetButton.setVisibility(View.INVISIBLE);
            destBuildingView.setVisibility(View.VISIBLE);
            origBuildingView.setVisibility(View.VISIBLE);
            MSGView.setVisibility(View.INVISIBLE);
            findPathButton.setVisibility(View.VISIBLE);
            findViewById(R.id.From).setVisibility(View.VISIBLE);
            findViewById(R.id.To).setVisibility(View.VISIBLE);
            origBuildingName = null;
            destBuildingName = null;
        }
    };

    /**
     * Listener object for use on Orig Building button
     */
    private ListView.OnItemClickListener origBuildingClickListener = new ListView.OnItemClickListener() {
        /**
         * Sets origBuilding and prints message with dest building for user
         * @param adapter array adapter for the view
         * @param v view this is attatched to
         * @param pos position of item in list clicked
         * @param id id of item in list clicked
         */
        public void onItemClick(AdapterView<?> adapter, View v, int pos, long id) {
            origBuildingName = (String) origBuildingView.getItemAtPosition(pos);
            Toast.makeText(getApplicationContext(), "Path from " + origBuildingName, Toast.LENGTH_SHORT).show();
        }
    };
}
