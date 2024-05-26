package edu.pidev.backend.common.enumuration;

public enum TunisianCity {
    TUNIS("Tunis"),
    SFAX("Sfax"),
    SOUSSE("Sousse"),
    KAIROUAN("Kairouan"),
    BIZERTE("Bizerte"),
    GABES("Gabès"),
    ARIANA("Ariana"),
    GAFSA("Gafsa"),
    DJERBA("Djerba"),
    EL_KEF("El Kef"),
    MONASTIR("Monastir"),
    NABEUL("Nabeul"),
    SIDI_BOUZID("Sidi Bouzid"),
    TATAOUINE("Tataouine"),
    TOZEUR("Tozeur"),
    ZAGHOUAN("Zaghouan"),
    JENDOUBA("Jendouba"),
    MEDENINE("Medenine"),
    BEJA("Béja"),
    KEBILI("Kebili"),
    LE_KEF("Le Kef"),
    MAHDIA("Mahdia"),
    MANOUBA("Manouba"),
    SILIANA("Siliana");

    private String cityName;

    TunisianCity(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }
}

