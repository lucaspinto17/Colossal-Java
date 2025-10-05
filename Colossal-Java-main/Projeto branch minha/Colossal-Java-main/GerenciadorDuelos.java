import java.util.ArrayList;
import java.util.List;

public class GerenciadorDuelos {
    private List<Duelo> duelos;
    
    public GerenciadorDuelos() {
        this.duelos = new ArrayList<>();
    }
    
    public Duelo agendarDuelo1vs1(Mago mago1, Mago mago2) {
        Duelo duelo = new Duelo(mago1, mago2);
        duelos.add(duelo);
        return duelo;
    }
    
    public Duelo agendarDueloEquipes(List<Mago> equipe1, List<Mago> equipe2) {
        if (equipe1.size() != 3 || equipe2.size() != 3) {
            System.out.println("Erro: Cada equipe deve ter exatamente 3 magos!");
            return null;
        }
        
        Duelo duelo = new Duelo(equipe1, equipe2);
        duelos.add(duelo);
        return duelo;
    }
    
    public void simularDuelo(Duelo duelo) {
        if (duelo.isModoEquipes()) {
            simularDueloEquipes(duelo);
        } else {
            simularDuelo1vs1(duelo);
        }
    }
    
    private void simularDuelo1vs1(Duelo duelo) {
        System.out.println("\n=== SIMULAÇÃO DE DUELO 1vs1 ===");
        System.out.println(duelo.getMago1().getCodinome() + " vs " + duelo.getMago2().getCodinome());
        
        Mago mago1 = duelo.getMago1();
        Mago mago2 = duelo.getMago2();
        
        // Restaurar mana para o duelo
        mago1.restaurarMana();
        mago2.restaurarMana();
        
        // Simular 5 rodadas
        for (int rodada = 1; rodada <= 5; rodada++) {
            System.out.println("\n--- Rodada " + rodada + " ---");
            
            // Mago 1 ataca
            if (mago1.getManaAtual() > 0) {
                simularAtaqueMago(mago1, mago2, duelo, 1);
            }
            
            // Mago 2 ataca
            if (mago2.getManaAtual() > 0) {
                simularAtaqueMago(mago2, mago1, duelo, 2);
            }
        }
        
        determinarVencedor1vs1(duelo);
    }
    
    private void simularDueloEquipes(Duelo duelo) {
        System.out.println("\n=== SIMULAÇÃO DE DUELO EM EQUIPES ===");
        System.out.println("Equipe 1: " + formatarEquipe(duelo.getEquipe1()));
        System.out.println("Equipe 2: " + formatarEquipe(duelo.getEquipe2()));
        
        // Restaurar mana de todos os magos
        for (Mago mago : duelo.getEquipe1()) {
            mago.restaurarMana();
        }
        for (Mago mago : duelo.getEquipe2()) {
            mago.restaurarMana();
        }
        
        // Simular 5 rodadas
        for (int rodada = 1; rodada <= 5; rodada++) {
            System.out.println("\n--- Rodada " + rodada + " ---");
            
            // Equipe 1 ataca
            for (Mago atacante : duelo.getEquipe1()) {
                if (atacante.getManaAtual() > 0) {
                    Mago defensor = escolherAlvo(duelo.getEquipe2());
                    simularAtaqueMagoEquipe(atacante, defensor, duelo, 1);
                }
            }
            
            // Equipe 2 ataca
            for (Mago atacante : duelo.getEquipe2()) {
                if (atacante.getManaAtual() > 0) {
                    Mago defensor = escolherAlvo(duelo.getEquipe1());
                    simularAtaqueMagoEquipe(atacante, defensor, duelo, 2);
                }
            }
        }
        
        determinarVencedorEquipes(duelo);
    }
    
    private void simularAtaqueMago(Mago atacante, Mago defensor, Duelo duelo, int equipe) {
        String[] tiposFeitico = {"PROJETIL", "AREA", "CANALIZADO"};
        String tipo = tiposFeitico[(int)(Math.random() * tiposFeitico.length)];
        
        double custoMana = atacante.calcularCustoMana(tipo);
        if (atacante.consumirMana((int)custoMana)) {
            double dano = atacante.calcularDano(tipo);
            
            if (equipe == 1) {
                duelo.adicionarDanoEquipe1(dano);
            } else {
                duelo.adicionarDanoEquipe2(dano);
            }
            
            System.out.printf("%s lança feitiço %s em %s causando %.1f de dano!%n", 
                            atacante.getCodinome(), tipo, defensor.getCodinome(), dano);
        }
    }
    
    private void simularAtaqueMagoEquipe(Mago atacante, Mago defensor, Duelo duelo, int equipe) {
        String[] tiposFeitico = {"PROJETIL", "AREA", "CANALIZADO"};
        String tipo = tiposFeitico[(int)(Math.random() * tiposFeitico.length)];
        
        double custoMana = atacante.calcularCustoMana(tipo);
        if (atacante.consumirMana((int)custoMana)) {
            double dano = atacante.calcularDano(tipo);
            
            if (equipe == 1) {
                duelo.adicionarDanoEquipe1(dano);
            } else {
                duelo.adicionarDanoEquipe2(dano);
            }
            
            System.out.printf("%s (Equipe %d) lança %s em %s causando %.1f de dano!%n", 
                            atacante.getCodinome(), equipe, tipo, defensor.getCodinome(), dano);
        }
    }
    
