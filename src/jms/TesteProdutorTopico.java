package jms;

import java.io.StringWriter;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.xml.bind.JAXB;

import modelo.Pedido;
import modelo.PedidoFactory;


public class TesteProdutorTopico {
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
		Destination topico = (Destination) context.lookup("loja"); //lugar que armazenará as mensagens enviadas, até que o consumidor as consuma
		
		MessageProducer producer = session.createProducer(topico);
		
		Pedido pedido = new PedidoFactory().geraPedidoComValores();
		
//		StringWriter writer = new StringWriter();
//		JAXB.marshal(pedido, writer);
//		String xml = writer.toString();
		//System.out.println(xml);
		
		Message message = session.createObjectMessage(pedido);
		
		//message.setBooleanProperty("ebook", false); //enviando pra testar o filtro no consumidor
		
		producer.send(message);
		
		
		
		session.close();
		connection.close();
		context.close();
	}

}
