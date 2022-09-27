/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.platzi.gatos_app;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Gomez
 */
public class GatosService {
    public static void verGatos() throws IOException{
//        primero vamos a traer los datos de la API
            OkHttpClient client = new OkHttpClient();

          Request request = new Request.Builder().url("https://api.thecatapi.com/v1/images/search").get().build();

          Response response = client.newCall(request).execute();

          String elJson = response.body().string();
            
//            Cortar los corchete de la respuesta a API
            elJson = elJson.substring(1, elJson.length());
            elJson = elJson.substring(0, elJson.length()-1);
            
//            Crear un objeto de la clase Gson
            Gson gson = new Gson();
            Gatos gatos = gson.fromJson(elJson, Gatos.class);
            
//            Redimensionamos la imagen en caso de ser necesario
            Image image = null;
            try {
                URL url = new URL(gatos.getUrl());
                image = ImageIO.read(url);
                ImageIcon fondoGato = new ImageIcon(image);
                if (fondoGato.getIconWidth() > 800) {
                    Image fondo = fondoGato.getImage();
                    Image modificada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                    fondoGato = new ImageIcon(modificada);
                }
                
                String menu = "Opciones: \n"
                        +"1. Ver otra Imagen\n"
                        +"2.Agregar a Favorito \n"
                        +"Salir";
                String[] botones = {"Ver otra Imagen", "Favorito","Volver"};
                
                String id_gato = gatos.getId();
                
                String opcion =(String) JOptionPane.showInputDialog(null,menu,id_gato, JOptionPane.INFORMATION_MESSAGE,
                        fondoGato, botones, botones[0]);
                int selecion = -1;
                
                for (int i = 0; i < botones.length; i++) {
                    if (opcion.equals(botones[i])) {
                        selecion = i;
                    }  
                }
                
                switch (selecion) {
                    case 0:
                        verGatos();
                        break;
                    case 1:
                        gatoFavorito(gatos);
                        break;
                    default:
                        break;
                }   
        } catch (Exception e) {
                System.out.println(e);
        }
    }
    
    public static void gatoFavorito(Gatos gato){
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n\t\"image_id\":\""+gato.getId()+"\"\n}");
            Request request = new Request.Builder()
              .url("https://api.thecatapi.com/v1/favourites")
              .post(body)
              .addHeader("Content-Type", "application/json")
              .addHeader("x-api-key", gato.getApi_key())
              .build();
            Response response = client.newCall(request).execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    
    public static void verFavoritos(String api_key) throws IOException{
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
          .url("https://api.thecatapi.com/v1/favourites")
          .get()
          .addHeader("Content-Type", "application/json")
          .addHeader("x-api-key", api_key)
          .build();

        Response response = client.newCall(request).execute();
        
//        parseamos la respuesta de la API
            String elJson = response.body().string();
            
           Gson gson = new Gson();
           
           GatosFav[] gatosFav = gson.fromJson(elJson, GatosFav[].class);
           
           if (gatosFav.length > 0) {
            int min = 1;
            int max= gatosFav.length;
            int aleatorio = (int) (Math.random() * ((max -min)+1))+ min;
            int indice = aleatorio-1;
            
            GatosFav gatoFav = gatosFav[indice];
            
//              Redimensionamos la imagen en caso de ser necesario
                    Image image = null;
                    try {
                        URL url = new URL(gatoFav.image.url);
                        image = ImageIO.read(url);
                        ImageIcon fondoGato = new ImageIcon(image);
                        if (fondoGato.getIconWidth() > 800) {
                            Image fondo = fondoGato.getImage();
                            Image modificada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                            fondoGato = new ImageIcon(modificada);
                        }

                        String menu = "Opciones: \n"
                                +"1. Ver otro Favorito\n"
                                +"2.Eliminar Favorito \n"
                                +"Salir";
                        String[] botones = {"Ver otra Imagen", "Eliminar Favorito","Volver"};

                        String id_gato = gatoFav.getId();

                        String opcion =(String) JOptionPane.showInputDialog(null,menu,id_gato, JOptionPane.INFORMATION_MESSAGE,
                                fondoGato, botones, botones[0]);
                        int selecion = -1;

                        for (int i = 0; i < botones.length; i++) {
                            if (opcion.equals(botones[i])) {
                                selecion = i;
                            }  
                        }

                        switch (selecion) {
                            case 0:
                                verFavoritos(api_key);
                                break;
                            case 1:
                                eliminarFavorito(gatoFav);
                                break;
                            default:
                                break;
                        }   
                } catch (Exception e) {
                        System.out.println(e);
                }
            
            
        }
    
    }
    
    
    public static void eliminarFavorito(GatosFav gatoFav){
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
              .url("https://api.thecatapi.com/v1/favourites/"+gatoFav.getId()+"")
              .delete(null)
              .addHeader("Content-Type", "application/json")
              .addHeader("x-api-key", gatoFav.getApi_key())
              .build();

            Response response = client.newCall(request).execute();
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
