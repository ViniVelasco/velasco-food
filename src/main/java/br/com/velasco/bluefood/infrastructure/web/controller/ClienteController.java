package br.com.velasco.bluefood.infrastructure.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.velasco.bluefood.domain.cliente.Cliente;
import br.com.velasco.bluefood.domain.cliente.ClienteRepository;
import br.com.velasco.bluefood.domain.pedido.Pedido;
import br.com.velasco.bluefood.domain.pedido.PedidoRepository;
import br.com.velasco.bluefood.domain.restaurante.CategoriaRestaurante;
import br.com.velasco.bluefood.domain.restaurante.CategoriaRestauranteRepository;
import br.com.velasco.bluefood.domain.restaurante.ItemCardapio;
import br.com.velasco.bluefood.domain.restaurante.ItemCardapioRepository;
import br.com.velasco.bluefood.domain.restaurante.Restaurante;
import br.com.velasco.bluefood.domain.restaurante.RestauranteRepository;
import br.com.velasco.bluefood.domain.restaurante.SearchFilter;
import br.com.velasco.bluefood.service.ClienteService;
import br.com.velasco.bluefood.service.RestauranteService;
import br.com.velasco.bluefood.service.ValidationException;
import br.com.velasco.bluefood.util.SecurityUtils;

@Controller
@RequestMapping(path = "/cliente")
public class ClienteController {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private CategoriaRestauranteRepository categoriaRestauranteRepository;
	
	@Autowired
	private ItemCardapioRepository itemCardapioRepository;
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private RestauranteService restauranteService;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@GetMapping(path = "/home")
	public String home(Model model) {
		List<CategoriaRestaurante> categorias = categoriaRestauranteRepository.findAll(Sort.by("nome"));
		model.addAttribute("categorias", categorias);
		model.addAttribute("searchFilter", new SearchFilter());
		
		List<Pedido> pedidos = pedidoRepository.listaPedidosByCliente(SecurityUtils.loggedCliente().getId());
		model.addAttribute("pedidos", pedidos);
		
		return "cliente-home";
	}
	
	
	@GetMapping(path = "/edit")
	public String edit(Model model) {
		Integer clienteId = SecurityUtils.loggedCliente().getId();
		Cliente cliente = clienteRepository.findById(clienteId).orElseThrow();
		model.addAttribute("cliente", cliente);
		ControllerHelper.setEditMode(model, true);
		model.addAttribute("link", "/cliente/home");
		return "cliente-cadastro";
	}
	
	@PostMapping("/save")
	public String save(@ModelAttribute("cliente") @Valid Cliente cliente, Errors errors, Model model) {
		if(!errors.hasErrors()) {
			try {
				clienteService.saveCliente(cliente);
				model.addAttribute("msg", "Cliente gravado com sucesso");
			} catch (ValidationException e) {
				errors.rejectValue("email", null, e.getMessage());
			}
		}
		
		ControllerHelper.setEditMode(model, true);
		return "cliente-cadastro";
	}
	
	@GetMapping(path = "/search")
	public String search(@ModelAttribute("searchFilter") SearchFilter filter, Model model, @RequestParam(value="cmd", required=false) String command) {
		
		filter.processFilter(command);
		
		List<Restaurante> restaurantes = restauranteService.search(filter);
		model.addAttribute("restaurantes", restaurantes);
		
		ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);
		
		model.addAttribute("searchFilter", filter);
		
		model.addAttribute("cep", SecurityUtils.loggedCliente().getCep());
		
		return "cliente-busca";
	}
	
	@GetMapping(path = "/restaurante")
	public String viewRestaurante(
				@RequestParam("restauranteId") Integer restauranteId,
				@RequestParam(value = "categoria", required = false) String categoria,
				Model model) {
		
		Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow();
		model.addAttribute("restaurante", restaurante);
		model.addAttribute("cep", SecurityUtils.loggedCliente().getCep());
		
		List<String> categorias = itemCardapioRepository.findCategorias(restauranteId);
		model.addAttribute("categorias", categorias);
		
		List<ItemCardapio> itensCardapioDestaque;
		List<ItemCardapio> itensCardapioNaoDestaque;
		
		if(categoria == null) {
			itensCardapioDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueOrderByNome(restauranteId, true);
			itensCardapioNaoDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueOrderByNome(restauranteId, false);
		} else {
			itensCardapioDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueAndCategoriaOrderByNome(restauranteId, true, categoria);
			itensCardapioNaoDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueAndCategoriaOrderByNome(restauranteId, false, categoria);
		}
		
		model.addAttribute("itensCardapioDestaque", itensCardapioDestaque);
		model.addAttribute("itensCardapioNaoDestaque", itensCardapioNaoDestaque);
		model.addAttribute("categoriaSelecionada", categoria);
		
		return "cliente-restaurante";
	}
	

}
