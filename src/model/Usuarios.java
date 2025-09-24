package model;

public class Usuarios {
    
    private int    idUsuario;
    private String dsUsuario;
    private String dsEmail;
    private String dsSenha;

    /**
     * @return the idUsuario
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * @param idUsuario the idUsuario to set
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * @return the dsUsuario
     */
    public String getDsUsuario() {
        return dsUsuario;
    }

    /**
     * @param dsUsuario the dsUsuario to set
     */
    public void setDsUsuario(String dsUsuario) {
        this.dsUsuario = dsUsuario;
    }

    /**
     * @return the dsEmail
     */
    public String getDsEmail() {
        return dsEmail;
    }

    /**
     * @param dsEmail the dsEmail to set
     */
    public void setDsEmail(String dsEmail) {
        this.dsEmail = dsEmail;
    }

    /**
     * @return the dsSenha
     */
    public String getDsSenha() {
        return dsSenha;
    }

    /**
     * @param dsSenha the dsSenha to set
     */
    public void setDsSenha(String dsSenha) {
        this.dsSenha = dsSenha;
    }
    
}

