package com.example.techfix.network;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.maplibre.geojson.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RouteApiClient {

    public interface RouteCallback {
        void onSuccess(List<Point> routePoints);
        void onError(String errorMessage);
    }

    private final OkHttpClient client;

    public RouteApiClient() {
        client = new OkHttpClient();
    }

    public void fetchRoute(double startLat, double startLon, double endLat, double endLon, RouteCallback callback) {
        // OSRM coordinates format: longitude,latitude
        String url = String.format("http://router.project-osrm.org/route/v1/driving/%f,%f;%f,%f?overview=full&geometries=geojson",
                startLon, startLat, endLon, endLat);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                postError(callback, "Failed to connect to routing server: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    postError(callback, "Routing server returned error: " + response.code());
                    return;
                }

                try {
                    String responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);

                    String code = jsonObject.optString("code");
                    if (!"Ok".equals(code)) {
                        postError(callback, "No route found. Server response: " + code);
                        return;
                    }

                    JSONArray routes = jsonObject.optJSONArray("routes");
                    if (routes == null || routes.length() == 0) {
                        postError(callback, "No route options available.");
                        return;
                    }

                    JSONObject firstRoute = routes.getJSONObject(0);
                    JSONObject geometry = firstRoute.getJSONObject("geometry");
                    JSONArray coordinates = geometry.getJSONArray("coordinates");

                    List<Point> routePoints = new ArrayList<>();
                    for (int i = 0; i < coordinates.length(); i++) {
                        JSONArray coord = coordinates.getJSONArray(i);
                        // GeoJSON is [longitude, latitude]
                        double lon = coord.getDouble(0);
                        double lat = coord.getDouble(1);
                        routePoints.add(Point.fromLngLat(lon, lat));
                    }

                    postSuccess(callback, routePoints);

                } catch (JSONException e) {
                    postError(callback, "Error parsing routing response.");
                }
            }
        });
    }

    private void postError(RouteCallback callback, String message) {
        new Handler(Looper.getMainLooper()).post(() -> callback.onError(message));
    }

    private void postSuccess(RouteCallback callback, List<Point> routePoints) {
        new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(routePoints));
    }
}
