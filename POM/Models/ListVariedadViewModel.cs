using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace POM.Models
{
    public class ListVariedadViewModel
    {
        public List<Variedad> variedades { get; set; }
        public string VariedadSeleccionado { get; set; }
    }
}