package sw.laux.Studentrack.application.services.interfaces;

import de.daniilgutin.bank4you_ain.Business.DTO.ApiUserDto;
import de.daniilgutin.bank4you_ain.Business.DTO.TransactionDto;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.application.exceptions.StudentrackOperationNotAllowedException;
import sw.laux.Studentrack.persistence.entities.Student;
import sw.laux.Studentrack.persistence.entities.User;

public interface IBankingService {
    String registerUserForBankingService(User user) throws StudentrackOperationNotAllowedException, StudentrackObjectNotFoundException;
    String registerUserAtApi(ApiUserDto apiUserDto) throws StudentrackOperationNotAllowedException, StudentrackObjectNotFoundException;
    void payPremiumAccount(Student student) throws StudentrackObjectNotFoundException, StudentrackOperationNotAllowedException;
    void transferPremiumAccountMoney(TransactionDto transactionDto) throws StudentrackOperationNotAllowedException;
}
