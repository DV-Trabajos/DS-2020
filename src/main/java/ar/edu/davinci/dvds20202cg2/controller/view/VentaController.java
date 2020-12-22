package ar.edu.davinci.dvds20202cg2.controller.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.davinci.dvds20202cg2.controller.TiendaApp;
import ar.edu.davinci.dvds20202cg2.controller.view.VentaController;
import ar.edu.davinci.dvds20202cg2.service.ClienteService;
import ar.edu.davinci.dvds20202cg2.service.ItemService;
import ar.edu.davinci.dvds20202cg2.service.PrendaService;
import ar.edu.davinci.dvds20202cg2.service.VentaService;
import ar.edu.davinci.dvds20202cg2.model.Cliente;
import ar.edu.davinci.dvds20202cg2.model.Item;
import ar.edu.davinci.dvds20202cg2.model.Prenda;
import ar.edu.davinci.dvds20202cg2.model.TipoVenta;
import ar.edu.davinci.dvds20202cg2.model.Venta;
import ar.edu.davinci.dvds20202cg2.model.VentaEfectivo;
import ar.edu.davinci.dvds20202cg2.model.VentaTarjeta;


@Controller
public class VentaController extends TiendaApp {
	private final Logger LOGGER = LoggerFactory.getLogger(VentaController.class);

	@Autowired
	private VentaService ventaService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private PrendaService prendaService;
	
	@Autowired
	private ItemService itemService;
	
	@GetMapping(path = "ventas/list")
	public String showVentaPage(Model model) {
		LOGGER.info("GET - showVentaPage  - /ventas/list");
		
		Pageable pageable = PageRequest.of(0, 20);
		Page<Venta> ventas = ventaService.list(pageable);
		LOGGER.info("GET - showVentaPage venta importe final: " + ventas.getContent().toString());
		
		model.addAttribute("listVentas", ventas);

		LOGGER.info("ventas.size: " + ventas.getNumberOfElements());
		return "ventas/list_ventas";
	}
	
	@GetMapping(path = "/ventas/new")
	public String showNewVentaPage(Model model) {
		LOGGER.info("GET - showNewVentaPage - /ventas/new");
		
		//String tipoVenta = "Efectivo";
		//model.addAttribute("tipoVenta", tipoVenta);
		model.addAttribute("tipoVentas", ventaService.getTipoVentas());
		LOGGER.info("tipoVentas: " + ventaService.getTipoVentas());
		
		/*VentaEfectivo venta = new VentaEfectivo();
		model.addAttribute("venta", venta);
		LOGGER.info("ventas: " + venta.toString());*/

		List<Cliente> clientes = clienteService.listAll();
		model.addAttribute("listClientes", clientes);
		LOGGER.info("clientes: " + clientes.toString());
		
		List<Item> items = itemService.listAll();
		model.addAttribute("items", items);

		return "ventas/new_ventas";
	}
	
	@GetMapping(path = "/items/add_items/{id}")
	public String showItem(@Valid Model model, @PathVariable(name = "id") Long ventaId) {
		LOGGER.info("GET - showItem - /ventas/item");
		
		Optional<Venta> venta = ventaService.findById(ventaId);
		Item item = new Item();
		
		if(venta.isPresent()) {
			item.setVenta(venta.get());
		}
		
		model.addAttribute("item", item);
		
		List<Prenda> prendas = prendaService.listAll();
		model.addAttribute("prendas", prendas);
		model.addAttribute("ventaId", ventaId);
		
		LOGGER.info("items" + item.toString());

		return "items/add_items";
	}
	
	@PostMapping(value = "/ventas/saveItems")
	public String saveItems(@Valid @ModelAttribute("item") Item item, @ModelAttribute("ventaId") String ventaId, 
							@ModelAttribute("itemId") String itemId, BindingResult result, ModelMap model) {
		LOGGER.info("POST - saveItem - /ventas/saveItems");
		LOGGER.info("item: " + item.toString());
		
		if (result.hasErrors()) {
			model.addAttribute("item", item);
			model.addAttribute("ventaId", ventaId);
			model.addAttribute("itemId", itemId);			
		}
		else {
			//Editar item
			if (!itemId.isEmpty()) {
				Optional<Item> foundItem = itemService.findById(Long.parseLong(itemId));
				foundItem.get().setCantidad(item.getCantidad());
				foundItem.get().setPrenda(item.getPrenda());
				
				itemService.save(foundItem.get());
			}
			//Nuevo item
			else {
				Optional<Venta> venta = ventaService.findById(Long.parseLong(ventaId));
				item.setVenta(venta.get());
				
				itemService.save(item);
			}			
		}
		
		return "redirect:edit/" + ventaId;
	}
	
	/*@PostMapping(value = "/ventas/saveItems_newVenta")
	public String saveItemsNewVenta(@ModelAttribute("item") Item item, @ModelAttribute("ventaId") String ventaId, @ModelAttribute("itemId") String itemId) {
		LOGGER.info("POST - saveItem - /ventas/saveItems");
		LOGGER.info("item: " + item.toString());
		
		//Editar item
		if (!itemId.isEmpty()) {
			Optional<Item> foundItem = itemService.findById(Long.parseLong(itemId));
			foundItem.get().setCantidad(item.getCantidad());
			foundItem.get().setPrenda(item.getPrenda());
			
			itemService.save(foundItem.get());
		}
		//Nuevo item
		else {
			Optional<Venta> venta = ventaService.findById(Long.parseLong(ventaId));
			item.setVenta(venta.get());
			
			itemService.save(item);
		}
		
		return "redirect:edit/" + ventaId;
	}*/
	
