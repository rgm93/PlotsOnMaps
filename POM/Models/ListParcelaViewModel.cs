using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace POM.Models
{
    public class ListParcelaViewModel
    {
        public List<Parcela> fincas { get; set; }
        public string ParcelaSeleccionado { get; set; }
    }
}