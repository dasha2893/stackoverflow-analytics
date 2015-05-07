package selects

import java.sql.DriverManager

import org.olap4j.OlapConnection
import org.olap4j._
import org.olap4j.metadata.Member
import org.olap4j.metadata.Cube
import org.olap4j.metadata.NamedList

/**
 * Created by user on 07.05.2015.
 */
class ConnectToMondrian {
  Class.forName("mondrian.olap4j.MondrianOlap4jDriver")
  val olapConnection: OlapConnection = DriverManager.getConnection("jdbc:mondrian: "
    + "JdbcDrivers=org.postgresql.Driver; "
    + "Jdbc=jdbc:postgresql://localhost:5432/stackoverflow?user=postgres&password=123; "
    + "Catalog=C:\\Users\\user\\Desktop\\d\\cube-stackoverflow.xml; ").asInstanceOf[OlapConnection]

  val cubes: NamedList[Cube] = olapConnection.getOlapSchema.getCubes
  
  def getStatement() = {
    val olapStatement = olapConnection.createStatement
    olapStatement
  }

}
