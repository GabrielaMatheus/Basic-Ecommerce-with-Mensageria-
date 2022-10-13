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
import javax.jms.Topic;
import javax.naming.InitialContext;


public class TesteConsumidorTopicoEstoqueSelector {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		
		//Acessando o JNI do activeMQ
		InitialContext context = new InitialContext();
		
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory"); //esse nome o seu MOM tem que falar na documenta��o.
		//Estabelecendo a conex�o
		Connection connection = factory.createConnection();
		connection.setClientID("estoque");
		connection.start();
		
		//acessando a queue
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE); //confirma o recebimento da mensagem, nesse caso, a confirma��o � auto-confirmativa
		Topic topico = (Topic) context.lookup("loja"); //lugar que armazenar� as mensagens enviadas, at� que o consumidor as consuma
		MessageConsumer consumer = session.createDurableSubscriber(topico,"assinatura-selector","ebook=false",false);
		
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
