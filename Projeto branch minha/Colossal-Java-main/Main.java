
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        LocalDateTime hora = LocalDateTime.now();
        DateTimeFormatter dataformatada = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String data = hora.format(dataformatada);
        System.out.println("Hora inicializada: " + data);
        Mago merlin = new Mago("M001", "Merlin", 10000, 100, 100000, 30, 20, "Arcano", "Cajado", 0);
        Mago darkMage = new Mago("M002", "Sombrio", 80, 80, 80, 25, 15, "Sombrio", "Tomo", 0);
        Mago elementalMage = new Mago("M003", "Elementalista", 90, 90, 90, 28, 18, "Elemental", "Vara", 0);
        
        Feitico bolaFogo = new Feitico("Bola de Fogo", "Projetil", "Elemental", 20, 3.0, 25.0);
        Feitico nevasca = new Feitico("Nevasca", "Area", "Elemental", 40, 8.0, 30.0);
        Feitico drenagemVida = new Feitico("Drenagem de Vida", "Canalizado", "Sombrio", 30, 5.0, 20.0);
        
        System.out.println("=== FEITIÇO PROJÉTIL ===");
        merlin.utilizarFeitico(bolaFogo, darkMage);//feitico e alvo
        System.out.println("Vida do " + darkMage.getCodinome() + ": " + darkMage.getVida());
        
        System.out.println("\n=== FEITIÇO DE ÁREA ===");
        ArrayList<Personagem> alvosArea = new ArrayList<>();
        alvosArea.add(darkMage);
        alvosArea.add(elementalMage);//adicionei os alvos manualmente, seria bom adiconar uma funçao
        
        merlin.utilizarFeiticoArea(nevasca, alvosArea);
        System.out.println("Vida do " + darkMage.getCodinome() + ": " + darkMage.getVida());
        System.out.println("Vida do " + elementalMage.getCodinome() + ": " + elementalMage.getVida());
        
        System.out.println("\n=== FEITIÇO CANALIZADO ===");
        merlin.utilizarFeiticoCanalizado(drenagemVida, darkMage, 1);
        merlin.utilizarFeiticoCanalizado(drenagemVida, darkMage, 0);
        System.out.println("Vida do " + darkMage.getCodinome() + ": " + darkMage.getVida());
    }
}