(ns contacts.routes
  (:require [contacts.handlers :as handlers]
            [clojure.spec.alpha :as s]))


(def email-regex #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$")
(s/def ::email-type (s/and string? #(re-matches email-regex %)))
(s/def :params/email ::email-type)
(s/def :params/first_name string?)
(s/def :params/last_name string?)
(s/def ::new-contact (s/keys :req-un [:params/email :params/first_name :params/last_name]))
(s/def ::updated-contact (s/keys :opt-un [:params/email :params/first_name :params/last_name]))

(re-matches email-regex "pato@gmail.pato.com")

(def ping ["/ping" {:get {:swagger {:tags ["test"]}
                          :summary "Health check"
                          :handler (fn [_]
                                     (println "db" (:db _))
                                     {:status 200
                                      :body   {:ping "pong"}})}}])

(def contacts ["/contacts" {:swagger {:tags ["contacts"]}}
               ["/" {:get  {:summary "Get all contacts"
                            :handler (fn [req]
                                       {:status 200
                                        :body   (handlers/get-all (:parameters req))})}
                     :post {:summary    "Create a contact"
                            :parameters {:body ::new-contact}
                            :handler    (fn [req]
                                          {:status 200
                                           :body   (handlers/create (:parameters req))})}}]
               ["/:id" {:get    {:summary    "Get contact by id"
                                 :parameters {:path {:id int?}}
                                 :handler    (fn [req]
                                               {:status 200
                                                :body   (handlers/get-by-id (:parameters req))})}
                        :delete {:summary    "Delete contact by id"
                                 :parameters {:path {:id int?}}
                                 :handler    (fn [req]
                                               {:status 200
                                                :body   (handlers/delete (:parameters req))})}
                        :put    {:summary    "Update a contact"
                                 :parameters {:path {:id int?}
                                              :body ::updated-contact}
                                 :handler    (fn [req]
                                               {:status 200
                                                :body   (handlers/update (:parameters req))})}}]])
