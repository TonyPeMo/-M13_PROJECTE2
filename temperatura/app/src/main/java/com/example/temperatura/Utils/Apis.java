package com.example.temperatura.Utils;

public class Apis {

    public static final String URL_001="http://192.168.17.183:8080/registros/";

    public static RegistroService getPersonaService(){
        return  Cliente.getClient(URL_001).create(RegistroService.class);
    }
}