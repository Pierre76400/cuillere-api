package fr.softeam.cuillereapi.prodist;

import org.junit.jupiter.api.Test;

//FIXME uniquement pour avoir un pense bête pour calculer la distance entre 2 coordonnées
public class Coordinates {
    private double latitude;
    private double longitude;

    // Constructeurs, getters et setters

    public static double calculateDistance(Coordinates coord1, Coordinates coord2) {
        double earthRadiusKm = 6371; // Rayon de la Terre en kilomètres

        double dLat = Math.toRadians(coord2.getLatitude() - coord1.getLatitude());
        double dLon = Math.toRadians(coord2.getLongitude() - coord1.getLongitude());

        double lat1 = Math.toRadians(coord1.getLatitude());
        double lat2 = Math.toRadians(coord2.getLatitude());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadiusKm * c;
    }

    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
