package fr.softeam.cuillereapi.api;

import fr.softeam.cuillereapi.model.CategoriePlat;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class PlatDto {
	private String libelle;

	private String categoriePlat;

	private String libelleCategoriePlat;

	private Double prix;

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getCategoriePlat() {
		return categoriePlat;
	}

	public void setCategoriePlat(String categoriePlat) {
		this.categoriePlat = categoriePlat;
	}

	public Double getPrix() {
		return prix;
	}

	public void setPrix(Double prix) {
		this.prix = prix;
	}

	public String getLibelleCategoriePlat() {
		return libelleCategoriePlat;
	}

	public void setLibelleCategoriePlat(String libelleCategoriePlat) {
		this.libelleCategoriePlat = libelleCategoriePlat;
	}
}
