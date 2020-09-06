(ns contacts.system
  (:require [contacts.core :as handler]
            [integrant.core :as ig]
            [next.jdbc :as jdbc]
            [org.httpkit.server :refer [run-server]]))

(def system-config
  {::server  {:handler (ig/ref ::handler)
              :port    4000}
   ::handler {:db (ig/ref ::db)}
   ::db      nil})

(defmethod ig/init-key ::server [server {:keys [handler port]}]
  (println "Server started on port" port)
  (run-server handler {:port port}))

(defmethod ig/init-key ::handler [handler {:keys [db]}]
  (println "Initialize" handler)
  (handler/create-app db))

(defmethod ig/init-key ::db [db _]
  (println "Initialize" db)
  (let [db {:dbtype "h2" :dbname "clj_contacts"}]
    (jdbc/get-datasource db)))

(defmethod ig/halt-key! ::server [_ server]
  (println "Server stopped")
  (server :timeout 100))

(defmethod ig/halt-key! ::handler [_ handler]
  (println "Halt" handler))

(defmethod ig/halt-key! ::db [_ db]
  (println "Halt" db))

(defn -main []
  (ig/init system-config))

(comment
  ;; TODO: Ver como tener una referencia a lo que seria el ::handler para poder hacer
  ;; (app {:request-method :post :uri "/api/contacts/"})
  (def system (ig/init system-config))
  (ig/halt! system)
  )
