package fr.softeam.cuillereapi.model;


import jakarta.persistence.*;

//FIXME regarder le pb de mapping
//Faut il du monodirectionnel ou du bidirectionnel ?
@Entity
public class Plat {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;

	private String libelle;

	@ManyToOne
	@JoinColumn(name = "categorie_plat_code")
	private CategoriePlat categoriePlat;

	private Double prix;

	@ManyToOne
	private Restaurant restaurant;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public CategoriePlat getCategoriePlat() {
		return categoriePlat;
	}

	public void setCategoriePlat(CategoriePlat categoriePlat) {
		this.categoriePlat = categoriePlat;
	}

	public Double getPrix() {
		return prix;
	}

	public void setPrix(Double prix) {
		this.prix = prix;
	}

}
