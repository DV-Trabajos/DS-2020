package ar.edu.davinci.dvds20202cg2.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Prenda
 * 
 * @author nzalazar, fbotto y mgiron
 *
 */

@Entity
@Table(name="prendas")

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prenda implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1591131176880228892L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "prd_id")
	private Long id;
	//private Estado estado;

	@Column(name = "prd_precio_base")
	@NotNull(message="El precio base es obligatorio")
	private BigDecimal precioBase;
	
	@Column(name = "prd_tipo_prenda")
	@NotNull(message="Debe seleccionar un valor de la lista")
	@Enumerated(EnumType.STRING)	
	private TipoPrenda tipo;
	
	@Column(name = "prd_descripcion")
	@NotEmpty(message = "La descripción no puede estar vacía")
	private String descripcion;
	
	public BigDecimal getPrecioFinal(){
		return precioBase; //estado.precioFinal(precioPropio);
	}
}