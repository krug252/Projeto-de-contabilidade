package model;

public class Movimentacao {
    
    private int    idMovimento;
    private String dtMovimento;
    private int    idReduzida;
    private String dsHistorico;
    private double vlMovimento;
    private String dsDC;

    /**
     * @return the idMovimento
     */
    public int getIdMovimento() {
        return idMovimento;
    }

    /**
     * @param idMovimento the idMovimento to set
     */
    public void setIdMovimento(int idMovimento) {
        this.idMovimento = idMovimento;
    }

    /**
     * @return the dtMovimento
     */
    public String getDtMovimento() {
        return dtMovimento;
    }

    /**
     * @param dtMovimento the dtMovimento to set
     */
    public void setDtMovimento(String dtMovimento) {
        this.dtMovimento = dtMovimento;
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
     * @return the dsHistorico
     */
    public String getDsHistorico() {
        return dsHistorico;
    }

    /**
     * @param dsHistorico the dsHistorico to set
     */
    public void setDsHistorico(String dsHistorico) {
        this.dsHistorico = dsHistorico;
    }

    /**
     * @return the vlMovimento
     */
    public double getVlMovimento() {
        return vlMovimento;
    }

    /**
     * @param vlMovimento the vlMovimento to set
     */
    public void setVlMovimento(double vlMovimento) {
        this.vlMovimento = vlMovimento;
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
    
}

