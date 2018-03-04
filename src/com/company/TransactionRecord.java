package com.company;

public class TransactionRecord
{
    public String getID() {
        return ID;
    }

    private final String ID;

    public String getSellerTaxNumber() {
        return sellerTaxNumber;
    }

    private final String sellerTaxNumber;

    public String getSellerReasonCodeForRegistration() {
        return sellerReasonCodeForRegistration;
    }

    private final String sellerReasonCodeForRegistration;

    public String getCustomerTaxNumber() {
        return customerTaxNumber;
    }

    private final String customerTaxNumber;

    public String getCustomerReasonCodeForRegistration() {
        return customerReasonCodeForRegistration;
    }

    private final String customerReasonCodeForRegistration;

    public String getTransactionAmmount() {
        return transactionAmmount;
    }

    private final String transactionAmmount;

    public String getTaxValue() {
        return taxValue;
    }

    private final String taxValue;

    public String getTransactionAmmountWithTax() {
        return transactionAmmountWithTax;
    }

    private final String transactionAmmountWithTax;

    public TransactionRecord(String ID, String sellerTaxNumber, String sellerReasonCodeForRegistration, String customerTaxNumber,
                             String customerReasonCodeForRegistration, String transactionAmmount, String taxValue,
                             String transactionAmmountWithTax) {
        this.ID = ID;
        this.sellerTaxNumber = sellerTaxNumber;
        this.sellerReasonCodeForRegistration = sellerReasonCodeForRegistration;

        this.customerTaxNumber = customerTaxNumber;
        this.customerReasonCodeForRegistration = customerReasonCodeForRegistration;

        this.transactionAmmount = transactionAmmount;
        this.taxValue = taxValue;
        this.transactionAmmountWithTax = transactionAmmountWithTax;
    }
}