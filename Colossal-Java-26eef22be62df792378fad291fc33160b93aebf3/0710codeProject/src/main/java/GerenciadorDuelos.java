import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class GerenciadorDuelos {
    private List<Duelo> duelosAgendados = new ArrayList<>();
    // Adicionado um Scanner estático para ler a entrada do usuário durante o duelo
    private static final Scanner scanner = new Scanner(System.in);

    public Duelo agendarDuelo(String modo, List<Mago> eq1, List<Mago> eq2, String arena, LocalDateTime data) {
        for (Duelo agendado : duelosAgendados) {
            if (agendado.getArena().equals(arena) && Math.abs(java.time.Duration.between(agendado.getDataHora(), data).toMinutes()) < 30) {
                System.out.println("ERRO DE AGENDAMENTO: Já existe um duelo nesta arena neste horário!");
                return null;
            }
        }
        Duelo novoDuelo = new Duelo(modo, eq1, eq2, arena, data);
        duelosAgendados.add(novoDuelo);
        System.out.println("Duelo " + modo + " agendado com sucesso para " + data.toLocalDate() + " às " + data.toLocalTime() + " na arena " + arena);
        return novoDuelo;
    }

    public void simularDuelo(Duelo duelo) {
        System.out.println("\n=== INICIANDO SIMULAÇÃO DE DUELO " + duelo.getModo().toUpperCase() + " ===");
        duelo.getEquipe1().forEach(Mago::restaurarParaDuelo);
        duelo.getEquipe2().forEach(Mago::restaurarParaDuelo);
        if ("1v1".equals(duelo.getModo()) || "Treino".equals(duelo.getModo())) {
            simularDuelo1v1(duelo);
        } else {
            simularDueloEquipes(duelo);
        }
    }

    private void simularDueloEquipes(Duelo duelo) {
        List<Mago> equipe1Vivos = new ArrayList<>(duelo.getEquipe1());
        List<Mago> equipe2Vivos = new ArrayList<>(duelo.getEquipe2());
        int rodada = 1;
        while (!equipe1Vivos.isEmpty() && !equipe2Vivos.isEmpty() && rodada <= 10) {
            System.out.printf("%n--- Rodada %d ---%n", rodada++);
            List<Mago> todosOsVivos = new ArrayList<>(equipe1Vivos);
            todosOsVivos.addAll(equipe2Vivos);
            for (Mago mago : todosOsVivos) { mago.processarEfeitosDoTurno(); }
            Collections.shuffle(todosOsVivos);
            for (Mago atacante : todosOsVivos) {
                if (!atacante.estaVivo()) continue;
                List<Mago> inimigosVivos = equipe1Vivos.contains(atacante) ? equipe2Vivos : equipe1Vivos;
                if (inimigosVivos.isEmpty()) break;
                executarAcaoDoMago(atacante, inimigosVivos, duelo);
            }
            equipe1Vivos.removeIf(m -> !m.estaVivo());
            equipe2Vivos.removeIf(m -> !m.estaVivo());
        }
        determinarVencedor(duelo);
    }
    
    private void simularDuelo1v1(Duelo duelo) {
        Mago mago1 = duelo.getEquipe1().get(0);
        Mago mago2 = duelo.getEquipe2().get(0);
        int rodada = 1;
        while (mago1.estaVivo() && mago2.estaVivo() && rodada <= 20) {
            System.out.printf("%n--- Rodada %d ---%n", rodada++);
            mago1.processarEfeitosDoTurno();
            mago2.processarEfeitosDoTurno();
            if (mago1.estaVivo()) executarAcaoDoMago(mago1, List.of(mago2), duelo);
            if (mago2.estaVivo()) executarAcaoDoMago(mago2, List.of(mago1), duelo);
        }
        determinarVencedor(duelo);
    }

    // ===================================================================================
    // MÉTODO CENTRAL MODIFICADO PARA ACEITAR CONTROLE DO USUÁRIO
    // ===================================================================================
    private void executarAcaoDoMago(Mago atacante, List<Mago> inimigosVivos, Duelo duelo) {
        if (atacante.possuiStatus(EfeitoStatus.ATORDOAMENTO)) {
            System.out.printf("%s está atordoado e perde a vez!%n", atacante.getCodinome());
            return;
        }

        Feitico feiticoEscolhido;
        List<Mago> alvosEscolhidos = new ArrayList<>();

        // Se o mago é controlado por IA, usa a lógica antiga
        if (atacante.getControlador().equals(Mago.IA)) {
            feiticoEscolhido = atacante.escolherFeitico(inimigosVivos);
            if (feiticoEscolhido == null) {
                if (!atacante.possuiStatus(EfeitoStatus.SILENCIO)) {
                     System.out.printf("%s está sem mana para agir.%n", atacante.getCodinome());
                }
                return;
            }
            System.out.printf("%s lança '%s'!%n", atacante.getCodinome(), feiticoEscolhido.getNome());

            if (feiticoEscolhido.getTipo().equals(Feitico.PROJETIL)) {
                alvosEscolhidos.add(escolherAlvoAleatorio(inimigosVivos));
            } else { // Feitiço em AREA
                System.out.printf("   > O feitiço atinge toda a Equipe Inimiga!%n");
                alvosEscolhidos.addAll(inimigosVivos);
            }
        
        // Se o mago é controlado por um HUMANO, exibe o menu de ação
        } else {
            System.out.printf("%n--== VEZ DE %s (Vida: %d/%d | Mana: %d/%d) ==--%n", 
                atacante.getCodinome().toUpperCase(), atacante.getVidaAtual(), atacante.getVidaMax(), atacante.getManaAtual(), atacante.getManaMax());

            if (atacante.possuiStatus(EfeitoStatus.SILENCIO)) {
                System.out.printf("%s está silenciado e não pode lançar feitiços!%n", atacante.getCodinome());
                return;
            }

            List<Feitico> feiticosDisponiveis = atacante.getFeiticosDisponiveis();
            if (feiticosDisponiveis.isEmpty()) {
                System.out.printf("%s não tem mana suficiente para nenhum feitiço.%n", atacante.getCodinome());
                return;
            }

            // Menu para escolha de feitiço
            System.out.println("Escolha um feitiço para lançar:");
            for (int i = 0; i < feiticosDisponiveis.size(); i++) {
                Feitico f = feiticosDisponiveis.get(i);
                System.out.printf(" %d: %s (Custo: %d Mana | Poder: %.0f)%n", i + 1, f.getNome(), f.getCustoMana(), f.getPoderBase());
            }
            
            int escolhaFeitico = -1;
            while(escolhaFeitico < 1 || escolhaFeitico > feiticosDisponiveis.size()) {
                System.out.print("Sua escolha: ");
                try {
                    escolhaFeitico = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida. Digite um número.");
                    scanner.next(); // Limpa o buffer do scanner
                }
            }
            feiticoEscolhido = feiticosDisponiveis.get(escolhaFeitico - 1);
            System.out.printf("%s lança '%s'!%n", atacante.getCodinome(), feiticoEscolhido.getNome());

            // Lógica para escolher o alvo
            if (feiticoEscolhido.getTipo().equals(Feitico.AREA)) {
                System.out.printf("   > O feitiço atinge toda a Equipe Inimiga!%n");
                alvosEscolhidos.addAll(inimigosVivos);
            } else { // Feitiço de PROJETIL
                if (inimigosVivos.size() == 1) {
                    alvosEscolhidos.add(inimigosVivos.get(0));
                } else {
                    System.out.println("Escolha um alvo:");
                     for (int i = 0; i < inimigosVivos.size(); i++) {
                        Mago inimigo = inimigosVivos.get(i);
                        System.out.printf(" %d: %s (Vida: %d/%d)%n", i + 1, inimigo.getCodinome(), inimigo.getVidaAtual(), inimigo.getVidaMax());
                    }
                    int escolhaAlvo = -1;
                    while(escolhaAlvo < 1 || escolhaAlvo > inimigosVivos.size()) {
                        System.out.print("Alvo: ");
                        try {
                           escolhaAlvo = scanner.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Entrada inválida. Digite um número.");
                            scanner.next();
                        }
                    }
                    alvosEscolhidos.add(inimigosVivos.get(escolhaAlvo - 1));
                }
            }
        }

        // Aplica o dano e efeitos (código comum para IA e Humano)
        atacante.consumirMana(feiticoEscolhido.getCustoMana());
        aplicarDano(atacante, alvosEscolhidos, feiticoEscolhido, duelo);
    }
    
    private void aplicarDano(Mago atacante, List<Mago> alvos, Feitico feitico, Duelo duelo) {
        for (Mago alvo : alvos) {
            if (!alvo.estaVivo()) continue;
            double danoBruto = feitico.getPoderBase() + atacante.getPoderBase();
            if (duelo.getArena().equals(Duelo.CIRCULO_RUNICO) && atacante.getEscola().equals("Arcano")) danoBruto *= 1.15;
            if(duelo.getArena().equals(Duelo.CAMARA_ANTIMAGIA)) danoBruto *= 0.8;
            double danoFinal = danoBruto * (1.0 - alvo.getResistenciaMagica() / 100.0);
            double danoMitigado = danoBruto - danoFinal;
            boolean estavaVivo = alvo.estaVivo();
            alvo.receberDano(danoFinal);
            duelo.adicionarDano(atacante, danoFinal);
            duelo.adicionarDanoMitigado(alvo, danoMitigado);
            duelo.setUltimoAtacante(alvo, atacante);
            System.out.printf("   > %s sofreu %.1f de dano! (Vida: %d/%d)%n", alvo.getCodinome(), danoFinal, alvo.getVidaAtual(), alvo.getVidaMax());
            if (estavaVivo && !alvo.estaVivo()) {
                System.out.printf("   > %s DERROTOU %s!%n", atacante.getCodinome().toUpperCase(), alvo.getCodinome().toUpperCase());
                duelo.adicionarAbate(atacante);
                duelo.registarAssistencias(alvo, atacante);
            }
            if (feitico.getEfeitoAplicado() != null && Math.random() < feitico.getChanceDeAplicarEfeito()) {
                alvo.adicionarEfeito(new EfeitoStatus(feitico.getEfeitoAplicado(), feitico.getDuracaoEfeito()));
            }
        }
    }

    private Mago escolherAlvoAleatorio(List<Mago> inimigos) { return inimigos.get((int) (Math.random() * inimigos.size())); }

    private void determinarVencedor(Duelo duelo) {
        long sobreviventesEq1 = duelo.getEquipe1().stream().filter(Mago::estaVivo).count();
        long sobreviventesEq2 = duelo.getEquipe2().stream().filter(Mago::estaVivo).count();
        String vencedor;
        if (sobreviventesEq1 > 0 && sobreviventesEq2 == 0) vencedor = "Equipe 1";
        else if (sobreviventesEq2 > 0 && sobreviventesEq1 == 0) vencedor = "Equipe 2";
        else {
            double danoTotal1 = 0; for (Mago mago : duelo.getEquipe1()) { danoTotal1 += duelo.getDanoCausado().get(mago); }
            double danoTotal2 = 0; for (Mago mago : duelo.getEquipe2()) { danoTotal2 += duelo.getDanoCausado().get(mago); }
            if (danoTotal1 > danoTotal2) vencedor = "Equipe 1 (por dano)";
            else if (danoTotal2 > danoTotal1) vencedor = "Equipe 2 (por dano)";
            else vencedor = "Empate";
        }
        duelo.setVencedor(vencedor);
        if (!"Treino".equals(duelo.getModo())) {
            duelo.setFinalizado(true);
        }
        System.out.println("\n=== FIM DA SIMULAÇÃO ===");
        System.out.println("O VENCEDOR É: " + duelo.getVencedor());
        System.out.println("\n--- PLACAR FINAL ---");
        System.out.println("Equipe 1:");
        duelo.getEquipe1().forEach(m -> System.out.printf("   %s - Dano: %.1f, Abates: %d%n", m.getCodinome(), duelo.getDanoCausado().get(m), duelo.getAbates().get(m)));
        System.out.println("Equipe 2:");
        duelo.getEquipe2().forEach(m -> System.out.printf("   %s - Dano: %.1f, Abates: %d%n", m.getCodinome(), duelo.getDanoCausado().get(m), duelo.getAbates().get(m)));
    }
    
    public List<Duelo> getDuelosAgendados() { return duelosAgendados.stream().filter(d -> !d.isFinalizado()).collect(Collectors.toList()); }
    public List<Duelo> getDuelosFinalizados() { return duelosAgendados.stream().filter(Duelo::isFinalizado).collect(Collectors.toList()); }
    public Duelo buscarDueloPorId(int id) {
        for(Duelo d : this.duelosAgendados) {
            if (d.getId() == id) return d;
        }
        return null;
    }

    public void gerarRankingDeMagos() {
        System.out.println("\n--- RANKING GERAL DE MAGOS ---");
        Map<Mago, int[]> stats = new HashMap<>(); // [0]=abates, [1]=dano
        for (Duelo d : getDuelosFinalizados()) {
            for (Map.Entry<Mago, Integer> entry : d.getAbates().entrySet()) { stats.computeIfAbsent(entry.getKey(), k -> new int[2])[0] += entry.getValue(); }
            for (Map.Entry<Mago, Double> entry : d.getDanoCausado().entrySet()) { stats.computeIfAbsent(entry.getKey(), k -> new int[2])[1] += entry.getValue().intValue(); }
        }
        if (stats.isEmpty()) { System.out.println("Nenhum duelo finalizado para gerar ranking."); return; }
        List<Mago> ranking = new ArrayList<>(stats.keySet());
        ranking.sort(Comparator.comparing((Mago m) -> stats.get(m)[0]).reversed().thenComparing(m -> stats.get(m)[1], Comparator.reverseOrder()));
        int pos = 1;
        for (Mago m : ranking) {
            System.out.printf("%d. %s (%s) - Abates: %d, Dano: %d%n", pos++, m.getCodinome(), m.getEscola(), stats.get(m)[0], stats.get(m)[1]);
        }
    }
    public void gerarTaxaDeVitoriaPorEscola() {
        System.out.println("\n--- TAXA DE VITÓRIA POR ESCOLA ---");
        List<Duelo> finalizados = getDuelosFinalizados();
        if (finalizados.isEmpty()) { System.out.println("Nenhum duelo finalizado."); return; }
        Map<String, int[]> stats = new HashMap<>(); // [0]=vitorias, [1]=participações
        for (Duelo d : finalizados) {
            if(d.getVencedor() == null || d.getVencedor().contains("Empate")) continue;
            List<Mago> vencedores = d.getVencedor().startsWith("Equipe 1") ? d.getEquipe1() : d.getEquipe2();
            List<Mago> todos = new ArrayList<>(d.getEquipe1());
            todos.addAll(d.getEquipe2());
            for (Mago m : todos) {
                stats.computeIfAbsent(m.getEscola(), k -> new int[2])[1]++;
                if(vencedores.contains(m)) stats.get(m.getEscola())[0]++;
            }
        }
        stats.forEach((escola, placar) -> {
            double taxa = (placar[1] == 0) ? 0 : ((double) placar[0] / placar[1]) * 100;
            System.out.printf("- %s: %.1f%% de vitória (%d vitórias em %d participações)%n", escola, taxa, placar[0], placar[1]);
        });
    }
    public void gerarMapaDeCalorDeHorarios() {
        System.out.println("\n--- MAPA DE CALOR DE HORÁRIOS DE DUELOS ---");
        if (duelosAgendados.isEmpty()) { System.out.println("Nenhum duelo agendado."); return; }
        int[] horas = new int[24];
        for(Duelo d : duelosAgendados) { horas[d.getDataHora().getHour()]++; }
        System.out.println("Contagem de duelos agendados por hora:");
        for(int i = 0; i < 24; i++) {
            System.out.printf("%02d:00 | ", i);
            for(int j = 0; j < horas[i]; j++) { System.out.print("■"); }
            System.out.println(" (" + horas[i] + ")");
        }
    }
}