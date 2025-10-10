import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class GerenciadorDuelos {
    private List<Duelo> duelosAgendados = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    
    // ... (Os métodos mais simples como agendarDuelo, buscarDueloPorId, etc. não
    // foram comentados em detalhe por serem autoexplicativos) ...

    public Duelo agendarDuelo(String modo, List<Mago> eq1, List<Mago> eq2, String arena, LocalDateTime data) {
        for (Duelo agendado : duelosAgendados) {
            if (agendado.getArena().equals(arena)
                    && Math.abs(java.time.Duration.between(agendado.getDataHora(), data).toMinutes()) < 30) {
                System.out.println("ERRO DE AGENDAMENTO: Já existe um duelo nesta arena neste horário!");
                return null;
            }
        }
        Duelo novoDuelo = new Duelo(modo, eq1, eq2, arena, data);
        duelosAgendados.add(novoDuelo);
       
        System.out.println("Duelo " + modo + " agendado com sucesso para " + data.toLocalDate() + " às "
                + data.toLocalTime() + " na arena " + arena);
        return novoDuelo;
    }

    public void simularDuelo(Duelo duelo) {
        System.out.println("\n=== INICIANDO SIMULAÇÃO DE DUELO " + duelo.getModo().toUpperCase() + " ===");
        duelo.getEquipe1().forEach(Mago::restaurarParaDuelo);
        duelo.getEquipe2().forEach(Mago::restaurarParaDuelo);
        duelo.setCondicaoCampoAtual(CondicaoCampo.NORMAL);

        if ("1v1".equals(duelo.getModo()) || "Treino".equals(duelo.getModo())) {
            simularDuelo1v1(duelo);
        } else {
            simularDueloEquipes(duelo);
        }
    }

    /**
     * Atualiza a condição de campo do duelo de forma aleatória.
     * Existe uma chance de 30% a cada rodada para que o campo mude.
     * 
     * @param duelo O duelo em andamento.
     */
    private void atualizarCondicaoDeCampo(Duelo duelo) {
        // nextDouble() gera um número entre 0.0 e 1.0. Se for menor que 0.3, a condição
        // muda (30% de chance).
        if (random.nextDouble() < 0.3) {
            // Cria uma lista com todas as condições de campo possíveis.
            String[] condicoes = { CondicaoCampo.TEMPESTADE_ARCANA, CondicaoCampo.CHUVA_ELEMENTAL,
                    CondicaoCampo.NUVEM_SOMBRIA, CondicaoCampo.NORMAL };
            // Sorteia uma nova condição da lista.
            String novaCondicao = condicoes[random.nextInt(condicoes.length)];

            // Verifica se a nova condição é diferente da atual para evitar mensagens
            // repetidas.
            if (!duelo.getCondicaoCampoAtual().equals(novaCondicao)) {
                // Define a nova condição no objeto do duelo.
                duelo.setCondicaoCampoAtual(novaCondicao);
                // Anuncia a mudança para o jogador.
                System.out.printf(">> O campo de batalha mudou! Condição atual: %s <<%n", novaCondicao);
            }
        }
    }

    /**
     * Controla o fluxo de um duelo de equipes (ex: 3v3).
     * Gerencia rodadas, turnos, e verifica a condição de vitória.
     * 
     * @param duelo O duelo a ser simulado.
     */
    private void simularDueloEquipes(Duelo duelo) {
        // Listas separadas para controlar os magos vivos de cada equipe.
        List<Mago> equipe1Vivos = new ArrayList<>(duelo.getEquipe1());
        List<Mago> equipe2Vivos = new ArrayList<>(duelo.getEquipe2());
        int rodada = 1;

        // O loop principal do duelo. Continua enquanto houver magos vivos em ambas as
        // equipes.
        while (!equipe1Vivos.isEmpty() && !equipe2Vivos.isEmpty() && rodada <= 10) {
            System.out.printf("%n--- Rodada %d ---%n", rodada++);

            // No início de cada rodada, chama a função que pode mudar a condição do campo.
            atualizarCondicaoDeCampo(duelo);

            // Junta todos os magos vivos em uma única lista para processar os turnos.
            List<Mago> todosOsVivos = new ArrayList<>(equipe1Vivos);
            todosOsVivos.addAll(equipe2Vivos);

            // Processa efeitos de status (como queima de mana, veneno, etc.) em todos os
            // magos antes dos turnos.
            for (Mago mago : todosOsVivos) {
                mago.processarEfeitosDoTurno();
            }

            // Embaralha a lista de magos para que a ordem dos turnos seja aleatória a cada
            // rodada.
            Collections.shuffle(todosOsVivos);

            // Itera sobre cada mago vivo para que ele realize sua ação.
            for (Mago atacante : todosOsVivos) {
                // Se um mago foi derrotado no meio da rodada, ele não joga.
                if (!atacante.estaVivo())
                    continue;

                // Define qual é a equipe inimiga do atacante.
                List<Mago> inimigosVivos = equipe1Vivos.contains(atacante) ? equipe2Vivos : equipe1Vivos;

                // Se não há mais inimigos, encerra a rodada.
                if (inimigosVivos.isEmpty())
                    break;

                // Executa a ação do mago (controlado por IA ou Humano).
                executarAcaoDoMago(atacante, inimigosVivos, duelo);
            }

            // Ao final da rodada, remove os magos derrotados das listas de vivos.
            equipe1Vivos.removeIf(m -> !m.estaVivo());
            equipe2Vivos.removeIf(m -> !m.estaVivo());
        }
        // Quando o loop termina (uma equipe foi derrotada ou o tempo acabou), define o
        // vencedor.
        determinarVencedor(duelo);
    }

    private void simularDuelo1v1(Duelo duelo) {
        // A lógica do 1v1 é similar, mas mais simples, sem a necessidade de listas
        // complexas.
        Mago mago1 = duelo.getEquipe1().get(0);
        Mago mago2 = duelo.getEquipe2().get(0);
        int rodada = 1;
        while (mago1.estaVivo() && mago2.estaVivo() && rodada <= 20) {
            System.out.printf("%n--- Rodada %d ---%n", rodada++);

            atualizarCondicaoDeCampo(duelo);

            mago1.processarEfeitosDoTurno();
            mago2.processarEfeitosDoTurno();

            // Cada mago tem seu turno, se ainda estiver vivo.
            if (mago1.estaVivo())
                executarAcaoDoMago(mago1, List.of(mago2), duelo);
            if (mago2.estaVivo())
                executarAcaoDoMago(mago2, List.of(mago1), duelo);
        }
        determinarVencedor(duelo);
    }

    /**
     * Executa a ação de um único mago em seu turno.
     * Este método diferencia a lógica para magos controlados por IA e por Humanos.
     * 
     * @param atacante      O mago que está jogando.
     * @param inimigosVivos A lista de alvos possíveis.
     * @param duelo         O duelo atual.
     */
    private void executarAcaoDoMago(Mago atacante, List<Mago> inimigosVivos, Duelo duelo) {
        // Verifica se o mago está atordoado. Se estiver, ele perde a vez.
        if (atacante.possuiStatus(EfeitoStatus.ATORDOAMENTO)) {
            System.out.printf("%s está atordoado e perde a vez!%n", atacante.getCodinome());
            return;
        }

        Feitico feiticoEscolhido;
        List<Mago> alvosEscolhidos = new ArrayList<>();

        // ---- LÓGICA DA INTELIGÊNCIA ARTIFICIAL (IA) ----
        if (atacante.getControlador().equals(Mago.IA)) {
            // Pede para a IA do Mago escolher um feitiço baseado em sua estratégia (ex:
            // Agressivo).
            feiticoEscolhido = atacante.escolherFeitico(inimigosVivos);

            // Se a IA não escolheu feitiço (sem mana ou silenciado), o turno acaba.
            if (feiticoEscolhido == null) {
                if (!atacante.possuiStatus(EfeitoStatus.SILENCIO)) {
                    System.out.printf("%s está sem mana para agir.%n", atacante.getCodinome());
                }
                return;
            }
            System.out.printf("%s lança '%s'!%n", atacante.getCodinome(), feiticoEscolhido.getNome());

            // Se o feitiço é um projétil, a IA escolhe um alvo aleatório.
            if (feiticoEscolhido.getTipo().equals(Feitico.PROJETIL)) {
                alvosEscolhidos.add(escolherAlvoAleatorio(inimigosVivos));
            } else { // Se for em área, atinge todos os inimigos.
                System.out.printf("   > O feitiço atinge toda a Equipe Inimiga!%n");
                alvosEscolhidos.addAll(inimigosVivos);
            }

            // ---- LÓGICA DO JOGADOR HUMANO ----
        } else {
            // Mostra o status atual do mago do jogador para ajudar na decisão.
            System.out.printf("%n--== VEZ DE %s (Vida: %d/%d | Mana: %d/%d) ==--%n",
                    atacante.getCodinome().toUpperCase(), atacante.getVidaAtual(), atacante.getVidaMax(),
                    atacante.getManaAtual(), atacante.getManaMax());

            // Verifica se o jogador está silenciado.
            if (atacante.possuiStatus(EfeitoStatus.SILENCIO)) {
                System.out.printf("%s está silenciado e não pode lançar feitiços!%n", atacante.getCodinome());
                return;
            }

            // Obtém a lista de feitiços que o jogador pode pagar com a mana atual.
            List<Feitico> feiticosDisponiveis = atacante.getFeiticosDisponiveis();
            if (feiticosDisponiveis.isEmpty()) {
                System.out.printf("%s não tem mana suficiente para nenhum feitiço.%n", atacante.getCodinome());
                return;
            }

            // Mostra um menu numerado com os feitiços disponíveis.
            System.out.println("Escolha um feitiço para lançar:");
            for (int i = 0; i < feiticosDisponiveis.size(); i++) {
                Feitico f = feiticosDisponiveis.get(i);
                System.out.printf(" %d: %s (Custo: %d Mana | Poder: %.0f)%n", i + 1, f.getNome(), f.getCustoMana(),
                        f.getPoderBase());
            }

            // Loop para garantir que o jogador digite uma opção válida.
            int escolhaFeitico = -1;
            while (escolhaFeitico < 1 || escolhaFeitico > feiticosDisponiveis.size()) {
                System.out.print("Sua escolha: ");
                try {
                    escolhaFeitico = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida. Digite um número.");
                    scanner.next(); // Limpa o buffer do scanner para evitar loop infinito.
                }
            }
            // Pega o feitiço escolhido da lista (ajustando o índice, pois a lista começa em
            // 0).
            feiticoEscolhido = feiticosDisponiveis.get(escolhaFeitico - 1);
            System.out.printf("%s lança '%s'!%n", atacante.getCodinome(), feiticoEscolhido.getNome());

            // Lógica para o jogador escolher o alvo.
            if (feiticoEscolhido.getTipo().equals(Feitico.AREA)) {
                System.out.printf("   > O feitiço atinge toda a Equipe Inimiga!%n");
                alvosEscolhidos.addAll(inimigosVivos);
            } else { // Se for projétil.
                if (inimigosVivos.size() == 1) { // Se só há um inimigo, ele é o alvo automático.
                    alvosEscolhidos.add(inimigosVivos.get(0));
                } else { // Se há múltiplos inimigos, mostra um menu de alvos.
                    System.out.println("Escolha um alvo:");
                    for (int i = 0; i < inimigosVivos.size(); i++) {
                        Mago inimigo = inimigosVivos.get(i);
                        System.out.printf(" %d: %s (Vida: %d/%d)%n", i + 1, inimigo.getCodinome(),
                                inimigo.getVidaAtual(), inimigo.getVidaMax());
                    }
                    // Loop para garantir que o jogador escolha um alvo válido.
                    int escolhaAlvo = -1;
                    while (escolhaAlvo < 1 || escolhaAlvo > inimigosVivos.size()) {
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

        // Após IA ou Humano decidirem, o resultado é processado da mesma forma.
        atacante.consumirMana(feiticoEscolhido.getCustoMana());
        aplicarDano(atacante, alvosEscolhidos, feiticoEscolhido, duelo);
    }

    /**
     * Calcula e aplica o dano de um feitiço em um ou mais alvos.
     * Considera poder base, bônus de arena, bônus de condição de campo e
     * resistência do alvo.
     * 
     * @param atacante O mago que lançou o feitiço.
     * @param alvos    A lista de magos que serão atingidos.
     * @param feitico  O feitiço utilizado.
     * @param duelo    O duelo atual.
     */
    private void aplicarDano(Mago atacante, List<Mago> alvos, Feitico feitico, Duelo duelo) {
        // Itera sobre cada alvo que foi atingido.
        for (Mago alvo : alvos) {
            if (!alvo.estaVivo())
                continue;

            // 1. Calcula o dano base (poder do feitiço + poder do mago).
            double danoBruto = feitico.getPoderBase() + atacante.getPoderBase();

            // 2. Aplica modificadores da ARENA (permanente do duelo).
            if (duelo.getArena().equals(Duelo.CIRCULO_RUNICO) && atacante.getEscola().equals("Arcano"))
                danoBruto *= 1.15;
            if (duelo.getArena().equals(Duelo.CAMARA_ANTIMAGIA))
                danoBruto *= 0.8;

            // 3. Aplica modificadores da CONDIÇÃO DE CAMPO (temporária da rodada).
            String condicao = duelo.getCondicaoCampoAtual();
            if (condicao.equals(CondicaoCampo.TEMPESTADE_ARCANA) && atacante.getEscola().equals("Arcano")) {
                System.out.printf("   > A Tempestade Arcana fortalece '%s'!%n", feitico.getNome());
                danoBruto *= 1.25;
            } else if (condicao.equals(CondicaoCampo.CHUVA_ELEMENTAL) && atacante.getEscola().equals("Elemental")) {
                System.out.printf("   > A Chuva Elemental fortalece '%s'!%n", feitico.getNome());
                danoBruto *= 1.25;
            }

            // 4. Calcula o dano final com base na resistência mágica do alvo.
            double danoFinal = danoBruto * (1.0 - alvo.getResistenciaMagica() / 100.0);
            double danoMitigado = danoBruto - danoFinal;

            boolean estavaVivo = alvo.estaVivo(); // Guarda se o alvo estava vivo ANTES do dano.

            // 5. Aplica o dano ao alvo e registra as estatísticas.
            alvo.receberDano(danoFinal);
            duelo.adicionarDano(atacante, danoFinal);
            duelo.adicionarDanoMitigado(alvo, danoMitigado);
            duelo.setUltimoAtacante(alvo, atacante); // Registra quem foi o último a atacar este alvo (para
                                                     // assistências).
            System.out.printf("   > %s sofreu %.1f de dano! (Vida: %d/%d)%n", alvo.getCodinome(), danoFinal,
                    alvo.getVidaAtual(), alvo.getVidaMax());

            // 6. Verifica se o alvo foi derrotado e registra o abate/assistência.
            if (estavaVivo && !alvo.estaVivo()) {
                System.out.printf("   > %s DERROTOU %s!%n", atacante.getCodinome().toUpperCase(),
                        alvo.getCodinome().toUpperCase());
                duelo.adicionarAbate(atacante);
                duelo.registarAssistencias(alvo, atacante);
            }

            // 7. Calcula e aplica efeitos de status (Atordoar, Silêncio, etc.).
            double chanceEfeito = feitico.getChanceDeAplicarEfeito();
            // Bônus da condição de campo para magos sombrios.
            if (condicao.equals(CondicaoCampo.NUVEM_SOMBRIA) && atacante.getEscola().equals("Sombrio")) {
                System.out.printf("   > A Nuvem Sombria aprimora o efeito de '%s'!%n", feitico.getNome());
                chanceEfeito += 0.20;
            }

            // Rola um "dado" para ver se o efeito é aplicado.
            if (feitico.getEfeitoAplicado() != null && random.nextDouble() < chanceEfeito) {
                alvo.adicionarEfeito(new EfeitoStatus(feitico.getEfeitoAplicado(), feitico.getDuracaoEfeito()));
            }
        }
    }

    private Mago escolherAlvoAleatorio(List<Mago> inimigos) {
        return inimigos.get(random.nextInt(inimigos.size()));
    }

    /**
     * Determina o vencedor do duelo após o fim do combate.
     * A condição primária é ter sobreviventes. A secundária é o total de dano
     * causado.
     * 
     * @param duelo O duelo que acabou de ser finalizado.
     */
    private void determinarVencedor(Duelo duelo) {
        // Conta quantos magos sobreviveram em cada equipe.
        long sobreviventesEq1 = duelo.getEquipe1().stream().filter(Mago::estaVivo).count();
        long sobreviventesEq2 = duelo.getEquipe2().stream().filter(Mago::estaVivo).count();
        String vencedor;

        // Se uma equipe tem sobreviventes e a outra não, a vitória é clara.
        if (sobreviventesEq1 > 0 && sobreviventesEq2 == 0)
            vencedor = "Equipe 1";
        else if (sobreviventesEq2 > 0 && sobreviventesEq1 == 0)
            vencedor = "Equipe 2";
        else { // Se ambas as equipes têm sobreviventes (ou nenhuma), o critério é o dano.
            double danoTotal1 = 0;
            for (Mago mago : duelo.getEquipe1()) {
                danoTotal1 += duelo.getDanoCausado().get(mago);
            }
            double danoTotal2 = 0;
            for (Mago mago : duelo.getEquipe2()) {
                danoTotal2 += duelo.getDanoCausado().get(mago);
            }

            if (danoTotal1 > danoTotal2)
                vencedor = "Equipe 1 (por dano)";
            else if (danoTotal2 > danoTotal1)
                vencedor = "Equipe 2 (por dano)";
            else
                vencedor = "Empate";
        }

        duelo.setVencedor(vencedor);
        if (!"Treino".equals(duelo.getModo())) {
            duelo.setFinalizado(true);
            System.out.println("Salvando resultados do duelo no banco de dados...");

        }

        // Exibe o resultado e o placar final.
        System.out.println("\n=== FIM DA SIMULAÇÃO ===");
        System.out.println("O VENCEDOR É: " + duelo.getVencedor());
        System.out.println("\n--- PLACAR FINAL ---");
        System.out.println("Equipe 1:");
        duelo.getEquipe1().forEach(m -> System.out.printf("   %s - Dano: %.1f, Abates: %d%n", m.getCodinome(),
                duelo.getDanoCausado().get(m), duelo.getAbates().get(m)));
        System.out.println("Equipe 2:");
        duelo.getEquipe2().forEach(m -> System.out.printf("   %s - Dano: %.1f, Abates: %d%n", m.getCodinome(),
                duelo.getDanoCausado().get(m), duelo.getAbates().get(m)));
    }

    public List<Duelo> getDuelosAgendados() {
        return duelosAgendados.stream().filter(d -> !d.isFinalizado()).collect(Collectors.toList());
    }

    public List<Duelo> getDuelosFinalizados() {
        return duelosAgendados.stream().filter(Duelo::isFinalizado).collect(Collectors.toList());
    }

    public Duelo buscarDueloPorId(int id) {
        for (Duelo d : this.duelosAgendados) {
            if (d.getId() == id)
                return d;
        }
        return null;
    }

    /**
     * Gera e exibe um ranking de magos baseado em abates e, como critério de
     * desempate, dano total.
     */
    public void gerarRankingDeMagos() {
        System.out.println("\n--- RANKING GERAL DE MAGOS ---");
        // Um 'Map' para armazenar as estatísticas de cada mago. A chave é o Mago, o
        // valor é um array [abates, dano].
        Map<Mago, int[]> stats = new HashMap<>();

        // Itera sobre todos os duelos finalizados para coletar os dados.
        for (Duelo d : getDuelosFinalizados()) {
            // Adiciona os abates de cada mago ao mapa.
            for (Map.Entry<Mago, Integer> entry : d.getAbates().entrySet()) {
                stats.computeIfAbsent(entry.getKey(), k -> new int[2])[0] += entry.getValue();
            }
            // Adiciona o dano de cada mago ao mapa.
            for (Map.Entry<Mago, Double> entry : d.getDanoCausado().entrySet()) {
                stats.computeIfAbsent(entry.getKey(), k -> new int[2])[1] += entry.getValue().intValue();
            }
        }

        if (stats.isEmpty()) {
            System.out.println("Nenhum duelo finalizado para gerar ranking.");
            return;
        }

        // Cria uma lista com todos os magos que participaram de duelos.
        List<Mago> ranking = new ArrayList<>(stats.keySet());

        // Ordena a lista. O critério principal é o número de abates (stats[0]), em
        // ordem decrescente.
        // O critério secundário (desempate) é o dano (stats[1]), também decrescente.
        ranking.sort(Comparator.comparing((Mago m) -> stats.get(m)[0]).reversed()
                .thenComparing(m -> stats.get(m)[1], Comparator.reverseOrder()));

        // Exibe o ranking formatado.
        int pos = 1;
        for (Mago m : ranking) {
            System.out.printf("%d. %s (%s) - Abates: %d, Dano: %d%n", pos++, m.getCodinome(), m.getEscola(),
                    stats.get(m)[0], stats.get(m)[1]);
        }
    }

    // ... (Os demais métodos de relatórios não foram comentados em detalhe) ...
    public void gerarTaxaDeVitoriaPorEscola() {
        System.out.println("\n--- TAXA DE VITÓRIA POR ESCOLA ---");
        List<Duelo> finalizados = getDuelosFinalizados();
        if (finalizados.isEmpty()) {
            System.out.println("Nenhum duelo finalizado.");
            return;
        }
        Map<String, int[]> stats = new HashMap<>(); // [0]=vitorias, [1]=participações
        for (Duelo d : finalizados) {
            if (d.getVencedor() == null || d.getVencedor().contains("Empate"))
                continue;
            List<Mago> vencedores = d.getVencedor().startsWith("Equipe 1") ? d.getEquipe1() : d.getEquipe2();
            List<Mago> todos = new ArrayList<>(d.getEquipe1());
            todos.addAll(d.getEquipe2());
            for (Mago m : todos) {
                stats.computeIfAbsent(m.getEscola(), k -> new int[2])[1]++;
                if (vencedores.contains(m))
                    stats.get(m.getEscola())[0]++;
            }
        }
        stats.forEach((escola, placar) -> {
            double taxa = (placar[1] == 0) ? 0 : ((double) placar[0] / placar[1]) * 100;
            System.out.printf("- %s: %.1f%% de vitória (%d vitórias em %d participações)%n", escola, taxa, placar[0],
                    placar[1]);
        });
    }

    /**
     * Gera e exibe um "mapa de calor" em formato de texto.
     * Este mapa é um histograma que mostra a quantidade de duelos agendados para
     * cada hora do dia,
     * ajudando a visualizar os horários de pico de atividade no coliseu.
     */
    public void gerarMapaDeCalorDeHorarios() {
        // Imprime o título do relatório.
        System.out.println("\n--- MAPA DE CALOR DE HORÁRIOS DE DUELOS ---");

        // Verificação inicial: se não houver duelos, informa o usuário e encerra a
        // função.
        if (duelosAgendados.isEmpty()) {
            System.out.println("Nenhum duelo agendado.");
            return; // O 'return' para a execução do método aqui.
        }

        // Cria um array de inteiros com 24 posições (índices de 0 a 23).
        // Cada posição representará uma hora do dia e armazenará a contagem de duelos
        // naquela hora.
        // Por padrão, todos os valores começam em 0.
        int[] horas = new int[24];

        // Percorre a lista de TODOS os duelos agendados para popular o array 'horas'.
        for (Duelo d : duelosAgendados) {
            // Para cada duelo 'd':
            // 1. Pega a data e hora do duelo com 'd.getDataHora()'.
            // 2. Extrai apenas a HORA (um número de 0 a 23) com '.getHour()'.
            // 3. Usa essa hora como ÍNDICE do array 'horas'.
            // 4. Incrementa (++) o valor naquela posição.
            // Exemplo: Se um duelo é às 15:30, horas[15] será incrementado.
            horas[d.getDataHora().getHour()]++;
        }

        System.out.println("Contagem de duelos agendados por hora:");

        // Agora, percorre o array 'horas' do índice 0 ao 23 para exibir os dados.
        for (int i = 0; i < 24; i++) {
            // Imprime a etiqueta da hora formatada. O "%02d" garante que a hora tenha dois
            // dígitos (ex: "07:00" em vez de "7:00").
            System.out.printf("%02d:00 | ", i);

            // Este é o loop que desenha a "barra de calor".
            // Ele vai repetir o número de vezes correspondente à contagem de duelos para a
            // hora 'i'.
            for (int j = 0; j < horas[i]; j++) {
                // A cada repetição, imprime um caractere de bloco, construindo a barra visual.
                System.out.print("■");
            }

            // Após desenhar a barra, imprime a contagem numérica e quebra a linha para a
            // próxima hora.
            System.out.println(" (" + horas[i] + ")");
        }
    }
}
