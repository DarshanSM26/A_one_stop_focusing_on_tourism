package com.example.mapfinshing;
import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class cabing extends AppCompatActivity {
    private static final String CHANNEL_ID = "vehicle_booking_channel";
    private RadioGroup bikeRadioGroup, carRadioGroup;
    private EditText distanceEditText;
    private Button confirmBookingButton,nearbyHotelButton;
    TextView res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cabing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        Button nearbyHotelButton = findViewById(R.id.nearbyHotelButton);
        bikeRadioGroup = findViewById(R.id.bikeRadioGroup);
        carRadioGroup = findViewById(R.id.carRadioGroup);
        distanceEditText = findViewById(R.id.distanceEditText);
        confirmBookingButton = findViewById(R.id.confirmBookingButton);
        res=findViewById(R.id.textView3);

        nearbyHotelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cabing.this, MainActivity.class);
                startActivity(intent);
            }
        });

        confirmBookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processBooking();
            }
        });
    }

        private void processBooking() {
            int selectedBikeId = bikeRadioGroup.getCheckedRadioButtonId();
            int selectedCarId = carRadioGroup.getCheckedRadioButtonId();

            String selectedVehicle = "";
            double pricePerKm = 0.0;

            // Determine which vehicle is selected
            if (selectedBikeId == R.id.rx100RadioButton) {
                selectedVehicle = "RX100";
                pricePerKm = 2.0; // Example price per km
            } else if (selectedBikeId == R.id.activaRadioButton) {
                selectedVehicle = "Activa";
                pricePerKm = 1.5; // Example price per km
            } else if (selectedBikeId == R.id.splendorRadioButton) {
                selectedVehicle = "Splendor";
                pricePerKm = 1.8; // Example price per km
            } else if (selectedCarId == R.id.sedanRadioButton) {
                selectedVehicle = "Sedan";
                pricePerKm = 5.0; // Example price per km
            } else if (selectedCarId == R.id.suvRadioButton) {
                selectedVehicle = "SUV";
                pricePerKm = 7.0; // Example price per km
            } else if (selectedCarId == R.id.hatchbackRadioButton) {
                selectedVehicle = "Hatchback";
                pricePerKm = 4.5; // Example price per km
            }

            // Check if a vehicle is selected
            if (!selectedVehicle.isEmpty()) {
                String distanceInput = distanceEditText.getText().toString();
                if (!distanceInput.isEmpty()) {
                    try {
                        double distance = Double.parseDouble(distanceInput);
                        double totalCost = distance * pricePerKm;

                        // Prepare booking details
                        String message = "Vehicle: " + selectedVehicle + "\nPrice per km: ₹" + pricePerKm +
                                "\nDistance: " + distance + " km\nTotal Cost: ₹" + totalCost;

                        // Update the TextView
                        res.setText(message);

                        // Display Toast
                        Toast.makeText(cabing.this, message, Toast.LENGTH_LONG).show();

                        // Show notification
                        showNotification(selectedVehicle, totalCost, distance, pricePerKm);

                    } catch (NumberFormatException e) {
                        Toast.makeText(cabing.this, "Please enter a valid distance.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(cabing.this, "Please enter the distance.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(cabing.this, "Please select a vehicle.", Toast.LENGTH_SHORT).show();
            }
        }

       /* private void showNotification(String vehicle, double totalCost, double distance, double pricePerKm) {
            String channelId = "booking_channel";
            String channelName = "Booking Notifications";

            // Create notification channel for Android O and above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription("Notification for booking details");
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
            }

            // Build the notification content
            String notificationContent = "Vehicle: " + vehicle + "\nDistance: " + distance + " km\nTotal Cost: ₹" + totalCost;

            Intent intent = new Intent(this, cabing.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your app's icon
                    .setContentTitle("Booking Confirmed")
                    .setContentText("Click for details")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationContent))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, builder.build());
        }*/
       private void showNotification(String vehicle, double totalCost, double distance, double pricePerKm) {
           String channelId = "booking_channel";
           String channelName = "Booking Notifications";

           // Define the vibration pattern (e.g., 0ms delay, 500ms vibrate, 500ms pause)
           long[] vibrationPattern = {0, 500, 500, 500};

           // Create notification channel for Android O and above
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
               NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
               channel.setDescription("Notification for booking details");
               channel.enableVibration(true);
               channel.setVibrationPattern(vibrationPattern);
               channel.setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI, Notification.AUDIO_ATTRIBUTES_DEFAULT);

               NotificationManager manager = getSystemService(NotificationManager.class);
               manager.createNotificationChannel(channel);
           }

           // Build the notification content
           String notificationContent = "Vehicle: " + vehicle + "\nDistance: " + distance + " km\nTotal Cost: ₹" + totalCost;

           Intent intent = new Intent(this, cabing.class);
           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

           PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

           NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                   .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your app's icon
                   .setContentTitle("Booking Confirmed")
                   .setContentText("Click for details")
                   .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationContent))
                   .setPriority(NotificationCompat.PRIORITY_HIGH) // High priority for important notifications
                   .setVibrate(vibrationPattern) // Enable vibration
                   .setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI) // Set sound to default notification sound
                   .setContentIntent(pendingIntent)
                   .setAutoCancel(true);

           NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
           notificationManager.notify(1, builder.build());
       }



}
