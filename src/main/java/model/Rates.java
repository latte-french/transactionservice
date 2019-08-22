package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum Rates {

    USDRUB("USDRUB",66.479),
    GBPRUB("GBPRUB",80.7665),
    EURRUB("EURRUB",73.7351),
    RUBUSD("RUBUSD",66.479),
    RUBGBP("RUBGBP",80.7665),
    RUBEUR("RUBEUR",73.7351),
    USDGBP("RUBGBP",80.7665),
    USDEUR("RUBEUR",73.7351),
    GBPUSD("RUBGBP",80.7665),
    EURUSD("RUBEUR",73.7351),
    GBPEUR("RUBGBP",80.7665),
    EURGBP("RUBGBP",80.7665);


    @Getter
    @Setter
    private final String name;
    @Getter
    @Setter
    private final double rate;

    public static Double valueOfRate(String name) {
        for (Rates r : values()) {
            if (r.name.equals(name)) {
                return r.getRate();
            }
        }
        return null;
    }
}
