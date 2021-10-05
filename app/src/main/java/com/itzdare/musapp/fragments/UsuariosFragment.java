package com.itzdare.musapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itzdare.musapp.R;
import com.itzdare.musapp.acciones.SubirInfoActivity;
import com.itzdare.musapp.adapters.MascotasAdapter;
import com.itzdare.musapp.pojos.Mascota;

import java.util.ArrayList;

public class UsuariosFragment extends Fragment {


    //INICIO DEL CÓDIGO
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ProgressBar progressBar;

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        View view = inflater.inflate(R.layout.fragment_usuarios, container, false);

        //TextView tv_user = view.findViewById(R.id.tv_user);
        //ImageView img_user = view.findViewById(R.id.img_user);


        progressBar = view.findViewById(R.id.progressbar);
        assert user != null;
        //tv_user.setText(user.getDisplayName());
        //Glide.with(this).load(user.getPhotoUrl()).into(img_user);

        //Muestra imágenes de las fotos cargadas
        RecyclerView rv_mascota;
        ArrayList<Mascota>mascotaArrayList;
        MascotasAdapter mascotasAdapter;
        LinearLayoutManager mLayoutManager;

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        rv_mascota = view.findViewById(R.id.rv_mascota);
        rv_mascota.setLayoutManager(mLayoutManager);

        mascotaArrayList = new ArrayList<>();
        mascotasAdapter = new MascotasAdapter(mascotaArrayList,getContext());
        rv_mascota.setAdapter(mascotasAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref_mascota = database.getReference("Mascota");
        ref_mascota.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    rv_mascota.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    mascotaArrayList.removeAll(mascotaArrayList);
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        Mascota mascota = snapshot1.getValue(Mascota.class);
                        mascotaArrayList.add(mascota);
                    }
                    mascotasAdapter.notifyDataSetChanged();
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "No existen mascotas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //progressBar.setVisibility(View.GONE);

        ImageButton btn_add;
        btn_add = view.findViewById(R.id.btn_add);
        view.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), SubirInfoActivity.class);
                startActivity(i);
            }
        });


        return view;
    }//FIN DEL ONCREATE
}