using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace POM.Models
{
    public class Parcela
    {
        public String codigo { get; set; }
        public String nombre { get; set; }
        public String conjunta { get; set; }
        public String variedad { get; set; }
        public String finca { get; set; }
        public String coordenadas { get; set; }
        public String conjuntaTexto { get; set; }

        public Parcela() {}
        public Parcela(String codigo, String nombre, String conjunta, String variedad, String finca)
        {
            this.codigo = codigo;
            this.nombre = nombre;
            this.conjunta = conjunta;
            this.variedad = variedad;
            this.finca = finca;
        }

        public Parcela(String codigo, String nombre, String conjunta, String variedad, String finca, String coordenadas)
        {
            this.codigo = codigo;
            this.nombre = nombre;
            this.conjunta = conjunta;
            this.variedad = variedad;
            this.finca = finca;
            this.coordenadas = coordenadas;
        }
    }
}