package fr.softeam.cuillereapi.model;


import jakarta.persistence.*;

@Entity
public class CategoriePlat {
	@Id
	private String code;

	private String libelle;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
}
