using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace POM.Models
{
    public class CoordenadasParcela
    {
        public int codigo { get; set; }
        public double latitud { get; set; }
        public double longitud { get; set; }
        public string colorHex { get; set; }
        public String parcela { get; set; }

        public CoordenadasParcela() {}
        public CoordenadasParcela(int codigo, double latitud, double longitud, String parcela)
        {
            this.codigo = codigo;
            this.latitud = latitud;
            this.longitud = longitud;
            this.parcela = parcela;
        }
    }
}