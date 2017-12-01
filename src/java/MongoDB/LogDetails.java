package MongoDB;

/**
 * This is a class of Log Details - Used to record the server and application interaction
 * 
 */


public class LogDetails {
    private String userID, model, manufacturer, versionRelease, requestType, content, timestamp, serverMethodCalled;
    
    //Getter method for user id
    public String getUserID() {
        return userID;
    }
    
    // setter method for user id
    public void setUserID(String userID) {
        this.userID = userID;
    }
    
    //getter method for obtaining the model of the phone
    public String getModel() {
        return model;
    }
    
    //Setter method for obtaining the model of the phone
    public void setModel(String model) {
        this.model = model;
    }
    
    //getter method for obtaining the manufaturer of the phone
    public String getManufacturer() {
        return manufacturer;
    }
    
    //Setter method for obtaining the manufacturer of the phone
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    
    
    //getter method of Version Release
    public String getVersionRelease() {
        return versionRelease;
    }
    
    //setter method of version release
    public void setVersionRelease(String versionRelease) {
        this.versionRelease = versionRelease;
    }
    
    //getter method of request type
    public String getRequestType() {
        return requestType;
    }
    
    //setter method of request type
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
    
    //getter method of content
    public String getContent() {
        return content;
    }
    
    //setter method of content
    public void setContent(String content) {
        this.content = content;
    }
    
    //getter method of timestamp
    public String getTimestamp() {
        return timestamp;
    }
    
    //setter method of timestamp                
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    //getter method of server method called
    public String getServerMethodCalled() {
        return serverMethodCalled;
    }

    //Setter method of server method called
    public void setServerMethodCalled(String serverMethodCalled) {
        this.serverMethodCalled = serverMethodCalled;
    }
}
