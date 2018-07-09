package tr.com.cenk.java.partfour;

import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLogDirect {

	private static final String EXCHANGE_NAME = "direct_logs";

	public static void main(String[] argv) throws java.io.IOException, TimeoutException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		factory.setPassword("Admin@123");
		factory.setUsername("admin");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "direct");

		String severity = "error";
		String message = getMessage(argv);

		channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
		System.out.println(" [x] Sent '" + severity + "':'" + message + "'");

		channel.close();
		connection.close();
	}

	private static String getSeverity(String[] strings) {
		if(strings.length < 1) return "Hello World!";
		return joinStrings(strings, " ");
	}

	private static String getMessage(String[] strings) {
		if(strings.length < 1) return "Hello World!";
		return joinStrings(strings, " ");
	}

	private static String joinStrings(String[] strings, String delimiter) {
		int length = strings.length;
		if(length == 0) return "";
		StringBuilder words = new StringBuilder(strings[0]);
		for(int i = 1; i < length; i++){
			words.append(delimiter).append(strings[i]);
		}
		return words.toString();
	}
}