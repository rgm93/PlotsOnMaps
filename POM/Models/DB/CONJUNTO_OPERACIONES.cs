//------------------------------------------------------------------------------
// <auto-generated>
//     Este código se generó a partir de una plantilla.
//
//     Los cambios manuales en este archivo pueden causar un comportamiento inesperado de la aplicación.
//     Los cambios manuales en este archivo se sobrescribirán si se regenera el código.
// </auto-generated>
//------------------------------------------------------------------------------

namespace POM.Models.DB
{
    using System;
    using System.Collections.Generic;
    
    public partial class CONJUNTO_OPERACIONES
    {
        public int CODIGO { get; set; }
        public string NOMBRE { get; set; }
        public string OPERACION { get; set; }
    
        public virtual OPERACION OPERACION1 { get; set; }
    }
}
