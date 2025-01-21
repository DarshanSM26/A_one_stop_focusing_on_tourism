package com.example.mapfinshing;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class USERpage extends AppCompatActivity {
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_userpage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EditText et = findViewById(R.id.username);
        EditText et2 = findViewById(R.id.password);
        EditText et3 = findViewById(R.id.Confirmpassword);
        Button btn1 = findViewById(R.id.signup);
        Button btn2 = findViewById(R.id.signin);
        db = new DBHelper(this);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String User = et.getText().toString();
                String pass = et2.getText().toString();
                String conpass = et3.getText().toString();

                if (TextUtils.isEmpty(User) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(conpass))
                    Toast.makeText(USERpage.this, "All fields Required", Toast.LENGTH_SHORT).show();
                else {
                    if (pass.equals(conpass)) {
                        Boolean checkuser = db.checkusername(User);
                        if (checkuser == false) {
                            Boolean insert = db.insertData(User, pass);
                            Toast.makeText(USERpage.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(getApplicationContext(), StartingPage.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(USERpage.this, "User already exists", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(USERpage.this, "Password are not matching", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(i);

            }
        });
    }
}