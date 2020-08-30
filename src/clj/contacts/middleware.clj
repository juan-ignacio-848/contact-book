(ns contacts.middleware)

(def db
  {:name    ::db
   :compile (fn [{:keys [db]} _]
              (fn [handler]
                (fn [req]
                  (handler (assoc-in req [:parameters :db] db)))))})
