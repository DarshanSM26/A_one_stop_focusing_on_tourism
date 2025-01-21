package com.example.mapfinshing;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StartingPage extends AppCompatActivity {
    int diff;
    String selectedDate="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_starting_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        AutoCompleteTextView goingto=findViewById(R.id.autoCompleteTextView2);

        AutoCompleteTextView startingfrom=findViewById(R.id.autoCompleteTextView);
        EditText date=findViewById(R.id.editTextText);
        Button btn=findViewById(R.id.button);

        String[] startingplace={"DELHI","TAMIL NAIDU","KOLATA","BANGLORE","CHANDIGARH","JAIPUR","LUCKNOW","AHMEDABAD","HYDERABAD","PUNE","ANDHRA PRADESH","KERALA"};
        ArrayAdapter adapter=new ArrayAdapter<>(StartingPage.this, android.R.layout.select_dialog_item,startingplace);
        startingfrom.setThreshold(1);
        startingfrom.setAdapter(adapter);

        String[] goingplace={"DELHI","TAMIL NAIDU","KOLATA","BANGLORE","CHANDIGARH","JAIPUR","LUCKNOW","AHMEDABAD","HYDERABAD","PUNE","ANDHRA PRADESH","KERALA"};
        ArrayAdapter adapter1=new ArrayAdapter<>(StartingPage.this, android.R.layout.select_dialog_item,goingplace);
        goingto.setThreshold(1);
        goingto.setAdapter(adapter1);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c=Calendar.getInstance();
                int pyear=c.get(Calendar.YEAR);
                int pmonth=c.get(Calendar.MONTH);
                int pdate=c.get(Calendar.DATE);
                DatePickerDialog dialog=new DatePickerDialog(StartingPage.this, android.R.style.Theme_DeviceDefault_DialogWhenLarge, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        selectedDate=i2+"/"+(i1+1)+"/"+i;
                        date.setText(selectedDate);
                        diff=pyear-i;
                    }
                },pyear,pmonth,pdate);
                dialog.show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String startingLocation = startingfrom.getText().toString();
                String destination = goingto.getText().toString();

                if (startingLocation.isEmpty() || destination.isEmpty() || selectedDate.isEmpty()) {
                    Toast.makeText(StartingPage.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Build the result string with the actual text from the AutoCompleteTextViews
                String res = "Starting: " + startingLocation +
                        "\nLeaving: " + destination +
                        "\nDate: " + selectedDate +
                        "\nYear Difference: " + diff;
                Toast.makeText(StartingPage.this, res, Toast.LENGTH_SHORT).show();
                Intent i=new Intent(StartingPage.this, RoomBooking.class);
                startActivity(i);
            }
        });

    }
}