import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * MagoDAO (Data Access Object)
 * Reformado para alinhar com a classe Mago e suas subclasses.
 * Gerencia a persistência (salvar, carregar, etc.) dos objetos Mago no banco de dados.
 */
public class MagoDAO {

    /**
     * Salva um novo objeto Mago no banco de dados.
     * Os dados são extraídos do objeto e inseridos na tabela 'magos'.
     *
     * @param mago O objeto Mago a ser persistido.
     */
    public void inserirMago(Mago mago) {
        // O SQL inclui todos os campos relevantes da sua classe Mago.
        String sql = "INSERT INTO magos(id, codinome, escola, foco, controlador, perfilIA, " +
                     "manaMax, vidaMax, poderBase, resistenciaMagica, classe) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, mago.getId());
            pstmt.setString(2, mago.getCodinome());
            pstmt.setString(3, mago.getEscola());
            pstmt.setString(4, mago.foco); // Acesso direto ao atributo, pois não há getter
            pstmt.setString(5, mago.getControlador());
            pstmt.setString(6, mago.perfilIA); // Acesso direto
            pstmt.setInt(7, mago.manaMax); // Acesso direto
            pstmt.setInt(8, mago.getVidaMax());
            pstmt.setDouble(9, mago.getPoderBase());
            pstmt.setDouble(10, mago.getResistenciaMagica());

            // Determina a 'classe' com base no tipo de objeto
            if (mago instanceof MagoArcano) {
                pstmt.setString(11, "Mago Arcano");
            } else if (mago instanceof MagoElemental) {
                pstmt.setString(11, "Mago Elemental");
            } else if (mago instanceof MagoSombrio) {
                pstmt.setString(11, "Mago Sombrio");
            } else {
                pstmt.setString(11, "Mago Genérico");
            }
            
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao inserir mago no banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Busca um único Mago no banco de dados pelo seu ID.
     *
     * @param id O ID do mago a ser procurado.
     * @return Um objeto Mago se encontrado, ou null caso contrário.
     */
    public Mago buscarMagoPorId(int id) {
        String sql = "SELECT * FROM magos WHERE id = ?";
        Mago mago = null;

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Usa o método auxiliar para construir o objeto Mago
                    mago = criarMagoDoResultSet(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar mago por ID: " + e.getMessage());
        }
        return mago;
    }

    /**
     * Carrega e retorna uma lista com todos os Magos cadastrados no banco de dados.
     *
     * @return Uma List<Mago> contendo todos os magos.
     */
    public List<Mago> listarTodosMagos() {
        String sql = "SELECT * FROM magos ORDER BY id";
        List<Mago> magos = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Para cada linha no resultado, cria um objeto Mago e adiciona à lista
                magos.add(criarMagoDoResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar todos os magos: " + e.getMessage());
        }
        return magos;
    }

    /**
     * Método auxiliar para converter uma linha do ResultSet em um objeto Mago.
     * Esta é a parte mais importante, pois lê os dados do banco e instancia a
     * subclasse correta de Mago (Arcano, Elemental ou Sombrio).
     *
     * @param rs O ResultSet posicionado na linha correta.
     * @return O objeto Mago construído.
     * @throws SQLException Em caso de erro ao ler os dados.
     */
    private Mago criarMagoDoResultSet(ResultSet rs) throws SQLException {
        // Extrai todos os dados da linha atual do ResultSet
        int id = rs.getInt("id");
        String codinome = rs.getString("codinome");
        String escola = rs.getString("escola");
        String foco = rs.getString("foco");
        String controlador = rs.getString("controlador");
        String perfilIA = rs.getString("perfilIA");
        int manaMax = rs.getInt("manaMax");
        double poderBase = rs.getDouble("poderBase");
        double resistenciaMagica = rs.getDouble("resistenciaMagica");

        // A lógica principal: usa a coluna 'escola' para decidir qual
        // construtor de subclasse chamar.
        return switch (escola) {
            case "Arcano" -> new MagoArcano(id, codinome, manaMax, poderBase, resistenciaMagica, foco, controlador, perfilIA);
            case "Elemental" -> new MagoElemental(id, codinome, manaMax, poderBase, resistenciaMagica, foco, controlador, perfilIA);
            case "Sombrio" -> new MagoSombrio(id, codinome, manaMax, poderBase, resistenciaMagica, foco, controlador, perfilIA);
            default -> {
                // Caso encontre uma escola desconhecida, retorna null ou lança um erro.
                System.err.println("Escola de mago desconhecida no banco de dados: " + escola);
                yield null;
            }
        };
    }
}