package Messages;

import Data.InformationReader;

import java.util.*;

public class MessagingSystem extends Observable{

    private final Observer AdminObserver;
    private final Observer RespondObserver;
    private final AdminMessageManager AdminManager;

    public MessagingSystem(InformationReader reader){
        this.AdminObserver = new AdminMessageObserver();
        this.RespondObserver = new RespondMessageObserver();
        this.AdminManager = new AdminMessageManager(reader.ReadMessage());
    }

    // Write messages created by regular user to file
    public void WriteMessage(Scanner x, String Username){
        System.out.println("Please write your Message to the admin below");
        String input = x.nextLine();
        Messages newMessage = new AdminMessage(Username, input);
        newMessage.AddObserver(this.AdminObserver);
        newMessage.notifyAllObservers(this.AdminManager);
        System.out.println("message sent!");
    }

    // Admin user respond only when the List of Messages are not empty
    public void ReadAndRespond(Scanner x){
        HashMap<String, List<String>> UsernameToMessage = ReadMessage();
        if(!UsernameToMessage.isEmpty()){
            respondToMessage(x, UsernameToMessage);
        }
    }

    // Admin Users Read Message
    public HashMap<String, List<String>> ReadMessage(){
        HashMap<String, List<String>> UsernameToMessage = new HashMap<>();

        for(AdminMessage admin: this.AdminManager.getAllAdminMessages()){
            if (UsernameToMessage.containsKey(admin.getUser())){
                UsernameToMessage.get(admin.getUser()).add(admin.getMessage()+":" + admin.getState());
            }
            else {
                List<String> newList = new ArrayList<>();
                newList.add(admin.getMessage() + ":" + admin.getState());
                UsernameToMessage.put(admin.getUser(), newList);
            }
        }
        System.out.println("Users with messages: " + UsernameToMessage.keySet());
        return UsernameToMessage;
    }

    // Admin User Respond to Message
    public void respondToMessage(Scanner x, HashMap<String, List<String>> UsernameToMessage) {
        while (true){
            System.out.println("Which person's message do you want to respond to? Please input username");
            String name = x.nextLine();
            if (UsernameToMessage.containsKey(name)){
                printLineByLine(UsernameToMessage.get(name));
                System.out.println("please input your response");
                String response = x.nextLine();
                StringBuilder respondTo = new StringBuilder("[");
                for(AdminMessage message: this.AdminManager.findAdmin(name)){
                    respondTo.append("/").append(message.getMessage());
                }
                respondTo.append("]");
                String nameAndResponse = name + ",Response to" + respondTo + ","+ response;
                Messages newMessage = new RespondMessage(this.AdminManager.findAdmin(name), nameAndResponse);
                newMessage.AddObserver(this.RespondObserver);
                newMessage.notifyAllObservers(this.AdminManager);
                System.out.println("message sent!");
                return;
            }

            else{
                System.out.println("invalid User Please try again!");
            }
        }
    }

    public void printLineByLine(List<String> messages){
        for(String message: messages){
            System.out.println("Message " + (messages.lastIndexOf(message) + 1) + ": " + message);
        }
    }


}
