package com.company;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.Random;

import java.io.FileWriter;

public class TaxNumberGenerator {

    @NotNull
    public static String concatenateDigits(int... digits) {
        StringBuilder resultDigit = new StringBuilder(digits.length);
        for (int digit : digits) {
            resultDigit.append(digit);
        }
        return resultDigit.toString();
    }


    // NOTE: numbers of regions are ordered from 1 to 79, others (UNORDERED_REGIONS_NUMBERS) are not
    @NotNull
    public static ArrayList getRegionNumbers() {

        ArrayList regionNumbersList = new ArrayList();
        int lastRegionOrderedNumber = 79;
        for (int i = 1; i < lastRegionOrderedNumber; i++) {
            regionNumbersList.add(i);
        }
        Integer[] UNORDERED_REGIONS_NUMBERS = {83, 86, 87, 89, 99};

        for (int i = 0; i < UNORDERED_REGIONS_NUMBERS.length; ++i) {
            regionNumbersList.add(UNORDERED_REGIONS_NUMBERS[i]);
        }
        return regionNumbersList;
    }

    public static String getRandomRegionNumber(ArrayList regions) {
        int position = new Random().nextInt(regions.size());
        Integer regionNumberValue = (Integer)regions.get(position);
        String regionNumber = regionNumberValue.toString();
        if (regionNumber.length() == 1) {
            regionNumber = "0" + regionNumber;
        }
        return regionNumber;
    }

    @NotNull
    // NOTE: Tax inspection number consists of two digits in range of [1 (minValue), 99 (maxValue)]
    // ... the method generates digits separately to avoid one-digit value
    public static String getTaxInspectionNumber() {
        int minValue = 1;
        int maxValue = 9;
        int firstDigit = ThreadLocalRandom.current().nextInt(minValue, maxValue + 1);
        int secondDigit = ThreadLocalRandom.current().nextInt(minValue, maxValue + 1);
        String taxInspectionNumber = concatenateDigits(firstDigit, secondDigit);
        return taxInspectionNumber;
    }

    @NotNull
    //  NOTE: Tax number in Government register consists of five digits, each generates ...
    // ... separately in range of [1 (minValue), 99 (maxValue)]
    public static String getTaxNumberInRegister() {
        int minValue = 1;
        int maxValue = 9;
        int firstDigit = ThreadLocalRandom.current().nextInt(minValue, maxValue + 1);
        int secondDigit = ThreadLocalRandom.current().nextInt(minValue, maxValue + 1);
        int thirdDigit = ThreadLocalRandom.current().nextInt(minValue, maxValue + 1);
        int fourthDigit = ThreadLocalRandom.current().nextInt(minValue, maxValue + 1);
        int fifthDigit = ThreadLocalRandom.current().nextInt(minValue, maxValue + 1);
        String taxInspectionNumber = concatenateDigits(firstDigit, secondDigit, thirdDigit, fourthDigit, fifthDigit);
        return taxInspectionNumber;

    }

    @NotNull
    // NOTE: Control number for a legal entity calculates with two steps:
    // ... 1) get summary of previous individual tax number digits multiplied by specific coefficients
    // ... (listed in COEFFICIENTS)
    // ... 2) Calculate the remainder of the dividing the summary by 11 (divisionCoefficient)
    public static String getControlNumber(String incompleteIndividualTaxNumber) {
        Integer[] COEFFICIENTS = {2, 4, 10, 3, 5, 9, 4, 6, 8};
        System.out.println(incompleteIndividualTaxNumber);
        int summaryOfTaxDigits = 0;
        for (int i = 0; i < COEFFICIENTS.length; ++i) {
            int currentDigit = Character.getNumericValue(incompleteIndividualTaxNumber.charAt(i));
            summaryOfTaxDigits += currentDigit*COEFFICIENTS[i];
        }
        int divisionCoefficient = 11;
        int controlValue = summaryOfTaxDigits % divisionCoefficient;
        if (controlValue == 10) {
            controlValue = 1; // according to federal law
        }
        return Integer.toString(controlValue);
    }

    // NOTE: Individual tax number structure:
    // ...                             [tax authority number (4 digits)]
    // ...                           [tax number in register (5 digits)]
    // ...                                    [control number (1 digit)]

    @NotNull
    public static String generateIndividualTaxNumber(String taxAuthorityNumber) {
        String taxNumberInRegister = getTaxNumberInRegister();
        String incompleteIndividualTaxNumber = taxAuthorityNumber + taxNumberInRegister;
        String controlNumber = getControlNumber(incompleteIndividualTaxNumber);
        String individualTaxNumber =  incompleteIndividualTaxNumber + controlNumber;
        return individualTaxNumber;
    }

