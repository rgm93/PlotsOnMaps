﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace POM.Models
{
    public class Filtrar
    {
        public String finca { get; set; }
        public String parcela { get; set; }
        public String variedad { get; set; }
        public String fecha { get; set; }
        public String fecha2 { get; set; }
        public String tipoOperacion { get; set; }
        public String color { get; set; }
        public String articulo { get; set; }
        public String usuario { get; set; }
        public String nombre { get; set; }
        public String codigo { get; set; }

        public Filtrar() {}
        public Filtrar(String finca, String parcela, String color, String variedad, String fecha, String fecha2, String tipoOperacion, String articulo, String usuario, String nombre, String codigo)
        {
            this.finca = finca;
            this.color = color;
            this.parcela = parcela;
            this.variedad = variedad;
            this.fecha = fecha;
            this.fecha2 = fecha2;
            this.tipoOperacion = tipoOperacion;
            this.articulo = articulo;
            this.usuario = usuario;
            this.nombre = nombre;
            this.codigo = codigo;
        }
    }
}