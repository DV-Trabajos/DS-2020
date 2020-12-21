package ar.edu.davinci.dvds20202cg2.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="clientes")

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4818599039117586768L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "cli_id")
	private Long id;
	
	@Column(name = "cli_nombre")
	@NotEmpty(message = "El nombre no puede estar vacío")
	private String nombre;

	@Column(name = "cli_apellido")
	@NotEmpty(message = "El apellido no puede estar vacío")
	private String apellido;
	
	public String getRazonSocial() {
		return nombre + " " + apellido;
	}
}