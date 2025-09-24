package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.PlanoDeContas;

public class DaoPlanoDeContas {

    private DataSource dataSource;

    public DaoPlanoDeContas(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Listar todos os registros
    public ArrayList<PlanoDeContas> readAll() {
        PreparedStatement ps = null;

        try {
            String SQL = "SELECT * FROM planodecontas";
            ps = dataSource.getConnection().prepareStatement(SQL);

            ResultSet rs = ps.executeQuery();
            ArrayList<PlanoDeContas> lista = new ArrayList<>();

            while (rs.next()) {
                PlanoDeContas p = new PlanoDeContas();
                p.setIdConta(rs.getString("idConta"));
                p.setIdReduzida(rs.getInt("idReduzida"));
                p.setDsDescricao(rs.getString("dsDescricao"));
                p.setVlExAnterior(rs.getDouble("vlExAnterior"));
                p.setDsExAnteriorDC(rs.getString("dsExAnteriorDC"));
                p.setVlExAtual(rs.getDouble("vlExAtual"));
                p.setDsExAtualDC(rs.getString("dsExAtualDC"));
                p.setVlMesAnterior(rs.getDouble("vlMesAnterior"));
                p.setDsMesAnteriorDC(rs.getString("dsMesAnteriorDC"));
                p.setVlMesAtual(rs.getDouble("vlMesAtual"));
                p.setDsMesAtualDC(rs.getString("dsMesAtualDC"));

                lista.add(p);
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

    // Consultar por descrição
    public ArrayList<PlanoDeContas> consulta(String descricao) {
        PreparedStatement ps = null;

        try {
            String SQL = "SELECT * FROM planodecontas WHERE dsDescricao LIKE ?";
            ps = dataSource.getConnection().prepareStatement(SQL);
            ps.setString(1, "%" + descricao + "%");

            ResultSet rs = ps.executeQuery();
            ArrayList<PlanoDeContas> lista = new ArrayList<>();

            while (rs.next()) {
                PlanoDeContas p = new PlanoDeContas();
                p.setIdConta(rs.getString("idConta"));
                p.setIdReduzida(rs.getInt("idReduzida"));
                p.setDsDescricao(rs.getString("dsDescricao"));
                p.setVlExAnterior(rs.getDouble("vlExAnterior"));
                p.setDsExAnteriorDC(rs.getString("dsExAnteriorDC"));
                p.setVlExAtual(rs.getDouble("vlExAtual"));
                p.setDsExAtualDC(rs.getString("dsExAtualDC"));
                p.setVlMesAnterior(rs.getDouble("vlMesAnterior"));
                p.setDsMesAnteriorDC(rs.getString("dsMesAnteriorDC"));
                p.setVlMesAtual(rs.getDouble("vlMesAtual"));
                p.setDsMesAtualDC(rs.getString("dsMesAtualDC"));

                lista.add(p);
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
    public void inserir(PlanoDeContas p) {
        Connection con = dataSource.getConnection();
        PreparedStatement ps = null;

        try {
            String SQL = "INSERT INTO planodecontas " +
                         "(idConta, idReduzida, dsDescricao, vlExAnterior, dsExAnteriorDC, " +
                         "vlExAtual, dsExAtualDC, vlMesAnterior, dsMesAnteriorDC, vlMesAtual, dsMesAtualDC) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            ps = con.prepareStatement(SQL);
            ps.setString(1, p.getIdConta());
            ps.setInt(2, p.getIdReduzida());
            ps.setString(3, p.getDsDescricao());
            ps.setDouble(4, p.getVlExAnterior());
            ps.setString(5, p.getDsExAnteriorDC());
            ps.setDouble(6, p.getVlExAtual());
            ps.setString(7, p.getDsExAtualDC());
            ps.setDouble(8, p.getVlMesAnterior());
            ps.setString(9, p.getDsMesAnteriorDC());
            ps.setDouble(10, p.getVlMesAtual());
            ps.setString(11, p.getDsMesAtualDC());

            ps.executeUpdate();
            ps.close();

            JOptionPane.showMessageDialog(null, "Plano de contas salvo com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar plano de contas!\n" + ex);
        } finally {
            dataSource.closeDataSource();
        }
    }

    // Excluir registro
    public void excluir(String idConta) {
        Connection con = dataSource.getConnection();
        PreparedStatement ps = null;

        try {
            String SQL = "DELETE FROM planodecontas WHERE idConta=?";
            ps = con.prepareStatement(SQL);
            ps.setString(1, idConta);

            ps.executeUpdate();
            ps.close();

            JOptionPane.showMessageDialog(null, "Plano de contas excluído com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir plano de contas!\n" + ex);
        } finally {
            dataSource.closeDataSource();
        }
    }

    // Alterar registro
    public void alterar(PlanoDeContas p) {
        Connection con = dataSource.getConnection();
        PreparedStatement ps = null;

        try {
            String SQL = "UPDATE planodecontas SET " +
                         "idReduzida=?, dsDescricao=?, vlExAnterior=?, dsExAnteriorDC=?, " +
                         "vlExAtual=?, dsExAtualDC=?, vlMesAnterior=?, dsMesAnteriorDC=?, " +
                         "vlMesAtual=?, dsMesAtualDC=? WHERE idConta=?";

            ps = con.prepareStatement(SQL);
            ps.setInt(1, p.getIdReduzida());
            ps.setString(2, p.getDsDescricao());
            ps.setDouble(3, p.getVlExAnterior());
            ps.setString(4, p.getDsExAnteriorDC());
            ps.setDouble(5, p.getVlExAtual());
            ps.setString(6, p.getDsExAtualDC());
            ps.setDouble(7, p.getVlMesAnterior());
            ps.setString(8, p.getDsMesAnteriorDC());
            ps.setDouble(9, p.getVlMesAtual());
            ps.setString(10, p.getDsMesAtualDC());
            ps.setString(11, p.getIdConta());

            ps.executeUpdate();
            ps.close();

            JOptionPane.showMessageDialog(null, "Plano de contas alterado com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao alterar plano de contas!\n" + ex);
        } finally {
            dataSource.closeDataSource();
        }
    }
}