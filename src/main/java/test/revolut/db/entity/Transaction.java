package test.revolut.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.StringJoiner;

@Entity
@Table(name = "transaction")
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "createdTime")
    private Timestamp createdTime;

    @ManyToOne(fetch = FetchType.EAGER)
    private Account toAccount;
    @Column(name = "valid")
    private boolean valid;
    @Column(name = "status")
    private String status;
    @Column(name = "amount")
    private BigDecimal amount;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Column(name = "currency")
    private String currency;

    @Column(name = "action")
    private String action;

    @Column(name = "balance")
    private BigDecimal balance;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "clientInfo")
    private String clientInfo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status.toLowerCase();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Transaction.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("createdTime=" + createdTime)
                .add("toAccount=" + toAccount)
                .add("valid=" + valid)
                .add("status='" + status + "'")
                .add("amount=" + amount)
                .add("currency='" + currency + "'")
                .add("action='" + action + "'")
                .add("balance=" + balance)
                .add("remarks='" + remarks + "'")
                .add("clientInfo='" + clientInfo + "'")
                .toString();
    }
}
