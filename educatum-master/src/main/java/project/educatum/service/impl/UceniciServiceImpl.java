package project.educatum.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.educatum.model.*;
import project.educatum.model.exceptions.InvalidArgumentsException;
import project.educatum.model.exceptions.PasswordsDoNotMatchException;
import project.educatum.model.exceptions.StudentNotFoundException;
import project.educatum.model.exceptions.UsernameAlreadyExistsException;
import project.educatum.repository.AdminiJpa;
import project.educatum.repository.NastavniciJpa;
import project.educatum.repository.UceniciJpa;
import project.educatum.repository.ZainteresiranZaJpa;
import project.educatum.service.UceniciService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UceniciServiceImpl implements UceniciService {

    private final UceniciJpa uceniciRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminiJpa adminiRepository;
    private final NastavniciJpa nastavniciRepository;
    private final ZainteresiranZaJpa zainteresiranZaJpa;

    public UceniciServiceImpl(UceniciJpa uceniciRepository, PasswordEncoder passwordEncoder, AdminiJpa adminiRepository, NastavniciJpa nastavniciRepository, ZainteresiranZaJpa zainteresiranZaJpa) {
        this.uceniciRepository = uceniciRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminiRepository = adminiRepository;
        this.nastavniciRepository = nastavniciRepository;
        this.zainteresiranZaJpa = zainteresiranZaJpa;
    }

    @Override
    public void register(String ime, String prezime, String email, String password, String repeatPassword, String telBroj, String opis) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty())
            throw new InvalidArgumentsException();
        if (!password.equals(repeatPassword)) throw new PasswordsDoNotMatchException();

        for (Nastavnici n : nastavniciRepository.findAll()) {
            if (n.getEmail().equals(email)) throw new UsernameAlreadyExistsException("Username already exists!");
        }
        for (Ucenici u : uceniciRepository.findAll()) {
            if (u.getEmail().equals(email)) throw new UsernameAlreadyExistsException("Username already exists!");
        }
        for (Admini a : adminiRepository.findAll()) {
            if (a.getEmail().equals(email)) throw new UsernameAlreadyExistsException("Username already exists!");
        }
        Ucenici user = new Ucenici(opis, ime, prezime, email, passwordEncoder.encode(password), telBroj);
        uceniciRepository.save(user);
    }

    @Override
    public List<Ucenici> findAll() {
        return uceniciRepository.findAll();
    }

    @Override
    public Ucenici findByEmail(String email) {
        return uceniciRepository.findByEmail(email);
    }

    @Override
    public List<Ucenici> findAllByNameLike(String ime, List<Ucenici> ucenici) {
        List<Ucenici> searchedStudents = uceniciRepository.findAllByImeContainingIgnoreCase(ime);
        Set<Ucenici> result = new HashSet<>();
        for (Ucenici u : searchedStudents) {
            for (Ucenici u2 : ucenici)
                if (u.getId().equals(u2.getId())) result.add(u2);
        }
        return new ArrayList<>(result);
    }

    @Override
    public void delete(Integer id) {
        Ucenici u = uceniciRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        uceniciRepository.delete(u);
    }

    @Override
    public Ucenici findById(Integer id) {
        return uceniciRepository.findById(id).orElseThrow(StudentNotFoundException::new);
    }

    @Override
    public List<Ucenici> findAllByName(String ime) {
        return uceniciRepository.findAllByImeContainingIgnoreCase(ime);
    }

    @Override
    public void interestedIn(Integer subjectId, Integer studentId){
        ZainteresiranZaId zId = new ZainteresiranZaId(subjectId,studentId);
        ZainteresiranZa z = new ZainteresiranZa(zId, LocalDate.now());
        zainteresiranZaJpa.save(z);
    }

}
