package wine.com.LojasCEP.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "loja_cep")
public class Loja {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 45, nullable = false)
	private String codigo_loja;
	@Column(nullable = false)
	private Double faixa_inicio;
	@Column(nullable = false)
	private Double faixa_fim;
	
	
	public Integer getId() {
		return id;
	}
	
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public String getCodigo_loja() {
		return codigo_loja;
	}
	
	
	public void setCodigo_loja(String codigo_loja) {
		this.codigo_loja = codigo_loja;
	}
	
	
	public Double getFaixa_inicio() {
		return faixa_inicio;
	}
	
	
	public void setFaixa_inicio(Double faixa_inicio) {
		this.faixa_inicio = faixa_inicio;
	}
	
	
	public Double getFaixa_fim() {
		return faixa_fim;
	}
	
	
	public void setFaixa_fim(Double faixa_fim) {
		this.faixa_fim = faixa_fim;
	}
	
	@Override
	public String toString() {
		return String.format("Loja: %s, Faixa CEP Inicio : %f, Faixa CEP fim: %f",this.codigo_loja,this.faixa_inicio,this.faixa_fim);
	}


}
