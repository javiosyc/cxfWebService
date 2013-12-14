package demo.order.client;

import demo.order.OrderProcess;
import demo.order.Order;

import org.apache.cxf.binding.soap.interceptor.SoapInterceptor;
import org.apache.cxf.frontend.ClientProxy;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class Client {

	private final static String SPRING_BEAN_SETTINGS = "demo/order/client/client-beans.xml";

	private final static String CLIENT_BEAN_NAME = "orderClient";

	public Client() {
	}

	public static void main(String args[]) throws Exception {

		String orderID = callOrderService();
		String message = (orderID == null) ? "Order not approved"
				: "Order approved; order ID is " + orderID;
		System.out.println(message);
	}

	public static String callOrderService() {

		OrderProcess client = genOrederClient();

		Order order = genOrder();

		return client.processOrder(order);
	}

	private static OrderProcess genOrederClient() {
		SoapInterceptor interceptor = generateOrderProcessClientHandler();
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				SPRING_BEAN_SETTINGS);
		OrderProcess client = (OrderProcess) context.getBean(CLIENT_BEAN_NAME);
		org.apache.cxf.endpoint.Client cxfClient = ClientProxy
				.getClient(client);
		cxfClient.getOutInterceptors().add(interceptor);

		return client;
	}

	private static Order genOrder() {
		Order order = new Order();
		order.setCustomerID("C001");
		order.setItemID("I001");
		order.setQty(100);
		order.setPrice(200.00);
		return order;
	}

	private static SoapInterceptor generateOrderProcessClientHandler() {

		OrderProcessClientHandler clientInterceptor = new OrderProcessClientHandler();
		clientInterceptor.setUserName("John");
		clientInterceptor.setPassword("password");

		return clientInterceptor;
	}
}
