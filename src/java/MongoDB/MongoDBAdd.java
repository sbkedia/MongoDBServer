
package MongoDB;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import java.util.Arrays;
import java.util.List;
import static MongoDB.MongoDBAdd.DBConnection;
import com.mongodb.client.MongoCollection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.Document;

import com.mongodb.client.MongoCursor;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This the web service for our application There are two kinds of Get Requests
 * - Login Authentication and Time Tracking screen of the algorithm There are
 * two kinds of Post Requests - Add User after registration and add tracking
 * information from the application
 * Source - http://mongodb.github.io/mongo-java-driver/3.4/driver/getting-started/quick-start/
 */
@WebServlet(name = "MongoDBAdd", urlPatterns = {"/MongoDBAdd/"})
public class MongoDBAdd extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
        System.out.println(request.getRequestURI());
        System.out.println(request.getPathInfo());
        System.out.println(request.getParameter("csvString"));
        // Get path info to invoke the appropriate method
//        String check = request.getPathInfo().toString().substring(1);
        String check = request.getParameter("csvString");
        List<String> StringParse = Arrays.asList(check.split(","));
        LogDetails ld = new LogDetails();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();

        // When time tracking - Get the appropriate subset of data from MongoDB
        if (StringParse.get(0).equals("FetchUser")) {
            String content = StringParse.get(1) + "," + StringParse.get(2);
            String str1 = StringParse.get(1);
            String str2 = StringParse.get(2);
            List<String> l1 = FetchUser(str1, str2);
            // Create log
            ld.setUserID(StringParse.get(3));
            ld.setModel(StringParse.get(4));
            ld.setManufacturer(StringParse.get(5));
            ld.setVersionRelease(StringParse.get(6));
            ld.setRequestType("Get");
            ld.setContent(content);
            ld.setTimestamp(dateFormat.format(date));
            ld.setServerMethodCalled("FetchUser");
            createLog(ld);
            PrintWriter out = response.getWriter();
            out.println(String.join(",", l1));// The resultant dataset                       
        }
        
        // When time tracking - Get the appropriate subset of data for the Manager from MongoDB
        if (StringParse.get(0).equals("FetchManager")) {
            String content = StringParse.get(1);
            String str1 = StringParse.get(1);
            List<String> l1 = FetchManager(str1);
            // Create log
            ld.setUserID(StringParse.get(3));
            ld.setModel(StringParse.get(4));
            ld.setManufacturer(StringParse.get(5));
            ld.setVersionRelease(StringParse.get(6));
            ld.setRequestType("Get");
            ld.setContent(content);
            ld.setTimestamp(dateFormat.format(date));
            ld.setServerMethodCalled("FetchManager");
            createLog(ld);
            PrintWriter out = response.getWriter();
            out.println(String.join(",", l1));// The resultant dataset                       
        }
                
        // for dashboard purpose
        if (check.equals("Operational")) {
            List<String> l3 = FetchAll();
            PrintWriter out = response.getWriter();
            out.println(l3.toString());
            System.out.println(l3.toString());
        }
        
        //for dashboard purpose
        if (check.equals("Log")) {
            List<String> l4 = FetchLog();
            PrintWriter out = response.getWriter();
            out.println(l4.toString());
            System.out.println(l4.toString());
        }

        //When loging in - Authenticating the user
        if (StringParse.get(0).equals("Login")) {
            System.out.println("In Login");
            String content = StringParse.get(1) + "," + StringParse.get(2);
            String str1 = StringParse.get(1);
            String str2 = StringParse.get(2);
            String result = Login(str1, str2);
            String role=LoginRole(str1);// Get user role

            // Create log
            ld.setUserID(StringParse.get(3));
            ld.setModel(StringParse.get(4));
            ld.setManufacturer(StringParse.get(5));
            ld.setVersionRelease(StringParse.get(6));
            ld.setRequestType("Get");
            ld.setContent(content);
            ld.setTimestamp(dateFormat.format(date));
            ld.setServerMethodCalled("Login");
            createLog(ld);
            
            //Send back to client
            PrintWriter out = response.getWriter();
            out.println(result+","+role);// Sending the appropriate message

        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("InPost");
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String csvString = br.readLine();
        if (csvString.equals("")) {
            // missing input return 401
            response.setStatus(401);
            return;
        }

        // prepare response code
        response.setStatus(200);

        UserInfo u = new UserInfo();
        LogDetails ld = new LogDetails();
        Track t = new Track();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        // Parsing the path info to invoke the appropriate method
        List<String> StringParse = Arrays.asList(csvString.split(","));

        // When a new user registers
        if (StringParse.get(0).equals("AddUser")) {
            String content = StringParse.get(1) + "," + StringParse.get(2) + "," + StringParse.get(3) + "," + StringParse.get(4) + "," + StringParse.get(5);
            // Parse to obtain First Name, Last Name, email, password, role
            u.setFName(StringParse.get(1));
            u.setLName(StringParse.get(2));
            u.setEmail(StringParse.get(3));
            u.setPassword(StringParse.get(4));
            u.setRole(StringParse.get(5));
            // Add to MongoDB
            AddUser(u);

            // Create log
            ld.setUserID(StringParse.get(6));
            ld.setModel(StringParse.get(7));
            ld.setManufacturer(StringParse.get(8));
            ld.setVersionRelease(StringParse.get(9));
            ld.setRequestType("Post");
            ld.setContent(content);
            ld.setTimestamp(dateFormat.format(date));
            ld.setServerMethodCalled("AddUser");
            createLog(ld);
        }
        // When a logged in user starts the time tracker
        if (StringParse.get(0).equals("AddTrack")) {
            String content = StringParse.get(1) + "," + StringParse.get(2) + "," + StringParse.get(3) + "," + StringParse.get(4);
            t.setuserID(StringParse.get(1));
            t.setTime(StringParse.get(2));
            t.setDate(StringParse.get(3));
            t.setMovement(StringParse.get(4));
            // Add information to time tracker
            AddTrack(t);

            // Create log
            ld.setUserID(StringParse.get(5));
            ld.setModel(StringParse.get(6));
            ld.setManufacturer(StringParse.get(7));
            ld.setVersionRelease(StringParse.get(8));
            ld.setRequestType("Post");
            ld.setContent(content);
            ld.setTimestamp(dateFormat.format(date));
            ld.setServerMethodCalled("AddTrack");
            createLog(ld);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

    // MongoDB connection
    public static MongoDatabase DBConnection() {
        //Connect to MongoDB server
        MongoCredential mc = MongoCredential.createCredential("shruti209", "ahn_mobile_app", "ahn12345".toCharArray());
        ServerAddress addr = new ServerAddress("ds249325.mlab.com", 49325);
        // Specify appropraite USI
        MongoClientURI uri = new MongoClientURI("mongodb://user:user@ds249325.mlab.com:49325/ahn_mobile_app");
        MongoClient client = new MongoClient(uri);
        MongoDatabase db = client.getDatabase("ahn_mobile_app");
        return db;
    }

    //Method to add user
    public void AddUser(UserInfo userInfo) {
        MongoDatabase db = DBConnection();
        //Appropriate collection accessed
        MongoCollection<Document> user_info = db.getCollection("user_info");
        Document doc = new Document()
                .append("First_Name", userInfo.getFName())
                .append("Last_Name", userInfo.getLName())
                .append("Email", userInfo.getEmail())
                .append("Password", userInfo.getPassword())
                .append("Role", userInfo.getRole());

        user_info.insertOne(doc);
    }

    //Method to add traking information
    public void AddTrack(Track track) {
        MongoDatabase db = DBConnection();
        //Appropriate collection accessed
        MongoCollection<Document> track_info = db.getCollection("track_info");
        Document doc = new Document()
                .append("User_ID", track.getuserID())
                .append("Date", track.getTime())
                .append("Time", track.getDate())
                .append("Movement", track.getMovement());

        track_info.insertOne(doc);
    }

    // Method to fetch appropriate user and date for time reporting
    public List<String> FetchUser(String str1, String str2) {

        MongoDatabase db = DBConnection();
        //Appropriate collection accessed
        MongoCollection<Document> collection = db.getCollection("track_info");
        List<Document> documents = new ArrayList<>();
        List<String> user_tracker = new ArrayList<String>();
        //Access the tracking information
        collection.find().into(documents);

        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            String temp1 = ((String) doc.get((String) "User_ID"));
            String temp2 = ((String) doc.get((String) "Date"));

            // Check for date and user ID
            if (temp1.equals(str1) && temp2.equals(str2)) {
                user_tracker.add((String) doc.get("Date"));
                user_tracker.add((String) doc.get("Time"));
                user_tracker.add((String) doc.get("Movement"));
            }
        }
        return user_tracker;
    }
    
     // Method to fetch appropriate user and date for time reporting
    public List<String> FetchManager(String str2) {

        MongoDatabase db = DBConnection();
        //Appropriate collection accessed
        MongoCollection<Document> collection = db.getCollection("track_info");
        List<Document> documents = new ArrayList<>();
        List<String> user_tracker = new ArrayList<String>();
        //Access the tracking information
        collection.find().into(documents);

        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
           String temp2 = ((String) doc.get((String) "Date"));

            // Check for date
            if (temp2.equals(str2)) {
                
                user_tracker.add((String) doc.get("User_ID"));
                user_tracker.add((String) doc.get("Date"));
                user_tracker.add((String) doc.get("Time"));
                user_tracker.add((String) doc.get("Movement"));
            }
        }
        return user_tracker;
    }
    

    //For user authentication
    public String Login(String str1, String str2) {

        MongoDatabase db = DBConnection();
        //Access user information
        MongoCollection<Document> collection = db.getCollection("user_info");
        List<Document> documents = new ArrayList<>();
        collection.find().into(documents);
        String pswd = null;
        String message = null;
        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            String temp1 = ((String) doc.get((String) "Email"));
            //Get the password if email exists
            if (temp1.equals(str1)) {
                pswd = ((String) doc.get((String) "Password"));
            }

        }
        // If no user ID exists
        if (pswd == null) {
            message = "Illegal User";
        } else {
            if (pswd.equals(str2)) {
                message = "Authenticated";// When userID exists

            } else {
                message = "Illegal User";// When user ID doesn't exist
            }
        }
        return message;
    }
    
        //For user authentication
    public String LoginRole(String str1) {

        MongoDatabase db = DBConnection();
        //Access user information
        MongoCollection<Document> collection = db.getCollection("user_info");
        List<Document> documents = new ArrayList<>();
        collection.find().into(documents);
        String role = null;
        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            String temp1 = ((String) doc.get((String) "Email"));
            System.out.println(temp1);
            //Get the role if email exists
            
            if (temp1.equals(str1)) {
                role = ((String) doc.get((String) "Role"));
                return role;
            }
            else{
               role=null; 
            }
        }
    return role;
    }
    
    
    
    //creater the server logs
    public void createLog(LogDetails log) {
        MongoDatabase db = DBConnection();
        //Appropriate collection accessed
        MongoCollection<Document> log_info = db.getCollection("log_info");
        Document doc = new Document()
                .append("UserID", log.getUserID())
                .append("Model", log.getModel())
                .append("Manufacturer", log.getManufacturer())
                .append("Version_Release", log.getVersionRelease())
                .append("Request_Type", log.getRequestType())
                .append("Content", log.getContent())
                .append("Timestamp", log.getTimestamp())
                .append("ServerMethodCalled", log.getServerMethodCalled());
        log_info.insertOne(doc);
    }

    //Query and get all info from mongoDB collection track_info
    public List<String> FetchAll() {

        MongoDatabase db = DBConnection();
        //Appropriate collection accessed
        MongoCollection<Document> collection = db.getCollection("track_info");
        List<Document> documents = new ArrayList<>();
        List<String> user_tracker = new ArrayList<String>();
        //Access the tracking information
        collection.find().into(documents);

        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            user_tracker.add((String) doc.get("User_ID"));
            user_tracker.add((String) doc.get("Date"));
            user_tracker.add((String) doc.get("Time"));
            user_tracker.add((String) doc.get("Movement"));

        }
        return user_tracker;
    }
    
    // Method to fetch the server log information
    public List<String> FetchLog() {

        MongoDatabase db = DBConnection();
        //Appropriate collection accessed
        MongoCollection<Document> collection = db.getCollection("log_info");
        List<Document> documents = new ArrayList<>();
        List<String> log_tracker = new ArrayList<String>();
        //Access the tracking information
        collection.find().into(documents);

        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            log_tracker.add((String) doc.get("UserID"));
            log_tracker.add((String) doc.get("Model"));
            log_tracker.add((String) doc.get("Manufacturer"));
            log_tracker.add((String) doc.get("Version_Release"));
            log_tracker.add((String) doc.get("Request_Type"));
            log_tracker.add((String) doc.get("Content"));
            log_tracker.add((String) doc.get("Timestamp"));
            log_tracker.add((String) doc.get("ServerMethodCalled"));
        }
        return log_tracker;
    }
    
    

}
