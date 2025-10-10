// Se você estiver a usar pacotes, adicione a linha do seu pacote aqui. Ex:
// package br.com.coliseu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste para o GerenciadorDuelos.
 * Foca em validar as regras de negócio como agendamento,
 * prevenção de conflitos e a correta geração de relatórios.
 */
class GerenciadorDuelosTest {

    private GerenciadorDuelos gerenciador;
    private Mago mago1, mago2, mago3, mago4;

    /**
     * @BeforeEach: Esta anotação do JUnit executa este método ANTES de cada
     * método de teste (@Test) ser iniciado.
     * * Objetivo: Garantir que cada teste comece com um "ambiente limpo" e
     * previsível. Aqui, criamos um novo gerenciador e magos "mock" (de mentira)
     * para usar nos testes, evitando que os testes interfiram uns com os outros.
     */
    @BeforeEach
    void setUp() {
        gerenciador = new GerenciadorDuelos();
        mago1 = new MagoArcano(1, "Mago A", 100, 10, 10, Mago.VARA, Mago.HUMANO, null);
        mago2 = new MagoElemental(2, "Mago B", 100, 10, 10, Mago.CAJADO, Mago.HUMANO, null);
        mago3 = new MagoSombrio(3, "Mago C", 100, 10, 10, Mago.TOMO, Mago.HUMANO, null);
        mago4 = new MagoArcano(4, "Mago D", 100, 10, 10, Mago.VARA, Mago.HUMANO, null);
    }

    /**
     * @Test e @DisplayName: Usados para definir e descrever um caso de teste.
     * * Objetivo deste teste: Validar o "caminho feliz" do agendamento.
     * Verifica se, em condições normais, o duelo é criado, adicionado à lista
     * de agendados e retornado corretamente.
     */
    @Test
    @DisplayName("Deve agendar um duelo com sucesso quando não há conflitos")
    void agendarDuelo_QuandoNaoHaConflito_DeveAgendarComSucesso() {
        // --- 1. PREPARAÇÃO (Arrange) ---
        LocalDateTime horario = LocalDateTime.of(2025, 10, 20, 15, 0);
        
        // --- 2. AÇÃO (Act) ---
        Duelo dueloAgendado = gerenciador.agendarDuelo("1v1", List.of(mago1), List.of(mago2), Duelo.CIRCULO_RUNICO, horario);

        // --- 3. VERIFICAÇÃO (Assert) ---
        // Verificamos se o duelo foi realmente criado.
        assertNotNull(dueloAgendado, "O duelo não deveria ser nulo para um agendamento válido.");
        // Verificamos se a lista de agendados agora contém 1 duelo.
        assertEquals(1, gerenciador.getDuelosAgendados().size(), "A lista de agendados deveria conter um duelo.");
        // Verificamos se o duelo na lista é o mesmo que foi retornado.
        assertEquals(dueloAgendado, gerenciador.getDuelosAgendados().get(0));
    }

    /**
     * @Test e @DisplayName: Outro caso de teste.
     * * Objetivo deste teste: Validar a regra de negócio que impede
     * o agendamento de duelos na mesma arena com menos de 30 minutos de diferença.
     * Este é um teste de um "caminho de falha" esperado.
     */
    @Test
    @DisplayName("Não deve agendar um duelo se houver conflito de horário na mesma arena")
    void agendarDuelo_QuandoHaConflitoDeHorario_DeveRetornarNull() {
        // --- 1. PREPARAÇÃO (Arrange) ---
        // Primeiro, agendamos um duelo que servirá de base para o conflito.
        LocalDateTime horario1 = LocalDateTime.of(2025, 10, 20, 15, 0);
        gerenciador.agendarDuelo("1v1", List.of(mago1), List.of(mago2), Duelo.CIRCULO_RUNICO, horario1);
        
        // Agora, definimos um horário conflitante (29 minutos depois).
        LocalDateTime horarioConflitante = horario1.plusMinutes(29);

        // --- 2. AÇÃO (Act) ---
        // Tentamos agendar o segundo duelo, que deve falhar.
        Duelo dueloConflitante = gerenciador.agendarDuelo("1v1", List.of(mago3), List.of(mago4), Duelo.CIRCULO_RUNICO, horarioConflitante);

        // --- 3. VERIFICAÇÃO (Assert) ---
        // Verificamos se o método corretamente retornou null, indicando a falha.
        assertNull(dueloConflitante, "O duelo deveria ser nulo devido ao conflito de horário.");
        // Verificamos se a lista de agendados não foi alterada, mantendo apenas o primeiro duelo.
        assertEquals(1, gerenciador.getDuelosAgendados().size(), "A lista de agendados não deveria crescer após um agendamento falho.");
    }

