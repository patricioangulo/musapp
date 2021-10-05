package com.itzdare.musapp.acciones;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.itzdare.musapp.MainActivity;
import com.itzdare.musapp.R;


public class VerMascotaActivity extends AppCompatActivity {

    TextView nombreMascota, desMascota, idMascota;
    ImageView imagen;
    Button borrarMascota, editarMascota;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_mascota);


        Bundle parametros = getIntent().getExtras();
        String nombre = parametros.getString("nombre");
        String foto = parametros.getString("imagen");
        String des = parametros.getString("des");
        String id = parametros.getString("id");


        nombreMascota = findViewById(R.id.tv_nombre_mascota);
        imagen = findViewById(R.id.img_mascota);
        desMascota = findViewById(R.id.tv_descripcion);
        //idMascota = findViewById(R.id.tv_id_mascota);

        nombreMascota.setText(nombre);
        Glide.with(this).load(foto).into(imagen);
        desMascota.setText(des);
        //idMascota.setText(id);


        //BOTÓN BORRAR MASCOTA
        borrarMascota = findViewById(R.id.btn_borrar);
        borrarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database= FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference();
                Query query = ref.child("Mascota").orderByChild("id_mascota").equalTo(id);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if(snapshot.exists()){
                           for(DataSnapshot data: snapshot.getChildren()){
                               String key = data.getKey();
                               FirebaseDatabase database= FirebaseDatabase.getInstance();
                               DatabaseReference ref = database.getReference("Mascota").child(key);
                               ref.removeValue();

                           }
                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                finish();
                Toast.makeText(VerMascotaActivity.this, "Mascota Borrada", Toast.LENGTH_SHORT).show();


            }
        });//FIN BOTÓN BORRAR MASCOTA

        //BOTÓN EDITAR MASCOTA
        editarMascota = findViewById(R.id.btn_editar);
        editarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




    }//FIN DEL ON CREATE

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
                                Toast.makeText(VerMascotaActivity.this, "Cerrando Sesión", Toast.LENGTH_SHORT).show();
                                vamosalogin();
                            }
                        });




        }
        return super.onOptionsItemSelected(item);
    }

    private void vamosalogin() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

}
