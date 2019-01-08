using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace POM.Models
{
    public class ListUserViewModel
    {
        public List<Usuario> usuarios { get; set; }
        public SelectList Usr { get; set; }
        public string UsuarioSeleccionado { get; set; }
        public string UsuarioSeleccionadoText { get; set; }
    }
}