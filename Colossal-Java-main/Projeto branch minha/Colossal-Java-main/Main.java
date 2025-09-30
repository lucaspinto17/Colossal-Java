import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== TORNEIO DOS 5 MAGOS ===");
        
        // Criando 5 magos de escolas diferentes
        Mago merlin = new Mago("M001", "Merlin", 150, 200, 200, 30, 20, "Arcano", "Cajado", 5);
        Mago morgana = new Mago("M002", "Morgana", 120, 180, 180, 25, 25, "Sombrio", "Tomo", 5);
        Mago ignis = new Mago("M003", "Ignis", 130, 220, 190, 35, 15, "Elemental", "Vara", 5);
        Mago arcane = new Mago("M004", "Arcane", 140, 190, 210, 28, 22, "Arcano", "Cajado", 5);
        Mago umbra = new Mago("M005", "Umbra", 110, 170, 170, 22, 28, "Sombrio", "Tomo", 5);

        // Usando setEscola para alterar escolas de alguns magos
        System.out.println("\n--- CONFIGURANDO ESCOLAS ---");
        ignis.setEscola("Elemental"); // Já é Elemental, mas mostra o print(SER)
        arcane.setEscola("Arcano");   // Já é Arcano, mas mostra o print
        umbra.setEscola("Sombrio");   // Já é Sombrio, mas mostra o print
        merlin.setEscola("Elemental");
        morgana.setEscola("Arcano");

        // Criando feitiços
        Feitico bolaFogo = new Feitico("Bola de Fogo", "Projetil", "Elemental", 30, 2.0, 40);
        Feitico raioGelado = new Feitico("Raio Gelado", "Projetil", "Elemental", 25, 1.5, 35);
        Feitico tempestade = new Feitico("Tempestade Arcana", "Area", "Arcano", 50, 3.0, 60);
        Feitico drenarVida = new Feitico("Drenar Vida", "Canalizado", "Sombrio", 40, 4.0, 45);
        Feitico explosao = new Feitico("Explosão Elemental", "Area", "Elemental", 45, 2.5, 55);
        Feitico misselArcano = new Feitico("Míssel Arcano", "Projetil", "Arcano", 35, 2.0, 50);

        // Magos aprendem feitiços
        System.out.println("\n--- APRENDENDO FEITIÇOS ---");
        merlin.aprenderFeitico(bolaFogo);
        merlin.aprenderFeitico(explosao);
        
        morgana.aprenderFeitico(misselArcano);
        morgana.aprenderFeitico(tempestade);
        
        ignis.aprenderFeitico(bolaFogo);
        ignis.aprenderFeitico(explosao);
        ignis.aprenderFeitico(drenarVida);
        
        arcane.aprenderFeitico(misselArcano);
        arcane.aprenderFeitico(tempestade);
        arcane.aprenderFeitico(raioGelado);
        
        umbra.aprenderFeitico(drenarVida);
        umbra.aprenderFeitico(raioGelado);

        // Listando feitiços dos magos
        System.out.println("\n--- GRIMÓRIOS DOS MAGOS ---");
        System.out.println("Merlin (Elemental):");
        merlin.listarFeitico();
        
        System.out.println("\nMorgana (Arcano):");
        morgana.listarFeitico();
        
        System.out.println("\nIgnis (Elemental):");
        ignis.listarFeitico();
        
        System.out.println("\nArcane (Arcano):");
        arcane.listarFeitico();
        
        System.out.println("\nUmbra (Sombrio):");
        umbra.listarFeitico();

        // BATALHA EM EQUIPES
        System.out.println("\n=== INÍCIO DA BATALHA ===");
        System.out.println("Time Elemental: Merlin e Ignis");
        System.out.println("Time Arcano: Morgana e Arcane");
        System.out.println("Time Sombrio: Umbra");


        merlin.utilizarFeitico(bolaFogo, morgana);
        morgana.utilizarFeitico(misselArcano, merlin);

        ArrayList<Personagem> alvosArcano = new ArrayList<>();//tenho que botar isso no metodo
        alvosArcano.add(morgana);
        alvosArcano.add(arcane);
        ignis.utilizarFeiticoArea(explosao, alvosArcano);
        
        ArrayList<Personagem> alvosElemental = new ArrayList<>();//botar no metodo
        alvosElemental.add(merlin);
        alvosElemental.add(ignis);
        arcane.utilizarFeiticoArea(tempestade, alvosElemental);
        
        umbra.utilizarFeitico(drenarVida, ignis);

        // Turno 2 - Aplicando estados
        System.out.println("\n--- TURNO 2 ---");
        arcane.aplicarSilencio(2);
        
        arcane.utilizarFeitico(misselArcano, umbra);
        
        merlin.aplicarAtordoamento(1);
        
        merlin.utilizarFeitico(bolaFogo, morgana);
        
        ignis.utilizarFeitico(bolaFogo, umbra);

        System.out.println("\n--- TURNO 3 ---");
        morgana.utilizarFeitico(misselArcano, ignis);
        umbra.ativarEscudoSombrio();
        ignis.utilizarFeitico(drenarVida, umbra);

        System.out.println("\n--- TURNO 4 ---");
        morgana.aplicarManaBurn(2);
        
        ArrayList<Personagem> alvosTodos = new ArrayList<>();//mesma coisa
        alvosTodos.add(morgana);
        alvosTodos.add(arcane);
        alvosTodos.add(umbra);
        merlin.utilizarFeiticoArea(explosao, alvosTodos);

        // Processar fim dos turnos
        System.out.println("\n--- PROCESSANDO FIM DOS TURNOS ---");//CHAT BOTOU MAS ACHEI ESTRANHO, TALVEZ NAO PRECISE
        merlin.processarFimTurno();
        morgana.processarFimTurno();
        ignis.processarFimTurno();
        arcane.processarFimTurno();
        umbra.processarFimTurno();

        // Status final(é legal criar metodo pra mostrar)
        System.out.println("\n=== STATUS FINAL ===");
        System.out.println("Merlin - Vida: " + merlin.getVida() + ", Mana: " + merlin.getManaAtual() + ", Vivo: " + merlin.estaVivo());
        System.out.println("Morgana - Vida: " + morgana.getVida() + ", Mana: " + morgana.getManaAtual() + ", Vivo: " + morgana.estaVivo());
        System.out.println("Ignis - Vida: " + ignis.getVida() + ", Mana: " + ignis.getManaAtual() + ", Vivo: " + ignis.estaVivo());
        System.out.println("Arcane - Vida: " + arcane.getVida() + ", Mana: " + arcane.getManaAtual() + ", Vivo: " + arcane.estaVivo());
        System.out.println("Umbra - Vida: " + umbra.getVida() + ", Mana: " + umbra.getManaAtual() + ", Vivo: " + umbra.estaVivo());

    }
}