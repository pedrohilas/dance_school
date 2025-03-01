package com.example.escoladanca.service;

import com.example.escoladanca.model.Aluno;
import com.example.escoladanca.model.AppUser;
import com.example.escoladanca.model.Professor;
import com.example.escoladanca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public boolean userexiste(String email) {
        return userRepository.existsByUsername(email);
    }
    public void saveUser(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ALUNO");
        userRepository.save(user);
    }
    public void saveUser(AppUser user, String role) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        userRepository.save(user);
    }


//converter prof para user
    public static AppUser convertProfessorToUser(Professor professor) {
        AppUser user = new AppUser();
        user.setUsername(professor.getEmail());
        user.setRole("PROFESSOR");
        return user;
    }
    //converter aluno para user

    public static AppUser convertAlunoToUser(Aluno alunos) {
        AppUser user = new AppUser();
        user.setUsername(alunos.getEmail());
        user.setRole("ALUNO");
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        return new UserDetailsImpl(user);
    }
}
