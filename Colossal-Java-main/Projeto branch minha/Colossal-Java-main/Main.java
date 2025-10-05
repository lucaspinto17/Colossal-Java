import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GerenciadorMagos gerenciador = new GerenciadorMagos();
        GerenciadorDuelos gerenciadorDuelos = new GerenciadorDuelos();
        boolean executando = true;
        
        // Cadastrar alguns magos de exemplo
        cadastrarMagosExemplo(gerenciador);
        
        while (executando) {
            System.out.println("=== COLISEU DE MAGOS ===");
            System.out.println("1. Cadastrar Mago");
            System.out.println("2. Listar Magos");
            System.out.println("3. Agendar Duelo 1vs1");
            System.out.println("4. Agendar Duelo em Equipes");
            System.out.println("5. Listar Duelos");
            System.out.println("6. Simular Duelo");
            System.out.println("7. Gerar Relatórios");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer
            
            switch (opcao) {
                case 1:
                    cadastrarMago(scanner, gerenciador);
                    break;
                case 2:
                    gerenciador.listarMagos();
                    break;
                case 3:
                    agendarDuelo1vs1(scanner, gerenciadorDuelos, gerenciador);
                    break;
                case 4:
                    agendarDueloEquipes(scanner, gerenciadorDuelos, gerenciador);
                    break;
                case 5:
                    gerenciadorDuelos.listarDuelos();
                    break;
                case 6:
                    simularDuelo(scanner, gerenciadorDuelos);
                    break;
                case 7:
                    gerarRelatorios(gerenciadorDuelos);
                    break;
                case 0:
                    executando = false;
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
        scanner.close();
    }
    
    private static void cadastrarMagosExemplo(GerenciadorMagos gerenciador) {
        // Magos Elementais
        gerenciador.cadastrarMago(new MagoElemental(1, "Ignis", 100, 15.0, 8.0));
        gerenciador.cadastrarMago(new MagoElemental(2, "Glacius", 90, 12.0, 10.0));
        gerenciador.cadastrarMago(new MagoElemental(3, "Ventus", 80, 10.0, 7.0));
        
        // Magos Arcanos
        gerenciador.cadastrarMago(new MagoArcano(4, "Merlin", 120, 18.0, 6.0));
        gerenciador.cadastrarMago(new MagoArcano(5, "Arcana", 110, 16.0, 5.0));
        gerenciador.cadastrarMago(new MagoArcano(6, "Mystra", 100, 14.0, 7.0));
        
        // Magos Sombrios
        gerenciador.cadastrarMago(new MagoSombrio(7, "Sombra", 95, 17.0, 9.0));
        gerenciador.cadastrarMago(new MagoSombrio(8, "Nocturne", 85, 15.0, 8.0));
        gerenciador.cadastrarMago(new MagoSombrio(9, "Umbra", 90, 16.0, 7.0));
    }
    
    private static void cadastrarMago(Scanner scanner, GerenciadorMagos gerenciador) {
        System.out.println("\n=== CADASTRAR MAGO ===");
        System.out.print("ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Codinome: ");
        String codinome = scanner.nextLine();
        
        System.out.println("Escola de Magia:");
        System.out.println("1. Elemental");
        System.out.println("2. Arcano");
        System.out.println("3. Sombrio");
        System.out.print("Escolha: ");
        int escolaOpcao = scanner.nextInt();
        
        System.out.print("Mana Máxima: ");
        int manaMax = scanner.nextInt();
        
        System.out.print("Poder Base: ");
        double poderBase = scanner.nextDouble();
        
        System.out.print("Resistência Mágica: ");
        double resistencia = scanner.nextDouble();
        
        Mago mago = null;
        switch (escolaOpcao) {
            case 1:
                mago = new MagoElemental(id, codinome, manaMax, poderBase, resistencia);
                break;
            case 2:
                mago = new MagoArcano(id, codinome, manaMax, poderBase, resistencia);
                break;
            case 3:
                mago = new MagoSombrio(id, codinome, manaMax, poderBase, resistencia);
                break;
            default:
                System.out.println("Escola inválida!");
                return;
        }
        
        if (gerenciador.cadastrarMago(mago)) {
            System.out.println("Mago cadastrado com sucesso!");
        } else {
            System.out.println("Erro: Limite de magos atingido (máx 12)!");
        }
    }
    
    private static void agendarDuelo1vs1(Scanner scanner, GerenciadorDuelos gerenciadorDuelos, GerenciadorMagos gerenciador) {
        System.out.println("\n=== AGENDAR DUELO 1vs1 ===");
        System.out.print("ID do Mago 1: ");
        int id1 = scanner.nextInt();
        System.out.print("ID do Mago 2: ");
        int id2 = scanner.nextInt();
        
        Mago mago1 = gerenciador.buscarMagoPorId(id1);
        Mago mago2 = gerenciador.buscarMagoPorId(id2);
        
        if (mago1 == null || mago2 == null) {
            System.out.println("Erro: Magos não encontrados!");
            return;
        }
        
        Duelo duelo = gerenciadorDuelos.agendarDuelo1vs1(mago1, mago2);
        if (duelo != null) {
            System.out.println("Duelo 1vs1 agendado com sucesso! ID: " + duelo.getId());
        } else {
            System.out.println("Erro ao agendar duelo!");
        }
    }
    
    private static void agendarDueloEquipes(Scanner scanner, GerenciadorDuelos gerenciadorDuelos, GerenciadorMagos gerenciador) {
        System.out.println("\n=== AGENDAR DUELO EM EQUIPES (3vs3) ===");
        
        List<Mago> equipe1 = new ArrayList<>();
        List<Mago> equipe2 = new ArrayList<>();
        
        System.out.println("\n--- Equipe 1 ---");
        for (int i = 1; i <= 3; i++) {
            System.out.print("ID do Mago " + i + " da Equipe 1: ");
            int id = scanner.nextInt();
            Mago mago = gerenciador.buscarMagoPorId(id);
            if (mago != null) {
                equipe1.add(mago);
            } else {
                System.out.println("Mago não encontrado!");
                return;
            }
        }
        
        System.out.println("\n--- Equipe 2 ---");
        for (int i = 1; i <= 3; i++) {
            System.out.print("ID do Mago " + i + " da Equipe 2: ");
            int id = scanner.nextInt();
            Mago mago = gerenciador.buscarMagoPorId(id);
            if (mago != null) {
                equipe2.add(mago);
            } else {
                System.out.println("Mago não encontrado!");
                return;
            }
        }
        
        Duelo duelo = gerenciadorDuelos.agendarDueloEquipes(equipe1, equipe2);
        if (duelo != null) {
            System.out.println("Duelo em equipes agendado com sucesso! ID: " + duelo.getId());
        } else {
            System.out.println("Erro ao agendar duelo!");
        }
    }
    
    private static void simularDuelo(Scanner scanner, GerenciadorDuelos gerenciadorDuelos) {
        System.out.println("\n=== SIMULAR DUELO ===");
        System.out.print("ID do Duelo: ");
        int idDuelo = scanner.nextInt();
        
        Duelo duelo = gerenciadorDuelos.buscarDueloPorId(idDuelo);
        if (duelo != null) {
            gerenciadorDuelos.simularDuelo(duelo);
        } else {
            System.out.println("Duelo não encontrado!");
        }
    }
    
    private static void gerarRelatorios(GerenciadorDuelos gerenciadorDuelos) {
        System.out.println("\n=== RELATÓRIOS ===");
        gerenciadorDuelos.gerarRanking();
        gerenciadorDuelos.gerarEstatisticasEscolas();
        gerenciadorDuelos.gerarEstatisticasModos();
    }
}