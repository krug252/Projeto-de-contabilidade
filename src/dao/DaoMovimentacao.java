package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.Movimentacao;

public class DaoMovimentacao {

    private DataSource dataSource;

    public DaoMovimentacao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Listar todos os registros
    public ArrayList<Movimentacao> readAll() {
        PreparedStatement ps = null;

        try {
            String SQL = "SELECT * FROM movimentacao";
            ps = dataSource.getConnection().prepareStatement(SQL);

            ResultSet rs = ps.executeQuery();
            ArrayList<Movimentacao> lista = new ArrayList<>();

            while (rs.next()) {
                Movimentacao m = new Movimentacao();
                m.setIdMovimento(rs.getInt("idMovimento"));
                m.setDtMovimento(rs.getString("dtMovimento"));
                m.setIdReduzida(rs.getInt("idReduzida"));
                m.setDsHistorico(rs.getString("dsHistorico"));
                m.setVlMovimento(rs.getDouble("vlMovimento"));
                m.setDsDC(rs.getString("dsDC"));

                lista.add(m);
            }

            ps.close();
            dataSource.closeDataSource();

            return lista;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao recuperar dados: " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro geral: " + ex.getMessage());
        }

        dataSource.closeDataSource();
        return null;
    }

    // Consultar por histórico
    public ArrayList<Movimentacao> consulta(String historico) {
        PreparedStatement ps = null;

        try {
            String SQL = "SELECT * FROM movimentacao WHERE dsHistorico LIKE ?";
            ps = dataSource.getConnection().prepareStatement(SQL);
            ps.setString(1, "%" + historico + "%");

            ResultSet rs = ps.executeQuery();
            ArrayList<Movimentacao> lista = new ArrayList<>();

            while (rs.next()) {
                Movimentacao m = new Movimentacao();
                m.setIdMovimento(rs.getInt("idMovimento"));
                m.setDtMovimento(rs.getString("dtMovimento"));
                m.setIdReduzida(rs.getInt("idReduzida"));
                m.setDsHistorico(rs.getString("dsHistorico"));
                m.setVlMovimento(rs.getDouble("vlMovimento"));
                m.setDsDC(rs.getString("dsDC"));

                lista.add(m);
            }

            ps.close();
            dataSource.closeDataSource();

            return lista;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao recuperar dados: " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro geral: " + ex.getMessage());
        }

        dataSource.closeDataSource();
        return null;
    }

    // Inserir novo registro
    public void inserir(Movimentacao m) {
        Connection con = dataSource.getConnection();
        PreparedStatement ps = null;

        try {
            String SQL = "INSERT INTO movimentacao (dtMovimento, idReduzida, dsHistorico, vlMovimento, dsDC) VALUES (?, ?, ?, ?, ?)";
            ps = con.prepareStatement(SQL);
            ps.setString(1, m.getDtMovimento());
            ps.setInt(2, m.getIdReduzida());
            ps.setString(3, m.getDsHistorico());
            ps.setDouble(4, m.getVlMovimento());
            ps.setString(5, m.getDsDC());

            ps.executeUpdate();
            ps.close();

            JOptionPane.showMessageDialog(null, "Movimento salvo com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar movimento!\n" + ex);
        } finally {
            dataSource.closeDataSource();
        }
    }

    // Excluir registro
    public void excluir(int idMovimento) {
        Connection con = dataSource.getConnection();
        PreparedStatement ps = null;

        try {
            String SQL = "DELETE FROM movimentacao WHERE idMovimento=?";
            ps = con.prepareStatement(SQL);
            ps.setInt(1, idMovimento);

            ps.executeUpdate();
            ps.close();

            JOptionPane.showMessageDialog(null, "Movimento excluído com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir movimento!\n" + ex);
        } finally {
            dataSource.closeDataSource();
        }
    }

    // Alterar registro
    public void alterar(Movimentacao m) {
        Connection con = dataSource.getConnection();
        PreparedStatement ps = null;

        try {
            String SQL = "UPDATE movimentacao SET dtMovimento=?, idReduzida=?, dsHistorico=?, vlMovimento=?, dsDC=? WHERE idMovimento=?";
            ps = con.prepareStatement(SQL);
            ps.setString(1, m.getDtMovimento());
            ps.setInt(2, m.getIdReduzida());
            ps.setString(3, m.getDsHistorico());
            ps.setDouble(4, m.getVlMovimento());
            ps.setString(5, m.getDsDC());
            ps.setInt(6, m.getIdMovimento());

            ps.executeUpdate();
            ps.close();

            JOptionPane.showMessageDialog(null, "Movimento alterado com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao alterar movimento!\n" + ex);
        } finally {
            dataSource.closeDataSource();
        }
    }

}