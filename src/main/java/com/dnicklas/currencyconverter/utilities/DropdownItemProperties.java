package com.dnicklas.currencyconverter.utilities;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.SortedMap;

@Configuration
@PropertySource("classpath:dropdownValues.properties")
@ConfigurationProperties(prefix = "dropdown-value")
public class DropdownItemProperties {
    private SortedMap currencies;

    public SortedMap getCurrencies() {
        return currencies;
    }

    public void setCurrencies(SortedMap currencies) {
        this.currencies = currencies;
    }
}
