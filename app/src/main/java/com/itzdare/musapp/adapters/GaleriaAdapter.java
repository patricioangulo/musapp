package com.itzdare.musapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itzdare.musapp.R;
import com.itzdare.musapp.acciones.MensajesActivity;
import com.itzdare.musapp.pojos.Mascota;
import com.itzdare.musapp.pojos.Solicitudes;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GaleriaAdapter extends RecyclerView.Adapter<GaleriaAdapter.FotosViewHolder> {

    List<Mascota> galeriaList;
    Context context;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    SharedPreferences mPref;

    public GaleriaAdapter(List<Mascota> galeriaList, Context context) {
        this.galeriaList = galeriaList;
        this.context = context;

    }


    @NonNull
    @Override
    public FotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_galeria,parent,false);
        FotosViewHolder holder = new FotosViewHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull FotosViewHolder holder, int position) {


        Mascota gal = galeriaList.get(position);
        holder.tv_nombre_mascota.setText(gal.getNombre_mascota());
        holder.tv_edad_mascota.setText(gal.getEdad_mascota()+""+gal.getMeses());
        holder.tv_ciudad.setText(gal.getCiudad());
        holder.tv_descripcion.setText(gal.getDescripcion());
        Picasso.with(context).load(gal.getFoto()).into(holder.img_foto, new Callback() {
            @Override
            public void onSuccess() {
                holder.progress.setVisibility(View.GONE);
                holder.img_foto.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                Toast.makeText(context, "Tienes un error!", Toast.LENGTH_SHORT).show();

            }
        });

        if(user.getUid().equals(gal.getId_user())){
            holder.cardView.setVisibility(View.GONE);
        }else{
            holder.cardView.setVisibility(View.VISIBLE);
        }

        DatabaseReference ref_mis_botones = database.getReference("Solicitudes 2").child(user.getUid());
        ref_mis_botones.child(gal.getId_user()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String estado = snapshot.child("estado").getValue(String.class);
                String mascota = snapshot.child("id_mascota").getValue(String.class);
                String mascota2 = snapshot.child("id_mascota").getValue(String.class);


                if(snapshot.exists()){
                    if(estado.equals("send") && mascota.equals(mascota2)) {
                        holder.send.setVisibility(View.VISIBLE);
                        holder.amigos.setVisibility(View.GONE);
                        holder.tengosolicitud.setVisibility(View.GONE);
                        holder.add.setVisibility(View.GONE);
                        holder.progress.setVisibility(View.GONE);
                    }
                    if(estado.equals("amigos") && mascota.equals(mascota2)) {
                        holder.send.setVisibility(View.GONE);
                        holder.amigos.setVisibility(View.VISIBLE);
                        holder.tengosolicitud.setVisibility(View.GONE);
                        holder.add.setVisibility(View.GONE);
                        holder.progress.setVisibility(View.GONE);
                    }
                    if(estado.equals("solicitud") && mascota.equals(mascota2)) {
                        holder.send.setVisibility(View.GONE);
                        holder.amigos.setVisibility(View.GONE);
                        holder.tengosolicitud.setVisibility(View.VISIBLE);
                        holder.add.setVisibility(View.GONE);
                        holder.progress.setVisibility(View.GONE);
                    }

                }else{
                    holder.send.setVisibility(View.GONE);
                    holder.amigos.setVisibility(View.GONE);
                    holder.tengosolicitud.setVisibility(View.GONE);
                    holder.add.setVisibility(View.VISIBLE);
                    holder.progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Enviar Solicitud
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enviarNotificacion(position);

                DatabaseReference ref = database.getReference("Token").child(user.getUid());
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String token = snapshot.child("tokenId").getValue(String.class);
                        final DatabaseReference refA = database.getReference("Solicitudes 2").child(user.getUid());
                        refA.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Solicitudes sol = new Solicitudes("send","",gal.getId_mascota(),gal.getNombre_mascota(),gal.getFoto(),token);
                                refA.child(gal.getId_user()).setValue(sol);
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        final DatabaseReference refB = database.getReference("Solicitudes 2").child(gal.getId_user());
                        refB.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Solicitudes sol = new Solicitudes("solicitud","",gal.getId_mascota(),gal.getNombre_mascota(),gal.getFoto(),token);
                                refB.child(user.getUid()).setValue(sol);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }//FIN DEL ON CLICK ADD
        });//FIN DEL HOLDER ADD

        //Amigos
        holder.amigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPref = v.getContext().getSharedPreferences("usuario_sp",Context.MODE_PRIVATE);
                final  SharedPreferences.Editor editor = mPref.edit();

                final DatabaseReference ref = database.getReference("Solicitudes 2").child(user.getUid()).child(gal.getId_user()).child("idchat");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String id_unico = snapshot.getValue(String.class);
                        if (snapshot.exists()){
                            Intent intent = new Intent(v.getContext(), MensajesActivity.class);
                            intent.putExtra("nombre",gal.getNombre_user());
                            intent.putExtra("img_user",gal.getFoto_user());
                            intent.putExtra("id_user",gal.getId_user());
                            intent.putExtra("id_unico",id_unico);
                            editor.putString("usuario_sp",gal.getId_user());
                            editor.apply();

                            v.getContext().startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


    }//FIN DEL ON BIND VIEW HOLDER



    private void enviarNotificacion(int position) {

        Mascota gal = galeriaList.get(position);
        //DatabaseReference ref = database.getReference("Token").child(gal.getId_user());
        DatabaseReference ref = database.getReference("Token").child(gal.getId_user());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RequestQueue myrequest = Volley.newRequestQueue(context.getApplicationContext());
                JSONObject json = new JSONObject();

                try{

                    String token = snapshot.child("tokenId").getValue(String.class);
                    //String token = "cYRjE4U_R0WeL2NhDVFv8u:APA91bEssWyiS9WkVxgEqe8V-UHGuF-Rw4KUe5XyIF9sz7lc9tFzGD5--YrH6feuPSIV3H17TnjHrZOP6alNlC7sux8lxSTUTQyDB_nDDZIinMJrRLJyFbVxum9KPLfYSKDMxgHwUHiI";
                    json.put("to",token);
                    JSONObject notificacion = new JSONObject();
                    notificacion.put("titulo","Notificación de Adpoción");
                    notificacion.put("detalle",user.getDisplayName() +" quiere adoptar a "+gal.getNombre_mascota());

                    json.put("data",notificacion);

                    String URL = "https://fcm.googleapis.com/fcm/send";
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,json,null,null){
                        @Override
                        public Map<String, String> getHeaders()  {
                            Map<String,String> header = new HashMap<>();

                            header.put("content-type", "application/json");
                            header.put("authorization", "key=AAAA9fsS70Y:APA91bE8-lHzRKiUylUeS_VOO45jy2STrpzF-TkYsPKfvOeP0-xfOoCZQjUiJYnnF4BNvfPYr27eIlvR3sM-v-YW_EGhwWniP4unL3qJxGNAGketAlTE5Wk73nNSMygdh1-ieBijaw3A");
                            return header;
                        }
                    };
                    myrequest.add(request);

                }catch(JSONException e){
                    e.printStackTrace();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return galeriaList.size();
    }

    public class FotosViewHolder extends RecyclerView.ViewHolder {

        TextView tv_nombre_mascota,tv_edad_mascota,tv_descripcion, tv_ciudad;
        ImageView img_foto;
        Button add,send,amigos,tengosolicitud;
        CardView cardView;
        Button btnEnviarToken;
        ProgressBar progress;

        public FotosViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nombre_mascota= itemView.findViewById(R.id.tv_nombre_mascota);
            tv_edad_mascota = itemView.findViewById(R.id.tv_edad_mascota);
            tv_ciudad = itemView.findViewById(R.id.tv_ciudad);
            tv_descripcion = itemView.findViewById(R.id.tv_descripcion);
            img_foto = itemView.findViewById(R.id.img_foto);
            add = itemView.findViewById(R.id.btn_add);
            send = itemView.findViewById(R.id.btn_send);
            tengosolicitud = itemView.findViewById(R.id.btn_tengosolicitud);
            amigos = itemView.findViewById(R.id.btn_amigos);
            cardView = itemView.findViewById(R.id.cardview);
            progress = itemView.findViewById(R.id.progress_bar_galeria);


        }
    }
}
