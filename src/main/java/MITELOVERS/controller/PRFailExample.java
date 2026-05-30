package MITELOVERS.controller;

import java.io.IOException;

public class PRFailExample {

    public void runCheck(String userInput) throws IOException {
        Runtime.getRuntime().exec("echo " + userInput);
    }
}