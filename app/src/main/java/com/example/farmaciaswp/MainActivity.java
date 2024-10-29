package com.example.farmaciaswp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth; // Importa FirebaseAuth
import com.google.firebase.auth.FirebaseUser; // Importa FirebaseUser

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics; // Declara FirebaseAnalytics
    private FirebaseAuth mAuth; // Declara FirebaseAuth
    private EditText editTextCorreo;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inicializa Firebase Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Inicializa Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Registro de un evento de prueba
        Bundle bundle = new Bundle();
        bundle.putString("example_param", "example_value");
        mFirebaseAnalytics.logEvent("example_event", bundle);

        // Ajuste de márgenes según las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializa los elementos de la interfaz
        editTextCorreo = findViewById(R.id.editTextText2);
        editTextPassword = findViewById(R.id.editTextTextPassword2);
        buttonLogin = findViewById(R.id.button4);
        buttonRegister = findViewById(R.id.button2);

        // Configura el listener para el botón de inicio de sesión
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = editTextCorreo.getText().toString();
                String password = editTextPassword.getText().toString();

                // Lógica de autenticación
                if (correo.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Iniciar sesión
                    mAuth.signInWithEmailAndPassword(correo, password)
                            .addOnCompleteListener(MainActivity.this, task -> {
                                if (task.isSuccessful()) {
                                    // Inicio de sesión exitoso
                                    Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // Inicia otra actividad después del inicio de sesión exitoso
                                    Intent intent = new Intent(MainActivity.this, Catalogo_Productos.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, "Error de inicio de sesión: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        // Configura el listener para el botón de registro
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = editTextCorreo.getText().toString();
                String password = editTextPassword.getText().toString();

                // Lógica de registro
                if (correo.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Registrar usuario
                    mAuth.createUserWithEmailAndPassword(correo, password)
                            .addOnCompleteListener(MainActivity.this, task -> {
                                if (task.isSuccessful()) {
                                    // Registro exitoso
                                    Toast.makeText(MainActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // Aquí puedes redirigir a otra actividad o realizar más acciones
                                } else {
                                    Toast.makeText(MainActivity.this, "Error de registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        Button nextButton = findViewById(R.id.button12);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Catalogo_Productos.class);
                startActivity(intent);
            }
        });
    }
}

