using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace POM.Models
{
    public class Variedad
    {
        public String codigo { get; set; }
        public String nombre { get; set; }

        public Variedad() {}
        public Variedad(String codigo, String nombre)
        {
            this.codigo = codigo;
            this.nombre = nombre;
        }
    }
}