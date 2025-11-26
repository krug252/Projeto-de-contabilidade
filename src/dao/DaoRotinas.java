package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import model.PlanoDeContas;

public class DaoRotinas {

    private final DataSource dataSource;

    public DaoRotinas(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // ============================================================
    // VIRADA DE MÊS (corrigida)
    // ============================================================
    public void virarMes(String mesAno) {
        Connection con = dataSource.getConnection();
        DateTimeFormatter inFmt = DateTimeFormatter.ofPattern("MM/yyyy");
        DateTimeFormatter sqlFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            YearMonth ym = YearMonth.parse(mesAno, inFmt);
            YearMonth prevYm = ym.minusMonths(1);
            LocalDate firstThis = ym.atDay(1);
            LocalDate lastPrev = prevYm.atEndOfMonth();
            LocalDate lastThis = ym.atEndOfMonth();
            String firstThisStr = firstThis.format(sqlFmt);
            String lastPrevStr = lastPrev.format(sqlFmt);
            String lastThisStr = lastThis.format(sqlFmt);
            String sqlContas = "SELECT idconta, idreduzida FROM planodecontas";
            try (PreparedStatement psContas = con.prepareStatement(sqlContas); ResultSet rsContas = psContas.executeQuery()) {
                String sqlSaldoPrev = """
                    SELECT SUM(IF(dsdc='C', vlmovimento, -vlmovimento)) AS bal
                    FROM movimentacao
                    WHERE idreduzida = ? AND STR_TO_DATE(dtmovimento, '%d/%m/%Y') <= STR_TO_DATE(?, '%d/%m/%Y')
                """;
                String sqlSaldoMes = """
                    SELECT SUM(IF(dsdc='C', vlmovimento, -vlmovimento)) AS bal
                    FROM movimentacao
                    WHERE idreduzida = ? AND STR_TO_DATE(dtmovimento, '%d/%m/%Y')
                    BETWEEN STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y')
                """;
                while (rsContas.next()) {
                    String idconta = rsContas.getString("idconta");
                    int idreduzida = rsContas.getInt("idreduzida");
                    double saldoPrev = getSaldo(con, sqlSaldoPrev, idreduzida, lastPrevStr);
                    double saldoMes = getSaldo(con, sqlSaldoMes, idreduzida, firstThisStr, lastThisStr);
                    double saldoThis = saldoPrev + saldoMes;
                    try (PreparedStatement psUpd = con.prepareStatement("""
                        UPDATE planodecontas
                        SET vlmesanterior=?, dsmesanteriordc=?, vlmesatual=?, dsmesatualdc=?
                        WHERE idconta=?
                    """)) {
                        psUpd.setDouble(1, saldoPrev);
                        psUpd.setString(2, saldoPrev >= 0 ? "C" : "D");
                        psUpd.setDouble(3, saldoThis);
                        psUpd.setString(4, saldoThis >= 0 ? "C" : "D");
                        psUpd.setString(5, idconta);
                        psUpd.executeUpdate();
                    }
                }
            }
            totalizarPlanoDeContas(con);
            System.out.println("✅ Virada de mês concluída: " + mesAno);
        } catch (Exception e) {
            System.err.println("❌ Erro na virada de mês: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ============================================================
    // VIRADA DE ANO (corrigida)
    // ============================================================
    public void virarAno(int ano) {
        Connection con = dataSource.getConnection();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate firstThisYear = LocalDate.of(ano, 1, 1);
            LocalDate lastPrevYear = LocalDate.of(ano - 1, 12, 31);
            LocalDate lastThisYear = LocalDate.of(ano, 12, 31);
            String firstThisStr = firstThisYear.format(fmt);
            String lastPrevStr = lastPrevYear.format(fmt);
            String lastThisStr = lastThisYear.format(fmt);
            String sqlContas = "SELECT idconta, idreduzida FROM planodecontas";
            try (PreparedStatement psContas = con.prepareStatement(sqlContas); ResultSet rsContas = psContas.executeQuery()) {
                String sqlSaldoPrev = """
                    SELECT SUM(IF(dsdc='C', vlmovimento, -vlmovimento)) AS bal
                    FROM movimentacao
                    WHERE idreduzida = ? AND STR_TO_DATE(dtmovimento, '%d/%m/%Y') <= STR_TO_DATE(?, '%d/%m/%Y')
                """;
                String sqlSaldoAno = """
                    SELECT SUM(IF(dsdc='C', vlmovimento, -vlmovimento)) AS bal
                    FROM movimentacao
                    WHERE idreduzida = ? AND STR_TO_DATE(dtmovimento, '%d/%m/%Y')
                    BETWEEN STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y')
                """;
                while (rsContas.next()) {
                    String idconta = rsContas.getString("idconta");
                    int idreduzida = rsContas.getInt("idreduzida");
                    double saldoPrev = getSaldo(con, sqlSaldoPrev, idreduzida, lastPrevStr);
                    double saldoAno = getSaldo(con, sqlSaldoAno, idreduzida, firstThisStr, lastThisStr);
                    double saldoThis = saldoPrev + saldoAno;
                    try (PreparedStatement psUpd = con.prepareStatement("""
                        UPDATE planodecontas
                        SET vlexanterior=?, dsexanteriordc=?, vlexatual=?, dsexatualdc=?
                        WHERE idconta=?
                    """)) {
                        psUpd.setDouble(1, saldoPrev);
                        psUpd.setString(2, saldoPrev >= 0 ? "C" : "D");
                        psUpd.setDouble(3, saldoThis);
                        psUpd.setString(4, saldoThis >= 0 ? "C" : "D");
                        psUpd.setString(5, idconta);
                        psUpd.executeUpdate();
                    }
                }
            }
            totalizarPlanoDeContas(con);
            System.out.println("✅ Virada de ano concluída: " + ano);
        } catch (Exception e) {
            System.err.println("❌ Erro na virada de ano: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ============================================================
    // EXIBIR MOVIMENTAÇÕES DO MÊS
    // ============================================================
    public void exibirMovimentacoesDoMes(String mesAno) {
        Connection con = dataSource.getConnection();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/yyyy");
        DateTimeFormatter sqlFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            YearMonth ym = YearMonth.parse(mesAno, fmt);
            LocalDate first = ym.atDay(1);
            LocalDate last = ym.atEndOfMonth();
            String sql = """
                SELECT idreduzida,
                       SUM(CASE WHEN dsdc='C' THEN vlmovimento ELSE 0 END) AS total_credito,
                       SUM(CASE WHEN dsdc='D' THEN vlmovimento ELSE 0 END) AS total_debito
                FROM movimentacao
                WHERE STR_TO_DATE(dtmovimento, '%d/%m/%Y')
                BETWEEN STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y')
                GROUP BY idreduzida
                ORDER BY idreduzida
            """;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, first.format(sqlFmt));
                ps.setString(2, last.format(sqlFmt));
                ResultSet rs = ps.executeQuery();
                System.out.println("📅 Movimentações de " + mesAno);
                System.out.println("-----------------------------------");
                while (rs.next()) {
                    int id = rs.getInt("idreduzida");
                    double credito = rs.getDouble("total_credito");
                    double debito = rs.getDouble("total_debito");
                    double saldo = credito - debito;
                    String natureza = saldo >= 0 ? "C" : "D";
                    System.out.printf("Conta %d\n", id);
                    System.out.printf(" Créditos: %.2f\n", credito);
                    System.out.printf(" Débitos : %.2f\n", debito);
                    System.out.printf(" Saldo mês: %.2f (%s)\n", saldo, natureza);
                    System.out.println("-----------------------------------");
                }
                rs.close();
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao exibir movimentações: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ============================================================
    // TOTALIZAÇÃO CONTÁBIL (implementada corretamente)
    // ============================================================
    private void totalizarPlanoDeContas(Connection con) throws Exception {
        Map<Integer, Double> totaisMes = new LinkedHashMap<>();
        Map<Integer, Double> totaisAno = new LinkedHashMap<>();
        String sql = "SELECT idreduzida, vlmesatual, vlexatual FROM planoDeContas ORDER BY idreduzida DESC";
        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("idreduzida");
                double mes = rs.getDouble("vlmesatual");
                double ano = rs.getDouble("vlexatual");
                totaisMes.put(id, mes);
                totaisAno.put(id, ano);
                int pai = id / 10;
                while (pai > 0) {
                    totaisMes.put(pai, totaisMes.getOrDefault(pai, 0.0) + mes);
                    totaisAno.put(pai, totaisAno.getOrDefault(pai, 0.0) + ano);
                    pai /= 10;
                }
            }
        }
        String sqlUpd = "UPDATE planoDeContas SET vlmesatual=?, dsmesatualdc=?, vlexatual=?, dsexatualdc=? WHERE idreduzida=?";
        try (PreparedStatement psUpd = con.prepareStatement(sqlUpd)) {
            for (var entry : totaisMes.entrySet()) {
                int id = entry.getKey();
                double mes = entry.getValue();
                double ano = totaisAno.get(id);
                psUpd.setDouble(1, mes);
                psUpd.setString(2, mes >= 0 ? "C" : "D");
                psUpd.setDouble(3, ano);
                psUpd.setString(4, ano >= 0 ? "C" : "D");
                psUpd.setInt(5, id);
                psUpd.executeUpdate();
            }
        }
        System.out.println("✅ Totalização contábil concluída.");
    }

    // ============================================================
    // AUXILIAR DE CÁLCULO
    // ============================================================
    private double getSaldo(Connection con, String sql, int idreduzida, String... datas) throws Exception {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idreduzida);
            for (int i = 0; i < datas.length; i++) {
                ps.setString(i + 2, datas[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble("bal") : 0.0;
            }
        }
    }

    // ============================================================
    // ATUALIZAR PLANO DE CONTAS COMPLETO (sem virar mês/ano)
    // ============================================================
    public void atualizarPlanoDeContas() {
        Connection con = dataSource.getConnection();
        System.out.println("🔄 Atualizando saldos do Plano de Contas...");
        try {
            String sqlContas = "SELECT idconta, idreduzida FROM planoDeContas";
            try (PreparedStatement psContas = con.prepareStatement(sqlContas); ResultSet rsContas = psContas.executeQuery()) {
                String sqlSaldo = """
                    SELECT SUM(IF(dsdc='C', vlmovimento, -vlmovimento)) AS bal
                    FROM movimentacao
                    WHERE idreduzida = ?
                """;
                while (rsContas.next()) {
                    String idconta = rsContas.getString("idconta");
                    int idreduzida = rsContas.getInt("idreduzida");
                    double saldoAtual = getSaldo(con, sqlSaldo, idreduzida);
                    try (PreparedStatement psUpd = con.prepareStatement("""
                        UPDATE planoDeContas
                        SET vlmesatual=?, dsmesatualdc=?, vlexatual=?, dsexatualdc=?
                        WHERE idconta=?
                    """)) {
                        psUpd.setDouble(1, saldoAtual);
                        psUpd.setString(2, saldoAtual >= 0 ? "C" : "D");
                        psUpd.setDouble(3, saldoAtual);
                        psUpd.setString(4, saldoAtual >= 0 ? "C" : "D");
                        psUpd.setString(5, idconta);
                        psUpd.executeUpdate();
                    }
                }
            }
            totalizarPlanoDeContas(con);
            System.out.println("✅ Plano de Contas atualizado com sucesso.");
        } catch (Exception e) {
            System.err.println("❌ Erro ao atualizar plano de contas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void voltarMes(String mesAno) {
        Connection con = dataSource.getConnection();
        try {
            String sql = """
            UPDATE planoDeContas
            SET vlmesatual = vlmesanterior,
                dsmesatualdc = dsmesanteriordc
        """;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                int rows = ps.executeUpdate();
                System.out.println("🔁 " + rows + " contas atualizadas na volta de mês (" + mesAno + ")");
            }
            totalizarPlanoDeContas(con);
            System.out.println("✅ Volta de mês concluída: " + mesAno);
        } catch (Exception e) {
            System.err.println("❌ Erro na volta de mês: " + e.getMessage());
            e.printStackTrace();
        }
    }

// ============================================================
// VOLTA DE ANO (inverso da virada de ano)
// ============================================================
    public void voltarAno(int ano) {
        Connection con = dataSource.getConnection();
        try {
            String sql = """
            UPDATE planoDeContas
            SET vlexatual = vlexanterior,
                dsexatualdc = dsexanteriordc
        """;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                int rows = ps.executeUpdate();
                System.out.println("🔁 " + rows + " contas atualizadas na volta de ano (" + ano + ")");
            }
            totalizarPlanoDeContas(con);
            System.out.println("✅ Volta de ano concluída: " + ano);
        } catch (Exception e) {
            System.err.println("❌ Erro na volta de ano: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ResultSet consultarHistoricoContas(String dataInicio, String dataFim, int idContaInicial, int idContaFinal) {
        Connection con = dataSource.getConnection();
        try {
            String sql = """
            SELECT 
                m.idreduzida AS id_conta,
                m.dtmovimento AS data,
                m.dsdc AS debito_credito,
                m.vlmovimento AS valor,
                p.dsdescricao AS descricao_conta
            FROM movimentacao m
            JOIN planoDeContas p ON m.idreduzida = p.idreduzida
            WHERE STR_TO_DATE(m.dtmovimento, '%d/%m/%Y')
                  BETWEEN STR_TO_DATE(?, '%d/%m/%Y') AND STR_TO_DATE(?, '%d/%m/%Y')
              AND m.idreduzida BETWEEN ? AND ?
            ORDER BY m.idreduzida, STR_TO_DATE(m.dtmovimento, '%d/%m/%Y')
        """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, dataInicio);
            ps.setString(2, dataFim);
            ps.setInt(3, idContaInicial);
            ps.setInt(4, idContaFinal);

            return ps.executeQuery();
            // ⚠️ IMPORTANTE: quem chamar esse método deve fechar o ResultSet e PreparedStatement depois.

        } catch (Exception e) {
            System.err.println("❌ Erro ao consultar histórico: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}