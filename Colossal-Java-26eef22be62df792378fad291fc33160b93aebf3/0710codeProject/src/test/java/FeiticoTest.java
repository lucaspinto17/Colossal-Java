// Se você estiver a usar pacotes, adicione a linha do seu pacote aqui. Ex:
// package br.com.coliseu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste para a classe Feitico.
 * O objetivo é garantir que os objetos Feitico são criados
 * corretamente e que os seus dados são armazenados e retornados
 * de forma fiável pelos seus construtores e getters.
 */
class FeiticoTest {

    /**
     * @Test: Esta é a anotação principal do JUnit. Ela marca o método abaixo
     * como um caso de teste que deve ser executado automaticamente.
     * * @DisplayName: Uma anotação opcional que nos permite dar um nome mais descritivo
     * e legível ao teste, que aparecerá nos relatórios. É ótimo para explicar
     * o propósito do teste de forma clara.
     * * Objetivo deste teste: Verificar se o construtor completo, que inclui um efeito,
     * atribui corretamente todos os valores a todos os atributos da classe.
     */
    @Test
    @DisplayName("Construtor completo deve atribuir todos os atributos corretamente")
    void construtorCompletoDeveAtribuirTodosOsValores() {
        // --- 1. PREPARAÇÃO (Arrange) ---
        // Nesta fase, nós preparamos todos os dados e objetos necessários para o teste.
        // Criamos um feitiço com valores conhecidos e específicos para podermos verificar
        // se cada um deles foi guardado no lugar certo.
        String nome = "Seta de Gelo";
        String tipo = Feitico.PROJETIL;
        int custoMana = 20;
        double poderBase = 15.0;
        String efeito = "Congelamento";
        int duracao = 2; // 2 turnos
        double chance = 0.5; // 50% de chance

        // --- 2. AÇÃO (Act) ---
        // Aqui, nós executamos a ação que queremos testar. Neste caso, é chamar
        // o construtor completo da classe Feitico.
        Feitico setaDeGelo = new Feitico(nome, tipo, custoMana, poderBase, efeito, duracao, chance);

        // --- 3. VERIFICAÇÃO (Assert) ---
        // Esta é a fase final e mais importante. Usamos os métodos de "assert" do JUnit
        // para verificar se o resultado da AÇÃO foi o que esperávamos.
        // Se qualquer uma destas verificações falhar, o teste inteiro falha.
        
        // assertEquals(valorEsperado, valorReal, "Mensagem de erro opcional");
        // Verifica se o valor retornado pelo getter é exatamente o que passamos para o construtor.
        assertEquals(nome, setaDeGelo.getNome(), "O nome do feitiço não foi atribuído corretamente.");
        assertEquals(tipo, setaDeGelo.getTipo(), "O tipo do feitiço não foi atribuído corretamente.");
        assertEquals(custoMana, setaDeGelo.getCustoMana(), "O custo de mana não foi atribuído corretamente.");
        assertEquals(poderBase, setaDeGelo.getPoderBase(), "O poder base não foi atribuído corretamente.");
        assertEquals(efeito, setaDeGelo.getEfeitoAplicado(), "O efeito aplicado não foi atribuído corretamente.");
        assertEquals(duracao, setaDeGelo.getDuracaoEfeito(), "A duração do efeito não foi atribuída corretamente.");
        assertEquals(chance, setaDeGelo.getChanceDeAplicarEfeito(), "A chance de aplicar o efeito não foi atribuída corretamente.");
    }

    /**
     * @Test e @DisplayName: Usados da mesma forma que no teste anterior.
     * * Objetivo deste teste: Verificar se o construtor simplificado (usado para feitiços
     * que não têm efeitos) funciona como esperado. Ele deve atribuir os valores básicos
     * e, crucialmente, definir os valores relacionados a efeitos como nulos ou zero.
     */
    @Test
    @DisplayName("Construtor simplificado deve atribuir valores padrão para os efeitos")
    void construtorSimplificadoDeveAtribuirValoresPadrao() {
        // --- 1. PREPARAÇÃO (Arrange) ---
        // Criamos um feitiço de dano puro, sem efeito.
        String nome = "Bola de Fogo";
        String tipo = Feitico.AREA;
        int custoMana = 30;
        double poderBase = 25.0;

        // --- 2. AÇÃO (Act) ---
        // Chamamos o construtor simplificado.
        Feitico bolaDeFogo = new Feitico(nome, tipo, custoMana, poderBase);

        // --- 3. VERIFICAÇÃO (Assert) ---
        // Primeiro, verificamos se os dados básicos foram guardados corretamente.
        assertEquals(nome, bolaDeFogo.getNome(), "O nome do feitiço (simplificado) está incorreto.");
        assertEquals(tipo, bolaDeFogo.getTipo(), "O tipo do feitiço (simplificado) está incorreto.");
        assertEquals(custoMana, bolaDeFogo.getCustoMana(), "O custo de mana (simplificado) está incorreto.");
        assertEquals(poderBase, bolaDeFogo.getPoderBase(), "O poder base (simplificado) está incorreto.");
        
        // Agora, a parte mais importante deste teste:
        // Verificamos se os atributos de efeito foram inicializados com valores padrão.
        // assertNull verifica se o valor é nulo.
        assertNull(bolaDeFogo.getEfeitoAplicado(), "O efeito deveria ser nulo para o construtor simplificado.");
        // assertEquals para valores numéricos padrão (zero).
        assertEquals(0, bolaDeFogo.getDuracaoEfeito(), "A duração do efeito deveria ser 0 para o construtor simplificado.");
        assertEquals(0.0, bolaDeFogo.getChanceDeAplicarEfeito(), "A chance de aplicar o efeito deveria ser 0.0 para o construtor simplificado.");
    }
}