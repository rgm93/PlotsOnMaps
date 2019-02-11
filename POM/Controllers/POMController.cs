using DevExpress.Web.Mvc;
using POM.Models;
using POM.Models.DB;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.Web.Mvc;
using System.Data.Entity;
using System.Linq;
using System.Text;
using System.Collections;

namespace POM.Controllers
{
    public class POMController : Controller
    {
        public const string EditResultKey = "EditResult";
        Usuario usuario;

        public POMController() { }

        public ActionResult Login()
        {
            Session["usuario"] = null;
            return View();
        }
        public ActionResult Home()
        {
            if (Session["usuario"] != null)
            {
                cargarParcelasEnMapa();
                cargarOperacionesEnMapa();
                return View();
            }

            return RedirectToAction("Login");

        }

        public ActionResult Filtrado(Filtrar f)
        {
            if (Session["usuario"] != null)
            {
                AzureDB azure = new AzureDB();
                azure.Conectar();

                cargarParcelasEnMapa();

                if (f.parcela != null)
                {
                    Filtrar faux = Filtrar(f);
                    cargarOperacionesEnMapa(faux);
                }

                else cargarOperacionesEnMapa();

                /*List<Finca> fincas = new List<Finca>();
                fincas = azure.filtrarFincas();

                List<SelectListItem> li = new List<SelectListItem>();
                li.Add(new SelectListItem { Text = "Todas las fincas", Value = "0" });
                for (int i = 0; i < fincas.Count; i++)
                {
                    li.Add(new SelectListItem { Text = fincas[i].nombre, Value = (i + 1).ToString() });
                }

                List<Variedad> variedades = new List<Variedad>();
                variedades = azure.filtrarVariedades();

                List<SelectListItem> li2 = new List<SelectListItem>();
                li2.Add(new SelectListItem { Text = "Todas las variedades", Value = "0" });
                for (int i = 0; i < variedades.Count; i++)
                {
                    li2.Add(new SelectListItem { Text = variedades[i].nombre, Value = (i + 1).ToString() });
                }

                List<Parcela> parcelas = new List<Parcela>();
                parcelas = azure.filtrarParcelas();
                //parcelas = azure.filtrarParcelas();
                List<SelectListItem> li3 = new List<SelectListItem>();
                li3.Add(new SelectListItem { Text = "Todas las parcelas", Value = "0" });
                /*for (int i = 0; i < parcelas.Count; i++)
                {
                    li3.Add(new SelectListItem { Text = parcelas[i].nombre, Value = (i + 1).ToString() });
                }

                List<Articulo> articulos = new List<Articulo>();
                articulos = azure.filtrarArticulos();
                List<SelectListItem> li4 = new List<SelectListItem>();
                li4.Add(new SelectListItem { Text = "Todos los artículos", Value = "0" });
                for (int i = 0; i < articulos.Count; i++)
                {
                    li4.Add(new SelectListItem { Text = articulos[i].nombre, Value = (i + 1).ToString() });
                }

                List<Operacion> tiposOperacion = new List<Operacion>();
                tiposOperacion = azure.filtrarOperaciones();
                List<SelectListItem> li5 = new List<SelectListItem>();
                li5.Add(new SelectListItem { Text = "Todos los tipos de operaciones", Value = "0" });
                for (int i = 0; i < tiposOperacion.Count; i++)
                {
                    li5.Add(new SelectListItem { Text = tiposOperacion[i].nombre, Value = (i + 1).ToString() });
                }

                List<Usuario> usuarios = new List<Usuario>();
                usuarios = azure.filtrarUsuarios();
                List<SelectListItem> li6 = new List<SelectListItem>();
                li6.Add(new SelectListItem { Text = "Todos los usuarios", Value = "0" });
                for (int i = 0; i < usuarios.Count; i++)
                {
                    li6.Add(new SelectListItem { Text = usuarios[i].username, Value = (i + 1).ToString() });
                }*/
                
                /*ViewData["Fincas"] = li;
                ViewData["Variedades"] = li2;
                ViewData["Parcelas"] = li3;
                ViewData["Articulos"] = li4;
                ViewData["TiposOperaciones"] = li5;
                ViewData["Usuarios"] = li6;*/

                return View();
            }

            return RedirectToAction("Login");
            
        }


        /**/

        [HttpPost]
        public ActionResult ObtenerFiltrado(Filtrar filter)
        {
            string filtrado = (string)Session["query"];
            if (filtrado != null)
            {
                string[] v = filtrado.Split('[', ']', ' ', ')', '\'', '>', '<', '=');
                List<String> cadena = new List<String>();

                for (int i = 0; i < v.Count(); i++)
                {
                    cadena.Add(v[i]);
                }

                /*cadena.RemoveAll("StartsWith(");
                cadena.RemoveAll(",");
                cadena.RemoveAll(" ");*/

                for (int i = 0; i < cadena.Count; i++)
                {
                    if (cadena[i] == "" || cadena[i] == "Contains(" || cadena[i] == "," || cadena[i] == "And" || cadena[i] == " " || cadena[i] == "=")
                    {
                        cadena.RemoveAt(i);
                        i--;
                    }
                }

                for (int i = 0; i < cadena.Count; i++)
                {
                    switch (cadena[i])
                    {
                        case "CODIGO":
                            filter.codigo = cadena[i + 1];
                            break;
                        case "NOMBRE":
                            filter.nombre = cadena[i + 1];
                            break;
                        case "COLOR":
                            filter.color = cadena[i + 1];
                            break;
                        case "TIPO":
                            filter.tipoOperacion = cadena[i + 1];
                            break;
                        case "ARTICULOS":
                            filter.articulo = cadena[i + 1];
                            break;
                        case "FECHA":
                            filter.fecha = cadena[i + 1];
                            break;
                        case "FECHA2":
                            filter.fecha2 = cadena[i + 1];
                            break;
                        case "PARCELA":
                            filter.parcela = cadena[i + 1];
                            break;
                        case "FINCA":
                            filter.finca = cadena[i + 1];
                            break;
                        case "USUARIO":
                            filter.usuario = cadena[i + 1];
                            break;
                    }
                    i++;
                }

                AzureDB azure = new AzureDB();
                azure.Conectar();

                Filtrar faux = Filtrar(filter);
                CargarFiltrado cf = cargarOperacionesEnMapa(faux);

                //Filtrado(f);

                /*List<Object> resultados = new List<Object>();
                resultados.Add(ViewBag.PomsOP);
                resultados.Add(ViewBag.PomsNDibujos);
                resultados.Add(ViewBag.Operaciones);*/

                //return PartialView("Filtrado", f);
                //return Json(resultados);
                Session["query"] = "";
                return Json(cf, JsonRequestBehavior.AllowGet);
            }
            else
            {
                CargarFiltrado cf = cargarOperacionesEnMapaSinFiltro();
                return Json(cf, JsonRequestBehavior.AllowGet);
            }
        }


        public ActionResult Parcelas()
        {
            if (Session["usuario"] != null)
            {
                AzureDB azure = new AzureDB();
                azure.Conectar();

                List<Finca> fincas = new List<Finca>();
                fincas = azure.filtrarFincas();

                List<SelectListItem> li = new List<SelectListItem>();
                li.Add(new SelectListItem { Text = "Selecciona una finca", Value = "0" });
                for (int i = 0; i < fincas.Count; i++)
                {
                    li.Add(new SelectListItem { Text = fincas[i].nombre, Value = (i + 1).ToString() });
                }

                List<Variedad> variedades = new List<Variedad>();
                variedades = azure.filtrarVariedades();

                List<SelectListItem> li2 = new List<SelectListItem>();
                li2.Add(new SelectListItem { Text = "Selecciona una variedad", Value = "0" });
                for (int i = 0; i < variedades.Count; i++)
                {
                    li2.Add(new SelectListItem { Text = variedades[i].nombre, Value = (i + 1).ToString() });
                }

                List<Parcela> parcelas = new List<Parcela>();
                parcelas = azure.filtrarParcelas();

                List<SelectListItem> li3 = new List<SelectListItem>();
                li3.Add(new SelectListItem { Text = "Selecciona una parcela", Value = "0" });
                for (int i = 0; i < parcelas.Count; i++)
                {
                    li3.Add(new SelectListItem { Text = parcelas[i].nombre, Value = (i + 1).ToString() });
                }

                PlotsOnMapsDBEntities4 pmd = new PlotsOnMapsDBEntities4();
                for (int i = 0; i < parcelas.Count; i++)
                {
                    FINCA f = pmd.FINCA.Find(parcelas[i].finca);
                    VARIEDAD v = pmd.VARIEDAD.Find(parcelas[i].variedad);
                    parcelas[i].finca = f.NOMBRE;
                    parcelas[i].variedad = v.NOMBRE;
                }

                ViewData["Fincas"] = li;
                ViewData["Variedades"] = li2;
                ViewData["Parcelas"] = li3;
                ViewData["TablaParcelas"] = parcelas;

                cargarParcelasEnMapa();

                return View();
            }
            return RedirectToAction("Login");
        }

        public void cargarParcelasEnMapa()
        {
            List<List<CoordenadasParcela>> cp = ObtenerParcelas();

            List<String> coordsPolys = new List<string>();

            for (int i = 0; i < cp.Count; i++)
            {
                string polys = "[";
                for (int j = 0; j < cp[i].Count; j++)
                {
                    String lat = cp[i][j].latitud.ToString();
                    String lng = cp[i][j].longitud.ToString();
                    lat = lat.Replace(',', '.');
                    lng = lng.Replace(',', '.');
                    polys += "{";
                    polys += "lat: " + lat + ",";
                    polys += "lng: " + lng;
                    polys += "},";
                }

                polys += "];";
                coordsPolys.Add(polys);
            }

            ViewBag.Poms = cp;
        }

        public void cargarOperacionesEnMapa()
        {
            List<List<CoordenadasOperaciones>> cp = ObtenerOperaciones();

            List<String> coordsPolys = new List<string>();

            AzureDB azure = new AzureDB();
            azure.Conectar();
            List<Parcela> parcelas = azure.filtrarParcelas();
            List<int> nDibujos = new List<int>();
            List<Dibujo> operaciones;
            operaciones = azure.filtrarDibujos();

            for (int i = 0; i < cp.Count; i++)
            {
                nDibujos.Add(azure.obtenerNumDibujos(operaciones[i]));

            }

            for (int i = 0; i < operaciones.Count(); i++)
            {
                operaciones[i].FINCA = azure.obtenerNombreFinca(operaciones[i].FINCA);
                String parcN = azure.obtenerNombreParcela(operaciones[i].PARCELA);
                if(!parcN.Equals(""))  operaciones[i].PARCELA = parcN;
                operaciones[i].TIPO = azure.obtenerNombreTipoOperacion(operaciones[i].TIPO);
            }

            for (int i = 0; i < parcelas.Count(); i++)
            {
                parcelas[i].finca = azure.obtenerNombreFinca(parcelas[i].finca);
                parcelas[i].variedad = azure.obtenerNombreVariedad(parcelas[i].variedad);
            }

            ViewBag.PomsParcelas = parcelas;
            ViewBag.PomsOP = cp;
            ViewBag.PomsNDibujos = nDibujos;
            ViewBag.PomsOperaciones = operaciones;
        }

