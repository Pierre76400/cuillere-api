package fr.softeam.cuillereapi.model;


import jakarta.persistence.*;

@Entity
public class Avis {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;

	private String commentaire;

	private String auteur;

	private Long note;

	/**
	 @deprecated Ce champ ne sert plus
	 */
	@Deprecated
	private String lieu;

	@ManyToOne
	private Restaurant restaurant;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuteur() {
		return auteur;
	}

	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public Long getNote() {
		return note;
	}

	public void setNote(Long note) {
		this.note = note;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public String getLieu() {
		return lieu;
	}

	public void setLieu(String lieu) {
		this.lieu = lieu;
	}
}
