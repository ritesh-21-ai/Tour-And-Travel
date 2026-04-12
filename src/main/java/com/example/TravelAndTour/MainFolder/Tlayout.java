package com.example.TravelAndTour.MainFolder;

public record Tlayout(Long id, String name, String description, int price, String message) {

    public Tlayout(String name, String description, int price, String message) {
        this(null, name, description, price, message);
    }
    
  public static Tlayout fromTravelPlan(TravelPlan travelPlan) {
    return new Tlayout(

        travelPlan.getPlanId(),
        travelPlan.getName(),
        travelPlan.getDescription(),
        travelPlan.getPrice(),
        travelPlan.getMessage()
    );
  }
   
  public static TravelPlan toTravelPlan(Tlayout tlayout) {
    TravelPlan travelPlan = new TravelPlan();
    travelPlan.setName(tlayout.name());
    travelPlan.setDescription(tlayout.description());
    travelPlan.setPrice(tlayout.price());
    travelPlan.setMessage(tlayout.message());
    return travelPlan;
  }

}
