package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.Usuarios;

public class DaoUsuarios {

    private DataSource dataSource;

    public DaoUsuarios(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ArrayList<Usuarios> readAll() {
        PreparedStatement ps = null;

        try {
            String SQL = "SELECT * FROM usuarios";

            //Conecta e faz o comando desjado (acima)
            ps = dataSource.getConnection().prepareStatement(SQL);

            //Executa e consulta no banco
            ResultSet rs = ps.executeQuery();
            ArrayList<Usuarios> lista = new ArrayList<Usuarios>();

            while (rs.next()) {
                Usuarios u = new Usuarios();
                u.setIdUsuario(rs.getInt("idUsuario"));
                u.setDsUsuario(rs.getString("dsUsuario"));
                u.setDsEmail(rs.getString("dsEmail"));
                u.setDsSenha(rs.getString("dsSenha"));

                lista.add(u);
            }

            ps.close();
            dataSource.closeDataSource();

            return lista;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao recuperar dados" + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro Geral " + ex.getMessage());
        }

        dataSource.closeDataSource();

        return null;
    }

    public ArrayList<Usuarios> consulta(String descricao) {

        //Connection con = dataSource.getConnection();
        PreparedStatement ps = null;

        try {
            String SQL = "SELECT * FROM usuarios WHERE dsUsuario LIKE ?";

            // para mandar como uma instrução, precisa usar o PreparedStatement
            // traduz o comando SQL para execução
            ps = dataSource.getConnection().prepareStatement(SQL);
            ps.setString(1, "%" + descricao + "%");

            // executa a consulta no banco
            ResultSet rs = ps.executeQuery();

            // cria a lista de resultados trazidos da tabela
            ArrayList<Usuarios> lista = new ArrayList<Usuarios>();

            while (rs.next()) {
                Usuarios u = new Usuarios();
                u.setIdUsuario(rs.getInt("idUsuario"));
                u.setDsUsuario(rs.getString("dsUsuario"));
                u.setDsEmail(rs.getString("dsEmail"));
                u.setDsSenha(rs.getString("dsSenha"));

                lista.add(u);
            }

            // fecha o statement e o datasource
            ps.close();
            dataSource.closeDataSource();

            // retorna os dados consultados
            return lista;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao recuperar dados " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro geral " + ex.getMessage());
        }

        // fecha o statement e o datasource
        dataSource.closeDataSource();

        // caso aconteça alguma coisa
        return null;
    }

    // insere dados
    public void inserir(Usuarios u) {
        Connection con = dataSource.getConnection();
        PreparedStatement ps = null;

        try {
            String SQL = "INSERT INTO usuarios (dsUsuario, dsEmail, dsSenha) VALUES (?, ?, ?)";
            con = dataSource.getConnection();
            ps = con.prepareStatement(SQL);
            ps.setString(1, u.getDsUsuario());
            ps.setString(2, u.getDsEmail());
            ps.setString(3, u.getDsSenha());

            // executa a inserção no banco
            ps.executeUpdate();
            ps.close();

            JOptionPane.showMessageDialog(null, "Salvo com sucesso!");
        } catch (SQLException ex) {
            //System.err.println("Erro ao salvar os dados "+ex.getMessage());
            JOptionPane.showMessageDialog(null, "Erro ao salvar!\n" + ex);
        } finally {
            // fecha o statement e o datasource
            //ps.close();
            dataSource.closeDataSource();
        }
    }

    /* ROTINA DE EXCLUSÃO DE DADOS */
    // excluir registro
    public void excluir(int codigo) {
        Connection con = dataSource.getConnection();
        PreparedStatement ps = null;

        try {
            String SQL = "DELETE FROM usuarios WHERE idUsuario=?";
            con = dataSource.getConnection();
            ps = con.prepareStatement(SQL);
            ps.setInt(1, codigo);

            // executa a inserção no banco
            ps.executeUpdate();
            ps.close();

            JOptionPane.showMessageDialog(null, "Excluído com sucesso!");
        } catch (SQLException ex) {
            //System.err.println("Erro ao salvar os dados "+ex.getMessage());
            JOptionPane.showMessageDialog(null, "Erro ao excluir!\n" + ex);
        } finally {
            // fecha o statement e o datasource
            dataSource.closeDataSource();
        }
    }

    /* ROTINA DE ALTERAÇÃO DE DADOS */
    // alterar dados
    public void alterar(Usuarios u) {
        Connection con = dataSource.getConnection();
        PreparedStatement ps = null;

        try {
            String SQL = "UPDATE usuarios SET dsUsuario=?, dsEmail=? WHERE idUsuario=?";
            con = dataSource.getConnection();
            ps = con.prepareStatement(SQL);
            ps.setString(1, u.getDsUsuario());
            ps.setString(2, u.getDsEmail());
            ps.setInt(3, u.getIdUsuario());

            // executa a inserção no banco
            ps.executeUpdate();
            ps.close();

            JOptionPane.showMessageDialog(null, "Alterado com sucesso!");
        } catch (SQLException ex) {
            //System.err.println("Erro ao salvar os dados "+ex.getMessage());
            JOptionPane.showMessageDialog(null, "Erro ao alterar!\n" + ex);
        } finally {
            // fecha o statement e o datasource
            dataSource.closeDataSource();
        }
    }
   
    public boolean autenticar(String usuario, String senha) {
        String sql = "SELECT * FROM usuarios WHERE dsUsuario = ? AND dsSenha = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, usuario);
            ps.setString(2, senha);
            
            ResultSet rs = ps.executeQuery();
            return rs.next(); // se encontrou, existe usuário com essa senha
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}