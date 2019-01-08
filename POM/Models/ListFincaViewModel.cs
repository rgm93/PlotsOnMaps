using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace POM.Models
{
    public class ListFincaViewModel
    {
        public List<Finca> fincas { get; set; }
        public string FincaSeleccionado { get; set; }
    }
}