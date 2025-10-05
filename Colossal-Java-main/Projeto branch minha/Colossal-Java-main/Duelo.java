import java.util.ArrayList;
import java.util.List;

public class Duelo {
    private static int proximoId = 1;
    
    private int id;
    private String modo; // "1VS1" ou "EQUIPES"
    private Mago mago1; // Para 1vs1
    private Mago mago2; // Para 1vs1
    private List<Mago> equipe1; // Para equipes
    private List<Mago> equipe2; // Para equipes
    private boolean finalizado;
    private String vencedor;
    private double danoEquipe1;
    private double danoEquipe2;
    
    // Construtor para duelo 1vs1
    public Duelo(Mago mago1, Mago mago2) {
        this.id = proximoId++;
        this.modo = "1VS1";
        this.mago1 = mago1;
        this.mago2 = mago2;
        this.equipe1 = new ArrayList<>();
        this.equipe2 = new ArrayList<>();
        this.finalizado = false;
        this.danoEquipe1 = 0;
        this.danoEquipe2 = 0;
    }
    
    // Construtor para duelo em equipes
    public Duelo(List<Mago> equipe1, List<Mago> equipe2) {
        this.id = proximoId++;
        this.modo = "EQUIPES";
        this.equipe1 = new ArrayList<>(equipe1);
        this.equipe2 = new ArrayList<>(equipe2);
        this.mago1 = null;
        this.mago2 = null;
        this.finalizado = false;
        this.danoEquipe1 = 0;
        this.danoEquipe2 = 0;
    }
    
    // Getters
    public int getId() { return id; }
    public String getModo() { return modo; }
    public Mago getMago1() { return mago1; }
    public Mago getMago2() { return mago2; }
    public List<Mago> getEquipe1() { return equipe1; }
    public List<Mago> getEquipe2() { return equipe2; }
    public boolean isFinalizado() { return finalizado; }
    public String getVencedor() { return vencedor; }
    public double getDanoEquipe1() { return danoEquipe1; }
    public double getDanoEquipe2() { return danoEquipe2; }
    
    public void setFinalizado(boolean finalizado) { this.finalizado = finalizado; }
    public void setVencedor(String vencedor) { this.vencedor = vencedor; }
    public void adicionarDanoEquipe1(double dano) { this.danoEquipe1 += dano; }
    public void adicionarDanoEquipe2(double dano) { this.danoEquipe2 += dano; }
    
    public boolean isModoEquipes() {
        return "EQUIPES".equals(modo);
    }
    
    @Override
    public String toString() {
        String status = finalizado ? "Finalizado - Vencedor: " + vencedor : "Agendado";
        
        if (isModoEquipes()) {
            return String.format("Duelo [ID: %d, Modo: %s, %s vs %s, Status: %s]", 
                               id, modo, formatarEquipe(equipe1), formatarEquipe(equipe2), status);
        } else {
            return String.format("Duelo [ID: %d, Modo: %s, %s vs %s, Status: %s]", 
                               id, modo, mago1.getCodinome(), mago2.getCodinome(), status);
        }
    }
    
    private String formatarEquipe(List<Mago> equipe) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < equipe.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(equipe.get(i).getCodinome());
        }
        sb.append("]");
        return sb.toString();
    }
}