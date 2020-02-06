package com.example.retrofitupt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//Ejecutamos Retrofit en modo sincrono,para no bloquear el hilo principal,ocupamos AsyncTask para colocar la
//tarea en el background
        new Peticion().execute();
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
