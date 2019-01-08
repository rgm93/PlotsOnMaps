using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace POM.Models
{
    public class ListOperacionViewModel
    {
        public List<Operacion> operaciones { get; set; }
        public string OperacionSeleccionado { get; set; }
    }
}