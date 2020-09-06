(ns contacts.db
  (:require [next.jdbc :as jdbc]
            [honeysql.core :as sql]
            [honeysql.helpers :as helpers]))

(defn get-all [{:keys [db]}]
  (println db)
  (jdbc/execute! db (sql/format {:select [:*]
                                 :from   [:contacts]})))

(defn get-by-id [{:keys [db] {:keys [id]} :path}]
  (jdbc/execute! db (sql/format {:select [:*]
                                 :from   [:contacts]
                                 :where  [:= :id id]})))

(defn create [{:keys [db] {:keys [first_name last_name email]} :body}]
  (let [sql (sql/format {:insert-into :contacts
                         :columns     [:first-name
                                       :last-name
                                       :email]
                         :values      [[first_name last_name email]]})]
    (jdbc/execute! db sql)))

(defn delete [parameters]
  (let [db (get parameters :db)
        id (get-in parameters [:path :id])]
    (jdbc/execute! db (sql/format {:delete-from :contacts
                                   :where       [:= :id id]}))))
(defn update [parameters]
  (let [db   (get parameters :db)
        id   (get-in parameters [:path :id])
        data (get parameters :body)
        data (assoc data :id id)]
    (println data)
    (jdbc/execute! db (sql/format {:update :contacts
                                   :set    data
                                   :where  [:= :id id]}))))
(comment
  (def db {:dbtype "h2" :dbname "clj_contacts"})
  (def ds (jdbc/get-datasource db))
  (def create-contacts-table "CREATE TABLE contacts
(id SERIAL PRIMARY KEY,
 first_name varchar(50),
 last_name varchar(50),
 email varchar(255),
 created_at TIMESTAMP NOT NULL DEFAULT current_timestamp)")
  (jdbc/execute! ds [create-contacts-table])
  (jdbc/execute! ds ["select * from contacts"])
  (jdbc/execute! ds ["DROP TABLE contacts"])
  (jdbc/execute! ds ["delete from contacts"])
  (def db2 {:dbtype "postgres" :dbname "clj_contacts" :user "postgres" :password "postgres"})
  (def ds (jdbc/get-datasource db2))
  (def create-contacts-table "CREATE TABLE contacts
(id SERIAL PRIMARY KEY,
 first_name TEXT,
 last_name TEXT,
 email TEXT,
 created_at TIMESTAMP NOT NULL DEFAULT current_timestamp)")
  ds
  (jdbc/execute! ds (sql/format {:delete-from :contacts :where [:= :id 3]}))
  (jdbc/execute! ds (sql/format {:update :contacts :set {} :where [:= :id 3]}))
  (jdbc/execute! ds (sql/format {:insert-into :contacts
                                 :columns     [:first-name
                                               :last-name
                                               :email]
                                 :values      [["Mary" "Poppins" "mary@poppins.com"]]}))
  (get-all)
  (helpers/insert-into :contacts)
  (helpers/columns :first-name :last-name :email)
  (helpers/values [["a" "b" "c"]])
  (helpers/sset {:a 1})
  )
(comment
  (sql/format {:select [:a :b :c] :from [:foo] :where [:and [:= :foo/a "baz"] [:= :foo/b "boz"]]})
  (sql/format {:create :a})

  (jdbc/execute! ds ["
insert into address(id, name, email)
values (1, 'Salmon', 'salmon@gmail.com')"])

  (jdbc/execute! ds ["select * from address"])

  )
