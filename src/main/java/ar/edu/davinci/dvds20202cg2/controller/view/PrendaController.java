package ar.edu.davinci.dvds20202cg2.controller.view;

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
import org.springframework.web.servlet.ModelAndView;

import ar.edu.davinci.dvds20202cg2.controller.TiendaApp;
import ar.edu.davinci.dvds20202cg2.controller.view.PrendaController;
import ar.edu.davinci.dvds20202cg2.service.PrendaService;
import ar.edu.davinci.dvds20202cg2.model.Prenda;
import ar.edu.davinci.dvds20202cg2.model.TipoPrenda;

@Controller
public class PrendaController extends TiendaApp {
	private final Logger LOGGER = LoggerFactory.getLogger(PrendaController.class);

	@Autowired
	private PrendaService prendaService;
	
	@GetMapping(path = "prendas/list")
	public String showPrendaPage(Model model) {
		LOGGER.info("GET - showPrendaPage  - /prendas/list");
		
		Pageable pageable = PageRequest.of(0, 20);
		Page<Prenda> prendas = prendaService.list(pageable);
		model.addAttribute("listPrendas", prendas);

		LOGGER.info("prendas.size: " + prendas.getNumberOfElements());
		return "prendas/list_prendas";
	}
	
	@GetMapping(path = "/prendas/new")
	public String showNewPrendaPage(Model model) {
		LOGGER.info("GET - showNewPrendaPage - /prendas/new");
		Prenda prenda = new Prenda();
		model.addAttribute("prenda", prenda);
		model.addAttribute("tipoPrendas", prendaService.getTipoPrendas());

		LOGGER.info("prendas: " + prenda.toString());

		return "prendas/new_prendas";
	}
	
	@PostMapping(value = "/prendas/save")
	public String savePrenda(@Valid @ModelAttribute("prenda") Prenda prenda, BindingResult result, ModelMap model) {
		LOGGER.info("POST - savePrenda - /prendas/save");
		LOGGER.info("prenda: " + prenda.toString());
		
		String pageReturn = "";
		
		if (result.hasErrors()) {			
			model.addAttribute("prenda", prenda);
			model.addAttribute("tipoPrendaActual", prenda.getTipo());
			model.addAttribute("tipoPrendas", prendaService.getTipoPrendas());
			
			//Verifico si es en la edición o nueva prenda
			pageReturn = prenda.getId() != null ? "prendas/edit_prendas" : "prendas/new_prendas";

			return pageReturn;
		} 
		else {
			prendaService.save(prenda);
		}

		return "redirect:/tienda/prendas/list";
	}
	
	@RequestMapping(value = "/prendas/edit/{id}", method = RequestMethod.GET)
	public ModelAndView showEditPrendaPage(@PathVariable(name = "id") Long prendaId) {
		LOGGER.info("GET - showEditPrendaPage - /prendas/edit/{id}");
		LOGGER.info("prenda: " + prendaId);

		ModelAndView mav = new ModelAndView("prendas/edit_prendas");
		Optional<Prenda> prendaOptional = prendaService.findById(prendaId);
		Prenda prenda = null;		
		
		if (prendaOptional.isPresent()) {
			prenda  = prendaOptional.get();
			mav.addObject("prenda", prenda);
			mav.addObject("tipoPrendaActual", prenda.getTipo());
		}
		mav.addObject("tipoPrendas", prendaService.getTipoPrendas());
		return mav;
	}

	@RequestMapping(value = "/prendas/delete/{id}", method = RequestMethod.GET)
	public String deletePrenda(@PathVariable(name = "id") Long prendaId) {
		LOGGER.info("GET - deletePrenda - /prendas/delete/{id}");
		LOGGER.info("prenda: " + prendaId);
		prendaService.delete(prendaId);
		return "redirect:/tienda/prendas/list";
	}
}