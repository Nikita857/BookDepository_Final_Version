package ru.rsue.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText user_field;
    private Button main_btn;
    private TextView result_info;
    private TextView result_info_wind;
    private TextView result_info_humidity;
    private TextView result_info_feels_like;
    private TextView result_info_name;
    private ImageView img;
    private View activity_main;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_field = findViewById(R.id.user_field);
        main_btn = findViewById(R.id.main_btn);
        result_info = findViewById(R.id.result_info);
        result_info_wind = findViewById(R.id.result_info2);
        result_info_humidity = findViewById(R.id.result_info3);
        result_info_feels_like = findViewById(R.id.result_info4);
        result_info_name = findViewById(R.id.result_info5);
        activity_main = findViewById(R.id.main);

        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_field.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, R.string.no_user_input, Toast.LENGTH_LONG);
                } else {
                    String city = user_field.getText().toString();
                    String key = "2eefac7a87a15f603641345518287195";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+key+"&units=metric&lang=ru";

                    new GetUrlData().execute(url);
                }
            }
        });

    }
    private class GetUrlData extends AsyncTask<String, String, String> {

        protected void onPreExecute(){
            super.onPreExecute();
            result_info.setText("Ожидайте...");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while((line = reader.readLine())!=null){
                    buffer.append(line).append("\n");
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (connection != null)
                    connection.disconnect();
                    try {
                        if(reader != null)
                        reader.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

                try {
                    JSONObject obj = new JSONObject(result);
                    int temp = (int) obj.getJSONObject("main").getDouble("temp");
                    int humidity = (int) obj.getJSONObject("main").getDouble("humidity");
                    if(temp>15 && humidity<80){
                    activity_main.setBackground(getResources().getDrawable(R.drawable.vesna));
                    } else if (temp<=15 && humidity>80) {
                        activity_main.setBackground(getResources().getDrawable(R.drawable.as));
                    } else if (humidity>100) {
                        activity_main.setBackground(getResources().getDrawable(R.drawable.rain));
                    }
                    result_info_name.setText("Выбранный город "+ obj.getString("name"));
                    result_info.setText("Температура "+ obj.getJSONObject("main").getDouble("temp")+" гр.Ц.");
                    result_info_wind.setText("Скорость ветра "+ obj.getJSONObject("wind").getDouble("speed")+" м.с");
                    result_info_humidity.setText("Влажность "+ obj.getJSONObject("main").getDouble("humidity")+" %");
                    result_info_feels_like.setText("Ощущается как "+ obj.getJSONObject("main").getDouble("feels_like")+" гр.Ц.");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }
    }
