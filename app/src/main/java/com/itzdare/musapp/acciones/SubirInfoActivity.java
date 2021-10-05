package com.itzdare.musapp.acciones;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.style.MaskFilterSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.model.ByteArrayLoader;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itzdare.musapp.CoreActivity;
import com.itzdare.musapp.HomeActivity;
import com.itzdare.musapp.MainActivity;
import com.itzdare.musapp.R;
import com.itzdare.musapp.fragments.UsuariosFragment;
import com.itzdare.musapp.pojos.Ciudad;
import com.itzdare.musapp.pojos.Edad;
import com.itzdare.musapp.pojos.Galeria;
import com.itzdare.musapp.pojos.Mascota;
import com.itzdare.musapp.pojos.Mes;
import com.itzdare.musapp.pojos.Tipo;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class SubirInfoActivity extends AppCompatActivity {
    EditText nombre_mascota,  descripcion;
    TextView edad_mascota, mes_mascota;
    ImageView foto;
    Button subirInfo, seleFoto, prueba;
    ProgressDialog cargando;
    DatabaseReference imgref;
    StorageReference storageReference;
    ValidarActivity objValidar;
    Uri mCropImageUri;
    Spinner spi_ciudad, spi_tipo, spi_meses, spi_edad;


    Bitmap thumb_bitmap = null;


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference ref_info3 = FirebaseDatabase.getInstance().getReference("Mascota");
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref_info = database.getReference("Mascota");
    DatabaseReference ref_info2 = database.getReference();
    String idpush3 = ref_info3.push().getKey();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_info);
        //String idpush = ref_info.push().getKey();
        //String idpush2 = ref_info2.push().getKey();

        foto = findViewById(R.id.img_foto);
        seleFoto = findViewById(R.id.btn_selefoto);
        prueba = findViewById(R.id.btn_prueba);
        descripcion = findViewById(R.id.et_descripcion);
        nombre_mascota = findViewById(R.id.et_nombre_mascota);
        subirInfo = findViewById(R.id.btn_subir_info);


        imgref = FirebaseDatabase.getInstance().getReference().child("Fotos_subidas");
        storageReference = FirebaseStorage.getInstance().getReference().child("img_comprimidas");
        objValidar = new ValidarActivity();


        //SPINNER CIUDAD
        spi_ciudad = findViewById(R.id.spi_ciudad);
        ArrayList<Ciudad> ciudad = new ArrayList<>();
        ciudad.add(new Ciudad(1,"Santiago"));
        ciudad.add(new Ciudad(2,"Valparaíso"));
        ciudad.add(new Ciudad(3,"Concepción"));
        ciudad.add(new Ciudad(4,"Temuco"));

        ArrayAdapter<Ciudad> adapterCiudad = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, ciudad);

        spi_ciudad.setAdapter(adapterCiudad);
        //FIN SPINNER CIUDAD

        //SPINNER TIPO
        spi_tipo = findViewById(R.id.spi_tipo);
        ArrayList<Tipo> tipo = new ArrayList<>();
        tipo.add(new Tipo(1, "Perro"));
        tipo.add(new Tipo(2, "Gato"));
        tipo.add(new Tipo(3, "Conejo"));
        tipo.add(new Tipo(4, "Hámster"));
        tipo.add(new Tipo(5, "Cuy/Cobaya/Conejillo de Indias"));
        tipo.add(new Tipo(6, "Erizo"));
        tipo.add(new Tipo(7, "Pájaro"));

        ArrayAdapter<Tipo> adapterTipo = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, tipo);

        spi_tipo.setAdapter(adapterTipo);
        //FIN SPINNER TIPO

        //SPINNER MESES
        spi_meses = findViewById(R.id.spi_meses);
        ArrayList<Mes> mes = new ArrayList<>();
        mes.add(new Mes(1," 0 meses"));
        mes.add(new Mes(2," 1 mes"));
        mes.add(new Mes(3," 2 meses"));
        mes.add(new Mes(4," 3 meses"));
        mes.add(new Mes(5," 4 meses"));
        mes.add(new Mes(6," 5 meses"));
        mes.add(new Mes(7," 6 meses"));
        mes.add(new Mes(8," 7 meses"));
        mes.add(new Mes(9," 8 meses"));
        mes.add(new Mes(10," 9 meses"));
        mes.add(new Mes(11," 10 meses"));
        mes.add(new Mes(12," 11 meses"));

        ArrayAdapter<Mes> adapterMes = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, mes);

        spi_meses.setAdapter(adapterMes);
        //FIN SPINNER MESES
        //SPINNER AÑOS
        spi_edad = findViewById(R.id.spi_edad);
        ArrayList<Edad> edad = new ArrayList<>();
        edad.add(new Edad(1,"0 años"));
        edad.add(new Edad(2,"1 año"));
        edad.add(new Edad(3,"2 años"));
        edad.add(new Edad(4,"3 años"));
        edad.add(new Edad(5,"4 años"));
        edad.add(new Edad(6,"5 años"));
        edad.add(new Edad(7,"6 años"));
        edad.add(new Edad(8,"7 años"));
        edad.add(new Edad(9,"8 años"));
        edad.add(new Edad(10,"9 años"));
        edad.add(new Edad(11,"10 años"));
        edad.add(new Edad(11,"11 o más años"));


        ArrayAdapter<Edad> adapterEdad = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, edad);

        spi_edad.setAdapter(adapterEdad);
        //FIN SPINNER AÑOS

        seleFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                CropImage.startPickImageActivity(SubirInfoActivity.this);
            }
        });

        cargando = new ProgressDialog(this);


    }//FIN DEL ONCREATE---


    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri imageuri = CropImage.getPickImageResultUri(this,data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageuri)){
                mCropImageUri = imageuri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }else{

            //Recortar Imagen...
            CropImage.activity(imageuri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setRequestedSize(640, 480)
                    .setAspectRatio(1,1)
                    .start(SubirInfoActivity.this);
            }

        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();

                File url = new File(resultUri.getPath());

                //mostrar foto...
                Picasso.with(this).load(url).into(foto);

                //comprimiendo imagen...

                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(640)
                            .setMaxHeight(480)
                            .setQuality(90)
                            .compressToBitmap(url);
                }catch (IOException e){
                    e.printStackTrace();
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,90,byteArrayOutputStream);
                final byte [] thumb_byte = byteArrayOutputStream.toByteArray();
                //fin del compresor...

                int p = (int) (Math.random() * 25 +1);int s = (int) (Math.random() * 25 + 1);
                int t = (int) (Math.random() * 25 +1);int c = (int) (Math.random() * 25 + 1);
                int numero1 = (int) (Math.random() * 1012 + 2111);
                int numero2 = (int) (Math.random() * 1012 + 2111);

                String[] elementos = {"a","b","c","d","e","f","g","h","i","j","k","l","m",
                        "n","o","p","q","r","s","t","u","v","w","x","y","z"};

                final String aleatorio = elementos[p] + elementos[s] + numero1 + elementos[t] +
                        elementos[c] + numero2 + "comprimido.jpg";

                subirInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (objValidar.Vacio(descripcion) || objValidar.Vacio(nombre_mascota)) {
                            Toast.makeText(getApplicationContext(), "Faltan Campos", Toast.LENGTH_SHORT).show();
                        } else {

                            cargando.setTitle("Subiendo foto...");
                            cargando.setMessage("Espere por favor...");
                            cargando.show();

                            StorageReference ref = storageReference.child(aleatorio);
                            UploadTask uploadTask = ref.putBytes(thumb_byte);

                            //subir imagen en storage...

                            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw Objects.requireNonNull(task.getException());
                                    }
                                    return ref.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {


                                    Uri downloaduri = task.getResult();
                                    String descripcion_string = descripcion.getText().toString();
                                    String nombreMascota = nombre_mascota.getText().toString();
                                    String edadMascota = spi_edad.getSelectedItem().toString();
                                    String mesMascota = spi_meses.getSelectedItem().toString();
                                    String ciudadMascota = spi_ciudad.getSelectedItem().toString();
                                    String tipoMascota = spi_tipo.getSelectedItem().toString();
                                    String filtroMascota = ciudadMascota+tipoMascota;

                                    Galeria gal = new Galeria(descripcion_string, downloaduri.toString());


                                    Mascota mas = new Mascota(user.getUid(), idpush3, nombreMascota, edadMascota, descripcion_string,
                                            downloaduri.toString(),user.getDisplayName(),user.getPhotoUrl().toString(),
                                            ciudadMascota,"",mesMascota, tipoMascota, filtroMascota );

                                    ref_info.push().setValue(mas);
                                    imgref.push().setValue(gal);
                                    cargando.dismiss();

                                    Toast.makeText(SubirInfoActivity.this, "Información cargada con éxito", Toast.LENGTH_LONG).show();

                                    Intent i = new Intent(v.getContext(), CoreActivity.class);
                                    startActivity(i);


                                }
                            });


                        }
                    }
                });//FIN DEL BOTON SUBIR INFO


            }

        }


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
                                Toast.makeText(SubirInfoActivity.this, "Cerrando Sesión", Toast.LENGTH_SHORT).show();
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