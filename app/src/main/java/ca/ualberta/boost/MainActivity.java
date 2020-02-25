package ca.ualberta.boost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button signUpButton = findViewById(R.id.signUpButton);
        Button loginButton = findViewById(R.id.loadButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRiderMap();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDriverMap();
            }
        });
    }

    public void gotoRiderMap(){
        Intent intent = new Intent(this, RiderMainPage.class);
        startActivity(intent);
    }

    public void gotoDriverMap(){
        Intent intent = new Intent(this, DriverMainPage.class);
        startActivity(intent);
    }
}
