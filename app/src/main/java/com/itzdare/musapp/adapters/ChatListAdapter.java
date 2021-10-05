package com.itzdare.musapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.data.model.User;
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
import com.itzdare.musapp.pojos.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.viewHolderChatList> {

    List<Users> usersList;
    Context context;
    SharedPreferences mPref;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public ChatListAdapter(List<Users> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolderChatList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chatlista,parent,false);
        viewHolderChatList holder = new viewHolderChatList(v);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderChatList holder, int position) {
        Users userss = usersList.get(position);
        final Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);

        //nombre de usuario e imagen
        holder.tv_usuario.setText(userss.getNombre());
        Glide.with(context).load(userss.getFoto()).into(holder.img_user);

        //mostrar amigo y ocultar los que no lo son
        DatabaseReference ref_mis_solicitudes = database.getReference("Solicitudes 2").child(user.getUid());
        ref_mis_solicitudes.child(userss.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String estado = snapshot.child("estado").getValue(String.class);

                if(snapshot.exists()){
                    if(estado.equals("amigos")){
                        holder.cardView.setVisibility(View.VISIBLE);
                        holder.amigos.setVisibility(View.VISIBLE);
                        holder.tengosolicitud.setVisibility(View.GONE);
                    }
                    if(estado.equals("solicitud")){
                        holder.cardView.setVisibility(View.VISIBLE);
                        holder.amigos.setVisibility(View.GONE);
                        holder.tengosolicitud.setVisibility(View.VISIBLE);
                    }


                }else{
                    holder.cardView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.tengosolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                aceptarNotificacion(position);


                final String idchat = ref_mis_solicitudes.push().getKey();
                final DatabaseReference A = database.getReference("Solicitudes 2").child(userss.getId()).child(user.getUid());
                A.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("idchat", idchat);
                        hashMap.put("estado", "amigos");

                        A.updateChildren(hashMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                final DatabaseReference B = database.getReference("Solicitudes 2").child(user.getUid()).child(userss.getId());
                B.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("idchat", idchat);
                        hashMap.put("estado", "amigos");

                        B.updateChildren(hashMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }//FIN DEL ON CLICK TENGO SOLICITUD
        });//FIN DEL HOLDER TENGO SOLICITUD

        holder.amigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPref = v.getContext().getSharedPreferences("usuario_sp",Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = mPref.edit();

                final DatabaseReference ref = database.getReference("Solicitudes 2").child(user.getUid()).child(userss.getId()).child("idchat");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String id_unico = snapshot.getValue(String.class);
                        if (snapshot.exists()){
                            Intent intent = new Intent(v.getContext(), MensajesActivity.class);
                            intent.putExtra("nombre",userss.getNombre());
                            intent.putExtra("img_user",userss.getFoto());
                            intent.putExtra("id_user",userss.getId());
                            intent.putExtra("id_unico",id_unico);
                            editor.putString("usuario_sp",userss.getId());
                            editor.apply();

                            v.getContext().startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });//FIN DEL HOLDER CARDVIEW
    }//FIN DEL ON BIND VIEW HOLDER

    private void aceptarNotificacion(int position) {
        Users userss = usersList.get(position);
        DatabaseReference ref = database.getReference("Solicitudes 2")
                .child(user.getUid())
                .child(userss.getId());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RequestQueue myrequest = Volley.newRequestQueue(context.getApplicationContext());
                JSONObject json = new JSONObject();

                try{

                    //String token = "elTKULezRQC7sHrYjCMgUm:APA91bFwxT6VaTGehlJsQwcguDOZ6bKZhGdr-Fb5ESsYYRAtnpiYxBFKnGfTphca2HpdFfvdex33bGcTb8Qk6gDZN61MMxP7tEnTYTRlDNFYqcgJScmhfFe3qV2BNK1P2OKQbYVi1-8D";
                    String token = snapshot.child("token").getValue(String.class);
                    String mascota = snapshot.child("nombre_mascota").getValue(String.class);
                    json.put("to",token);
                    JSONObject notificacion = new JSONObject();
                    notificacion.put("titulo","Solicitud Aceptada");
                    notificacion.put("detalle",mascota +" y " + user.getDisplayName() +" Aceptaron tu solicitud. Envíales un mensaje para que comience la adopción");

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
        return usersList.size();
    }

    public class viewHolderChatList extends RecyclerView.ViewHolder {

        TextView tv_usuario;
        ImageView img_user;
        CardView cardView;
        TextView tv_conectado,tv_desconectado;
        ImageView icon_conectado,icon_desconectado;
        ImageButton tengosolicitud, amigos;




        public viewHolderChatList(@NonNull View itemView) {
            super(itemView);
            tv_usuario = itemView.findViewById(R.id.tv_user);
            img_user = itemView.findViewById(R.id.img_usuario);
            cardView = itemView.findViewById(R.id.cardview);
            tv_conectado = itemView.findViewById(R.id.tv_conectado);
            tv_desconectado = itemView.findViewById(R.id.tv_desconectado);
            icon_conectado = itemView.findViewById(R.id.icon_conectado);
            icon_desconectado = itemView.findViewById(R.id.icon_desconectado);
            tengosolicitud = itemView.findViewById(R.id.btn_tengosolicitud);
            amigos = itemView.findViewById(R.id.btn_amigos);





        }
    }
}