        public CargarFiltrado cargarOperacionesEnMapa(Filtrar f)
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            List<Parcela> parcelas = azure.filtrarParcelas();
            List<int> nDibujos = new List<int>();
            List<Dibujo> operaciones = azure.filtrarOperaciones(f);
            List<List<CoordenadasOperaciones>> cp = ObtenerOperaciones(operaciones);

            for (int i = 0; i < cp.Count; i++)
            {
                nDibujos.Add(azure.obtenerNumDibujos(operaciones[i]));

            }

            for (int i = 0; i < operaciones.Count(); i++)
            {
                operaciones[i].FINCA = azure.obtenerNombreFinca(operaciones[i].FINCA);
                String parcN = azure.obtenerNombreParcela(operaciones[i].PARCELA);
                if (!parcN.Equals("")) operaciones[i].PARCELA = parcN;
                operaciones[i].TIPO = azure.obtenerNombreTipoOperacion(operaciones[i].TIPO);
            }

            return new CargarFiltrado(cp, nDibujos, operaciones, parcelas);
        }

        public CargarFiltrado cargarOperacionesEnMapaSinFiltro()
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            List<Parcela> parcelas = azure.filtrarParcelas();
            List<int> nDibujos = new List<int>();
            List<Dibujo> operaciones = azure.filtrarOperacionesSinFiltro();
            List<List<CoordenadasOperaciones>> cp = ObtenerOperaciones(operaciones);

            for (int i = 0; i < cp.Count; i++)
            {
                nDibujos.Add(azure.obtenerNumDibujos(operaciones[i]));

            }

            for (int i = 0; i < operaciones.Count(); i++)
            {
                operaciones[i].FINCA = azure.obtenerNombreFinca(operaciones[i].FINCA);
                String parcN = azure.obtenerNombreParcela(operaciones[i].PARCELA);
                if (!parcN.Equals("")) operaciones[i].PARCELA = parcN;
                operaciones[i].TIPO = azure.obtenerNombreTipoOperacion(operaciones[i].TIPO);
            }

