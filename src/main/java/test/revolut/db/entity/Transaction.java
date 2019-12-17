package test.revolut.db.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "createdTime")
    private Date createdTime;
    @Column(name = "fromAccount")
    private Account fromAccount;
    @Column(name = "toAccount")
    private Account toAccount;
    @Column(name = "valid")
    private boolean valid;
    @Column(name = "status")
    private String status;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "clientInfo")
    private String clientInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
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
        this.status = status;
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

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", createdTime=" + createdTime +
                ", fromAccount=" + fromAccount +
                ", toAccount=" + toAccount +
                ", valid=" + valid +
                ", status='" + status + '\'' +
                ", amount=" + amount +
                ", remarks='" + remarks + '\'' +
                ", clientInfo='" + clientInfo + '\'' +
                '}';
    }
}
