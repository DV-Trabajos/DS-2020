package ar.edu.davinci.dvds20202cg2.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ar.edu.davinci.dvds20202cg2.model.Prenda;
import ar.edu.davinci.dvds20202cg2.model.TipoPrenda;
import ar.edu.davinci.dvds20202cg2.repository.PrendaRepository;

@Service
public class PrendaServiceImpl implements PrendaService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(PrendaServiceImpl.class);

	private final PrendaRepository prendaRepository;
	
	@Autowired
	public PrendaServiceImpl(final PrendaRepository prendaRepository) {
		this.prendaRepository = prendaRepository;
	}

	@Override
	public List<Prenda> listAll() {
		return prendaRepository.findAll();
	}

	@Override
	public Page<Prenda> list(Pageable pageable) {
		LOGGER.info("Pagegable: offset: " + pageable.getOffset() + " - pageSize:" + pageable.getPageSize());
		return prendaRepository.findAll(pageable);
	}

	@Override
	public Prenda findById(Long id) {
		Optional<Prenda> prendaOptional = prendaRepository.findById(id);
		if (prendaOptional.isPresent()) {
			return prendaOptional.get();
		}
		return null;
	}

	@Override
	public Prenda save(Prenda prenda) {
		return prendaRepository.save(prenda);
	}

	@Override
	public void delete(Prenda prenda) {
		prendaRepository.delete(prenda);
	}

	@Override
	public void delete(Long id) {
		prendaRepository.deleteById(id);
	}
	
	@Override
	public long count() {
		return prendaRepository.count();
	}

	@Override
	public List<TipoPrenda> getTipoPrendas() {
		return TipoPrenda.getTipoPrendas();
	}
}