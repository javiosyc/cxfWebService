package demo.order.client;

import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.endpoint.Client;

import java.lang.reflect.Method;

public class OrderProcessJaxWsDynamicSimpleClient {
	public OrderProcessJaxWsDynamicSimpleClient() {
	}

	private static final String WSDL_URL = "http://localhost:8070/orderapp/OrderProcess?wsdl";

	private static final String WS_INPUT_ORDER_CLASS = "demo.order.Order";

	public static void main(String str[]) throws Exception {

		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient(WSDL_URL);
		
		dynamicClientUsingLoadClass(client);

	}

	public static void dynamicClientUsingLoadClass(Client client) throws Exception {
		Object order = Thread.currentThread().getContextClassLoader()
				.loadClass(WS_INPUT_ORDER_CLASS).newInstance();

		Method m1 = order.getClass().getMethod("setCustomerID", String.class);
		Method m2 = order.getClass().getMethod("setItemID", String.class);
		Method m3 = order.getClass().getMethod("setQty", Integer.class);
		Method m4 = order.getClass().getMethod("setPrice", Double.class);

		m1.invoke(order, "C001");
		m2.invoke(order, "I001");
		m3.invoke(order, 100);
		m4.invoke(order, 200.00);

		Object[] response = client.invoke("processOrder", order);
		System.out.println("Response is " + response[0]);
	}
}
