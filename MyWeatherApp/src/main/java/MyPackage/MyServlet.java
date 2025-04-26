package MyPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		String userData = request.getParameter("userInput");
		
		//API Setup
		String apiKey = "5c3d5cb626c3eb89fcb38145b77f015f";
		
		//Get the city from the FORM
		String cityData = request.getParameter("city");
		
		//Creating the url for open weather api
		String Url = "https://api.openweathermap.org/data/2.5/weather?q="+ cityData + "&appid="+ apiKey +" ";
//		https://api.openweathermap.org/data/2.5/weather?q=New%20Delhi%20&appid=5c3d5cb626c3eb89fcb38145b77f015f
		
		
		
		//API Integration
		URL url = new URL(Url);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");		
		
		//Reading the data from the network
		InputStream is = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(is);
		
		//Want to store data in string
		StringBuilder responseContent = new StringBuilder();
		
		//Taking input from the reader, will create the scanner class object
		Scanner sc = new Scanner(reader);
		
		while(sc.hasNext()) {
			responseContent.append(sc.nextLine());
		}
		
		sc.close();
//		System.out.println(responseContent);
		
		
//		try {
		
		//Parse the json response to extract temperature and date & time
		Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);

		//System.out.println(jsonObject);
		
		//Date & Time
		long dateTimeStamp = jsonObject.get("dt").getAsLong() * 1000;
		String date = new Date(dateTimeStamp).toString();
		
		//Temperature
		double tempk = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int tempcel = (int) (tempk - 273.15);
		
		//Humidity
		int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
		
		//Wind speed
		double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
		
		//Weather condition
		String weatherCond = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		
		//Set the data as request attributes (to sending to JSP page )
		request.setAttribute("date", date);
		request.setAttribute("city", cityData);
		request.setAttribute("temprature", tempcel);
		request.setAttribute("weatherCond", weatherCond);
		request.setAttribute("humidity", humidity);
		request.setAttribute("windspeed", windSpeed);
		request.setAttribute("weatherData", responseContent.toString());
		
		connection.disconnect();
		
//		}catch(IOException e) {
//			e.printStackTrace();
//		}
		
		//Forward the request to the weather.jsp page to rendering
		request.getRequestDispatcher("index.jsp").forward(request, response);
		
		
	}

}
