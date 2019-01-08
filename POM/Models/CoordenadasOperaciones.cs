using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace POM.Models
{
    public class CoordenadasOperaciones
    {
        public int codigo { get; set; }
        public double latitud { get; set; }
        public double longitud { get; set; }
        public int trozo { get; set; }
        public String colorHex { get; set; }
        public String operacion { get; set; }

        public CoordenadasOperaciones() {}
        public CoordenadasOperaciones(int codigo, double latitud, double longitud, int trozo, String operacion)
        {
            this.codigo = codigo;
            this.latitud = latitud;
            this.longitud = longitud;
            this.trozo = trozo;
            this.operacion = operacion;
        }
    }
}