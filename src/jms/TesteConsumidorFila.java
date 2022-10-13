package jms;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;


public class TesteConsumidorFila {
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
		MessageConsumer consumer = session.createConsumer(fila);
		
		//recebendo a mensagem
		consumer.setMessageListener(new MessageListener(){

		    @Override
		    public void onMessage(Message message){
		        TextMessage textMessage  = (TextMessage) message;
		        try{
		            System.out.println(textMessage.getText());
		        } catch(JMSException e){
		            e.printStackTrace();
		        }    
		    }

		});
		
		new Scanner(System.in).nextLine();
		
		session.close();
		connection.close();
		context.close();
	}

}
