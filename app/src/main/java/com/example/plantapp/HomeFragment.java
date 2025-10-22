package com.example.plantapp;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private LinearLayout plantsContainer;
    private LinearLayout alertBanner;
    private TextView alertText, dismissAlert;
    private Button refreshButton;
    private DatabaseReference dbRef;

    private final int[] plantImages = {
            R.drawable.baby_rubber_plant,
            R.drawable.cactus_plant,
            R.drawable.jade_plant,
            R.drawable.lace_aloe_plant,
            R.drawable.snake_plant
    };

    private final String[] plantKeys = {
            "baby_rubber_plant",
            "cactus_plant",
            "jade_plant",
            "lace_aloe_plant",
            "snake_plant"
    };

    private final String[] plantNames = {
            "Baby Rubber Plant",
            "Cactus Plant",
            "Jade Plant",
            "Lace Aloe Plant",
            "Snake Plant"
    };

    private final String[] plantDescriptions = {
            "Baby Rubber Plant loves indirect light and moist soil.",
            "Cactus Plant thrives in hot, dry environments and stores water in its stems.",
            "Jade Plant brings prosperity and needs minimal watering.",
            "Lace Aloe Plant has thick leaves and tolerates dry conditions.",
            "Snake Plant purifies air and survives even in low light."
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        plantsContainer = view.findViewById(R.id.plantsContainer);
        alertBanner = view.findViewById(R.id.alertBanner);
        alertText = view.findViewById(R.id.alertText);
        dismissAlert = view.findViewById(R.id.dismissAlert);
        refreshButton = view.findViewById(R.id.refreshButton);

        dbRef = FirebaseDatabase.getInstance().getReference("plants");

        dismissAlert.setOnClickListener(v -> alertBanner.setVisibility(View.GONE));
        refreshButton.setOnClickListener(v -> loadPlantData(inflater));

        // load data from Firebase
        loadPlantData(inflater);

        return view;
    }

    private void loadPlantData(LayoutInflater inflater) {
        plantsContainer.removeAllViews();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> lowMoisturePlants = new ArrayList<>();

                for (int i = 0; i < plantKeys.length; i++) {
                    String key = plantKeys[i];
                    DataSnapshot plantSnap = snapshot.child(key);

                    if (!plantSnap.exists()) continue;

                    Float temp = plantSnap.child("temperature").getValue(Float.class);
                    Float humidity = plantSnap.child("humidity").getValue(Float.class);
                    Float moisture = plantSnap.child("moisture").getValue(Float.class);

                    if (temp == null || humidity == null || moisture == null) continue;

                    View card = inflater.inflate(R.layout.plant_card, plantsContainer, false);

                    ImageView plantImage = card.findViewById(R.id.plantImage);
                    TextView plantName = card.findViewById(R.id.plantName);
                    TextView plantDescription = card.findViewById(R.id.plantDescription);
                    TextView temperatureText = card.findViewById(R.id.temperatureText);
                    TextView humidityText = card.findViewById(R.id.humidityText);
                    TextView moistureText = card.findViewById(R.id.moistureText);
                    TextView statusText = card.findViewById(R.id.statusText);

                    plantImage.setImageResource(plantImages[i]);
                    plantName.setText(plantNames[i]);
                    plantDescription.setText(plantDescriptions[i]);
                    temperatureText.setText("🌡️ Temperature: " + temp + "°C");
                    humidityText.setText("💧 Humidity: " + humidity + "%");
                    moistureText.setText("🌱 Moisture: " + moisture + "%");

                    if (moisture < 40) {
                        statusText.setText("Needs Water 💧");
                        statusText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                        lowMoisturePlants.add(plantNames[i]);
                    } else {
                        statusText.setText("Healthy 🌿");
                        statusText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    }

                    plantsContainer.addView(card);
                }

                if (!lowMoisturePlants.isEmpty()) {
                    showAlertBanner(lowMoisturePlants);
                    sendNotifications(lowMoisturePlants);
                } else {
                    alertBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void showAlertBanner(List<String> lowPlants) {
        alertBanner.setVisibility(View.VISIBLE);
        String message = lowPlants.size() == 1
                ? lowPlants.get(0) + " needs water 💧"
                : lowPlants.size() + " plants need water 💧";
        alertText.setText(message);
    }

    private void sendNotifications(List<String> lowPlants) {
        if (getContext() == null) return;
        NotificationManager nm = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);

        for (String plant : lowPlants) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), "plant_alerts")
                    .setSmallIcon(R.drawable.ic_water_drop)
                    .setColor(Color.parseColor("#2E7D32"))
                    .setContentTitle(plant + " needs water 💧")
                    .setContentText("Check moisture level for " + plant)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            nm.notify(plant.hashCode(), builder.build());
        }
    }
}
