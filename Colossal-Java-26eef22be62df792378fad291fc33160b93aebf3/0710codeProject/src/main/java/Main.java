import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        Database.inicializarBanco();
        DueloDAO dueloDAO = new DueloDAO();
    
        MagoDAO magoDAO = new MagoDAO();

        GerenciadorMagos gm = new GerenciadorMagos();
        Scanner scanner = new Scanner(System.in);
        GerenciadorMagos gerenciadorMagos = new GerenciadorMagos();
        GerenciadorDuelos gerenciadorDuelos = new GerenciadorDuelos();
        cadastrarMagosExemplo(gerenciadorMagos);

        boolean executando = true;
        while (executando) {
            exibirMenuPrincipal();
            int opcao = lerInteiro(scanner);
            switch (opcao) {
                case 1:
                    menuGerirMagos(scanner, gerenciadorMagos);
                    break;
                case 2:
                    menuGerirDuelos(scanner, gerenciadorMagos, gerenciadorDuelos);
                    break;
                case 3:
                    menuRelatorios(scanner, gerenciadorDuelos);
                    break;
                case 0:
                    executando = false;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
        System.out.println("A sair do Coliseu de Magos. Até à próxima!");
        scanner.close();
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n//=============================================\\\\");
        System.out.println("||        MENU PRINCIPAL DO COLISEU            ||");
        System.out.println("\\\\=============================================//");
        System.out.println("  1. Gestão de Magos");
        System.out.println("  2. Gestão de Duelos");
        System.out.println("  3. Relatórios e Estatísticas");
        System.out.println("  0. Sair do Jogo");
        System.out.print("  Escolha uma opção: ");
    }

    private static void menuGerirMagos(Scanner scanner, GerenciadorMagos gm) {
        System.out.println("\n--- Gestão de Magos ---");
        System.out.println("1. Cadastrar Novo Mago");
        System.out.println("2. Listar Magos Cadastrados");
        System.out.println("0. Voltar ao Menu Principal");
        System.out.print("  Escolha uma opção: ");
        int opcao = lerInteiro(scanner);
        switch (opcao) {
            case 1:
                cadastrarMago(scanner, gm);
                break;
            case 2:
                gm.listarMagos();
                break;
            case 0:
                return;
            default:
                System.out.println("Opção inválida.");
        }
    }

    private static void menuGerirDuelos(Scanner scanner, GerenciadorMagos gm, GerenciadorDuelos gd) {
        System.out.println("\n--- Gestão de Duelos ---");
        System.out.println("1. Agendar Novo Duelo");
        System.out.println("2. Listar Duelos Agendados");
        System.out.println("3. Simular Duelo Agendado");
        System.out.println("4. Iniciar Duelo de Treino (1v1 vs IA)");
        System.out.println("0. Voltar ao Menu Principal");
        System.out.print("  Escolha uma opção: ");
        int opcao = lerInteiro(scanner);
        switch (opcao) {
            case 1:
                agendarDuelo(scanner, gm, gd);
                break;
            case 2:
                listarDuelos(gd, "AGENDADOS");
                break;
            case 3:
                simularDueloAgendado(scanner, gd);
                break;
            case 4:
                simularTreino(scanner, gm, gd);
                break;
            case 0:
                return;
            default:
                System.out.println("Opção inválida.");
        }
    }

    private static void menuRelatorios(Scanner scanner, GerenciadorDuelos gd) {
        System.out.println("\n--- Relatórios de Desempenho ---");
        System.out.println("1. Ranking Geral de Magos");
        System.out.println("2. Taxa de Vitória por Escola");
        System.out.println("3. Mapa de Calor de Horários");
        System.out.println("4. Listar Duelos Finalizados");
        System.out.println("0. Voltar ao Menu Principal");
        System.out.print("  Escolha uma opção: ");
        int opcao = lerInteiro(scanner);
        switch (opcao) {
            case 1:
                gd.gerarRankingDeMagos();
                break;
            case 2:
                gd.gerarTaxaDeVitoriaPorEscola();
                break;
            case 3:
                gd.gerarMapaDeCalorDeHorarios();
                break;
            case 4:
                listarDuelos(gd, "FINALIZADOS");
                break;
            case 0:
                return;
            default:
                System.out.println("Opção inválida.");
        }
    }

    private static void cadastrarMago(Scanner scanner, GerenciadorMagos gerenciador) {
        System.out.println("\n--- Cadastro de Mago ---");
        System.out.print("ID (número único): ");
        int id = lerInteiro(scanner);
        if (gerenciador.buscarMagoPorId(id) != null) {
            System.out.println("Erro: ID já existe!");
            return;
        }
        System.out.print("Codinome: ");
        String codinome = scanner.nextLine();
        System.out.println("Escola: 1-Arcano, 2-Elemental, 3-Sombrio");
        int esc = lerInteiro(scanner);
        System.out.println("Foco: 1-Vara, 2-Cajado, 3-Tomo");
        int f = lerInteiro(scanner);
        String foco = f == 1 ? Mago.VARA : (f == 2 ? Mago.CAJADO : Mago.TOMO);
        System.out.println("Controlador: 1-Humano, 2-IA");
        int c = lerInteiro(scanner);
        String ctrl = c == 1 ? Mago.HUMANO : Mago.IA;
        String perfilIA = ctrl.equals(Mago.IA) ? Mago.IA_OPORTUNISTA : null;
        if (ctrl.equals(Mago.IA)) {
            System.out.println("Perfil da IA: 1-Agressivo, 2-Oportunista (Padrão)");
            int p = lerInteiro(scanner);
            perfilIA = p == 1 ? Mago.IA_AGRESSIVO : Mago.IA_OPORTUNISTA;
        }
        System.out.print("Mana Máxima: ");
        int mana = lerInteiro(scanner);
        System.out.print("Poder Base: ");
        double poder = lerDouble(scanner);
        System.out.print("Resistência Mágica: ");
        double res = lerDouble(scanner);
        Mago novoMago = null;
        switch (esc) {
            case 1:
                novoMago = new MagoArcano(id, codinome, mana, poder, res, foco, ctrl, perfilIA);
                break;
            case 2:
                novoMago = new MagoElemental(id, codinome, mana, poder, res, foco, ctrl, perfilIA);
                break;
            case 3:
                novoMago = new MagoSombrio(id, codinome, mana, poder, res, foco, ctrl, perfilIA);
                break;
            default:
                System.out.println("Escola inválida!");
                return;
        }
        if (gerenciador.cadastrarMago(novoMago))
            System.out.println("\n==> Mago '" + codinome + "' cadastrado com sucesso!");
    }

    private static void agendarDuelo(Scanner scanner, GerenciadorMagos gerMagos, GerenciadorDuelos gerDuelos) {
        System.out.println("\n--- Agendar Novo Duelo ---");
        System.out.println("Modo: 1-(1v1), 2-(3v3)");
        int modoNum = lerInteiro(scanner);
        String modo = modoNum == 1 ? "1v1" : "3v3";
        int tamanhoEquipe = modoNum == 1 ? 1 : 3;
        if (gerMagos.getQuantidadeMagos() < tamanhoEquipe * 2) {
            System.out.println("Não há magos suficientes para este modo!");
            return;
        }
        gerMagos.listarMagos();
        List<Mago> equipe1 = montarEquipe(scanner, gerMagos, "Equipe 1", tamanhoEquipe, new ArrayList<>());
        if (equipe1 == null) {
            System.out.println("Agendamento cancelado.");
            return;
        }
        List<Mago> equipe2 = montarEquipe(scanner, gerMagos, "Equipe 2", tamanhoEquipe, equipe1);
        if (equipe2 == null) {
            System.out.println("Agendamento cancelado.");
            return;
        }
        System.out.println("Arena: 1-Círculo Rúnico, 2-Labirinto Ilusório, 3-Câmara Antimagia");
        int arenaNum = lerInteiro(scanner);
        String arena = arenaNum == 1 ? Duelo.CIRCULO_RUNICO
                : (arenaNum == 2 ? Duelo.LABIRINTO_ILUSORIO : Duelo.CAMARA_ANTIMAGIA);
        LocalDateTime dataHora = null;
        while (dataHora == null) {
            System.out.print("Data e Hora do Duelo (formato dd/MM/yyyy HH:mm) ou 'agora': ");
            String dataStr = scanner.nextLine();
            if (dataStr.equalsIgnoreCase("agora")) {
                dataHora = LocalDateTime.now();
                break;
            }
            try {
                dataHora = LocalDateTime.parse(dataStr, FORMATADOR_DATA);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido! Tente novamente.");
            }
        }
        gerDuelos.agendarDuelo(modo, equipe1, equipe2, arena, dataHora);
    }

    private static void simularDueloAgendado(Scanner scanner, GerenciadorDuelos gd) {
        System.out.println("\n--- Simular Duelo Agendado ---");
        listarDuelos(gd, "AGENDADOS");
        List<Duelo> agendados = gd.getDuelosAgendados();
        if (agendados.isEmpty())
            return;
        System.out.print("Digite o ID do duelo que deseja simular: ");
        int id = lerInteiro(scanner);
        Duelo duelo = gd.buscarDueloPorId(id);
        if (duelo != null && !duelo.isFinalizado()) {
            gd.simularDuelo(duelo);
        } else {
            System.out.println("ID de duelo inválido ou o duelo já foi finalizado.");
        }
    }

    /**
     * Lógica para um duelo rápido 1v1 contra uma IA aleatória.
     * VERSÃO CORRIGIDA para o erro "effectively final".
     */
    private static void simularTreino(Scanner scanner, GerenciadorMagos gm, GerenciadorDuelos gd) {
        System.out.println("\n--- MODO DE TREINO (1v1 vs IA) ---");
        gm.listarMagos();

        Mago jogadorEscolhido = null;
        while (jogadorEscolhido == null) {
            System.out.print("Escolha o ID do seu Mago: ");
            int idJogador = lerInteiro(scanner);
            jogadorEscolhido = gm.buscarMagoPorId(idJogador);
            if (jogadorEscolhido == null)
                System.out.println("ID inválido!");
        }

        // ======================= INÍCIO DA CORREÇÃO =======================
        //
        // Criamos uma nova variável 'final' que recebe o valor final de
        // 'jogadorEscolhido'.
        // É esta nova variável que será usada dentro do stream.
        final Mago jogadorFinal = jogadorEscolhido;

        List<Mago> iasDisponiveis = gm.getMagos().stream()
                // Usamos a variável 'jogadorFinal' aqui, que nunca muda de valor.
                .filter(m -> m.getControlador().equals(Mago.IA) && m != jogadorFinal)
                .collect(Collectors.toList());
        //
        // ======================== FIM DA CORREÇÃO =========================

        if (iasDisponiveis.isEmpty()) {
            System.out.println("Não há outros magos controlados por IA para treino.");
            return;
        }

        Mago oponenteIA = iasDisponiveis.get(RANDOM.nextInt(iasDisponiveis.size()));
        System.out.printf("O seu oponente será: %s%n", oponenteIA.getCodinome());

        Duelo treino = new Duelo("Treino", List.of(jogadorFinal), List.of(oponenteIA), Duelo.CIRCULO_RUNICO,
                LocalDateTime.now());
        gd.simularDuelo(treino);

        System.out.println("\n==> Treino finalizado. Os resultados não afetam as estatísticas gerais.");
    }

    private static void listarDuelos(GerenciadorDuelos gd, String tipo) {
        List<Duelo> lista = tipo.equals("AGENDADOS") ? gd.getDuelosAgendados() : gd.getDuelosFinalizados();
        System.out.println("\n--- Duelos " + tipo + " ---");
        if (lista.isEmpty()) {
            System.out.println("Nenhum duelo para mostrar.");
        } else {
            lista.forEach(System.out::println);
        }
    }

    private static List<Mago> montarEquipe(Scanner scanner, GerenciadorMagos gerMagos, String nomeEquipe, int tamanho,
            List<Mago> jaEscolhidos) {
        List<Mago> equipe = new ArrayList<>();
        System.out.printf("%n-- Monte a %s -- (digite 0 para cancelar)%n", nomeEquipe);
        while (equipe.size() < tamanho) {
            System.out.printf("Digite o ID do Mago %d/%d: ", equipe.size() + 1, tamanho);
            int id = lerInteiro(scanner);
            if (id == 0)
                return null;
            Mago mago = gerMagos.buscarMagoPorId(id);
            if (mago != null && !jaEscolhidos.contains(mago) && !equipe.contains(mago)) {
                equipe.add(mago);
                System.out.printf(" > %s adicionado.%n", mago.getCodinome());
            } else {
                System.out.println(" > ID inválido ou mago já escolhido.");
            }
        }
        return equipe;
    }

    private static int lerInteiro(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Entrada inválida. Por favor, digite um número inteiro: ");
            scanner.next();
        }
        int numero = scanner.nextInt();
        scanner.nextLine();
        return numero;
    }

    private static double lerDouble(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            System.out.print("Entrada inválida. Por favor, digite um número (ex: 10,5): ");
            scanner.next();
        }
        double numero = scanner.nextDouble();
        scanner.nextLine();
        return numero;
    }

    private static void cadastrarMagosExemplo(GerenciadorMagos gerenciador) {
        gerenciador.cadastrarMago(new MagoArcano(1, "Merlin", 100, 15, 10, Mago.CAJADO, Mago.IA, Mago.IA_AGRESSIVO));
        gerenciador.cadastrarMago(new MagoElemental(2, "Ignis", 120, 12, 15, Mago.VARA, Mago.IA, Mago.IA_OPORTUNISTA));
        gerenciador.cadastrarMago(new MagoSombrio(3, "Nocturne", 90, 18, 5, Mago.TOMO, Mago.IA, Mago.IA_OPORTUNISTA));
        gerenciador.cadastrarMago(new MagoArcano(4, "Gandalf", 105, 14, 12, Mago.CAJADO, Mago.IA, Mago.IA_AGRESSIVO));
        gerenciador
                .cadastrarMago(new MagoElemental(5, "Glacius", 115, 13, 13, Mago.VARA, Mago.IA, Mago.IA_OPORTUNISTA));
        gerenciador.cadastrarMago(new MagoSombrio(6, "Umbra", 95, 17, 8, Mago.TOMO, Mago.IA, Mago.IA_AGRESSIVO));
    }
}
/*
 * ================================== FUNCIONALIDADES DA CLASSE
 * ==================================
 * - É o ponto de entrada principal do programa (método main).
 * - Gere a interface de utilizador através de um menu de texto no console.
 * - Utiliza submenus para organizar as diferentes funcionalidades (Gestão de
 * Magos, Duelos, Relatórios).
 * - Interage com as classes 'GerenciadorMagos' e 'GerenciadorDuelos' para
 * executar as ações
 * solicitadas pelo utilizador, como registar magos, agendar e simular duelos.
 * - Contém métodos utilitários para ler dados do utilizador de forma segura.
 * =============================================================================
 * ================
 */