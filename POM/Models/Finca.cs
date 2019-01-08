using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace POM.Models
{
    public class Finca
    {
        public String codigo { get; set; }
        public String nombre { get; set; }
        public String color { get; set; }
        public String usuario { get; set; }

        public Finca() {}
        public Finca(String codigo, String nombre, String color, String usuario)
        {
            this.codigo = codigo;
            this.nombre = nombre;
            this.color = color;
            this.usuario = usuario;
        }
    }
}