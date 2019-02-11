package Entidades;

import java.io.Serializable;

public class Usuario implements Serializable{
    private String cod;
    private String username;
    private String password;
    private String tipo;
    private String activado;

    public Usuario(String cod, String username, String password, String tipo, String activado) {
        this.cod = cod;
        this.username = username;
        this.password = password;
        this.tipo = tipo;
        this.activado = activado;
    }

    public String getCod() {
        return cod;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTipo() {
        return tipo;
    }

    public String getHabilitado() { return activado; }
}
