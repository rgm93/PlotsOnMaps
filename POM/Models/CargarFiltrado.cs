using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace POM.Models
{
    public class CargarFiltrado
    {
        public List<List<CoordenadasOperaciones>> coordenadas;
        public List<int> nDibujos;
        public List<Dibujo> operaciones;
        public List<Parcela> parcelas;

        public CargarFiltrado() {}
        public CargarFiltrado(List<List<CoordenadasOperaciones>> coordenadas, List<int> nDibujos, List<Dibujo> operaciones, List<Parcela> parcelas)
        {
            this.coordenadas = coordenadas;
            this.nDibujos = nDibujos;
            this.operaciones = operaciones;
            this.parcelas = parcelas;
        }
    }
}