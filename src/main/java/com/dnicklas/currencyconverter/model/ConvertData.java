package com.dnicklas.currencyconverter.model;

public class ConvertData {
    private String baseCurrency;

    private String convertToCurrency;

    private String amount;

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getConvertToCurrency() {
        return convertToCurrency;
    }

    public void setConvertToCurrency(String convertToCurrency) {
        this.convertToCurrency = convertToCurrency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
