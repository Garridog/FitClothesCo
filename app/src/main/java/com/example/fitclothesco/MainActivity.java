package com.example.fitclothesco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.contentcapture.DataShareWriteAdapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitclothesco.Adaptadores.ListViewVentasAdapter;
import com.example.fitclothesco.model.Venta;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Venta> listVenta = new ArrayList<Venta>();
    ArrayAdapter<Venta> arrayAdapterVenta;
    ListViewVentasAdapter listViewVentasAdapter;
    LinearLayout linearLayoutEditar;
    ListView listViewVentas;

    EditText inputNombre, inputPrenda, inputPrecio;
    Button btnCancelar, btnSalir;
    TextView textViewName;

    Venta ventaSeleccionada;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputNombre = findViewById(R.id.inputNombre);
        inputPrenda = findViewById(R.id.inputPrenda);
        inputPrecio = findViewById(R.id.inputPrecio);

        btnCancelar = findViewById(R.id.btnCancelar);
        btnSalir = findViewById(R.id.btnSalir);
        textViewName = findViewById(R.id.textViewName);

        listViewVentas = findViewById(R.id.listViewPersonas);
        linearLayoutEditar = findViewById(R.id.LinearLayoutEditar);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
            }
        });

        getUserInfo();

        listViewVentas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ventaSeleccionada = (Venta) parent.getItemAtPosition(position);
                inputNombre.setText(ventaSeleccionada.getNombreCliente());
                inputPrenda.setText(ventaSeleccionada.getPrenda());
                inputPrecio.setText(ventaSeleccionada.getTotalVenta());
                //HACER VISIBLE EL LINEARLAYOUT
                linearLayoutEditar.setVisibility(View.VISIBLE);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutEditar.setVisibility(View.GONE);
                ventaSeleccionada = null;
            }
        });

        inicializarFirebase();
        listarVentas();
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void listarVentas(){
        databaseReference.child("Venta").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listVenta.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    Venta v = objSnaptshot.getValue(Venta.class);
                    listVenta.add(v);
                }
                //iniciar nuestro propio adaptador
                listViewVentasAdapter = new ListViewVentasAdapter(MainActivity.this, listVenta);
                //arrayAdapterVenta = new ArrayAdapter<Venta>(
                //        MainActivity.this,
                //        android.R.layout.simple_list_item_1,
                //        listVenta
                //);
                listViewVentas.setAdapter(listViewVentasAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crud_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String nombres = inputNombre.getText().toString();
        String prendas = inputPrenda.getText().toString();
        String precio = inputPrecio.getText().toString();

        switch (item.getItemId()){
            case R.id.menu_agregar:
                insertar();
                break;
            case R.id.menu_guardar:
                if (ventaSeleccionada != null){
                    if (validarInputs()==false){
                        Venta v = new Venta();
                        v.setIdVenta(ventaSeleccionada.getIdVenta());
                        v.setNombreCliente(nombres);
                        v.setPrenda(prendas);
                        v.setTotalVenta(precio);
                        v.setFecha(ventaSeleccionada.getFecha());
                        v.setTimestamp(ventaSeleccionada.getTimestamp());
                        databaseReference.child("Venta").child(v.getIdVenta()).setValue(v);
                        Toast.makeText(this, "Actualizado Correctamente", Toast.LENGTH_SHORT).show();
                        linearLayoutEditar.setVisibility(View.GONE);
                        ventaSeleccionada = null;
                    } else {
                        Toast.makeText(this, "Selecciona una persona", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.menu_eliminar:
                if (ventaSeleccionada != null){
                    Venta v2 = new Venta();
                    v2.setIdVenta(ventaSeleccionada.getIdVenta());
                    databaseReference.child("Venta").child(v2.getIdVenta()).removeValue();
                    linearLayoutEditar.setVisibility(View.GONE);
                    ventaSeleccionada = null;
                    Toast.makeText(this, "Eliminado Correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Seleccione una persona para eliminar", Toast.LENGTH_SHORT).show();

                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertar(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(
                MainActivity.this
        );
        View mView = getLayoutInflater().inflate(R.layout.insertar, null);
        Button btnInsertar = (Button) mView.findViewById(R.id.btnInsertar);
        final EditText mInputNombres = (EditText) mView.findViewById(R.id.inputNombre);
        final EditText mInputPrenda = (EditText) mView.findViewById(R.id.inputPrenda);
        final EditText mInputPrecio = (EditText) mView.findViewById(R.id.inputPrecio);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombres = mInputNombres.getText().toString();
                String prendas = mInputPrenda.getText().toString();
                String precio = mInputPrecio.getText().toString();

                if (nombres.isEmpty() || nombres.length()<3){
                    showError(mInputNombres, "Nombre invalido (Min. 3 Letras)");
                } else if (prendas.isEmpty() || prendas.length()<5){
                    showError(mInputPrenda, "Nombre de Prenda invalido (Min. 5 Letras)");
                } else if (precio.isEmpty() || precio.length()<3) {
                    showError(mInputPrecio, "Nombre de Prenda invalido (Min. 3 Digitos)");
                } else {
                    Venta venta = new Venta();
                    venta.setIdVenta(UUID.randomUUID().toString());
                    venta.setNombreCliente(nombres);
                    venta.setPrenda(prendas);
                    venta.setTotalVenta(precio);

                    venta.setFecha(getFechaNormal(getFechaMilisegundos()));
                    venta.setTimestamp((getFechaMilisegundos() * -1));
                    databaseReference.child("Venta").child(venta.getIdVenta()).setValue(venta);
                    Toast.makeText(MainActivity.this, "La venta se registro correctamente", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
    }
    public void  showError(EditText input, String s){
        input.requestFocus();
        input.setError(s);
    }

    public long getFechaMilisegundos(){
        Calendar calendar = Calendar.getInstance();
        long tiempounix = calendar.getTimeInMillis();

        return tiempounix;
    }

    public String getFechaNormal(long fechamilisegundos){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fecha = sdf.format(fechamilisegundos);
        return fecha;
    }

    public boolean validarInputs(){
        String nombres = inputNombre.getText().toString();
        String prendas = inputPrenda.getText().toString();
        String precio = inputPrecio.getText().toString();

        if (nombres.isEmpty() || nombres.length()<3){
            showError(inputNombre, "Nombre invalido (Min. 3 Letras)");
            return true;
        } else if (prendas.isEmpty() || prendas.length()<5){
            showError(inputPrenda, "Nombre de Prenda invalido (Min. 5 Letras)");
            return true;
        } else if (precio.isEmpty() || precio.length()<3) {
            showError(inputPrecio, "Nombre de Prenda invalido (Min. 3 Digitos)");
            return true;
        } else {
            return  false;
        }
    }

    public void getUserInfo(){
        String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("name").getValue().toString();

                    textViewName.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}