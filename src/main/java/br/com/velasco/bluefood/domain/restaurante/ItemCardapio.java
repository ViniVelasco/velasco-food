package br.com.velasco.bluefood.domain.restaurante;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import br.com.velasco.bluefood.infrastructure.web.validator.UploadConstraint;
import br.com.velasco.bluefood.util.FileType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "item_cardapio")
public class ItemCardapio implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message = "O nome n�o pode ser vazio")
	@Size(max = 50)
	private String nome;
	
	@NotBlank(message = "A categoria n�o pode ser vazia")
	@Size(max = 25)
	private String categoria;
	
	@NotBlank(message = "A descri��o n�o pode ser vazia")
	@Size(max = 80)
	private String descricao;
	
	@Size(max = 50)
	private String imagem;
	
	@NotNull(message = "O pre�o n�o pode ser vazio")
	@Min(0)
	private BigDecimal preco;
	
	@NotNull
	private boolean destaque;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "restaurante_id")
	private Restaurante restaurante;
	
	@UploadConstraint(acceptedTypes = FileType.PNG, message = "O arquivo n�o � um arquivo de imagem v�lido")
	private transient MultipartFile imagemFile;
	
	public void setImageFileName() {
		if(getId() == null) {
			throw new IllegalStateException("O objeto precisa primeiro ser criado");
		}
		
		this.imagem = String.format("%04d-comida.%s", getId(), FileType.of(imagemFile.getContentType()).getExtension());
	}

}
