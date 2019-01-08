using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace POM.Models
{
    public class CargarParcelas
    {
        public List<List<CoordenadasParcela>> coordenadas;
        public Boolean eliminado;

        public CargarParcelas() {}
        public CargarParcelas(List<List<CoordenadasParcela>> coordenadas)
        {
            this.coordenadas = coordenadas;
        }
    }
}