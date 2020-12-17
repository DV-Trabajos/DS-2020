package ar.edu.davinci.dvds20202cg2.model;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Calcular Venta en Efectivo
 * 
 * @author nzalazar, fbotto y mgiron
 *
 */

@Entity
@PrimaryKeyJoinColumn(name = "vta_id")
@DiscriminatorValue("EFECTIVO")
@Table(name="ventas_efectivo")

@Data
@NoArgsConstructor(force = true)
@SuperBuilder
public class VentaEfectivo extends Venta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2918042443389239712L;

	@Override
	public Double conRecargo(Double importeBase) {
		return importeBase;
	}
}