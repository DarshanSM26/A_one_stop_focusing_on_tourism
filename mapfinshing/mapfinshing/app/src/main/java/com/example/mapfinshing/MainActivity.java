package com.example.mapfinshing;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private MyLocationNewOverlay locationOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE));

       // setContentView(R.layout.activity_main);

        // Initialize MapView
        mapView = findViewById(R.id.mapview);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(15.0);
        mapView.getController().setCenter(new GeoPoint(0.0, 0.0)); // Placeholder location

        // Request location permissions
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            initializeLocationOverlay();
        }

        // Set up search functionality
        EditText searchEditText = findViewById(R.id.search_edit_text);
        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(v -> searchForHotels(searchEditText.getText().toString()));
    }

    private void initializeLocationOverlay() {
        locationOverlay = new MyLocationNewOverlay(mapView);
        locationOverlay.enableMyLocation();
        mapView.getOverlays().add(locationOverlay);
    }

    private void searchForHotels(String query) {
        if (query == null || query.isEmpty()) return;

        new Thread(() -> {
            try {
                // Get current location first
                GeoPoint currentLocation = locationOverlay.getMyLocation();
                String urlString;

                if (currentLocation != null) {
                    // Use current location to bias search results
                    urlString = "https://nominatim.openstreetmap.org/search?format=json&q=" + query +
                            "&lat=" + currentLocation.getLatitude() +
                            "&lon=" + currentLocation.getLongitude() +
                            "&limit=10"; // Limit results
                } else {
                    // Fallback to standard search if no location
                    urlString = "https://nominatim.openstreetmap.org/search?format=json&q=" + query;
                }

                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "YourAppName/1.0"); // Recommended for Nominatim API

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                conn.disconnect();

                // Parse JSON response
                JSONArray results = new JSONArray(response.toString());
                runOnUiThread(() -> {
                    if (results.length() > 0) {
                        addMarkersToMap(results);
                    } else {
                        Toast.makeText(MainActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Search failed", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void addMarkersToMap(JSONArray results) {
        try {
            mapView.getOverlays().clear(); // Clear previous markers
            initializeLocationOverlay();  // Reinitialize the location overlay

            // Get current location to calculate distances
            GeoPoint currentLocation = locationOverlay.getMyLocation();

            for (int i = 0; i < results.length(); i++) {
                JSONObject location = results.getJSONObject(i);
                double lat = location.getDouble("lat");
                double lon = location.getDouble("lon");
                String displayName = location.getString("display_name");

                // Create a marker
                Marker marker = new Marker(mapView);
                marker.setPosition(new GeoPoint(lat, lon));
                marker.setTitle(displayName);
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

                // Calculate distance if current location is available
                if (currentLocation != null) {
                    float[] results1 = new float[1];
                    Location.distanceBetween(
                            currentLocation.getLatitude(), currentLocation.getLongitude(),
                            lat, lon, results1
                    );
                    String distanceText = String.format("%.2f km", results1[0] / 1000);
                    marker.setSnippet(distanceText); // Show distance in marker snippet
                }

                // On click, show hotel details and distance
                marker.setOnMarkerClickListener((marker1, mapView1) -> {
                    showHotelDetails(location);
                    return true;
                });

                mapView.getOverlays().add(marker);
            }

            // Zoom to the first result
            if (results.length() > 0) {
                JSONObject firstResult = results.getJSONObject(0);
                double lat = firstResult.getDouble("lat");
                double lon = firstResult.getDouble("lon");
                mapView.getController().setCenter(new GeoPoint(lat, lon));
                mapView.getController().setZoom(15.0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showHotelDetails(JSONObject location) {
        try {
            double hotelLat = location.getDouble("lat");
            double hotelLon = location.getDouble("lon");
            String hotelAddress = location.getString("display_name");

            // Get current location (from MyLocationNewOverlay)
            GeoPoint currentLocation = locationOverlay.getMyLocation();

            if (currentLocation != null) {
                // Calculate distance from current location to hotel
                float[] results = new float[1];
                Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(),
                        hotelLat, hotelLon, results);
                float distance = results[0];  // distance in meters

                // Format the distance to kilometers
                String distanceText = String.format("%.2f km", distance / 1000);

                // Define the menu for different times of day with South Indian food items and prices
                String morningMenu = "- Idli: ₹40\n- Dosa: ₹50\n- Pongal: ₹60\n- Upma: ₹45\n- Filter Coffee: ₹25";
                String afternoonMenu = "- Sambar Rice: ₹70\n- Thali (Rice, Sambar, Rasam, Poriyal, Appalam): ₹150\n" +
                        "- Puliyodarai (Tamarind Rice): ₹60\n- Curd Rice: ₹50";
                String eveningMenu = "- Vada: ₹40\n- Bonda: ₹35\n- Paniyaram: ₹50\n- Masala Dosa: ₹60\n- Kesari: ₹45";

                // Show the details in a Dialog
                new AlertDialog.Builder(this)
                        .setTitle("Hotel Details")
                        .setMessage("Address: " + hotelAddress +
                                "\nDistance: " + distanceText +
                                "\n\nMenu with Prices:" +
                                "\n\nMorning:\n" + morningMenu +
                                "\n\nAfternoon:\n" + afternoonMenu +
                                "\n\nEvening:\n" + eveningMenu)
                        .setPositiveButton("Close", null)
                        .show();
            } else {
                // Handle case where current location is unavailable
                Toast.makeText(this, "Current location not available", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeLocationOverlay();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }

    }
}