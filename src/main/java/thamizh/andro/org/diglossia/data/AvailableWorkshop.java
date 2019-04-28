package thamizh.andro.org.diglossia.data;

public class AvailableWorkshop {

    //private variables
    String _workshopname;
    int _idtrk_branches;
    String _city;
    String _areaName;
    double _currentLat;
    double _currentLong;
    double _distance;
    String _type;

    // Empty constructor
    public AvailableWorkshop(){

    }
    // constructor
    public AvailableWorkshop(String workshopname, int idtrk_branches, String city, String areaName, double currentLat, double currentLong, double currentdistance, String type, int seats){
        this._workshopname = workshopname;
        this._idtrk_branches = idtrk_branches;
        this._city = city;
        this._areaName = areaName;
        this._currentLat = currentLat;
        this._currentLong = currentLong;
        this._distance = currentdistance;
        this._type = type;
    }
     
    
    // getting customer
    public String getDistance(){
        return this._workshopname;
    }
     
    // setting customer
    public void setDistance(double distance){
        this._distance = distance;
    }
    public void setType(String type){
        this._type = type;
    } 
    public String getCustomer(){
        return this._workshopname;
    }
     
    // setting customer
    public void setCustomer(String customer){
        this._workshopname = customer;
    }
     
 // getting branch
    public int getBranch(){
        return this._idtrk_branches;
    }
     
    // setting branch
    public void setBranch(int idtrk_branches){
        this._idtrk_branches = idtrk_branches;
    }
    
    
    // getting city
    public String getCity(){
        return this._city;
    }
    // getting city
    // setting seats
    // setting city
    public void setCity(String city){
        this._city = city;
    }
 // getting areaName
    public String getAreaName(){
        return this._areaName;
    }
     
    // setting areaName
    public void setAreaName(String areaName){
        this._areaName = areaName;
    }
    
 // getting currentLat
    public double getCurrentLat(){
        return this._currentLat;
    }
     
    // setting currentLat
    public void setCurrentLat(double currentLat){
        this._currentLat = currentLat;
    }
  
 // getting currentLong
    public double getCurrentLong(){
        return this._currentLong;
    }
     
    // setting currentLong
    public void setCurrentLong(double currentLong){
        this._currentLong = currentLong;
    }
    public String getType(){
        return this._type;
    }
   
}