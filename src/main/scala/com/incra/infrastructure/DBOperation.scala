package com.incra.infrastructure

import java.sql.{Connection, PreparedStatement, ResultSetMetaData}

import com.incra.infrastructure.UseResource._

/**
 * The DBOperation class is a proxy for data base connections and corresponding operations.  This is used
 * by the non-Slick-based services.
 *
 */
class DBOperation(val connectionFactory: DBConnectionFactory) extends Logging {

  /**
   * Run the sql query prepared by the specified prepareStatementBuilder. The result is
   * converted into a List of Map. Each element in the list is the returned row,
   * and the map contains the column-to-value mapping of each row.
   *
   * @param prepareStatementBuilder the specified prepareStatementBuilder
   * @return query result in a List of Map.
   */
  def executeQuery(query: String, prepareStatementBuilder: PreparedStatement => Unit): List[Map[String, Any]] = {
    logSQL(">>> executing query: " + query)
    checkNamespace(query)

    using(connectionFactory.getConnection)(_.close()) {
      connection => using(connection.prepareStatement(setNamespaceInSql(query)))(_.close()) {
        preparedStatement =>
          try {
            prepareStatementBuilder(preparedStatement)
            val resultSet = preparedStatement.executeQuery()
            val colNames = extractColumnNames(resultSet.getMetaData)

            new Iterator[Map[String, Any]] {
              def hasNext = resultSet.next()

              def next() = colNames.map(colName => (colName, resultSet.getObject(colName))).toMap
            }.toList
          } catch {
            case e: Exception =>
              rollback(connection)
              throw e
          }
      }
    }
  }

  /**
   * Run the specified sql query using a Statement.
   *
   * @param query the specified query
   * @return query result in a List of Map.
   */
  def executeQueryWithStatement(query: String): List[Map[String, Any]] = {
    logSQL(">>> executing query with statement: " + query)
    checkNamespace(query)

    using(connectionFactory.getConnection)(_.close()) {
      connection => using(connection.createStatement())(_.close()) {
        statement =>
          try {
            val resultSet = statement.executeQuery(setNamespaceInSql(query))
            val colNames = extractColumnNames(resultSet.getMetaData)

            new Iterator[Map[String, Any]] {
              def hasNext = resultSet.next()

              def next() = colNames.map(colName => (colName, resultSet.getObject(colName))).toMap
            }.toList
          } catch {
            case e: Exception =>
              rollback(connection)
              throw e
          }
      }
    }
  }

  /**
   * Execute the specified sql statement.
   *
   * @param sql the specified sql statement
   */
  def executeStatement(sql: String) {
    logSQL(">>> executing statement: " + sql)
    checkNamespace(sql)

    using(connectionFactory.getConnection)(_.close()) {
      connection => using(connection.createStatement())(_.close()) {
        statement =>
          try {
            statement.execute(setNamespaceInSql(sql))
          } catch {
            case e: Exception =>
              rollback(connection)
              throw e
          }
      }
    }
  }

  /**
   * Execute the specified insert sql. You have an opportunity to specify an option to pull the generated id
   * (pass Some(Statement.RETURN_GENERATED_KEYS) to statementOption).
   *
   * @param sql the specified insert sql
   * @return generated id or 0
   */
  def executeInsert(
    sql: String,
    prepareStatementBuilder: PreparedStatement => Unit,
    statementOption: Option[Int] = None): Long = {
    logSQL(">>> executing insert: " + sql)
    checkNamespace(sql)

    using(connectionFactory.getConnection)(_.close()) {
      connection =>
        val fullSQL = setNamespaceInSql(sql)
        val ps = statementOption match {
          case Some(opt) => connection.prepareStatement(fullSQL, opt)
          case None => connection.prepareStatement(fullSQL)
        }
        using(ps)(_.close()) {
          ps =>
            try {
              prepareStatementBuilder(ps)
              ps.executeUpdate()

              val rs = ps.getGeneratedKeys
              if (rs.next()) rs.getLong(1) else 0L

            } catch {
              case e: Exception =>
                rollback(connection)
                throw e
            }
      }
    }
  }

  /**
   * Execute the specified insert/update sql.
   *
   * @param sql the specified insert/update sql
   * @return number of inserted/updated rows
   */
  def executeUpdate(sql: String, prepareStatementBuilder: PreparedStatement => Unit): Int = {
    logSQL(">>> executing update: " + sql)
    checkNamespace(sql)

    using(connectionFactory.getConnection)(_.close()) {
      connection => using(connection.prepareStatement(setNamespaceInSql(sql)))(_.close()) {
        preparedStatement =>
          try {
            prepareStatementBuilder(preparedStatement)
            preparedStatement.executeUpdate()
          } catch {
            case e: Exception =>
              rollback(connection)
              throw e
          }
      }
    }
  }

  private def rollback(connection: Connection) = {
    try {
      log.info("rollback!")
      using(connection.createStatement())(_.close())(_.execute("rollback"))
    } catch {
      case e: Exception => log.info(s"Not rolling back: ${e.getMessage}")
    }
  }

  /**
   * Extract column names from the specified ResultSetMetaData.
   *
   * @param md the specified ResultSetMetaData
   * @return List of column names
   */
  private def extractColumnNames(md: ResultSetMetaData) =
    for (i <- 1 until md.getColumnCount + 1) yield md.getColumnName(i)

  private def setNamespaceInSql(sql: String) = sql

  private def checkNamespace(query: String) = {
    //if (namespace == "fake" && query.contains("[[NS]]"))
    //  throw new UnsupportedOperationException(
    //    "Unable to find the client substitution key. Are you using the correct DBOperation?")
    // this happens if the ClientSession was not set when DBOperation was created
  }
}