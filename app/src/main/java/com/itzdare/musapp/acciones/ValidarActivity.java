package com.itzdare.musapp.acciones;

import android.text.TextUtils;
import android.widget.EditText;

public class ValidarActivity {
    //metodo para validar si editext esta vacio
    public  boolean Vacio(EditText campo){
        String dato = campo.getText().toString().trim();
        if(TextUtils.isEmpty(dato)){
            campo.setError("Campo Requerido");
            campo.requestFocus();
            return true;
        }
        else{
            return false;
        }
    }


}
