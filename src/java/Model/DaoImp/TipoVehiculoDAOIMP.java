/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DaoImp;

import Core.Conexion;
import Model.Dao.TipoVehiculoDAO;
import Model.Dto.TipoVehiculoDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mike
 */
public class TipoVehiculoDAOIMP implements TipoVehiculoDAO{
private String sql;
    private Conexion conexion;
    private PreparedStatement ps;
    private ResultSet rs;

    public TipoVehiculoDAOIMP() {
        conexion = new Conexion();
    }

    @Override
    public boolean agregarRegistro(TipoVehiculoDTO dto) {
       try {
            conexion.Transaccion(Conexion.TR.INICIAR);
            sql = "INSERT INTO public.tipo_vehiculo(descripcion) VALUES (?);";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            ps.setString(1, dto.getDescripcion());
            if (ps.executeUpdate() > 0) {
                conexion.Transaccion(Conexion.TR.CONFIRMAR);
                return true;
            } else {
                conexion.Transaccion(Conexion.TR.CANCELAR);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipoVehiculoDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            conexion.Transaccion(Conexion.TR.CANCELAR);
            return false;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(TipoVehiculoDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public boolean modificarRegistro(TipoVehiculoDTO dto) {
        try {
            conexion.Transaccion(Conexion.TR.INICIAR);
            sql = "UPDATE public.tipo_vehiculo SET descripcion=? WHERE id_tipo=?;";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            ps.setString(1, dto.getDescripcion());                    
            ps.setInt(2, dto.getId_tipo());
            if (ps.executeUpdate() > 0) {
                conexion.Transaccion(Conexion.TR.CONFIRMAR);
                return true;
            } else {
                conexion.Transaccion(Conexion.TR.CANCELAR);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipoVehiculoDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            conexion.Transaccion(Conexion.TR.CANCELAR);
            return false;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(TipoVehiculoDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public boolean eliminarRegistro(TipoVehiculoDTO dto) {
    try {
            conexion.Transaccion(Conexion.TR.INICIAR);
            sql = "DELETE FROM  public.tipo_vehiculo  WHERE id_tipo=?;";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            ps.setInt(1, dto.getId_tipo());
            if (ps.executeUpdate() > 0) {
                conexion.Transaccion(Conexion.TR.CONFIRMAR);
                return true;
            } else {
                conexion.Transaccion(Conexion.TR.CANCELAR);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipoVehiculoDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            conexion.Transaccion(Conexion.TR.CANCELAR);
            return false;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(TipoVehiculoDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public TipoVehiculoDTO recuperarRegistro(Integer id) {
    try {
            TipoVehiculoDTO dto = null;
            sql = "SELECT id_tipo, descripcion FROM public.tipo_vehiculo WHERE id_tipo = ? ";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                dto = new TipoVehiculoDTO();
                dto.setId_tipo(rs.getInt("id_tipo"));
                dto.setDescripcion(rs.getString("descripcion"));
                         
            }
            return dto;
        } catch (SQLException ex) {
            Logger.getLogger(TipoVehiculoDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(TipoVehiculoDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public List<TipoVehiculoDTO> recuperarRegistros() {
    try {
            TipoVehiculoDTO dto = null;
            List<TipoVehiculoDTO> lista;
            sql = "SELECT id_tipo, descripcion FROM public.tipo_vehiculo";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            rs = ps.executeQuery();
            lista = new ArrayList<>();
            while (rs.next()) {
                dto = new TipoVehiculoDTO();
                dto.setId_tipo(rs.getInt("id_tipo"));
                dto.setDescripcion(rs.getString("descripcion"));          
                lista.add(dto);
            }
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(TipoVehiculoDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(TipoVehiculoDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
