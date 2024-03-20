package com.dnicklas.currencyconverter.controller;

import com.dnicklas.currencyconverter.api.Access;
import com.dnicklas.currencyconverter.model.ConvertData;
import com.dnicklas.currencyconverter.utilities.DropdownItemProperties;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Currency;

@Controller
public class CurrencyController {

    @Autowired
    private Access apiAccess;

    @Autowired
    private DropdownItemProperties dropdownItemProperties;

    @GetMapping("/")
    public String getHomePage(Model model) {
        modelHome(model);
        return "home";
    }


    @PostMapping("/convert")
    public String convertCurrency(@ModelAttribute("convertDate") ConvertData data, Model model) throws IOException, ParseException {
        modelHome(model);

        String baseCurrency = data.getBaseCurrency();
        String convertToCurrency = data.getConvertToCurrency();
        String stringAmount = data.getAmount();

        if(baseCurrency.isBlank() || convertToCurrency.isBlank() || stringAmount.isBlank()) {
            model.addAttribute("errorMessage", "All fields have to be filled out - Dropdown menus must not be set to 'N/A'");
            return "home";
        }

        String amountReadyToConvert = stringAmount.replace(',', '.');
        BigDecimal amount = null;
        try {
            amount = new BigDecimal(amountReadyToConvert);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Only numeric values are valid in the amount field");
            return "home";
        }
        BigDecimal currencyFaktor = apiAccess.getCurrencyFaktor(baseCurrency, convertToCurrency);
        BigDecimal convertedCurrency = amount.multiply(currencyFaktor);
        BigDecimal roundedConvertedCurrency = convertedCurrency.setScale(2, RoundingMode.CEILING);
        String formattedResultCurrency = NumberFormat.getNumberInstance().format(roundedConvertedCurrency);
        String formattedBeginningCurrency = NumberFormat.getNumberInstance().format(amount);

        String baseCurrencySymbol = Currency.getInstance(baseCurrency).getSymbol();
        String convertToCurrencySymbol = Currency.getInstance(convertToCurrency).getSymbol();


        String convertMessage = String.format("%s %s = %s %s", formattedBeginningCurrency, baseCurrency, formattedResultCurrency, convertToCurrency);
        String convertMessageSymbol = String.format("%s%s = %s%s", formattedBeginningCurrency, baseCurrencySymbol, formattedResultCurrency, convertToCurrencySymbol);
        model.addAttribute("convertMessage", convertMessage);
        model.addAttribute("convertMessageSymbol", convertMessageSymbol);
        return "home";
    }

    private void modelHome(Model model) {
        model.addAttribute("convertData", new ConvertData());
        model.addAttribute("currencyOptions", dropdownItemProperties.getCurrencies());
    }

}