	@PostMapping(value = "/ventas/save")
	public String saveVenta(@Valid @ModelAttribute("clienteId") Cliente cliente, @ModelAttribute("tipoVta") String tipoVenta, 
							@ModelAttribute("id_cantidadCuotas") String cantidadCuotas,
							BindingResult result, ModelMap model) {
		
		LOGGER.info("POST - saveVenta - /ventas/save");
		LOGGER.info("cliente: " + cliente.toString());
		
		Venta venta = null;
		String pageReturn = "";
		
		if (result.hasErrors()) {
			model.addAttribute("listClientes", cliente);
			model.addAttribute("tipoVta", ventaService.getTipoVentas());
			model.addAttribute("id_cantidadCuotas", cantidadCuotas);
			
			//Verifico si es en la edición o nueva venta
			pageReturn = cliente.getId() != null ? "ventas/edit_ventas" : "ventas/new_ventas";

			return pageReturn;			
		}
		else {
			switch (tipoVenta.toUpperCase()) {
			case "EFECTIVO":
				VentaEfectivo ventaEfectivo = new VentaEfectivo();
				ventaEfectivo.setCliente(cliente);
				//ventaEfectivo.setFecha(today());
				try {
					venta = ventaService.save(ventaEfectivo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case "TARJETA":
				VentaTarjeta ventaTarjeta = new VentaTarjeta();
				ventaTarjeta.setCliente(cliente);
				ventaTarjeta.setCantidadCuotas(Integer.parseInt(cantidadCuotas));
				//ventaTarjeta.setFecha(fecha);
				ventaTarjeta.setCoeficienteTarjeta(new BigDecimal(0.5));
				try {
					venta = ventaService.save(ventaTarjeta);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			default:
				break;
			}			
		}
		
		//return "redirect:/tienda/ventas/list";
		return "redirect:/tienda/ventas/edit/" + venta.getId();
	}
	
	@RequestMapping(value = "/ventas/edit/{id}", method = RequestMethod.GET)
	public ModelAndView showEditVentaPage(@PathVariable(name = "id") Long ventaId) {
		LOGGER.info("GET - showEditVentaPage - /ventas/edit/{id}");
		LOGGER.info("venta: " + ventaId);
		
		ModelAndView mav = new ModelAndView("ventas/edit_ventas");
		List<Cliente> clientes = clienteService.listAll();		
		List<Item> items = itemService.listAll();
		List<Item> itemsAgregar = new ArrayList<Item>();
		
		for(int i = 1; i < items.size(); i++) {
			if(items.get(i).getVenta().getId() == ventaId) {
				itemsAgregar.add(items.get(i));
			}
		}
		
		mav.addObject("clientes", clientes);
		mav.addObject("listItems", itemsAgregar);
		
		String tipoVenta = "Efectivo";		
		Optional<Venta> ventaOptional = ventaService.findById(ventaId);		
		Venta venta = null;
		
		if (ventaOptional.isPresent()) {
			venta  = ventaOptional.get();
			mav.addObject("venta", venta);
			
			Cliente clienteOptional = venta.getCliente();
			mav.addObject("clienteActual", clienteOptional);
			
			if(venta.getClass() == VentaTarjeta.class) {
				tipoVenta = "Tarjeta";
			}
		}
		
		mav.addObject("tipoVenta", tipoVenta);
		return mav;
	}
	
	@RequestMapping(value = "/items/edit/{id}", method = RequestMethod.GET)
	public ModelAndView showEditItemPage(@PathVariable(name = "id") Long itemId) {
		LOGGER.info("GET - showEditItemPage - /items/edit/{id}");
		LOGGER.info("item: " + itemId);
		
		ModelAndView mav = new ModelAndView("items/edit_items");
		
		Optional<Item> itemOptional = itemService.findById(itemId);
		List<Prenda> prendas = prendaService.listAll();
		Item item = null;
		
		if (itemOptional.isPresent()) {
			item  = itemOptional.get();
			mav.addObject("item", item);
			mav.addObject("itemId", item.getId());
			mav.addObject("ventaId", item.getVenta().getId());
			mav.addObject("prendas", prendas);
			mav.addObject("prendaActual", item.getPrenda());
			
			Venta venta = item.getVenta();
			
			String tipoVenta = venta.getClass() == VentaTarjeta.class ? "Tarjeta" : "Efectivo";	
			
			mav.addObject("tipoVenta", tipoVenta);
		}
		
		return mav;
	}

	@RequestMapping(value = "/ventas/delete/{id}", method = RequestMethod.GET)
	public String deleteVenta(@PathVariable(name = "id") Long ventaId) {
		LOGGER.info("GET - deleteVenta - /ventas/delete/{id}");
		LOGGER.info("venta: " + ventaId);
		ventaService.delete(ventaId);
		return "redirect:/tienda/ventas/list";
	}
	
	@RequestMapping(value = "/ventas/deleteItem/{id}", method = RequestMethod.GET)
	public String deleteItemVenta(@PathVariable(name = "id") Long itemId) {
		LOGGER.info("GET - deleteItemVenta - /ventas/deleteItem/{id}");
		LOGGER.info("item: " + itemId);
		
		Item item = null;
		String ventaId = "";
		Optional<Item> itemOpcional = itemService.findById(itemId);
		if (itemOpcional.isPresent()) {
			item = itemOpcional.get();
			ventaId = String.valueOf(item.getVenta().getId());
		}
		
		itemService.delete(itemId);
		
		return "redirect:/tienda/ventas/edit/" + ventaId;//"redirect:/tienda/ventas/list";
	}
}