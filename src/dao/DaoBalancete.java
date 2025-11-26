package dao;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.Balancete;

public class DaoBalancete {

    private DataSource dataSource;

    public DaoBalancete(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public ArrayList<Balancete> consultarBalancete(String dataInicio, String dataFim) {
        ArrayList<Balancete> lista = new ArrayList<>();

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
                    AND m.dtMovimento BETWEEN ? AND ?
                GROUP BY p.idConta, p.idReduzida, p.dsDescricao
                ORDER BY p.idConta;
            """;

            PreparedStatement ps = dataSource.getConnection().prepareStatement(SQL);
            ps.setString(1, dataInicio);
            ps.setString(2, dataFim);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Balancete b = new Balancete();
                b.setIdConta(rs.getInt("idConta"));
                b.setIdReduzida(rs.getInt("idReduzida"));
                b.setDsDescricao(rs.getString("dsDescricao"));

                double saldo = rs.getDouble("saldo");
                b.setSaldo(saldo);

                // define D/C no saldo
                if (saldo >= 0) {
                    b.setDsDC("D");
                } else {
                    b.setDsDC("C");
                    b.setSaldo(saldo * -1); // exibe saldo positivo mesmo sendo crédito
                }

                lista.add(b);
            }

            ps.close();
            dataSource.closeDataSource();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao gerar balancete: " + ex.getMessage());
        }

        return lista;
    }
}