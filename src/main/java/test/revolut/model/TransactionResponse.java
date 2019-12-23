package test.revolut.model;

import java.math.BigDecimal;
import java.util.StringJoiner;

public class TransactionResponse {
    private int accountId;
    private String emailId;
    private BigDecimal currentAmount;
    private String currency;
    private String status;
    private String dateCreated;
    private String action;
    private BigDecimal transactionAmount;
    private String remarks;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TransactionResponse.class.getSimpleName() + "[", "]")
                .add("accountId=" + accountId)
                .add("emailId='" + emailId + "'")
                .add("currentAmount=" + currentAmount)
                .add("currency='" + currency + "'")
                .add("status='" + status + "'")
                .add("dateCreated='" + dateCreated + "'")
                .add("action='" + action + "'")
                .add("transactionAmount=" + transactionAmount)
                .add("remarks='" + remarks + "'")
                .toString();
    }
}
