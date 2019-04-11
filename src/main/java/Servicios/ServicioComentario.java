package Servicios;

import Modelos.Comentario;

import java.sql.*;
import java.util.ArrayList;

public class ServicioComentario {

    public static ArrayList<Comentario> listarComentarios(long articuloID) {
        Connection conexion = ServicioBaseDatos.getInstancia().getConexion();
        ArrayList<Comentario> comentarios = new ArrayList<>();

        try {
            String comentariosQuery = "SELECT * FROM comentarios WHERE articuloid = " + articuloID + ";";

            // Ejecuta el query pasado por parámetro "usuarioDefecto".
            Statement statement = conexion.createStatement();
            ResultSet resultado = statement.executeQuery(comentariosQuery);

            while(resultado.next()) {
                comentarios.add(
                    new Comentario(
                        resultado.getLong("id"),
                        resultado.getNString("comentario"),
                        ServicioUsuario.buscarUsuario(resultado.getLong("autor")),
                null
                    )
                );
            }

            statement.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally{
            try {
                conexion.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return comentarios;
    }

    public static boolean crearComentario(long id, String comentario, Long autor, Long articuloID) {
        boolean creadoCorrectamente = false;
        Connection conexion = ServicioBaseDatos.getInstancia().getConexion();

        try {
            String comentarioNuevo = "MERGE INTO comentarios \n" +
                    "KEY(ID) \n" +
                    "VALUES (" + id + ",'" + comentario + "'," + autor + "," + articuloID + ");";

            PreparedStatement prepareStatement = conexion.prepareStatement(comentarioNuevo);

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

    public static Long conseguirTamano() {
        Long ultimoID = new Long(0);
        Connection conexion = ServicioBaseDatos.getInstancia().getConexion();

        try {
            String conseguirTamanoTabla = "SELECT TOP 1 * FROM comentarios ORDER BY ID DESC;";

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
