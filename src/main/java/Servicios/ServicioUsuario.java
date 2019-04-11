
package Servicios;

import Modelos.Usuario;

import java.sql.*;

public class ServicioUsuario {
    public boolean crearUsuarioPorDefecto(){
        boolean creadoCorrectamente = false;
        Connection conexion = ServicioBaseDatos.getInstancia().getConexion();

        try {
            Usuario usu = buscarUsuario(1);
            if(usu == null) {
                // Crealo si no existe y si existe actualizalo.
                String usuarioDefecto = "INSERT INTO usuarios \n" +
                        "VALUES (1, 'admin', '1234', true, true, null);";

                // Ejecuta el query pasado por parámetro "usuarioDefecto".
                PreparedStatement prepareStatement = conexion.prepareStatement(usuarioDefecto);

                // Si se ejecutó el query bien pues la cantidad de filas de la tabla debe ser mayor a 0, pues se ha insertado una fila.
                int fila = prepareStatement.executeUpdate();
                creadoCorrectamente = fila > 0 ;
            } else {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally{
            try {
                conexion.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return creadoCorrectamente;
    }

    public boolean registrarUsuarios(Long id, String usuario, String password, boolean administador, boolean autor){
        boolean creadoCorrectamente = false;
        Connection conexion = ServicioBaseDatos.getInstancia().getConexion();

        try {
            String usuarioNuevo = "MERGE INTO usuarios \n" +
                    "KEY(ID) \n" +
                    "VALUES (" + id + "," + usuario + "," + password + "," + administador + "," + autor + ", " + null + ");";

            // Ejecuta el query pasado por parámetro "usuarioDefecto".
            PreparedStatement prepareStatement = conexion.prepareStatement(usuarioNuevo);

            int fila = prepareStatement.executeUpdate();
            creadoCorrectamente = fila > 0 ;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally{
            try {
                conexion.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return creadoCorrectamente;
    }

    public static Usuario buscarUsuario(long id) {
        Usuario usuario = null;
        try
        {
            ServicioBaseDatos servicioBaseDatos = new ServicioBaseDatos();
            Connection conexion = servicioBaseDatos.getConexion();

            Statement statement = conexion.createStatement();
            ResultSet rs = statement.executeQuery("select * from Usuarios where id = " + id + ";");
            while (rs.next())
            {
                usuario = new Usuario(rs.getLong("id"), rs.getNString("username"), rs.getNString("password"), rs.getBoolean("administrator"), rs.getBoolean("autor"));
            }
            statement.close();
            conexion.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return usuario;
    }

    public static boolean guardarSesion(String sesion, long id) {
        boolean creadoCorrectamente = false;
        Connection conexion = ServicioBaseDatos.getInstancia().getConexion();

        try {
            // Crealo si no existe y si existe actualizalo.
            String usuarioNuevo = "UPDATE usuarios \n" +
                    "SET sesion='" + sesion + "' WHERE id=" + id + " ;";

            // Ejecuta el query pasado por parámetro "usuarioDefecto".
            PreparedStatement prepareStatement = conexion.prepareStatement(usuarioNuevo);

            // Si se ejecutó el query bien pues la cantidad de filas de la tabla debe ser mayor a 0, pues se ha insertado una fila.
            int fila = prepareStatement.executeUpdate();
            creadoCorrectamente = fila > 0 ;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally{
            try {
                conexion.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return creadoCorrectamente;
    }

    public static Usuario restaurarSesion(String sesion) {
        try
        {
            ServicioBaseDatos servicioBaseDatos = new ServicioBaseDatos();
            Connection conexion = servicioBaseDatos.getConexion();

            Statement statement = conexion.createStatement();
            ResultSet rs = statement.executeQuery("select * from Usuarios where sesion = '" + sesion +"';");
            while (rs.next())
            {
                return new Usuario(rs.getLong("id"), rs.getNString("username"), rs.getNString("password"), rs.getBoolean("administrator"), rs.getBoolean("autor"));
            }

            statement.close();
            conexion.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Usuario elUsuarioExiste(String nombreUsuario, String password)
    {
        try
        {
            ServicioBaseDatos servicioBaseDatos = new ServicioBaseDatos();
            Connection conexion = servicioBaseDatos.getConexion();

            Statement statement = conexion.createStatement();
            ResultSet rs = statement.executeQuery("select * from Usuarios where username = '" + nombreUsuario +"' and password = '" + password + "'");
            while (rs.next())
            {
                return new Usuario(rs.getLong("id"), rs.getNString("username"), rs.getNString("password"), rs.getBoolean("administrator"), rs.getBoolean("autor"));
            }
            statement.close();
            conexion.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Long conseguirTamano() {
        Long ultimoID = new Long(0);
        Connection conexion = ServicioBaseDatos.getInstancia().getConexion();

        try {
            // Crealo si no existe y si existe actualizalo.
            String conseguirTamanoTabla = "SELECT TOP 1 * FROM usuarios ORDER BY ID DESC;";

            // Ejecuta el query.
            PreparedStatement prepareStatement = conexion.prepareStatement(conseguirTamanoTabla);
            ResultSet resultado = prepareStatement.executeQuery();
            while(resultado.next()){
                ultimoID = resultado.getLong("id");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally{
            try {
                conexion.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return ultimoID;
    }
}