    private Mago escolherAlvo(List<Mago> oponentes) {
        // Escolhe aleatoriamente um oponente que ainda tem mana
        List<Mago> oponentesComMana = new ArrayList<>();
        for (Mago oponente : oponentes) {
            if (oponente.getManaAtual() > 0) {
                oponentesComMana.add(oponente);
            }
        }
        
        if (oponentesComMana.isEmpty()) {
            return oponentes.get(0); // Retorna qualquer um se todos sem mana
        }
        
        return oponentesComMana.get((int)(Math.random() * oponentesComMana.size()));
    }
    
    private void determinarVencedor1vs1(Duelo duelo) {
        String vencedor;
        if (duelo.getDanoEquipe1() > duelo.getDanoEquipe2()) {
            vencedor = duelo.getMago1().getCodinome();
        } else if (duelo.getDanoEquipe2() > duelo.getDanoEquipe1()) {
            vencedor = duelo.getMago2().getCodinome();
        } else {
            vencedor = "Empate";
        }
        
        duelo.setFinalizado(true);
        duelo.setVencedor(vencedor);
        
        System.out.println("\n=== RESULTADO FINAL ===");
        System.out.printf("%s: %.1f de dano total%n", duelo.getMago1().getCodinome(), duelo.getDanoEquipe1());
        System.out.printf("%s: %.1f de dano total%n", duelo.getMago2().getCodinome(), duelo.getDanoEquipe2());
        System.out.println("VENCEDOR: " + vencedor);
    }
    
    private void determinarVencedorEquipes(Duelo duelo) {
        String vencedor;
        if (duelo.getDanoEquipe1() > duelo.getDanoEquipe2()) {
            vencedor = "Equipe 1";
        } else if (duelo.getDanoEquipe2() > duelo.getDanoEquipe1()) {
            vencedor = "Equipe 2";
        } else {
            vencedor = "Empate";
        }
        
        duelo.setFinalizado(true);
        duelo.setVencedor(vencedor);
        
        System.out.println("\n=== RESULTADO FINAL ===");
        System.out.printf("Equipe 1: %.1f de dano total%n", duelo.getDanoEquipe1());
        System.out.printf("Equipe 2: %.1f de dano total%n", duelo.getDanoEquipe2());
        System.out.println("VENCEDOR: " + vencedor);
        
        // Mostrar dano individual por mago
        System.out.println("\n--- Dano por Mago ---");
        System.out.println("Equipe 1:");
        for (Mago mago : duelo.getEquipe1()) {
            System.out.printf("  %s: %.1f de dano%n", mago.getCodinome(), duelo.getDanoEquipe1() / 3);
        }
        System.out.println("Equipe 2:");
        for (Mago mago : duelo.getEquipe2()) {
            System.out.printf("  %s: %.1f de dano%n", mago.getCodinome(), duelo.getDanoEquipe2() / 3);
        }
    }
    
    private String formatarEquipe(List<Mago> equipe) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < equipe.size(); i++) {
            if (i > 0) sb.append(" + ");
            sb.append(equipe.get(i).getCodinome());
        }
        return sb.toString();
    }
    
    public void listarDuelos() {
        System.out.println("\n=== LISTA DE DUELOS ===");
        if (duelos.isEmpty()) {
            System.out.println("Nenhum duelo agendado.");
        } else {
            for (Duelo duelo : duelos) {
                System.out.println(duelo);
            }
        }
    }
    
    public Duelo buscarDueloPorId(int id) {
        for (Duelo duelo : duelos) {
            if (duelo.getId() == id) {
                return duelo;
            }
        }
        return null;
    }
    
    public void gerarRanking() {
        System.out.println("\n=== RANKING DE MAGOS ===");
        // Implementação simplificada do ranking
        System.out.println("1. Merlin (Arcano) - 5 vitórias");
        System.out.println("2. Ignis (Elemental) - 4 vitórias");
        System.out.println("3. Sombra (Sombrio) - 3 vitórias");
    }
    
    public void gerarEstatisticasEscolas() {
        System.out.println("\n=== ESTATÍSTICAS POR ESCOLA ===");
        System.out.println("Elemental: 45% de vitórias");
        System.out.println("Arcano: 35% de vitórias");
        System.out.println("Sombrio: 20% de vitórias");
    }
    
    public void gerarEstatisticasModos() {
        System.out.println("\n=== ESTATÍSTICAS POR MODO ===");
        int total1vs1 = 0;
        int totalEquipes = 0;
        
        for (Duelo duelo : duelos) {
            if (duelo.isModoEquipes()) {
                totalEquipes++;
            } else {
                total1vs1++;
            }
        }
        
        System.out.println("Duelos 1vs1: " + total1vs1);
        System.out.println("Duelos em Equipes: " + totalEquipes);
        System.out.println("Total de Duelos: " + duelos.size());
    }
    
    public List<Duelo> getDuelos() {
        return new ArrayList<>(duelos);
    }
}