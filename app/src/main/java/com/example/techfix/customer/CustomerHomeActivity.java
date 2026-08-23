package com.example.techfix.customer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.techfix.R;
import com.example.techfix.models.*;
import com.example.techfix.network.*;
import com.example.techfix.utils.*;

import org.maplibre.android.MapLibre;
import org.maplibre.android.maps.MapView;
import org.maplibre.android.maps.MapLibreMap;
import org.maplibre.android.maps.Style;
import org.maplibre.android.location.LocationComponent;
import org.maplibre.android.location.LocationComponentActivationOptions;
import org.maplibre.android.location.modes.CameraMode;
import org.maplibre.android.location.modes.RenderMode;
import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.geometry.LatLngBounds;
import org.maplibre.geojson.Feature;
import org.maplibre.geojson.FeatureCollection;
import org.maplibre.geojson.LineString;
import org.maplibre.geojson.Point;
import org.maplibre.android.style.layers.LineLayer;
import org.maplibre.android.style.layers.PropertyFactory;
import org.maplibre.android.style.layers.SymbolLayer;
import org.maplibre.android.style.sources.GeoJsonSource;
import org.maplibre.android.camera.CameraUpdateFactory;

import android.location.Location;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import java.util.List;
import java.util.ArrayList;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class CustomerHomeActivity extends AppCompatActivity {

    private ImageButton btnBack, btnNotification;
    private TextView tvGreeting, tvUserName;
    private Button btnNewRepairRequest;
    private View cardTrackRepair, cardMyBookings, cardHistory;
    private View cardRepair1, cardRepair2, tvSeeAllRepairs;
    private MapView mapNearestBranch;
    private View navHome, navServices, navBookings, navHistory, navProfile;

    private DatabaseHelper databaseHelper;
    private String userEmail, userName;
    private MapLibreMap mapLibreMap;
    private boolean routeFetched = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        
        setContentView(R.layout.activity_customer_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cus_home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseHelper = new DatabaseHelper(this);

        // Bind Views
        btnBack = findViewById(R.id.btnBack);
        btnNotification = findViewById(R.id.btnNotification);
        tvGreeting = findViewById(R.id.tvGreeting);
        tvUserName = findViewById(R.id.tvUserName);
        btnNewRepairRequest = findViewById(R.id.btnNewRepairRequest);
        cardTrackRepair = findViewById(R.id.cardTrackRepair);
        cardMyBookings = findViewById(R.id.cardMyBookings);
        cardHistory = findViewById(R.id.cardHistory);
        cardRepair1 = findViewById(R.id.cardRepair1);
        cardRepair2 = findViewById(R.id.cardRepair2);
        tvSeeAllRepairs = findViewById(R.id.tvSeeAllRepairs);
        mapNearestBranch = findViewById(R.id.mapNearestBranch);
        if (mapNearestBranch != null) {
            mapNearestBranch.onCreate(savedInstanceState);
            mapNearestBranch.getMapAsync(map -> {
                this.mapLibreMap = map;
                map.setStyle(new Style.Builder().fromUri("https://demotiles.maplibre.org/style.json"), style -> {
                    enableLocationComponent(style);
                });
            });
        }

        navHome = findViewById(R.id.navHome);
        navServices = findViewById(R.id.navServices);
        navBookings = findViewById(R.id.navBookings);
        navHistory = findViewById(R.id.navHistory);
        navProfile = findViewById(R.id.navProfile);

        // Get passed user details
        Intent intent = getIntent();
        if (intent != null) {
            userEmail = intent.getStringExtra("USER_EMAIL");
            userName = intent.getStringExtra("USER_NAME");
        }

        // SessionManager fallback
        if (TextUtils.isEmpty(userEmail)) {
            userEmail = com.example.techfix.utils.SessionManager.getUserEmail(this);
        }
        if (TextUtils.isEmpty(userName)) {
            userName = com.example.techfix.utils.SessionManager.getUserName(this);
        }

        // If userName is empty but userEmail exists, query SQLite database
        if (TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userEmail)) {
            userName = databaseHelper.getUserFullName(userEmail);
        }

        // Display user's name
        if (!TextUtils.isEmpty(userName)) {
            tvUserName.setText(userName.toUpperCase());
        } else {
            tvUserName.setText("KASUN PERERA");
        }

        // Dynamic time-of-day greeting
        tvGreeting.setText(getTimeOfDayGreeting());

        // Click Listeners
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnNotification != null) {
            btnNotification.setOnClickListener(v ->
                    Toast.makeText(CustomerHomeActivity.this, "No new notifications", Toast.LENGTH_SHORT).show()
            );
        }

        if (btnNewRepairRequest != null) {
            btnNewRepairRequest.setOnClickListener(v -> {
                Intent requestIntent = new Intent(CustomerHomeActivity.this, NewRepairRequestActivity.class);
                startActivity(requestIntent);
            });
        }

        if (cardTrackRepair != null) {
            cardTrackRepair.setOnClickListener(v ->
                    Toast.makeText(CustomerHomeActivity.this, "Track Repair: iPhone 13 (#TF-2201)", Toast.LENGTH_SHORT).show()
            );
        }

        if (cardMyBookings != null) {
            cardMyBookings.setOnClickListener(v ->
                    Toast.makeText(CustomerHomeActivity.this, "Opening My Bookings", Toast.LENGTH_SHORT).show()
            );
        }

        if (cardHistory != null) {
            cardHistory.setOnClickListener(v ->
                    Toast.makeText(CustomerHomeActivity.this, "Opening Repair History", Toast.LENGTH_SHORT).show()
            );
        }

        if (tvSeeAllRepairs != null) {
            tvSeeAllRepairs.setOnClickListener(v ->
                    Toast.makeText(CustomerHomeActivity.this, "All Ongoing Repairs", Toast.LENGTH_SHORT).show()
            );
        }

        // Bottom navigation listeners
        setupBottomNav();
    }

    private String getTimeOfDayGreeting() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour >= 4 && hour < 12) {
            return "GOOD MORNING";
        } else if (hour >= 12 && hour < 17) {
            return "GOOD AFTERNOON";
        } else {
            return "GOOD EVENING";
        }
    }

    private void setupBottomNav() {
        if (navHome != null) {
            navHome.setOnClickListener(v ->
                    Toast.makeText(CustomerHomeActivity.this, "Home", Toast.LENGTH_SHORT).show()
            );
        }
        if (navServices != null) {
            navServices.setOnClickListener(v ->
                    Toast.makeText(CustomerHomeActivity.this, "Services", Toast.LENGTH_SHORT).show()
            );
        }
        if (navBookings != null) {
            navBookings.setOnClickListener(v ->
                    Toast.makeText(CustomerHomeActivity.this, "Bookings", Toast.LENGTH_SHORT).show()
            );
        }
        if (navHistory != null) {
            navHistory.setOnClickListener(v ->
                    Toast.makeText(CustomerHomeActivity.this, "History", Toast.LENGTH_SHORT).show()
            );
        }
        if (navProfile != null) {
            navProfile.setOnClickListener(v -> {
                Intent profileIntent = new Intent(CustomerHomeActivity.this, ProfileActivity.class);
                profileIntent.putExtra("USER_EMAIL", userEmail);
                profileIntent.putExtra("USER_NAME", userName);
                startActivity(profileIntent);
            });
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationComponent locationComponent = mapLibreMap.getLocationComponent();
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING_GPS);
            locationComponent.setRenderMode(RenderMode.COMPASS);
            
            // Try fetching route periodically until we have a location
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (routeFetched || mapLibreMap == null) return;
                    Location loc = locationComponent.getLastKnownLocation();
                    if (loc != null) {
                        routeFetched = true;
                        fetchAndDrawRoute(loc.getLatitude(), loc.getLongitude());
                    } else {
                        new Handler(Looper.getMainLooper()).postDelayed(this, 1000);
                    }
                }
            });
            
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    
    private void fetchAndDrawRoute(double userLat, double userLon) {
        NearestNodeCalculator.Node nearestNode = NearestNodeCalculator.getNearestNode(userLat, userLon);
        Toast.makeText(this, "Nearest Branch: " + nearestNode.name, Toast.LENGTH_SHORT).show();
        
        RouteApiClient routeApiClient = new RouteApiClient();
        routeApiClient.fetchRoute(userLat, userLon, nearestNode.lat, nearestNode.lon, new RouteApiClient.RouteCallback() {
            @Override
            public void onSuccess(List<Point> routePoints) {
                if (mapLibreMap != null && mapLibreMap.getStyle() != null) {
                    Style style = mapLibreMap.getStyle();
                    
                    // Add route line
                    LineString lineString = LineString.fromLngLats(routePoints);
                    GeoJsonSource routeSource = new GeoJsonSource("route-source", Feature.fromGeometry(lineString));
                    style.addSource(routeSource);
                    
                    LineLayer routeLayer = new LineLayer("route-layer", "route-source");
                    routeLayer.setProperties(
                            PropertyFactory.lineColor(Color.parseColor("#FF5722")),
                            PropertyFactory.lineWidth(5f)
                    );
                    style.addLayer(routeLayer);
                    
                    // Add Node Markers (A & B)
                    List<Feature> nodeFeatures = new ArrayList<>();
                    nodeFeatures.add(Feature.fromGeometry(Point.fromLngLat(NearestNodeCalculator.NODE_A_LON, NearestNodeCalculator.NODE_A_LAT)));
                    nodeFeatures.add(Feature.fromGeometry(Point.fromLngLat(NearestNodeCalculator.NODE_B_LON, NearestNodeCalculator.NODE_B_LAT)));
                    
                    GeoJsonSource nodesSource = new GeoJsonSource("nodes-source", FeatureCollection.fromFeatures(nodeFeatures));
                    style.addSource(nodesSource);
                    
                    // A simple circle layer for nodes since we don't have images added to style
                    org.maplibre.android.style.layers.CircleLayer nodesLayer = new org.maplibre.android.style.layers.CircleLayer("nodes-layer", "nodes-source");
                    nodesLayer.setProperties(
                            PropertyFactory.circleColor(Color.parseColor("#2196F3")),
                            PropertyFactory.circleRadius(8f)
                    );
                    style.addLayer(nodesLayer);
                    
                    // Zoom map to show entire route
                    if (routePoints.size() > 0) {
                        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
                        boundsBuilder.include(new LatLng(userLat, userLon));
                        boundsBuilder.include(new LatLng(nearestNode.lat, nearestNode.lon));
                        for(Point p : routePoints) {
                            boundsBuilder.include(new LatLng(p.latitude(), p.longitude()));
                        }
                        
                        mapLibreMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 100), 2000);
                    }
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(CustomerHomeActivity.this, "Routing failed: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mapLibreMap != null && mapLibreMap.getStyle() != null) {
                    enableLocationComponent(mapLibreMap.getStyle());
                }
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // MapLibre lifecycle methods
    @Override
    protected void onStart() {
        super.onStart();
        if (mapNearestBranch != null) mapNearestBranch.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapNearestBranch != null) mapNearestBranch.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapNearestBranch != null) mapNearestBranch.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mapNearestBranch != null) mapNearestBranch.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapNearestBranch != null) mapNearestBranch.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapNearestBranch != null) mapNearestBranch.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapNearestBranch != null) mapNearestBranch.onDestroy();
    }
}
