package test.revolut.resource;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.revolut.db.entity.Account;
import test.revolut.db.entity.Transaction;
import test.revolut.exception.InvalidAccountException;
import test.revolut.exception.InvalidBalanceException;
import test.revolut.exception.InvalidBankTransaction;
import test.revolut.exception.UnsettledTransactionException;
import test.revolut.model.TransactionResponse;
import test.revolut.service.AccountServiceImpl;
import test.revolut.service.TransactionServiceImpl;
import test.revolut.util.BankConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("api")
public class RestEndPoint {

    static Logger logger = LoggerFactory.getLogger(RestEndPoint.class);
    //TODO:DI
    private TransactionServiceImpl transactionService = new TransactionServiceImpl();

    private AccountServiceImpl accountService = new AccountServiceImpl();
    //test method.
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getAccounts() {
        List<String> result = accountService.getAccounts().stream().map(a -> a.getFirstName()).collect(Collectors.toList());
        StringBuffer resultS = new StringBuffer();
         for(String s:result){
             resultS.append(s +" \t ");
         }
        return resultS.toString();
    }

    @POST
    @Path("/accounts")
    @Produces(MediaType.TEXT_PLAIN)
    public String createAccount(@QueryParam("firstname") String firstName, @QueryParam("lastname") String lastName, @QueryParam("email") String email){
        if(StringUtils.isEmpty(firstName) || StringUtils.isEmpty(lastName) || StringUtils.isEmpty(email)){
            return "Please provide all the information to create your account.ie. firstname,lastname and email";
        }
        Account account = accountService.createAccount(firstName,lastName,email);
        String response = "Your account has been created with account id: " + account.getId() + " . Please keep this with you for any future transactions.";
        return response;
    }



    @GET
    @Path("/accounts/{accountId}/balance")
    @Produces(MediaType.TEXT_PLAIN)
    public String getBalance(@PathParam("accountId") String accountId){

        String noTransString = "There is no transaction done on this account.";
        BigDecimal balance;
        try {
            Optional<Transaction> transaction = transactionService.getLatestTransactionFor(Integer.valueOf(accountId));
            if(transaction.isPresent()){
                logger.info("Transaction is present for the account :{}",accountId);
                balance = transaction.get().getBalance();
                String currency = transaction.get().getCurrency();
                String balanceString = "Balance in your account is : " + balance + " in " + currency + " .";
                logger.info(balanceString);
                return balanceString;
            }else{
                logger.info(noTransString);
                return noTransString;
            }
        }catch(InvalidAccountException exception){
            logger.error(BankConstants.LOG,exception);
            return exception.getMsg();
        }
        catch (Exception e){
            logger.error(BankConstants.LOG, e);
            String errResponse = "Unable to process your request. Please check the account details.";
            return errResponse;
        }
    }
    @POST
    @Path("/accounts/{accountId}/credit")
    @Produces(MediaType.TEXT_PLAIN)
    public String addMoney(@PathParam("accountId") String accountId, @QueryParam("amount") int amount, @QueryParam("currency") String currency){
String errorMsg;
        try {
            if(amount <= 0 || amount > Integer.MAX_VALUE){
                throw new InvalidBalanceException();
            }
            Account account = accountService.getAccount(Integer.valueOf(accountId));

            if(StringUtils.isEmpty(currency)){
                currency = BankConstants.DEFAULT_CURRENCY;
            }
            validateLastTransactionState(account);
            Optional<Transaction> transaction = transactionService.createTransaction(account,amount,currency,BankConstants.Action.CREDIT.name());
            if(transaction.isPresent()){
                return "your transaction is successful. Your updated balance is : "+ transaction.get().getBalance();
            }else{
                return "Error Processing your request. Please try again.";
            }

        } catch (InvalidAccountException e) {
            logger.error(BankConstants.LOG, e);
            errorMsg= e.getMsg();

        }catch(InvalidBalanceException e){
            logger.error(BankConstants.LOG, e);
            errorMsg = e.getMsg();
        }catch (Exception e){
            logger.error(BankConstants.LOG,e);
            errorMsg = "Internal Server Error";
        }
        resetTransactionStateInCaseOfFailure(accountId);
        return errorMsg;
    }



