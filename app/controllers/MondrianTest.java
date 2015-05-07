package controllers;

/**
 * Created by user on 06.05.2015.
 */
import org.olap4j.*;
import org.olap4j.metadata.Member;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.NamedList;

import java.sql.*;

public class MondrianTest {

    private final static int COLUMNS = 0; //see Cellset javadoc
    private final static int ROWS= 1; //see Cellset javadoc

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("mondrian.olap4j.MondrianOlap4jDriver");
//        Class.forName("org.postgresql.Driver");

        OlapConnection olapConnection = (OlapConnection) DriverManager.getConnection(
                "jdbc:mondrian: " // Driver ident
                        + "JdbcDrivers=org.postgresql.Driver; " // Relational driver
                        + "Jdbc=jdbc:postgresql://localhost:5432/stackoverflow?user=postgres&password=123; " // Relational DB
                        + "Catalog=C:\\Users\\user\\Desktop\\d\\cube-stackoverflow.xml; "
        );

        NamedList<Cube> cubes = ((OlapConnection) olapConnection).getOlapSchema().getCubes();

        OlapStatement olapStatement = (OlapStatement) olapConnection.createStatement();

        String mdx = "SELECT\n" +
                "NON EMPTY {Hierarchize({[Id.Users-UserId].[All Users-UserIds]})} ON COLUMNS,\n" +
                "NON EMPTY Hierarchize(Union(CrossJoin({[Measures].[count_users]}, [Data.Date].[Year].Members)," +
                " CrossJoin({[Measures].[count_users]}, [Data.Date].[Month].Members))) ON ROWS\n" +
                "FROM [data_users]";

        CellSet cellSet = olapStatement.executeOlapQuery(mdx);
//
//        for (Position row : cellSet.getAxes().get(1)) {
//            for (Position column : cellSet.getAxes().get(0)) {
//                for (Member member : row.getMembers()) {
//                    System.out.println(member.getUniqueName());
//                }
//                for (Member member : column.getMembers()) {
//                    System.out.println(member.getUniqueName());
//                }
//                final Cell cell = cellSet.getCell(column, row);
//                System.out.println(cell.getFormattedValue());
//                System.out.println();
//            }
//        }
    }
}