package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum Rates {

    USDRUB("USDRUB",66.0465),
    GBPRUB("GBPRUB",81.0579),
    EURRUB("EURRUB",73.5824),
    RUBUSD("RUBUSD",0.0151),
    RUBGBP("RUBGBP",0.0123),
    RUBEUR("RUBEUR",0.0136),
    USDGBP("USDGBP",0.8146),
    USDEUR("USDEUR",0.8975),
    GBPUSD("GBPUSD",1.2277),
    EURUSD("EURUSD",1.1142),
    GBPEUR("GBPEUR",1.1017),
    EURGBP("EURGBP",0.9078);


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
