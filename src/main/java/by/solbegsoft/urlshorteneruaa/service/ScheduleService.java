package by.solbegsoft.urlshorteneruaa.service;

import by.solbegsoft.urlshorteneruaa.model.ActivateKey;
import by.solbegsoft.urlshorteneruaa.repository.ActivateKeyRepository;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ActivateKeyRepository activateKeyRepository;
    private final UserRepository userRepository;

    @Scheduled(initialDelay = 5000, fixedDelay = 60000*24*10)
    public void dropOldActivateKey(){
        log.info("Delete old activate key from database");
        List<ActivateKey> allActivateKey = activateKeyRepository.findAllByCreatedAtBefore(LocalDateTime.now().minusDays(10));
        log.info("{}", allActivateKey);
        for (ActivateKey activateKey : allActivateKey) {
            activateKeyRepository.deleteById(activateKey.getUuid());
            userRepository.deleteById(activateKey.getUser().getUuid());
        }
    }
}