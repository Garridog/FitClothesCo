package com.example.fitclothesco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText etCorreo, etPassword;
    Button btnRegistrar, btnIngresar;

    String email = "";
    String password = "";

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnIngresar = findViewById(R.id.btnIngresar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pantallaLogin = new Intent(Login.this, Registro.class);
                startActivity(pantallaLogin);
                finish();
            }
        });

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = etCorreo.getText().toString();
                password = etPassword.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()){
                    loginUser();
                } else {
                    Toast.makeText(Login.this, "Complete los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void loginUser(){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(Login.this, MainActivity.class));
                } else {
                    Toast.makeText(Login.this, "No se pudo iniciar sesion, verifica tus datos ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}