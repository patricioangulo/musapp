package com.itzdare.musapp.acciones;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.itzdare.musapp.CoreActivity;
import com.itzdare.musapp.MainActivity;
import com.itzdare.musapp.R;
import com.itzdare.musapp.adapters.ChatsAdapter;
import com.itzdare.musapp.pojos.Chats;
import com.itzdare.musapp.pojos.Estado;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class MensajesActivity extends AppCompatActivity {
    CircleImageView img_user;
    TextView username;
    ImageView ic_conectado, ic_desconectado;
    SharedPreferences mPref;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref_estado = database.getReference("Estado").child(user.getUid());
    DatabaseReference ref_chat = database.getReference("Chats");

    EditText et_mensaje_txt;
    ImageButton btn_enviar_msj;

    //ID CHAT GLOBAL---

    String id_chat_global;
    Boolean amigoonline = false;

    //RV----
    RecyclerView rv_chats;
    ChatsAdapter adapter;
    ArrayList<Chats> chatlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);

        mPref = getApplicationContext().getSharedPreferences("usuario_sp",MODE_PRIVATE);
        img_user = findViewById(R.id.img_usuario);
        username = findViewById(R.id.tv_user);
        ic_conectado = findViewById(R.id.icon_conectado);
        ic_desconectado = findViewById(R.id.icon_desconectado);

        String usuario = getIntent().getExtras().getString("nombre");
        String foto = getIntent().getExtras().getString("img_user");
        final String id_user = getIntent().getExtras().getString("id_user");
        id_chat_global= getIntent().getExtras().getString("id_unico");

        colocarenvisto();

        et_mensaje_txt = findViewById(R.id.et_txt_mensaje);
        btn_enviar_msj = findViewById(R.id.btn_enviar_msj);
        btn_enviar_msj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msj = et_mensaje_txt.getText().toString();

                if (!msj.isEmpty()){
                    final Calendar c = Calendar.getInstance();
                    SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
                    String idpush = ref_chat.push().getKey();

                    if(amigoonline){
                        Chats chatmsj = new Chats(idpush,user.getUid(),id_user,msj,"si",dateformat.format(c.getTime()),timeformat.format(c.getTime()));
                        ref_chat.child(id_chat_global).child(idpush).setValue(chatmsj);
                        Toast.makeText(MensajesActivity.this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
                        et_mensaje_txt.setText("");
                    }else{
                        Chats chatmsj = new Chats(idpush,user.getUid(),id_user,msj,"no",dateformat.format(c.getTime()),timeformat.format(c.getTime()));
                        ref_chat.child(id_chat_global).child(idpush).setValue(chatmsj);
                        Toast.makeText(MensajesActivity.this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
                        et_mensaje_txt.setText("");
                    }

                }else{
                    Toast.makeText(MensajesActivity.this, "El mensaje está vacío", Toast.LENGTH_SHORT).show();
                }



            }
        });


        final String id_user_sp = mPref.getString("usuario_sp","");

        username.setText(usuario);
        Glide.with(this).load(foto).into(img_user);

        final DatabaseReference ref = database.getReference("Estado").child(id_user_sp).child("chatcon");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String chatcon = snapshot.getValue(String.class);
                if(snapshot.exists()){
                    if(chatcon.equals(user.getUid())){
                        amigoonline = true;
                        ic_conectado.setVisibility(View.VISIBLE);
                        ic_desconectado.setVisibility(View.GONE);
                    }else{
                        amigoonline = false;
                        ic_conectado.setVisibility(View.GONE);
                        ic_desconectado.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //RV---

        rv_chats = findViewById(R.id.rv);
        rv_chats.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rv_chats.setLayoutManager(linearLayoutManager);

        chatlist = new ArrayList<>();
        adapter = new ChatsAdapter(chatlist, this);
        rv_chats.setAdapter(adapter);

        Leermensajes();



    }//FIN DEL ON CREATE

    private void colocarenvisto() {
        ref_chat.child(id_chat_global).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Chats chats = snapshot1.getValue(Chats.class);
                    if(chats.getRecibe().equals(user.getUid())){
                        ref_chat.child(id_chat_global).child(chats.getId()).child("visto").setValue("si");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void Leermensajes() {
        ref_chat.child(id_chat_global).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    chatlist.removeAll(chatlist);
                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        Chats chat = snapshot1.getValue(Chats.class);
                        chatlist.add(chat);
                        setScroll();
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setScroll() {
        rv_chats.scrollToPosition(adapter.getItemCount()-1);
    }
    private void estadousuario(final String estado) {
        ref_estado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String id_user_sp = mPref.getString("usuario_sp","");
                Estado est = new Estado(estado,"","",id_user_sp);
                ref_estado.setValue(est);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    //manipulación del ususario de conectado a desconectado y viceversa
    @Override
    protected void onResume() {
        super.onResume();
        estadousuario("conectado");
    }

    @Override
    protected void onPause() {
        super.onPause();
        estadousuario("desconectado");
        dameultimafecha();
    }

    private void dameultimafecha() {
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");

        ref_estado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ref_estado.child("fecha").setValue(dateformat.format(c.getTime()));
                ref_estado.child("hora").setValue(timeformat.format(c.getTime()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                                Toast.makeText(MensajesActivity.this, "Cerrando Sesión", Toast.LENGTH_SHORT).show();
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