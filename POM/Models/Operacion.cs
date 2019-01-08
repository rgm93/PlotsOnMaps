using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace POM.Models
{
    public class Operacion
    {
        public String codigo { get; set; }
        public String nombre { get; set; }
        public String tiene_articulos { get; set; }

        public Operacion() {}
        public Operacion(String codigo, String nombre, String tiene_articulos)
        {
            this.codigo = codigo;
            this.nombre = nombre;
            this.tiene_articulos = tiene_articulos;
        }
    }
}