package utils;

import model.Rates;

public class CurrencyConverter {

    private static CurrencyConverter instance;

    public static CurrencyConverter getInstance() {
        if (instance == null) {
            instance = new CurrencyConverter();
        }
        return instance;
    }

    public Double currencyConverter(Double sumToConvert, String currencyFrom, String currencyTo){
        if(currencyFrom.equals(currencyTo)) {return sumToConvert;}
        String currencies = currencyFrom + currencyTo;
        return(sumToConvert * Rates.valueOfRate(currencies));
    }
}


