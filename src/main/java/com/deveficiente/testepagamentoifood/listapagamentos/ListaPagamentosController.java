package com.deveficiente.testepagamentoifood.listapagamentos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.deveficiente.testepagamentoifood.Restaurante;
import com.deveficiente.testepagamentoifood.Usuario;

@RestController
public class ListaPagamentosController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private List<PossivelRestricaoPagamento> possiveisRestricoes;

	@GetMapping(value = "/pagamentos-disponiveis/{restauranteId}")
	@Cacheable(cacheNames = "pagamentos-possiveis-usuario")
	public Collection<DetalhePagamentoListaDTO> execute(String tokenUsuario,
			@PathVariable("restauranteId") Long restauranteId) {
		Usuario usuarioLogado = usuarioRepository.findByNome(tokenUsuario);

		Restaurante restaurante = manager.find(Restaurante.class,
				restauranteId);

		return usuarioLogado.pagamentosPossiveisParaRestaurante(restaurante,possiveisRestricoes)
				.stream().map(DetalhePagamentoListaDTO::new)
				.collect(Collectors.toList());
	}

}
