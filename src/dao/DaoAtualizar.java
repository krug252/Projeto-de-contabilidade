package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import model.PlanoDeContas;
import model.Movimentacao;

public class DaoAtualizar {

    private DataSource dataSource;

    public DaoAtualizar(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public void virarMes() {
        String sql = "UPDATE planodecontas "
                + "SET vlMesAnterior = vlMesAtual, "
                + "dsMesAnteriorDC = dsMesAtualDC, "
                + "vlMesAtual = 0";

        try (Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
            System.out.println("Virada de mês realizada com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        
    public void virarAno() {
        String sql = "UPDATE planodecontas "
                + "SET vlExAnterior = vlExAtual, "
                + "dsExAnteriorDC = dsExAtualDC, "
                + "vlExAtual = 0";

        try (Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
            System.out.println("Virada de ano realizada com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
