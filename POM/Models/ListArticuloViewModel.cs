using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace POM.Models
{
    public class ListArticuloViewModel
    {
        public List<Articulo> articulos { get; set; }
        public string ArticulosSeleccionado { get; set; }
    }
}