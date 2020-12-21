package ar.edu.davinci.dvds20202cg2.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class PrendaTest {

	@Test
	void testBuilder() {
		
		//Given
		Long 		id = 1L;
		String 		camisa = "Camisa";
		TipoPrenda	tipo = TipoPrenda.CAMISA;
		BigDecimal	precio = new BigDecimal(10.2D);
		
		Prenda prenda = Prenda.builder().id(1L)
										.descripcion("Camisa")
										.tipo(TipoPrenda.CAMISA)
										.precioBase(new BigDecimal(10.2D))
										.build();
		
		assertNotNull(prenda);
		assertEquals(id, prenda.getId());
		assertEquals(camisa, prenda.getDescripcion());
		assertEquals(tipo, prenda.getTipo());
		assertEquals(precio, prenda.getPrecioBase());		
	}
}
