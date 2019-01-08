using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace POM.Models
{
    public class Usuario
    {
        public String codigo { get; set; }
        public String username { get; set; }
        public String password { get; set; }
        public String tipo { get; set; }
        public String habilitado { get; set; }

        public Usuario() {}
        public Usuario(String codigo, String username, String password, String tipo, String habilitado)
        {
            this.codigo = codigo;
            this.username = username;
            this.password = password;
            this.tipo = tipo;
            this.habilitado = habilitado;
        }
    }
}