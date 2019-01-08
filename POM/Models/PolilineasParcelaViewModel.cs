using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace POM.Models
{
    public class PolilineasParcelaViewModel
    {
        public String codigo { get; set; }
        public String nombre { get; set; }
        public String conjunta { get; set; }
        public String variedad { get; set; }
        public String finca { get; set; }
        public String coordenadas { get; set; }

        public PolilineasParcelaViewModel() {}
    }
}