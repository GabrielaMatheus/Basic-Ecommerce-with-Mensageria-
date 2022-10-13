package jms;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;

import org.apache.activemq.ActiveMQConnectionFactory;

import modelo.Pedido;


public class TesteConsumidorTopicoComercial {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		
		//Acessando o JNI do activeMQ
		InitialContext context = new InitialContext();
		
		ActiveMQConnectionFactory  factory = (ActiveMQConnectionFactory) context.lookup("ConnectionFactory"); //esse nome o seu MOM tem que falar na documentação.
		factory.setTrustAllPackages(true);
		
		//Estabelecendo a conexão
		Connection connection = factory.createConnection();
		connection.setClientID("comercial");
		connection.start();
		
		//acessando a queue
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE); //confirma o recebimento da mensagem, nesse caso, a confirmação é auto-confirmativa
		Topic topico = (Topic) context.lookup("loja"); //lugar que armazenará as mensagens enviadas, até que o consumidor as consuma
		MessageConsumer consumer = session.createDurableSubscriber(topico,"assinatura");
		
		//recebendo a mensagem
		consumer.setMessageListener(new MessageListener(){

		    @Override
	    public void onMessage(Message message){
		        ObjectMessage objectMessage  = (ObjectMessage) message;
		        try{
		        	Pedido pedido = (Pedido) objectMessage.getObject();
		            System.out.println(pedido.getCodigo());
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
