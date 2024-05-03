package com.example.temperatura.Utils;

import com.example.temperatura.Model.Registro;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RegistroService {

    @GET("listar")
    Call<List<Registro>> getRegistros();

    @POST("agregar")
    Call<Registro>addRegistro(@Body Registro registro);

    @POST("actualizar/{id}")
    Call<Registro>updateRegistro(@Body Registro registro,@Path("id") int id);

    @POST("eliminar/{id}")
    Call<Registro>deleteRegistro(@Path("id")int id);

}