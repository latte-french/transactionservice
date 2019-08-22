package utils;

import model.Rates;

public class CurrencyConverter {


    public static Double currencyConverter(Double sumToConvert, String currencyFrom, String currencyTo){
        if(currencyFrom.equals(currencyTo)) {return sumToConvert;}
        String currencies = currencyFrom + currencyTo;
        return(sumToConvert * Rates.valueOfRate(currencies));
    }
}


