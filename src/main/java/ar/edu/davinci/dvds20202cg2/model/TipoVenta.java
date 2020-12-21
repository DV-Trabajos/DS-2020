package ar.edu.davinci.dvds20202cg2.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Tipo de Ventas
 * 
 * @author nzalazar, fbotto y mgiron
 *
 */
public enum TipoVenta {

	EFECTIVO("Efectivo"), 
	TARJETA("Tarjeta");

	private String descripcion;

	TipoVenta(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}
	
	public static List<TipoVenta> getTipoVentas() {
		List<TipoVenta> tipoVentas = new LinkedList<TipoVenta>();
		tipoVentas.add(TipoVenta.EFECTIVO);
		tipoVentas.add(TipoVenta.TARJETA);
		return tipoVentas;
	}
}