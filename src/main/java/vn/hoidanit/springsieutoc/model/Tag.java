package vn.hoidanit.springsieutoc.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tags")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // object generic<> id == null

	@NotBlank(message = "name không được để trống")
	private String name;

	@JsonIgnoreProperties(value = { "tags", "user", "comments" })
	@ManyToMany(mappedBy = "tags")
	private List<Post> posts;

	public Tag(Long id) {
		this.id = id;
	}

}
