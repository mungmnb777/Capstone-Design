package graduation.mazerunner;

import graduation.mazerunner.constant.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@SpringBootApplication
public class MazerunnerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MazerunnerApplication.class, args);
	}
}
