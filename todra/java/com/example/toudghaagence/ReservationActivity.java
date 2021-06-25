package com.example.toudghaagence;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ReservationActivity extends AppCompatActivity {
    Button btnAllerSimple,btnAllerRetour,btnDateDepart,btnDateRetour,btnChercher;
    RadioButton rdPietons,rdVehicule;
    Spinner spTrajet,spAdultes,spEnfants,spVehicule;
    List<HashMap<String,Object>> trajets = new ArrayList<>();
    public DatePickerDialog datePickerDialog;
    date dt = new date();
    Boolean dateDepartEstValide = false;
    Boolean dateRetourEstValide = false;

    Calendar cal = Calendar.getInstance();
    int annee = cal.get(Calendar.YEAR);
    int mois = cal.get(Calendar.MONTH);
    int jour = cal.get(Calendar.DAY_OF_MONTH);

    Boolean AllerSimple = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        btnAllerSimple = (Button)findViewById(R.id.btnAllerSimple);
        btnAllerRetour = (Button)findViewById(R.id.btnAllerRetour);
        btnDateDepart = (Button)findViewById(R.id.btnDateDepart);
        btnDateRetour = (Button)findViewById(R.id.btnDateRetour);
        btnChercher = (Button)findViewById(R.id.btnChercher);

        btnDateDepart.setText(stringDate(jour,mois+1,annee));
        btnDateRetour.setText(stringDate(jour,mois+1,annee));




        rdPietons = (RadioButton)findViewById(R.id.rdPietons);
        rdVehicule = (RadioButton)findViewById(R.id.rdVehicule);

        spTrajet = (Spinner)findViewById(R.id.spTrajet);
        spAdultes = (Spinner)findViewById(R.id.spAdultes);
        spEnfants = (Spinner)findViewById(R.id.spEnfants);
        spVehicule = (Spinner)findViewById(R.id.spVehicule);

        RemplirDonnees(LireDepuisAsset("trajets.json"));

        SimpleAdapter adapter = new SimpleAdapter(ReservationActivity.this,
                trajets,
                R.layout.layout_spinner_trajets,
                new String[] {"Nom"},
                new int[] {R.id.tvNomTrajet});
        spTrajet.setAdapter(adapter);
        spTrajet.setSelection(0);


        btnAllerSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDateRetour.setEnabled(false);
                btnDateRetour.setText("--/--/--");
                AllerSimple=true;
            }
        });
        btnAllerRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDateRetour.setEnabled(true);
                btnDateRetour.setText(stringDate(jour,mois+1,annee));
                AllerSimple=false;

            }
        });
        rdVehicule.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(rdVehicule.isChecked()){
                    spVehicule.setVisibility(View.VISIBLE);
                }else if(rdPietons.isChecked()){
                    spVehicule.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnChercher.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
              AlertDialog.Builder b = new AlertDialog.Builder(ReservationActivity.this);
              b.setTitle("Informations");
              StringBuilder str = new StringBuilder() ;
                str.append("Type de réservation : ");
                if(AllerSimple) {
                    str.append(btnAllerSimple.getText().toString() + "\n");
                }else{
                    str.append(btnAllerRetour.getText().toString() + "\n");
                }
                str.append("Trajet : ");
                str.append(trajets.get(spTrajet.getSelectedItemPosition()).get("Nom").toString()+"\n");
                str.append("Date de départ : ");
                str.append(btnDateDepart.getText().toString()+"\n");
                str.append("Date de retour : ");
                str.append(btnDateRetour.getText().toString()+"\n");
                str.append("Kilométrage : ");
                str.append(trajets.get(spTrajet.getSelectedItemPosition()).get("Distance").toString()+"Km \n");
                str.append("Durée : ");
                str.append(trajets.get(spTrajet.getSelectedItemPosition()).get("Duree").toString()+"H \n");
                float montant=0;
                montant =(Integer.parseInt(trajets.get(spTrajet.getSelectedItemPosition()).get("Distance").toString()))/10;
                montant =montant+ (80*(Integer.parseInt(spAdultes.getSelectedItem().toString())));
                montant = montant +(40*(Integer.parseInt(spEnfants.getSelectedItem().toString())));
                float VehiculeFrais = 0;
                if(spVehicule.getVisibility()==View.VISIBLE){
                    switch (spVehicule.getSelectedItem().toString()){
                        case "Voiture" :
                            VehiculeFrais = 100;
                            break;
                        case "Moto" :
                            VehiculeFrais = 50;
                            break;
                        case "Fourgonnette" :
                            VehiculeFrais = 150;
                            break;
                        case "Camion" :
                            VehiculeFrais = 200;
                            break;

                    }
                }
                if(!AllerSimple){
                    montant=montant*2;
                }
                str.append("Montant pour passagers : ");
                str.append(montant+"Euro \n");
                str.append("Frais de véhicule : ");
                str.append(VehiculeFrais+"Euro \n");
                str.append("Total : ");
                str.append(montant+VehiculeFrais+"Euro \n");


              b.setMessage(str);
              b.setPositiveButton("Réserver", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                      Intent intent = new Intent(ReservationActivity.this ,Valider_Reservation_Activity.class);
                      startActivity(intent);
                  }
              });

              b.show();




            }
        });
    }
    public void RemplirDonnees(String json) {
        try {
            JSONObject fichierJson = new JSONObject(json);
            JSONArray trajetsJson = fichierJson.getJSONArray("trajets");
            for(int i = 0; i < trajetsJson.length(); i++) {
                JSONObject trajet = trajetsJson.getJSONObject(i);
                HashMap<String, Object> trajetHash = new HashMap<>();
                trajetHash.put("Nom", trajet.getString("Nom"));
                trajetHash.put("Distance", trajet.getInt("Distance"));
                trajetHash.put("Duree", trajet.getInt("Duree"));
                trajets.add(trajetHash);
            }

        } catch (JSONException e) {

        }
    }
    public String LireDepuisAsset(String nomFichierJson) {
        String json = "";
        try {
            InputStream is = getAssets().open(nomFichierJson);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {

        }
        return json;
    }
    private void initialiserDatePicker(View v){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = stringDate(dayOfMonth,month,year);
                if(v==btnDateDepart){
                    btnDateDepart.setText(date);
                }else if(v==btnDateRetour){
                    btnDateRetour.setText(date);
                }

            }
        };
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(ReservationActivity.this,style,dateSetListener,annee,mois,jour);
        datePickerDialog.show();
    }

    private String stringDate(int dayOfMonth, int month, int year) {
        return dayOfMonth+"/"+month+"/"+year;
    }
    public void ouvrirDatePicker(View view) {
        initialiserDatePicker(view);
    }
}