package ar.edu.davinci.dvds20202cg2.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Item de Venta
 * 
 * @author nzalazar, fbotto y mgiron
 *
 */

@Entity
@Table(name="venta_items")

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "itm_id")
	private Long id;
	
	@Column(name = "itm_cantidad")
	private Integer cantidad;
	
	@ManyToOne(targetEntity = Prenda.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="itm_prd_id", referencedColumnName="prd_id")
	private Prenda prenda;
	
	public BigDecimal importe() {
		return prenda.getPrecioFinal().multiply(new BigDecimal(cantidad));
	}
}