            return new CargarFiltrado(cp, nDibujos, operaciones, parcelas);
        }

        public List<List<CoordenadasParcela>> recuperarParcelas()
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            List<List<CoordenadasParcela>> cp = ObtenerParcelas();
            
            return cp;
        }

        public ActionResult Autentication()
        {
            Usuario u = new Usuario();
            if (!Request.Form["username"].ToString().Equals("") && !Request.Form["password"].ToString().Equals(""))
            {
                AzureDB azure = new AzureDB();
                azure.Conectar();
                u = azure.comprobarUsuario(Request.Form["username"].ToString(), Request.Form["password"].ToString());
                if (u != null)
                {
                    usuario = u;
                    Session["usuario"] = u;
                    return RedirectToAction("Home");
                }
            }

            return RedirectToAction("Login");
        }


        [HttpGet]
        public ActionResult Usuarios()
        {
            if (Session["usuario"] != null)
            {
                AzureDB azure = new AzureDB();
                azure.Conectar();
                List<Usuario> usuarios = new List<Usuario>();
                usuarios = azure.filtrarUsuarios();

                List<SelectListItem> li = new List<SelectListItem>();
                li.Add(new SelectListItem { Text = "Selecciona un usuario", Value = "0" });
                for (int i = 0; i < usuarios.Count; i++)
                {
                    li.Add(new SelectListItem { Text = usuarios[i].username, Value = (i + 1).ToString() });
                }
                ViewData["Usuarios"] = li;
                PlotsOnMapsDBEntities4 pomdb = new PlotsOnMapsDBEntities4();
                return View(pomdb.USUARIO);
            }

            return RedirectToAction("Login");
        }

        [HttpGet]
        public ActionResult Fincas()
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            List<Finca> fincas = new List<Finca>();
            fincas = azure.filtrarFincas();

            List<SelectListItem> li = new List<SelectListItem>();
            li.Add(new SelectListItem { Text = "Selecciona una finca", Value = "0" });
            for (int i = 0; i < fincas.Count; i++)
            {
                li.Add(new SelectListItem { Text = fincas[i].nombre, Value = (i + 1).ToString() });
            }
            ViewData["Fincas"] = li;
            return View();
        }

        [HttpGet]
        public ActionResult Variedades(string busqueda)
        {
            if (Session["usuario"] != null)
            {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            List<Variedad> variedades = new List<Variedad>();
            variedades = azure.filtrarVariedades();

            List<SelectListItem> li = new List<SelectListItem>();
            li.Add(new SelectListItem { Text = "Selecciona una variedad", Value = "0" });
            for (int i = 0; i < variedades.Count; i++)
            {
                li.Add(new SelectListItem { Text = variedades[i].nombre, Value = (i + 1).ToString() });
            }
            ViewData["Variedades"] = li;
            PlotsOnMapsDBEntities4 pomdb = new PlotsOnMapsDBEntities4();
            return View(pomdb.VARIEDAD);
            }
            
            return RedirectToAction("Login");
        }

        [HttpGet]
        public ActionResult Articulos()
        {
            if (Session["usuario"] != null)
            {
                AzureDB azure = new AzureDB();
                azure.Conectar();
                List<Articulo> articulos = new List<Articulo>();
                articulos = azure.filtrarArticulos();

                List<SelectListItem> li = new List<SelectListItem>();
                li.Add(new SelectListItem { Text = "Selecciona un articulo", Value = "0" });
                for (int i = 0; i < articulos.Count; i++)
                {
                    li.Add(new SelectListItem { Text = articulos[i].nombre, Value = (i + 1).ToString() });
                }
                ViewData["Articulos"] = li;
                PlotsOnMapsDBEntities4 pomdb = new PlotsOnMapsDBEntities4();
                return View(pomdb.ARTICULO);
            }

            return RedirectToAction("Login");
        }

        [HttpGet]
        public ActionResult Operaciones()
        {
            if (Session["usuario"] != null)
            {
                AzureDB azure = new AzureDB();
                azure.Conectar();
                List<Operacion> operaciones = new List<Operacion>();
                operaciones = azure.filtrarOperaciones();

                List<SelectListItem> li = new List<SelectListItem>();
                li.Add(new SelectListItem { Text = "Selecciona una operación", Value = "0" });
                for (int i = 0; i < operaciones.Count; i++)
                {
                    li.Add(new SelectListItem { Text = operaciones[i].nombre, Value = (i + 1).ToString() });
                }
                ViewData["Operaciones"] = li;
                PlotsOnMapsDBEntities4 pomdb = new PlotsOnMapsDBEntities4();
                return View(pomdb.TIPO_OPERACION);
            }
            
            return RedirectToAction("Login");
        }

        public ActionResult CrearUsuario()
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            if (!azure.obtenerCODUsuario(Request.Form["codigo"].ToString())) {
                if (Request.Form["password"].ToString().Equals(Request.Form["password2"].ToString()))
                {
                    bool creado = azure.crearUsuario(Request.Form["codigo"], Request.Form["username"].ToString(), Request.Form["password"].ToString(), Request.Form["tipo"].ToString());
                    if (!creado)
                    {
                        Console.WriteLine("Usuario creado");
                    }
                }
            }

            else
            {
                Console.WriteLine("Error");
            }

            return RedirectToAction("Usuarios");
        }

        [HttpPost]
        public ActionResult ModificarUsuario()
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            //String n = fm["smu"].ToString();
            int pos = int.Parse(Request.Form["UsuariosL"].ToString()) - 1;
            List<Usuario> usuarios = new List<Usuario>();
            usuarios = azure.filtrarUsuarios();
            String username_selected = usuarios[pos].username;

            if (azure.comprobarUsuario(username_selected))
            {
                if (Request.Form["password_m"].ToString().Equals(Request.Form["password2_m"].ToString()))
                {
                    String username = Request.Form["username_m"].ToString();
                    String pass = Request.Form["password_m"].ToString();
                    String pass2 = Request.Form["password2_m"].ToString();
                    if (!username.Equals("") && !pass.Equals("") && !pass2.Equals(""))
                    {
                        bool modificado = azure.modificarUsuario(username_selected, username, pass);
                        if (!modificado)
                        {
                            Console.WriteLine("Usuario modificado");
                        }
                    }
                    else
                    {
                        Console.WriteLine("Error");
                    }
                }
            }

            else
            {
                Console.WriteLine("Error");
            }

            azure.Cerrar();

            return RedirectToAction("Usuarios");
        }

        /*[HttpPost]
        public ActionResult EliminarUsuario(Usuario u)
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            String cod = azure.obtenerCodigo(u.codigo, "USUARIO");
            bool eliminado = false;
            if (!azure.contieneOperacionCampo(cod, "USUARIO"))
            {
                eliminado = azure.eliminarUsuario(cod);
            }

            return Json(eliminado, JsonRequestBehavior.AllowGet);
        }*/

        [HttpPost]
        public ActionResult EliminarParcela(Parcela p)
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            CargarParcelas cf = new CargarParcelas();
            cf.eliminado = false;
            if(!p.codigo.Equals("0"))
            {
                String cod = azure.obtenerCodigo(p.codigo, "PARCELA", "CODIGO");
                if (!azure.contieneOperacionCampo(cod, "PARCELA"))
                {
                    cf.eliminado = azure.eliminarParcela(cod);
                    cf.coordenadas = recuperarParcelas();
                }
            }

            return Json(cf, JsonRequestBehavior.AllowGet);
        }

        public ActionResult CrearVariedad()
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            if (!azure.obtenerCODVariedad(Request.Form["codigo"].ToString()))
            {
                bool creado = azure.crearVariedad(Request.Form["codigo"], Request.Form["nombre"].ToString());
                if (!creado)
                {
                    Console.WriteLine("Variedad creada");
                }
            }

            else
            {
                Console.WriteLine("Error");
            }
            azure.Cerrar();

            return RedirectToAction("Variedades");
        }

        [HttpPost]
        public ActionResult ModificarVariedad()
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            //String n = fm["smu"].ToString();
            int pos = int.Parse(Request.Form["VariedadL"].ToString()) - 1;
            List<Variedad> variedades = new List<Variedad>();
            variedades = azure.filtrarVariedades();
            String variedad_selected = variedades[pos].nombre;

            if (azure.comprobarVariedad(variedad_selected))
            {
                String nombre = Request.Form["nombre"].ToString();
                if (!nombre.Equals(""))
                {
                    bool modificado = azure.modificarVariedad(variedad_selected, nombre);
                    if (!modificado)
                    {
                        Console.WriteLine("Variedad modificada");
                    }
                }
                else
                {
                    Console.WriteLine("Error");
                }
            }

            else
            {
                Console.WriteLine("Error");
            }

            azure.Cerrar();

            return RedirectToAction("Variedades");
        }

        /*[HttpPost]
        public ActionResult EliminarVariedad(Variedad v)
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            bool eliminado = false;
            String cod = azure.obtenerCodigo(v.codigo, "VARIEDAD");
            if (!azure.contieneOperacionVariedad(cod))
            {
                eliminado = azure.eliminarVariedad(cod);
            }
            return Json(eliminado, JsonRequestBehavior.AllowGet);
        }*/

        public ActionResult CrearArticulo()
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            if (!azure.obtenerCODArticulo(Request.Form["codigo"].ToString()))
            {
                bool creado = azure.crearArticulo(Request.Form["codigo"], Request.Form["nombre"].ToString());
                if (!creado)
                {
                    Console.WriteLine("Articulo creado");
                }
            }

            else
            {
                Console.WriteLine("Error");
            }

            azure.Cerrar();

            return RedirectToAction("Articulos");
        }

        [HttpPost]
        public ActionResult ModificarArticulo()
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            //String n = fm["smu"].ToString();
            int pos = int.Parse(Request.Form["ArticuloL"].ToString()) - 1;
            List<Articulo> articulos = new List<Articulo>();
            articulos = azure.filtrarArticulos();
            String articulo_selected = articulos[pos].nombre;

            if (azure.comprobarArticulo(articulo_selected))
            {
                String nombre = Request.Form["nombre"].ToString();
                if (!nombre.Equals(""))
                {
                    bool modificado = azure.modificarArticulo(articulo_selected, nombre);
                    if (!modificado)
                    {
                        Console.WriteLine("Articulo modificado");
                    }
                }
                else
                {
                    Console.WriteLine("Error");
                }
            }

            else
            {
                Console.WriteLine("Error");
            }

            azure.Cerrar();

            return RedirectToAction("Articulos");
        }

        /*[HttpPost]
        public ActionResult EliminarArticulo(Articulo a)
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            String cod = azure.obtenerCodigo(a.codigo, "ARTICULO");
            bool eliminado = false;
            if (!azure.contieneOperacionCampo(cod, "ARTICULOS"))
            {
                eliminado = azure.eliminarArticulo(cod);
            }

            return Json(eliminado, JsonRequestBehavior.AllowGet);
        }*/

        public ActionResult CrearOperacion()
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            if (!azure.obtenerCODOperacion(Request.Form["codigo"].ToString()))
            {
                bool creado = azure.crearOperacion(Request.Form["codigo"], Request.Form["nombre"].ToString(), Request.Form["tiene"].ToString());
                if (!creado)
                {
                    Console.WriteLine("Operación creada");
                }
            }

            else
            {
                Console.WriteLine("Error");
            }

            azure.Cerrar();

            return RedirectToAction("Operaciones");
        }

        [HttpPost]
        public ActionResult ModificarOperacion()
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            int pos = int.Parse(Request.Form["OperacionL"].ToString()) - 1;
            List<Operacion> operaciones = new List<Operacion>();
            operaciones = azure.filtrarOperaciones();
            String operacion_selected = operaciones[pos].nombre;

            if (azure.comprobarOperacion(operacion_selected))
            {
                String nombre = Request.Form["nombre"].ToString();
                String tiene = Request.Form["tiene"].ToString();
                if (!nombre.Equals(""))
                {
                    bool modificado = azure.modificarOperacion(operacion_selected, nombre, tiene);
                    if (!modificado)
                    {
                        Console.WriteLine("Operación modificada");
                    }
                }
                else
                {
                    Console.WriteLine("Error");
                }
            }

            else
            {
                Console.WriteLine("Error");
            }

            azure.Cerrar();

            return RedirectToAction("Operaciones");
        }

        /*[HttpPost]
        public ActionResult EliminarOperacion(Operacion tp)
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            bool eliminado = false;
            String cod = azure.obtenerCodigo(tp.codigo, "TIPO_OPERACION");
            if (!azure.contieneOperacionCampo(cod, "TIPO"))
            {
                eliminado = azure.eliminarOperacion(cod);
            }
            return Json(eliminado, JsonRequestBehavior.AllowGet);
        }*/

        [HttpPost]
        public ActionResult CrearFinca(Finca f)
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            using (PlotsOnMapsDBEntities4 entities = new PlotsOnMapsDBEntities4())
            {
                var model = entities.FINCA;
                if (!azure.obtenerCODParcela(f.codigo))
                {
                    usuario = Session["usuario"] as Usuario;
                    if (usuario != null)
                    {
                        f.codigo = f.codigo.ToUpper();
                        f.nombre = f.nombre.ToUpper();
                        bool creado = azure.crearFinca(f.codigo, f.nombre, f.color, usuario.codigo);
                        if (!creado)
                        {
                            FINCA finca = new FINCA();
                            finca.CODIGO = f.codigo;
                            finca.NOMBRE = f.nombre;
                            finca.COLOR = f.color;
                            finca.USUARIO = usuario.codigo;
                            var modelItem = model.FirstOrDefault(it => it.CODIGO == finca.CODIGO);
                            if (modelItem != null)
                            {
                                return Json(obtenerFincasFiltradas(azure), JsonRequestBehavior.AllowGet);
                            }
                        }
                    }
                }
            }

            azure.Cerrar();

            return Json(String.Format("'Success':'false'"));
        }

        public List<SelectListItem> obtenerFincasFiltradas (AzureDB azure) {
            List<Finca> fincas = new List<Finca>();
            fincas = azure.filtrarFincas();
            List<SelectListItem> li = new List<SelectListItem>();
            li.Add(new SelectListItem { Text = "Selecciona una finca", Value = "0" });
            for (int i = 0; i<fincas.Count; i++)
            {
                li.Add(new SelectListItem { Text = fincas[i].nombre, Value = (i + 1).ToString() });
            }
            return li;
        }

        public List<List<CoordenadasParcela>> ObtenerParcelas()
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            
            List<Parcela> parcelas;
            parcelas = azure.filtrarParcelas();

            List<List<CoordenadasParcela>> cp = new List<List<CoordenadasParcela>>();
            for (int i = 0; i < parcelas.Count; i++)
            {
                cp.Add(azure.obtenerCoordenadasParcelas(parcelas[i]));
            }

            azure.Cerrar();

            return cp;
        }

        public List<List<CoordenadasOperaciones>> ObtenerOperaciones()
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();

            List<Dibujo> operaciones;
            operaciones = azure.filtrarDibujos();

            List<List<CoordenadasOperaciones>> cp = new List<List<CoordenadasOperaciones>>();
            for (int i = 0; i < operaciones.Count; i++)
            {
                cp.Add(azure.obtenerCoordenadasOperaciones(operaciones[i]));
            }

            azure.Cerrar();

            return cp;
        }

        public List<List<CoordenadasOperaciones>> ObtenerOperaciones(List<Dibujo> operaciones)
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();

            List<List<CoordenadasOperaciones>> cp = new List<List<CoordenadasOperaciones>>();
            for (int i = 0; i < operaciones.Count; i++)
            {
                cp.Add(azure.obtenerCoordenadasOperaciones(operaciones[i]));
            }

            azure.Cerrar();

            return cp;
        }

        [HttpPost]
        public JsonResult ObtenerParcelasDeFinca(Filtrar filtro)
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            int posF = int.Parse(filtro.finca.ToString()) - 1;
            List<Finca> fincas = new List<Finca>();
            List<Parcela> parcelas = new List<Parcela>();

            if (posF != -1)
            {
                fincas = azure.filtrarFincas();
                String finca_selected = fincas[posF].codigo;
                parcelas = azure.filtrarParcelas(finca_selected);
            }

            azure.Cerrar();

            return Json(parcelas, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        public JsonResult ObtenerDatosDeParcela(Filtrar filtro)
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            int posF = int.Parse(filtro.finca.ToString()) - 1;
            List<Finca> fincas = new List<Finca>();
            List<Parcela> parcelas = new List<Parcela>();
            Variedad variedad = new Variedad();
            if (posF != -1)
            {
                fincas = azure.filtrarFincas();
                String finca_selected = fincas[posF].codigo;
                parcelas = azure.filtrarParcelas(finca_selected);
                int posP = int.Parse(filtro.parcela.ToString()) - 1;
                String parcela_selected = parcelas[posP].codigo;
            }

            azure.Cerrar();

            return Json(variedad, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        public ActionResult CrearParcela(Parcela p)
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            
            int posV = int.Parse(p.variedad.ToString()) - 1;
            List<Variedad> variedades = new List<Variedad>();
            variedades = azure.filtrarVariedades();
            String variedad_selected = variedades[posV].codigo;

            int posF = int.Parse(p.finca.ToString()) - 1;
            List<Finca> fincas = new List<Finca>();
            fincas = azure.filtrarFincas();
            String finca_selected = fincas[posF].codigo;

            String coordenates = p.coordenadas;
            List<float[]> listaCoordenadas = new List<float[]>();
            string[] aux = p.coordenadas.Split(',');
            for (int i = 0; i < aux.Length; i=i+2)
            {
                float lat, log;
                float.TryParse(aux[i], NumberStyles.Any, CultureInfo.InvariantCulture, out lat);
                float.TryParse(aux[i+1], NumberStyles.Any, CultureInfo.InvariantCulture, out log);
                float[] coord = { lat, log };
                
                listaCoordenadas.Add(coord);
            }

            if (!azure.obtenerCODParcela(p.codigo) && listaCoordenadas.Count() != 0)
            {
                Parcela parc = new Parcela(p.codigo, p.nombre, p.conjunta, p.variedad, p.finca, p.coordenadas);
                bool creado = azure.crearParcela(p.codigo, p.nombre, "1", variedad_selected, finca_selected, listaCoordenadas);
                if (!creado)
                {
                    Console.WriteLine("Parcela creada");
                    return Json(parc, JsonRequestBehavior.AllowGet);
                }
            }

            else
            {
                Console.WriteLine("Error");
            }

            azure.Cerrar();

            return Json(String.Format("'Success':'false'"));
        }

        [HttpPost]
        public List<String> ObtenerCoordenadasParcelas()
        {
            
            List<List<CoordenadasParcela>> cp = ObtenerParcelas();

            List<String> coordsPolys = new List<string>();

            for (int i = 0; i < cp.Count; i++)
            {
                string polys = "[";
                for (int j = 0; j < cp[i].Count; j++)
                {
                    String lat = cp[i][j].latitud.ToString();
                    String lng = cp[i][j].longitud.ToString();
                    lat = lat.Replace(',', '.');
                    lng = lng.Replace(',', '.');
                    polys += "{";
                    polys += "lat: " + lat + ",";
                    polys += "lng: " + lng;
                    polys += "},";
                }

                polys += "];";
                coordsPolys.Add(polys);
            }
            
            return coordsPolys;
        }

        [HttpPost]
        public List<String> ObtenerCoordenadasOperaciones()
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();

            List<Dibujo> operaciones;
            operaciones = azure.filtrarDibujos();

            List<List<CoordenadasOperaciones>> cp = ObtenerOperaciones();

            List<String> coordsPolys = new List<string>();

            for (int i = 0; i < cp.Count; i++)
            {
                string polys = "[";
                int j = 0;
                List<int> nTrozos = new List<int>();

                for (int k = 0; k < operaciones.Count; k++)
                {
                    nTrozos[k] = azure.obtenerNumDibujos(operaciones[k]);
                }
               
                while(j < cp[i].Count)
                {
                    Console.WriteLine("Coordenada: " + cp[i][j].latitud.ToString() + "," + cp[i][j].longitud.ToString() + " - trozo: " + cp[i][j].trozo);
                    if (cp[i][j].trozo + 1 > nTrozos[i])
                    {
                        polys += "];";
                        coordsPolys.Add(polys);
                    }
                    String lat = cp[i][j].latitud.ToString();
                    String lng = cp[i][j].longitud.ToString();
                    lat = lat.Replace(',', '.');
                    lng = lng.Replace(',', '.');
                    polys += "{";
                    polys += "lat: " + lat + ",";
                    polys += "lng: " + lng;
                    polys += "},";

                    j++;
                }
            }

            azure.Cerrar();

            return coordsPolys;
        }

        [HttpPost]
        public List<String> ObtenerCoordenadasOperaciones(Filtrar f, AzureDB azure)
        {
            List<Dibujo> operaciones;
            operaciones = azure.filtrarDibujos(f);

            List<List<CoordenadasOperaciones>> cp = ObtenerOperaciones(operaciones);

            List<String> coordsPolys = new List<string>();

            for (int i = 0; i < cp.Count; i++)
            {
                string polys = "[";
                int j = 0;
                List<int> nTrozos = new List<int>();

                for (int k = 0; k < operaciones.Count; k++)
                {
                    nTrozos[k] = azure.obtenerNumDibujos(operaciones[k]);
                }

                while (j < cp[i].Count)
                {
                    Console.WriteLine("Coordenada: " + cp[i][j].latitud.ToString() + "," + cp[i][j].longitud.ToString() + " - trozo: " + cp[i][j].trozo);
                    if (cp[i][j].trozo + 1 > nTrozos[i])
                    {
                        polys += "];";
                        coordsPolys.Add(polys);
                    }
                    String lat = cp[i][j].latitud.ToString();
                    String lng = cp[i][j].longitud.ToString();
                    lat = lat.Replace(',', '.');
                    lng = lng.Replace(',', '.');
                    polys += "{";
                    polys += "lat: " + lat + ",";
                    polys += "lng: " + lng;
                    polys += "},";

                    j++;
                }
            }

            return coordsPolys;
        }

        [HttpPost]
        public Filtrar Filtrar(Filtrar f)
        {
            //azure.Conectar();
            
            /*int posF = int.Parse(f.finca.ToString()) - 1;
            String finca_selected = "";
            if (posF != -1)
            {
                List<Finca> fincas = new List<Finca>();
                fincas = azure.filtrarFincas();
                finca_selected = fincas[posF].codigo;
            }

            String parcela_selected = "";
            if (f.parcela.ToString() != "Todas las parcelas")
            {
                parcela_selected = azure.obtenerCODParcelaDesdeFinca(finca_selected, f.parcela.ToString());
            }

            int posV = int.Parse(f.variedad.ToString()) - 1;
            String variedad_selected = "";
            if(posV != -1)
            {
                List<Variedad> variedades = new List<Variedad>();
                variedades = azure.filtrarVariedades();
                variedad_selected = variedades[posV].codigo;
            }

            String fecha_selected = f.fecha;

            int posOp = int.Parse(f.tipoOperacion.ToString()) - 1;
            String tipoOperacion_selected = "";
            if(posOp != -1) {
                List<Operacion> operaciones = new List<Operacion>();
                operaciones = azure.filtrarOperaciones();
                tipoOperacion_selected = operaciones[posOp].codigo;
            }
           
            int posPd = int.Parse(f.articulo.ToString()) - 1;
            String producto_selected = "";
            if(posPd != -1)
            {
                List<Articulo> productos = new List<Articulo>();
                productos = azure.filtrarArticulos();
                producto_selected = productos[posPd].codigo;
            }

            int posU = int.Parse(f.usuario.ToString()) - 1;
            String usuario_selected = "";
            if (posU != -1)
            {
                List<Usuario> usuarios = new List<Usuario>();
                usuarios = azure.filtrarUsuarios();
                usuario_selected = usuarios[posU].codigo;
            }*/

            Filtrar faux;
            faux = new Filtrar(f.finca, f.parcela, f.color, f.variedad, f.fecha, f.fecha2, f.tipoOperacion, f.articulo, f.usuario, f.nombre, f.codigo);
            return faux;
        }

        [HttpPost]
        public JsonResult InsertarUsuario(USUARIO objeto)
        {
            bool insertado = false;
            using (PlotsOnMapsDBEntities4 entities = new PlotsOnMapsDBEntities4())
            {
                USUARIO existe = entities.USUARIO.Find(objeto.CODIGO);
                var objetos = entities.USUARIO;
                bool existe2 = false;
                foreach (var item in objetos)
                {
                    if (item.USERNAME.Equals(objeto.USERNAME))
                    {
                        existe2 = true;
                    }
                }
                if (existe == null && !existe2)
                {
                    entities.USUARIO.Add(objeto);
                    entities.SaveChanges();
                    insertado = true;
                }
            }

            return Json(insertado);
        }

        [HttpPost]
        public ActionResult ActualizarUsuario(USUARIO objeto)
        {
            bool actualizado = false;
            using (PlotsOnMapsDBEntities4 entities = new PlotsOnMapsDBEntities4())
            {
                USUARIO updated = entities.USUARIO.Find(objeto.CODIGO);
                var objetos = entities.USUARIO;
                bool existe = false;
                foreach (var item in objetos)
                {
                    if (!item.USERNAME.Equals(objeto.USERNAME))
                    {
                        if (item.USERNAME.Equals(objeto.USERNAME))
                        {
                            existe = true;
                        }
                    }
                }
                if (!existe)
                {
                    updated.USERNAME = objeto.USERNAME;
                    updated.PASS = objeto.PASS;
                    updated.TIPO = objeto.TIPO;
                    updated.ACTIVADO = objeto.ACTIVADO;
                    entities.SaveChanges();
                    actualizado = true;
                }
            }

            return Json(actualizado);
        }

        [HttpPost]
        public ActionResult EliminarUsuario(USUARIO objeto)
        {
            bool encontrado = false;
            PlotsOnMapsDBEntities4 entities = new PlotsOnMapsDBEntities4();
            USUARIO removedUsuario = entities.USUARIO.Find(objeto.CODIGO);
            OPERACION existe = entities.OPERACION.Find(objeto.CODIGO);
            if (existe == null)
            {
                entities.USUARIO.Remove(removedUsuario);
                entities.SaveChanges();
                encontrado = true;
            }
            
            return Json(encontrado);
        }

        [HttpPost]
        public ActionResult BuscarUsuario(string busqueda)
        {
            List<Usuario> lista = new List<Usuario>();
            AzureDB azure = new AzureDB();
            azure.Conectar();
            lista = azure.filtrarUsuarios();
            azure.Cerrar();
            var listaBusqueda = lista.Where(s => s.username.ToUpper().Contains(busqueda.ToUpper()) || s.username.ToUpper().Contains(busqueda.ToUpper()));

            return Json(listaBusqueda, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        public JsonResult InsertarVariedad(VARIEDAD objeto)
        {
            bool insertado = false;
            using (PlotsOnMapsDBEntities4 entities = new PlotsOnMapsDBEntities4())
            {
                VARIEDAD existe = entities.VARIEDAD.Find(objeto.CODIGO);
                var objetos = entities.VARIEDAD;
                bool existe2 = false;
                foreach (var item in objetos)
                {
                    if (item.NOMBRE.Equals(objeto.NOMBRE))
                    {
                        existe2 = true;
                    }
                }
                if (existe == null && !existe2)
                {
                    objeto.NOMBRE = objeto.NOMBRE.ToUpper();
                    entities.VARIEDAD.Add(objeto);
                    entities.SaveChanges();
                    insertado = true;
                }
                    
            }

            return Json(insertado);
        }

        [HttpPost]
        public ActionResult ActualizarVariedad(VARIEDAD objeto)
        {
            bool encontrado = false;
            using (PlotsOnMapsDBEntities4 entities = new PlotsOnMapsDBEntities4())
            {
                VARIEDAD updated = entities.VARIEDAD.Find(objeto.CODIGO);
                var objetos = entities.VARIEDAD;
                bool existe = false;
                foreach (var item in objetos)
                {
                    if (!item.NOMBRE.Equals(objeto.NOMBRE))
                    {
                        if (item.NOMBRE.Equals(objeto.NOMBRE))
                        {
                            existe = true;
                        }
                    }
                }
                if (!existe)
                {
                    updated.NOMBRE = objeto.NOMBRE;
                    entities.SaveChanges();
                    encontrado = true;
                }
               
            }

            return Json(encontrado);
        }

        [HttpPost]
        public ActionResult EliminarVariedad(VARIEDAD objeto)
        {
            bool encontrado = false;
            PlotsOnMapsDBEntities4 entities = new PlotsOnMapsDBEntities4();
            VARIEDAD removed = entities.VARIEDAD.Find(objeto.CODIGO);
            PARCELA existe = entities.PARCELA.Find(objeto.CODIGO);
            if (existe == null)
            {
                entities.VARIEDAD.Remove(removed);
                entities.SaveChanges();
                encontrado = true;
            }

            return Json(encontrado);
        }

        static string RemoveDiacritics(string text)
        {
            var normalizedString = text.Normalize(NormalizationForm.FormD);
            var stringBuilder = new StringBuilder();

            foreach (var c in normalizedString)
            {
                var unicodeCategory = CharUnicodeInfo.GetUnicodeCategory(c);
                if (unicodeCategory != UnicodeCategory.NonSpacingMark)
                {
                    stringBuilder.Append(c);
                }
            }

            return stringBuilder.ToString().Normalize(NormalizationForm.FormC);
        }

        [HttpPost]
        public ActionResult BuscarVariedad(string busqueda)
        {
            List<Variedad> variedades = new List<Variedad>();
            AzureDB azure = new AzureDB();
            azure.Conectar();
            variedades = azure.filtrarVariedades();
            azure.Cerrar();
            var variedadesBusqueda = variedades.Where(s => s.nombre.ToUpper().Contains(busqueda.ToUpper()) || s.nombre.ToUpper().Contains(busqueda.ToUpper()));
            
            return Json(variedadesBusqueda, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        public JsonResult InsertarArticulo(ARTICULO objeto)
        {
            bool insertado = false;
            using (PlotsOnMapsDBEntities4 entities = new PlotsOnMapsDBEntities4())
            {
                ARTICULO existe = entities.ARTICULO.Find(objeto.CODIGO);
                var objetos = entities.ARTICULO;
                bool existe2 = false;
                foreach (var item in objetos)
                {
                    if (item.NOMBRE.Equals(objeto.NOMBRE))
                    {
                        existe2 = true;
                    }
                }
                if (existe == null && !existe2)
                {
                    objeto.CODIGO = objeto.CODIGO.ToUpper();
                    objeto.NOMBRE = objeto.NOMBRE.ToUpper();
                    entities.ARTICULO.Add(objeto);
                    entities.SaveChanges();
                    insertado = true;
                }

            }

            return Json(insertado);
        }

        [HttpPost]
        public ActionResult ActualizarArticulo(ARTICULO objeto)
        {
            bool actualizado = false;
            using (PlotsOnMapsDBEntities4 entities = new PlotsOnMapsDBEntities4())
            {
                ARTICULO updated = entities.ARTICULO.Find(objeto.CODIGO);
                var objetos = entities.ARTICULO;
                bool existe = false;
                foreach (var item in objetos)
                {
                    if (!item.NOMBRE.Equals(objeto.NOMBRE))
                    {
                        if (item.NOMBRE.Equals(objeto.NOMBRE))
                        {
                            existe = true;
                        }
                    }
                }
                if (!existe)
                {
                    updated.NOMBRE = objeto.NOMBRE;
                    entities.SaveChanges();
                    actualizado = true;
                }
               
            }

            return Json(actualizado);
        }

        [HttpPost]
        public ActionResult EliminarArticulo(ARTICULO objeto)
        {
            bool encontrado = false;
            PlotsOnMapsDBEntities4 entities = new PlotsOnMapsDBEntities4();
            ARTICULO removed = entities.ARTICULO.Find(objeto.CODIGO);
            OPERACION existe = entities.OPERACION.Find(objeto.CODIGO);
            if (existe == null)
            {
                entities.ARTICULO.Remove(removed);
                entities.SaveChanges();
                encontrado = true;
            }

            return Json(encontrado);
        }

        [HttpPost]
        public ActionResult BuscarArticulo(string busqueda)
        {
            List<Articulo> lista = new List<Articulo>();
            AzureDB azure = new AzureDB();
            azure.Conectar();
            lista = azure.filtrarArticulos();
            azure.Cerrar();
            var listaBusqueda = lista.Where(s => s.nombre.ToUpper().Contains(busqueda.ToUpper()) || s.nombre.ToUpper().Contains(busqueda.ToUpper()));

            return Json(listaBusqueda, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        public JsonResult InsertarTipoOperacion(TIPO_OPERACION objeto)
        {
            bool insertado = false;
            using (PlotsOnMapsDBEntities4 entities = new PlotsOnMapsDBEntities4())
            {
                TIPO_OPERACION existe = entities.TIPO_OPERACION.Find(objeto.CODIGO);
                var objetos = entities.TIPO_OPERACION;
                bool existe2 = false;
                foreach (var item in objetos)
                {
                    if (item.NOMBRE.Equals(objeto.NOMBRE))
                    {
                        existe2 = true;
                    }
                }
                if (existe == null && !existe2)
                {
                    objeto.CODIGO = objeto.CODIGO.ToUpper();
                    objeto.NOMBRE = objeto.NOMBRE.ToUpper();
                    entities.TIPO_OPERACION.Add(objeto);
                    entities.SaveChanges();
                    insertado = true;
                }

            }

            return Json(insertado);
        }

        [HttpPost]
        public ActionResult ActualizarOperacionFiltrado(OPERACION objeto)
        {
            bool actualizado = false;
            using (PlotsOnMapsDBEntities4 entities = new PlotsOnMapsDBEntities4())
            {
                OPERACION updated = entities.OPERACION.Find(objeto.CODIGO);
                var objetos = entities.OPERACION;
                bool existe = false;
                foreach (var item in objetos)
                {
                    if (!item.NOMBRE.Equals(objeto.NOMBRE))
                    {
                        if (item.NOMBRE.Equals(objeto.NOMBRE))
                        {
                            existe = true;
                        }
                    }
                }
                if (!existe)
                {
                    AzureDB azure = new AzureDB();
                    azure.Conectar();
                    updated.NOMBRE = objeto.NOMBRE;
                    updated.FECHA = objeto.FECHA;
                    updated.OBSERVACIONES = objeto.OBSERVACIONES;
                    updated.COLOR = azure.obtenerColorInt(objeto.COLOR);
                    entities.SaveChanges();
                    actualizado = true;
                    azure.Cerrar();
                }

            }

            return Json(actualizado);
        }

        [HttpPost]
        public ActionResult ActualizarTipoOperacion(TIPO_OPERACION objeto)
        {
            bool actualizado = false;
            using (PlotsOnMapsDBEntities4 entities = new PlotsOnMapsDBEntities4())
            {
                TIPO_OPERACION updated = entities.TIPO_OPERACION.Find(objeto.CODIGO);
                var objetos = entities.TIPO_OPERACION;
                bool existe = false;
                foreach (var item in objetos)
                {
                    if (!item.NOMBRE.Equals(objeto.NOMBRE))
                    {
                        if (item.NOMBRE.Equals(objeto.NOMBRE))
                        {
                            existe = true;
                        }
                    }
                }
                if (!existe)
                {
                    AzureDB azure = new AzureDB();
                    azure.Conectar();
                    updated.NOMBRE = objeto.NOMBRE;
                    updated.TIENE_ARTICULOS = objeto.TIENE_ARTICULOS == "SI" ? "1" : "0";
                    updated.COLOR = azure.obtenerColorInt(objeto.COLOR);

                    String codtipo = objeto.CODIGO.ToString();
                    string codop = entities.Database.SqlQuery<string>("select CODIGO from OPERACION where TIPO = '" + codtipo + "'").FirstOrDefault();
                    OPERACION updated2 = entities.OPERACION.Find(codop);
                    updated2.COLOR = azure.obtenerColorInt(objeto.COLOR);
                    entities.SaveChanges();
                    actualizado = true;
                    azure.Cerrar();
                }

               

            }

            return Json(actualizado);
        }
        
        [HttpPost]
        public ActionResult EliminarTipoOperacion(TIPO_OPERACION objeto)
        {
            bool encontrado = false;
            PlotsOnMapsDBEntities4 entities = new PlotsOnMapsDBEntities4();
            TIPO_OPERACION removed = entities.TIPO_OPERACION.Find(objeto.CODIGO);
            OPERACION existe = entities.OPERACION.Find(objeto.CODIGO);
            if (existe == null)
            {
                entities.TIPO_OPERACION.Remove(removed);
                entities.SaveChanges();
                encontrado = true;
            }

            return Json(encontrado);
        }

        [HttpPost]
        public ActionResult BuscarTipoOperacion(string busqueda)
        {
            List<Operacion> lista = new List<Operacion>();
            AzureDB azure = new AzureDB();
            azure.Conectar();
            lista = azure.filtrarOperaciones();
            azure.Cerrar();
            var listaBusqueda = lista.Where(s => s.nombre.ToUpper().Contains(busqueda.ToUpper()) || s.nombre.ToUpper().Contains(busqueda.ToUpper()));

            return Json(listaBusqueda, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        public ActionResult ActualizarFinca(FINCA objeto)
        {
            bool actualizado = false;
            using (PlotsOnMapsDBEntities4 entities = new PlotsOnMapsDBEntities4())
            {
                FINCA updated = entities.FINCA.Find(objeto.CODIGO);
                var objetos = entities.FINCA;
                bool existe = false;
                foreach (var item in objetos)
                {
                    if (!item.NOMBRE.Equals(objeto.NOMBRE))
                    {
                        if (item.NOMBRE.Equals(objeto.NOMBRE))
                        {
                            existe = true;
                        }
                    }
                }
                if (!existe)
                {
                    AzureDB azure = new AzureDB();
                    azure.Conectar();
                    updated.NOMBRE = objeto.NOMBRE;
                    updated.PARCELA = objeto.PARCELA;
                    updated.COLOR = azure.obtenerColorInt(objeto.COLOR);
                    entities.SaveChanges();
                    actualizado = true;
                    azure.Cerrar();
                }
            }

            return Json(actualizado);
        }

        [HttpPost]
        public ActionResult ActualizarParcela(PARCELA objeto)
        {
            bool actualizado = false;
            using (PlotsOnMapsDBEntities4 entities = new PlotsOnMapsDBEntities4())
            {
                AzureDB azure = new AzureDB();
                azure.Conectar();
                PARCELA updated = entities.PARCELA.Find(objeto.CODIGO);
                var objetos = entities.PARCELA;
                bool existe = false;
                foreach (var item in objetos)
                {
                    if (!item.NOMBRE.Equals(objeto.NOMBRE))
                    {
                        if (item.NOMBRE.Equals(objeto.NOMBRE))
                        {
                            existe = true;
                        }
                    }
                }
                if (!existe)
                {
                    updated.NOMBRE = objeto.NOMBRE;
                    updated.CONJUNTA = objeto.CONJUNTA;
                    updated.VARIEDAD = azure.obtenerCodigo(objeto.VARIEDAD, "VARIEDAD", "NOMBRE");
                    updated.CONJUNTA = objeto.CONJUNTA == "SI" ? "1" : "0";
                    entities.SaveChanges();
                    actualizado = true;
                }
                azure.Cerrar();
            }

            return Json(actualizado);
        }

        // ¿Borrar las funciones anteriores?

        PlotsOnMapsDBEntities4 db = new PlotsOnMapsDBEntities4();

        [ValidateInput(false)]
        public ActionResult GridViewPartialUsuarios()
        {
            var model = db.USUARIO;
            return PartialView("_GridViewPartialUsuarios", model.ToList());
        }

        [HttpPost, ValidateInput(false)]
        public ActionResult GridViewPartialUsuariosUpdate([ModelBinder(typeof(DevExpressEditorsBinder))] USUARIO item)
        {
            var model = db.USUARIO;
            if (ModelState.IsValid)
            {
                bool existe2 = false;
                foreach (var i in model)
                {
                    if (i.USERNAME.Equals(item.USERNAME) && !i.CODIGO.Equals(item.CODIGO))
                    {
                        existe2 = true;
                    }
                }
                if (!existe2)
                {
                    try
                    {
                        var modelItem = model.FirstOrDefault(it => it.CODIGO == item.CODIGO);
                        if (modelItem != null)
                        {
                            item.USERNAME = item.USERNAME.ToUpper();
                            ActualizarUsuario(item);
                            model = db.USUARIO;
                            UpdateModel(model);
                            db.SaveChanges();
                            ViewData[EditResultKey] = string.Format("Actualizado el usuario: '{0}' - Recarga la página para ver los cambios", item.USERNAME);
                        }
                        else ViewData[EditResultKey] = string.Format("No es posible actualizar el usuario: '{0}'", item.USERNAME);
                    }
                    catch (Exception e)
                    {
                        ViewData["EditError"] = e.Message;
                    }
                }
            }
            
            return PartialView("_GridViewPartialUsuarios", model.ToList());
        }
        [HttpPost, ValidateInput(false)]
        public ActionResult GridViewPartialUsuariosDelete([ModelBinder(typeof(DevExpressEditorsBinder))] USUARIO item)
        {
            var model = db.USUARIO;
            var modelFinca = db.FINCA;
            var modelOperacion = db.OPERACION;

            if (item.CODIGO != null)
            {
                try
                {
                    var itemDelete = model.FirstOrDefault(it => it.CODIGO == item.CODIGO);
                    var itemDelete2 = modelFinca.FirstOrDefault(it => it.USUARIO == item.CODIGO);
                    var itemDelete3 = modelOperacion.FirstOrDefault(it => it.USUARIO == item.CODIGO);

                    if ((itemDelete.TIPO.Equals("ADMIN") && itemDelete2 == null) || (itemDelete.TIPO.Equals("TECNICO") && itemDelete3 == null))
                    {
                        model.Remove(itemDelete);
                        db.SaveChanges();
                        ViewData[EditResultKey] = string.Format("Eliminado el usuario: '{0}'", itemDelete.USERNAME);
                    }
                    else ViewData[EditResultKey] = string.Format("No es posible eliminar el usuario: '{0}'", itemDelete.USERNAME);
                }
                catch (Exception e)
                {
                    ViewData["EditError"] = e.Message;
                }
            }

            return PartialView("_GridViewPartialUsuarios", model.ToList());
        }

        PlotsOnMapsDBEntities4 db1 = new PlotsOnMapsDBEntities4();

        [ValidateInput(false)]
        public ActionResult GridViewPartialFincas()
        {
            var model = db1.FINCA;
            AzureDB azure = new AzureDB();
            azure.Conectar();
            List<FINCA> p = model.ToList();
            for (int i = 0; i < model.Count(); i++)
            {
                p[i].COLOR = azure.obtenerColor(p[i].COLOR);
                p[i].USUARIO = azure.obtenerNombreUsuario(p[i].USUARIO);
            }
            azure.Cerrar();
              
            return PartialView("_GridViewPartialFincas", p.ToList());
        }
        
        [HttpPost, ValidateInput(false)]
        public ActionResult GridViewPartialFincasUpdate([ModelBinder(typeof(DevExpressEditorsBinder))] FINCA item)
        {
            var model = db1.FINCA;
            if (ModelState.IsValid)
            {
                try
                {
                    /*var modelItem = model.FirstOrDefault(it => it.NOMBRE == item.NOMBRE);
                    if (modelItem != null)
                    {
                        item.NOMBRE = item.NOMBRE.ToUpper();
                        ActualizarFinca(item);
                        model = db1.FINCA;
                        UpdateModel(model);
                        db1.SaveChanges();
                    }*/

                    ActualizarFinca(item);
                    AzureDB azure = new AzureDB();
                    azure.Conectar();
                    List<FINCA> p = model.ToList();
                    for (int i = 0; i < model.Count(); i++)
                    {
                        p[i].COLOR = azure.obtenerColor(p[i].COLOR);
                        p[i].USUARIO = azure.obtenerNombreUsuario(p[i].USUARIO);
                    }
                    azure.Cerrar();
                    ViewData[EditResultKey] = string.Format("Actualizada la finca: '{0}', recarga el mapa para ver los cambios", item.NOMBRE);

                }
                catch (Exception e)
                {
                    ViewData["EditError"] = e.Message;
                }
            }
            else ViewData[EditResultKey] = string.Format("No es posible actualizar la finca: '{0}'", item.NOMBRE);

            /*List<FINCA> p = model.ToList();
            AzureDB azure = new AzureDB();
            azure.Conectar();
            for (int i = 0; i < model.Count(); i++)
            {
                p[i].COLOR = azure.obtenerColor(p[i].COLOR);
                p[i].USUARIO = azure.obtenerNombreUsuario(p[i].USUARIO);
            }
            azure.Cerrar();*/
            return PartialView("_GridViewPartialFincas", model.ToList());



            /*
                    ActualizarParcela(item);
                    AzureDB azure = new AzureDB();
                    azure.Conectar();
                    List<PARCELA> p = model.ToList();
                    for (int i = 0; i < model.Count(); i++)
                    {
                        p[i].FINCA = azure.obtenerNombreFinca(p[i].FINCA);
                        p[i].VARIEDAD = azure.obtenerNombreVariedad(p[i].VARIEDAD);
                    }
                    azure.Cerrar();
             */
        }
        [HttpPost, ValidateInput(false)]
        public ActionResult GridViewPartialFincasDelete([ModelBinder(typeof(DevExpressEditorsBinder))] FINCA item)
        {
            var model = db1.FINCA;
            var modelParcela = db1.PARCELA;
            var modelOperacion = db1.OPERACION;
            if (item.CODIGO != null)
            {
                try
                {
                    var itemDelete = model.FirstOrDefault(it => it.CODIGO == item.CODIGO);
                    var itemDelete2 = modelParcela.FirstOrDefault(it => it.FINCA == item.CODIGO);
                    var itemDelete3 = modelOperacion.FirstOrDefault(it => it.FINCA == item.CODIGO);
                    if (itemDelete != null && itemDelete2 == null && itemDelete3 == null)
                    {
                        model.Remove(itemDelete);
                        db1.SaveChanges();
                        ViewData[EditResultKey] = string.Format("Eliminada la finca: '{0}', recarga el mapa para ver los cambios", itemDelete.NOMBRE);
                    }
                    else ViewData[EditResultKey] = string.Format("No es posible eliminar la finca: '{0}'", itemDelete.NOMBRE);
                     AzureDB azure = new AzureDB();
                    azure.Conectar();
                    List<FINCA> p = model.ToList();
                    for (int i = 0; i < model.Count(); i++)
                    {
                        p[i].COLOR = azure.obtenerColor(p[i].COLOR);
                        p[i].USUARIO = azure.obtenerNombreUsuario(p[i].USUARIO);
                    }
                    azure.Cerrar();
                    
                }
                catch (Exception e)
                {
                   
                    ViewData["EditError"] = e.Message;
                }
            }
            return PartialView("_GridViewPartialFincas", model.ToList());
        }

        PlotsOnMapsDBEntities4 db2 = new PlotsOnMapsDBEntities4();

        [ValidateInput(false)]
        public ActionResult GridViewPartialParcelas()
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            var model = db2.PARCELA;
            List<PARCELA> p = model.ToList();
            for (int i = 0; i < model.Count(); i++)
            {
                p[i].FINCA = azure.obtenerNombreFinca(p[i].FINCA);
                p[i].VARIEDAD = azure.obtenerNombreVariedad(p[i].VARIEDAD);
                p[i].CONJUNTA = p[i].CONJUNTA == "1" ? "SI" : "NO";

            }
            azure.Cerrar();
            return PartialView("_GridViewPartialParcelas", p.ToList());
        }
        
        [HttpPost, ValidateInput(false)]
        public ActionResult GridViewPartialParcelasUpdate([ModelBinder(typeof(DevExpressEditorsBinder))] PARCELA item)
        {
            var model = db2.PARCELA;
            if (ModelState.IsValid)
            {
                try
                {
                    ActualizarParcela(item);
                    AzureDB azure = new AzureDB();
                    azure.Conectar();
                    List<PARCELA> p = model.ToList();
                    for (int i = 0; i < model.Count(); i++)
                    {
                        p[i].FINCA = azure.obtenerNombreFinca(p[i].FINCA);
                        p[i].VARIEDAD = azure.obtenerNombreVariedad(p[i].VARIEDAD);
                        p[i].CONJUNTA = p[i].CONJUNTA == "1" ? "SI" : "NO";
                    }
                    azure.Cerrar();
                    ViewData[EditResultKey] = string.Format("Actualizada la parcela: '{0}'", item.NOMBRE);
                    //GridViewPartialParcelas.grid.JSProperties["cpMessage"] = string.Format("Recargue el mapa para ver los resultados");

                    /*var modelItem = model.FirstOrDefault(it => it.CODIGO == item.CODIGO);
                    if (modelItem != null)
                    {
                        this.UpdateModel(modelItem);
                        db2.SaveChanges();
                    }*/
                }
                catch (Exception e)
                {
                    ViewData["EditError"] = e.Message;
                }
            }
            else ViewData[EditResultKey] = string.Format("No es posible actualizar la parcela: '{0}'", item.NOMBRE);
            return PartialView("_GridViewPartialParcelas", model.ToList());
        }

        [HttpPost, ValidateInput(false)]
        public ActionResult GridViewPartialParcelasDelete([ModelBinder(typeof(DevExpressEditorsBinder))] PARCELA item)
        {
            var model = db2.PARCELA;
            var modelCoordenadas = db2.COORDENADAS_PARCELA;
            if (item.CODIGO != null)
            {
                try
                {
                    var itemDelete = model.FirstOrDefault(it => it.CODIGO == item.CODIGO);
                    var itemDelete2 = modelCoordenadas.FirstOrDefault(it => it.PARCELA == item.CODIGO);
                    if (itemDelete != null && itemDelete2 != null)
                    {
                        AzureDB azure = new AzureDB();
                        azure.Conectar();
                        azure.eliminarParcela(item.CODIGO);
                        //modelCoordenadas.Remove(itemDelete2);
                        //model.Remove(itemDelete);
                        db2.SaveChanges();
                        model = db2.PARCELA;
                        List<PARCELA> p = model.ToList();
                        for (int i = 0; i < model.Count(); i++)
                        {
                            p[i].FINCA = azure.obtenerNombreFinca(p[i].FINCA);
                            p[i].VARIEDAD = azure.obtenerNombreVariedad(p[i].VARIEDAD);
                            p[i].CONJUNTA = p[i].CONJUNTA == "1" ? "SI" : "NO";

                        }
                        azure.Cerrar();
                        ViewData[EditResultKey] = string.Format("Eliminada la parcela: '{0}'", itemDelete.NOMBRE);
                    }
                    else ViewData[EditResultKey] = string.Format("No es posible eliminar la parcela: '{0}'", itemDelete.NOMBRE);
                }
                catch (Exception e)
                {
                    ViewData["EditError"] = e.Message;
                }
            }
            return PartialView("_GridViewPartialParcelas", model.ToList());
        }

        PlotsOnMapsDBEntities4 db3 = new PlotsOnMapsDBEntities4();

        [ValidateInput(false)]
        public ActionResult GridViewPartialVariedades()
        {
            var model = db3.VARIEDAD;
            return PartialView("_GridViewPartialVariedades", model.ToList());
        }

        [HttpPost, ValidateInput(false)]
        public ActionResult GridViewPartialVariedadesUpdate([ModelBinder(typeof(DevExpressEditorsBinder))] VARIEDAD item)
        {
            var model = db3.VARIEDAD;
            if (ModelState.IsValid)
            {
                try
                {
                    var modelItem = model.FirstOrDefault(it => it.NOMBRE == item.NOMBRE);
                    if (modelItem == null)
                    {
                        item.NOMBRE = item.NOMBRE.ToUpper();
                        ActualizarVariedad(item);
                        model = db3.VARIEDAD;
                        UpdateModel(model);
                        db1.SaveChanges();
                    }
                }
                catch (Exception e)
                {
                    ViewData[EditResultKey] = string.Format("Actualizada la variedad: '{0}'", item.NOMBRE);
                    ViewData["EditError"] = e.Message;
                }
            }
            else ViewData[EditResultKey] = string.Format("No es posible actualizar la variedad: '{0}'", item.NOMBRE);
            return PartialView("_GridViewPartialVariedades", model.ToList());
        }
        [HttpPost, ValidateInput(false)]
        public ActionResult GridViewPartialVariedadesDelete([ModelBinder(typeof(DevExpressEditorsBinder))] VARIEDAD item)
        {
            var model = db3.VARIEDAD;
            var modelParcela = db3.PARCELA;
            if (item.CODIGO != null)
            {
                try
                {
                    var itemDelete = model.FirstOrDefault(it => it.CODIGO == item.CODIGO);
                    var itemDelete2 = modelParcela.FirstOrDefault(it => it.VARIEDAD == item.CODIGO);
                    if (itemDelete != null && itemDelete2 == null)
                    {
                        model.Remove(itemDelete);
                        db3.SaveChanges();
                        ViewData[EditResultKey] = string.Format("Eliminada la variedad: '{0}'", itemDelete.NOMBRE);
                    }
                    else ViewData[EditResultKey] = string.Format("No es posible eliminar la variedad: '{0}'", itemDelete.NOMBRE);
                }
                catch (Exception e)
                {
                    ViewData["EditError"] = e.Message;
                }
            }
            return PartialView("_GridViewPartialVariedades", model.ToList());
        }

        PlotsOnMapsDBEntities4 db4 = new PlotsOnMapsDBEntities4();

        [ValidateInput(false)]
        public ActionResult GridViewPartialArticulos()
        {
            var model = db4.ARTICULO;
            return PartialView("_GridViewPartialArticulos", model.ToList());
        }
        
        [HttpPost, ValidateInput(false)]
        public ActionResult GridViewPartialArticulosUpdate([ModelBinder(typeof(DevExpressEditorsBinder))] ARTICULO item)
        {
            var model = db4.ARTICULO;
            if (ModelState.IsValid)
            {
                try
                {
                    var modelItem = model.FirstOrDefault(it => it.NOMBRE == item.NOMBRE);
                    if (modelItem == null)
                    {
                        item.NOMBRE = item.NOMBRE.ToUpper();
                        ActualizarArticulo(item);
                        model = db4.ARTICULO;
                        UpdateModel(model);
                        db4.SaveChanges();
                        ViewData[EditResultKey] = string.Format("Actualizado el artículo: '{0}'", item.NOMBRE);
                    }
                    else ViewData[EditResultKey] = string.Format("No es posible actualizar el artículo: '{0}'", item.NOMBRE);
                }
                catch (Exception e)
                {
                    ViewData["EditError"] = e.Message;
                }
            }
            return PartialView("_GridViewPartialArticulos", model.ToList());
        }
        [HttpPost, ValidateInput(false)]
        public ActionResult GridViewPartialArticulosDelete([ModelBinder(typeof(DevExpressEditorsBinder))] ARTICULO item)
        {
            var model = db4.ARTICULO;
            var modelOperacion = db4.OPERACION;
            var modelConjuntoArticulos = db4.CONJUNTO_ARTICULOS;
            if (item.CODIGO != null)
            {
                try
                {
                    var itemDelete = model.FirstOrDefault(it => it.CODIGO == item.CODIGO);
                    var itemDelete2 = modelOperacion.FirstOrDefault(it => it.ARTICULOS == item.CODIGO);
                    var itemDelete3 = modelConjuntoArticulos.FirstOrDefault(it => it.ARTICULO == item.CODIGO);
                    if (itemDelete != null && itemDelete2 == null && itemDelete3 == null)
                    {
                        model.Remove(itemDelete);
                        db4.SaveChanges();
                        ViewData[EditResultKey] = string.Format("Eliminado el artículo: '{0}'", itemDelete.NOMBRE);
                    }
                    else ViewData[EditResultKey] = string.Format("No es posible eliminar el artículo: '{0}'", itemDelete.NOMBRE);
                }
                catch (Exception e)
                {
                    ViewData["EditError"] = e.Message;
                }
            }
            return PartialView("_GridViewPartialArticulos", model.ToList());
        }

        //PlotsOnMapsDBEntities4 db4 = new PlotsOnMapsDBEntities4();

        [ValidateInput(false)]
        public ActionResult GridViewPartialConjuntoArticulos()
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            var model = db4.CONJUNTO_ARTICULOS;
            List<CONJUNTO_ARTICULOS> p = model.ToList();
            for (int i = 0; i < model.Count(); i++)
            {
                p[i].ARTICULO = azure.obtenerNombreArticulo(p[i].ARTICULO);

            }
            azure.Cerrar();
            return PartialView("_GridViewPartialConjuntoArticulos", p.ToList());
        }
        
        [HttpPost, ValidateInput(false)]
        public ActionResult GridViewPartialConjuntoArticulosDelete([ModelBinder(typeof(DevExpressEditorsBinder))] CONJUNTO_ARTICULOS item)
        {
            var modelOperacion = db4.OPERACION;
            var modelConjuntoArticulos = db4.CONJUNTO_ARTICULOS;
            if (item.CODIGO != null)
            {
                try
                {
                    var itemDelete2 = modelOperacion.FirstOrDefault(it => it.ARTICULOS.Equals(item.ARTICULO));
                    var itemDelete3 = modelConjuntoArticulos.FirstOrDefault(it => it.CODIGO.Equals(item.CODIGO));
                    if (itemDelete2 == null && itemDelete3 == null)
                    {
                        modelConjuntoArticulos.Remove(itemDelete3);
                        db4.SaveChanges();
                        
                        ViewData[EditResultKey] = string.Format("Eliminado el artículo: '{0}'", itemDelete3.ARTICULO);
                    }
                    else ViewData[EditResultKey] = string.Format("No es posible eliminar el conjunto: '{0}'", itemDelete3.ARTICULO);
                }
                catch (Exception e)
                {
                    ViewData["EditError"] = e.Message;
                }
            }

            AzureDB azure = new AzureDB();
            azure.Conectar();
            var modelc = db4.CONJUNTO_ARTICULOS;
            List<CONJUNTO_ARTICULOS> p = modelc.ToList();
            for (int i = 0; i < modelc.Count(); i++)
            {
                p[i].ARTICULO = azure.obtenerNombreArticulo(p[i].ARTICULO);

            }
            azure.Cerrar();
            return PartialView("_GridViewPartialConjuntoArticulos", p.ToList());
        }

        PlotsOnMapsDBEntities4 db5 = new PlotsOnMapsDBEntities4();

        [ValidateInput(false)]
        public ActionResult GridViewPartialTipoOperaciones()
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            var model = db5.TIPO_OPERACION;
            List<TIPO_OPERACION> p = model.ToList();
            for (int i = 0; i < model.Count(); i++)
            {
                p[i].TIENE_ARTICULOS = p[i].TIENE_ARTICULOS == "1" ? "SI" : "NO";
                p[i].COLOR = azure.obtenerColor(p[i].COLOR);
            }
            azure.Cerrar();
            return PartialView("_GridViewPartialTipoOperaciones", model.ToList());
        }
        
        [HttpPost, ValidateInput(false)]
        public ActionResult GridViewPartialTipoOperacionesUpdate([ModelBinder(typeof(DevExpressEditorsBinder))] TIPO_OPERACION item)
        {
            var model = db5.TIPO_OPERACION;
            if (ModelState.IsValid)
            {
                try
                {
                    var modelItem = model.FirstOrDefault(it => it.NOMBRE == item.NOMBRE);
                    if (modelItem != null)
                    {
                        item.NOMBRE = item.NOMBRE.ToUpper();
                        ActualizarTipoOperacion(item);
                        model = db4.TIPO_OPERACION;
                        UpdateModel(model);
                        db4.SaveChanges();
                        ViewData[EditResultKey] = string.Format("Actualizado el tipo de operación: '{0}'", item.NOMBRE);
                    }
                    else ViewData[EditResultKey] = string.Format("No es posible actualizar el tipo de operación: '{0}'", item.NOMBRE);
                }
                catch (Exception e)
                {
                    ViewData["EditError"] = e.Message;
                }
            }

            List<TIPO_OPERACION> p = model.ToList();
            AzureDB azure = new AzureDB();
            azure.Conectar();
            for (int i = 0; i < model.Count(); i++)
            {
                p[i].TIENE_ARTICULOS = p[i].TIENE_ARTICULOS == "1" ? "SI" : "NO";
                p[i].COLOR = azure.obtenerColor(p[i].COLOR);
            }
            azure.Cerrar();
            return PartialView("_GridViewPartialTipoOperaciones", p.ToList());
        }
        [HttpPost, ValidateInput(false)]
        public ActionResult GridViewPartialTipoOperacionesDelete([ModelBinder(typeof(DevExpressEditorsBinder))] TIPO_OPERACION item)
        {
            var model = db5.TIPO_OPERACION;
            var modelOperacion = db4.OPERACION;
            if (item.CODIGO != null)
            {
                try
                {
                    var itemDelete = model.FirstOrDefault(it => it.CODIGO == item.CODIGO);
                    var itemDelete2 = modelOperacion.FirstOrDefault(it => it.ARTICULOS == item.CODIGO);
                    if (itemDelete != null && itemDelete2 == null)
                    {
                        model.Remove(itemDelete);
                        db5.SaveChanges();
                        ViewData[EditResultKey] = string.Format("Eliminado el tipo de operación: '{0}'", itemDelete.NOMBRE);
                    }
                    else ViewData[EditResultKey] = string.Format("No es posible eliminar el tipo de operación: '{0}'", itemDelete.NOMBRE);
                }
                catch (Exception e)
                {
                    ViewData["EditError"] = e.Message;
                }
            }
            return PartialView("_GridViewPartialTipoOperaciones", model.ToList());
        }

        PlotsOnMapsDBEntities4 db6 = new PlotsOnMapsDBEntities4();

        [ValidateInput(false)]
        public ActionResult GridViewPartialFiltrado(List<OPERACION> filter)
        {
            AzureDB azure = new AzureDB();
            var query = Session["query"];
            azure.Conectar();
            var model = db6.OPERACION;
            List<OPERACION> p = model.ToList();
            for (int i = 0; i < model.Count(); i++)
            {
                p[i].FINCA = azure.obtenerNombreFinca(p[i].FINCA);
                String parcN = azure.obtenerNombreParcela(p[i].PARCELA);
                if (!parcN.Equals("")) p[i].PARCELA = parcN;
                p[i].COLOR = azure.obtenerColor(p[i].COLOR);
                p[i].TIPO = azure.obtenerNombreTipoOperacion(p[i].TIPO);
                if(azure.hayArticulosEnTOP(p[i].TIPO))
                {
                    String comp = azure.obtenerNombreArticulo(p[i].ARTICULOS);
                    if(!comp.Equals(""))
                    {
                        p[i].ARTICULOS = azure.obtenerNombreArticulo(p[i].ARTICULOS);
                    }
                } else p[i].ARTICULOS = " - ";

                p[i].USUARIO = azure.obtenerNombreUsuario(p[i].USUARIO);

            }
            azure.Cerrar();
            ViewData[EditResultKey] = string.Format("Recargue el mapa para ver los resultados del filtrado");
            return PartialView("_GridViewPartialFiltrado", p.ToList());
        }


        [HttpPost, ValidateInput(false)]
        public ActionResult GridViewPartialFiltradoAddNew([ModelBinder(typeof(DevExpressEditorsBinder))] OPERACION item)
        {
            var model = db6.OPERACION;
            if (ModelState.IsValid)
            {
                try
                {
                    model.Add(item);
                    db6.SaveChanges();
                }
                catch (Exception e)
                {
                    ViewData["EditError"] = e.Message;
                }
            }
            else
                ViewData["EditError"] = "Please, correct all errors.";
            return PartialView("_GridViewPartialFiltrado", model.ToList());
        }
        [HttpPost, ValidateInput(false)]
        public ActionResult GridViewPartialFiltradoUpdate([ModelBinder(typeof(DevExpressEditorsBinder))] OPERACION item)
        {

            var model = db6.OPERACION;
            if (ModelState.IsValid)
            {
                try
                {
                    ActualizarOperacionFiltrado(item);
                    /*model = db6.OPERACION;
                    UpdateModel(model);
                    db6.SaveChanges();*/
                }
                catch (Exception e)
                {
                    ViewData["EditError"] = e.Message;
                }
            }
            else
                ViewData["EditError"] = "Please, correct all errors.";

            AzureDB azure = new AzureDB();
            azure.Conectar();
            model = db6.OPERACION;
            List<OPERACION> p = model.ToList();
            for (int i = 0; i < model.Count(); i++)
            {
                p[i].FINCA = azure.obtenerNombreFinca(p[i].FINCA);
                p[i].PARCELA = azure.obtenerNombreParcela(p[i].PARCELA);
                p[i].COLOR = azure.obtenerColor(p[i].COLOR);
                p[i].TIPO = azure.obtenerNombreTipoOperacion(p[i].TIPO);
                p[i].ARTICULOS = azure.obtenerNombreArticulo(p[i].ARTICULOS);
                p[i].USUARIO = azure.obtenerNombreUsuario(p[i].USUARIO);

            }
            azure.Cerrar();
            return PartialView("_GridViewPartialFiltrado", p.ToList());
        }
        [HttpPost, ValidateInput(false)]
        public ActionResult GridViewPartialFiltradoDelete([ModelBinder(typeof(DevExpressEditorsBinder))] OPERACION item)
        {
            var model = db6.OPERACION;
            var modelCoordenadas = db6.COORDENADAS_OPERACION;
            AzureDB azure = new AzureDB();
            azure.Conectar();
            if (item.CODIGO != null)
            {
                try
                {
                    var itemDelete = model.FirstOrDefault(it => it.CODIGO == item.CODIGO);
                    var itemDelete2 = modelCoordenadas.FirstOrDefault(it => it.OPERACION == item.CODIGO);
                    if (itemDelete != null && itemDelete2 != null)
                    {
                        azure.eliminarFiltradoOperacion(item.CODIGO);
                        //model.Remove(itemDelete);
                        db6.SaveChanges();
                        ViewData[EditResultKey] = string.Format("Eliminado la operación: '{0}'", itemDelete.NOMBRE);
                    }
                    else ViewData[EditResultKey] = string.Format("No es posible eliminar la operación: '{0}'", itemDelete.NOMBRE);
                }
                catch (Exception e)
                {
                    ViewData["EditError"] = e.Message;
                }
            }

            List<OPERACION> p = model.ToList();
            for (int i = 0; i < model.Count(); i++)
            {
                p[i].FINCA = azure.obtenerNombreFinca(p[i].FINCA);
                String parcN = azure.obtenerNombreParcela(p[i].PARCELA);
                if (!parcN.Equals("")) p[i].PARCELA = parcN;
                p[i].COLOR = azure.obtenerColor(p[i].COLOR);
                p[i].TIPO = azure.obtenerNombreTipoOperacion(p[i].TIPO);
                if (azure.hayArticulosEnTOP(p[i].TIPO))
                {
                    String comp = azure.obtenerNombreArticulo(p[i].ARTICULOS);
                    if (!comp.Equals(""))
                    {
                        p[i].ARTICULOS = azure.obtenerNombreArticulo(p[i].ARTICULOS);
                    }
                }
                else p[i].ARTICULOS = " - ";

                p[i].USUARIO = azure.obtenerNombreUsuario(p[i].USUARIO);

            }
            azure.Cerrar();
            return PartialView("_GridViewPartialFiltrado", p.ToList());
        }

        public bool existeArticulo(TIPO_OPERACION objeto)
        {
            PlotsOnMapsDBEntities4 entities = new PlotsOnMapsDBEntities4();
            var objetos = entities.TIPO_OPERACION;
            bool existe = false;
            foreach (var item in objetos)
            {
                if (!item.NOMBRE.Equals(objeto.NOMBRE))
                {
                    if (item.NOMBRE.Equals(objeto.NOMBRE))
                    {
                        existe = true;
                    }
                }
            }

            return existe;
        }

        public bool existeVariedad(VARIEDAD objeto)
        {
            PlotsOnMapsDBEntities4 entities = new PlotsOnMapsDBEntities4();
            var objetos = entities.VARIEDAD;
            bool existe = false;
            foreach (var item in objetos)
            {
                if (!item.NOMBRE.Equals(objeto.NOMBRE))
                {
                    if (item.NOMBRE.Equals(objeto.NOMBRE))
                    {
                        existe = true;
                    }
                }
            }

            return existe;
        }

        public bool existeTipoOperacion(TIPO_OPERACION objeto)
        {
            PlotsOnMapsDBEntities4 entities = new PlotsOnMapsDBEntities4();
            var objetos = entities.TIPO_OPERACION;
            bool existe = false;
            foreach (var item in objetos)
            {
                if (!item.NOMBRE.Equals(objeto.NOMBRE))
                {
                    if (item.NOMBRE.Equals(objeto.NOMBRE))
                    {
                        existe = true;
                    }
                }
            }

            return existe;
        }

        [HttpPost]
        public void ObtenerFiltradoDVExpress(Filtrar filter)
        {
            string filtrado = (string) Session["query"];
            string[] v = filtrado.Split('[', ']', ' ', ')', '\'', '>', '<', '=');
            List<String> cadena = new List<String>();

            for (int i = 0; i < v.Count(); i++)
            {
                cadena.Add(v[i]);
            }

            cadena.Remove("StartsWith(");
            cadena.Remove(",");
            cadena.Remove(" ");

            for (int i = 0; i < cadena.Count; i++)
            {
                if(cadena[i] == "")
                {
                    cadena.RemoveAt(i);
                    i--;
                }
            }

            for (int i = 0; i < cadena.Count; i++)
            {
                switch(cadena[i])
                {
                    case "CODIGO":
                        filter.codigo = cadena[i + 1];
                        break;
                    case "NOMBRE":
                        filter.nombre = cadena[i + 1];
                        break;
                    case "TIPO":
                        filter.codigo = cadena[i + 1];
                        break;
                    case "ARTICULOS":
                        filter.codigo = cadena[i + 1];
                        break;
                    case "FECHA":
                        filter.codigo = cadena[i + 1];
                        break;
                    case "FECHA2":
                        filter.codigo = cadena[i + 1];
                        break;
                    case "PARCELA":
                        filter.codigo = cadena[i + 1];
                        break;
                    case "FINCA":
                        filter.codigo = cadena[i + 1];
                        break;
                    case "USUARIO":
                        filter.usuario = cadena[i + 1];
                        break;
                }
                i++;
            }

            ObtenerFiltrado(filter);
        }

        public CargarFiltrado cargarOperacionesEnMapaDEV(List<OPERACION> f)
        {
            AzureDB azure = new AzureDB();
            azure.Conectar();
            List<int> nDibujos = new List<int>();
            List<Parcela> parcelas = azure.filtrarParcelas();
            List<Dibujo> operaciones = azure.filtrarDibujosDEV(f);
            List<List<CoordenadasOperaciones>> cp = ObtenerOperaciones(operaciones);

            for (int i = 0; i < cp.Count; i++)
            {
                nDibujos.Add(azure.obtenerNumDibujos(operaciones[i]));

            }

            for (int i = 0; i < operaciones.Count(); i++)
            {
                operaciones[i].FINCA = azure.obtenerNombreFinca(operaciones[i].FINCA);
                operaciones[i].PARCELA = azure.obtenerNombreParcela(operaciones[i].PARCELA);
                operaciones[i].TIPO = azure.obtenerNombreTipoOperacion(operaciones[i].TIPO);
            }

            azure.Cerrar();
            return new CargarFiltrado(cp, nDibujos, operaciones, parcelas);
        }

        static public String[] getColores()
        {
            var colores = new string[] { "AZUL", "VERDE", "ROJO", "AMARILLO", "MAGENTA", "CYAN", "BLANCO", "NEGRO" };
            return colores;
        }

        static public String[] getTieneArticulos()
        {
            var tiene = new string[] { "SI", "NO" };
            return tiene;
        }

        static public String[] getRolUsuario()
        {
            var rol = new string[] { "ADMIN", "TECNICO" };
            return rol;
        }

        static public String[] getUsuarioHabilitado()
        {
            var habilitado = new string[] { "SI", "NO" };
            return habilitado;
        }

        static public String[] getParcelaConjunta()
        {
            var conjunta = new string[] { "SI", "NO" };
            return conjunta;
        }

        static public String[] getVariedades()
        {
            PlotsOnMapsDBEntities4 db = new PlotsOnMapsDBEntities4();
            String[] variedades = db.VARIEDAD.Select(x => x.NOMBRE).ToArray();
            return variedades;
        }

        static public String[] getTipoOperaciones()
        {
            PlotsOnMapsDBEntities4 db = new PlotsOnMapsDBEntities4();
            String[] tipo_operaciones = db.TIPO_OPERACION.Select(x => x.NOMBRE).ToArray();
            return tipo_operaciones;
        }

        static public String[] getArticulos()
        {
            PlotsOnMapsDBEntities4 db = new PlotsOnMapsDBEntities4();
            String[] articulos = db.ARTICULO.Select(x => x.NOMBRE).ToArray();
            //String[] articulos_c = db.CONJUNTO_ARTICULOS.Select(x => x.ARTICULO).Distinct().ToArray();
            //String[] a = articulos.Concat(articulos_c).ToArray();
            return articulos;
        }

        static public String[] getParcelas()
        {
            PlotsOnMapsDBEntities4 db = new PlotsOnMapsDBEntities4();
            String[] parcelas = db.PARCELA.Select(x => x.NOMBRE).Distinct().ToArray();
           // String[] parcelas_c = db.CONJUNTO_PARCELAS.Select(x => x.PARCELA).Distinct().ToArray();
           // String[] p = parcelas.Concat(parcelas_c).ToArray();
            return parcelas;
        }

        static public String[] getFincas()
        {
            PlotsOnMapsDBEntities4 db = new PlotsOnMapsDBEntities4();
            String[] fincas = db.FINCA.Select(x => x.NOMBRE).ToArray();
            return fincas;
        }

        static public String[] getUsuarios()
        {
            PlotsOnMapsDBEntities4 db = new PlotsOnMapsDBEntities4();
            String[] usuarios = db.USUARIO.Where(x => x.TIPO.Equals("TECNICO")).Select(x => x.USERNAME).ToArray();
            return usuarios;
        }
    }
}