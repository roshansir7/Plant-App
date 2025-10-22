package com.example.plantapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphsFragment extends Fragment {

    private LineChart tempChart, humidityChart;
    private BarChart moistureChart;

    private final String[] plantNames = {
            "Baby Rubber Plant", "Cactus Plant", "Jade Plant", "Lace Aloe Plant", "Snake Plant"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graphs, container, false);

        tempChart = view.findViewById(R.id.tempChart);
        humidityChart = view.findViewById(R.id.humidityChart);
        moistureChart = view.findViewById(R.id.moistureChart);

        setupTemperatureChart();
        setupHumidityChart();
        setupMoistureChart();

        return view;
    }

    private void setupTemperatureChart() {
        List<Entry> entries = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < plantNames.length; i++) {
            entries.add(new Entry(i, 15 + random.nextInt(16)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Temperature (°C)");
        dataSet.setColor(Color.parseColor("#FF5722"));
        dataSet.setCircleColor(Color.parseColor("#E64A19"));
        dataSet.setLineWidth(2f);
        dataSet.setValueTextSize(10f);

        LineData lineData = new LineData(dataSet);
        tempChart.setData(lineData);

        Description desc = new Description();
        desc.setText("Plant Temperature Levels");
        tempChart.setDescription(desc);
        tempChart.getXAxis().setGranularity(1f);
        tempChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return plantNames[(int) value % plantNames.length];
            }
        });
        tempChart.animateY(1000);
        tempChart.invalidate();
    }

    private void setupHumidityChart() {
        List<Entry> entries = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < plantNames.length; i++) {
            entries.add(new Entry(i, 30 + random.nextInt(41)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Humidity (%)");
        dataSet.setColor(Color.parseColor("#03A9F4"));
        dataSet.setCircleColor(Color.parseColor("#0288D1"));
        dataSet.setLineWidth(2f);
        dataSet.setValueTextSize(10f);

        LineData lineData = new LineData(dataSet);
        humidityChart.setData(lineData);

        Description desc = new Description();
        desc.setText("Plant Humidity Levels");
        humidityChart.setDescription(desc);
        humidityChart.getXAxis().setGranularity(1f);
        humidityChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return plantNames[(int) value % plantNames.length];
            }
        });

        humidityChart.animateY(1000);
        humidityChart.invalidate();
    }

    private void setupMoistureChart() {
        List<BarEntry> entries = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < plantNames.length; i++) {
            entries.add(new BarEntry(i, random.nextInt(101)));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Moisture (%)");
        dataSet.setColor(Color.parseColor("#4CAF50"));
        dataSet.setValueTextSize(10f);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.7f);
        moistureChart.setData(barData);

        Description desc = new Description();
        desc.setText("Soil Moisture Levels");
        moistureChart.setDescription(desc);
        moistureChart.getXAxis().setGranularity(1f);
        moistureChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override        public String getFormattedValue(float value) {
                return plantNames[(int) value % plantNames.length];
            }
        });


        moistureChart.animateY(1000);
        moistureChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        moistureChart.invalidate();
    }
}
