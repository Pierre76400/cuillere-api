package fr.softeam.cuillereapi.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Restaurant {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;

	private String nom;

	private String adresse;

	private String vegetarien;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getVegetarien() {
		return vegetarien;
	}

	public void setVegetarien(String vegetarien) {
		this.vegetarien = vegetarien;
	}

}
