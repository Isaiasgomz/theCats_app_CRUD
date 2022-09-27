/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.platzi.gatos_app;

import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Gomez
 */
public class Gatos_app {

    public static void main(String[] args) throws IOException {
        int opcion_menu = -1;
        String[] botones = {"1.Ver gatos", "2. Ver Favoritos", "Salir"};
        
        do {
        
           String opcion =(String) JOptionPane.showInputDialog(null, "Gatitios Java", "Mrnu Principal",
                   JOptionPane.INFORMATION_MESSAGE,null, botones, botones[0]);
            for (int i = 0; i < botones.length; i++) {
                if (opcion.equals(botones[i])) {
                    opcion_menu = i;
                }
                switch (opcion_menu) {
                    case 0:
                        GatosService.verGatos();
                        break;
                    case 1:
                        Gatos gato = new Gatos();
                       GatosService.verFavoritos(gato.api_key);
                    default:
                        break;
                }
            }
        } while (opcion_menu != 1);
    }
}
