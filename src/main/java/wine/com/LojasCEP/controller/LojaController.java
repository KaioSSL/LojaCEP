package wine.com.LojasCEP.controller;

import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import wine.com.LojasCEP.modelo.Loja;
import wine.com.LojasCEP.repository.LojaRepository;

@SuppressWarnings("rawtypes")
@RestController
//Controller, responsável por receber as requisições HTTP e realizar as operações no objeto.
public class LojaController {
	

	//Injeção de depêndencia para o objeto do tipo LojaRepository
	@Autowired
	private LojaRepository repository;
	
	//Método que recebe uma requisição GET, e retorna uma String.
	@GetMapping(path = "/api/loja/olamundo")
	public String olaMundo() {
		return "Olá Mundo !";
	}
	
	//Método responsável por receber uma requisição Get, verifica a loja mais proxima. 
	//Contando com a integridade já realizada no momento de inserção, é garantido que somente uma loja conterá o cep inserido em seu range.
	@GetMapping(path = "api/loja/lojaMaisProxima/{cep}")
	public ResponseEntity lojaNoRange(@PathVariable("cep") Double cep) {
		//Função responsável por verificar se o cep se encontra dentro do range da faixa.
		Predicate<Loja> dentroDoRange = loja -> {
			if(cep >= loja.getFaixa_inicio() && cep<=loja.getFaixa_fim()) {
				return true;
			}
			return false;
		};
		//Stream de todas as faixas de CEP
		Stream<Loja> lojasStream = StreamSupport.stream(repository.findAll().spliterator(), false);
		//Realiza um filtro para verificar as faixas que contém o cep no range
		return lojasStream.filter(dentroDoRange)
				//pega a primeira faixa.
				.findFirst()
				//caso Resposta HTTP ok, faz o build da resposta com o objeto.
				.map(loja-> ResponseEntity.ok().body(loja))
				//Caso não, faz o build da Resposta notFound.
				.orElse(ResponseEntity.notFound().build());
	}
	//CRUD
	//Método responsável por receber uma requisição POST, e um body de um Objeto, retornando esse objeto construído.
	@PostMapping(path = "/api/loja")
	public Loja create(@RequestBody Loja loja){
		//Verificação de integridade do intervalo de cep.
		if(loja.getFaixa_fim()<loja.getFaixa_inicio()) return null;
		//Método responsável por verificar a faixa de cep á ser inserida não está em conflito com alguma ja existente.
		Predicate<Loja> foraDoRange = (l->{
			if((loja.getFaixa_inicio()>l.getFaixa_inicio() && loja.getFaixa_inicio()>l.getFaixa_fim()) ||
					(loja.getFaixa_inicio()<l.getFaixa_inicio() && loja.getFaixa_fim()<l.getFaixa_inicio())) {
				return true;
			}
			return false;
		});
		//Retorna uma stream de todas faixas de CEP ja existentes.
		Stream<Loja> lojasStream = StreamSupport.stream(repository.findAll().spliterator(), false);
		//Aplica para todas lojas a comparação do método 'foraRange', se todas retornarem verdadeiro, não conflita com nenhuma faixa de cep
		if(lojasStream.allMatch(foraDoRange)) {	
			//Realiza o cadastro do objeto.
			return repository.save(loja);
		}
		return null;
	}	
	//Método responsável por receber uma requisição GET e um ID de um objeto, e retornar a resposta HTTP contruída.
	@GetMapping(path = "api/loja/{id}")
	public ResponseEntity consultarLoja(@PathVariable("id")Integer id) {
		return repository.findById(id)
				.map(loja -> ResponseEntity.ok().body(loja))
				.orElse(ResponseEntity.notFound().build());
	}
	//Método responsável por receber uma REquisição Put, um id e um body. Retorna uma resposta HTTP.
	@PutMapping(path = "api/loja/{id}")
	public ResponseEntity updateLoja(@PathVariable("id") Integer id, @RequestBody Loja loja) {
		//Verifica integridade do update na faixa.
		if(loja.getFaixa_fim()<loja.getFaixa_inicio()) return ResponseEntity.notFound().build();
		//Verifica se realmente existe alguma faixa com o ID inserido.
		return repository.findById(id)
				//caso exist Verifica conflito entre ranges.
				.map(lojaA->{
					Predicate<Loja> foraDoRange = (l->{
						if((loja.getFaixa_inicio()>l.getFaixa_inicio() && loja.getFaixa_inicio()>l.getFaixa_fim()) ||
								(loja.getFaixa_inicio()<l.getFaixa_inicio() && loja.getFaixa_fim()<l.getFaixa_inicio())) {
							return true;
						}
						return false;
					});
					Stream<Loja> lojasStream = StreamSupport.stream(repository.findAll().spliterator(), false);
					if(lojasStream.allMatch(foraDoRange)) {
						//Caso não conflite com nenhum faixa. Faz o update do objeto.
						lojaA.setCodigo_loja(loja.getCodigo_loja());
						lojaA.setFaixa_inicio(loja.getFaixa_inicio());
						lojaA.setFaixa_fim(loja.getFaixa_fim());
						Loja update = repository.save(lojaA);
						//Retorna a resposta construída com o objeto alterado.
						return ResponseEntity.ok().body(update);					
					}
					return null;
				})
				//Caso não encontre, builda uma resposta notFound.
				.orElse(ResponseEntity.notFound().build());
	} 
	//Método responsável por receber uma requisição DELETE e um ID, retorna uma resposta HTTP.
	@DeleteMapping(path = "api/loja/{id}")
	public ResponseEntity deleteLoja(@PathVariable("id") Integer id) {
		//Verifica se existe uma faixa com o ID Inserido.
		return repository.findById(id)
				//Caso exista, realiza o Delete do objeto com o id e retorna resposta HTTP construída com o objeto.
				.map(loja ->{
					repository.deleteById(id);
					return ResponseEntity.ok().build();
				//Caso contrário, retorna resposta HTTP Not Found buildada;
				}).orElse(ResponseEntity.notFound().build());
	}	
}
