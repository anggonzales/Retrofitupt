package com.example.retrofitupt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class InsertarCliente extends AppCompatActivity {

    @BindView(R.id.nombre)
    EditText edtnombre;
    @BindView(R.id.apellido)
    EditText edtapellido;
    @BindView(R.id.sexo)
    Spinner spsexo;
    @BindView(R.id.direccion)
    EditText edtdireccion;
    @BindView(R.id.telefono)
    EditText edttelefono;
    Retrofit retrofit;
    servicesRetrofit miserviceretrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_cliente);
        ButterKnife.bind(this);
        final String url = "https://notogaea-decoration.000webhostapp.com/";
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        miserviceretrofit = retrofit.create(servicesRetrofit.class);
    }

    public void Insertar(View view) {
        int isexo=(spsexo.getSelectedItem().toString().equals("Masculino"))?0:1;
        Call<String> call = miserviceretrofit.registercliente(
                new ClienteInsertar(edtnombre.getText().toString(),
                        edtapellido.getText().toString(),
                        isexo,edtdireccion.getText().toString(),
                        edttelefono.getText().toString()));
        call.enqueue(new Callback<String>() {
            //Metodo que se ejecutara cuando no hay problemas y obtenemos respuesta del server
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//Exactamente igual a la manera sincrona,la respuesta esta en el body
                Log.e("miinsertar: ","check:"+response.body().toString());
                String mirespuesta=response.body();

                String respuesta = "check:OK\\n";
                if(mirespuesta.equals(respuesta))
                    Toast.makeText(getApplicationContext(), "Registro insertado",Toast.LENGTH_SHORT).show();
                else
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(getApplicationContext(), "Ingreso fallido",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            //Metodo que se ejecutara cuando ocurrio algun problema
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("miinsertar",t.toString());// mostrmos el error
            }
        });
    }
}
