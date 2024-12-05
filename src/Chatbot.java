import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Chatbot {

    // Arrays for menu options
    static String[] randomReplies = {
        "I'm not sure I understood that. Could you rephrase?",
        "Hmm, I didn’t catch that. Can you clarify?",
        "I’m still learning! Maybe try asking about our menu items?",
        "I can help with rice, beans, and protein. What would you like to know?",
        "Let me know if you’re interested in condiments, toppings, or sides!"
    };

    // Menu items stored as Food objects
    static Food[] menu = {
        // Rice options
        new Food("White Rice", 1.0, "mild"),
        new Food("Brown Rice", 1.0, "mild"),
        new Food("Cauliflower Rice", 2.0, "mild"),
        
        // Beans options
        new Food("Black Beans", 1.0, "mild"),
        new Food("Pinto Beans", 1.0, "mild"),
        
        // Protein options
        new Food("Chicken", 3.0, "mild"),
        new Food("Steak", 4.0, "medium"),
        new Food("Barbacoa", 4.0, "spicy"),
        new Food("Carnitas", 4.0, "mild"),
        new Food("Sofritas", 3.5, "spicy"),
        new Food("Chicken Al Pastor", 3.5, "medium")
    };

    // Order list
    static List<Food> order = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bot: Hello, welcome to Chipotle! My name is Chatpotle, and I will help you build your meal.");
        System.out.println("Bot: Would you prefer a bowl or a burrito?");
        
        boolean chatting = true;
        while (chatting) {
            System.out.print("User: ");
            String userInput = scanner.nextLine().toLowerCase();

            // Check for goodbye
            if (checkGoodbye(userInput)) {
                chatting = false;
                System.out.println("Bot: Goodbye! Enjoy your meal!");
            } else {
                String response = getResponse(userInput);
                System.out.println("Bot: " + response);
            }
        }

        scanner.close();
    }

    // Check if user wants to stop the conservation
    public static boolean checkGoodbye(String input) {
        String[] goodByeWords = {"bye", "goodbye", "exit"}; //if any of these words are mentioned, then the bot will end the conservation immeditaely
        for (String word : goodByeWords) {
            if (input.contains(word)) {
                return true;
            }
        }
        return false;
    }

    public static String getResponse(String statement) {  //Method to give the customer an order summary
        statement = statement.toLowerCase();
        String response = "";
    
        // Check if the user wants the order summary
        if (statement.contains("receipt") || statement.contains("summary") || statement.contains("total") || statement.contains("finalize")) {
            if (order.isEmpty()) {
                response = "You haven’t added anything to your order yet. Let me know what you’d like to start with!";
            } else {
                response = "Here’s your order summary: " + getOrderSummary() + "\nThank you for ordering with us! Your meal will be ready soon!";
                order.clear(); // Clears the past order for future customers
            }
            return response; // Exit immediately because the order summary has been requested
        }
    
        // Checks if the statement is asking for clarification about specefic food
        Food food = getFoodDetails(statement);
        if (food != null) {
            if (statement.contains("add") || statement.contains("i want") || statement.contains("include")) {
                order.add(food);
    
                // Handle transitions after adding an item
                if (food.getName().toLowerCase().contains("rice")) {
                    response = "Got it! What type of beans do you want? We have black beans and pinto beans. If you don’t want beans, type 'none'.";
                } else if (food.getName().toLowerCase().contains("beans")) {
                    response = "Ok awesome! Chipotle offers lots of protein options. We have chicken, steak, barbacoa, carnitas, sofritas, and chicken al pastor. If you don’t want protein, type 'none'.";
                } else if (food.getName().toLowerCase().contains("protein")) {
                    response = "Great choice! If you’re done, you can ask for a 'summary' or 'receipt' to see your total!";
                } else {
                    response = "I've added " + food.getName() + " to your order. What else would you like?";
                }
            } else {
                response = "The " + food.getName() + " is priced at $" + food.getPrice() + " and has a spice level of '" + food.getSpiceLevel() + "'. "
                         + "Would you like to add this to your order, or do you want to know about something else?";
            }
        } else if (statement.contains("burrito")) {
            response = "Great choice! Do you want white rice, brown rice, or cauliflower rice? If you don’t want rice, type 'none'.";
        } else if (statement.contains("bowl")) {
            response = "Yum! Do you want white rice, brown rice, or cauliflower rice? If you don’t want rice, type 'none'.";
        } else if (statement.contains("rice")) {
            if (statement.contains("white") || statement.contains("brown") || statement.contains("cauliflower") || statement.equals("none")) {
                response = "Got it! What type of beans do you want? We have black beans and pinto beans. If you don’t want beans, type 'none'.";
            } else {
                response = "I didn’t catch that. Do you want white rice, brown rice, or cauliflower rice? If you don’t want rice, type 'none'.";
            }
        } else if (statement.contains("beans")) {
            if (statement.contains("black") || statement.contains("pinto") || statement.equals("none")) {
                response = "Ok awesome! Chipotle offers lots of protein options. We have chicken, steak, barbacoa, carnitas, sofritas, and chicken al pastor. If you don’t want protein, type 'none'.";
            } else {
                response = "I didn’t catch that. Do you want black beans or pinto beans? If no beans, type 'none'.";
            }
        } else if (statement.contains("protein") || statement.contains("meat")) {
            if (statement.contains("chicken") || statement.contains("steak") || statement.contains("barbacoa") || statement.contains("carnitas") || statement.contains("sofritas") || statement.equals("none")) {
                response = "Great choice! If you’re done, you can ask for a 'summary' or 'receipt' to see your total!";
            } else {
                response = "I didn’t catch that. Our options are chicken, steak, barbacoa, carnitas, sofritas, and chicken al pastor.";
            }
        } else if (statement.equals("none")) {
            response = "Okay, moving on. What would you like to add next?";
        } else {
            response = getRandomResponse();
        }
    
        return response;
    }
    
    // Method to summarize the order
    public static String getOrderSummary() { 
        StringBuilder summary = new StringBuilder(); 
        double total = 0;
    
        // Build a summary of the order
        for (Food item : order) {
            summary.append(item.getName()).append(", ");
            total += item.getPrice();
        }
    
        // Remove comma
        if (summary.length() > 0) {
            summary.setLength(summary.length() - 2);
        }
    
        summary.append("\nTotal price: $").append(total);
        return summary.toString(); 
    }
    
    

    // Food details when asked about them
    public static Food getFoodDetails(String statement) {
        for (Food item : menu) {
            if (statement.contains(item.getName().toLowerCase())) {
                return item;
            }
        }
        return null; // Specific food was not found
    }

    // Random response
    public static String getRandomResponse() {
        Random rand = new Random();
        return randomReplies[rand.nextInt(randomReplies.length)];
    }

}
