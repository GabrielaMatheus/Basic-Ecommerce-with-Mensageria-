package jms;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;


public class TesteProdutorFila {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		
		//Acessando o JNI do activeMQ
		InitialContext context = new InitialContext();
		
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory"); //esse nome o seu MOM tem que falar na documentação.
		//Estabelecendo a conexão
		Connection connection = factory.createConnection();
		connection.start();
		
		//acessando a queue
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE); //confirma o recebimento da mensagem, nesse caso, a confirmação é auto-confirmativa
		Destination fila = (Destination) context.lookup("financeiro"); //lugar que armazenará as mensagens enviadas, até que o consumidor as consuma
		
		MessageProducer producer = session.createProducer(fila);
		
		Message message = session.createTextMessage("<pedido><id>123</id></pedido>");
		producer.send(message);
		
		
		new Scanner(System.in).nextLine();
		
		producer.close();
		session.close();
		connection.close();
		context.close();
	}

}
