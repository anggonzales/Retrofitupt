package com.example.retrofitupt;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface servicesRetrofit {
    @GET("clientes.php")//indicamos el metodo y el endpoint
    Call<List<Cliente>> getUsersGet();//Recuerda que debes colocar como recibiremos esos datos,en este caso una lista de objs

}
