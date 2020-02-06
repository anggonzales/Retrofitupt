package com.example.retrofitupt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {
    Retrofit retrofit;
    servicesRetrofit miserviceretrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        Call<List<Cliente>> call = miserviceretrofit.getUsersGet();
//Apartir de aqui la forma cambia de la manera sincrona a la asincrona
//basicamente mandamos a llamar el metodo enqueue, y le pasamos como parametro el call back
//Recuerda que el IDE es para ayudarte asi que lo creara automaticamente al escribir "new"
        call.enqueue(new Callback<List<Cliente>>() {
            //Metodo que se ejecutara cuando no hay problemas y obtenemos respuesta del server
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
//Exactamente igual a la manera sincrona,la respuesta esta en el body
                for(Cliente res : response.body()) {
                    Log.e("Usuario: ",res.getNombre()+" "+res.getApellido());
                }
            }
            //Metodo que se ejecutara cuando ocurrio algun problema
            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                Log.e("onFailure",t.toString());// mostrmos el error
            }
        });
    }

    public void ingresar(View view) {
        EditText user=findViewById(R.id.miuser);
        EditText pass=findViewById(R.id.mipass);
        Call<String> call = miserviceretrofit.getLoginGet(user.getText().toString(),pass.getText().toString());
        call.enqueue(new Callback<String>() {
            //Metodo que se ejecutara cuando no hay problemas y obtenemos respuesta del server
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            //Exactamente igual a la manera sincrona,la respuesta esta en el body
                String mirespuesta=response.body();
                Log.e("milogin: ",response.body());

                if(mirespuesta.equals("success"))
                    startActivity(new Intent(MainActivity.this, InsertarCliente.class));
                else{
                    Toast.makeText(MainActivity.this, "Ingreso Fallido", Toast.LENGTH_LONG);
                }
            }
            //Metodo que se ejecutara cuando ocurrio algun problema
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("milogin",t.toString());// mostrmos el error
            }
        });

    }

    public static class Peticion extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
//Url del servicio,sin el endpoint
            final String url = "https://notogaea-decoration.000webhostapp.com/";
//Creamos el objeto Retrofit
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)//Indicamos la url del servicio
                    .addConverterFactory(GsonConverterFactory.create())//Agregue la fábrica del convertidor para la serialización
// y la deserialización de objetos.
                    .build();//Cree la instancia de Retrofit utilizando los valores configurados.
//https://square.github.io/retrofit/2.x/retrofit/retrofit2/Retrofit.Builder.html
            servicesRetrofit service = retrofit.create(servicesRetrofit.class);
            Log.e("respuesta", "doInBackground");
//Recuerda que debemos colocar el modo en como obtenemos esa respuesta,en este caso es una lista de objetos
//pero puede ser simplemente un objeto.

            Call<List<Cliente>> response = service.getUsersGet();//indicamos el metodo que deseamos ejecutar
            try {
//Realizamos la peticion sincrona
//Recuerda que en el body se encuentra la respuesta,que en este caso es una lista de objetos
                for (Cliente user : response.execute().body())//realizamos un foreach para recorrer la lista
                    Log.e("Respuesta: ",user.getNombre()+ " "+user.getApellido());//mostamos en pantalla algunos de los datos del usuario
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
