package nmtasks.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.Repository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import nmtasks.repositories.UserRepo;

@Service
public class AuthService implements UserDetailsService {
	@Autowired
	private UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String email)	throws UsernameNotFoundException {
		List<nmtasks.beans.User> users = userRepo.findByEmail(email);
		if (users.size() == 0)
			throw new UsernameNotFoundException("Could not find user with email '" + email + "'");
		else {
			nmtasks.beans.User user = users.get(0);
			GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
			UserDetails userDetails = (UserDetails)new User(user.getEmail(), 
					user.getPassword(), Arrays.asList(authority));
			return userDetails;
		}
	}
} 