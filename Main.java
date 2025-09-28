package com.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {

        LocalDateTime hora = LocalDateTime.now();
        DateTimeFormatter dataformatada = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        String data = hora.format(dataformatada);

        System.out.println("Hora inicializada: " + data);
        /* Inicializando a Hora */

        Mago m1 = new Mago("data", "a", 10, 1, 2, 4000, 19, "elemental", "foco mil", 100);

        Feitico f1 = new Feitico("Fogo_Rosa", "Elemental", "Fodase", 330, 20, 01);
        Feitico fumaum = new Feitico("Sexo", "Elemental", "Fodase", 330, 20, 01);

        m1.aprenderFeitico(m1, f1);
        m1.aprenderFeitico(m1, fumaum);

        m1.listarFeitico();
        m1.utilizarFeitico(fumaum);

    }
}