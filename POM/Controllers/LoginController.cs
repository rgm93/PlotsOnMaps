using POM.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace POM.Controllers
{
    public class LoginController : Controller
    {
        public LoginController() {}

        // GET: Login
        public ActionResult Login()
        {
            return View();
        }

        public ActionResult Home()
        {
            Usuario u = new Usuario();
            if (!Request.Form["username"].ToString().Equals("") && !Request.Form["password"].ToString().Equals(""))
            {

                // Comproba en Azure
                AzureDB azure = new AzureDB();
                azure.Conectar();
                u = azure.comprobarUsuario(Request.Form["username"].ToString(), Request.Form["password"].ToString());
                if (u != null)
                {
                    return View(u);
                }
            }

            return Login();
        }
    }
}