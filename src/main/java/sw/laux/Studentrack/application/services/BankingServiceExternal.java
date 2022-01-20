package sw.laux.Studentrack.application.services;

import de.daniilgutin.bank4you_ain.Business.DTO.ApiUserDto;
import de.daniilgutin.bank4you_ain.Business.DTO.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sw.helblingd.terminportalbackend.repository.entity.dto.ScheduleDTO;
import sw.helblingd.terminportalbackend.repository.entity.persistent.Schedule;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.application.exceptions.StudentrackOperationNotAllowedException;
import sw.laux.Studentrack.application.services.interfaces.IBankingService;
import sw.laux.Studentrack.persistence.entities.Student;
import sw.laux.Studentrack.persistence.entities.User;
import org.springframework.http.HttpHeaders;

@Service
public class BankingServiceExternal implements IBankingService {
    @Autowired
    private RestTemplate restServiceClient;

    @Override
    public String registerUserForBankingService(User user) throws StudentrackObjectNotFoundException {
        var apiUserDto = new ApiUserDto(user.getMailAddress(), "password", user.getFirstName() + " " + user.getLastName());
        return registerUserAtApi(apiUserDto);
    }

    @Override
    public String registerUserAtApi(ApiUserDto apiUserDto) throws StudentrackObjectNotFoundException {
        var headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        var entity = new HttpEntity<>(apiUserDto, headers);

        var resultApiUserDto = restServiceClient.postForObject("http://localhost:7001/api/registerNewUser", entity, ApiUserDto.class);

        if (resultApiUserDto == null || apiUserDto.getApiKey() == null) {
            throw new StudentrackObjectNotFoundException(ApiUserDto.class, apiUserDto);
        }

        return apiUserDto.getApiKey();
    }

    @Override
    public void payPremiumAccount(Student student) throws StudentrackObjectNotFoundException, StudentrackOperationNotAllowedException {
        var key = student.getBankingServiceApiKey();
        // Guaranteed for major and enrollment at Studentrack
        var faculty = student.getFaculty();

        if (faculty.getDean() == null || faculty.getDean().getBankingServiceApiKey() == null) {
            throw new StudentrackObjectNotFoundException(TransactionDto.class, student);
        }

        var transaction = new TransactionDto(key, student.getMailAddress(), student.getFaculty().getDean().getMailAddress(), 1337);
        transferPremiumAccountMoney(transaction);
    }

    @Override
    public void transferPremiumAccountMoney(TransactionDto transactionDto) throws StudentrackOperationNotAllowedException {
        var headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        var entity = new HttpEntity<>(transactionDto, headers);
        var resultTransactionDto = restServiceClient.postForObject("http://localhost:7001/api/registerNewUser", entity, TransactionDto.class);
        // Throw exception for failure
        if (resultTransactionDto == null) {
            throw new StudentrackOperationNotAllowedException(TransactionDto.class, transactionDto);
        }
    }
}
