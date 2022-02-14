/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Dto;

/**
 *
 * @author mike
 */
public class TipoVehiculoDTO {

    private Integer id_tipo;
    private String descripcion;
    private String token;

    public TipoVehiculoDTO(Integer id_tipo) {
        this.id_tipo = id_tipo;
    }

    public TipoVehiculoDTO() {
    }

    public TipoVehiculoDTO(Integer id_tipo, String descripcion) {
        this.id_tipo = id_tipo;
        this.descripcion = descripcion;
    }

    public Integer getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(Integer id_tipo) {
        this.id_tipo = id_tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    
}
