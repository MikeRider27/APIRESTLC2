/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DaoImp;

import Core.Conexion;
import Model.Dao.ClienteDAO;
import Model.Dto.CiudadDTO;
import Model.Dto.ClienteDTO;
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
public class ClienteDAOIMP implements ClienteDAO {

    private String sql;
    private Conexion conexion;
    private PreparedStatement ps;
    private ResultSet rs;

    public ClienteDAOIMP() {
        conexion = new Conexion();
    }

    @Override
    public boolean agregarRegistro(ClienteDTO dto) {
        try {
            conexion.Transaccion(Conexion.TR.INICIAR);
            sql = "INSERT INTO public.cliente(cedula, nombres, apellidos, telefono, email, id_ciudad) "
                    + "VALUES (?, ?, ?, ?, ?, ?);";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            ps.setString(1, dto.getCedula());
            ps.setString(2, dto.getNombres());
            ps.setString(3, dto.getApellidos());
            ps.setString(4, dto.getTelefono());
            ps.setString(5, dto.getEmail());
            ps.setInt(6, dto.getId_ciudad());
            if (ps.executeUpdate() > 0) {
                conexion.Transaccion(Conexion.TR.CONFIRMAR);
                return true;
            } else {
                conexion.Transaccion(Conexion.TR.CANCELAR);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            conexion.Transaccion(Conexion.TR.CANCELAR);
            return false;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(ClienteDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public boolean modificarRegistro(ClienteDTO dto) {
        try {
            conexion.Transaccion(Conexion.TR.INICIAR);
            sql = "UPDATE public.cliente SET  cedula=?, nombres=?, apellidos=?, telefono=?, email=?, id_ciudad=? WHERE id_cliente=?;";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            ps.setString(1, dto.getCedula());
            ps.setString(2, dto.getNombres());
            ps.setString(3, dto.getApellidos());
            ps.setString(4, dto.getTelefono());
            ps.setString(5, dto.getEmail());
            ps.setInt(6, dto.getId_ciudad());
            ps.setInt(7, dto.getId_cliente());
            if (ps.executeUpdate() > 0) {
                conexion.Transaccion(Conexion.TR.CONFIRMAR);
                return true;
            } else {
                conexion.Transaccion(Conexion.TR.CANCELAR);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            conexion.Transaccion(Conexion.TR.CANCELAR);
            return false;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(ClienteDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public boolean eliminarRegistro(ClienteDTO dto) {
        try {
            conexion.Transaccion(Conexion.TR.INICIAR);
            sql = "DELETE FROM  public.cliente  WHERE id_cliente=?;";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            ps.setInt(1, dto.getId_cliente());
            if (ps.executeUpdate() > 0) {
                conexion.Transaccion(Conexion.TR.CONFIRMAR);
                return true;
            } else {
                conexion.Transaccion(Conexion.TR.CANCELAR);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            conexion.Transaccion(Conexion.TR.CANCELAR);
            return false;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(ClienteDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public ClienteDTO recuperarRegistro(Integer id) {
        try {
            ClienteDTO dto = null;
            sql = "SELECT c.id_cliente, c.cedula, c.nombres, c.apellidos, c.telefono, c.email, c.id_ciudad, d.descripcion as ciudad\n"
                    + "	FROM public.cliente as c inner join ciudad as d ON c.id_ciudad = d.id_ciudad WHERE c.id_cliente = ? ";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                dto = new ClienteDTO();
                dto.setId_cliente(rs.getInt("id_cliente"));
                dto.setCedula(rs.getString("cedula"));
                dto.setNombres(rs.getString("nombres"));
                dto.setApellidos(rs.getString("apellidos"));
                dto.setTelefono(rs.getString("telefono"));
                dto.setEmail(rs.getString("email"));
                dto.setId_ciudad(rs.getInt("id_ciudad"));
               // dto.setCiudad(new CiudadDTO(rs.getInt("id_ciudad"), rs.getString("ciudad")));
            }
            return dto;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(ClienteDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public List<ClienteDTO> recuperarRegistros() {
        try {
            ClienteDTO dto = null;
            List<ClienteDTO> lista;
            sql = "SELECT c.id_cliente, c.cedula, c.nombres, c.apellidos, c.telefono, c.email, c.id_ciudad, d.descripcion as ciudad\n"
                    + "	FROM public.cliente as c inner join ciudad as d ON c.id_ciudad = d.id_ciudad";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            rs = ps.executeQuery();
            lista = new ArrayList<>();
            while (rs.next()) {
                dto = new ClienteDTO();
                dto.setId_cliente(rs.getInt("id_cliente"));
                dto.setCedula(rs.getString("cedula"));
                dto.setNombres(rs.getString("nombres"));
                dto.setApellidos(rs.getString("apellidos"));
                dto.setTelefono(rs.getString("telefono"));
                dto.setEmail(rs.getString("email"));
                //dto.setId_ciudad(rs.getInt("id_ciudad"));
                dto.setCiudad(new CiudadDTO(rs.getInt("id_ciudad"), rs.getString("ciudad")));
                lista.add(dto);
            }
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(ColorDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(ColorDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
