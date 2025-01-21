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

public class LoginPage extends AppCompatActivity {
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EditText et1=findViewById(R.id.username);
        EditText et2=findViewById(R.id.password);
        db=new DBHelper(this);


        Button btn2=findViewById(R.id.signin1);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String User=et1.getText().toString();
                String pass=et2.getText().toString();
                if(TextUtils.isEmpty(User) || TextUtils.isEmpty(pass))
                    Toast.makeText(LoginPage.this,"All fields Required", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkuserpass=db.checkusername(User,pass);
                    if(checkuserpass==true){
                        Toast.makeText(LoginPage.this,"Login Successful",Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(getApplicationContext(), StartingPage.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(LoginPage.this,"Login Failed",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}