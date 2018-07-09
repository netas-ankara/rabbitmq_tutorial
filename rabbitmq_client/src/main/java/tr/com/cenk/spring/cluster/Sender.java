package tr.com.cenk.spring.cluster;

import java.util.Random;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Sender {

	private static Logger logger = Logger.getLogger("Sender");
	@Autowired
	RabbitTemplate template;

	public static void main(String[] args) {
		SpringApplication.run(Sender.class, args);
	}

	@PostConstruct
	public void send() {
		for(int i = 0; i < 100000; i++){
			int id = new Random().nextInt(100000);
			template.convertAndSend(new Order(id, "TEST" + id, OrderType.values()[(id % 2)]));
		}
		logger.info("Sending completed.");
	}

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setUsername("admin");
		connectionFactory.setPassword("Admin@123");
		//connectionFactory.setAddresses("192.168.99.100:30000,192.168.99.100:30002,,192.168.99.100:30004");
		connectionFactory.setAddresses("localhost:5672");
		return connectionFactory;
	}

	@Bean
	public RabbitTemplate template() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setExchange("ex.example");
		return rabbitTemplate;
	}
}