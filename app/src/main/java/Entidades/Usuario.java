package Entidades;

import java.io.Serializable;

public class Usuario implements Serializable{
    private String cod;
    private String username;
    private String password;
    private String tipo;

    public Usuario(String cod, String username, String password, String tipo) {
        this.cod = cod;
        this.username = username;
        this.password = password;
        this.tipo = tipo;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
