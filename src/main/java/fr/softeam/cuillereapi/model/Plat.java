package fr.softeam.cuillereapi.model;


import jakarta.persistence.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

//FIXME regarder le pb de mapping
//Faut il du monodirectionnel ou du bidirectionnel ?
@Entity
public class Plat {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;

	private String libelle;

	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@ManyToOne(fetch = FetchType.LAZY)
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

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
}
