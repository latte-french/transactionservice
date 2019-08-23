package model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class StatementModel {

    @Getter
    @Setter
    private String statementMessage = "";

    @Getter
    @Setter
    private ArrayList<String> statementObjects = new ArrayList<>();

    public StatementModel(String statementMessage, ArrayList<String> statementObjects){
        this.statementMessage = statementMessage;
        this.statementObjects = statementObjects;
    }

    public StatementModel(String statementMessage){
        this.statementMessage = statementMessage;
    }

    public StatementModel(){}

}
