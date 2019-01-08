using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace POM.Models
{
    public class Articulo
    {
        public String codigo { get; set; }
        public String nombre { get; set; }

        public Articulo() {}
        public Articulo(String codigo, String nombre)
        {
            this.codigo = codigo;
            this.nombre = nombre;
        }
    }
}