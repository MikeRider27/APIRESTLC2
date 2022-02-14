/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DaoImp;

import Core.Conexion;
import Core.Util;
import Model.Dao.UsuarioDAO;
import Model.Dto.RolDTO;
import Model.Dto.UsuarioDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dgtic-miguel
 */
public class UsuarioDAOIMP implements UsuarioDAO {

    private String sql;
    private String sql2;
    private Conexion conexion;
    private PreparedStatement ps;
    private ResultSet rs;
    private String token;

    public UsuarioDAOIMP() {
        conexion = new Conexion();
        token = "";
    }

    @Override
    public boolean agregarRegistro(UsuarioDTO dto) {
        try {
            System.out.println("llego");

            conexion.Transaccion(Conexion.TR.INICIAR);
            sql = "INSERT INTO public.usuarios(nombre, nick, password, id_rol, estado) VALUES (?, ?, ?, ?, ?);";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            ps.setString(1, dto.getNombre());
            ps.setString(2, dto.getNick());
            ps.setString(3, dto.getPassword());
            ps.setInt(4, dto.getId_rol());
            ps.setBoolean(5, true);

            if (ps.executeUpdate() > 0) {
                conexion.Transaccion(Conexion.TR.CONFIRMAR);
                sql2 = "INSERT INTO public.usuario_token(id_usuario) VALUES ((select coalesce(max(id_usuario),0) from usuarios));";
                ps = conexion.obtenerConexion().prepareStatement(sql2);
                if (ps.executeUpdate() > 0) {
                    return true;
                } else {
                    conexion.Transaccion(Conexion.TR.CANCELAR);
                    return false;
                }

            } else {
                conexion.Transaccion(Conexion.TR.CANCELAR);
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            conexion.Transaccion(Conexion.TR.CANCELAR);
            return false;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public boolean modificarRegistro(UsuarioDTO dto) {
        try {
            conexion.Transaccion(Conexion.TR.INICIAR);
            sql = "UPDATE public.usuarios SET nombre=?, nick=?, password=?, id_rol=? WHERE id_usuario=?;";
            ps = conexion.obtenerConexion().prepareStatement(sql);
         ps.setString(1, dto.getNombre());
            ps.setString(2, dto.getNick());
            ps.setString(3, dto.getPassword());
            ps.setInt(4, dto.getId_rol());
             ps.setInt(5, dto.getId_usuario());
            if (ps.executeUpdate() > 0) {
                conexion.Transaccion(Conexion.TR.CONFIRMAR);
                return true;
            } else {
                conexion.Transaccion(Conexion.TR.CANCELAR);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RolDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            conexion.Transaccion(Conexion.TR.CANCELAR);
            return false;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(RolDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public boolean eliminarRegistro(UsuarioDTO dto) {
        try {
            conexion.Transaccion(Conexion.TR.INICIAR);
            sql = "DELETE FROM  public.usuarios  WHERE id_usuario=?;";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            ps.setInt(1, dto.getId_usuario());
            if (ps.executeUpdate() > 0) {
                conexion.Transaccion(Conexion.TR.CONFIRMAR);
                return true;
            } else {
                conexion.Transaccion(Conexion.TR.CANCELAR);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            conexion.Transaccion(Conexion.TR.CANCELAR);
            return false;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public UsuarioDTO recuperarRegistro(Integer id) {
        try {
            UsuarioDTO dto = null;
            sql = "SELECT u.id_usuario, u.nombre, u.nick, u.id_rol, r.descripcion as rol, u.estado\n"
                    + " FROM public.usuarios as u INNER JOIN roles as r ON u.id_rol = r.id_rol"
                    + " WHERE id_usuario = ? ";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                dto = new UsuarioDTO();
                dto.setId_usuario(rs.getInt("id_usuario"));
                dto.setNombre(rs.getString("nombre"));
                dto.setNick(rs.getString("nick"));
                dto.setRol(new RolDTO(rs.getInt("id_rol"), rs.getString("rol")));
                dto.setEstado(rs.getBoolean("estado"));

            }
            return dto;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public List<UsuarioDTO> recuperarRegistros() {
        try {
            UsuarioDTO dto = null;
            List<UsuarioDTO> lista;
            sql = "SELECT u.id_usuario, u.nombre, u.nick, u.id_rol, r.descripcion as rol, u.estado\n"
                    + "                    FROM public.usuarios as u INNER JOIN roles as r ON u.id_rol = r.id_rol";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            rs = ps.executeQuery();
            lista = new ArrayList<>();
            while (rs.next()) {
                dto = new UsuarioDTO();
                dto.setId_usuario(rs.getInt("id_usuario"));
                dto.setNombre(rs.getString("nombre"));
                dto.setNick(rs.getString("nick"));
                dto.setRol(new RolDTO(rs.getInt("id_rol"), rs.getString("rol")));
                dto.setEstado(rs.getBoolean("estado"));

                lista.add(dto);
            }
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public boolean validarUsuario(UsuarioDTO dto) {
        try {

            sql = "SELECT id_usuario FROM public.usuarios "
                    + "WHERE nick = ? AND password = ?  AND estado = TRUE";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            ps.setString(1, dto.getNick());
            ps.setString(2, dto.getPassword());
            rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("reconoce los datos del usuario ");
                token = Util.generarToken();
                int idUsuario = rs.getInt("id_usuario");
                conexion.Transaccion(Conexion.TR.INICIAR);
                sql = "UPDATE public.usuario_token SET token=?, creacion_token=now() WHERE id_usuario = ?; ";
                ps = conexion.obtenerConexion().prepareStatement(sql);
                ps.setString(1, token);
                ps.setInt(2, idUsuario);
                if (ps.executeUpdate() > 0) {
                    conexion.Transaccion(Conexion.TR.CONFIRMAR);
                    return true;
                } else {
                    conexion.Transaccion(Conexion.TR.CANCELAR);
                    return false;
                }
            }
        } catch (SQLException ex) {
            //Logger.getLogger(MenuSistemaDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error " + ex.getMessage());
            return false;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public String getToken() {
        if (!token.isEmpty()) {
            return token;
        }
        return null;
    }

    @Override
    public boolean inactivarUsuario(UsuarioDTO dto){
    try {
            conexion.Transaccion(Conexion.TR.INICIAR);
            sql = "UPDATE public.usuarios SET  estado = ? WHERE id_usuario=? ;";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            ps.setBoolean(1, false);
            ps.setInt(2, dto.getId_usuario());
            if (ps.executeUpdate() > 0) {
                conexion.Transaccion(Conexion.TR.CONFIRMAR);
                return true;
            } else {
                conexion.Transaccion(Conexion.TR.CANCELAR);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            conexion.Transaccion(Conexion.TR.CANCELAR);
            return false;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }  
    }

}
