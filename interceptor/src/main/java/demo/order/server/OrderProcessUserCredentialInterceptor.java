package demo.order.server;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class OrderProcessUserCredentialInterceptor extends
		AbstractSoapInterceptor {

	private String userName;
	private String password;

	private final String CREDENTIALS_NAME = "OrderCredentials";

	public OrderProcessUserCredentialInterceptor() {
		super(Phase.PRE_INVOKE);
	}

	public void handleMessage(SoapMessage message) throws Fault {

		System.out
				.println("OrderProcessUserCredentialInterceptor handleMessage invoked");

		getCredentailInfo(message);

		System.out
				.println("userName reterived from SOAP Header is " + userName);
		System.out
				.println("password reterived from SOAP Header is " + password);

		// Perform dummy validation for John
		if ("John".equalsIgnoreCase(userName)
				&& "password".equalsIgnoreCase(password)) {
			System.out.println("Authentication successful for John");
		} else {
			throw new RuntimeException("Invalid user or password");
		}
	}

	private void getCredentailInfo(SoapMessage message) {
		
		QName qnameCredentials = new QName(CREDENTIALS_NAME);

		// Get header based on QNAME
		if (message.hasHeader(qnameCredentials)) {
			retrieveAuthInfo(message.getHeader(qnameCredentials));
		}
	}

	
	private void retrieveAuthInfo(Header header) {
		Element elementOrderCredential = (Element) header.getObject();
		Node nodeUser = elementOrderCredential.getFirstChild();
		Node nodePassword = elementOrderCredential.getLastChild();

		if (nodeUser != null) {
			userName = nodeUser.getTextContent();
		}
		if (nodePassword != null) {
			password = nodePassword.getTextContent();
		}
		
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