    @POST
    @Path("/accounts/{accountId}/debit")
    @Produces(MediaType.TEXT_PLAIN)
    public String debitMoney(@PathParam("accountId") String accountId, @QueryParam("amount") int amount, @QueryParam("currency") String currency){
            String errorMsg;
        try {
            if(amount <= 0){
                throw new InvalidBalanceException();
            }
            Account account = accountService.getAccount(Integer.valueOf(accountId));

            validateLastTransactionState(account);
            if(StringUtils.isEmpty(currency)){
                currency = BankConstants.DEFAULT_CURRENCY;
            }
            Optional<Transaction> transaction = transactionService.createTransaction(account,amount,currency,BankConstants.Action.DEBIT.name());
            if(transaction.isPresent()){
                return "your transaction is successful. Your updated balance is : "+ transaction.get().getBalance();
            }else{
                return "Error Processing your request. Please try again.";
            }

        }catch(InvalidBalanceException | InvalidBankTransaction | InvalidAccountException | UnsettledTransactionException e){
            logger.error(BankConstants.LOG, e);
            errorMsg= e.getMsg();
        }
        catch (Exception e){
            logger.error(BankConstants.LOG,e);
            errorMsg= "Internal Server Error";
        }
        resetTransactionStateInCaseOfFailure(accountId);
        return errorMsg;
    }

    private void resetTransactionStateInCaseOfFailure(String accountId) {
        try{

            Account account = accountService.getAccount(Integer.valueOf(accountId));
            account.setLatestTransactionState(null);
            accountService.updateAccount(account);
            logger.info(BankConstants.LOG,"Nullified last transaction status as it could be failed.");

        }catch(Exception e){
            logger.error(BankConstants.LOG,e);
        }
    }

    private void validateLastTransactionState(Account account) throws UnsettledTransactionException {
        String latestTransactionState = account.getLatestTransactionState();
        if(latestTransactionState !=null && !latestTransactionState.equalsIgnoreCase(BankConstants.Status.COMPLETED.name())){
            throw new UnsettledTransactionException();
        }
        account.setLatestTransactionState(BankConstants.Status.INITIATED.name());
        accountService.updateAccount(account);
        logger.info("Account status has been updated with INITIATED transaction state.");
    }

    @GET
    @Path("/accounts/{accountId}/statement")
    @Produces(MediaType.TEXT_PLAIN)
    public String getTransactions(@PathParam("accountId") String accountId){
        Gson gson = new Gson();
        try {
            List<test.revolut.db.entity.Transaction> transactions =  transactionService.getAllTransactionsFor(Integer.valueOf(accountId));
            List<TransactionResponse> resultList = new ArrayList<>();
            for (Transaction transaction: transactions){
                TransactionResponse response = new TransactionResponse();
                constructTransactionResponseObject(response,transaction);
                resultList.add(response);
            }
            return gson.toJson(resultList);
        }catch(InvalidAccountException exception){
            logger.error(BankConstants.LOG,exception);
            return null;
        }
        catch (Exception e){
            logger.error(BankConstants.LOG, e);
            return null;
        }
    }

    private TransactionResponse constructTransactionResponseObject(TransactionResponse response, Transaction transaction) {
        response.setAccountId(transaction.getToAccount().getId());
        response.setCurrency(transaction.getCurrency());
        response.setCurrentAmount(transaction.getBalance());
        response.setEmailId(transaction.getToAccount().getEmail());
        response.setStatus(transaction.getStatus());
        response.setTransactionAmount(transaction.getAmount());
        response.setAction(transaction.getAction());
        response.setDateCreated(transaction.getCreatedTime().toString());
        return response;
    }
}
