package com.itzdare.musapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.itzdare.musapp.CoreActivity;
import com.itzdare.musapp.R;
import com.itzdare.musapp.acciones.SubirInfoActivity;
import com.itzdare.musapp.adapters.GaleriaAdapter;
import com.itzdare.musapp.pojos.Ciudad;
import com.itzdare.musapp.pojos.Mascota;
import com.itzdare.musapp.pojos.Tipo;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    //INICIO DEL CÓDIGO
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ProgressBar progressBar;

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //TextView tv_user = view.findViewById(R.id.tv_user);
        //ImageView img_user = view.findViewById(R.id.img_user);

        progressBar = view.findViewById(R.id.progressbar);
        assert user != null;
        //tv_user.setText(user.getDisplayName());
        //Glide.with(this).load(user.getPhotoUrl()).into(img_user);


        //BOTONES DEL FILTRO
        Button btn_filtro, btn_buscar, btn_borrar_filtro;
        btn_borrar_filtro = view.findViewById(R.id.btn_borrar_filtro);
        btn_buscar = view.findViewById(R.id.btn_buscar);



        //MOSTRAR GALERÍA DE FOTOS
        RecyclerView rv_galeria, rv_tipo;
        ArrayList<Mascota> galeriaArrayList, tipoArrayList;
        GaleriaAdapter adapter, adapter1;
        LinearLayoutManager mLayoutManager, tipoLayoutManager;

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        rv_galeria = view.findViewById(R.id.rv_fotos_mascotas);
        rv_galeria.setLayoutManager(mLayoutManager);


        tipoLayoutManager = new LinearLayoutManager(getContext());
        tipoLayoutManager.setReverseLayout(true);
        tipoLayoutManager.setStackFromEnd(true);
        rv_tipo = view.findViewById(R.id.rv_tipo);
        rv_tipo.setLayoutManager(tipoLayoutManager);



        galeriaArrayList = new ArrayList<>();
        adapter = new GaleriaAdapter(galeriaArrayList,getContext());
        rv_galeria.setAdapter(adapter);


        Spinner spi_tipo, spi_ciudad;
        ArrayAdapter<Tipo> adapterTipo;
        spi_tipo = view.findViewById(R.id.spi_tipo);
        ArrayList<Tipo> tipo = new ArrayList<>();
        //tipo.add(new Tipo(0, "Seleccione una Opción"));
        tipo.add(new Tipo(1, "Perro"));
        tipo.add(new Tipo(2, "Gato"));
        tipo.add(new Tipo(3, "Conejo"));
        tipo.add(new Tipo(4, "Hámster"));
        tipo.add(new Tipo(5, "Cuy/Cobaya/Conejillo de Indias"));
        tipo.add(new Tipo(6, "Erizo"));
        tipo.add(new Tipo(7, "Pájaro"));

        adapterTipo = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item,tipo);
        spi_tipo.setAdapter(adapterTipo);

        //SPINNER CIUDAD
        spi_ciudad = view.findViewById(R.id.spi_ciudad);
        ArrayList<Ciudad> ciudad = new ArrayList<>();
        ciudad.add(new Ciudad(1,"Santiago"));
        ciudad.add(new Ciudad(2,"Valparaíso"));
        ciudad.add(new Ciudad(3,"Concepción"));
        ciudad.add(new Ciudad(4,"Temuco"));

        ArrayAdapter<Ciudad> adapterCiudad = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, ciudad);

        spi_ciudad.setAdapter(adapterCiudad);
        //FIN SPINNER CIUDAD


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Mascota");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    progressBar.setVisibility(View.GONE);
                    galeriaArrayList.removeAll(galeriaArrayList);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        Mascota gal = snapshot1.getValue(Mascota.class);
                        galeriaArrayList.add(gal);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tipoArrayList = new ArrayList<>();
        adapter1 = new GaleriaAdapter(tipoArrayList,getContext());
        rv_tipo.setAdapter(adapter1);


        //2. SELECT * FROM Mascota WHERE id = "-MVqSHz3PqfoVHbCq41V"
       /* Query query = FirebaseDatabase.getInstance().getReference("Mascota")
                .orderByChild("id_mascota")
                .equalTo("-MVqSHz3PqfoVHbCq41V");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    galeriaArrayList.removeAll(galeriaArrayList);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        Mascota gal = snapshot1.getValue(Mascota.class);
                        galeriaArrayList.add(gal);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/



        view.findViewById(R.id.btn_filtro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spi_tipo.getVisibility()==View.VISIBLE){
                    spi_tipo.setVisibility(View.GONE);
                    btn_buscar.setVisibility(View.GONE);
                    btn_borrar_filtro.setVisibility(View.GONE);
                    spi_ciudad.setVisibility(View.GONE);
                }else{

                spi_tipo.setVisibility(View.VISIBLE);
                btn_buscar.setVisibility(View.VISIBLE);
                btn_borrar_filtro.setVisibility(View.VISIBLE);
                spi_ciudad.setVisibility(View.VISIBLE);
                }
            }
        });

        view.findViewById(R.id.btn_buscar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ciudadMascota = spi_ciudad.getSelectedItem().toString();
                String tipoMascota = spi_tipo.getSelectedItem().toString();
                String filtroMascota = ciudadMascota+tipoMascota;

                //2. SELECT * FROM Mascota WHERE filtro = "SantiagoPerro"
                Query query = FirebaseDatabase.getInstance().getReference("Mascota")
                        .orderByChild("filtro")
                        .equalTo(filtroMascota);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            tipoArrayList.removeAll(tipoArrayList);
                            for (DataSnapshot snapshot1: snapshot.getChildren()){
                                Mascota gal = snapshot1.getValue(Mascota.class);
                                tipoArrayList.add(gal);
                            }
                            adapter1.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "No hay mascotas de ese tipo", Toast.LENGTH_SHORT).show();
                    }
                });
                rv_galeria.setVisibility(View.GONE);
                rv_tipo.setVisibility(View.VISIBLE);
            }
        });

        view.findViewById(R.id.btn_borrar_filtro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv_galeria.setVisibility(View.VISIBLE);
                rv_tipo.setVisibility(View.GONE);
                spi_tipo.setVisibility(View.GONE);
                btn_buscar.setVisibility(View.GONE);
                btn_borrar_filtro.setVisibility(View.GONE);
                spi_ciudad.setVisibility(View.GONE);



            }
        });




        return view;

    }//FIN DEL ONCREATE





}