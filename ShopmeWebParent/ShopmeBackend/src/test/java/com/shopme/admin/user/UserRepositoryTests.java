package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.user.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		System.out.println(roleAdmin);
		User userJim = new User("jim@example.com", "123505079", "jim", "chang");
		userJim.addRole(roleAdmin);
		
		User savedUser = userRepo.save(userJim);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userRavi = new User("ravi@example.com", "123505079", "ravi", "chen");

		Role roleEditor = entityManager.find(Role.class, 3);
		Role roleAssistant = entityManager.find(Role.class, 5);
		userRavi.addRole(roleEditor);
		userRavi.addRole(roleAssistant);
		
		User savedUser = userRepo.save(userRavi);
		assertThat(savedUser.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = userRepo.findAll();
		listUsers.forEach(user -> { 
		    System.out.println(user);
		});
	}
	
	@Test
	public void testFindUserById() {
		User theUser = userRepo.findById(2).get();
		System.out.println(theUser);
		assertThat(theUser.getId()).isEqualTo(2);
	
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userRavi = userRepo.findById(2).get();
		userRavi.setEnabled(true);
		userRavi.setEmail("ravi.chen@example.com");
		userRepo.save(userRavi);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userRavi = userRepo.findById(2).get();
		Role roleEditor = entityManager.find(Role.class, 3);
		Role roleSalesperson = entityManager.find(Role.class, 2);
		userRavi.getRoles().remove(roleEditor);
		userRavi.addRole(roleSalesperson);
		
		userRepo.save(userRavi);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		userRepo.deleteById(userId);
		userRepo.findById(userId);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "ravi@example.com";
		User user = userRepo.getUserByEmail(email);
		assertThat(user).isNotNull();
	}
	
}
