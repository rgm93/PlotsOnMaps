using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Web;

namespace POM.Models
{
    public class AzureDB
    {
        SqlConnection connection;
        SqlConnectionStringBuilder builder;

        public AzureDB()
        {
            builder = new SqlConnectionStringBuilder();
            builder.DataSource = "plotsonmaps.database.windows.net";
            builder.UserID = "rgm93@plotsonmaps";
            builder.Password = "Ruben07091993plotsonmaps";
            builder.InitialCatalog = "PlotsOnMapsDB";
            connection = new SqlConnection(builder.ConnectionString);
        }

        public void Conectar()
        {
            connection.Open();
        }

        public void Cerrar()
        {
            connection.Close();
        }

        public String getConexion() { return builder.ConnectionString; }

        public Usuario comprobarUsuario(String username, String password)
        {
            Usuario u = null;
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [USUARIO] WHERE USERNAME='" + username + "' AND PASS='" + password + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        if(reader.GetString(3).Equals("ADMIN"))
                        {
                            u = new Usuario(reader.GetString(0), reader.GetString(1), reader.GetString(2), reader.GetString(3), reader.GetString(4));
                        }
                    }
                }
            }

            return u;
        }

        public bool crearUsuario(String cod, String username, String password, String tipo)
        {
            bool creado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("INSERT INTO [USUARIO] VALUES ('" + cod + "','" + username + "','" + password + "','" + tipo + "','" + "SI" + "')");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        creado = true;
                    }
                }
            }

            return creado;
        }

        public bool modificarUsuario(String username_selected, String username, String pass)
        {
            bool creado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("UPDATE [USUARIO] SET USERNAME = '" + username + "', PASS = '" + pass + "' WHERE USERNAME = '" + username_selected + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                SqlDataAdapter adapter = new SqlDataAdapter();
                adapter.InsertCommand = command;
                adapter.InsertCommand.ExecuteNonQuery();
                command.Dispose();
            }

            return creado;
        }

        public bool eliminarUsuario(String codigo)
        {
            bool borrado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("DELETE [USUARIO] WHERE CODIGO = '" + codigo + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                SqlDataAdapter adapter = new SqlDataAdapter();
                adapter.InsertCommand = command;
                adapter.InsertCommand.ExecuteNonQuery();
                command.Dispose();
                borrado = true;
            }

            return borrado;
        }

        public bool eliminarParcela(String codigo)
        {
            bool borrado = false;
            StringBuilder sb = new StringBuilder();

            sb.Append("DELETE [COORDENADAS_PARCELA] WHERE PARCELA = '" + codigo + "';");
            sb.Append("DELETE [PARCELA] WHERE CODIGO = '" + codigo + "';");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                SqlDataAdapter adapter = new SqlDataAdapter();
                adapter.InsertCommand = command;
                adapter.InsertCommand.ExecuteNonQuery();
                command.Dispose();
                borrado = true;
            }

            return borrado;
        }

        public bool contieneOperacionCampo(String codigo, String columna)
        {
            bool contiene = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [OPERACION] WHERE " + columna + " = '" + codigo + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        contiene = true;
                    }
                }
            }

            return contiene;
        }

        public bool contieneOperacionVariedad(String codigo)
        {
            bool contiene = false;
            List<String> parcelas = new List<String>();
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [OPERACION]");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        parcelas.Add(reader.GetString(8));
                    }

                    reader.Close();

                    for (int i = 0; i < parcelas.Count; i++)
                    {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.Append("SELECT * FROM [PARCELA] WHERE VARIEDAD = '" + codigo + "' AND CODIGO = '" + parcelas[i] + "'");
                        String sql2 = sb2.ToString();
                        using (SqlCommand command2 = new SqlCommand(sql2, connection))
                        {
                            using (SqlDataReader reader2 = command2.ExecuteReader())
                            {
                                while (reader2.Read())
                                {
                                    contiene = true;
                                }

                                reader2.Close();
                            }
                        }
                    }
                    
                }
            }

            return contiene;
        }

        public List<Usuario> filtrarUsuarios()
        {
            List<Usuario> usuarios = new List<Usuario>();
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [USUARIO] WHERE TIPO != 'ADMIN'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        usuarios.Add(new Usuario(reader.GetString(0), reader.GetString(1), reader.GetString(2), reader.GetString(3), reader.GetString(4)));
                        
                    }
                }
            }

            return usuarios;
        }

        public bool obtenerCODUsuario(String codigo)
        {
            bool encontrado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [USUARIO] WHERE CODIGO = '" + codigo + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        encontrado = true;
                    }
                }
            }
        
            return encontrado;
        }

        public bool comprobarUsuario(String usuario)
        {
            bool encontrado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [USUARIO] WHERE USERNAME = '" + usuario + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        encontrado = true;
                    }
                }
            }

            return encontrado;
        }

        public bool crearVariedad(String cod, String nombre)
        {
            bool creado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("INSERT INTO [VARIEDAD] VALUES ('" + cod + "','" + nombre + "')");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        creado = true;
                    }
                }
            }

            return creado;
        }

        public bool modificarVariedad(String variedad_selected, String nombre)
        {
            bool creado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("UPDATE [VARIEDAD] SET NOMBRE = '" + nombre + "' WHERE NOMBRE = '" + variedad_selected + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                SqlDataAdapter adapter = new SqlDataAdapter();
                adapter.InsertCommand = command;
                adapter.InsertCommand.ExecuteNonQuery();
                command.Dispose();
            }

            return creado;
        }

        public bool eliminarVariedad(String nombre)
        {
            bool borrado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("DELETE [VARIEDAD] WHERE CODIGO = '" + nombre + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                SqlDataAdapter adapter = new SqlDataAdapter();
                adapter.InsertCommand = command;
                adapter.InsertCommand.ExecuteNonQuery();
                command.Dispose();
                borrado = true;
            }

            return borrado;
        }

        public List<Variedad> filtrarVariedades()
        {
            List<Variedad> variedades = new List<Variedad>();
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [VARIEDAD]");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        variedades.Add(new Variedad(reader.GetString(0), reader.GetString(1)));

                    }
                }
            }

            return variedades;
        }

        public bool obtenerCODVariedad(String codigo)
        {
            bool encontrado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [VARIEDAD] WHERE CODIGO = '" + codigo + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        encontrado = true;
                    }
                }
            }

            return encontrado;
        }

        public bool comprobarVariedad(String nombre)
        {
            bool encontrado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [VARIEDAD] WHERE NOMBRE = '" + nombre + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        encontrado = true;
                    }
                }
            }

            return encontrado;
        }

        public bool crearArticulo(String cod, String nombre)
        {
            bool creado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("INSERT INTO [ARTICULO] VALUES ('" + cod + "','" + nombre + "')");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        creado = true;
                    }
                }
            }

            return creado;
        }

        public bool modificarArticulo(String articulo_selected, String nombre)
        {
            bool creado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("UPDATE [ARTICULO] SET NOMBRE = '" + nombre + "' WHERE NOMBRE = '" + articulo_selected + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                SqlDataAdapter adapter = new SqlDataAdapter();
                adapter.InsertCommand = command;
                adapter.InsertCommand.ExecuteNonQuery();
                command.Dispose();
            }

            return creado;
        }

        public bool eliminarArticulo(String nombre)
        {
            bool borrado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("DELETE [ARTICULO] WHERE CODIGO = '" + nombre + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                SqlDataAdapter adapter = new SqlDataAdapter();
                adapter.InsertCommand = command;
                adapter.InsertCommand.ExecuteNonQuery();
                command.Dispose();
                borrado = true;
            }

            return borrado;
        }

        public List<Articulo> filtrarArticulos()
        {
            List<Articulo> articulos = new List<Articulo>();
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [ARTICULO]");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        articulos.Add(new Articulo(reader.GetString(0), reader.GetString(1)));

                    }
                }
            }

            return articulos;
        }

        public bool obtenerCODArticulo(String codigo)
        {
            bool encontrado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [ARTICULO] WHERE CODIGO = '" + codigo + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        encontrado = true;
                    }
                }
            }

            return encontrado;
        }

        public bool comprobarArticulo(String nombre)
        {
            bool encontrado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [ARTICULO] WHERE NOMBRE = '" + nombre + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        encontrado = true;
                    }
                }
            }

            return encontrado;
        }

        public bool crearOperacion(String cod, String nombre, String tiene)
        {
            bool creado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("INSERT INTO [TIPO_OPERACION] VALUES ('" + cod + "','" + nombre + "','" + tiene + "')");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        creado = true;
                    }
                }
            }

            return creado;
        }

        public bool modificarOperacion(String articulo_selected, String nombre, String tiene)
        {
            bool creado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("UPDATE [TIPO_OPERACION] SET NOMBRE = '" + nombre + "', TIENE_ARTICULOS = '" + tiene + "' WHERE NOMBRE = '" + articulo_selected + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                SqlDataAdapter adapter = new SqlDataAdapter();
                adapter.InsertCommand = command;
                adapter.InsertCommand.ExecuteNonQuery();
                command.Dispose();
            }

            return creado;
        }

        public bool eliminarOperacion(String nombre)
        {
            bool borrado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("DELETE [TIPO_OPERACION] WHERE CODIGO = '" + nombre + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                SqlDataAdapter adapter = new SqlDataAdapter();
                adapter.InsertCommand = command;
                adapter.InsertCommand.ExecuteNonQuery();
                command.Dispose();
                borrado = true;
            }

            return borrado;
        }

        public bool eliminarFiltradoOperacion(String nombre)
        {
            bool borrado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("DELETE [COORDENADAS_OPERACION] WHERE OPERACION = '" + nombre + "';");
            sb.Append("DELETE [OPERACION] WHERE CODIGO = '" + nombre + "';");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                SqlDataAdapter adapter = new SqlDataAdapter();
                adapter.InsertCommand = command;
                adapter.InsertCommand.ExecuteNonQuery();
                command.Dispose();
                borrado = true;
            }

            return borrado;
        }

        public List<Operacion> filtrarOperaciones()
        {
            List<Operacion> operaciones = new List<Operacion>();
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [TIPO_OPERACION]");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        operaciones.Add(new Operacion(reader.GetString(0), reader.GetString(1), reader.GetString(2)));

                    }
                }
            }

            return operaciones;
        }

        public bool obtenerCODOperacion(String codigo)
        {
            bool encontrado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [TIPO_OPERACION] WHERE CODIGO = '" + codigo + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        encontrado = true;
                    }
                }
            }

            return encontrado;
        }

        public bool comprobarOperacion(String nombre)
        {
            bool encontrado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [TIPO_OPERACION] WHERE NOMBRE = '" + nombre + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        encontrado = true;
                    }
                }
            }

            return encontrado;
        }

        public bool crearFinca(String cod, String nombre, String color, String usuario)
        {
            bool creado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("INSERT INTO [FINCA] VALUES ('" + cod + "','" + nombre + "','" + color + "','" + usuario + "')");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        creado = true;
                    }
                }
            }
            
            return creado;
        }

        public List<Finca> filtrarFincas()
        {
            List<Finca> fincas = new List<Finca>();
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [FINCA]");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        //String color = obtenerColor(reader.GetInt32(2)).ToString();
                        fincas.Add(new Finca(reader.GetString(0), reader.GetString(1), reader.GetString(2), reader.GetString(3)));
                    }
                }
            }

            return fincas;
        }

        public bool crearParcela(String cod, String nombre, String conjunta, String variedad, String finca, List<float[]> coordenadas)
        {
            bool creado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("INSERT INTO [PARCELA] VALUES ('" + cod + "','" + nombre + "','" + conjunta + "','" + variedad + "','" + finca + "')");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        creado = true;
                    }
                }
            }
            
            StringBuilder sb3 = new StringBuilder();
            Console.WriteLine(coordenadas);
            for (int i = 0; i < coordenadas.Count; i++)
            {
                float[] coord = coordenadas[i];
                string lat = coord[0].ToString();
                string log = coord[1].ToString();
                lat = lat.Replace(',', '.');
                log = log.Replace(',', '.');
                sb3.Append("INSERT INTO [COORDENADAS_PARCELA] (LAT, LONG, PARCELA) VALUES (" + lat + "," + log + ",'" + cod + "')");
            }
            
            String sql3 = sb3.ToString();

            using (SqlCommand command = new SqlCommand(sql3, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        creado = true;
                    }
                }
            }

            return creado;
        }

        public List<Parcela> filtrarParcelas()
        {
            List<Parcela> parcelas = new List<Parcela>();
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [PARCELA]");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        parcelas.Add(new Parcela(reader.GetString(0), reader.GetString(1), reader.GetString(2), reader.GetString(3), reader.GetString(4)));
                    }
                }
            }

            return parcelas;
        }

        public List<Parcela> filtrarParcelas(String codFinca)
        {
            List<Parcela> parcelas = new List<Parcela>();
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [PARCELA] WHERE FINCA = '" + codFinca + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        parcelas.Add(new Parcela(reader.GetString(0), reader.GetString(1), reader.GetString(2), reader.GetString(3), reader.GetString(4)));
                    }
                }
            }

            return parcelas;
        }

        public Parcela filtrarParcela(List<Parcela> parcelas, string codParcela)
        {
            int i = 0; bool encontrado = false;
            Parcela p = null;
            while (i < parcelas.Count && !encontrado)
            {
                if (parcelas[i].codigo == codParcela)
                {
                    p = parcelas[i];
                    encontrado = true;
                }
                else i++;
            }
            
            return p;
        }

        public List<Dibujo> filtrarDibujos()
        {
            List<Dibujo> dibujos = new List<Dibujo>();
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [OPERACION]");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        //String color = obtenerColor(reader.GetInt32(2));
                        dibujos.Add(new Dibujo(reader.GetString(0), reader.GetString(1), reader.GetString(2), reader.GetString(3), reader.GetString(4), reader.GetString(5), reader.GetString(6), reader.GetInt32(7), reader.GetString(8), reader.GetString(9), reader.GetString(10)));
                    }
                }
            }

            for (int j = 0; j < dibujos.Count; j++)
            {
                dibujos[j].FINCA = obtenerNombreFinca(dibujos[j].FINCA);
                dibujos[j].PARCELA = obtenerNombreParcela(dibujos[j].PARCELA);
                dibujos[j].ARTICULOS = obtenerNombreArticulo(dibujos[j].ARTICULOS);
                dibujos[j].TIPO = obtenerNombreTipoOperacion(dibujos[j].TIPO);
                dibujos[j].USUARIO = obtenerNombreUsuario(dibujos[j].USUARIO);
            }

            return dibujos;
        }

        public List<Dibujo> filtrarOperaciones(Filtrar f)
        {
            List<Dibujo> dibujos = new List<Dibujo>();

            StringBuilder sb1 = new StringBuilder();
            String sql1 = "";

            sb1.Append("SELECT * FROM [OPERACION] WHERE CODIGO LIKE '%" + f.codigo + "' AND NOMBRE LIKE '%" + f.nombre + "' AND TIPO LIKE '%" + f.tipoOperacion + "' AND FECHA LIKE '%" + f.fecha + "' AND ARTICULOS LIKE '%" + f.articulo + "' AND PARCELA LIKE '%" + f.parcela + "' AND FINCA LIKE '%" + f.finca + "' AND USUARIO LIKE '%" + f.usuario + "'");
            sql1 = sb1.ToString();
            using (SqlCommand command = new SqlCommand(sql1, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        //String color = obtenerColor(reader.GetInt32(2));
                        dibujos.Add(new Dibujo(reader.GetString(0), reader.GetString(1), reader.GetString(2), reader.GetString(3), reader.GetString(4), reader.GetString(5), reader.GetString(6), reader.GetInt32(7), reader.GetString(8), reader.GetString(9), reader.GetString(10)));
                    }
                }
            }
            

            return dibujos;
        }

        public List<Dibujo> filtrarDibujos(Filtrar f)
        {
            List<Dibujo> dibujos = new List<Dibujo>();
            String conjP = "", conjA = "";

            StringBuilder sb1 = new StringBuilder();
            String sql1 = "";
            List<string> recParc = new List<string>();
            if (conjP == "")
            {
                sb1.Append("SELECT * FROM [PARCELA] WHERE CODIGO LIKE '%" + f.parcela + "' AND FINCA LIKE '%" + f.finca + "' AND VARIEDAD LIKE '%" + f.variedad + "'");
                sql1 = sb1.ToString();

                using (SqlCommand command1 = new SqlCommand(sql1, connection))
                {
                    using (SqlDataReader reader1 = command1.ExecuteReader())
                    {
                        while (reader1.Read())
                        {
                            recParc.Add(reader1.GetString(0));
                        }
                        reader1.Close();
                    }
                }
            }
            

            StringBuilder sb2 = new StringBuilder();
            sb2.Append("SELECT * FROM [CONJUNTO_ARTICULOS] WHERE ARTICULO = '" + f.articulo + "'");
            String sql2 = sb2.ToString();
            using (SqlCommand command2 = new SqlCommand(sql2, connection))
            {
                using (SqlDataReader reader2 = command2.ExecuteReader())
                {
                    while (reader2.Read())
                    {
                        conjA = reader2.GetString(0);
                    }
                    reader2.Close();
                }
            }

            String nomArt = obtenerNombreArticulo(f.articulo);
            String nomTOp = obtenerNombreTipoOperacion(f.tipoOperacion);

            if (f.fecha == null) f.fecha = "";

            String nomUsr = obtenerNombreUsuario(f.usuario);

            StringBuilder sb4 = new StringBuilder();
            StringBuilder sb5 = new StringBuilder();
            for (int i = 0; i < recParc.Count; i++)
            {
                sb4 = new StringBuilder();
                sb5 = new StringBuilder();
                String conjPP = obtenerConjuntoParcelasDesdeParcela(recParc[i]);
                
                sb4.Append("SELECT * FROM [OPERACION] WHERE PARCELA LIKE '%" + recParc[i] + "' AND ARTICULOS LIKE '%" + f.articulo + "' AND TIPO LIKE '%" + f.tipoOperacion + "' AND USUARIO LIKE '%" + f.usuario + "' AND FECHA LIKE '%" + f.fecha + "';");
                sb5.Append("SELECT * FROM [OPERACION] WHERE PARCELA LIKE '%" + conjPP + "' AND ARTICULOS LIKE '%" + f.articulo + "' AND TIPO LIKE '%" + f.tipoOperacion + "' AND USUARIO LIKE '%" + f.usuario + "' AND FECHA LIKE '%" + f.fecha + "';");
                
                
                String sql4 = sb4.ToString();
                using (SqlCommand command4 = new SqlCommand(sql4, connection))
                {
                    using (SqlDataReader reader4 = command4.ExecuteReader())
                    {
                        while (reader4.Read())
                        {
                            //String color = obtenerColor(reader4.GetInt32(2));
                            dibujos.Add(new Dibujo(reader4.GetString(0), reader4.GetString(1), reader4.GetString(2), reader4.GetString(3), reader4.GetString(4), reader4.GetString(5), reader4.GetString(6), reader4.GetInt32(7), reader4.GetString(8), reader4.GetString(9), reader4.GetString(10)));
                        }

                        reader4.Close();

                        int j = 0;

                        for (; j < dibujos.Count; j++)
                        {
                            dibujos[j].FINCA = obtenerNombreFinca(dibujos[j].FINCA);
                            dibujos[j].PARCELA = obtenerNombreParcela(dibujos[j].PARCELA);
                            dibujos[j].ARTICULOS = obtenerNombreArticulo(dibujos[j].ARTICULOS);
                            dibujos[j].TIPO = obtenerNombreTipoOperacion(dibujos[j].TIPO);
                            dibujos[j].USUARIO = obtenerNombreUsuario(dibujos[j].USUARIO);
                        }

                        String sql5 = sb5.ToString();
                        using (SqlCommand command5 = new SqlCommand(sql5, connection))
                        {
                            using (SqlDataReader reader5 = command5.ExecuteReader())
                            {
                                while (reader5.Read())
                                {
                                    //String color = obtenerColor(reader5.GetInt32(2));
                                    dibujos.Add(new Dibujo(reader5.GetString(0), reader5.GetString(1), reader5.GetString(2), reader5.GetString(3), reader5.GetString(4), reader5.GetString(5), reader5.GetString(6), reader5.GetInt32(7), reader5.GetString(8), reader5.GetString(9), reader5.GetString(10)));
                                }

                                reader5.Close();
                            }
                        }

                        for (; j < dibujos.Count; j++)
                        {
                            dibujos[j].FINCA = obtenerNombreFinca(dibujos[j].FINCA);
                            dibujos[j].ARTICULOS = obtenerNombreArticulo(dibujos[j].ARTICULOS);
                            dibujos[j].TIPO = obtenerNombreTipoOperacion(dibujos[j].TIPO);
                            dibujos[j].USUARIO = obtenerNombreUsuario(dibujos[j].USUARIO);
                        }
                    }
                }
            }

            return dibujos;
        }

        public String obtenerCODFinca(String nombreFinca)
        {
            String nombre = "";
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [PARCELA] WHERE NOMBRE = '" + nombreFinca + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        nombre = reader.GetString(1);
                    }
                    reader.Close();
                }
            }

            return nombre;
        }

        public String obtenerConjuntoParcelasDesdeParcela(String codigo)
        {
            String conjP = "";
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [CONJUNTO_PARCELAS] WHERE PARCELA = '" + codigo + "'");
            //sb.Append("SELECT * FROM [OPERACION] WHERE ");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        conjP = reader.GetString(1);
                    }
                    reader.Close();
                }
            }
            
            return conjP;
        }

        public String obtenerCODParcelaDesdeFinca(String codigo, String nombre)
        {
            String parcela = "";
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [PARCELA] WHERE FINCA = '" + codigo + "' AND NOMBRE = '" + nombre + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        parcela = reader.GetString(0);
                    }
                    reader.Close();
                }
            }

            return parcela;
        }

        public bool obtenerCODParcela(String codigo)
        {
            bool encontrado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [PARCELA] WHERE CODIGO = '" + codigo + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        encontrado = true;
                    }
                    reader.Close();
                }
            }
            return encontrado;
        }

        public String obtenerCodigo(String nombre, String tabla)
        {
            String codigoAux = "";
            StringBuilder sb = new StringBuilder();
            String columna = "NOMBRE";
            if (tabla.Equals("USUARIO")) columna = "USERNAME";
            sb.Append("SELECT * FROM [" + tabla + "] WHERE " + columna + " = '" + nombre + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        codigoAux = reader.GetString(0);
                    }
                    reader.Close();
                }
            }
            return codigoAux;
        }

        public bool comprobarParcela(String codigo)
        {
            bool encontrado = false;
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [PARCELA] WHERE CODIGO = '" + codigo + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        encontrado = true;
                    }
                    reader.Close();
                }
            }

            return encontrado;
        }

        public List<CoordenadasParcela> obtenerCoordenadasParcelas(Parcela p)
        {
            List<CoordenadasParcela> cp = new List<CoordenadasParcela>();
            StringBuilder sb = new StringBuilder();

            sb.Append("SELECT * FROM [COORDENADAS_PARCELA] WHERE PARCELA = '" + p.codigo + "'");
            String sql = sb.ToString();

            String c = obtenerFinca(p.finca).color;

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        cp.Add(new CoordenadasParcela(reader.GetInt32(0), reader.GetDouble(1), reader.GetDouble(2), reader.GetString(3)));
                        //cp[cp.Count - 1].colorHex = obtenerColorHex(c);
                    }
                    reader.Close();
                }
            }
            
            return cp;
        }

        public Finca obtenerFinca(string codigo)
        {
            Finca f = new Finca();

            StringBuilder sb = new StringBuilder();

            sb.Append("SELECT * FROM [FINCA] WHERE CODIGO = '" + codigo + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        f = new Finca(reader.GetString(0), reader.GetString(1), reader.GetString(2), reader.GetString(3));
                    }
                    reader.Close();
                }
            }

            return f;
        }

        public int obtenerColorOperacion(string codigo)
        {
            int color = 0;

            StringBuilder sb = new StringBuilder();

            sb.Append("SELECT * FROM [OPERACION] WHERE CODIGO = '" + codigo + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        color = reader.GetInt32(2);
                    }
                    reader.Close();
                }
            }

            return color;
        }

        public List<CoordenadasOperaciones> obtenerCoordenadasOperaciones(Dibujo p)
        {
            List<CoordenadasOperaciones> cp = new List<CoordenadasOperaciones>();
            StringBuilder sb = new StringBuilder();

            sb.Append("SELECT * FROM [COORDENADAS_OPERACION] WHERE OPERACION = '" + p.CODIGO + "'");
            String sql = sb.ToString();

            int c = obtenerColorOperacion(p.CODIGO);
            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        cp.Add(new CoordenadasOperaciones(reader.GetInt32(0), reader.GetDouble(1), reader.GetDouble(2), reader.GetInt32(3), reader.GetString(4)));
                        cp[cp.Count - 1].colorHex = obtenerColorHex(c);
                    }
                    reader.Close();
                }
            }

            return cp;
        }

        public int obtenerNumDibujos(Dibujo p)
        {
            return p.TROZOS;
        }

        public String obtenerNombreFinca(String codigo)
        {
            String nombre = "";
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [FINCA] WHERE CODIGO = '" + codigo + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        nombre = reader.GetString(1);
                    }
                    reader.Close();
                }
            }
            return nombre;
        }

        public String obtenerNombreParcela(String codigo)
        {
            String nombre = "";
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [PARCELA] WHERE CODIGO = '" + codigo + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        nombre = reader.GetString(1);
                    }
                    reader.Close();
                }
            }

            if (nombre.Equals("")) nombre = obtenerNombreConjuntoParcela(codigo);
            return nombre;
        }

        public String obtenerNombreConjuntoParcela(String codigo)
        {
            String nombre = "";
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [CONJUNTO_PARCELAS] WHERE NOMBRE = '" + codigo + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        nombre = reader.GetString(1);
                    }
                    reader.Close();
                }
            }
            return nombre;
        }

        public String obtenerNombreVariedad(string codigo)
        {
            String nombre = "";
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [VARIEDAD] WHERE CODIGO = '" + codigo + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        nombre = reader.GetString(1);
                    }
                    reader.Close();
                }
            }
            return nombre;
        }

        public String obtenerNombreArticulo(String codigo)
        {
            String nombre = "";
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [ARTICULO] WHERE CODIGO = '" + codigo + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        nombre = reader.GetString(1);
                    }
                    reader.Close();
                }
            }
            return nombre;
        }

        public String obtenerNombreTipoOperacion(String codigo)
        {
            String nombre = "";
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [TIPO_OPERACION] WHERE CODIGO = '" + codigo + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        nombre = reader.GetString(1);
                    }
                    reader.Close();
                }
            }

            return nombre;
        }

        public String obtenerNombreUsuario(String codigo)
        {
            String nombre = "";
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [USUARIO] WHERE CODIGO = '" + codigo + "'");
            String sql = sb.ToString();

            using (SqlCommand command = new SqlCommand(sql, connection))
            {
                using (SqlDataReader reader = command.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        nombre = reader.GetString(1);
                    }
                    reader.Close();
                }
            }
            return nombre;
        }

        public String obtenerColor(String numero)
        {
            String nombre = "";
            if (numero.Equals("-65536")) nombre = "ROJO";
            else if (numero.Equals("-16711936")) nombre = "VERDE";
            else if (numero.Equals("-16776961")) nombre = "AZUL";
            else if (numero.Equals("-256")) nombre = "AMARILLO";
            else if (numero.Equals("-65281")) nombre = "MAGENTA";
            else if (numero.Equals("-16711681")) nombre = "CYAN";
            else if (numero.Equals("-16777216")) nombre = "BLANCO";
            else if (numero.Equals("-1")) nombre = "NEGRO";
            return nombre;
        }

        public String obtenerColorInt(String numero)
        {
            String nombre = "";
            if (numero.Equals("ROJO")) nombre = "-65536";
            else if (numero.Equals("VERDE")) nombre = "-16711936";
            else if (numero.Equals("AZUL")) nombre = "-16776961";
            else if (numero.Equals("AMARILLO")) nombre = "-256";
            else if (numero.Equals("MAGENTA")) nombre = "-65281";
            else if (numero.Equals("CYAN")) nombre = "-16711681";
            else if (numero.Equals("BLANCO")) nombre = "-16777216";
            else if (numero.Equals("NEGRO")) nombre = "-1";
            return nombre;
        }

        public String obtenerColorHex(int numeroAux)
        {
            String hex = "";
            System.Drawing.Color c = System.Drawing.ColorTranslator.FromWin32(numeroAux);
            hex = "#" + c.R.ToString("X2") + c.G.ToString("X2") + c.B.ToString("X2");
            Console.Write(hex);
            /*switch (hex){
                case "FF0000":
                    hex = "#0000FF";
                    break;
                case "0000FF":
                    hex = "#FF0000";
                    break;
                case "00FFFF":
                    hex = "##FFFF00";
                    break;
                case "FFFF00":
                    hex = "##FFFF00";
                    break;


            }*/
            if (hex.Equals("#FF0000")) hex = "#0000FF"; // RED to BLUE
            else if (hex.Equals("#0000FF")) hex = "#FF0000"; // BLUE to RED
            else if (hex.Equals("#00FFFF")) hex = "#FFFF00";  // CYAN to YELLOW
            else if (hex.Equals("#FFFF00")) hex = "#00FFFF";  // YELLOW to CYAN
            return hex;
        }

        public List<Dibujo> filtrarDibujosDEV(List<DB.OPERACION> f)
        {
            List<Dibujo> dibujos = new List<Dibujo>();
            StringBuilder sb = new StringBuilder();
            sb.Append("SELECT * FROM [OPERACION]");
            String sql = sb.ToString();

            for (int i = 0; i < f.Count; i++)
            {
                DB.OPERACION d = f[i];
                dibujos.Add(new Dibujo(d.CODIGO, d.NOMBRE,  d.COLOR.ToString(), d.TIPO, d.FECHA, d.ARTICULOS, d.OBSERVACIONES, d.TROZOS, d.PARCELA, d.FINCA, d.USUARIO));
            }

            for (int j = 0; j < dibujos.Count; j++)
            {
                dibujos[j].FINCA = obtenerNombreFinca(dibujos[j].FINCA);
                dibujos[j].PARCELA = obtenerNombreParcela(dibujos[j].PARCELA);
                dibujos[j].ARTICULOS = obtenerNombreArticulo(dibujos[j].ARTICULOS);
                dibujos[j].TIPO = obtenerNombreTipoOperacion(dibujos[j].TIPO);
                dibujos[j].USUARIO = obtenerNombreUsuario(dibujos[j].USUARIO);
            }

            return dibujos;
        }
    }
}