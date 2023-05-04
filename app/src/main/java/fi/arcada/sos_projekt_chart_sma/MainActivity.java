package fi.arcada.sos_projekt_chart_sma;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String currency, datefrom, dateto;

    double[] temp  = {-4.7, -4.8, -1.8, 0.7, 0.1, -6, -7.8, -7, -3.8, -10.6, -10.3, -0.3, 4.8, 2.6, 0.1, 1.2, -1.5, -2.7, 1.8, 0.2, -2, -5.5, -1.3, 2.1, -0.6, -0.9, 1, -0.5, -1.4, -1.6, -5.3, -7.7, -8.2, -9.5, -3.9, -0.4, 1, 0.8, -0.4, 0.6, 1, -1.5, -0.5, 1.4, 1.5, 1.8, 2, 1.1, -0.1, 0.1, -0.7, -0.4, -3, -6.8, 2, 1.5, -1.3, -0.2, 1.6, 1.9, 1.3, 0.6, -2, -2.4, 0.8, -0.3, -2.5, -2.6, -0.7, 1.8, 1.3, 0.9, 3, 0.7, 0.8, 1.6, 2.5, 2, 6.2};
    String[] dates = {"1.1", "2.1", "3.1", "4.1", "5.1", "6.1", "7.1", "8.1", "9.1", "10.1", "11.1", "12.1", "13.1", "14.1", "15.1", "16.1", "17.1", "18.1", "19.1", "20.1", "21.1", "22.1", "23.1", "24.1", "25.1", "26.1", "27.1", "28.1", "29.1", "30.1", "31.1", "1.2", "2.2", "3.2", "4.2", "5.2", "6.2", "7.2", "8.2", "9.2", "10.2", "11.2", "12.2", "13.2", "14.2", "15.2", "16.2", "17.2", "18.2", "19.2", "20.2", "21.2", "22.2", "23.2", "24.2", "25.2", "26.2", "27.2", "28.2", "1.3", "2.3", "3.3", "4.3", "5.3", "6.3", "7.3", "8.3", "9.3", "10.3", "11.3", "12.3", "13.3", "14.3", "15.3", "16.3", "17.3", "18.3", "19.3", "20.3"};
    LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        chart = (LineChart) findViewById(R.id.chart);

        // TEMPORÄRA VÄRDEN
        currency = "USD";
        datefrom = "2022-01-01";
        dateto = "2022-02-01";

        // Hämta växelkurser från API
        ArrayList<Double> currencyValues = getCurrencyValues(currency, datefrom, dateto);
        // Skriv ut dem i konsolen
        System.out.println(currencyValues.toString());
        System.out.println("the test values " + Arrays.toString(Statistics.movingAvg(temp, 3)));

        simpleChart(temp);


        Toast.makeText(this, "U gay kek", Toast.LENGTH_LONG).show();

    }


    // Färdig metod som hämtar växelkursdata
    public ArrayList<Double> getCurrencyValues(String currency, String from, String to) {

        CurrencyApi api = new CurrencyApi();
        ArrayList<Double> currencyData = null;

        String urlString = String.format("https://api.exchangerate.host/timeseries?start_date=%s&end_date=%s&symbols=%s",
                from.trim(),
                to.trim(),
                currency.trim());

        try {
            String jsonData = api.execute(urlString).get();

            if (jsonData != null) {
                currencyData = api.getCurrencyData(jsonData, currency.trim());
                Toast.makeText(getApplicationContext(), String.format("Hämtade %s valutakursvärden från servern", currencyData.size()), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Kunde inte hämta växelkursdata från servern: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return currencyData;
    }



    public void simpleChart(double[] temp){

        List<Entry>entries = new ArrayList<>();

        for (int i = 0; i < temp.length; i++) {
            entries.add(new Entry(i, (float) temp[i])); // casting the double to float
        }

        LineDataSet dataSet = new LineDataSet(entries, "Tempratures"); // add entries to dataset
        dataSet.setColor(Color.RED);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh


    }





}

