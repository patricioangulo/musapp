package com.itzdare.musapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itzdare.musapp.R;
import com.itzdare.musapp.acciones.VerMascotaActivity;
import com.itzdare.musapp.pojos.Mascota;

import java.util.List;

public class MascotasAdapter extends RecyclerView.Adapter<MascotasAdapter.viewHolderAdapter> {


        List<Mascota> mascotaList;
        Context context;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        SharedPreferences mPref;


    public MascotasAdapter(List<Mascota> mascotaList, Context context) {
        this.mascotaList = mascotaList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_mascotas,parent,false);
        viewHolderAdapter holder = new viewHolderAdapter(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderAdapter holder, int position) {
        Mascota mascota = mascotaList.get(position);
        final Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);


        Glide.with(context).load(mascota.getFoto()).into(holder.img_mascota);
        holder.tv_nombreMascota.setText(mascota.getNombre_mascota());

        //MOSTRAR MASCOTAS CREADAS POR EL USUARIO
        if(user.getUid().equals(mascota.getId_user())){
            holder.cardView.setVisibility(View.VISIBLE);
            holder.progressBar.setVisibility(View.GONE);
        }else{
            holder.cardView.setVisibility(View.GONE);
        }

        //MOSTRAR MASCOTA SELECCIONADA
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPref = v.getContext().getSharedPreferences("usuario_sp", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = mPref.edit();

                final DatabaseReference ref = database.getReference("Mascota").child(mascota.getId_mascota());
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                            Intent i = new Intent(v.getContext(), VerMascotaActivity.class);
                            i.putExtra("nombre", mascota.getNombre_mascota());
                            i.putExtra("imagen", mascota.getFoto());
                            i.putExtra("des", mascota.getDescripcion());
                            i.putExtra("id", mascota.getId_mascota());
                            v.getContext().startActivity(i);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

        /*holder.btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), EditarInfoActivity.class);
                v.getContext().startActivity(i);
            }
        });*/


    }//FIN DEL ON BIND VIEW HOLDER

    @Override
    public int getItemCount() {
        return mascotaList.size();
    }

    public class viewHolderAdapter extends RecyclerView.ViewHolder {

        TextView tv_nombreMascota;
        ImageView img_mascota;
        ImageButton btn_editar;
        CardView cardView;
        ProgressBar progressBar;
        public viewHolderAdapter(@NonNull View itemView) {
            super(itemView);
            tv_nombreMascota = itemView.findViewById(R.id.tv_nombre_mascota);
            img_mascota = itemView.findViewById(R.id.img_mascota);
            //btn_editar = itemView.findViewById(R.id.btn_editar);
            cardView = itemView.findViewById(R.id.cardviewMascotas);
            progressBar = itemView.findViewById(R.id.progress_bar_mascotas);




        }
    }
}
