package it.unibo.samplejavafx.cinema.db;



/**
 * Questa classe offre supporto alle operazioni CRUD su database.
 *
 * @param <T> Specifica il tipo, e quindi su quale tabella, di dato su cui operare.
 */
public class ORMHelper<T extends DatabaseObject> {

  /*private static ConnectionSource connectionSource = null;
   */
  /*private static AzureDbHelper databaseHelper = null;
  private static WeakReference<Context> contextWeakReference;*/
  /*
  private Class<T> clazz;
  private Dao<T, Object> dao;

  */
  /**
   * costruttore non pubblico in quanto le istanze vengono generate solo dalla classe ORMFactory.
   *
   * @param clazz Tipo generico di ORMHelper da creare.
   */
  /*
  public ORMHelper(final Class<T> clazz) {
      try {
          */
  /*if (databaseHelper == null) {
      databaseHelper = new AzureDbHelper(contextWeakReference.get());
  }*/
  /*
          if (connectionSource == null) {
              connectionSource = createAzureConnectionSource();
          }
          this.clazz = clazz;
          this.dao = DaoManager.createDao(connectionSource, clazz);
      } catch (Exception e) {
          System.out.print("ORMHelper.onCreate " + e.getMessage());
      }
  }

  // Aggiungi questo metodo per creare la connessione Azure
  public static ConnectionSource createAzureConnectionSource() throws SQLException {
      String connectionString = "jdbc:sqlserver://servercinema.database.windows.net:1433;database=DbCinema;user=pippo@servercinema;password=Alvaro1!;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
      return new JdbcConnectionSource(connectionString);
  }

  */
  /** Ritorna l'istanza corrente di AzureDbHelper usata internamente. */
  /*
   */
  /*public static AzureDbHelper getDatabaseHelper() {
      return databaseHelper;
  }*/
  /*

  */
  /** Imposta l'oggetto Context da usare per inizializzare la connessione al database. */
  /*
   */
  /*public static void setContext(final Context appContext) {
      contextWeakReference = new WeakReference<>(appContext);
  }*/
  /*

  */
  /**
   * Chiude la connessione al database.
   *
   * @throws SQLException in caso di errore chiusura di {@link ConnectionSource}
   */
  /*
  public void close() throws SQLException, IOException {
      if (connectionSource != null) {
          connectionSource.close();
          connectionSource = null;
      }*/
  /*
  if (databaseHelper != null) {
      databaseHelper.close();
      databaseHelper = null;
  }*/
  /*
  }

  */
  /**
   * Esegue le delete sulla tabella corrente per ogni oggetto della lista.
   *
   * @param databaseObjectsToDelete Lista di oggetti da cancellare.
   * @return Ritorna true se l'esecuzione e' andata a buon fine, false altrimenti.
   */
  /*
  public boolean delete(final List<T> databaseObjectsToDelete) {
      try {
          return this.dao.delete(databaseObjectsToDelete) >= 0;
      } catch (Exception e) {
          return false;
      }
  }

  */
  /**
   * Esegue la delete passata come parametro sulla tabella corrente.
   *
   * @param preparedDelete Statement da eseguire.
   * @return Ritorna true se l'esecuzione è andata a buon fine, false altrimenti.
   */
  /*
  public boolean delete(final PreparedDelete<T> preparedDelete) {
      try {
          return this.dao.delete(preparedDelete) >= 0;
      } catch (Exception e) {
          return false;
      }
  }

  */
  /**
   * Esegue una delete sulla tabella corrente di un oggetto specifico.
   *
   * @param databaseObjectToDelete Oggetto, e quindi record, da cancellare dalla tabella.
   * @return Ritorna true se l'esecuzione è andata a buon fine, false altrimenti.
   */
  /*
  public boolean delete(final T databaseObjectToDelete) {
      try {
          return this.dao.delete(databaseObjectToDelete) >= 0;
      } catch (Exception e) {
          return false;
      }
  }

  */
  /**
   * Cancella tutti i dati della tabella corrente.
   *
   * @return true se i dati sono stati cancellati, false altrimenti.
   */
  /*
  public boolean deleteAll() {
      try {
          TableUtils.clearTable(connectionSource, clazz);
          */
  /*TableUtils.dropTable(connectionSource, clazz, false);
  TableUtils.createTable(connectionSource, clazz);*/
  /*
          return true;
      } catch (Exception e) {
          return false;
      }
  }

  */
  /**
   * @return Ritorna la l'oggetto Class<T> in uso da questa istanza.
   */
  /*
  public Class<T> getDatabaseObjectClass() {
      return this.clazz;
  }

  */
  /**
   * Crea un oggetto DeleteBuilder per poter formulare delle clausole where per le operazioni di
   * delete.
   */
  /*
  public DeleteBuilder<T, Object> getDeleteBuilder() {
      return this.dao.deleteBuilder();
  }

  */
  /**
   * Crea un oggetto QueryBuilder per poter formulare delle clausole where per le operazioni di
   * select.
   */
  /*
  public QueryBuilder<T, Object> getQueryBuilder() {
      return this.dao.queryBuilder();
  }

  */
  /**
   * Ritorna il totale dei record per la query da eseguire sulla tabella corrente.
   *
   * @param preparedQuery Statement da eseguire.
   * @return Totale record presenti.
   */
  /*
  public Integer getRecordCount(final PreparedQuery<T> preparedQuery) {
      try {
          final String query = preparedQuery.getStatement().replaceFirst("\\*", "COUNT(\\*)");
          final GenericRawResults<String[]> results = this.dao.queryRaw(query);
          final String countAsString = results.getResults().get(0)[0];
          return Integer.parseInt(countAsString);
      } catch (Exception e) {
          return null;
      }
  }

  public Long count() {
      try {
          return dao.countOf();
      } catch (SQLException e) {
          return null;
      }
  }

  */
  /**
   * Crea un oggetto UpdateBuilder per poter formulare delle clausole where per le operazioni di
   * update.
   */
  /*
  public UpdateBuilder<T, Object> getUpdateBuilder() {
      return this.dao.updateBuilder();
  }

  */
  /**
   * Inserisce un oggetto (record) nella tabella corrente.
   *
   * @param dbObject Oggetto da inserire.
   * @return Ritorna true se l'esecuzione e' andata a buon fine, false altrimenti.
   */
  /*
  public boolean insert(final T dbObject) {
      try {
          // create ritorna il numero di righe aggiornate nel db
          return this.dao.create(dbObject) == 1;
      } catch (Exception e) {
          System.out.print("causa " + e.getCause());
          return false;
      }
  }

  */
  /**
   * Inserisce una collection di oggetti (record) nella tabella corrente.
   *
   * @param dbObjects Collection di oggetti da inserire.
   * @return Ritorna true se l'esecuzione è andata a buon fine, false altrimenti.
   */
  /*
  public boolean insert(final Collection<T> dbObjects) {
      int insertedRowCounter = 0;
      for (final T dbObject : dbObjects) {
          try {
              if (this.dao.create(dbObject) != 1) {
                  return false;
              } else {
                  insertedRowCounter++;
              }
          } catch (Exception e) {
              e.printStackTrace();
              System.out.print("causa " + e.getCause());
              return false;
          }
      }
      return insertedRowCounter == dbObjects.size();
  }

  */
  /**
   * Inserisce una lista di oggetti (record) nella tabella corrente.
   *
   * @param dbObjects Lista di oggetti da inserire.
   * @return Ritorna true se l'esecuzione è andata a buon fine, false altrimenti.
   */
  /*
  public boolean insert(final List<T> dbObjects) {
      int insertedRowCounter = 0;
      for (final T dbObject : dbObjects) {
          try {
              if (this.dao.create(dbObject) != 1) {
                  return false;
              } else {
                  insertedRowCounter++;
              }
          } catch (Exception e) {
              e.printStackTrace();
              System.out.print("causa "+ e.getCause());
              return false;
          }
      }
      return insertedRowCounter == dbObjects.size();
  }

  */
  /**
   * Esegue la query passata come parametro sulla tabella corrente.
   *
   * @param query Statement da eseguire.
   * @return Ritorna true se l'esecuzione è andata a buon fine, false altrimenti.
   */
  /*
  public List<T> query(final PreparedQuery<T> query) {
      try {
          return this.dao.query(query);
      } catch (SQLException e) {
          System.out.print("ORMHelper.query " + e.getMessage());
          return null;
      }
  }

  */
  /** Ritorna una lista con tutti i record della tabella corrente. */
  /*
  public List<T> queryForAll() {
      try {
          return this.dao.queryForAll();
      } catch (Exception e) {
          System.out.print("ORMHelper.queryForAll" + e.getMessage());
          return null;
      }
  }

  */
  /**
   * Ritorna il primo elemento del result-set generato dello statement da eseguire.
   *
   * @param query Statement da eseguire.
   * @return Primo elemento del result set o null se la query non ritorna risultati.
   */
  /*
  public T queryForFirst(final PreparedQuery<T> query) {
      try {
          return this.dao.queryForFirst(query);
      } catch (Exception e) {
          System.out.print("ORMHelper.queryForFirst " + e.getMessage());
          return null;
      }
  }

  */
  /**
   * Ritorna il primo elemento della tabella corrente ordinata in modo discendente sulla colonna
   * specificata.
   *
   * @param column Colonna per l'order-by.
   * @return Ritorna il primo oggetto trovato. Null se la tabella è vuota.
   * @throws SQLException in caso di errori SQL.
   */
  /*
  public T queryForMax(final String column) throws SQLException {
      final QueryBuilder<T, Object> queryBuilder = this.dao.queryBuilder();
      queryBuilder.orderBy(column, false);
      return this.dao.queryForFirst(queryBuilder.prepare());
  }

  */
  /**
   * Esegue un'update sulla tabella corrente.
   *
   * @param preparedUpdate Statement da eseguire.
   * @return Ritorna true se l'esecuzione è andata a buon fine, false altrimenti.
   */
  /*
  public boolean update(final PreparedUpdate<T> preparedUpdate) {
      try {
          return this.dao.update(preparedUpdate) >= 0;
      } catch (Exception e) {
          return false;
      }
  }

  */
  /**
   * Aggiorna un oggetto (record) già esistente nel database. Attenzione: non puo' cambiare il
   * valore della chiave primaria!
   *
   * @param dbObject Oggetto aggiornato.
   * @return Ritorna true se l'esecuzione è andata a buon fine, false altrimenti.
   */
  /*
  public boolean update(final T dbObject) {
      try {
          return this.dao.update(dbObject) >= 0;
      } catch (Exception e) {
          return false;
      }
  }

  public boolean tableExists() {
      try {
          return this.dao.isTableExists();
      } catch (SQLException e) {
          e.printStackTrace();
      }
      return false;
  }

  public boolean isTableEmpty() {
      try {
          final QueryBuilder<T, Object> queryBuilder = this.dao.queryBuilder();
          // Utilizzo questa query per verificare se nella tabella c'è qualcosa. Una
          // "SELECT *" semplice produce troppa latenza.
          queryBuilder.orderByRaw("ROWID").limit(1L);
          return this.dao.query(queryBuilder.prepare()).size() == 0;
      } catch (SQLException throwables) {
          throwables.printStackTrace();
      }
      return false;
  }

  public boolean createMe() {
      try {
          TableUtils.createTable(connectionSource, this.clazz);
          return true;
      } catch (SQLException e) {
          e.printStackTrace();
      }
      return false;
  }*/
}
