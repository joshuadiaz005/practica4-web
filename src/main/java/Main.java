import Modelos.Articulo;
import Modelos.Etiqueta;
import Modelos.Usuario;
import Servicios.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.jasypt.util.text.StrongTextEncryptor;
import spark.Session;

import java.io.StringWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;
public class Main {
    public static void main(String[] args) {

        try {
            ServicioBootstrap.iniciarBaseDatos();

            ServicioBaseDatos.getInstancia().testConexion();


            ServicioBootstrap.crearTablas();

            ServicioUsuario serviciouser = new ServicioUsuario();
            serviciouser.crearUsuarioPorDefecto();


            ArrayList<Etiqueta> etiquetasAux = new ArrayList<>();
            ArrayList<Articulo> articulos = ServicioArticulo.listarArticulos();
            String nombreUsuario = "";
            Boolean etiquetasBool = false;
            Usuario usuario;

            final Configuration configuration = new Configuration(new Version(2, 3, 23));
            configuration.setClassForTemplateLoading(Main.class, "/");

            staticFiles.location("/publico");

            before("/", (req, res) -> {
                if (req.cookie("sesionSemanal") != null) {
                    StrongTextEncryptor encriptador = new StrongTextEncryptor();
                    encriptador.setPassword("defaut");
                    String sesionSemanal = encriptador.decrypt(req.cookie("sesionSemanal"));

                    Usuario usuarioRestaurado = ServicioUsuario.restaurarSesion(sesionSemanal);
                    nombreUsuario = usuarioRestaurado.getUsername();
                    usuario = usuarioRestaurado;
                    req.session().attribute("sesionUsuario", usuarioRestaurado);

                    if (usuarioRestaurado != null) {
                        req.session(true);
                        req.session().attribute("sesionUsuario", usuarioRestaurado);
                    }
                }

                if (req.session().attribute("sesionUsuario") == null) {
                    res.redirect("/login");
                }
            });

            before("/registrar", (req, res) -> {
                if (req.session().attribute("sesionUsuario") == null) {
                    res.redirect("/");
                }
                if (!usuario.isAdminstrator()) {
                    res.redirect("/");
                }
            });

            before("/articulo/crear", (req, res) -> {
                if (req.session().attribute("sesionUsuario") == null) {
                    res.redirect("/");
                }

                if (!usuario.isAdminstrator()) {
                    if (!usuario.isAutor()) {
                        res.redirect("/");
                    }
                }
            });

            before("/articulo/editar/:id", (req, res) -> {
                if (req.session().attribute("sesionUsuario") == null) {
                    res.redirect("/");
                }
                if (!usuario.isAdminstrator()) {
                    if (!usuario.isAutor()) {
                        res.redirect("/");
                    }
                }
            });

            before("/articulo/eliminar/:id", (req, res) -> {
                if (req.session().attribute("sesionUsuario") == null) {
                    res.redirect("/");
                }

                if (!usuario.isAdminstrator()) {
                    if (!usuario.isAutor()) {
                        res.redirect("/");
                    }
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