    /**
     * @Test e @DisplayName: Teste de um caso de borda.
     * * Objetivo deste teste: Verificar se a lógica de conflito funciona
     * exatamente no limite. Um duelo agendado com 30 minutos de diferença
     * deve ser permitido.
     */
    @Test
    @DisplayName("Deve permitir agendamento com exatamente 30 minutos de diferença")
    void agendarDuelo_ComDiferencaDe30Minutos_DevePermitir() {
        // PREPARAÇÃO
        LocalDateTime horario1 = LocalDateTime.of(2025, 10, 20, 16, 0);
        gerenciador.agendarDuelo("1v1", List.of(mago1), List.of(mago2), Duelo.CIRCULO_RUNICO, horario1);
        
        LocalDateTime horarioLimite = horario1.plusMinutes(30);

        // AÇÃO
        Duelo dueloNoLimite = gerenciador.agendarDuelo("1v1", List.of(mago3), List.of(mago4), Duelo.CIRCULO_RUNICO, horarioLimite);

        // VERIFICAÇÃO
        assertNotNull(dueloNoLimite, "Deveria ser possível agendar um duelo com 30 minutos de diferença.");
        assertEquals(2, gerenciador.getDuelosAgendados().size(), "A lista deveria conter 2 duelos.");
    }

    /**
     * @Test e @DisplayName: Teste de um método de relatório.
     * * Objetivo: Validar se o relatório "Mapa de Calor" está a ser gerado
     * com os dados corretos. Como o método original imprime no console, nós
     * "capturamos" essa saída para uma String para podermos verificá-la.
     */
    @Test
    @DisplayName("Gerar Mapa de Calor deve mostrar a contagem correta de duelos por hora")
    void gerarMapaDeCalorDeHorarios_ComDados_DeveImprimirContagemCorreta() {
        // --- 1. PREPARAÇÃO (Arrange) ---
        // Agendamos alguns duelos em horários específicos para o nosso teste.
        gerenciador.agendarDuelo("1v1", List.of(mago1), List.of(mago2), "Arena1", LocalDateTime.of(2025, 11, 5, 10, 0)); // 1 duelo às 10h
        gerenciador.agendarDuelo("1v1", List.of(mago3), List.of(mago4), "Arena2", LocalDateTime.of(2025, 11, 5, 14, 0)); // 2 duelos às 14h
        gerenciador.agendarDuelo("1v1", List.of(mago1), List.of(mago4), "Arena3", LocalDateTime.of(2025, 11, 5, 14, 30)); // (ainda às 14h)

        // Preparamos o "capturador" da saída do console.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // --- 2. AÇÃO (Act) ---
        // Executamos o método que imprime no console.
        gerenciador.gerarMapaDeCalorDeHorarios();

        // --- 3. VERIFICAÇÃO (Assert) ---
        // Restauramos a saída padrão do console.
        System.setOut(originalOut);
        
        // Convertemos o conteúdo capturado para uma String.
        String saidaDoConsole = outContent.toString();
        
        // Verificamos se a String de saída contém as linhas que esperamos.
        // assertTrue verifica se a condição é verdadeira.
        assertTrue(saidaDoConsole.contains("10:00 | ■ (1)"), "O mapa de calor deveria mostrar 1 duelo às 10:00.");
        assertTrue(saidaDoConsole.contains("14:00 | ■■ (2)"), "O mapa de calor deveria mostrar 2 duelos às 14:00.");
        assertTrue(saidaDoConsole.contains("08:00 |  (0)"), "O mapa de calor deveria mostrar 0 duelos para um horário vazio.");
    }
}