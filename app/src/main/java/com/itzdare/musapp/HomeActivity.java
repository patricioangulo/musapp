package com.itzdare.musapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.itzdare.musapp.acciones.SubirInfoActivity;

import java.util.HashMap;


public class HomeActivity extends AppCompatActivity {
    int version;
    FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

    Button btnToken, btnAdoptar, btnAdopcion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        btnAdoptar = findViewById(R.id.btn_adoptar);
        btnAdoptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateToken();
                Intent p = new Intent(v.getContext(),CoreActivity.class);
                startActivity(p);


            }
        });

        btnAdopcion = findViewById(R.id.btn_user_1);
        btnAdopcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateToken();
                Intent p = new Intent(v.getContext(),SubirInfoActivity.class);
                startActivity(p);
            }
        });



    }//FIN DEL ONCREATE---

    void generateToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Token");
                        ref.child(user.getUid()).child("tokenId").setValue(token);
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item,menu);
        return true;
    }

   @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_cerrar:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                finish();
                                Toast.makeText(HomeActivity.this, "Cerrando Sesión", Toast.LENGTH_SHORT).show();
                                vamosalogin();
                            }
                        });

        }
        return super.onOptionsItemSelected(item);
    }

    private void vamosalogin() {
        Intent i = new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    //Alerta para Actualización
    private void updateAlert(String url, String versionName) {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(HomeActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        TextView tv_version_name = view.findViewById(R.id.tv_version_name);
        Button btn_update = view.findViewById(R.id.btn_update);

        tv_version_name.setText(versionName);

        mbuilder.setCancelable(false);
        mbuilder.setView(view);
        AlertDialog dialog = mbuilder.create();
        dialog.show();

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openUrl = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(openUrl);
            }
        });





    }



    @Override
    protected void onResume() {
        super.onResume();

        PackageInfo packageInfo;
        try {
            packageInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionCode;

        }catch (Exception e){
            e.printStackTrace();
        }
        long cacheExpiration = 3600;
        remoteConfig.setConfigSettingsAsync(new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(3600).build());
        HashMap<String, Object> update = new HashMap<>();
        update.put("versioncode",version);
        Task<Void> fetch = remoteConfig.fetch(0);
        fetch.addOnSuccessListener(this, aVoid -> {
            remoteConfig.activate();
            version(version);
        });


    }


    private void version(int version) {

        int nueva = (int) remoteConfig.getLong("versioncode");
        String web = remoteConfig.getString("web");
        String versionname = remoteConfig.getString("versionname");

        if(nueva > version) {
            updateAlert(web, versionname);
            Toast.makeText(this, "Existe una nueva versión", Toast.LENGTH_SHORT).show();
        }
    }
    //Fin de alerta de actualización




}


