import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Duelo {
    // --- Constantes ---
    public static final String CIRCULO_RUNICO = "Círculo Rúnico";
    public static final String LABIRINTO_ILUSORIO = "Labirinto Ilusório";
    public static final String CAMARA_ANTIMAGIA = "Câmara Antimagia";

    private static int proximoId = 1;
    private int id;
    private String modo, arena, vencedor;
    private LocalDateTime dataHora;
    private boolean finalizado = false;
    private List<Mago> equipe1, equipe2;

    // NOVO ATRIBUTO ADICIONADO
    private String condicaoCampoAtual = CondicaoCampo.NORMAL;

    private Map<Mago, Double> danoCausado = new HashMap<>();
    private Map<Mago, Double> danoMitigado = new HashMap<>();
    private Map<Mago, Integer> abates = new HashMap<>();
    private Map<Mago, Integer> assistencias = new HashMap<>();
    private Map<Mago, Mago> ultimoAtacante = new HashMap<>();

    public Duelo(String modo, List<Mago> eq1, List<Mago> eq2, String arena, LocalDateTime data) {
        this.id = proximoId++;
        this.modo = modo;
        this.equipe1 = eq1;
        this.equipe2 = eq2;
        this.arena = arena;
        this.dataHora = data;

        for (Mago m : eq1) inicializarEstatisticas(m);
        for (Mago m : eq2) inicializarEstatisticas(m);
    }

    private void inicializarEstatisticas(Mago m) {
        danoCausado.put(m, 0.0);
        danoMitigado.put(m, 0.0);
        abates.put(m, 0);
        assistencias.put(m, 0);
    }
    public void setId(int id) {
        this.id = id;
    }
    public void adicionarDano(Mago autor, double dano) { danoCausado.put(autor, danoCausado.get(autor) + dano); }
    public void adicionarAbate(Mago autor) { abates.put(autor, abates.get(autor) + 1); }
    public void adicionarDanoMitigado(Mago defensor, double mitigado) { danoMitigado.put(defensor, danoMitigado.get(defensor) + mitigado); }
    public void registarAssistencias(Mago alvo, Mago abatedor) {
        Mago assistente = ultimoAtacante.get(alvo);
        if (assistente != null && assistente != abatedor && assistente.estaVivo()) {
            assistencias.put(assistente, assistencias.get(assistente) + 1);
            System.out.printf("   > %s recebe uma assistência!%n", assistente.getCodinome());
        }
    }
    public void setUltimoAtacante(Mago alvo, Mago atacante) { this.ultimoAtacante.put(alvo, atacante); }

    public int getId() { return id; }
    public String getModo() { return modo; }
    public String getArena() { return arena; }
    public LocalDateTime getDataHora() { return dataHora; }
    public List<Mago> getEquipe1() { return equipe1; }
    public List<Mago> getEquipe2() { return equipe2; }
    public boolean isFinalizado() { return finalizado; }
    public String getVencedor() { return vencedor; }
    public Map<Mago, Double> getDanoCausado() { return danoCausado; }
    public Map<Mago, Integer> getAbates() { return abates; }
    public void setFinalizado(boolean finalizado) { this.finalizado = finalizado; }
    public void setVencedor(String vencedor) { this.vencedor = vencedor; }
    
    // NOVOS MÉTODOS GETTER E SETTER ADICIONADOS
    public String getCondicaoCampoAtual() { return condicaoCampoAtual; }
    public void setCondicaoCampoAtual(String condicaoCampoAtual) { this.condicaoCampoAtual = condicaoCampoAtual; }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String participantes = equipe1.size() > 1 ? "Equipe 1 vs Equipe 2" : equipe1.get(0).getCodinome() + " vs " + equipe2.get(0).getCodinome();
        return String.format("Duelo #%d: [%s] %s em %s às %s (%s)",
                id, modo.toUpperCase(), participantes, arena, dataHora.format(formatter), 
                finalizado ? "Finalizado - Vencedor: " + vencedor : "Agendado");
    }
}