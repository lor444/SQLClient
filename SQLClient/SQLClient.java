package it.corsojava.jdbc;

import it.corsojava.jdbc.printer.ResultSetPrinter;
import it.corsojava.jdbc.printer.exceptions.ResultSetPrinterException;
import it.corsojava.ui.terminal.TerminalUi;

import java.sql.*;

public class SQLClient {

    public static void main(String[] args) {

        TerminalUi ui = TerminalUi.getBuilder().build();
        boolean goAhead=true;
        while(goAhead){

            ui.write("SQL> ");
            String sql = ui.read();
            if(sql.equalsIgnoreCase("quit")){
                break;
            }

            try(Connection cn = DriverManager.getConnection("jdbc:postgresql://localhost/corsosql","corsosql","corsosql")) {
                Statement st = cn.createStatement();
                boolean isResultSet = st.execute(sql);

                boolean printResults=true;
                while (printResults) {
                    int updateCount = st.getUpdateCount();
                    if(isResultSet){
                        ResultSet rs = st.getResultSet();
                        ui.writeln(ResultSetPrinter.printResultSet(rs));
                    }else{
                        ui.writeln("Righe modificate: " + updateCount);
                    }
                    isResultSet=st.getMoreResults();
                    if(!isResultSet && updateCount==-1)
                        printResults=false;
                }
            }catch(SQLException sqle){
                ui.writeln("Impossible eseguire il comando: "+sqle.getMessage());
            }catch(ResultSetPrinterException rse){
                rse.printStackTrace();
                goAhead=false;
            }
        }

    }
}
