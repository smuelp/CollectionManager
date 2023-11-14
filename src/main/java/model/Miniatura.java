/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author samue
 */
public class Miniatura {

    private int id;
    private String lugar;
    private String estado;
    private String pais;
    private Date data;
    private String status;
    private String foto;
    private Usuario user;

    public Miniatura() {

    }

    public Miniatura(String lugar, String estado, String pais, Date data, String status, String foto, Usuario user) {
        this.lugar = lugar;
        this.estado = estado;
        this.pais = pais;
        this.data = data;
        this.status = status;
        this.foto = foto;
        this.user = user;
    }
    
    private Map<StatusLugar, Double> StatusLugar;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public Map<StatusLugar, Double> getStatusLugar() {
        return StatusLugar;
    }

    public void setStatusLugar(Map<StatusLugar, Double> StatusLugar) {
        this.StatusLugar = StatusLugar;
    }
 
    @Override
    public String toString() {
        return id + " | " + lugar + " | " + estado + " | " + pais + " | " + data + " | " + foto;
    }

}
