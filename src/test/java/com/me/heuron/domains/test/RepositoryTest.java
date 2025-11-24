package com.me.heuron.domains.test;

import com.me.heuron.domains.patient.domain.Patient;
import com.me.heuron.domains.patient.domain.PatientImage;
import com.me.heuron.domains.patient.repository.PatientImageRepository;
import com.me.heuron.domains.patient.repository.PatientRepository;
import com.me.heuron.domains.test.config.JpaTestConfig;
import com.me.heuron.domains.test.config.TestConfig;
import com.me.heuron.domains.test.config.TestProfile;
import com.me.heuron.domains.test.setup.DatabaseCleaner;
import jakarta.persistence.EntityManager;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles(TestProfile.TEST)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestConfig.class, JpaTestConfig.class, PatientRepository.class, PatientImageRepository.class, Patient.class, PatientImage.class})
@Getter
public abstract class RepositoryTest {
	@Autowired
	private DatabaseCleaner databaseCleaner;

	@Autowired
	private EntityManager entityManager;
}