    // NOTE: Reason Code For Registration's structure is : NNNNPPXXX
    // ... NNNN - tax authority code (@param taxAuthorityCode), already generated in Individual Tax Number and passes
    // ...         as an argument
    // ... PP - code of reason for registration ( 01 to 50 for a russian company, 51 to 99 for a foreign company
    // ... XXX - ordinal number of reason for registration (@param reasonForRegistrationValue)
    @NotNull
    public static String getReasonCodeForRegistration(String taxAuthorityCode) {
        int minPPvalue = 1;
        int maxPPvalue = 50;
        int PPvalue = ThreadLocalRandom.current().nextInt(minPPvalue, maxPPvalue + 1);
        String codeForRegistration = Integer.toString(PPvalue);
        // if value is less than ten (e.g. : 1), it converts to 01
        if (codeForRegistration.length() == 1) {
            codeForRegistration = "0" + codeForRegistration;
        }

        int minXXXvalue = 1;
        int maxXXXvalue = 50;
        int reasonForRegistrationValue = ThreadLocalRandom.current().nextInt(minXXXvalue, maxXXXvalue + 1);
        String reasonForRegistration = Integer.toString(reasonForRegistrationValue);
        if (reasonForRegistration.length() == 1) {
            reasonForRegistration = "00" + reasonForRegistration;
        } else if (reasonForRegistration.length() == 2) {
            reasonForRegistration = "0" + reasonForRegistration;
        }

        String reasonCodeForRegistration = taxAuthorityCode + reasonForRegistration + codeForRegistration;
        return  reasonCodeForRegistration;
    }


    // NOTE: Value-added tax IS NOT included in the ammount (USE getValueAddedTax(...) method to get tax value,
    // ...                  or getTransactionValueWithTax(...) to get tax-included value)
    @NotNull
    public static String getTransationAmmount() {
        final int minValue = 50000;
        final int maxValue = 1050000;
        int transactionValue = ThreadLocalRandom.current().nextInt(minValue, maxValue + 1);
        return Integer.toString(transactionValue);
    }

    @NotNull
    public static String getTransactionValueWithTax(String transactionAmmount, String taxValue) {
        Double transactionCost = Double.parseDouble(transactionAmmount);
        Double taxValueStr = Double.parseDouble(taxValue);
        Double transactionCostWithTax = transactionCost + taxValueStr;
        return String.format("%.2f", transactionCostWithTax);
    }


    @NotNull
    public static String getValueAddedTax(String transactionAmmount) {
        Integer transactionAmmountValue = Integer.parseInt(transactionAmmount);
        double taxValue = 0.18;
        double valueAddedTax = transactionAmmountValue * taxValue;
        return String.format("%.2f", valueAddedTax);
    }

    // NOTE: For the test purposes, we're adding errors to some transactions
    // ... also, we're fixing the list of parameters that could change, the list is:
    // ...      cusotmerTaxNumber
    // ...      sellerTaxNumber
    // ...      transactionAmmountWithTax
    // ...      transactionAmmount

