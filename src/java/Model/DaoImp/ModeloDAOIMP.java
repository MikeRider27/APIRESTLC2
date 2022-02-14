/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DaoImp;

import Core.Conexion;
import Model.Dao.ModeloDAO;
import Model.Dto.MarcaDTO;
import Model.Dto.ModeloDTO;
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
public class ModeloDAOIMP implements ModeloDAO {

    private String sql;
    private Conexion conexion;
    private PreparedStatement ps;
    private ResultSet rs;

    public ModeloDAOIMP() {
        conexion = new Conexion();
    }

    @Override
    public boolean agregarRegistro(ModeloDTO dto) {
        try {
            conexion.Transaccion(Conexion.TR.INICIAR);
            sql = "INSERT INTO public.modelo(descripcion, id_marca) VALUES (?,?);";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            ps.setString(1, dto.getDescripcion());
            ps.setInt(2, dto.getId_marca());
            if (ps.executeUpdate() > 0) {
                conexion.Transaccion(Conexion.TR.CONFIRMAR);
                return true;
            } else {
                conexion.Transaccion(Conexion.TR.CANCELAR);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            conexion.Transaccion(Conexion.TR.CANCELAR);
            return false;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(ModeloDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public boolean modificarRegistro(ModeloDTO dto) {
        try {
            conexion.Transaccion(Conexion.TR.INICIAR);
            sql = "UPDATE public.modelo SET descripcion=?, id_marca=? WHERE id_modelo=?;";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            ps.setString(1, dto.getDescripcion());
            ps.setInt(2, dto.getId_marca());
            ps.setInt(3, dto.getId_modelo());
            if (ps.executeUpdate() > 0) {
                conexion.Transaccion(Conexion.TR.CONFIRMAR);
                return true;
            } else {
                conexion.Transaccion(Conexion.TR.CANCELAR);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            conexion.Transaccion(Conexion.TR.CANCELAR);
            return false;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(ModeloDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public boolean eliminarRegistro(ModeloDTO dto) {
        try {
            conexion.Transaccion(Conexion.TR.INICIAR);
            sql = "DELETE FROM  public.modelo  WHERE id_modelo=?;";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            ps.setInt(1, dto.getId_modelo());
            if (ps.executeUpdate() > 0) {
                conexion.Transaccion(Conexion.TR.CONFIRMAR);
                return true;
            } else {
                conexion.Transaccion(Conexion.TR.CANCELAR);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            conexion.Transaccion(Conexion.TR.CANCELAR);
            return false;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(ModeloDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public ModeloDTO recuperarRegistro(Integer id) {
        try {
            ModeloDTO dto = null;
            sql = "SELECT c.id_modelo, c.descripcion, c.id_marca, m.descripcion as marca "
                    + "FROM modelo as c inner join marca as m ON c.id_marca = m.id_marca "
                    + " WHERE id_modelo = ? ";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                dto = new ModeloDTO();
                dto.setId_modelo(rs.getInt("id_modelo"));
                dto.setDescripcion(rs.getString("descripcion"));
                dto.setMarca(new MarcaDTO(rs.getInt("id_marca"), rs.getString("marca")));
            }
            return dto;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(ModeloDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public List<ModeloDTO> recuperarRegistros() {
        try {
            ModeloDTO dto = null;
            List<ModeloDTO> lista;
            sql = "SELECT c.id_modelo, c.descripcion, c.id_marca, m.descripcion as marca "
                    + "FROM modelo as c inner join marca as m ON c.id_marca = m.id_marca ";
            ps = conexion.obtenerConexion().prepareStatement(sql);
            rs = ps.executeQuery();
            lista = new ArrayList<>();
            while (rs.next()) {
                 dto = new ModeloDTO();
              dto.setId_modelo(rs.getInt("id_modelo"));
                dto.setDescripcion(rs.getString("descripcion"));
                dto.setMarca(new MarcaDTO(rs.getInt("id_marca"), rs.getString("marca")));
                lista.add(dto);
            }
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                conexion.cerrarConexion();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(ModeloDAOIMP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
