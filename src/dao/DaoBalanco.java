package dao;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.Balanco;

public class DaoBalanco {
    private DataSource dataSource;
    
    public DaoBalanco(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public ArrayList<Balanco> consultarBalanco(String ano) {
        ArrayList<Balanco> lista = new ArrayList<>();
        try {
            String SQL = """
                SELECT 
                    p.idConta,
                    p.idReduzida,
                    p.dsDescricao,
                    SUM(CASE WHEN m.dsDC = 'D' THEN m.vlMovimento ELSE 0 END) -
                    SUM(CASE WHEN m.dsDC = 'C' THEN m.vlMovimento ELSE 0 END) AS saldo
                FROM planodecontas p
                LEFT JOIN movimentacao m ON p.idReduzida = m.idReduzida
                    AND YEAR(m.dtMovimento) = ?
                GROUP BY p.idConta, p.idReduzida, p.dsDescricao
                ORDER BY p.idConta;
            """;
            
            PreparedStatement ps = dataSource.getConnection().prepareStatement(SQL);
            ps.setString(1, ano);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Balanco b = new Balanco();
                b.setIdConta(rs.getInt("idConta"));
                b.setIdReduzida(rs.getInt("idReduzida"));
                b.setDsDescricao(rs.getString("dsDescricao"));
                
                double saldo = rs.getDouble("saldo");
                b.setSaldo(saldo);
                
                // Define D/C no saldo
                if (saldo >= 0) {
                    b.setDsDC("D");
                } else {
                    b.setDsDC("C");
                    b.setSaldo(saldo * -1); // Exibe saldo positivo mesmo sendo crédito
                }
                
                lista.add(b);
            }
            
            ps.close();
            dataSource.closeDataSource();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao gerar balanço: " + ex.getMessage());
        }
        return lista;
    }
}