package sw.laux.Studentrack.application.services;


import de.daniilgutin.bank4you_ain.Business.DTO.ApiUserDto;
import de.daniilgutin.bank4you_ain.Business.DTO.TransactionDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.application.exceptions.StudentrackOperationNotAllowedException;
import sw.laux.Studentrack.application.services.interfaces.IBankingService;
import sw.laux.Studentrack.persistence.entities.Student;
import sw.laux.Studentrack.persistence.entities.User;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.UUID;

@Service
@Primary
public class BankingServiceInternal implements IBankingService {
    @Autowired
    private Logger logger;

    @Override
    public String registerUserForBankingService(User user) throws StudentrackOperationNotAllowedException {
        var apiUserDto = new ApiUserDto(user.getMailAddress(), "DummyPasswordInPlainText", user.getUsername() + " " + user.getLastName());
        return registerUserAtApi(apiUserDto);
    }

    @Override
    public String registerUserAtApi(ApiUserDto apiUserDto) throws StudentrackOperationNotAllowedException {
        var calendar = new GregorianCalendar();
        var currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // It's Wednesday, my dudes, and on Wednesday, authentication fails
        if (currentDayOfWeek == 3) {
            throw new StudentrackOperationNotAllowedException(apiUserDto.getClass(), apiUserDto);
        }

        logger.info("Banking registration for " + apiUserDto.getName());
        return UUID.randomUUID().toString();
    }

    @Override
    public void payPremiumAccount(Student student) throws StudentrackObjectNotFoundException, StudentrackOperationNotAllowedException {
        var key = student.getBankingServiceApiKey();
        // Guaranteed for major and enrollment at Studentrack
        var faculty = student.getMajor().getFaculty();

        if (faculty.getDean() == null || faculty.getDean().getBankingServiceApiKey() == null) {
            throw new StudentrackObjectNotFoundException(TransactionDto.class, student);
        }

        var transaction = new TransactionDto(key, student.getMailAddress(), student.getFaculty().getDean().getMailAddress(), 1337);
        transferPremiumAccountMoney(transaction);
    }

    @Override
    public void transferPremiumAccountMoney(TransactionDto transactionDto) throws StudentrackOperationNotAllowedException {
        var success = new Random().nextBoolean();

        if (!success) {
            throw new StudentrackOperationNotAllowedException(TransactionDto.class, transactionDto);
        }
    }
}
