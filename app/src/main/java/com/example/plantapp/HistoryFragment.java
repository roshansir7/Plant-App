package com.example.plantapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class HistoryFragment extends Fragment {

    private LinearLayout historyContainer;

    private final String[] plantNames = {
            "Baby Rubber Plant", "Cactus Plant", "Jade Plant", "Lace Aloe Plant", "Snake Plant",
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        historyContainer = view.findViewById(R.id.historyContainer);

        populateHistory();

        return view;
    }

    private void populateHistory() {
        historyContainer.removeAllViews();
        Random random = new Random();

        for (String plantName: plantNames) {
            // Create a card for each plant
            CardView cardView = new CardView(requireContext());
            cardView.setRadius(20);
            cardView.setCardElevation(6);
            cardView.setUseCompatPadding(true);
            cardView.setCardBackgroundColor(Color.parseColor("#F9F9F9"));

            LinearLayout cardLayout = new LinearLayout(requireContext());
            cardLayout.setOrientation(LinearLayout.VERTICAL);
            cardLayout.setPadding(20, 20, 20, 20);

            // Title
            TextView title = new TextView(requireContext());
            title.setText("🌿 " + plantName);
            title.setTextSize(18);
            title.setTextColor(Color.parseColor("#2E7D32"));
            title.setPadding(0, 0, 0, 10);
            cardLayout.addView(title);

            // Table layout for history readings
            TableLayout table = new TableLayout(requireContext());
            table.setStretchAllColumns(true);

            // Header row
            TableRow headerRow = new TableRow(requireContext());
            headerRow.addView(createTableHeader("Time"));
            headerRow.addView(createTableHeader("Moisture"));
            headerRow.addView(createTableHeader("Temp"));
            headerRow.addView(createTableHeader("Humidity"));
            table.addView(headerRow);

            // Generate 5 fake readings
            for (int i = 0; i < 5; i++) {
                TableRow row = new TableRow(requireContext());

                String time = new SimpleDateFormat("hh:mm a", Locale.getDefault())
                        .format(new Date(System.currentTimeMillis() - (i * 600000))); // every 10 min

                int moisture = random.nextInt(101);
                int temp = 15 + random.nextInt(16);
                int humidity = 30 + random.nextInt(41);

                row.addView(createTableCell(time));
                row.addView(createTableCell(moisture + "%"));
                row.addView(createTableCell(temp + "°C"));
                row.addView(createTableCell(humidity + "%"));

                table.addView(row);
            }

            cardLayout.addView(table);
            cardView.addView(cardLayout);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 24);
            historyContainer.addView(cardView, params);
        }
    }

    private TextView createTableHeader(String text) {
        TextView header = new TextView(requireContext());
        header.setText(text);
        header.setTextSize(14);
        header.setTextColor(Color.BLACK);
        header.setPadding(8, 8, 8, 8);
        header.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        header.setBackgroundColor(Color.parseColor("#E0E0E0"));
        return header;
    }

    private TextView createTableCell(String text) {
        TextView cell = new TextView(requireContext());
        cell.setText(text);
        cell.setTextSize(14);
        cell.setTextColor(Color.DKGRAY);
        cell.setPadding(8, 8, 8, 8);
        cell.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        return cell;
    }
}
