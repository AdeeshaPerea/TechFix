package com.example.techfix;

import android.app.Application;
import org.maplibre.android.MapLibre;

public class TechFixApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Globally initialize MapLibre
        MapLibre.getInstance(this);
    }
}
