package com.example.mapfinshing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RoomBooking extends AppCompatActivity {
    private final int SINGLE_ROOM_PRICE = 1000;
    private final int DOUBLE_ROOM_PRICE = 1500;
    private final int THREE_BHK_PRICE = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room_booking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        RadioButton singleRoomRadioButton = findViewById(R.id.singleRoomRadioButton);
        RadioButton doubleRoomRadioButton = findViewById(R.id.doubleRoomRadioButton);
        RadioButton threeBHKRadioButton = findViewById(R.id.threeBHKRadioButton);
        EditText nightsEditText = findViewById(R.id.nightsEditText);
        Button confirmRoomBookingButton = findViewById(R.id.confirmRoomBookingButton);

        confirmRoomBookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nightsStr = nightsEditText.getText().toString();

                if (nightsStr.isEmpty()) {
                    Toast.makeText(RoomBooking.this, "Please enter the number of nights", Toast.LENGTH_SHORT).show();
                    return;
                }

                int nights = Integer.parseInt(nightsStr);  // Convert the nights input to an integer
                int totalPrice = 0;
                String roomType = "";

                // Calculate the total price based on selected room type
                if (singleRoomRadioButton.isChecked()) {
                    totalPrice = SINGLE_ROOM_PRICE * nights;
                    roomType = "Single Room";
                } else if (doubleRoomRadioButton.isChecked()) {
                    totalPrice = DOUBLE_ROOM_PRICE * nights;
                    roomType = "Double Room";
                } else if (threeBHKRadioButton.isChecked()) {
                    totalPrice = THREE_BHK_PRICE * nights;
                    roomType = "3BHK";
                } else {
                    Toast.makeText(RoomBooking.this, "Please select a room type", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Show the calculated total price
                Toast.makeText(RoomBooking.this, roomType + " Booked! Total Price for " + nights + " nights: " + totalPrice + " units", Toast.LENGTH_LONG).show();
                Intent i=new Intent(RoomBooking.this, cabing.class);
                startActivity(i);
            }
        });

    }
}