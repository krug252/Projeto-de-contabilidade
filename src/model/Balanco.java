package model;

public class Balanco {
    /**
     * @return the idConta
     */
    public int getIdConta() {
        return idConta;
    }
    /**
     * @param idConta the idConta to set
     */
    public void setIdConta(int idConta) {
        this.idConta = idConta;
    }
    /**
     * @return the idReduzida
     */
    public int getIdReduzida() {
        return idReduzida;
    }
    /**
     * @param idReduzida the idReduzida to set
     */
    public void setIdReduzida(int idReduzida) {
        this.idReduzida = idReduzida;
    }
    /**
     * @return the dsDescricao
     */
    public String getDsDescricao() {
        return dsDescricao;
    }
    /**
     * @param dsDescricao the dsDescricao to set
     */
    public void setDsDescricao(String dsDescricao) {
        this.dsDescricao = dsDescricao;
    }
    /**
     * @return the dsDC
     */
    public String getDsDC() {
        return dsDC;
    }
    /**
     * @param dsDC the dsDC to set
     */
    public void setDsDC(String dsDC) {
        this.dsDC = dsDC;
    }
    /**
     * @return the saldo
     */
    public double getSaldo() {
        return saldo;
    }
    /**
     * @param saldo the saldo to set
     */
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
    private int idConta;
    private int idReduzida;
    private String dsDescricao;
    private String dsDC; 
    private double saldo;
}