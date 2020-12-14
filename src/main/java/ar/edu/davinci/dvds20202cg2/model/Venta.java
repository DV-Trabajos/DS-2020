package ar.edu.davinci.dvds20202cg2.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


/**
 * Venta del día
 * 
 * @author nzalazar, fbotto y mgiron
 *
 */

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="tipo_venta")
@Table(name="ventas")

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Venta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "vta_id")
	private Long id;
	
	@ManyToOne(targetEntity = Cliente.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="vta_cli_id", referencedColumnName="cli_id")
	private Cliente cliente;
	
	@Column(name = "vta_fecha")
	@Temporal(TemporalType.DATE)
	private Date fecha;
	
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinColumn(name="itm_vta_id", referencedColumnName="vta_id", nullable = false)
	private List<Item> items;
	
	public abstract Double conRecargo(Double importeBase);
	
	public BigDecimal importeBruto() {
		Double suma = items.stream()
				.collect(Collectors.summingDouble(item -> item.importe().doubleValue()));
		return new BigDecimal(suma);
	}

	// Template Method
	public BigDecimal importeFinal() {
		Double suma = items.stream()
		.collect(Collectors.summingDouble(item -> conRecargo(item.importe().doubleValue())));
		return new BigDecimal(suma);
	}
	
	public boolean esDeFecha(Date fecha) {
		return (this.fecha.compareTo(fecha) == 0) ? true : false;
	}
}