    public static void sendCopyOfTransaction(FileWriter transactionsWithErrorsFileWriter, TransactionRecord transaction) throws IOException {
        String transactionID = transaction.getID();
        String customerIndividualTaxNumber = transaction.getCustomerTaxNumber();
        String customerReasonCodeForRegistration = transaction.getCustomerReasonCodeForRegistration();
        String sellerIndividualTaxNumber = transaction.getSellerTaxNumber();
        String sellerResonCodeForRegistration = transaction.getSellerReasonCodeForRegistration();
        String valueAddedTax = transaction.getTaxValue();
        String transactionAmmountWithTax = transaction.getTransactionAmmountWithTax();
        String transactionAmmount = transaction.getTransactionAmmount();

        int MIN_VALUE = 1;
        int MAX_VALUE = 10;
        int indexOfChangingParameter = ThreadLocalRandom.current().nextInt(MIN_VALUE, MAX_VALUE + 1);
        if (indexOfChangingParameter == 2) {
            // putting error in transaction ammount with tax
            Double transactionValue = Double.parseDouble(transactionAmmountWithTax);
            Double randomCoefficient = ThreadLocalRandom.current().nextDouble((double)MIN_VALUE, (double)(MAX_VALUE + 1));
            transactionValue = transactionValue*randomCoefficient;
            transactionAmmountWithTax = String.valueOf(transactionValue);
        } else if (indexOfChangingParameter == 3) {
            // putting error in customer tax number
            customerIndividualTaxNumber = customerIndividualTaxNumber.replace('1', '6');
            customerIndividualTaxNumber = customerIndividualTaxNumber.replace('5', '9');
        } else if (indexOfChangingParameter == 4) {
            // putting error in seller tax number
            sellerIndividualTaxNumber = sellerIndividualTaxNumber.replace('6', '1');
            sellerIndividualTaxNumber = sellerIndividualTaxNumber.replace('2', '5');
            sellerIndividualTaxNumber = sellerIndividualTaxNumber.replace('0', '4');
        } else if (indexOfChangingParameter == 5) {
            // putting error in transaction ammount
            Double transactionValue = Double.parseDouble(transactionAmmount);
            Double randomCoefficient = ThreadLocalRandom.current().nextDouble((double)MIN_VALUE, (double)(MAX_VALUE + 1));
            transactionValue = transactionValue*randomCoefficient;
            transactionAmmount = String.valueOf(transactionValue);
        }

        CSVFileWriter.writeLine(transactionsWithErrorsFileWriter, Arrays.asList(transactionID, sellerIndividualTaxNumber, sellerResonCodeForRegistration,
                customerIndividualTaxNumber, customerReasonCodeForRegistration, transactionAmmount, valueAddedTax, transactionAmmountWithTax));



    }

    public static void createRecord(int ID, FileWriter transactionsFileWriter, FileWriter transactionsWithErrorsFileWriter) throws IOException {
        String transactionID = String.valueOf(ID);
        ArrayList regionNumbers = getRegionNumbers();

        String regionNumber = getRandomRegionNumber(regionNumbers);
        String taxInspectionNumber = getTaxInspectionNumber();
        String sellerAuthorityNumber = regionNumber + taxInspectionNumber;

        String sellerIndividualTaxNumber = generateIndividualTaxNumber(sellerAuthorityNumber);
        String sellerResonCodeForRegistration = getReasonCodeForRegistration(sellerAuthorityNumber);

        regionNumber = getRandomRegionNumber(regionNumbers);
        taxInspectionNumber = getTaxInspectionNumber();
        String customerAuthorityNumber = regionNumber + taxInspectionNumber;

        String customerIndividualTaxNumber = generateIndividualTaxNumber(customerAuthorityNumber);
        String customerReasonCodeForRegistration = getReasonCodeForRegistration(customerAuthorityNumber);

        String transactionAmmount = getTransationAmmount();

        String valueAddedTax = getValueAddedTax(transactionAmmount);

        String transactionAmmountWithTax = getTransactionValueWithTax(transactionAmmount, valueAddedTax);

        CSVFileWriter.writeLine(transactionsFileWriter, Arrays.asList(transactionID, sellerIndividualTaxNumber, sellerResonCodeForRegistration,
                customerIndividualTaxNumber, customerReasonCodeForRegistration, transactionAmmount, valueAddedTax, transactionAmmountWithTax));
        TransactionRecord transactionRecord = new TransactionRecord(transactionID, sellerIndividualTaxNumber, sellerResonCodeForRegistration, customerIndividualTaxNumber,
                                                    customerReasonCodeForRegistration, transactionAmmount, valueAddedTax, transactionAmmountWithTax);
        sendCopyOfTransaction(transactionsWithErrorsFileWriter, transactionRecord);

    }
    public static void main(String[] args) throws Exception {

    String transactionsFilePath = "/Users/Andrey/IdeaProjects/IndividualTaxNumberGenerator/resources/com/company/transactions.csv";
    String transactionsWithErrorsFilePath = "/Users/Andrey/IdeaProjects/IndividualTaxNumberGenerator/resources/com/company/transactionsWithErrors.csv";

    FileWriter transactionsFileWriter = new FileWriter(transactionsFilePath);
    FileWriter transactionsWithErrorsFileWriter = new FileWriter(transactionsWithErrorsFilePath);

    final int AMMOUNT_OF_TRANSACTIONS = 1000000;
    for (int i = 0; i < AMMOUNT_OF_TRANSACTIONS; ++i) {
        createRecord(i, transactionsFileWriter, transactionsWithErrorsFileWriter);
    }
        transactionsFileWriter.flush();
        transactionsFileWriter.close();;
        transactionsWithErrorsFileWriter.flush();;
        transactionsWithErrorsFileWriter.close();
    }
}
