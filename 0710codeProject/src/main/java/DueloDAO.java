import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DueloDAO (Data Access Object)
 * Reformado para gerenciar a persistência de objetos Duelo, incluindo suas
 * equipes de magos e as estatísticas de combate pós-duelo.
 */
public class DueloDAO {

    /**
     * Insere um novo Duelo agendado no banco de dados.
     * Salva o duelo principal na tabela 'duelos' e a relação de
     * participantes na tabela 'duelo_magos'.
     * Utiliza uma transação para garantir que ambas as operações ocorram com sucesso.
     *
     * @param duelo O objeto Duelo a ser inserido.
     */
    public void inserirDuelo(Duelo duelo) {
        String sqlDuelo = "INSERT INTO duelos (modo, arena, dataHora, finalizado) VALUES (?, ?, ?, ?)";
        String sqlParticipantes = "INSERT INTO duelo_magos (duelo_id, mago_id, equipe) VALUES (?, ?, ?)";
        Connection conn = null;

        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false); // Inicia a transação

            // Passo 1: Inserir o duelo e obter o ID gerado pelo banco
            int dueloId;
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDuelo, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, duelo.getModo());
                pstmt.setString(2, duelo.getArena());
                pstmt.setString(3, duelo.getDataHora().toString());
                pstmt.setBoolean(4, duelo.isFinalizado());
                pstmt.executeUpdate();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        dueloId = rs.getInt(1);
                        duelo.setId(dueloId); // Atualiza o objeto com o ID real do banco
                    } else {
                        throw new SQLException("Falha ao obter o ID do duelo inserido.");
                    }
                }
            }

            // Passo 2: Inserir os participantes na tabela de associação
            try (PreparedStatement pstmt = conn.prepareStatement(sqlParticipantes)) {
                for (Mago mago : duelo.getEquipe1()) {
                    pstmt.setInt(1, dueloId);
                    pstmt.setInt(2, mago.getId());
                    pstmt.setInt(3, 1); // Equipe 1
                    pstmt.addBatch();
                }
                for (Mago mago : duelo.getEquipe2()) {
                    pstmt.setInt(1, dueloId);
                    pstmt.setInt(2, mago.getId());
                    pstmt.setInt(3, 2); // Equipe 2
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }

            conn.commit(); // Confirma a transação se tudo deu certo

        } catch (SQLException e) {
            System.err.println("Erro de transação ao inserir duelo. Rollback acionado.");
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); // Desfaz tudo em caso de erro
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Atualiza um duelo existente, marcando-o como finalizado e salvando as estatísticas.
     *
     * @param duelo O objeto Duelo que foi finalizado e contém as estatísticas.
     */
    public void finalizarDuelo(Duelo duelo) {
        String sqlUpdateDuelo = "UPDATE duelos SET finalizado = ?, vencedor = ? WHERE id = ?";
        String sqlInsertStats = "INSERT INTO duelo_estatisticas (duelo_id, mago_id, dano_causado, abates, assistencias, dano_mitigado) " +
                                "VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;

        if (!duelo.isFinalizado()) {
            System.err.println("Tentativa de salvar estatísticas de um duelo não finalizado.");
            return;
        }

        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false);

            // Passo 1: Atualizar o status e o vencedor na tabela 'duelos'
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdateDuelo)) {
                pstmt.setBoolean(1, true);
                pstmt.setString(2, duelo.getVencedor());
                pstmt.setInt(3, duelo.getId());
                pstmt.executeUpdate();
            }

            // Passo 2: Inserir as estatísticas de todos os participantes
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsertStats)) {
                List<Mago> todosOsMagos = new ArrayList<>(duelo.getEquipe1());
                todosOsMagos.addAll(duelo.getEquipe2());

                for (Mago mago : todosOsMagos) {
                    pstmt.setInt(1, duelo.getId());
                    pstmt.setInt(2, mago.getId());
                    pstmt.setDouble(3, duelo.getDanoCausado().getOrDefault(mago, 0.0));
                    pstmt.setInt(4, duelo.getAbates().getOrDefault(mago, 0));
                    // Adicionar as outras estatísticas aqui quando os getters estiverem disponíveis
                    // pstmt.setInt(5, duelo.getAssistencias().getOrDefault(mago, 0));
                    // pstmt.setDouble(6, duelo.getDanoMitigado().getOrDefault(mago, 0.0));
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
            
            conn.commit();

        } catch (SQLException e) {
            System.err.println("Erro de transação ao finalizar duelo. Rollback acionado.");
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // NOTA: Métodos para listar/buscar duelos se tornariam mais complexos.
    // Eles precisariam carregar o duelo, depois buscar os participantes na tabela
    // 'duelo_magos', e se o duelo estiver finalizado, buscar as estatísticas
    // na tabela 'duelo_estatisticas', e então reconstruir o objeto Duelo em Java.
}