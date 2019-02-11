package com.example.ruben.Paginas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.sql.SQLException;

import Entidades.Usuario;
import Extra.AzureDB;

public class Login extends AppCompatActivity {

    public Login(){}
    private AzureDB azure;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        final EditText username = findViewById(R.id.user);
        final EditText password = findViewById(R.id.password);

        Button acceder = findViewById(R.id.acceder);
        acceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario usuario = null;
                String u = username.getText().toString();
                String p = password.getText().toString();
                if(!u.equals("") && !p.equals("")){
                    Intent intent = new Intent(Login.this, BottomActivity.class);
                    azure = new AzureDB();
                    azure.conectar();
                    try {
                        usuario = azure.comprobarUsuario(u, p);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    azure.desconectar();
                    if(usuario != null && usuario.getTipo().equals("TECNICO")) {
                        if(usuario.getHabilitado().equals("SI")) {
                            intent.putExtra("usuario", usuario);
                            startActivity(intent);
                        }
                        else Toast.makeText(getApplicationContext(),"El usuario no está habilitado", Toast.LENGTH_LONG).show();
                    }
                    else Toast.makeText(getApplicationContext(),"El usuario no es un técnico o no existe", Toast.LENGTH_LONG).show();
                }

                else {
                    Toast.makeText(getApplicationContext(), "Campos vacíos", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
