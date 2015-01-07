package com.unknown.httpcore;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpServerConnection;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpServerConnectionFactory;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.apache.http.protocol.UriHttpRequestHandlerMapper;


public class WebServer {

	private static final ExecutorService executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);
	
	public static void main(String[] args) throws Exception {
		
		HttpProcessor httpproc = HttpProcessorBuilder.create()
				 .add(new ResponseDate())
				 .add(new ResponseServer("MyServer-HTTP/1.1"))
				 .add(new ResponseContent())
				 .add(new ResponseConnControl())
				 .build();
		
		HttpRequestHandler service = new HttpRequestHandler() {
			
			@Override
			public void handle(HttpRequest request, HttpResponse response,
					HttpContext context) throws HttpException, IOException {
				System.out.println("service");
				response.setStatusCode(HttpStatus.SC_OK);
				StringEntity entity = new StringEntity(
						"<html><body>我是spring AOP</body></html>", ContentType.create(
								"text/html", "UTF-8"));
				response.setEntity(entity);
			}
		};
		HttpRequestHandler struts = new HttpRequestHandler() {
			
			@Override
			public void handle(HttpRequest request, HttpResponse response,HttpContext context) throws HttpException, IOException {
				System.out.println("struts");
				response.setStatusCode(HttpStatus.SC_OK);
				StringEntity entity = new StringEntity(
						"<html><body>我是struts</body></html>", ContentType.create(
								"text/html", "UTF-8"));
				response.setEntity(entity);
			}
		};
		
		HttpRequestHandler dHandler = new HttpRequestHandler() {
			@Override
			public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
				System.out.println("dHandler");
				response.setStatusCode(HttpStatus.SC_OK);
				StringEntity entity = new StringEntity(
						"<html><body>BB 请输入以下字符串(" + new Random().nextDouble()
								+ ")</body></html>", ContentType.create(
								"text/html", "UTF-8"));
				response.setEntity(entity);
			}
		};
		
		UriHttpRequestHandlerMapper handlerMapper = new UriHttpRequestHandlerMapper();
		handlerMapper.register("/service/*", service);
		handlerMapper.register("*.do", struts);
		handlerMapper.register("*", dHandler);
		
		HttpService httpService = new HttpService(httpproc, handlerMapper);

		ServerSocket listening = new ServerSocket(5888);
		while(true){
			Socket client = listening.accept();
			executors.execute(new WorkerThread(httpService, DefaultBHttpServerConnectionFactory.INSTANCE.createConnection(client)));
		}
	}
}
class WorkerThread extends Thread{
    private final HttpService service;
    private final HttpServerConnection conn;
    public WorkerThread(HttpService service, HttpServerConnection conn) {
        this.service = service;
        this.conn = conn;
    }
 
    @Override
    public void run() {
        HttpContext context = new BasicHttpContext();
        try {
            while (!Thread.interrupted() && conn.isOpen()) {
                this.service.handleRequest(this.conn, context);
            }
        } catch (IOException | HttpException e) {
            e.printStackTrace();
        }finally{
            try {
                this.conn.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
