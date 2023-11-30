package mx.uv;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.jdbc.StatementImpl;

public class DAO {

    public static List<Usuario> getUsuarios() {
        StatementImpl stm = null;
        ResultSet rs = null;
        Connection conn = null;
        List<Usuario> resultado = new ArrayList<>();

        conn = Conexion.getConnection();

        try {
            String sql = "SELECT * from usuarios";
            stm = (StatementImpl) conn.createStatement();
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                Usuario u = new Usuario(rs.getString("id"), rs.getString("nombre"), rs.getString("correo"));
                resultado.add(u);
            }
        } catch (Exception e) {
            System.out.println("Error del statement.");
            System.out.println(e);
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            rs = null;
            if (stm != null) {
                try {
                    ((Connection) stm).close();
                } catch (Exception e) {
                    System.out.println(e);
                }
                stm = null;
            }
            try {
                conn.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return resultado;
    }

    public static Usuario getUsuario(Usuario usuario) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        Connection conn = null;
        String msj = "";
        String sql = "SELECT * FROM usuarios WHERE id = ?";

        conn = Conexion.getConnection();
        try {
            stm = (PreparedStatement) conn.prepareStatement(sql);
            stm.setString(1, usuario.getId());
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                usuario = new Usuario(rs.getString("id"), rs.getString("nombre"), rs.getString("correo"));
                msj = "Usuario encontrado";
            }else{
                msj = "Usuario no encontrado";
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
                stm = null;
            }
            try {
                conn.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return usuario;
    }

    public static String crearUsuario(Usuario u) {
        PreparedStatement stm = null;
        Connection conn = null;
        String msj = "";
        String sql = "INSERT INTO usuarios (id, nombre, correo) values (?,?,?)";

        conn = Conexion.getConnection();
        try {

            stm = (PreparedStatement) conn.prepareStatement(sql);
            stm.setString(1, u.getId());
            stm.setString(2, u.getNombre());
            stm.setString(3, u.getCorreo());
            if (stm.executeUpdate() > 0)
                msj = "usuario agregado";
            else
                msj = "usuario no agregado";

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
                stm = null;
            }
            try {
                conn.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return msj;
    }

    public static String eliminarUsuario(String id) {
        PreparedStatement stm = null;
        Connection conn = null;
        String msj = "";
        String consulta = "DELETE FROM usuarios WHERE id = ?";

        conn = Conexion.getConnection();
        try {
            stm = conn.prepareStatement(consulta);
            stm.setString(1, id);
            if (stm.executeUpdate() > 0) {
                msj = "Usuario Eliminado";
            } else {
                msj = "Error al eliminar Usuario";
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
                stm = null;
            }
            try {
                conn.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return msj;
    }

    public static String editarUsuario(Usuario usuario) {
        PreparedStatement stm = null;
        Connection conn = null;
        String msj = "";
        String consulta = "UPDATE usuarios SET nombre=?, correo=? WHERE id =?";

        conn = Conexion.getConnection();
        try {
            stm = conn.prepareStatement(consulta);
            stm.setString(1, usuario.getNombre());
            stm.setString(2, usuario.getCorreo());
            stm.setString(3, usuario.getId());

            if (stm.executeUpdate() > 0) {
                msj = "Usuario Editado";
            } else {
                msj = "Error al editar Usuario";
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return msj;
    }
}
