import java.util.Scanner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        
        
           

        LocalDateTime hora = LocalDateTime.now();
        DateTimeFormatter dataformatada = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String data = hora.format(dataformatada);
        System.out.println("Hora inicializada: " + data);
        Mago m1 = new Mago("Default","Default",100,100,100,25,15,"Default","Tomo",0);
        Mago m2 = new Mago("Default","Default",100,100,100,25,15,"Default","Tomo",0);
        Mago m3 = new Mago("Default","Default",100,100,100,25,15,"Default","Tomo",0);  
        Feitico bolaFogo = new Feitico("Bola de Fogo", "Projetil", "Elemental", 20, 3.0, 25.0);
        Feitico nevasca = new Feitico("Nevasca", "Area", "Elemental", 40, 8.0, 30.0);
        Feitico drenagemVida = new Feitico("Drenagem de Vida", "Canalizado", "Sombrio", 30, 5.0, 20.0);
        /*Bem manual ainda, não possui o loop, porém fase de teste */
        System.out.println("{==Registro Mago==}");
        System.out.println("Escolha seu ID: ");
            String id = teclado.nextLine();
        System.out.println("Escolha seu Codinome:");    
            String codinome = teclado.nextLine();
        System.out.println("Escolha sua Escola");
            String escola = teclado.nextLine();
        m1.setId(id);
        m1.setEscola(escola);
        m1.setCodinome(codinome);
        System.out.println("Você é o " + m1.getCodinome());
        /*Bem manual ainda, não possui o loop, porém fase de teste */
        System.out.println("{==Registro Mago==}");
        System.out.println("Escolha seu ID: ");
            String id2 = teclado.nextLine();
        System.out.println("Escolha seu Codinome:");    
            String codinome2 = teclado.nextLine();
        System.out.println("Escolha sua Escola");
            String escola2 = teclado.nextLine();
        m2.setId(id2);
        m2.setEscola(escola2);
        m2.setCodinome(codinome2);
        System.out.println("Você é o " + m2.getCodinome());
        /*Bem manual ainda, não possui o loop, porém fase de teste */

        System.out.println("{==Registro Mago==}");
        System.out.println("Escolha seu ID: ");
            String id3 = teclado.nextLine();
        System.out.println("Escolha seu Codinome:");    
            String codinome3 = teclado.nextLine();
        System.out.println("Escolha sua Escola");
            String escola3 = teclado.nextLine();
        m3.setId(id3);
        m3.setEscola(escola3);
        m3.setCodinome(codinome3);
        System.out.println("Você é o " + m3.getCodinome());


        

        
        System.out.println("=== FEITIÇO PROJÉTIL ===");
        m3.utilizarFeitico(bolaFogo, m2);//feitico e alvo
        System.out.println("Vida do " + m2.getCodinome() + ": " + m2.getVida());
        
        System.out.println("\n=== FEITIÇO DE ÁREA ===");
        ArrayList<Personagem> alvosArea = new ArrayList<>();
        alvosArea.add(m2);
        alvosArea.add(m1);//adicionei os alvos manualmente, seria bom adiconar uma funçao
        
        m3.utilizarFeiticoArea(nevasca, alvosArea);
        System.out.println("Vida do " + m2.getCodinome() + ": " + m2.getVida());
        System.out.println("Vida do " + m1.getCodinome() + ": " + m1.getVida());
        
        System.out.println("\n=== FEITIÇO CANALIZADO ===");
        m3.utilizarFeiticoCanalizado(drenagemVida, m2, 1);
        m3.utilizarFeiticoCanalizado(drenagemVida, m2, 0);
        System.out.println("Vida do " + m2.getCodinome() + ": " + m2.getVida());

        
    }
}