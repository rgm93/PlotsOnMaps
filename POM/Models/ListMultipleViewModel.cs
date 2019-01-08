using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace POM.Models
{
    public class ListMultipleViewModel
    {
        public List<Finca> fincas { get; set; }
        public List<Parcela> parcelas { get; set; }
        public List<Variedad> variedades { get; set; }
        public List<Operacion> tipoOperaciones { get; set; }
        public List<CoordenadasParcela> coordenadas { get; set; }
        public List<Articulo> articulos { get; set; }
        public List<Operacion> tiposOperaciones { get; set; }
        public List<Usuario> usuarios { get; set; }
        public Filtrar filtro { get; set; }
        public CargarFiltrado filtroRecuperado { get; set; }
        public List<Dibujo> operaciones { get; set; }
    }
}