package project.educatum.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.educatum.model.*;
import project.educatum.model.exceptions.InvalidArgumentsException;
import project.educatum.model.exceptions.PasswordsDoNotMatchException;
import project.educatum.model.exceptions.StudentNotFoundException;
import project.educatum.model.exceptions.UsernameAlreadyExistsException;
import project.educatum.repository.AdminRepository;
import project.educatum.repository.TeacherRepository;
import project.educatum.repository.StudentRepository;
import project.educatum.repository.InterestRepository;
import project.educatum.service.StudentService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentsRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final TeacherRepository teachersRepository;
    private final InterestRepository interestRepository;

    public StudentServiceImpl(StudentRepository studentsRepository, PasswordEncoder passwordEncoder, AdminRepository adminRepository, TeacherRepository teachersRepository, InterestRepository interestRepository) {
        this.studentsRepository = studentsRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
        this.teachersRepository = teachersRepository;
        this.interestRepository = interestRepository;
    }

    @Override
    public void register(String ime, String prezime, String email, String password, String repeatPassword, String telBroj, String opis) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty())
            throw new InvalidArgumentsException();
        if (!password.equals(repeatPassword)) throw new PasswordsDoNotMatchException();

        for (Teacher n : teachersRepository.findAll()) {
            if (n.getEmail().equals(email)) throw new UsernameAlreadyExistsException("Username already exists!");
        }
        for (Student u : studentsRepository.findAll()) {
            if (u.getEmail().equals(email)) throw new UsernameAlreadyExistsException("Username already exists!");
        }
        for (Admin a : adminRepository.findAll()) {
            if (a.getEmail().equals(email)) throw new UsernameAlreadyExistsException("Username already exists!");
        }
        Student user = new Student(opis, ime, prezime, email, passwordEncoder.encode(password), telBroj);
        studentsRepository.save(user);
    }

    @Override
    public List<Student> findAll() {
        return studentsRepository.findAll();
    }

    @Override
    public Student findByEmail(String email) {
        return studentsRepository.findByEmail(email);
    }

    @Override
    public List<Student> findAllByNameLike(String ime, List<Student> students) {
        List<Student> searchedStudents = studentsRepository.findAllByImeContainingIgnoreCase(ime);
        Set<Student> result = new HashSet<>();
        for (Student u : searchedStudents) {
            for (Student u2 : students)
                if (u.getId().equals(u2.getId())) result.add(u2);
        }
        return new ArrayList<>(result);
    }

    @Override
    public void delete(Integer id) {
        Student u = studentsRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        studentsRepository.delete(u);
    }

    @Override
    public Student findById(Integer id) {
        return studentsRepository.findById(id).orElseThrow(StudentNotFoundException::new);
    }

    @Override
    public List<Student> findAllByName(String ime) {
        return studentsRepository.findAllByImeContainingIgnoreCase(ime);
    }

    @Override
    public void interestedIn(Integer subjectId, Integer studentId){
        InterestID zId = new InterestID(subjectId,studentId);
        Interest z = new Interest(zId, LocalDate.now());
        interestRepository.save(z);
    }

    public void addListening(Integer studentID){

    }

}