package nmtasks.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TasksSvc{
	
	TasksSvc() { }
	
	
	@RequestMapping(value="servername", method=RequestMethod.GET)
	public ResponseEntity<String> convert(){
		String text = "DUCK";
		return new ResponseEntity<>(text, HttpStatus.OK);
	}
	
}