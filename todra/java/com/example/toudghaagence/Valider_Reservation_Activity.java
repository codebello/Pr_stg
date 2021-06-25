package com.example.toudghaagence;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Valider_Reservation_Activity extends AppCompatActivity {
    Button btnContinuer;
    EditText etNom, etPrenom, etNumeroPass, etTele, etEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valider_reservation);
        btnContinuer = (Button)findViewById(R.id.btnContinuer);
        etNom = (EditText)findViewById(R.id.etNomClient);
        etPrenom = (EditText)findViewById(R.id.etPrenomClient);
        etNumeroPass = (EditText)findViewById(R.id.etNumPasse);
        etTele = (EditText)findViewById(R.id.etTelephone);
        etEmail = (EditText)findViewById(R.id.etEmail);
        btnContinuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etNom.getText().toString().equals("") ||
                        etPrenom.getText().toString().equals("") ||
                        etNumeroPass.getText().toString().equals("") ||
                        etTele.getText().toString().equals("") ||
                        etEmail.getText().toString().equals("")){
                Toast.makeText(Valider_Reservation_Activity.this , "Tous les champs sont requis",Toast.LENGTH_LONG).show();
                }else if(etTele.getText().toString().length()<11){
                Toast.makeText(Valider_Reservation_Activity.this , "Numéro de téléphone non valide",Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(Valider_Reservation_Activity.this, Coordonnees_Bancaires_Activity.class);
                    startActivity(intent);
                }
            }
        });
    }
}