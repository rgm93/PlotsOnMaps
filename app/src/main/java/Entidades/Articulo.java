package Entidades;

public class Articulo {
    private String codArticulo;
    private String nomArticulo;
    private String descripcion;
    private boolean tildado;

    public Articulo(String codArticulo, String nomArticulo, String descripcion, boolean tildado) {
        this.codArticulo = codArticulo;
        this.nomArticulo = nomArticulo;
        this.descripcion = descripcion;
        this.tildado = false;
    }

    public Articulo(String codArticulo, String nomArticulo, boolean tildado) {
        this.codArticulo = codArticulo;
        this.nomArticulo = nomArticulo;
        this.tildado = false;
    }

    public String getNomArticulo() { return nomArticulo; }
    public boolean esTildado() {
        return tildado;
    }

    public void setTildado(boolean tildado) {
        this.tildado = tildado;
    }
}
