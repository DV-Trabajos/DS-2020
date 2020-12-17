package ar.edu.davinci.dvds20202cg2.controller.view;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.davinci.dvds20202cg2.controller.TiendaApp;
import ar.edu.davinci.dvds20202cg2.controller.view.ClienteController;
import ar.edu.davinci.dvds20202cg2.service.ClienteService;
import ar.edu.davinci.dvds20202cg2.model.Cliente;

@Controller
public class ClienteController extends TiendaApp {
	private final Logger LOGGER = LoggerFactory.getLogger(ClienteController.class);
	
	@Autowired
	private ClienteService clienteService;

	@GetMapping(path = "clientes/list")
	public String showClientePage(Model model) {
		LOGGER.info("GET - showClientePage  - /clientes/list");
		
		Pageable pageable = PageRequest.of(0, 20);
		Page<Cliente> clientes = clienteService.list(pageable);
		model.addAttribute("listClientes", clientes);

		LOGGER.info("clientes.size: " + clientes.getNumberOfElements());
		return "clientes/list_clientes";
	}
	
	@GetMapping(path = "/clientes/new")
	public String showNewClientePage(Model model) {
		LOGGER.info("GET - showNewClientePage - /clientes/new");
		Cliente cliente = new Cliente();
		model.addAttribute("cliente", cliente);

		LOGGER.info("clientes: " + cliente.toString());

		return "clientes/new_clientes";
	}
	
	@PostMapping(value = "/clientes/save")
	public String saveCliente(@ModelAttribute("cliente") Cliente cliente) {
		LOGGER.info("POST - saveCliente - /clientes/save");
		LOGGER.info("cliente: " + cliente.toString());
		clienteService.save(cliente);

		return "redirect:/tienda/clientes/list";
	}
	
	@RequestMapping(value = "/clientes/edit/{id}", method = RequestMethod.GET)
	public ModelAndView showEditClientePage(@PathVariable(name = "id") Long clienteId) {
		LOGGER.info("GET - showEditClientePage - /clientes/edit/{id}");
		LOGGER.info("cliente: " + clienteId);

		ModelAndView mav = new ModelAndView("clientes/edit_clientes");
		Optional<Cliente> clienteOptional = clienteService.findById(clienteId);
		Cliente cliente = null;
		if (clienteOptional.isPresent()) {
			cliente  = clienteOptional.get();
			mav.addObject("cliente", cliente);
		}
		return mav;
	}

	@RequestMapping(value = "/clientes/delete/{id}", method = RequestMethod.GET)
	public String deleteCliente(@PathVariable(name = "id") Long clienteId) {
		LOGGER.info("GET - deleteCliente - /clientes/delete/{id}");
		LOGGER.info("cliente: " + clienteId);
		clienteService.delete(clienteId);
		return "redirect:/tienda/clientes/list";
	}
}