package it.unibo.samplejavafx.cinema.repositories;

import it.unibo.samplejavafx.cinema.application.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {}
/*private static ClienteRepository instance;
// Classe generale di connessione al db a cui passare il tipo Client, da mettere nel costruttore
ORMHelper<Cliente> ormHelper;
// Classe di costruzione delle query, da mettere nel costruttore
QueryBuilder<Client, Object> queryBuilder;

public ClienteRepository() {
  this.ormHelper = new ORMHelper(Client.class);
  this.queryBuilder = this.ormHelper.getQueryBuilder();
}

private static synchronized ClienteRepository getInstance() {
  if (instance == null) instance = new ClienteRepository();
  return instance;
}

public boolean insert(final Client client) {
  return this.ormHelper.insert(client);
} // query per inserire cliente

public boolean insert(final List<Client> clients) {
  return this.ormHelper.insert(clients);
} // query per inserire una lista di clienti

public Client get(final int clientId) {
  try {
    // query per prendere cliente da id
    this.queryBuilder.reset();
    this.queryBuilder.where().eq(Client.ID_COLUMN_NAME, new SelectArg(clientId));
    final Client client = this.ormHelper.queryForFirst(this.queryBuilder.prepare());
    if (client != null) return client;
    return null;
  } catch (SQLException throwables) {
    throwables.printStackTrace();
    return null;
  }
}

public List<Client> getAll() {
  final List<Client> clients = this.ormHelper.queryForAll(); // query per prendere tutti i clienti
  if (!Validator.isListNullOrEmpty(clients)) return clients;
  return null;
}

public Client getLastInserted() {
  try {
    final Client client =
        this.ormHelper.queryForMax(
            Client.ID_COLUMN_NAME); // query per prendere l'ultimo cliente inserito
    if (client != null) return client;
    return null;
  } catch (SQLException throwables) {
    throwables.printStackTrace();
    return null;
  }
}

public boolean update(final Client client) {
  return this.ormHelper.update(client);
}

public boolean delete(final Client client) {
  return this.ormHelper.delete(client);
}

public boolean deleteAll() {
  return this.ormHelper.deleteAll(); // query per eliminare tutti i clienti
}*/
