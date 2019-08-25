package model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class StatementModel {

    @Getter
    @Setter
    private final String statementMessage;

    @Getter
    @Setter
    private final ArrayList<String> statementObjects;

    public StatementModel(String statementMessage, ArrayList<String> statementObjects){
        this.statementMessage = statementMessage;
        this.statementObjects = statementObjects;
    }

    public StatementModel(String statementMessage){
        this(statementMessage, new ArrayList<>());
    }

    public StatementModel(){ this("", new ArrayList<>());}

}
