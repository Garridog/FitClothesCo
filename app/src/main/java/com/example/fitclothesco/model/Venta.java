package com.example.fitclothesco.model;

public class Venta {

    public String idVenta;
    public String fecha;
    public String nombreCliente;
    public String prenda;
    public String totalVenta;
    public long timestamp;

    public Venta() {
    }

    public String getIdVenta() { return idVenta; }

    public void setIdVenta(String idVenta) { this.idVenta = idVenta; }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getPrenda() {
        return prenda;
    }

    public void setPrenda(String prenda) {
        this.prenda = prenda;
    }

    public String getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(String totalVenta) {
        this.totalVenta = totalVenta;
    }

    public long getTimestamp() { return timestamp; }

    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return nombreCliente;
    }
}
