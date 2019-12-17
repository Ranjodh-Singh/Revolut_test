package test.revolut.resource;

import test.revolut.db.dao.AccountDAO;
import test.revolut.db.entity.Account;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("api")
public class RestEndPoint {
    public static int identity = 0;
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getAccounts() {
        Account account = new Account();
        account.setId(identity+1);
        account.setFirstName("Ranjodh_"+System.currentTimeMillis());
        account.setLastName("Singh");
        account.setEmail("ranjodh.87@gmail.com");
        AccountDAO accountDAO = new AccountDAO();
        accountDAO.saveAccount(account);
        System.out.println("Account saved : "+ account);
        List<String> result = accountDAO.getAccounts().stream().map(a -> a.getFirstName()).collect(Collectors.toList());
        StringBuffer resultS = new StringBuffer();
         for(String s:result){
             resultS.append(s +" \t ");
         }
        return resultS.toString();
    }
}
