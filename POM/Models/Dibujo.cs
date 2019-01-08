using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace POM.Models
{
    public class Dibujo
    {
        public String CODIGO { get; set; }
        public String NOMBRE { get; set; }
        public String COLOR { get; set; }
        public String TIPO { get; set; }
        public String FECHA { get; set; }
        public String ARTICULOS { get; set; }
        public String OBSERVACIONES { get; set; }
        public int TROZOS { get; set; }
        public String PARCELA { get; set; }
        public String FINCA { get; set; }
        public String USUARIO { get; set; }
        public String COORDENADAS { get; set; }

        public Dibujo() {}

        public Dibujo(String codigo, String nombre, String color, String tipo, String fecha, String articulos, String observaciones, int trozos, String parcela, String finca, String usuario)
        {
            this.CODIGO = codigo;
            this.NOMBRE = nombre;
            this.COLOR = color;
            this.TIPO = tipo;
            this.FECHA = fecha;
            this.ARTICULOS = articulos;
            this.OBSERVACIONES = observaciones;
            this.TROZOS = trozos;
            this.PARCELA = parcela;
            this.FINCA = finca;
            this.USUARIO = usuario;
        }

        public Dibujo(String codigo, String nombre, String color, String tipo, String fecha, String articulos, String observaciones, int trozos, String parcela, String finca, String usuario, String coordenadas)
        {
            this.CODIGO = codigo;
            this.NOMBRE = nombre;
            this.COLOR = color;
            this.TIPO = tipo;
            this.FECHA = fecha;
            this.ARTICULOS = articulos;
            this.OBSERVACIONES = observaciones;
            this.TROZOS = trozos;
            this.PARCELA = parcela;
            this.USUARIO = usuario;
            this.FINCA = finca;
            this.COORDENADAS = coordenadas;
        }
    }
}