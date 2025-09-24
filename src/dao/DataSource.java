package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DataSource {
    // variáveis para a conexão
    private String hostname;
    private int    porta;
    private String database;
    private String username;
    private String password;
    
    // Variáveis de conexão
    private Connection connection;
    
    //pedido/abertura de conexão
    public DataSource(){
        try{
            //seta valores nas variáveis de conexão
            hostname = "192.168.20.5";
            porta = 3306;
            database = "contabilidadekrug";
            username = "root";
            password = "12345";
                    
            //string de conexão
            String url = "jdbc:mysql://"+hostname+":"+porta+"/"+database+"?useTimezone=true&serverTimezone=UTC";
            
            //registrar o driver
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            
            // faz a conexão
            connection = DriverManager.getConnection(url, username, password);
        }
        catch (SQLException ex){
            JOptionPane.showMessageDialog(null, "Erro na conexão"+ex.getMessage());
        }
        catch (Exception ex){
            JOptionPane.showMessageDialog(null, "ERRO GERAL"+ex.getMessage());
        }
    }
    
    //pega a conexão ativa
    public Connection getConnection(){
        return this.connection;
    }
    
    //fechamnento da conexão
    public void closeDataSource(){
        try{
            connection.close();
        }
        catch (SQLException ex){
            JOptionPane.showMessageDialog(null, "Erro ao desconectar" + ex.getMessage());
        }
    }
}